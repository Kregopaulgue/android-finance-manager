package com.example.master.android_finance_manager.EntriesActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.master.android_finance_manager.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import adapters.RecyclerAdapterAccrual;
import adapters.RecyclerAdapterExpense;
import data.FinancialManagerDbHelper;
import entities.Accrual;
import entities.Constraint;
import entities.Expense;
import entities.FinancialEntry;
import entities.Tag;

import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_ACCOUNT_ID;
import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_APP;

public class EntriesHistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private FinancialManagerDbHelper mFinancialManagerDbHelper;
    private boolean isExpense;
    private ArrayList resultEntries;
    private Integer currentAccountId;

    private String dayToSearch;
    private String firstDate;
    private String secondDate;
    private String tagToSearch;
    private String titleToSearch;

    final static String SEARCH_DAY = "SEARCH_DAY";
    final static String SEARCH_DATES = "SEARCH_DATES";
    final static String SEARCH_TAG = "SEARCH_TAG";
    final static String SEARCH_TITLE = "SEARCH_TITLE";

    String currentSearch;
    boolean isTwoDatesSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entries_history_page);

        mFinancialManagerDbHelper = new FinancialManagerDbHelper(this);

        Toolbar toolbar = findViewById(R.id.entriesHistoryToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.entriesRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences preferences = getSharedPreferences(CURRENT_APP, MODE_PRIVATE);
        currentAccountId = preferences.getInt(CURRENT_ACCOUNT_ID, 1);
        isExpense = getIntent().getStringExtra("ENTRY_TYPE").equals("EXPENSE");
        if(isExpense) {
            ArrayList<Expense> expenses =
                    Expense.readAllFromDatabase(mFinancialManagerDbHelper, currentAccountId);
            adapter = new RecyclerAdapterExpense(expenses);
        } else {
            ArrayList<Accrual> accruals =
                    Accrual.readAllFromDatabase(mFinancialManagerDbHelper, currentAccountId);
            adapter = new RecyclerAdapterAccrual(accruals);
        }

        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionSearchByDay) {
            selectSingleDate(item.getActionView());
            return true;
        } else if(id == R.id.actionSearchByDates) {
            selectDatesDialog();
            return true;
        } else if(id == R.id.actionSearchByTags) {
            enterTitle(item.getActionView(), SEARCH_TAG);
            return true;
        } else if(id == R.id.actionSearchByTitle) {
            enterTitle(item.getActionView(), SEARCH_TITLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateView(String searchType) {

        if(searchType.equals(SEARCH_DAY)) {
            if(isExpense) {
                resultEntries = Expense.searchExpensesInDatabaseByDay(mFinancialManagerDbHelper, dayToSearch, currentAccountId);
            }
            else {
                resultEntries = Accrual.searchAccrualsInDatabaseByDay(mFinancialManagerDbHelper, dayToSearch, currentAccountId);
            }
        } else if(searchType.equals(SEARCH_DATES)) {
            if(isExpense) {
                resultEntries = Expense.searchExpensesInDatabaseByDates(mFinancialManagerDbHelper, firstDate, secondDate, currentAccountId);
            }
            else {
                resultEntries = Accrual.searchAccrualsInDatabaseByDates(mFinancialManagerDbHelper, firstDate, secondDate, currentAccountId);
            }
        } else if(searchType.equals(SEARCH_TAG)) {

            if(isExpense) {
                resultEntries = Expense.searchExpensesInDatabaseByTag(mFinancialManagerDbHelper, tagToSearch);
            }
            else {
                resultEntries = Accrual.searchAccrualsInDatabaseByTag(mFinancialManagerDbHelper, tagToSearch);
            }

        } else if(searchType.equals(SEARCH_TITLE)) {
            if(isExpense) {
                resultEntries = Expense.searchExpensesInDatabaseByTitle(mFinancialManagerDbHelper, titleToSearch, currentAccountId);
            }
            else {
                resultEntries = Accrual.searchAccrualsInDatabaseByTitle(mFinancialManagerDbHelper, titleToSearch, currentAccountId);
            }
        }

        if(isExpense) {
            adapter = new RecyclerAdapterExpense(resultEntries);
        } else {
            adapter = new RecyclerAdapterAccrual(resultEntries);
        }
        recyclerView.setAdapter(adapter);
    }

    public void enterTitle(View view, final String searchType) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.enter_title, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                if(searchType.equals(SEARCH_TITLE)) {
                                    titleToSearch = userInput.getText().toString();
                                    updateView(SEARCH_TITLE);
                                } else {
                                    tagToSearch = userInput.getText().toString();
                                    updateView(SEARCH_TAG);
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

    public void selectSingleDate(View view) {
        AddEntryActivity.DatePickerFragment fragment = new AddEntryActivity.DatePickerFragment();

        currentSearch = SEARCH_DAY;
        fragment.show(getSupportFragmentManager(),"Pick Date");
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        if(currentSearch.equals(SEARCH_DAY)) {
            setDayDate(cal);
        }
    }

    private void setDayDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dayToSearch = dateFormat.format(calendar.getTime());
        updateView(SEARCH_DAY);
    }

    private void selectDatesDialog() {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.select_dates, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(promptsView);

        final Button selectDateBegin = promptsView.findViewById(R.id.selectBeginButton);
        final Button selectDateEnd = promptsView.findViewById(R.id.selectEndButton);

        selectDateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendar now = Calendar.getInstance();
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                selectDateBegin.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

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
                                selectDateEnd.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                dpd.show();


            }
        });

        mDialogBuilder
                .setCancelable(true)
                .setPositiveButton("SEARCH",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                firstDate = selectDateBegin.getText().toString();
                                secondDate = selectDateEnd.getText().toString();
                                updateView(SEARCH_DATES);
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
}
