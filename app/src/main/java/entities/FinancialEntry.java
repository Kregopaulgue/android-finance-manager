package entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class FinancialEntry implements DatabaseHelperFunctions, Serializable{

    protected int entryId;
    protected String title;
    protected String entryDate;
    protected String comment;
    protected String entryType;
    protected Account parentAccount;

    public FinancialEntry(){}

    public FinancialEntry(String title, String entryDate) {
        this.title = title;
        this.entryDate = entryDate;
    }

    public FinancialEntry(String entryDate, String comment, String entryType,
                          Account parentAccount, String title) {
        this.entryDate = entryDate;
        this.comment = comment;
        this.entryType = entryType;
        this.parentAccount = parentAccount;
        this.title = title;
    }

    public FinancialEntry(int entryId, String entryDate, String comment,
                          String entryType, Account parentAccount, String title) {
        this.entryId = entryId;
        this.entryDate = entryDate;
        this.comment = comment;
        this.parentAccount = parentAccount;
        this.entryType = entryType;
        this.title = title;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.FinancialEntry.COLUMN_TITLE, this.title);
        values.put(FinancialManager.FinancialEntry.COLUMN_DATE_TIME, this.entryDate);
        values.put(FinancialManager.FinancialEntry.COLUMN_COMMENT, this.comment);
        values.put(FinancialManager.FinancialEntry.COLUMN_ACCOUNT_ID, this.parentAccount.getAccountId());
        values.put(FinancialManager.FinancialEntry.COLUMN_ENTRY_TYPE, this.entryType);

        this.entryId = (int) db.insert(FinancialManager.FinancialEntry.TABLE_NAME, null, values);


    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.entryId);
    }

    @Override
    public void updateToDatabase(FinancialManagerDbHelper dbHelper, int rowId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.FinancialEntry.COLUMN_TITLE, this.title);
        values.put(FinancialManager.FinancialEntry.COLUMN_DATE_TIME, this.entryDate);
        values.put(FinancialManager.FinancialEntry.COLUMN_COMMENT, this.comment);
        values.put(FinancialManager.FinancialEntry.COLUMN_ACCOUNT_ID, this.parentAccount.getAccountId());
        values.put(FinancialManager.FinancialEntry.COLUMN_ENTRY_TYPE, this.entryType);

        int amountOfUpdated = db.update(FinancialManager.FinancialEntry.TABLE_NAME, values, "entry_id=?",
                new String[] {Integer.toString(rowId)});
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id, * FROM finance_entries WHERE entry_id=?",
                new String[]{Integer.toString(entryId)});

        int titleIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry.COLUMN_TITLE);
        int idIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry._ID);
        int dateIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry.COLUMN_DATE_TIME);
        int commentIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry.COLUMN_COMMENT);
        int accountIdIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry.COLUMN_ACCOUNT_ID);
        int entryTypeIndex = cursor.getColumnIndex(FinancialManager.FinancialEntry.COLUMN_ENTRY_TYPE);

        cursor.moveToNext();
        this.title = cursor.getString(titleIndex);
        this.entryDate = cursor.getString(dateIndex);
        this.comment = cursor.getString(commentIndex);
        this.entryType = cursor.getString(entryTypeIndex);
        this.parentAccount = new Account();
        this.parentAccount.readFromDatabase(dbHelper, cursor.getInt(accountIdIndex));
        this.entryId = cursor.getInt(idIndex);

        cursor.close();
    }

    @Override
    public void deleteFromDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "entry_id=?";
        String[] whereArgs = new String[] { String.valueOf(this.entryId) };
        db.delete(FinancialManager.FinancialEntry.TABLE_NAME, whereClause, whereArgs);
    }

    public static ArrayList<FinancialEntry> searchInDatabaseByDay(FinancialManagerDbHelper dbHelper, String dayDate) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id, * FROM finance_entries WHERE date_time=?",
                new String[]{dayDate});
        return searchInDatabase(dbHelper, cursor);
    }

    public static ArrayList<FinancialEntry> searchInDatabaseByDates(FinancialManagerDbHelper dbHelper,
                                                                    String firstDate, String secondDate) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id, * FROM finance_entries WHERE date(?) <= date(date_time) AND date(date_time) <= date(?)",
                new String[]{firstDate, secondDate});
        return searchInDatabase(dbHelper, cursor);
    }

    public static ArrayList<FinancialEntry> searchInDatabaseByTag(FinancialManagerDbHelper dbHelper,
                                                                    String searchTag) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id FROM entry_tag_binders WHERE tag_id " +
                        "IN(SELECT tag_id FROM tags WHERE tags.title=?)",
                new String[]{searchTag});
        return searchInDatabase(dbHelper, cursor);
    }

    public static ArrayList<FinancialEntry> searchInDatabaseByTitle(FinancialManagerDbHelper dbHelper,
                                                                  String searchTitle) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id FROM finance_entries WHERE title=?",
                new String[]{searchTitle});
        return searchInDatabase(dbHelper, cursor);
    }

    private static ArrayList<FinancialEntry> searchInDatabase(FinancialManagerDbHelper dbHelper, Cursor cursor) {
        int idIndex = cursor.getColumnIndex(FinancialManager.Expense._ID);

        ArrayList<FinancialEntry> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            int entryId = cursor.getInt(idIndex);
            FinancialEntry entryToAdd = new FinancialEntry();
            entryToAdd.readFromDatabase(dbHelper, entryId);
            result.add(entryToAdd);
        }

        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
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