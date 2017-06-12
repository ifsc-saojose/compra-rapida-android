package com.example.beatriz.listatcc.Database;

public class Contract {

    public static abstract class ProductTable {
        public static final String TABLE_NAME = "products";
        public static final String _ID = "product_id";
        public static final String PRODUCT_NAME = "product_name";
        public static final String PRODUCT_CATEGORY_ID = "product_categoryId";
        public static final String PRODUCT_FREQUENCY_ON_LIST = "product_frequency_on_list";
    }

    public static abstract class ListsTable {
        public static final String TABLE_NAME = "lists";
        public static final String _ID = "id";
        public static final String LIST_NAME = "name";
        public static final String LIST_DATE = "date";
        public static final String IS_VISIBLE = "is_visible";
    }

    public static abstract class ItemOnList {
        public static final String TABLE_NAME = "item_on_list";
        public static final String PRODUCT_ID = "item_id";
        public static final String LIST_ID = "item_list_id";
    }

    public static abstract class CategoryTable {
        public static final String TABLE_NAME = "category";
        public static final String _ID = "category_id";
        public static final String CATEGORY_NAME = "category_name";
        public static final String CATEGORY_ISVISIBLE = "category_isVisible";
    }

    public static abstract class PurchaseTable {
        public static final String TABLE_NAME = "purchase";
        public static final String _ID = "purchase_id";
        public static final String PURCHASE_NAME = "purchase_name";
        public static final String LIST_ID = "list_id";
        public static final String MARKET_ID = "market_id";
        public static final String ACTIVE = "active";
        public static final String DATE = "date";
    }

    public static abstract class PurchaseItemStatusTable {
        public static final String _ID = "purchase_item_status_id";
        public static final String TABLE_NAME = "purchase_item_status";
        public static final String PURCHASE_ID = "purchase_id";
        public static final String PRODUCT_ID = "purchase_product_id";
        public static final String STATUS = "status";
        public static final String CURRENT_TIME = "current_time";
    }

    public static abstract class SupermarketCurrentNearbyTable {
        public static final String _ID = "supermarket_current_nearby_id";
        public static final String TABLE_NAME = "supermarket_current_nearby";
        public static final String LATITUDE = "supermarket_current_nearby_lat";
        public static final String LONGITUDE = "supermarket_current_nearby_longt";
        public static final String NAME = "supermarket_current_nearby_name";
        public static final String CITY = "supermarket_current_nearby_city";
        public static final String STREET = "supermarket_current_nearby_street";
    }

    public static abstract class RelacionamentoEntreProdutos {
        public static final String _ID = "relacionamento_entre_produtos_id";
        public static final String TABLE_NAME = "relacionamento_entre_produtos";
        public static final String ID_MERCADO = "relacionamento_entre_produtos_id_mercado";
        public static final String ID_SESSAO = "relacionamento_entre_produtos_id_secao";
        public static final String ID_PRODUTO = "relacionamento_entre_produtos_id_produto";
        public static final String MEDIA_DAS_ORDENS = "relacionamento_entre_produtos_media_das_ordens";
    }

    public static abstract class RelacionamentoEntreSecoes {
        public static final String _ID = "relacionamento_entre_secoes_id";
        public static final String TABLE_NAME = "relacionamento_entre_secoes";
        public static final String ID_MERCADO = "relacionamento_entre_secoes_id_mercado";
        public static final String ID_CATEGORIA_1 = "relacionamento_entre_secoes_id_categoria_1";
        public static final String ID_CATEGORIA_2 = "relacionamento_entre_secoes_id_categoria_2";
        public static final String PURCHASE_TOTAL = "relacionamento_entre_secoes_purchase_total";
        public static final String MEDIA_DAS_ORDENS = "relacionamento_entre_secoes_media_das_ordens";
    }

    public static abstract class OrdemProduto {
        public static final String _ID = "ordem_produto_id";
        public static final String TABLE_NAME = "ordem_produto";
        public static final String ID_MERCADO = "ordem_id_mercado";
        public static final String ID_SESSAO = "ordem_id_secao";
        public static final String ID_PRODUTO = "ordem_id_produto";
        public static final String ORDEM_INSERCAO = "ordem_de_insercao";
    }

    public static abstract class OrdemSecao {
        public static final String _ID = "ordem_sessao_id";
        public static final String TABLE_NAME = "ordem_secao";
        public static final String ID_MERCADO = "ordem_sessao_id_mercado";
        public static final String ID_SESSAO = "ordem_sessao_id_secao";
        public static final String ORDEM_INSERCAO = "ordem_sessao_ordem_insercao";
    }
}
