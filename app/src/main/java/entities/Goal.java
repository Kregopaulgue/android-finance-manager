package entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class Goal implements DatabaseHelperFunctions{

    private int goalId;
    private Double sumToReach;
    private Double currentSum;
    private String targetTitle;
    private String isReached;
    private Account parentAccount;

    public Goal() {
    }

    public Goal(Double sumToReach, Double currentSum, String targetTitle, String isReached, Account parentAccount) {
        this.sumToReach = sumToReach;
        this.currentSum = currentSum;
        this.targetTitle = targetTitle;
        this.isReached = isReached;
        this.parentAccount = parentAccount;
    }

    public Goal(int goalId, Double sumToReach, Double currentSum, String targetTitle,
                String isReached, Account parentAccount) {
        this.goalId = goalId;
        this.sumToReach = sumToReach;
        this.currentSum = currentSum;
        this.targetTitle = targetTitle;
        this.isReached = isReached;
        this.parentAccount = parentAccount;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Goal.COLUMN_SUM_TO_REACH, this.sumToReach);
        values.put(FinancialManager.Goal.COLUMN_CURRENT_SUM, this.currentSum);
        values.put(FinancialManager.Goal.COLUMN_TARGET_DESCRIPTION, this.targetTitle);
        values.put(FinancialManager.Goal.COLUMN_IS_REACHED, this.isReached);
        values.put(FinancialManager.Goal.COLUMN_ACCOUNT_ID, this.parentAccount.getAccountId());

        long newRowId = db.insert(FinancialManager.Goal.TABLE_NAME, null, values);

    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.goalId);
    }

    
    public static ArrayList<Goal> readAllFromDatabase(FinancialManagerDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT goal_id AS _id FROM goals;", null);
        int idIndex = cursor.getColumnIndex(FinancialManager.Goal._ID);

        ArrayList<Goal> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            int billId = cursor.getInt(idIndex);
            Goal goalToRead = new Goal();
            goalToRead.readFromDatabase(dbHelper, billId);
            result.add(goalToRead);
        }

        return result;
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT goal_id AS _id, * FROM goals" +
                " WHERE goal_id = ?", new String[]{String.valueOf(entryId)});

        int currentSumIndex = cursor.getColumnIndex(FinancialManager.Goal.COLUMN_CURRENT_SUM);
        int targetTitleIndex = cursor.getColumnIndex(FinancialManager.Goal.COLUMN_TARGET_DESCRIPTION);
        int isReachedIndex = cursor.getColumnIndex(FinancialManager.Goal.COLUMN_IS_REACHED);
        int accountIdIndex = cursor.getColumnIndex(FinancialManager.Goal.COLUMN_ACCOUNT_ID);
        int sumToReachIndex = cursor.getColumnIndex(FinancialManager.Goal.COLUMN_SUM_TO_REACH);
        int goalIdIndex = cursor.getColumnIndex(FinancialManager.Goal._ID);

        this.currentSum = cursor.getDouble(currentSumIndex);
        this.targetTitle = cursor.getString(targetTitleIndex);
        this.sumToReach = cursor.getDouble(sumToReachIndex);
        this.isReached = cursor.getString(isReachedIndex);

        this.parentAccount = new Account();
        this.parentAccount.readFromDatabase(dbHelper, cursor.getInt(accountIdIndex));

        this.goalId = cursor.getInt(goalIdIndex);

        cursor.close();
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public Double getSumToReach() {
        return sumToReach;
    }

    public void setSumToReach(Double sumToReach) {
        this.sumToReach = sumToReach;
    }

    public Double getCurrentSum() {
        return currentSum;
    }

    public void setCurrentSum(Double currentSum) {
        this.currentSum = currentSum;
    }

    public String getTargetTitle() {
        return targetTitle;
    }

    public void setTargetTitle(String targetTitle) {
        this.targetTitle = targetTitle;
    }

    public String isReached() {
        return isReached;
    }

    public void setReached(String reached) {
        isReached = reached;
    }

    public Account getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(Account parentAccount) {
        this.parentAccount = parentAccount;
    }
}