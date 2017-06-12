package com.example.beatriz.listatcc.Lists;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.beatriz.listatcc.Database.Contract;
import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.MainActivity;
import com.example.beatriz.listatcc.Model.Item;
import com.example.beatriz.listatcc.Model.Lists;
import com.example.beatriz.listatcc.Product.ItemClickListener;
import com.example.beatriz.listatcc.Product.Section;
import com.example.beatriz.listatcc.Product.SectionedExpandableLayoutHelper;
import com.example.beatriz.listatcc.R;

import java.util.ArrayList;

public class ListDetailFragment extends Fragment implements ItemClickListener {

    Context mContext;
    DatabaseHandler db;
    protected View view;
    long listId = 0;
    ArrayList<Item> itemOnList;
    Lists listDetail;

    public static ListDetailFragment newInstance(long listId) {
        ListDetailFragment listDetailFragment = new ListDetailFragment();

        Bundle args = new Bundle();
        args.putLong("ID", listId);
        listDetailFragment.setArguments(args);

        return listDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        db = new DatabaseHandler(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listId = getArguments().getLong("ID");
        itemOnList = db.retrieveItensByListId(listId);
        listDetail = db.retrieveListById(listId);

        ((MainActivity) getActivity())
                .setActionBarTitle(listDetail.getName(), true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_recycler_view, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.product_view);
        SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(mContext,
                mRecyclerView, this, 3);

        sectionedExpandableLayoutHelper.addSection("Produtos", itemOnList);
        sectionedExpandableLayoutHelper.notifyDataSetChanged();
        //TODO DESENVOLVER TELA VAZIA
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                try {
                    getFragmentManager().popBackStack();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;
            case R.id.menu_edit_list_name:
                break;
            case R.id.menu_delete_list:
                deleteList();
                break;
            case R.id.menu_share_list:
                showShareDialog();
                break;
        }
        return false;
    }

    private void deleteList() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Tem certeza que deseja excluir a lista? ")
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues cv = new ContentValues();
                        cv.put(Contract.ListsTable.IS_VISIBLE, 0);
                        db.updateList(listId, cv);
                        getFragmentManager().popBackStack();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    // Call to update the share intent
    private void showShareDialog() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        String shareSubject = "Nome da lista";
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        String content = "Lista: " + listDetail.getName() + "\n\n" + "Produtos: \n";

        for (Item itens : itemOnList) {
            content += "\t\t" + itens.getName() + "\n";
        }
        intent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(Intent.createChooser(intent, "Itens da lista"));
    }

    @Override
    public void itemClicked(Item item) {
        Toast.makeText(mContext, "Item: " + item.getId() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClicked(Section section) {
        Toast.makeText(mContext, "Section: " + section.getName() + " clicked", Toast.LENGTH_SHORT).show();
    }
}
