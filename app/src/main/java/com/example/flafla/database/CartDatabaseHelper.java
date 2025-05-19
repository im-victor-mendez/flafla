package com.example.flafla.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.flafla.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "flafla_cart.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "cart";
    private static final String PRODUCT_ID = "product_id";
    private static final String QUANTITY = "quantity";

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE + "("
                + PRODUCT_ID + " TEXT PRIMARY KEY,"
                + QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void addItem(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{QUANTITY},
                PRODUCT_ID + "=?", new String[]{productId},
                null, null, null);

        if (cursor.moveToFirst()) {
            int quantity = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put(QUANTITY, quantity + 1);
            db.update(TABLE, values, PRODUCT_ID + "=?", new String[]{productId});
        } else {
            ContentValues values = new ContentValues();
            values.put(PRODUCT_ID, productId);
            values.put(QUANTITY, 1);
            db.insert(TABLE, null, values);
        }

        cursor.close();
        db.close();
    }

    public void removeItem(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{QUANTITY},
                PRODUCT_ID + "=?", new String[]{productId},
                null, null, null);

        if (cursor.moveToFirst()) {
            int quantity = cursor.getInt(0);
            if (quantity > 1) {
                ContentValues values = new ContentValues();
                values.put(QUANTITY, quantity - 1);
                db.update(TABLE, values, PRODUCT_ID + "=?", new String[]{productId});
            } else {
                db.delete(TABLE, PRODUCT_ID + "=?", new String[]{productId});
            }
        }

        cursor.close();
        db.close();
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, null, null);
        db.close();
    }

    public List<CartItem> getAllItems() {
        List<CartItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String productId = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_ID));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY));

                CartItem item = new CartItem.Builder()
                        .setProductId(productId)
                        .setQuantity(quantity)
                        .build();
                items.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return items;
    }
}
