package com.example.passwordapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "password_generator_db";
    private static final String LIST_NAME = "list";
    private static final String LIST_TITLE = "title";
    private static final String LIST_USERNAME = "username";
    private static final String LIST_PASSWORD = "password";
    private static final String LIST_ID = "id";

    private Context context;

    SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + LIST_NAME + " (" +
                LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LIST_TITLE + " TEXT, " +
                LIST_USERNAME + " TEXT, " +
                LIST_PASSWORD + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LIST_NAME);
        onCreate(sqLiteDatabase);
    }

    public void saveToDB(String title, String username, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.LIST_TITLE, title);
        values.put(SQLiteDBHelper.LIST_USERNAME, username);
        values.put(SQLiteDBHelper.LIST_PASSWORD, password);

        database.insert(LIST_NAME, null, values);
        database.close();
    }

    public HashMap<String, String> getSingleData(int id){
        HashMap<String,String> data = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + LIST_NAME+ " WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            data.put(LIST_TITLE, cursor.getString(1));
            data.put(LIST_USERNAME, cursor.getString(2));
            data.put(LIST_PASSWORD, cursor.getString(3));
        }
        cursor.close();
        db.close();
        return data;
    }

    public ArrayList<HashMap<String, String>> getAllData(){
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + LIST_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);

        ArrayList<HashMap<String, String>> allData = new ArrayList<HashMap<String, String>>();
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                allData.add(map);
            } while (cursor.moveToNext());
        }
        database.close();

        return allData;
    }

    public void updateList (String title, String username, String password, int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LIST_TITLE, title);
        values.put(LIST_USERNAME, username);
        values.put(LIST_PASSWORD, password);

        database.update(LIST_NAME, values, LIST_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void deleteFromDB(String title, String username, String password){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(LIST_NAME, LIST_TITLE + " = ? AND " + LIST_USERNAME + " = ? AND " + LIST_PASSWORD + " = ?", new String[] {title, username, password});
    }

    public void clearDB(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(LIST_NAME, null, null);
        database.close();
    }

    public boolean isExist(String title, String username, String password){
        boolean is = false;
        String selectQuery = "SELECT * FROM " + LIST_NAME + " WHERE " + LIST_TITLE + " =? AND " + LIST_USERNAME + " =? AND " + LIST_PASSWORD + " =?";

        try (SQLiteDatabase database = this.getReadableDatabase(); Cursor cursor = database.rawQuery(selectQuery, new String[] {title, username, password})){
            cursor.moveToFirst();
            is = cursor.getCount() > 0;
        } catch (Exception e){
            e.printStackTrace();
        }

        return is;
    }
}
