package com.example.master.android_finance_manager.AdditionalEntriesHistories;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.master.android_finance_manager.R;

import java.util.ArrayList;

import adapters.RecyclerAdapterBillReminder;
import adapters.RecyclerAdapterExpense;
import data.FinancialManagerDbHelper;
import entities.BillReminder;
import entities.Expense;

public class BillRemindersHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapterBillReminder adapter;
    FinancialManagerDbHelper mFinancialManagerDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entries_history_page);

        mFinancialManagerDbHelper = new FinancialManagerDbHelper(this);

        recyclerView = findViewById(R.id.entriesRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        linearLayoutManager = new LinearLayoutManager(this);

        ArrayList<BillReminder> billReminders = BillReminder.readAllFromDatabase(mFinancialManagerDbHelper);

        adapter = new RecyclerAdapterBillReminder(billReminders);
        recyclerView.setAdapter(adapter);

    }

}
