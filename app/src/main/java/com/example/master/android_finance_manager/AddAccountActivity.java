package com.example.master.android_finance_manager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AddAccountActivity extends AppCompatActivity {

    private String mTitle;
    private Double mAmountOfMoney;
    private String mAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_account);
    }

}
