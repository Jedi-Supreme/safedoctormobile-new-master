package com.safedoctor.safedoctor.localpersistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.safedoctor.safedoctor.Utils.AppConstants;


/**
 * Created by stevkky on 10/17/17.
 */

public class DBHelper //extends SQLiteOpenHelper
{
    Context c;

   /* public DBHelper()
    {

    }
    public DBHelper(Context c)
    {
       // super(c, AppConstants.DBNAME,);

    }
    */
    //@Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //super(context,DATABASE_NAME,null,1);
    }

   // @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
