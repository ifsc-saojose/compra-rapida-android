package com.example.beatriz.listatcc.Purchase;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beatriz.listatcc.Database.Contract;
import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.MainActivity;
import com.example.beatriz.listatcc.Model.Category;
import com.example.beatriz.listatcc.Model.Item;
import com.example.beatriz.listatcc.Model.Lists;
import com.example.beatriz.listatcc.Model.Purchase;
import com.example.beatriz.listatcc.Model.PurchaseItemStatus;
import com.example.beatriz.listatcc.NavigationManager.NavigationManager;
import com.example.beatriz.listatcc.Product.ItemClickListener;
import com.example.beatriz.listatcc.Product.Section;
import com.example.beatriz.listatcc.Product.SectionedExpandableLayoutHelper;
import com.example.beatriz.listatcc.R;
import com.example.beatriz.listatcc.Util.RelacionamentoEntreSecoes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PurchaseItensFragment extends Fragment implements ItemClickListener {

    public static final int OPT_GOT = 0;
    public static final int OPT_GETTING = 1;
    public static final int OPT_DONT_GET = 2;

    DatabaseHandler db;
    protected View view;
    ArrayList<Item> itemOnList;
    RecyclerView mRecyclerView;
    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    int mItensType;

    List<Integer> mSectionsOnCurrentPurchase = new ArrayList();
    LinkedHashMap<Long, ArrayList<Item>> relationCategoryProduct;
    List<Long> categoriesOrdered;

    long mListId;
    String mMarketId;
    Boolean isHistoryFragment;
    private long mFirstCategory;
    long mPurchaseId;
    Context mContext;
    List<Long> allCategoriesIdsOnList = new ArrayList<>();

    public static PurchaseItensFragment newInstance(long listId, int type, long purchaseId, String marketId, boolean isHistoryDetail, long firstCategory) {
        PurchaseItensFragment purchaseItensFragment = new PurchaseItensFragment();

        Bundle args = new Bundle();
        args.putLong("ID", listId);
        args.putInt("TYPE", type);
        args.putLong("PURCHASE_ID", purchaseId);
        args.putString("MARKET_ID", marketId);
        args.putBoolean("IS_HISTORY", isHistoryDetail);
        args.putLong("FIRST_CATEGORY", firstCategory);
        purchaseItensFragment.setArguments(args);

        return purchaseItensFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isHistoryFragment = getArguments().getBoolean("IS_HISTORY") == true ? true : false;

        if (isHistoryFragment)
            ((MainActivity) getActivity()).setActionBarTitle("Histórico", true);
        else
            ((MainActivity) getActivity()).setActionBarTitle("Compra em andamento", true);

        setHasOptionsMenu(true);

        mFirstCategory = getArguments().getLong("FIRST_CATEGORY");
        configureList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getArguments().containsKey("PURCHASE_ID")) {
            inflater.inflate(R.menu.menu_purchase, menu);
            menu.findItem(R.id.menu_edit_finalize_purchase).setVisible(true);
            menu.findItem(R.id.menu_change_name).setVisible(false);
            if (isHistoryFragment) {
                menu.findItem(R.id.menu_edit_finalize_purchase).setVisible(false);
                menu.findItem(R.id.menu_change_name).setVisible(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainActivity) getActivity())
                        .onBackPressed();
                return true;

            case R.id.menu_change_name:
                changePurchaseName();
                break;
            case R.id.menu_edit_finalize_purchase:
                calculateAndUpdateSectionRelations();
                break;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        db = new DatabaseHandler(getContext());
        mListId = getArguments().getLong("ID");
        mItensType = getArguments().getInt("TYPE");
        mPurchaseId = getArguments().getLong("PURCHASE_ID");
        mMarketId = getArguments().getString("MARKET_ID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.product_recycler_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.product_view);
        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(mContext,
                mRecyclerView, this, 3);
        //TODO DESENVOLVER TELA VAZIA
        if (itemOnList.size() > 0) {
            configSectionOrderOnList();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void changePurchaseName() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View content = inflater.inflate(R.layout.dialog_change_purchase_name, null);
        builder.setView(content)
                .setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText purchaseName = (EditText) content.findViewById(R.id.purchase_name);
                        Purchase purchase = new Purchase();
                        purchase.setName(purchaseName.getText().toString());
                        purchase.setId(mPurchaseId);
                        purchase.setActive(1);
                        purchase.setFromList(mListId);
                        db.updatePurchase(purchase);
                        dialog.dismiss();
                        getParentFragment().getFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void configureList() {

        itemOnList = db.retrievePurchaseItensByStatus(mPurchaseId, mItensType);

        if (itemOnList.size() > 0) {
            relationCategoryProduct = separateItensIntoCategories(itemOnList);

            for (Map.Entry<Long, ArrayList<Item>> tmp : relationCategoryProduct.entrySet()) {
                allCategoriesIdsOnList.add(tmp.getKey());
            }

            categoriesOrdered = new ArrayList<>();
            if ((mItensType == 0 || mItensType == 2) || isHistoryFragment) {
                mFirstCategory = allCategoriesIdsOnList.get(0);
            }

            categoriesOrdered.add(mFirstCategory);
            allCategoriesIdsOnList.remove(mFirstCategory);
            long referenceCategory = mFirstCategory;
            for (int i = 0; i < (relationCategoryProduct.size() - 1); i++) {
                long nextCategory = nextReference(mMarketId, referenceCategory, allCategoriesIdsOnList);

                if (nextCategory == 999) {
                    nextCategory = allCategoriesIdsOnList.get(0);
                }
                categoriesOrdered.add(nextCategory);
                allCategoriesIdsOnList.remove(nextCategory);
                referenceCategory = nextCategory;
            }
        }
    }


    private long nextReference(String mMarketId, long categoryReferenceId, List<Long> categories) {
        String includedCategories = TextUtils.join(",", categories);
        //Seleciona do banco de dados a categoria  que está mais próxima a categoriaReferencia, tendo como base a menor média das ordens
        long categoriesInOrder = db.retrieveNextCategory(mMarketId, categoryReferenceId, includedCategories);
        return categoriesInOrder;
    }

    /**
     * Inseri os produtos em suas respectivas categorias, categorização dos produtos.
     *
     * @param itemOnList
     * @return
     */
    private LinkedHashMap<Long, ArrayList<Item>> separateItensIntoCategories(ArrayList<Item> itemOnList) {
        List<Long> mCategoryListIdOnPurchase = new ArrayList<>();
        LinkedHashMap<Long, ArrayList<Item>> relationCategoryProduct = new LinkedHashMap<>();

        int i = 0;

        while (i < itemOnList.size()) {
            Long category = itemOnList.get(i).getCategoryId();

            if (!mCategoryListIdOnPurchase.contains(category)) {
                mCategoryListIdOnPurchase.add(category);

                ArrayList<Item> itemList = new ArrayList<>();
                itemList.add(itemOnList.get(i));
                relationCategoryProduct.put(itemOnList.get(i).getCategoryId(), itemList);
            } else {
                relationCategoryProduct.get(itemOnList.get(i).getCategoryId()).add(itemOnList.get(i));
            }
            i++;
        }

        return relationCategoryProduct;
    }

    public void configSectionOrderOnList() {

        for (long sectionIds : categoriesOrdered) {
            Category category = db.retrieveCategoryById(sectionIds);
            sectionedExpandableLayoutHelper.addSection(category.getName(), relationCategoryProduct.get(sectionIds));
        }
        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }

    private void calculateAndUpdateSectionRelations() {

        createPurchaseContent();

        int secaoReferencia;
        int secaoRelacao;
        for (int i = 0; i < mSectionsOnCurrentPurchase.size(); i++) {
            secaoReferencia = mSectionsOnCurrentPurchase.get(i);
            int x = i + 1;

            while (x < mSectionsOnCurrentPurchase.size()) {
                secaoRelacao = mSectionsOnCurrentPurchase.get(x);
                Log.i("DEBUG ", "----------------");
                Log.i("DEBUG ", "Inicio do Debug");
                Log.i("DEBUG ", "Relação entre: " + secaoReferencia + " e " + secaoRelacao);
                float distanciaEntreSecoes = Math.abs(x - i);
                Log.i("DEBUG ", "Distancia entre as seções: " + distanciaEntreSecoes);
                RelacionamentoEntreSecoes relacionamentoEntreSecoes = db.retrieveParRelationCategories(mMarketId, secaoReferencia, secaoRelacao);
                RelacionamentoEntreSecoes relacionamentoEntreSecoesReverso = db.retrieveParRelationCategories(mMarketId, secaoRelacao, secaoReferencia);
                if (relacionamentoEntreSecoes != null) {
                    double oldMediaDasOrdens = relacionamentoEntreSecoes.getMediaDasOrdens();
                    int totalDeCompras = relacionamentoEntreSecoes.getPurchaseTotal();
                    int id = relacionamentoEntreSecoes.getId();
                    double mediaNovaNumerador = ((totalDeCompras * oldMediaDasOrdens) + distanciaEntreSecoes);
                    double mediaNovaDenominador = totalDeCompras + 1;
                    double mediaNova = mediaNovaNumerador / mediaNovaDenominador;
                    int totalDeCompasAtual = totalDeCompras + 1;
                    Log.i("DEBUG ", "Relação EXISTENTE, atualizando valores");
                    Log.i("DEBUG ", "Media das ordens Antigas: \t" + oldMediaDasOrdens);
                    Log.i("DEBUG ", "total de compras: \t" + totalDeCompras);
                    Log.i("DEBUG ", "id do relacionamento:: \t" + id);
                    Log.i("DEBUG ", "Media nova: \t" + mediaNova);
                    Log.i("DEBUG ", "Total de Compras: \t" + totalDeCompasAtual);
                    //update coluna media das ordens e numero de compras
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.RelacionamentoEntreSecoes.MEDIA_DAS_ORDENS, mediaNova);
                    contentValues.put(Contract.RelacionamentoEntreSecoes.PURCHASE_TOTAL, totalDeCompasAtual);
                    db.updateRelacionamentoEntreSecoes(id, contentValues);

                    //update coluna media das ordens e numero de compras
                    ContentValues contentValuesReverso = new ContentValues();
                    contentValuesReverso.put(Contract.RelacionamentoEntreSecoes.MEDIA_DAS_ORDENS, mediaNova);
                    contentValuesReverso.put(Contract.RelacionamentoEntreSecoes.PURCHASE_TOTAL, totalDeCompasAtual);
                    db.updateRelacionamentoEntreSecoes(relacionamentoEntreSecoesReverso.getId(), contentValuesReverso);

                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_1, secaoReferencia);
                    contentValues.put(Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_2, secaoRelacao);
                    contentValues.put(Contract.RelacionamentoEntreSecoes.ID_MERCADO, mMarketId);
                    contentValues.put(Contract.RelacionamentoEntreSecoes.MEDIA_DAS_ORDENS, distanciaEntreSecoes);
                    contentValues.put(Contract.RelacionamentoEntreSecoes.PURCHASE_TOTAL, 1);
                    db.createSessionRelations(contentValues);

                    ContentValues contentValuesReverso = new ContentValues();
                    contentValuesReverso.put(Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_1, secaoRelacao);
                    contentValuesReverso.put(Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_2, secaoReferencia);
                    contentValuesReverso.put(Contract.RelacionamentoEntreSecoes.ID_MERCADO, mMarketId);
                    contentValuesReverso.put(Contract.RelacionamentoEntreSecoes.MEDIA_DAS_ORDENS, distanciaEntreSecoes);
                    contentValuesReverso.put(Contract.RelacionamentoEntreSecoes.PURCHASE_TOTAL, 1);
                    db.createSessionRelations(contentValuesReverso);

                }
                Log.i("DEBUG ", "FIM da Interação");
                Log.i("DEBUG ", "*****************");
                Log.i("DEBUG ", " ");
                x++;
            }
        }

        NavigationManager.openHome(getActivity().getSupportFragmentManager());
    }

    private void createPurchaseContent() {

        Lists list = db.retrieveListById(mListId);
        ContentValues contentValuesReverso = new ContentValues();

        contentValuesReverso.put(Contract.PurchaseTable.PURCHASE_NAME, list.getName());
        contentValuesReverso.put(Contract.PurchaseTable.LIST_ID, mListId);
        contentValuesReverso.put(Contract.PurchaseTable.MARKET_ID, mMarketId);
        contentValuesReverso.put(Contract.PurchaseTable.ACTIVE, 1);
        contentValuesReverso.put(Contract.PurchaseTable.DATE, System.currentTimeMillis());
        db.updatePurchase(mPurchaseId, contentValuesReverso);
    }

    public void changeProductStatusDialog(final Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Escolha a opção: ")
                .setMessage("Pegou o item? ")
                .setPositiveButton("Sim!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO inserir produto na aba de itens pegos
                        updatePurchaseProductStatus(item, PurchaseItensFragment.OPT_GOT);
                        //Inseri na tabela ordem_secao a ordem em que o item foi inserido no carrinho
                        extractSectionOrder(item);
                        Toast.makeText(mContext, "Item inserido no carrinho com sucesso", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Nao!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO  inserir produto na aba de itens não pegos
                        updatePurchaseProductStatus(item, PurchaseItensFragment.OPT_DONT_GET);
                        //Inseri na tabela ordem_secao a ordem em que o item foi inserido no carrinho
                        extractSectionOrder(item);
                        Toast.makeText(mContext, "Item não pego", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Cancelar!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //// TODO apenas fecha o dialog
                        Toast.makeText(mContext, "Ação cancelada", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void updatePurchaseProductStatus(Item product, int productStatus) {
        PurchaseItemStatus purchaseItemStatus = new PurchaseItemStatus();
        purchaseItemStatus.setPurchaseId(mPurchaseId);
        purchaseItemStatus.setProductId(product.getId());
        purchaseItemStatus.setStatus(productStatus);

        db.updatePurchaseProductStatus(purchaseItemStatus);
        Category itemCategory = db.retrieveCategoryById(product.getCategoryId());

        sectionedExpandableLayoutHelper.removeItem(itemCategory.getName(), product);
        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }

    private void extractSectionOrder(Item item) {
        if (!mSectionsOnCurrentPurchase.contains((int) item.getCategoryId())) {
            mSectionsOnCurrentPurchase.add((int) item.getCategoryId());
        }
    }

    @Override
    public void itemClicked(Item item) {
        changeProductStatusDialog(item);
    }

    @Override
    public void itemClicked(Section section) {
        Toast.makeText(mContext, "Section: " + section.getName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

