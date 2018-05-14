package entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class Account implements DatabaseHelperFunctions{

    private int accountId;
    private String accountTitle;
    private String accountType;
    private Double amountOfMoney;

    public Account() {}

    public Account(int accountId, String accountTitle, String accountType, Double amountOfMoney) {
        this.accountId = accountId;
        this.accountTitle = accountTitle;
        this.accountType = accountType;
        this.amountOfMoney = amountOfMoney;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Account.COLUMN_TITLE, this.accountTitle);
        values.put(FinancialManager.Account.COLUMN_ACCOUNT_TYPE, this.accountType);
        values.put(FinancialManager.Account.COLUMN_AMOUNT_OF_MONEY, this.amountOfMoney);

        long newRowId = db.insert(FinancialManager.Account.TABLE_NAME, null, values);

    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.accountId);
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int accountId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT account_id AS _id, * FROM accounts WHERE account_id=?",
                new String[]{String.valueOf(accountId)});

        int idIndex = cursor.getColumnIndex(FinancialManager.Account._ID);
        int titleIndex = cursor.getColumnIndex(FinancialManager.Account.COLUMN_TITLE);
        int typeIndex = cursor.getColumnIndex(FinancialManager.Account.COLUMN_ACCOUNT_TYPE);
        int moneyIndex = cursor.getColumnIndex(FinancialManager.Account.COLUMN_AMOUNT_OF_MONEY);

        this.accountTitle = cursor.getString(titleIndex);
        this.accountType = cursor.getString(typeIndex);
        this.amountOfMoney = cursor.getDouble(moneyIndex);
        this.accountId = cursor.getInt(idIndex);

        cursor.close();
    }



    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(Double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }
}