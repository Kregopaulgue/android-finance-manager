package entities;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class BillReminder implements DatabaseHelperFunctions{

    private int billReminderId;
    private Double sumToPay;
    private String billTitle;
    private String dateTimeToPay;
    private Account parentAccount;
    private String isPaid;

    public BillReminder() {}

    public BillReminder(Double sumToPay, String billTitle, String dateTimeToPay, Account parentAccount, String isPaid) {
        this.sumToPay = sumToPay;
        this.billTitle = billTitle;
        this.dateTimeToPay = dateTimeToPay;
        this.parentAccount = parentAccount;
        this.isPaid = isPaid;
    }

    public BillReminder(int billReminderId, Double sumToPay, String billTitle,
                        String dateTimeToPay, Account parentAccount) {
        this.billReminderId = billReminderId;
        this.sumToPay = sumToPay;
        this.billTitle = billTitle;
        this.dateTimeToPay = dateTimeToPay;
        this.parentAccount = parentAccount;
        this.isPaid = isPaid;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.BillReminder.COLUMN_SUM_TO_PAY, this.sumToPay);
        values.put(FinancialManager.BillReminder.COLUMN_BILL_TITLE, this.billTitle);
        values.put(FinancialManager.BillReminder.COLUMN_DATE_TIME_TO_PAY, this.dateTimeToPay);
        values.put(FinancialManager.BillReminder.COLUMN_ACCOUNT_ID, this.parentAccount.getAccountId());
        values.put(FinancialManager.BillReminder.COLUMN_IS_PAID, this.parentAccount.getAccountId());

        this.billReminderId = (int) db.insert(FinancialManager.BillReminder.TABLE_NAME, null, values);

    }

    @Override
    public void updateToDatabase(FinancialManagerDbHelper dbHelper, int rowId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.BillReminder.COLUMN_SUM_TO_PAY, this.sumToPay);
        values.put(FinancialManager.BillReminder.COLUMN_BILL_TITLE, this.billTitle);
        values.put(FinancialManager.BillReminder.COLUMN_DATE_TIME_TO_PAY, this.dateTimeToPay);
        values.put(FinancialManager.BillReminder.COLUMN_ACCOUNT_ID, this.parentAccount.getAccountId());

        long amountOfUpdates = db.update(FinancialManager.BillReminder.TABLE_NAME, values, "bill_id=?",
                new String[] {Integer.toString(rowId)});
    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.billReminderId);
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT bill_id AS _id, * FROM bill_reminders " +
                "WHERE bill_id=?", new String[]{String.valueOf(entryId)});

        int sumToPayIndex = cursor.getColumnIndex(FinancialManager.BillReminder.COLUMN_SUM_TO_PAY);
        int billTitleIndex = cursor.getColumnIndex(FinancialManager.BillReminder.COLUMN_BILL_TITLE);
        int dateTimeToPayIndex = cursor.getColumnIndex(FinancialManager.BillReminder.COLUMN_DATE_TIME_TO_PAY);
        int accountIdIndex = cursor.getColumnIndex(FinancialManager.BillReminder.COLUMN_ACCOUNT_ID);
        int billTitleIdIndex = cursor.getColumnIndex(FinancialManager.BillReminder._ID);

        cursor.moveToNext();
        this.sumToPay = cursor.getDouble(sumToPayIndex);
        this.billTitle = cursor.getString(billTitleIndex);
        this.dateTimeToPay = cursor.getString(dateTimeToPayIndex);

        this.parentAccount = new Account();
        this.parentAccount.readFromDatabase(dbHelper, cursor.getInt(accountIdIndex));

        this.billReminderId = cursor.getInt(billTitleIdIndex);

        cursor.close();
    }

    public static ArrayList<BillReminder> readAllFromDatabase(FinancialManagerDbHelper dbHelper, int accId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT bill_id AS _id, * FROM bill_reminders WHERE account_id=?",
                new String[] {Integer.toString(accId)});
        int idIndex = cursor.getColumnIndex(FinancialManager.BillReminder._ID);

        ArrayList<BillReminder> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            int billId = cursor.getInt(idIndex);
            BillReminder billReminderToRead = new BillReminder();
            billReminderToRead.readFromDatabase(dbHelper, billId);
            result.add(billReminderToRead);
        }

        return result;
    }

    @Override
    public void deleteFromDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "bill_id=?";
        String[] whereArgs = new String[] { String.valueOf(this.billReminderId) };
        db.delete(FinancialManager.BillReminder.TABLE_NAME, whereClause, whereArgs);
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public int getBillReminderId() {
        return billReminderId;
    }

    public void setBillReminderId(int billReminderId) {
        this.billReminderId = billReminderId;
    }

    public Double getSumToPay() {
        return sumToPay;
    }

    public void setSumToPay(Double sumToPay) {
        this.sumToPay = sumToPay;
    }

    public String getBillTitle() {
        return billTitle;
    }

    public void setBillTitle(String billTitle) {
        this.billTitle = billTitle;
    }

    public String getDateTimeToPay() {
        return dateTimeToPay;
    }

    public void setDateTimeToPay(String dateTimeToPay) {
        this.dateTimeToPay = dateTimeToPay;
    }

    public Account getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(Account parentAccount) {
        this.parentAccount = parentAccount;
    }
}