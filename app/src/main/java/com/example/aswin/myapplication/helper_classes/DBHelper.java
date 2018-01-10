package com.example.aswin.myapplication.helper_classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.aswin.myapplication.model_classes.MoneyDonor;

import java.util.List;

/**
 * Created by ASWIN on 1/10/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="money.db";
    private static final int DB_VERSION=1;
    private static DBHelper mInstance;
    private static final String TABLE_RECORDS="records_table";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_AMOUNT="amount";
    private static final String id="_id";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context){
        if(mInstance==null){
            mInstance=new DBHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECORDS_TABLE="CREATE TABLE "+TABLE_RECORDS+" ( "+
                id+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_NAME+" TEXT,"+
                COLUMN_AMOUNT+" TEXT)";
        db.execSQL(CREATE_RECORDS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDonorList(List<MoneyDonor> donorList){

        SQLiteDatabase db=getWritableDatabase();

        for (int i=0;i<donorList.size();i++){

            MoneyDonor donor=donorList.get(i);
            ContentValues values=new ContentValues();
            values.put(COLUMN_NAME,donor.getName());
            values.put(COLUMN_AMOUNT,donor.getAmount());
            db.insert(TABLE_RECORDS,null,values);
        }

        db.close();
        getAll();
    }


    public void getAll(){
        SQLiteDatabase db=getReadableDatabase();

        String GET_RECORDS="SELECT "+COLUMN_NAME+","+COLUMN_AMOUNT+" FROM "+TABLE_RECORDS;
        Cursor cursor=db.rawQuery(GET_RECORDS,null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                MoneyDonor donor=new MoneyDonor();
                donor.setName(cursor.getString(0));
                donor.setAmount(cursor.getString(1));
                Log.d("####", "getAll: "+donor);
                cursor.moveToNext();

            }while (!cursor.isAfterLast());
        }

        cursor.close();
        db.close();
    }
}
