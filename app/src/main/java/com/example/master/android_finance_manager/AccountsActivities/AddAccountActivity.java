package com.example.master.android_finance_manager.AccountsActivities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.master.android_finance_manager.R;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import entities.Account;
import entities.Expense;

public class AddAccountActivity extends AppCompatActivity {

    private FinancialManagerDbHelper mFinancialManagerDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFinancialManagerDbHelper = new FinancialManagerDbHelper(this);
        setContentView(R.layout.add_account);
    }

    public void createAccount(View view) {
        String mTitle;
        String mAccountType;
        Double mAmountOfMoney;

        EditText titleText = findViewById(R.id.titleEditText);
        EditText amountOfMoney = findViewById(R.id.amountOfMoneyEditText);
        RadioButton walletRadio = findViewById(R.id.walletRadio);
        RadioButton cardRadio = findViewById(R.id.creditCardRadio);

        mTitle = titleText.getText().toString();
        mAmountOfMoney = Double.parseDouble(amountOfMoney.getText().toString());

        if(!walletRadio.isChecked() && !cardRadio.isChecked()) {
            showAlert("Account type is not selected!");
            return;
        }

        if(walletRadio.isChecked()) {
            mAccountType = "WALLET";
        } else {
            mAccountType = "CREDIT CARD";
        }

        SQLiteDatabase db = mFinancialManagerDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FinancialManager.Account.COLUMN_TITLE, mTitle);
        values.put(FinancialManager.Account.COLUMN_ACCOUNT_TYPE, mAccountType);
        values.put(FinancialManager.Account.COLUMN_AMOUNT_OF_MONEY, mAmountOfMoney);

        try {
            int newRowId = (int) db.insert(FinancialManager.Account.TABLE_NAME, null, values);
            Intent answerIntent = new Intent();

            answerIntent.putExtra("new_account_id", newRowId);
            setResult(RESULT_OK, answerIntent);
            finish();
        } catch (Exception e) {

            if(mTitle == null) {
                showAlert("Account title is not entered!");
            } else if(mAmountOfMoney == null) {
                showAlert("Amount of money is not entered!");
            }

        }
    }

    private void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
