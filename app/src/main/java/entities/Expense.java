package entities;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import data.FinancialManager;
import data.FinancialManagerDbHelper;

public class Expense extends FinancialEntry {

    private Double moneySpent;

    public Expense() {
    }

    public Expense(int entryId, String entryDate, String comment, String entryType,
                   Account parentAccount, Double moneySpent) {
        super(entryId, entryDate, comment, entryType, parentAccount);
        this.moneySpent = moneySpent;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {
        super.writeToDatabase(dbHelper);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Expense.COLUMN_MONEY_SPENT, this.moneySpent);
        values.put(FinancialManager.Expense.COLUMN_ENTRY_ID, this.entryId);

        long newRowId = db.insert(FinancialManager.Expense.TABLE_NAME, null, values);

    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.entryId);
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {
        super.readFromDatabase(dbHelper, entryId);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id, * FROM expenses WHERE entry_id=?",
                new String[]{String.valueOf(entryId)});

        int moneySpentIndex = cursor.getColumnIndex(FinancialManager.Expense.COLUMN_MONEY_SPENT);

        this.moneySpent = cursor.getDouble(moneySpentIndex);

        cursor.close();
    }

    public Double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(Double moneySpent) {
        this.moneySpent = moneySpent;
    }
}