package com.example.master.android_finance_manager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import entities.Account;

public class AddAccountActivity extends AppCompatActivity {

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

        SQLiteDatabase db = mFinancialManagerDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FinancialManager.Account.COLUMN_TITLE, mTitle);
        values.put(FinancialManager.Account.COLUMN_ACCOUNT_TYPE, mAccountType);
        values.put(FinancialManager.Account.COLUMN_AMOUNT_OF_MONEY, mAmountOfMoney);

        int newRowId = (int) db.insert(FinancialManager.Account.TABLE_NAME, null, values);
        Intent answerIntent = new Intent();

        answerIntent.putExtra("new_account_id", newRowId);
        setResult(RESULT_OK, answerIntent);
        finish();

    }
}
