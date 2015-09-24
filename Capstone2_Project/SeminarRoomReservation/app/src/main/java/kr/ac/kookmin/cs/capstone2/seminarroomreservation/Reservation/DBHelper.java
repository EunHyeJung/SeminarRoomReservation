package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "textdb";
    public static final String TABLE_PRO = "tblprovinces";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_RPO = "CREATE TABLE IF NOT EXISTS "+ TABLE_PRO+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, pname TEXT NULL UNIQUE)";

    public static final String DELETE_PRO="DROP TABLE IF EXISTS " + TABLE_PRO;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    public void onCreate(SQLiteDatabase db) {
        // Create the table
        db.execSQL(CREATE_RPO);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop older table if existed
        db.execSQL(DELETE_PRO);
        //Create tables again
        onCreate(db);

    }


    public void insertProvince(String pname) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;


        try

        {
            values = new ContentValues();
            values.put("pname", pname);
            // Insert Row
            db.insert(TABLE_PRO, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();


        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

        finally

        {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }


    public ArrayList<String> getAllProvinces(){

        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();


        try
        {

            String selectQuery = "SELECT * FROM "+ TABLE_PRO;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0)

            {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String pname= cursor.getString(cursor.getColumnIndex("pname"));
                    list.add(pname);

                }


            }
            db.setTransactionSuccessful();

        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        finally
        {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;


    }


}