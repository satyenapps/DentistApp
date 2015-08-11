package com.appslight.dentistapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Satyen on 13/06/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "user.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_NAME = "name";
    public static final String USERS_COLUMN_DOB = "dob";
    public static final String USERS_COLUMN_GENDER = "gender";
    public static final String USERS_COLUMN_CITY = "address";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_PHONE = "phone";

    public static final String CREATE_USERS = "CREATE TABLE " + USERS_TABLE_NAME + "(" + USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USERS_COLUMN_NAME + " TEXT, " + USERS_COLUMN_DOB + " TEXT, " + USERS_COLUMN_CITY + " TEXT, " + USERS_COLUMN_GENDER + " TEXT, " + USERS_COLUMN_PHONE + " TEXT, " + USERS_COLUMN_EMAIL + " TEXT)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void insertUser(String name, String dob, String gender, String email, String city) {
        SQLiteDatabase db = getWritableDatabase();

        /*String query = "select * from " + USERS_TABLE_NAME + " where " + USERS_COLUMN_NAME + "='" + name + "'";

        Cursor cursor = db.rawQuery(query, null);
        */


        ContentValues values = new ContentValues();
        values.put(USERS_COLUMN_NAME, name);
        values.put(USERS_COLUMN_CITY, city);
        values.put(USERS_COLUMN_EMAIL, email);
//        values.put(PHONE, frd.getPhone());
        values.put(USERS_COLUMN_GENDER, gender);
        values.put(USERS_COLUMN_DOB, dob);

        db.insert(USERS_TABLE_NAME, null, values);
    }

    public Cursor getUsers() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "select distinct " + USERS_COLUMN_NAME + " from " + USERS_TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}
