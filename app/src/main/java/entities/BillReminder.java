package entities;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class BillReminder implements DatabaseHelperFunctions{

    private int billReminderId;
    private Double sumToPay;
    private String billDescription;
    private String dateTimeToPay;
    private Account parentAccount;

    public BillReminder() {}

    public BillReminder(Double sumToPay, String billDescription, String dateTimeToPay, Account parentAccount) {
        this.sumToPay = sumToPay;
        this.billDescription = billDescription;
        this.dateTimeToPay = dateTimeToPay;
        this.parentAccount = parentAccount;
    }

    public BillReminder(int billReminderId, Double sumToPay, String billDescription,
                        String dateTimeToPay, Account parentAccount) {
        this.billReminderId = billReminderId;
        this.sumToPay = sumToPay;
        this.billDescription = billDescription;
        this.dateTimeToPay = dateTimeToPay;
        this.parentAccount = parentAccount;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.BillReminder.COLUMN_SUM_TO_PAY, this.sumToPay);
        values.put(FinancialManager.BillReminder.COLUMN_BILL_DESCRIPTION, this.billDescription);
        values.put(FinancialManager.BillReminder.COLUMN_DATE_TIME_TO_PAY, this.dateTimeToPay);
        values.put(FinancialManager.BillReminder.COLUMN_ACCOUNT_ID, this.parentAccount.getAccountId());

        long newRowId = db.insert(FinancialManager.BillReminder.TABLE_NAME, null, values);

    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.billReminderId);
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT bill_reminder_id AS _id, * FROM bill_reminders " +
                "WHERE bill_reminder_id=?", new String[]{String.valueOf(entryId)});

        int sumToPayIndex = cursor.getColumnIndex(FinancialManager.BillReminder.COLUMN_SUM_TO_PAY);
        int billDescriptionIndex = cursor.getColumnIndex(FinancialManager.BillReminder.COLUMN_BILL_DESCRIPTION);
        int dateTimeToPayIndex = cursor.getColumnIndex(FinancialManager.BillReminder.COLUMN_DATE_TIME_TO_PAY);
        int accountIdIndex = cursor.getColumnIndex(FinancialManager.BillReminder.COLUMN_ACCOUNT_ID);
        int billDescriptionIdIndex = cursor.getColumnIndex(FinancialManager.BillReminder._ID);

        this.sumToPay = cursor.getDouble(sumToPayIndex);
        this.billDescription = cursor.getString(billDescriptionIndex);
        this.dateTimeToPay = cursor.getString(dateTimeToPayIndex);

        this.parentAccount = new Account();
        this.parentAccount.readFromDatabase(dbHelper, cursor.getInt(accountIdIndex));

        this.billReminderId = cursor.getInt(billDescriptionIdIndex);

        cursor.close();
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

    public String getBillDescription() {
        return billDescription;
    }

    public void setBillDescription(String billDescription) {
        this.billDescription = billDescription;
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