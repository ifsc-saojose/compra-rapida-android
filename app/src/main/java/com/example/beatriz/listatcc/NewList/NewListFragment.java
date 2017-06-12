package com.example.beatriz.listatcc.NewList;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.Model.Lists;
import com.example.beatriz.listatcc.R;
import com.example.beatriz.listatcc.Util.Tab;
import com.example.beatriz.listatcc.Util.TabFragment;

import java.util.ArrayList;

public class NewListFragment extends TabFragment {

    private NewListItensFragment rif;
    private ListItensGottenFragment rif2;
    DatabaseHandler db;
    Lists newList;
    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newList = new Lists();

        db = new DatabaseHandler(getActivity());
        ArrayList<Tab> tabs = new ArrayList<>();
        rif = NewListItensFragment.newInstance();
        tabs.add(new Tab("Produtos", rif));
        rif2 = ListItensGottenFragment.newInstance();
        tabs.add(new Tab("Lista", rif2));

        setTabs(tabs);
    }

    @Override
    public void configureTabs(View view) {
        super.configureTabs(view);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getActivity().supportInvalidateOptionsMenu();
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        rif2.updateList();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actions, menu);
        menu.findItem(R.id.menu_save).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_save)
            putANameOnList();

        return super.onOptionsItemSelected(item);
    }

    public void putANameOnList() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View content = inflater.inflate(R.layout.dialog_change_purchase_name, null);
        builder.setView(content)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText listName = (EditText) content.findViewById(R.id.purchase_name);
                        if (!TextUtils.isEmpty(listName.getText().toString())) {
                            saveList(listName.getText().toString());
                            getFragmentManager().popBackStack();
                        } else {
                            putANameOnList();
                        }
                    }
                });
        builder.show();
    }

    public void saveList(String listName) {
        newList.setDate(System.currentTimeMillis());
        newList.setName(listName);
        newList.setProductItemList(rif2.productGottenItems);
        long listId = db.createList(newList);
        newList.setId(listId);

        for (ProductItem product : rif2.productGottenItems) {
            db.updateProductsFrequencyOnList(product.getId());
        }
    }
}
