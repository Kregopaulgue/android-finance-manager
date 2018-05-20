package entities;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import data.FinancialManager;
import data.FinancialManagerDbHelper;

public class Accrual extends FinancialEntry implements Serializable{

    private String source;
    private Double moneyGained;

    public Accrual() {
    }

    public Accrual(String entryDate, String comment, String entryType, Account parentAccount,
                   String title, String source, Double moneyGained) {
        super(entryDate, comment, entryType, parentAccount, title);
        this.source = source;
        this.moneyGained = moneyGained;
    }

    public Accrual(int entryId, String entryDate, String comment, String entryType,
                   Account parentAccount, String title, String source, Double moneyGained) {
        super(entryId, entryDate, comment, entryType, parentAccount, title);
        this.source = source;
        this.moneyGained = moneyGained;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {
        super.writeToDatabase(dbHelper);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Accrual.COLUMN_MONEY_GAINED, this.moneyGained);
        values.put(FinancialManager.Accrual.COLUMN_ENTRY_ID, this.entryId);

        this.entryId = (int) db.insert(FinancialManager.Accrual.TABLE_NAME, null, values);

    }

    public static ArrayList<Accrual> readAllFromDatabase(FinancialManagerDbHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id FROM accruals;", null);

        int accrualIdIndex = cursor.getColumnIndex(FinancialManager.Accrual._ID);

        ArrayList<Accrual> result = new ArrayList<>();

        while(cursor.moveToNext())
        {
            int currentId = cursor.getInt(accrualIdIndex);
            Accrual accrualToRead = new Accrual();
            accrualToRead.readFromDatabase(dbHelper, currentId);
            result.add(accrualToRead);
        }

        cursor.close();
        return result;
    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.entryId);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id, * FROM accruals WHERE entry_id=?",
                new String[]{String.valueOf(entryId)});

        int moneyGainedIndex = cursor.getColumnIndex(FinancialManager.Accrual.COLUMN_MONEY_GAINED);
        int sourceIndex = cursor.getColumnIndex(FinancialManager.Accrual.COLUMN_SOURCE);

        cursor.moveToNext();
        this.moneyGained = cursor.getDouble(moneyGainedIndex);
        this.source = cursor.getString(sourceIndex);

        cursor.close();
    }

    @Override
    public void updateToDatabase(FinancialManagerDbHelper dbHelper, int rowId) {
        super.updateToDatabase(dbHelper, rowId);
        super.updateToDatabase(dbHelper, rowId);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Accrual.COLUMN_MONEY_GAINED, this.moneyGained);
        values.put(FinancialManager.Accrual.COLUMN_ENTRY_ID, this.entryId);

        int amountOfUpdated = db.update(FinancialManager.Accrual.TABLE_NAME, values, "entry_id=?",
                new String[] {Integer.toString(rowId)});
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {
        super.readFromDatabase(dbHelper, entryId);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id, * FROM accruals WHERE entry_id=?",
                new String[]{String.valueOf(entryId)});

        int moneyGainedIndex = cursor.getColumnIndex(FinancialManager.Accrual.COLUMN_MONEY_GAINED);
        int sourceIndex = cursor.getColumnIndex(FinancialManager.Accrual.COLUMN_SOURCE);

        cursor.moveToNext();
        this.moneyGained = cursor.getDouble(moneyGainedIndex);
        this.source = cursor.getString(sourceIndex);

        cursor.close();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getMoneyGained() {
        return moneyGained;
    }

    public void setMoneyGained(Double moneyGained) {
        this.moneyGained = moneyGained;
    }
}