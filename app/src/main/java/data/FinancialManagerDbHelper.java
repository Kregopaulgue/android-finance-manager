package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

import entities.Category;

public class FinancialManagerDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FinancialManagerDbHelper.class.getSimpleName();


    //database file name
    private static final String DATABASE_NAME = "financial_manager.db";

    //database's version
    private static final int DATABASE_VERSION = 1;

    public FinancialManagerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //is called during opening database
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SqlQueries.SQL_CREATE_TABLE_ACCOUNTS);
        db.execSQL(SqlQueries.SQL_CREATE_TABLE_CATEGORY);
        db.execSQL(SqlQueries.SQL_CREATE_TABLE_TAGS);
        db.execSQL(SqlQueries.SQL_CREATE_FINANCE_ENTRIES);
        db.execSQL(SqlQueries.SQL_CREATE_TABLE_ACCRUALS);
        db.execSQL(SqlQueries.SQL_CREATE_TABLE_EXPENSES);
        db.execSQL(SqlQueries.SQL_CREATE_TABLES_ENTRY_TAG_BINDERS);
        db.execSQL(SqlQueries.SQL_CREATE_TABLE_BILL_REMINDERS);
        db.execSQL(SqlQueries.SQL_CREATE_TABLE_GOALS);
        db.execSQL(SqlQueries.SQL_CREATE_TABLE_CONSTRAINTS);

        db.execSQL(SqlQueries.SQL_INSERT_CATEGORY_FOOD);
        db.execSQL(SqlQueries.SQL_INSERT_CATEGORY_SERVICE);
        db.execSQL(SqlQueries.SQL_INSERT_CATEGORY_APPLIANCE);
        db.execSQL(SqlQueries.SQL_INSERT_CATEGORY_CLOTH);
        db.execSQL(SqlQueries.SQL_INSERT_CATEGORY_ACCRUAL);
        db.execSQL(SqlQueries.SQL_INSERT_CATEGORY_OTHER);

        db.execSQL(SqlQueries.SQL_TRIGGER_INSERT_EXPENSE);
        db.execSQL(SqlQueries.SQL_TRIGGER_INSERT_ACCRUAL);
        db.execSQL(SqlQueries.SQL_TRIGGER_DELETE_EXPENSE);
        db.execSQL(SqlQueries.SQL_TRIGGER_DELETE_ACCRUAL);
    }

    //is called during updating database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}