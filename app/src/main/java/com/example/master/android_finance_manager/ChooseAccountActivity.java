package com.example.master.android_finance_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import adapters.RecyclerAdapterAccount;
import adapters.RecyclerAdapterExpense;
import data.FinancialManagerDbHelper;
import entities.Account;

public class ChooseAccountActivity extends AppCompatActivity {

    FinancialManagerDbHelper mFinancialManagerDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entries_history_page);

        mFinancialManagerDbHelper = new FinancialManagerDbHelper(this);

        RecyclerView recyclerView = findViewById(R.id.entriesRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Account> accounts = Account.readAllFromDatabase(mFinancialManagerDbHelper);

        RecyclerAdapterAccount adapterAccount = new RecyclerAdapterAccount(accounts);
        recyclerView.setAdapter(adapterAccount);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateInfo();
    }

    private void updateInfo() {
        RecyclerAdapterAccount adapter = new RecyclerAdapterAccount(Account.readAllFromDatabase(mFinancialManagerDbHelper));
        RecyclerView recyclerView = findViewById(R.id.entriesRecyclerView);
        recyclerView.setAdapter(adapter);
    }
}
