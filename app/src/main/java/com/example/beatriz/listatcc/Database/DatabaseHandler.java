package com.example.beatriz.listatcc.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.beatriz.listatcc.Model.Category;
import com.example.beatriz.listatcc.Model.Item;
import com.example.beatriz.listatcc.Model.Lists;
import com.example.beatriz.listatcc.Model.Mercado;
import com.example.beatriz.listatcc.Model.Product;
import com.example.beatriz.listatcc.Model.Purchase;
import com.example.beatriz.listatcc.Model.PurchaseItemStatus;
import com.example.beatriz.listatcc.NewList.ProductItem;
import com.example.beatriz.listatcc.R;
import com.example.beatriz.listatcc.Util.RelacionamentoEntreSecoes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beatriz on 16/07/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static Context mContext;
    //Toda vez que alterar o banco lembrar de alterar a versao do BD
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.ListsTable.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    private void createAllTables(SQLiteDatabase db) {
        Log.i("SQL_table ", "CRIANDO TABELA");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.ListsTable.TABLE_NAME + " (" //
                + Contract.ListsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," //
                + Contract.ListsTable.LIST_NAME + " TEXT," //
                + Contract.ListsTable.LIST_DATE + " INTEGER,"
                + Contract.ListsTable.IS_VISIBLE + " INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.ItemOnList.TABLE_NAME + " (" //
                + Contract.ItemOnList.LIST_ID + " INTEGER ," //
                + Contract.ItemOnList.PRODUCT_ID + " INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.CategoryTable.TABLE_NAME + " (" //
                + Contract.CategoryTable._ID + " INTEGER PRIMARY KEY," //
                + Contract.CategoryTable.CATEGORY_NAME + " TEXT," //
                + Contract.CategoryTable.CATEGORY_ISVISIBLE + " INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.ProductTable.TABLE_NAME + " (" //
                + Contract.ProductTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," //
                + Contract.ProductTable.PRODUCT_NAME + " TEXT," //
                + Contract.ProductTable.PRODUCT_CATEGORY_ID + " INTEGER,"
                + Contract.ProductTable.PRODUCT_FREQUENCY_ON_LIST + " INTEGER DEFAULT 1)"); //

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.PurchaseTable.TABLE_NAME + " (" //
                + Contract.PurchaseTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," //
                + Contract.PurchaseTable.PURCHASE_NAME + " TEXT," //
                + Contract.PurchaseTable.LIST_ID + " INTEGER NOT NULL," //
                + Contract.PurchaseTable.MARKET_ID + " TEXT," //
                + Contract.PurchaseTable.ACTIVE + " INTEGER DEFAULT 1,"
                + Contract.PurchaseTable.DATE + " INTEGER)"); //

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.PurchaseItemStatusTable.TABLE_NAME + " (" //
                + Contract.PurchaseItemStatusTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," //
                + Contract.PurchaseItemStatusTable.PURCHASE_ID + " INTEGER NOT NULL," //
                + Contract.PurchaseItemStatusTable.PRODUCT_ID + " INTEGER NOT NULL," //
                + Contract.PurchaseItemStatusTable.STATUS + " INTEGER DEFAULT 1," //
                + Contract.PurchaseItemStatusTable.CURRENT_TIME + " INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.SupermarketCurrentNearbyTable.TABLE_NAME + " (" //
                + Contract.SupermarketCurrentNearbyTable._ID + " TEXT PRIMARY KEY," //
                + Contract.SupermarketCurrentNearbyTable.NAME + " TEXT," //
                + Contract.SupermarketCurrentNearbyTable.CITY + " TEXT," //
                + Contract.SupermarketCurrentNearbyTable.STREET + " TEXT," //
                + Contract.SupermarketCurrentNearbyTable.LATITUDE + " INTEGER,"
                + Contract.SupermarketCurrentNearbyTable.LONGITUDE + " INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.OrdemProduto.TABLE_NAME + " (" //
                + Contract.OrdemProduto._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," //
                + Contract.OrdemProduto.ID_MERCADO + " TEXT," //
                + Contract.OrdemProduto.ID_SESSAO + " INTEGER," //
                + Contract.OrdemProduto.ID_PRODUTO + " INTEGER," //
                + Contract.OrdemProduto.ORDEM_INSERCAO + " INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.OrdemSecao.TABLE_NAME + " (" //
                + Contract.OrdemSecao._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," //
                + Contract.OrdemSecao.ID_MERCADO + " TEXT," //
                + Contract.OrdemSecao.ID_SESSAO + " INTEGER," //
                + Contract.OrdemSecao.ORDEM_INSERCAO + " INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.RelacionamentoEntreProdutos.TABLE_NAME + " (" //
                + Contract.RelacionamentoEntreProdutos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," //
                + Contract.RelacionamentoEntreProdutos.ID_MERCADO + " TEXT," //
                + Contract.RelacionamentoEntreProdutos.ID_SESSAO + " INTEGER," //
                + Contract.RelacionamentoEntreProdutos.ID_PRODUTO + " INTEGER," //
                + Contract.RelacionamentoEntreProdutos.MEDIA_DAS_ORDENS + " FLOAT DEFAULT 0)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Contract.RelacionamentoEntreSecoes.TABLE_NAME + " (" //
                + Contract.RelacionamentoEntreSecoes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," //
                + Contract.RelacionamentoEntreSecoes.ID_MERCADO + " TEXT," //
                + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_1 + " INTEGER," //
                + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_2 + " INTEGER," //
                + Contract.RelacionamentoEntreSecoes.PURCHASE_TOTAL + " INTEGER DEFAULT 0," //
                + Contract.RelacionamentoEntreSecoes.MEDIA_DAS_ORDENS + " TEXT)");
    }

    public void updateSupermarketCurrentNearby(ArrayList<Mercado> mercadoNearby) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contract.SupermarketCurrentNearbyTable.TABLE_NAME, null, null);

        for (int i = 0; i < mercadoNearby.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.SupermarketCurrentNearbyTable._ID, mercadoNearby.get(i).getId());
            contentValues.put(Contract.SupermarketCurrentNearbyTable.NAME, mercadoNearby.get(i).getName());
            contentValues.put(Contract.SupermarketCurrentNearbyTable.CITY, mercadoNearby.get(i).getCidade());
            contentValues.put(Contract.SupermarketCurrentNearbyTable.STREET, mercadoNearby.get(i).getRua());
            db.insert(Contract.SupermarketCurrentNearbyTable.TABLE_NAME, null, contentValues);
        }
    }

    public void populateOrdemSecao(String idMercado, int idSecao, int secaoOrder) {

        ContentValues contentValues;
        SQLiteDatabase db = this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(Contract.OrdemSecao.ID_MERCADO, idMercado);
        contentValues.put(Contract.OrdemSecao.ID_SESSAO, idSecao);
        contentValues.put(Contract.OrdemSecao.ORDEM_INSERCAO, secaoOrder);


        db.insert(Contract.OrdemSecao.TABLE_NAME, null, contentValues);
        db.close();
    }

    public Cursor retrieveSectionRelationsByMarketId(String marketId) {
        String selectQuery = "SELECT * FROM " + Contract.RelacionamentoEntreProdutos.TABLE_NAME
                + " WHERE " + Contract.RelacionamentoEntreProdutos.ID_MERCADO + " = " + marketId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();

        return cursor;
    }


    public int retrieveAllSectionOrderByMarketId(String marketId, int categoriaId) {


        String selectQuery = "SELECT * FROM " + Contract.OrdemSecao.TABLE_NAME
                + " WHERE " + Contract.OrdemSecao.ID_MERCADO + " = '" + marketId + "'"
                + " AND " + Contract.OrdemSecao.ID_SESSAO + " = " + categoriaId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        int ordemDeInsercaoDaCategoriaReferencia = cursor.getInt(cursor.getColumnIndex(Contract.OrdemSecao.ORDEM_INSERCAO));
        cursor.close();

        return ordemDeInsercaoDaCategoriaReferencia;
    }

    public void retrieveAllSectionOrder() {
        String selectQuery = "SELECT * FROM " + Contract.OrdemSecao.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            Log.i("TAG_MERCADO: ", cursor.getString(cursor.getColumnIndex(Contract.OrdemSecao.ID_MERCADO)));
            Log.i("TAG_ID_SECAO: ", String.valueOf(cursor.getInt(cursor.getColumnIndex(Contract.OrdemSecao.ID_SESSAO))));
            Log.i("TAG_ORDEM: ", String.valueOf(cursor.getInt(cursor.getColumnIndex(Contract.OrdemSecao.ORDEM_INSERCAO))));

        }
        cursor.close();
    }

    public long retrieveNextCategory(String mercadoId, long categoryReference, String categoriesHasRelations) {
        //Seleciona a categoria que está mais próxima a categoriaReferencia, tendo como base o mercado, a menor média das ordens e que
        //a categoria não esteja presente ainda na lista de ordenamento de categorias.

        String selectQuery = " SELECT " + Contract.RelacionamentoEntreSecoes._ID + ","
                + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_1 + ","
                + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_2 + ","
                + " MIN( " + Contract.RelacionamentoEntreSecoes.MEDIA_DAS_ORDENS + ") AS " + Contract.RelacionamentoEntreSecoes.MEDIA_DAS_ORDENS
                + " FROM " + Contract.RelacionamentoEntreSecoes.TABLE_NAME
                + " WHERE " + Contract.RelacionamentoEntreSecoes.ID_MERCADO + " = " + "'" + mercadoId + "'"
                + " AND " + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_1 + " = " + categoryReference
                + " AND " + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_2 + " IN (" + categoriesHasRelations + ")"
                + " GROUP BY " + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_1
                + " LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.v("WW2", "cursor.getCount()" + cursor.getCount());
        Log.v("WW2", DatabaseUtils.dumpCursorToString(cursor));
        long categoryId = 999;

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            categoryId = cursor.getInt(cursor.getColumnIndex(Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_2));
        }

        cursor.close();
        return categoryId;
    }

    public RelacionamentoEntreSecoes retrieveParRelationCategories(String mercadoId, long categoria1, long categoria2) {
        //List<RelacionamentoEntreSecoes> relacionamentoEntreSecoesList = new ArrayList<>();
        Log.i("MERCADO_ID: ", mercadoId);

        String selectQuery = "SELECT * FROM " + Contract.RelacionamentoEntreSecoes.TABLE_NAME
                + " WHERE " + Contract.RelacionamentoEntreSecoes.ID_MERCADO + " = ? "
                + " AND " + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_1 + " = ? "
                + " AND " + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_2 + " = ? ";

//        String selectQuery = "SELECT * FROM " + Contract.RelacionamentoEntreSecoes.TABLE_NAME
//                + " WHERE " + Contract.RelacionamentoEntreSecoes.ID_MERCADO + " = ? "
//                + " AND (" + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_1 + " = ? "
//                + " OR " + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_2 + " = ? )"
//                + " AND (" + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_2 + " = ? "
//                + " OR " + Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_1 + " = ? )";

        Log.i("TAG_QUERY: ", selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{mercadoId, String.valueOf(categoria1), String.valueOf(categoria2)});

        //  Cursor cursor = db.rawQuery(selectQuery, null);
        //cursor.moveToFirst();
        Log.v("TAG_CursorObject", DatabaseUtils.dumpCursorToString(cursor));
        Log.i("TAG_CURSOR_COUNT: ", String.valueOf(cursor.getCount()));
        RelacionamentoEntreSecoes relacionamentoEntreSecoes = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            relacionamentoEntreSecoes = new RelacionamentoEntreSecoes();
            relacionamentoEntreSecoes.setId(cursor.getInt(cursor.getColumnIndex(Contract.RelacionamentoEntreSecoes._ID)));
            relacionamentoEntreSecoes.setCategoria1(cursor.getInt(cursor.getColumnIndex(Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_1)));
            relacionamentoEntreSecoes.setCategoria2(cursor.getInt(cursor.getColumnIndex(Contract.RelacionamentoEntreSecoes.ID_CATEGORIA_2)));
            relacionamentoEntreSecoes.setMediaDasOrdens(cursor.getDouble(cursor.getColumnIndex(Contract.RelacionamentoEntreSecoes.MEDIA_DAS_ORDENS)));
            relacionamentoEntreSecoes.setMercadoId(cursor.getString(cursor.getColumnIndex(Contract.RelacionamentoEntreSecoes.ID_MERCADO)));
            relacionamentoEntreSecoes.setPurchaseTotal(1 + cursor.getInt(cursor.getColumnIndex(Contract.RelacionamentoEntreSecoes.PURCHASE_TOTAL)));

            //   relacionamentoEntreSecoesList.add(relacionamentoEntreSecoes);
        }
        cursor.close();
        return relacionamentoEntreSecoes;
    }

    public ArrayList<Mercado> retrieveAllSupermarketCurrentNearby() {

        ArrayList<Mercado> scn = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Contract.SupermarketCurrentNearbyTable.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        while (cursor.moveToNext()) {
            Mercado supermarket = new Mercado();

            supermarket.setId(cursor.getString(cursor.getColumnIndex(Contract.SupermarketCurrentNearbyTable._ID)));
            supermarket.setName(cursor.getString(cursor.getColumnIndex(Contract.SupermarketCurrentNearbyTable.NAME)));
            supermarket.setRua(cursor.getString(cursor.getColumnIndex(Contract.SupermarketCurrentNearbyTable.STREET)));
            scn.add(supermarket);
        }
        cursor.close();
        return scn;
    }

    public long createList(Lists list) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = createContentValuesForList(list);

        long listId = db.insert(Contract.ListsTable.TABLE_NAME, Contract.ListsTable._ID, contentValues);
        list.setId(listId);

        createItensOnList(list.getId(), list.getProductItemList());

        db.close();
        return listId;
    }

    public void createItensOnList(long listId, List<ProductItem> productItems) {
        ContentValues contentValues;
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < productItems.size(); i++) {
            contentValues = new ContentValues();
            contentValues.put(Contract.ItemOnList.LIST_ID, listId);
            contentValues.put(Contract.ItemOnList.PRODUCT_ID, productItems.get(i).getId());
            db.insert(Contract.ItemOnList.TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    ContentValues createContentValuesForList(Lists list) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.ListsTable.LIST_NAME, list.getName());
        contentValues.put(Contract.ListsTable.LIST_DATE, list.getDate());

        return contentValues;
    }

    public ArrayList<Lists> retrieveAllLists() {
        ArrayList<Lists> allLists = new ArrayList<Lists>();
        String selectQuery = "SELECT  * FROM " + Contract.ListsTable.TABLE_NAME
                + " WHERE " + Contract.ListsTable.IS_VISIBLE + " = " + 1
                + " ORDER BY " + Contract.ListsTable.LIST_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        while (cursor.moveToNext()) {
            Lists list = new Lists();

            list.setId(cursor.getLong(cursor.getColumnIndex(Contract.ListsTable._ID)));
            list.setName(cursor.getString(cursor.getColumnIndex(Contract.ListsTable.LIST_NAME)));
            list.setDate(cursor.getLong(cursor.getColumnIndex(Contract.ListsTable.LIST_DATE)));

            allLists.add(list);
        }

        return allLists;
    }

    public Lists retrieveListById(long listId) {
        String selectQuery = "SELECT  * FROM " + Contract.ListsTable.TABLE_NAME
                + " WHERE " + Contract.ListsTable._ID + " = " + listId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        Lists list = new Lists();
        list.setName(cursor.getString(cursor.getColumnIndex(Contract.ListsTable.LIST_NAME)));
        list.setDate(cursor.getLong(cursor.getColumnIndex(Contract.ListsTable.LIST_DATE)));

        return list;
    }

    public void populateCategoryTable(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Contract.CategoryTable._ID, category.getId());
        cv.put(Contract.CategoryTable.CATEGORY_NAME, category.getName());

        db.insert(Contract.CategoryTable.TABLE_NAME, null, cv);
    }

    public void populateProductTable(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Contract.ProductTable._ID, product.getId());
        cv.put(Contract.ProductTable.PRODUCT_CATEGORY_ID, product.getCategory().getId());
        cv.put(Contract.ProductTable.PRODUCT_NAME, product.getName());

        db.insert(Contract.ProductTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Category> retrieveAllCategories() {
        ArrayList<Category> allCategories = new ArrayList<Category>();
        String selectQuery = "SELECT  * FROM " + Contract.CategoryTable.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        while (cursor.moveToNext()) {
            Category category = new Category();

            category.setId(cursor.getLong(cursor.getColumnIndex(Contract.CategoryTable._ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(Contract.CategoryTable.CATEGORY_NAME)));

            allCategories.add(category);
        }

        return allCategories;
    }

    public Category retrieveCategoryById(long categoryId) {
        String selectQuery = "SELECT  * FROM " + Contract.CategoryTable.TABLE_NAME
                + " WHERE " + Contract.CategoryTable._ID + " = " + categoryId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        Category category = new Category();
        category.setId(cursor.getLong(cursor.getColumnIndex(Contract.CategoryTable._ID)));
        category.setName(cursor.getString(cursor.getColumnIndex(Contract.CategoryTable.CATEGORY_NAME)));

        return category;
    }

    public ArrayList<Item> retrieveItensByCategory(long categoryId) {

        ArrayList<Item> itensByCategory = new ArrayList<Item>();
        String query = "SELECT " + Contract.ProductTable._ID + "," + Contract.ProductTable.PRODUCT_NAME
                + "," + Contract.ProductTable.PRODUCT_CATEGORY_ID

                + " FROM " + Contract.ProductTable.TABLE_NAME
                + " INNER JOIN " + Contract.CategoryTable.TABLE_NAME
                + " ON " + Contract.ProductTable.PRODUCT_CATEGORY_ID + "=" + Contract.CategoryTable._ID
                + " WHERE " + Contract.ProductTable.PRODUCT_CATEGORY_ID + "=" + categoryId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Item item = new Item();

            item.setId(cursor.getLong(cursor.getColumnIndex(Contract.ProductTable._ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(Contract.ProductTable.PRODUCT_NAME)));
            item.setCategoryId(cursor.getLong(cursor.getColumnIndex(Contract.ProductTable.PRODUCT_CATEGORY_ID)));

            itensByCategory.add(item);
        }
        return itensByCategory;
    }

    public ArrayList<ProductItem> retrieveAllItems() {

        ArrayList<ProductItem> itensByCategory = new ArrayList<ProductItem>();
        String query = "SELECT * FROM " + Contract.ProductTable.TABLE_NAME
                + " ORDER BY " + Contract.ProductTable.PRODUCT_FREQUENCY_ON_LIST + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            ProductItem product = new ProductItem();

            product.setId(cursor.getLong(cursor.getColumnIndex(Contract.ProductTable._ID)));
            product.setTitle(cursor.getString(cursor.getColumnIndex(Contract.ProductTable.PRODUCT_NAME)));
            product.setImage(R.drawable.no_product_image);

            itensByCategory.add(product);
        }

        return itensByCategory;
    }

    public ArrayList<Item> retrieveItensByListId(long listId) {

        ArrayList<Item> itensByList = new ArrayList<>();
        String query = "SELECT " + Contract.ProductTable._ID + "," + Contract.ProductTable.PRODUCT_NAME
                + "," + Contract.ProductTable.PRODUCT_CATEGORY_ID
                + " FROM " + Contract.ItemOnList.TABLE_NAME
                + " INNER JOIN " + Contract.ProductTable.TABLE_NAME
                + " ON " + Contract.ItemOnList.PRODUCT_ID + "=" + Contract.ProductTable._ID
                + " WHERE " + Contract.ItemOnList.LIST_ID + "=" + listId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Item item = new Item();

            item.setId(cursor.getLong(cursor.getColumnIndex(Contract.ProductTable._ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(Contract.ProductTable.PRODUCT_NAME)));
            item.setCategoryId(cursor.getLong(cursor.getColumnIndex(Contract.ProductTable.PRODUCT_CATEGORY_ID)));

            itensByList.add(item);
        }
        cursor.close();
        return itensByList;
    }

    public void createSessionRelations(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Contract.RelacionamentoEntreSecoes.TABLE_NAME, null, contentValues);
    }

    public void updatePurchase(long purchaseId, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(Contract.PurchaseTable.TABLE_NAME, contentValues, Contract.PurchaseTable._ID + "=?",
                new String[]{String.valueOf(purchaseId)});
    }

    public void updateRelacionamentoEntreSecoes(int id, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(Contract.RelacionamentoEntreSecoes.TABLE_NAME, contentValues,
                Contract.RelacionamentoEntreSecoes._ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public long createPurchase(Purchase purchase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = createContentValuesForPurchase(purchase);

        long purchaseId = db.insert(Contract.PurchaseTable.TABLE_NAME, null, contentValues);
        purchase.setId(purchaseId);

        return purchaseId;
    }

    public void updatePurchase(Purchase purchase) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.PurchaseTable.PURCHASE_NAME, purchase.getName());
        contentValues.put(Contract.PurchaseTable.ACTIVE, purchase.getActive());
        contentValues.put(Contract.PurchaseTable.MARKET_ID, purchase.getMarketId());
        contentValues.put(Contract.PurchaseTable.LIST_ID, purchase.getFromList());

        db.update(Contract.PurchaseTable.TABLE_NAME, contentValues,
                Contract.PurchaseTable._ID + "=?",
                new String[]{String.valueOf(purchase.getId())});
    }

    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.ProductTable.PRODUCT_NAME, product.getName());

        db.update(Contract.ProductTable.TABLE_NAME, contentValues,
                Contract.ProductTable._ID + "=?",
                new String[]{String.valueOf(product.getId())});
    }

    public void updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.CategoryTable.CATEGORY_NAME, category.getName());

        db.update(Contract.CategoryTable.TABLE_NAME, contentValues,
                Contract.CategoryTable._ID + "=?",
                new String[]{String.valueOf(category.getId())});
    }


    public void updateList(long listId, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.update(Contract.ListsTable.TABLE_NAME, contentValues,
                Contract.ListsTable._ID + "=?",
                new String[]{String.valueOf(listId)});

    }

    ContentValues createContentValuesForPurchase(Purchase purchase) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(Contract.PurchaseTable.LIST_ID, purchase.getFromList());
        contentvalues.put(Contract.PurchaseTable.MARKET_ID, purchase.getMarketId());
        contentvalues.put(Contract.PurchaseTable.ACTIVE, 1);

        return contentvalues;
    }

    public void populatePurchaseItens(Purchase purchase) {
        ArrayList<Item> itens = retrieveItensByListId(purchase.getFromList());

        ContentValues contentValues;
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < itens.size(); i++) {
            contentValues = new ContentValues();
            contentValues.put(Contract.PurchaseItemStatusTable.PURCHASE_ID, purchase.getId());
            contentValues.put(Contract.PurchaseItemStatusTable.PRODUCT_ID, itens.get(i).getId());

            db.insert(Contract.PurchaseItemStatusTable.TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    public ArrayList<Item> retrievePurchaseItensByStatus(long purchaseId, int status) {

        ArrayList<Item> itensByList = new ArrayList<>();
        String query = "SELECT " + Contract.ProductTable._ID + "," + Contract.ProductTable.PRODUCT_NAME
                + "," + Contract.ProductTable.PRODUCT_CATEGORY_ID
                + " FROM " + Contract.PurchaseItemStatusTable.TABLE_NAME
                + " INNER JOIN " + Contract.ProductTable.TABLE_NAME
                + " ON " + Contract.PurchaseItemStatusTable.PRODUCT_ID + "=" + Contract.ProductTable._ID
                + " WHERE " + Contract.PurchaseItemStatusTable.PURCHASE_ID + "=" + purchaseId
                + " AND " + Contract.PurchaseItemStatusTable.STATUS + "=" + status;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Item item = new Item();

            item.setId(cursor.getLong(cursor.getColumnIndex(Contract.ProductTable._ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(Contract.ProductTable.PRODUCT_NAME)));
            item.setCategoryId(cursor.getLong(cursor.getColumnIndex(Contract.ProductTable.PRODUCT_CATEGORY_ID)));
            itensByList.add(item);
        }
        cursor.close();
        return itensByList;
    }

    public ArrayList<Category> retrievePurchaseCategory(long listId) {
        ArrayList<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.CategoryTable.TABLE_NAME
                + " INNER JOIN " + Contract.ProductTable.TABLE_NAME
                + " ON " + Contract.ProductTable.PRODUCT_CATEGORY_ID + "=" + Contract.CategoryTable._ID
                + " INNER JOIN " + Contract.ItemOnList.TABLE_NAME
                + " ON " + Contract.ItemOnList.PRODUCT_ID + "=" + Contract.ProductTable._ID
                + " WHERE " + Contract.ItemOnList.LIST_ID + "=" + listId
                + " GROUP BY " + Contract.CategoryTable._ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Category category = new Category();
            category.setId(cursor.getLong(cursor.getColumnIndex(Contract.CategoryTable._ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(Contract.CategoryTable.CATEGORY_NAME)));
            categories.add(category);
        }
        cursor.close();
        return categories;

    }

    public ArrayList<Purchase> retrieveAllPurchases() {
        ArrayList<Purchase> allPurchases = new ArrayList<Purchase>();
        String selectQuery = "SELECT  * FROM " + Contract.PurchaseTable.TABLE_NAME
                + " ORDER BY " + Contract.PurchaseTable.DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            Purchase purchase = new Purchase();

            purchase.setId(cursor.getLong(cursor.getColumnIndex(Contract.PurchaseTable._ID)));
            purchase.setName(cursor.getString(cursor.getColumnIndex(Contract.PurchaseTable.PURCHASE_NAME)));
            purchase.setMarketId(cursor.getString(cursor.getColumnIndex(Contract.PurchaseTable.MARKET_ID)));
            purchase.setDate(cursor.getLong(cursor.getColumnIndex(Contract.PurchaseTable.DATE)));

            allPurchases.add(purchase);
        }
        return allPurchases;
    }

    public void updatePurchaseProductStatus(PurchaseItemStatus purchaseItemStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = createContentValuesForPurchaseItemStatus(purchaseItemStatus);
        db.update(Contract.PurchaseItemStatusTable.TABLE_NAME, contentValues, Contract.PurchaseItemStatusTable.PURCHASE_ID + " = ? AND "
                        + Contract.PurchaseItemStatusTable.PRODUCT_ID + " = ?",
                new String[]{String.valueOf(purchaseItemStatus.getPurchaseId()), String.valueOf(purchaseItemStatus.getProductId())});
    }

    private ContentValues createContentValuesForPurchaseItemStatus(PurchaseItemStatus purchaseItemStatus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.PurchaseItemStatusTable.PURCHASE_ID, purchaseItemStatus.getPurchaseId());
        contentValues.put(Contract.PurchaseItemStatusTable.PRODUCT_ID, purchaseItemStatus.getProductId());
        contentValues.put(Contract.PurchaseItemStatusTable.STATUS, purchaseItemStatus.getStatus());

        return contentValues;
    }

    public void updateProductsFrequencyOnList(long productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + Contract.ProductTable.TABLE_NAME
                        + " SET " + Contract.ProductTable.PRODUCT_FREQUENCY_ON_LIST + "=" + Contract.ProductTable.PRODUCT_FREQUENCY_ON_LIST + "+1"
                        + " WHERE " + Contract.ProductTable._ID + "=?",
                new String[]{String.valueOf(productId)});

    }
}
