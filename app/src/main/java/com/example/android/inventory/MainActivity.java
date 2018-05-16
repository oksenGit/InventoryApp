package com.example.android.inventory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory.data.InventoryContract.InventoryEntry;
import com.example.android.inventory.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

    private EditText pName;
    private EditText pPrice;
    private EditText pQuantity;
    private EditText sName;
    private EditText sPhone;
    private FloatingActionButton bAdd;
    private FloatingActionButton bShow;
    private TextView dbResult;

    InventoryDbHelper mDbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pName = (EditText) findViewById(R.id.edit_product_name);
        pPrice = (EditText) findViewById(R.id.edit_product_price);
        pQuantity = (EditText) findViewById(R.id.edit_product_quantity);
        sName = (EditText) findViewById(R.id.edit_supplier_name);
        sPhone = (EditText) findViewById(R.id.edit_supplier_phone);
        bAdd = (FloatingActionButton) findViewById(R.id.floating_insert);
        bShow = (FloatingActionButton) findViewById(R.id.floating_show);
        dbResult = (TextView) findViewById(R.id.text_result);

        mDbHelper = new InventoryDbHelper(this);
        db = mDbHelper.getReadableDatabase();

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = insertProduct();
                if(id != -1){
                    Toast.makeText(getApplicationContext(),"Product added with ID: " + id,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Failed to add product!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatabaseInfo();
                Toast.makeText(getApplicationContext(),"Products Printed in Log Tag: InventoryShow",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private long insertProduct(){
        String nameText = pName.getText().toString().trim();
        Double priceReal = Double.parseDouble(pPrice.getText().toString().trim());
        int quantityInt = Integer.parseInt(pQuantity.getText().toString().trim());
        String supplierNameText = sName.getText().toString().trim();
        String supplierPhoneText = sPhone.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, nameText);
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, priceReal);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, quantityInt);
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierNameText);
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, supplierPhoneText);

        return db.insert(InventoryEntry.TABLE_NAME, null, values);
    }

    private void showDatabaseInfo() {

        String[] projection = {"*"};
        Cursor cursor = db.query(InventoryEntry.TABLE_NAME, projection, null, null, null, null, null);
        try {
            StringBuilder result = new StringBuilder();
            result.append("The products table contains ").append(cursor.getCount()).append(" Product(s).\n\n");
            result.append(InventoryEntry._ID + " - "
                    + InventoryEntry.COLUMN_PRODUCT_NAME + " - "
                    + InventoryEntry.COLUMN_PRODUCT_PRICE + " - "
                    + InventoryEntry.COLUMN_PRODUCT_QUANTITY + " - "
                    + InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " - "
                    + InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                Double currentPrice = cursor.getDouble(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                String currentPhone = cursor.getString(phoneColumnIndex);

                result.append("\n")
                        .append(currentID)
                        .append(" - ")
                        .append(currentName)
                        .append(" - ")
                        .append(currentPrice)
                        .append(" - ")
                        .append(currentQuantity)
                        .append(" - ")
                        .append(currentSupplier)
                        .append(" - ")
                        .append(currentPhone);
            }
            dbResult.setText(result.toString());
            Log.i("InventoryShow", result.toString());
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
}
