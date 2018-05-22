package entities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class Tag implements DatabaseHelperFunctions, Serializable{

    private int tagId;
    private String tagTitle;
    private String isMadeByUser;
    private Category parentCategory;

    public Tag() {

    }

    public Tag(String tagTitle, String isMadeByUser, Category parentCategory) {
        this.tagTitle = tagTitle;
        this.isMadeByUser = isMadeByUser;
        this.parentCategory = parentCategory;
    }

    public Tag(int tagId, String tagTitle, String isMadeByUser, Category parentCategory) {
        this.tagId = tagId;
        this.tagTitle = tagTitle;
        this.isMadeByUser = isMadeByUser;
        this.parentCategory = parentCategory;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Tag.COLUMN_TITLE, this.tagTitle);
        values.put(FinancialManager.Tag.COLUMN_TAG_TYPE, this.isMadeByUser);
        values.put(FinancialManager.Tag.COLUMN_CATEGORY_ID, this.parentCategory.getCategoryId());

        this.tagId = (int)db.insert(FinancialManager.Tag.TABLE_NAME, null, values);

    }

    @Override
    public void updateToDatabase(FinancialManagerDbHelper dbHelper, int rowId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Tag.COLUMN_TITLE, this.tagTitle);
        values.put(FinancialManager.Tag.COLUMN_TAG_TYPE, this.isMadeByUser);
        values.put(FinancialManager.Tag.COLUMN_CATEGORY_ID, this.parentCategory.getCategoryId());

        long amountOfUpdated = db.update(FinancialManager.Tag.TABLE_NAME, values, "tag_id=?",
                new String[]{Integer.toString(rowId)});
    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.tagId);
    }

    public static ArrayList<Tag> readAllFromDatabaseWhereEntry(FinancialManagerDbHelper dbHelper, int entryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT tag_id AS _id FROM tags WHERE tag_id IN" +
                        "(SELECT tag_id FROM entry_tag_binders WHERE entry_id=?)",
                new String[]{Integer.toString(entryId)});
        int idIndex = cursor.getColumnIndex(FinancialManager.Tag._ID);

        ArrayList<Tag> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            int tagId = cursor.getInt(idIndex);
            Tag tagToRead = new Tag();
            tagToRead.readFromDatabase(dbHelper, tagId);
            result.add(tagToRead);
        }

        return result;
    }
    
    public static ArrayList<Tag> readAllFromDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT tag_id AS _id FROM tags;", null);
        int idIndex = cursor.getColumnIndex(FinancialManager.Tag._ID);

        ArrayList<Tag> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            int billId = cursor.getInt(idIndex);
            Tag billReminderToRead = new Tag();
            billReminderToRead.readFromDatabase(dbHelper, billId);
            result.add(billReminderToRead);
        }

        return result;
    }

    public static ArrayList<Tag> readAllFromDatabaseWhereCategory(FinancialManagerDbHelper dbHelper,
                                                                  int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT tag_id AS _id FROM tags WHERE category_id=?",
                new String[] {Integer.toString(categoryId)});
        int idIndex = cursor.getColumnIndex(FinancialManager.Tag._ID);

        ArrayList<Tag> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            int billId = cursor.getInt(idIndex);
            Tag billReminderToRead = new Tag();
            billReminderToRead.readFromDatabase(dbHelper, billId);
            result.add(billReminderToRead);
        }

        return result;
    }
    
    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT tag_id AS _id, * FROM tags WHERE tag_id=?",
                new String[]{String.valueOf(entryId)});

        int idIndex = cursor.getColumnIndex(FinancialManager.Tag._ID);
        int titleIndex = cursor.getColumnIndex(FinancialManager.Tag.COLUMN_TITLE);
        int typeIndex = cursor.getColumnIndex(FinancialManager.Tag.COLUMN_TAG_TYPE);
        int categoryIdIndex = cursor.getColumnIndex(FinancialManager.Tag.COLUMN_CATEGORY_ID);

        cursor.moveToNext();
        this.tagTitle = cursor.getString(titleIndex);
        this.isMadeByUser = cursor.getString(typeIndex);
        this.tagId = cursor.getInt(idIndex);
        this.parentCategory = new Category();
        this.parentCategory.readFromDatabase(dbHelper, cursor.getInt(categoryIdIndex));

        cursor.close();
    }

    @Override
    public void deleteFromDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "tag_id=?";
        String[] whereArgs = new String[] { String.valueOf(this.tagId) };
        db.delete(FinancialManager.Tag.TABLE_NAME, whereClause, whereArgs);
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public String isMadeByUser() {
        return isMadeByUser;
    }

    public void setMadeByUser(String madeByUser) {
        isMadeByUser = madeByUser;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
}