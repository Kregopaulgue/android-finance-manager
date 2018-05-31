package com.example.master.android_finance_manager.StatisticsActivities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.example.master.android_finance_manager.EntriesActivities.AddEntryActivity;
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
import entities.Accrual;
import entities.Category;
import entities.Expense;
import entities.FinancialEntry;

import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_ACCOUNT_ID;
import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_APP;

public class StatisticsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    FinancialManagerDbHelper dbHelper;
    Integer currentAccountId;
    Category selectedCategory;
    ArrayList<Category> categories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.statistics_layout);

        dbHelper = new FinancialManagerDbHelper(this);

        Toolbar toolbar = findViewById(R.id.statisticsToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences preferences = getSharedPreferences(CURRENT_APP, MODE_PRIVATE);
        currentAccountId = preferences.getInt(CURRENT_ACCOUNT_ID, 1);

        Calendar calendar = new GregorianCalendar();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        categories = Category.readAllFromDatabase(dbHelper, currentAccountId);
        ArrayList<Expense> source = Expense.searchExpensesInDatabaseByDay(dbHelper, dateFormat.format(calendar.getTime()), currentAccountId);

        updateChart(source);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statistics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionStatisticByDay) {
            try {
                selectSingleDate(item.getActionView());
            } catch (Exception e) {

            }
            return true;
        } else if(id == R.id.actionStatisticByDates) {
            try {
                selectDatesDialog();
            } catch (Exception e) {

            }
            return true;
        } else if(id == R.id.actionStatisticByCategories) {
            try {
                selectCategoriesAndDates();
            } catch (Exception e) {

            }
            return true;
        } else if(id == R.id.actionSearchByTags) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        updateChart(Expense.searchExpensesInDatabaseByDay(dbHelper, dateFormat.format(cal.getTime()), currentAccountId));
    }

    public void selectSingleDate(View view) {
        AddEntryActivity.DatePickerFragment fragment = new AddEntryActivity.DatePickerFragment();

        fragment.show(getSupportFragmentManager(),"Pick Date");
    }


    private void selectDatesDialog() {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.select_dates_statistics, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(promptsView);

        final Button selectDateBegin = promptsView.findViewById(R.id.selectBeginButton);
        final Button selectDateEnd = promptsView.findViewById(R.id.selectEndButton);
        final RadioButton accrualButton = promptsView.findViewById(R.id.chooseAccrual);
        final RadioButton expenseButton = promptsView.findViewById(R.id.chooseExpense);

        selectDateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
                                selectDateBegin.setText(dateFormat.format(cal.getTime()));

                            }
                        }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                dpd.show();


            }
        });

        selectDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
                                selectDateEnd.setText(dateFormat.format(cal.getTime()));

                            }
                        }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                dpd.show();


            }
        });

        mDialogBuilder
                .setCancelable(true)
                .setPositiveButton("CHECK STATISTICS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String firstDate = selectDateBegin.getText().toString();
                                String secondDate = selectDateEnd.getText().toString();
                                if(expenseButton.isChecked()) {
                                    updateChart(Expense.searchExpensesInDatabaseByDates(dbHelper, firstDate, secondDate, currentAccountId));
                                } else if(accrualButton.isChecked()) {
                                    updateChart(Accrual.searchAccrualsInDatabaseByDates(dbHelper, firstDate, secondDate, currentAccountId));
                                }
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();

    }

    public void selectCategoriesAndDates() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.select_category_statistics, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(promptsView);

        final Button selectDateBegin = promptsView.findViewById(R.id.selectBeginButton);
        final Button selectDateEnd = promptsView.findViewById(R.id.selectEndButton);
        final Button selectCategory = promptsView.findViewById(R.id.selectCategoryButton);
        final RadioButton accrualButton = promptsView.findViewById(R.id.chooseAccrual);
        final RadioButton expenseButton = promptsView.findViewById(R.id.chooseExpense);

        selectDateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
                                selectDateBegin.setText(dateFormat.format(cal.getTime()));

                            }
                        }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                dpd.show();


            }
        });

        selectDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
                                selectDateEnd.setText(dateFormat.format(cal.getTime()));

                            }
                        }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                dpd.show();


            }
        });

        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        mDialogBuilder
                .setCancelable(true)
                .setPositiveButton("CHECK STATISTICS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String firstDate = selectDateBegin.getText().toString();
                                String secondDate = selectDateEnd.getText().toString();
                                int categoryId = selectCategory.getId();
                                if(expenseButton.isChecked()) {
                                    ArrayList<Expense> results = Expense.searchExpensesInDatabaseByCategoryAndDate(dbHelper, firstDate,
                                            secondDate, currentAccountId, categoryId);
                                    updateChart(results);
                                } else if(accrualButton.isChecked()) {
                                    ArrayList<Accrual> results = Accrual.searchAccrualsInDatabaseByCategoryAndDate(dbHelper, firstDate,
                                            secondDate, currentAccountId, categoryId);
                                    updateChart(results);
                                }
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    private void updateChart(ArrayList<? extends FinancialEntry> source) {
        LineChart chart = findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();
        try  {
            if(source.get(0).getClass().equals(Expense.class)) {
                for(int i = 0; i < source.size(); i++) {
                    entries.add(new Entry(i + 1, ((Expense)source.get(i)).getMoneySpent().floatValue()));
                }
            } else {
                for(int i = 0; i < source.size(); i++) {
                    entries.add(new Entry(i + 1, ((Accrual)source.get(i)).getMoneyGained().floatValue()));
                }
            }
        } catch (Exception e) {
            showAlert("Nothing to show!");
            return;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Day Money Spent");
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.category_popup_menu);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.foodMenuItem:
                                selectedCategory = categories.get(0);
                                return true;
                            case R.id.serviceMenuItem:
                                selectedCategory = categories.get(1);
                                return true;
                            case R.id.applianceMenuItem:
                                selectedCategory = categories.get(2);
                                return true;
                            case R.id.clothMenuItem:
                                selectedCategory = categories.get(3);
                                return true;
                            case R.id.otherMenuItem:
                                selectedCategory = categories.get(4);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {

            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
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
