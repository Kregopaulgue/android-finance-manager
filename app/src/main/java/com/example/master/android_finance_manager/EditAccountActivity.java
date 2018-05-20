package com.example.master.android_finance_manager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import entities.Account;

public class EditAccountActivity extends AppCompatActivity {

    private FinancialManagerDbHelper mFinancialManagerDbHelper;

    private String mTitle;
    private String mAccountType;
    private Double mAmountOfMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFinancialManagerDbHelper = new FinancialManagerDbHelper(this);
        setContentView(R.layout.add_account);
    }

    public void createAccount(View view) {

        EditText titleText = findViewById(R.id.titleEditText);
        EditText amountOfMoney = findViewById(R.id.amountOfMoneyEditText);
        RadioGroup accountTypeGroup = findViewById(R.id.accountTypeRadioGroup);
        RadioButton walletRadio = findViewById(R.id.walletRadio);
        RadioButton cardRadio = findViewById(R.id.creditCardRadio);

        mTitle = titleText.getText().toString();
        mAmountOfMoney = Double.parseDouble(amountOfMoney.getText().toString());

        if(walletRadio.isSelected()) {
            mAccountType = "WALLET";
        } else {
            mAccountType = "CREDIT CARD";
        }

        int accountId = getIntent().getIntExtra("account_id", 1);

        Account accountToEdit = new Account(accountId, mTitle, mAccountType, mAmountOfMoney);
        accountToEdit.updateToDatabase(mFinancialManagerDbHelper, accountId);
        finish();

    }

}
