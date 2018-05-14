package entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import interfaces.DatabaseHelperFunctions;

public class Goal implements DatabaseHelperFunctions{

    private int goalId;
    private Double sumToReach;
    private Double currentSum;
    private String targetDescription;
    private String isReached;
    private Account parentAccount;

    public Goal() {
    }

    public Goal(int goalId, Double sumToReach, Double currentSum, String targetDescription,
                String isReached, Account parentAccount) {
        this.goalId = goalId;
        this.sumToReach = sumToReach;
        this.currentSum = currentSum;
        this.targetDescription = targetDescription;
        this.isReached = isReached;
        this.parentAccount = parentAccount;
    }

    @Override
    public void writeToDatabase(FinancialManagerDbHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FinancialManager.Goal.COLUMN_SUM_TO_REACH, this.sumToReach);
        values.put(FinancialManager.Goal.COLUMN_CURRENT_SUM, this.currentSum);
        values.put(FinancialManager.Goal.COLUMN_TARGET_DESCRIPTION, this.targetDescription);
        values.put(FinancialManager.Goal.COLUMN_IS_REACHED, this.isReached);
        values.put(FinancialManager.Goal.COLUMN_ACCOUNT_ID, this.parentAccount.getAccountId());

        long newRowId = db.insert(FinancialManager.Goal.TABLE_NAME, null, values);

    }

    @Override
    public void updateFromDatabase(FinancialManagerDbHelper dbHelper) {
        readFromDatabase(dbHelper, this.goalId);
    }

    @Override
    public void readFromDatabase(FinancialManagerDbHelper dbHelper, int entryId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT goal_id AS _id, * FROM goals" +
                " WHERE goal_id = ?", new String[]{String.valueOf(entryId)});

        int currentSumIndex = cursor.getColumnIndex(FinancialManager.Goal.COLUMN_CURRENT_SUM);
        int targetDescriptionIndex = cursor.getColumnIndex(FinancialManager.Goal.COLUMN_TARGET_DESCRIPTION);
        int isReachedIndex = cursor.getColumnIndex(FinancialManager.Goal.COLUMN_IS_REACHED);
        int accountIdIndex = cursor.getColumnIndex(FinancialManager.Goal.COLUMN_ACCOUNT_ID);
        int sumToReachIndex = cursor.getColumnIndex(FinancialManager.Goal.COLUMN_SUM_TO_REACH);
        int goalIdIndex = cursor.getColumnIndex(FinancialManager.Goal._ID);

        this.currentSum = cursor.getDouble(currentSumIndex);
        this.targetDescription = cursor.getString(targetDescriptionIndex);
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

    public String getTargetDescription() {
        return targetDescription;
    }

    public void setTargetDescription(String targetDescription) {
        this.targetDescription = targetDescription;
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