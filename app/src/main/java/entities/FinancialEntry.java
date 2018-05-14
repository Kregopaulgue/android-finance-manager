package entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.format.DateTimeFormatter;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class FinancialEntry implements DatabaseHelperFunctions{

    protected int entryId;
    protected String entryDate;
    protected String comment;
    protected String entryType;
    protected Account parentAccount;

    public FinancialEntry(){}

    public FinancialEntry(int entryId, String entryDate, String comment,
                          String entryType, Account parentAccount) {
        this.entryId = entryId;
        this.entryDate = entryDate;
        this.comment = comment;
        this.parentAccount = parentAccount;
        this.entryType = entryType;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.FinancialEntry.COLUMN_DATE_TIME, this.entryDate);
        values.put(FinancialManager.FinancialEntry.COLUMN_COMMENT, this.comment);
        values.put(FinancialManager.FinancialEntry.COLUMN_ACCOUNT_ID, this.parentAccount.getAccountId());
        values.put(FinancialManager.FinancialEntry.COLUMN_ENTRY_TYPE, this.entryType);

        long newRowId = db.insert(FinancialManager.FinancialEntry.TABLE_NAME, null, values);

    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.entryId);
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id, * FROM financial_entries WHERE entry_id=?",
                new String[]{String.valueOf(entryId)});

        int idIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry._ID);
        int dateIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry.COLUMN_DATE_TIME);
        int commentIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry.COLUMN_COMMENT);
        int accountIdIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry.COLUMN_ACCOUNT_ID);
        int entryTypeIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry.COLUMN_ENTRY_TYPE);

        this.entryDate = cursor.getString(dateIndex);
        this.comment = cursor.getString(commentIndex);
        this.entryType = cursor.getString(entryTypeIndex);
        this.parentAccount = new Account();
        this.parentAccount.readFromDatabase(dbHelper, cursor.getInt(accountIdIndex));
        this.entryId = cursor.getInt(idIndex);

        cursor.close();
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Account getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(Account parentAccount) {
        this.parentAccount = parentAccount;
    }
}