package com.example.master.android_finance_manager.AccountsActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.master.android_finance_manager.R;

import data.FinancialManagerDbHelper;
import entities.Account;
import entities.Expense;

public class CurrentAccountActivity extends AppCompatActivity {

    private FinancialManagerDbHelper mFinancialManagerDbHelper;

    public final static String CURRENT_ACCOUNT_ID = "current_account_id";

    private Account mCurrentAccount;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFinancialManagerDbHelper = new FinancialManagerDbHelper(this);
        mSharedPreferences = getSharedPreferences("com.example.app", MODE_PRIVATE);
        int currentAccountId = mSharedPreferences.getInt(CURRENT_ACCOUNT_ID, 1);
        mCurrentAccount = new Account();
        setContentView(R.layout.current_account);
        try {
            this.mCurrentAccount.readFromDatabase(mFinancialManagerDbHelper, currentAccountId);
        } catch (Exception e) {

        }
        TextView title = findViewById(R.id.showingCurrentAccountTitle);
        TextView currentSum = findViewById(R.id.showingCurrentAccountSumOfMoney);
        TextView accountType = findViewById(R.id.showingCurrentAccountType);

        try {
            title.setText(mCurrentAccount.getAccountTitle());
            currentSum.setText(mCurrentAccount.getAmountOfMoney().toString());
            accountType.setText(mCurrentAccount.getAccountType());
        } catch (Exception e) {

        }
    }

    public void addAccount(View view) {

        Intent intent = new Intent(CurrentAccountActivity.this, AddAccountActivity.class);
        startActivityForResult(intent, 0);

    }

    public void chooseAccount(View view) {
        Intent intent = new Intent(CurrentAccountActivity.this, ChooseAccountActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                int newCurrentAccountId = data.getIntExtra("new_account_id", 0);
                mCurrentAccount.readFromDatabase(mFinancialManagerDbHelper, newCurrentAccountId);
                updateInfo();
            }
        }
        else if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Integer currentAccountId;
                currentAccountId = mSharedPreferences.getInt(CURRENT_ACCOUNT_ID, 1);
                mCurrentAccount.readFromDatabase(mFinancialManagerDbHelper, currentAccountId);
                updateInfo();
            }
        }
    }

    public void updateInfo() {
        TextView showingAccountTitle = findViewById(R.id.showingCurrentAccountTitle);
        TextView showingAccountMoney = findViewById(R.id.showingCurrentAccountSumOfMoney);
        TextView showingAccountType = findViewById(R.id.showingCurrentAccountType);

        showingAccountTitle.setText(mCurrentAccount.getAccountTitle());
        showingAccountMoney.setText(mCurrentAccount.getAmountOfMoney().toString());
        showingAccountType.setText(mCurrentAccount.getAccountType());
    }
}
