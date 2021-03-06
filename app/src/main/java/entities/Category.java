package entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class Category implements DatabaseHelperFunctions, Serializable{

    private int categoryId;
    private String categoryTitle;

    public Category() {}

    public Category(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public Category(int categoryId, String categoryTitle) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Category.COLUMN_TITLE, this.categoryTitle);

        this.categoryId = (int) db.insert(FinancialManager.Category.TABLE_NAME, null, values);

    }

    @Override
    public void updateToDatabase(FinancialManagerDbHelper dbHelper, int rowId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Category.COLUMN_TITLE, this.categoryTitle);

        long amountOfUpdated = db.update(FinancialManager.Category.TABLE_NAME, values, "category_id=?",
                new String[] {});
    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.categoryId);
    }

    public static ArrayList<Category> readAllFromDatabase(FinancialManagerDbHelper dbHelper, int accId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT category_id AS _id FROM categories", null);
        int idIndex = cursor.getColumnIndex(FinancialManager.Category._ID);

        ArrayList<Category> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            int billId = cursor.getInt(idIndex);
            Category categoryToRead = new Category();
            categoryToRead.readFromDatabase(dbHelper, billId);
            result.add(categoryToRead);
        }

        return result;
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT category_id AS _id, * FROM categories WHERE category_id=?",
                new String[]{String.valueOf(entryId)});

        int idIndex = cursor.getColumnIndex(FinancialManager.Category._ID);
        int titleIndex = cursor.getColumnIndex(FinancialManager.Category.COLUMN_TITLE);

        cursor.moveToNext();
        this.categoryTitle = cursor.getString(titleIndex);
        this.categoryId = cursor.getInt(idIndex);

        cursor.close();
    }

    @Override
    public void deleteFromDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "category_id=?";
        String[] whereArgs = new String[] { String.valueOf(this.categoryId) };
        db.delete(FinancialManager.Category.TABLE_NAME, whereClause, whereArgs);
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}