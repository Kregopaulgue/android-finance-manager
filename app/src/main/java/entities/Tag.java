package entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class Tag implements DatabaseHelperFunctions{

    private int tagId;
    private String tagTitle;
    private String isMadeByUser;
    private Category parentCategory;

    public Tag() {

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

        long newRowId = db.insert(FinancialManager.Category.TABLE_NAME, null, values);

    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.tagId);
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

        this.tagTitle = cursor.getString(titleIndex);
        this.isMadeByUser = cursor.getString(typeIndex);
        this.tagId = cursor.getInt(idIndex);
        this.parentCategory = new Category();
        this.parentCategory.readFromDatabase(dbHelper, cursor.getInt(categoryIdIndex));

        cursor.close();
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