package entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class EntryTagBinder implements DatabaseHelperFunctions{
    private int bindId;
    private Tag parentTag;
    private FinancialEntry parentEntry;

    public EntryTagBinder() {}

    public EntryTagBinder(Tag parentTag, FinancialEntry parentEntry) {
        this.parentTag = parentTag;
        this.parentEntry = parentEntry;
    }

    public EntryTagBinder(int bindId, Tag parentTag, FinancialEntry parentEntry) {
        this.bindId = bindId;
        this.parentTag = parentTag;
        this.parentEntry = parentEntry;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.EntryTagBinder.COLUMN_TAG_ID, this.parentTag.getTagId());
        values.put(FinancialManager.EntryTagBinder.COLUMN_ENTRY_ID, this.parentEntry.getEntryId());
        //values.put(FinancialManager.EntryTagBinder.COLUMN_CATEGORY_ID, this.parentTag.getParentCategory().getCategoryId());
        //values.put(FinancialManager.EntryTagBinder.COLUMN_ACCOUNT_ID, this.parentEntry.getParentAccount().getAccountId());

        long newRowId = db.insert(FinancialManager.EntryTagBinder.TABLE_NAME, null, values);

    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.bindId);
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT bind_id AS _id, * FROM entry_tag_binders " +
                "WHERE bind_id=?", new String[]{String.valueOf(entryId)});

        int tagIdIndex = cursor.getColumnIndex(FinancialManager.EntryTagBinder.COLUMN_TAG_ID);
        int entryIdIndex = cursor.getColumnIndex(FinancialManager.EntryTagBinder.COLUMN_ENTRY_ID);
        int bindIdIndex = cursor.getColumnIndex(FinancialManager.EntryTagBinder._ID);

        this.parentTag = new Tag();
        this.parentTag.readFromDatabase(dbHelper, cursor.getInt(tagIdIndex));

        //check how to change for accrual or expense

        this.parentEntry = new FinancialEntry();
        this.parentEntry.readFromDatabase(dbHelper, cursor.getInt(entryIdIndex));

        if(this.parentEntry.entryType.equals("ACCRUAL")) {
            this.parentEntry = new Accrual();
            this.parentEntry.readFromDatabase(dbHelper, cursor.getInt(entryIdIndex));
        }
        else if(this.parentEntry.entryType.equals("EXPENSE")) {
            this.parentEntry = new Expense();
            this.parentEntry.readFromDatabase(dbHelper, cursor.getInt(entryIdIndex));
        }
        else
        {
            //write code for some kind of exception
        }

        this.bindId = cursor.getInt(bindIdIndex);

        cursor.close();
    }

    public int getBindId() {
        return bindId;
    }

    public void setBindId(int bindId) {
        this.bindId = bindId;
    }

    public Tag getParentTag() {
        return parentTag;
    }

    public void setParentTag(Tag parentTag) {
        this.parentTag = parentTag;
    }

    public FinancialEntry getParentEntry() {
        return parentEntry;
    }

    public void setParentEntry(FinancialEntry parentEntry) {
        this.parentEntry = parentEntry;
    }
}