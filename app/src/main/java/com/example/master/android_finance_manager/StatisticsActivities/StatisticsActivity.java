package com.example.master.android_finance_manager.StatisticsActivities;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.master.android_finance_manager.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import data.FinancialManagerDbHelper;
import entities.Expense;

public class StatisticsActivity extends AppCompatActivity {

    FinancialManagerDbHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.statistics_layout);
        LineChart chart = findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();

        dbHelper = new FinancialManagerDbHelper(this);

        Calendar calendar = new GregorianCalendar();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        ArrayList<Expense> source = Expense.searchExpensesInDatabaseByDay(dbHelper, dateFormat.format(calendar.getTime()));

        for(int i = 0; i < source.size(); i++) {
            entries.add(new Entry(i + 1, source.get(i).getMoneySpent().floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Day Money Spent");
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

    }


}
