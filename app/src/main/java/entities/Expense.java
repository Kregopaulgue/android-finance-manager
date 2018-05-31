package entities;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import data.FinancialManager;
import data.FinancialManagerDbHelper;


public class Expense extends FinancialEntry implements Serializable{

    private Double moneySpent;
    private int importance;

    public Expense() {
    }

    public Expense(String title, String entryDate, Double moneySpent) {
        super(title, entryDate);
        this.entryType = "EXPENSE";
        this.moneySpent = moneySpent;
    }

    public Expense(String entryDate, String comment, String entryType, int importance,
                   Account parentAccount, String title, Double moneySpent) {
        super(entryDate, comment, entryType, parentAccount, title);
        this.moneySpent = moneySpent;
        this.importance = importance;
        this.entryType = "EXPENSE";
    }

    public Expense(int entryId, String entryDate, String comment, String entryType, int importance,
                   Account parentAccount, String title, Double moneySpent) {
        super(entryId, entryDate, comment, entryType, parentAccount, title);
        this.moneySpent = moneySpent;
        this.importance = importance;
        this.entryType = "EXPENSE";
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {
        super.writeToDatabase(dbHelper);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Expense.COLUMN_IMPORTANCE, this.importance);
        values.put(FinancialManager.Expense.COLUMN_MONEY_SPENT, this.moneySpent);
        values.put(FinancialManager.Expense.COLUMN_ENTRY_ID, this.entryId);

        db.insert(FinancialManager.Expense.TABLE_NAME, null, values);

    }

    @Override
    public void updateToDatabase(FinancialManagerDbHelper dbHelper, int rowId) {
        super.updateToDatabase(dbHelper, rowId);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Expense.COLUMN_IMPORTANCE, this.importance);
        values.put(FinancialManager.Expense.COLUMN_MONEY_SPENT, this.moneySpent);
        values.put(FinancialManager.Expense.COLUMN_ENTRY_ID, this.entryId);

        int amountOfUpdated = (int) db.update(FinancialManager.Expense.TABLE_NAME, values, "entry_id=?",
                new String[] {Integer.toString(rowId)});
    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.entryId);
    }

    
    public static ArrayList<Expense> readAllFromDatabase(FinancialManagerDbHelper dbHelper, int accId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id, * FROM expenses WHERE entry_id IN" +
                        "(SELECT entry_id FROM finance_entries WHERE account_id=?)",
                new String[]{Integer.toString(accId)});
        int idIndex = cursor.getColumnIndex(FinancialManager.Expense._ID);

        ArrayList<Expense> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            int billId = cursor.getInt(idIndex);
            Expense expenseToRead = new Expense();
            expenseToRead.readFromDatabase(dbHelper, billId);
            result.add(expenseToRead);
        }

        return result;
    }
    
    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {
        super.readFromDatabase(dbHelper, entryId);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id, * FROM expenses WHERE entry_id=?",
                new String[] {Integer.toString(entryId)});

        int moneySpentIndex = cursor.getColumnIndex(FinancialManager.Expense.COLUMN_MONEY_SPENT);
        int importanceIndex = cursor.getColumnIndex(FinancialManager.Expense.COLUMN_IMPORTANCE);

        while(cursor.moveToNext()) {
            this.moneySpent = cursor.getDouble(moneySpentIndex);
            this.importance = cursor.getInt(importanceIndex);
        }

        cursor.close();
    }

    @Override
    public void deleteFromDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "entry_id=?";
        String[] whereArgs = new String[] { String.valueOf(this.entryId) };
        db.delete(FinancialManager.FinancialEntry.TABLE_NAME, whereClause, whereArgs);
    }

    public void readLastFromDatabase(FinancialManagerDbHelper dbHelper, int accountId) {
        readLastExpenseFromDatabase(dbHelper, accountId);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT entry_id AS _id, * FROM expenses WHERE entry_id=?",
                new String[] {Integer.toString(entryId)});

        int moneySpentIndex = cursor.getColumnIndex(FinancialManager.Expense.COLUMN_MONEY_SPENT);
        int importanceIndex = cursor.getColumnIndex(FinancialManager.Expense.COLUMN_IMPORTANCE);

        while(cursor.moveToNext()) {
            this.moneySpent = cursor.getDouble(moneySpentIndex);
            this.importance = cursor.getInt(importanceIndex);
        }

        cursor.close();
    }

    public static ArrayList<Expense> searchExpensesInDatabaseByCategoryAndDate(FinancialManagerDbHelper dbHelper, String firstDate,
                                                                               String secondDate, int accountId, int categoryId) {
        ArrayList<FinancialEntry> resultEntries = FinancialEntry.searchInDatabaseByCategoryAndDate(dbHelper, firstDate,
                secondDate, accountId, categoryId);
        return fromEntriesToExpenses(dbHelper, resultEntries);
    }

    public static ArrayList<Expense> searchExpensesInDatabaseByDay(FinancialManagerDbHelper dbHelper, String dayDate, int accountId) {
        ArrayList<FinancialEntry> resultEntries = FinancialEntry.searchInDatabaseByDay(dbHelper, dayDate, accountId);
        return fromEntriesToExpenses(dbHelper, resultEntries);
    }

    public static ArrayList<Expense> searchExpensesInDatabaseByDates(FinancialManagerDbHelper dbHelper,
                                                                    String firstDate, String secondDate, int accountId) {
        ArrayList<FinancialEntry> resultEntries = FinancialEntry.searchInDatabaseByDates(dbHelper, firstDate, secondDate, accountId);
        return fromEntriesToExpenses(dbHelper, resultEntries);
    }

    public static ArrayList<Expense> searchExpensesInDatabaseByTag(FinancialManagerDbHelper dbHelper, String tagToSearch) {
        ArrayList<FinancialEntry> resultEntries = FinancialEntry.searchInDatabaseByTag(dbHelper, tagToSearch);
        return fromEntriesToExpenses(dbHelper, resultEntries);
    }

    public static ArrayList<Expense> searchExpensesInDatabaseByTitle(FinancialManagerDbHelper dbHelper, String title, int accountId) {
        ArrayList<FinancialEntry> resultEntries = FinancialEntry.searchInDatabaseByTitle(dbHelper, title, accountId);
        return fromEntriesToExpenses(dbHelper, resultEntries);
    }

    private static ArrayList<Expense> fromEntriesToExpenses(FinancialManagerDbHelper dbHelper,
                                                    ArrayList<FinancialEntry> resultEntries) {
        ArrayList<Expense> resultExpenses = new ArrayList<>();

        for(int i = 0; i < resultEntries.size(); i++) {
            FinancialEntry financialEntry = resultEntries.get(i);
            if(financialEntry.getEntryType().equals("EXPENSE")) {
                Expense tempExpense = new Expense();
                tempExpense.readFromDatabase(dbHelper, financialEntry.entryId);
                resultExpenses.add(tempExpense);
            }
        }

        return resultExpenses;
    }



    public Double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(Double moneySpent) {
        this.moneySpent = moneySpent;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }
}