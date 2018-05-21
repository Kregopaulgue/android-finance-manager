package com.example.master.android_finance_manager.EntriesActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.master.android_finance_manager.R;

import java.util.ArrayList;

import adapters.RecyclerAdapterAccrual;
import adapters.RecyclerAdapterExpense;
import data.FinancialManagerDbHelper;
import entities.Accrual;
import entities.Expense;

public class AccrualsHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapterAccrual adapter;
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

        ArrayList<Accrual> accruals = Accrual.readAllFromDatabase(mFinancialManagerDbHelper);

        adapter = new RecyclerAdapterAccrual(accruals);
        recyclerView.setAdapter(adapter);

    }

}
