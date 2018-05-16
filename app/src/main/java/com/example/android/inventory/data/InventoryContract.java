package com.example.android.inventory.data;

import android.provider.BaseColumns;

public class InventoryContract {
    public static class InventoryEntry implements BaseColumns{
        public static final String TABLE_NAME = "products";
        public static final String ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE = "supplier_phone";
    }
}
