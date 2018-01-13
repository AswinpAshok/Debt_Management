package com.example.aswin.myapplication.helper_classes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.aswin.myapplication.model_classes.DashboardInfo;
import com.example.aswin.myapplication.model_classes.MoneyDonor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASWIN on 1/10/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String BROADCAST_ID="1457";

    private static final String DB_NAME="money.db";
    private static final int DB_VERSION=1;
    private static DBHelper mInstance;
    private static final String TABLE_RECORDS="records_table";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_AMOUNT="amount";
    private static final String COLUMN_CREATED_DATE="created_date";
    private static final String id="_id";
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

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
                COLUMN_AMOUNT+" INTEGER,"+
                COLUMN_CREATED_DATE+" TEXT)";
        db.execSQL(CREATE_RECORDS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDonorList(List<MoneyDonor> donorList){

        SQLiteDatabase db=getWritableDatabase();

        for (int i=0;i<donorList.size();i++){

            MoneyDonor donor=donorList.get(i);

            String CHECK_RECORD="SELECT "+id+" FROM "+TABLE_RECORDS
                    +" WHERE "+COLUMN_NAME+"='"+donor.getName()
                    +"' AND "+COLUMN_AMOUNT+"='"+donor.getAmount()+"'";
            Cursor c=db.rawQuery(CHECK_RECORD,null);

            if(c.getCount()==0){

                ContentValues values=new ContentValues();
                values.put(COLUMN_NAME,donor.getName());
                values.put(COLUMN_AMOUNT,donor.getAmount());
                values.put(COLUMN_CREATED_DATE, System.currentTimeMillis());
                db.insert(TABLE_RECORDS,null,values);
            }
        }

//        listener.onDatabaseChanged();
        Intent intent=new Intent(BROADCAST_ID);
//        intent.setAction("ACTION_DB_CHANGED");
        context.sendBroadcast(intent);
        db.close();
//        getAll();
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
                donor.setAmount(cursor.getInt(1));
                Log.d("####", "getAll: "+donor);
                cursor.moveToNext();

            }while (!cursor.isAfterLast());
        }

        cursor.close();
        db.close();
    }

    public List<MoneyDonor> searchDonors(String searchQuery){
        List<MoneyDonor> donorList=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        String SEARCH="SELECT * FROM "+TABLE_RECORDS+" WHERE "+COLUMN_NAME+" LIKE '%"+searchQuery+"%'";
        Cursor cursor=db.rawQuery(SEARCH,null );

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                MoneyDonor donor=new MoneyDonor();
                donor.setId(cursor.getInt(0));
                Log.d("####", "searchDonors: "+donor.getId());
                donor.setName(cursor.getString(1));
                donor.setAmount(cursor.getInt(2));
                donorList.add(donor);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }else {
            cursor.close();
            db.close();
            return null;
        }

        cursor.close();
        db.close();
        return donorList;
    }

    public MoneyDonor getDonorDetails(int ID){
        SQLiteDatabase db=getReadableDatabase();
        String GET_DETAILS="SELECT * FROM "+TABLE_RECORDS+" WHERE "+id+"="+ID;

        Cursor cursor=db.rawQuery(GET_DETAILS,null);
        if(cursor.getCount()==1){
            cursor.moveToFirst();
            MoneyDonor donor=new MoneyDonor();
            donor.setId(ID);
            donor.setName(cursor.getString(1));
            donor.setAmount(cursor.getInt(2));
            donor.setCreatedDate(cursor.getString(3));

            cursor.close();
            db.close();
            return donor;
        }

        cursor.close();
        db.close();
        return null;
    }

    public int addDonor(String name,String amount){
        SQLiteDatabase db=getWritableDatabase();

        SQLiteDatabase db1=getReadableDatabase();

        String CHECK_CLASH_QUERY="SELECT "+id+" FROM "+TABLE_RECORDS+
                " WHERE "+COLUMN_NAME+"='"+name+"' AND "
                +COLUMN_AMOUNT+"="+amount;

        Cursor cursor=db1.rawQuery(CHECK_CLASH_QUERY,null);

        if(cursor.getCount()>0){
            cursor.close();
            db.close();
            db1.close();
            return 0;
        }else {

            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_AMOUNT, amount);
            values.put(COLUMN_CREATED_DATE, System.currentTimeMillis());
            db.insert(TABLE_RECORDS, null, values);
            db.close();
            cursor.close();
            db1.close();
            Intent intent=new Intent(BROADCAST_ID);
//            intent.setAction("ACTION_DB_CHANGED");
            context.sendBroadcast(intent);
//            listener.onDatabaseChanged();

            return 1;
        }
    }

    public DashboardInfo getDashboardInfo(){
        DashboardInfo info=new DashboardInfo();
        SQLiteDatabase database=getReadableDatabase();

        String GET_RECORD_COUNT="SELECT "+id+" FROM "+TABLE_RECORDS;
        Cursor cursor=database.rawQuery(GET_RECORD_COUNT,null);
        int count=cursor.getCount();
        info.setTotalRecords("Total Records : "+String.valueOf(count));

        String GET_TOTAL_AMOUNT="SELECT "+COLUMN_AMOUNT+" FROM "+TABLE_RECORDS;
        Cursor cursor1=database.rawQuery(GET_TOTAL_AMOUNT,null);
        float totalAmount=0;
        if(cursor1.getCount()>0){
            cursor1.moveToFirst();
            do{

                String amount=cursor1.getString(0);
                totalAmount=totalAmount+Float.parseFloat(amount);
                cursor1.moveToNext();
            }while (!cursor1.isAfterLast());
            info.setTotalAmount("Total amount : "+String.valueOf(totalAmount));
        }else {
            info.setTotalAmount("Total amount : 0");
        }

        cursor1.close();
        cursor.close();
        database.close();

        return info;

    }

    public List<MoneyDonor> getTopTen(){
        SQLiteDatabase db=getReadableDatabase();
        List<MoneyDonor> donorList=new ArrayList<>();

        String QUERY="SELECT * FROM "+TABLE_RECORDS+" ORDER BY "+COLUMN_AMOUNT+" DESC LIMIT 10";
        Cursor cursor=db.rawQuery(QUERY,null);

        if(cursor.getCount()<1){
            cursor.close();
            db.close();
            return null;
        }else {
            cursor.moveToFirst();
            do {
                MoneyDonor donor=new MoneyDonor();
                donor.setId(cursor.getInt(0));
                donor.setName(cursor.getString(1));
                donor.setAmount(cursor.getInt(2));
                donorList.add(donor);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }

        cursor.close();
        db.close();
        return donorList;
    }

    public int updateDonor(MoneyDonor donor){
        SQLiteDatabase db=getReadableDatabase();

        String CHECK_CLASH_QUERY="SELECT "+id+" FROM "+TABLE_RECORDS+
                " WHERE "+COLUMN_NAME+"='"+donor.getName()+"' AND "
                +COLUMN_AMOUNT+"="+donor.getAmount();

        Cursor cursor=db.rawQuery(CHECK_CLASH_QUERY,null);

        if(cursor.getCount()>0){
            cursor.close();
            db.close();
            return 0;
        }else {
            SQLiteDatabase db1=getWritableDatabase();
            String UPDATE_RECORD="UPDATE "+TABLE_RECORDS+
                    " SET "+COLUMN_NAME+"='"+donor.getName()+"', "+
                    COLUMN_AMOUNT+"="+donor.getAmount()+
                    " WHERE "+id+"="+donor.getId();
            db.execSQL(UPDATE_RECORD);
            db1.close();
        }

        cursor.close();
        db.close();
        return 1;
    }

    public void deleteRecord(int donorID){
        SQLiteDatabase db=getWritableDatabase();
        String DELETE_ITEM="DELETE FROM "+TABLE_RECORDS+" WHERE "+id+"="+donorID;
        db.execSQL(DELETE_ITEM);
        db.close();
    }

}
