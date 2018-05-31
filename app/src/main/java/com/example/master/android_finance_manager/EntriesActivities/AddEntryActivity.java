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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.master.android_finance_manager.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import data.FinancialManagerDbHelper;
import entities.Account;
import entities.Accrual;
import entities.EntryTagBinder;
import entities.Expense;
import entities.FinancialEntry;
import entities.Tag;

import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_ACCOUNT_ID;
import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_APP;

public class AddEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public int parentAccountId;
    public FinancialEntry mEntry;
    private FinancialManagerDbHelper dbHelper;
    private ArrayList<Tag> selectedTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(CURRENT_APP, MODE_PRIVATE);
        parentAccountId = preferences.getInt(CURRENT_ACCOUNT_ID, 1);

        mEntry = new FinancialEntry();
        String entryType = getIntent().getStringExtra("ENTRY_TYPE");
        if(entryType.equals("ACCRUAL")) {
            mEntry = new Accrual();
        }
        else {
            mEntry = new Expense();
        }
        mEntry.setEntryType(entryType);
        dbHelper = new FinancialManagerDbHelper(this);
        setContentView(R.layout.add_entry_manager);
    }

    public void selectDate(View view) {
        DatePickerFragment fragment = new DatePickerFragment();

        fragment.show(getSupportFragmentManager(),"Pick Date");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        mEntry.setEntryDate(dateFormat.format(calendar.getTime()));

    }

    public void enterTitle(View view) {
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
                                mEntry.setTitle(userInput.getText().toString());
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

    public void clarifyEntry(View view) {

        Intent intent;
        if(mEntry.getEntryType().equals("EXPENSE")) {
            intent = new Intent(this, ClarifyEntryActivity.class);
        } else {
            intent = new Intent(this, ClarifyAccrualActivity.class);
        }
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if(data.getStringExtra("date") != null) {
                    this.mEntry.setEntryDate(data.getStringExtra("date"));
                }
                this.mEntry.setComment(data.getStringExtra("comment"));
                this.selectedTags = (ArrayList<Tag>) data.getSerializableExtra("tags");

                if(mEntry.getClass().equals(Expense.class)) {
                    ((Expense)this.mEntry).setImportance(data.getIntExtra("importance", 1));
                } else {
                    ((Accrual)this.mEntry).setSource(data.getStringExtra("source"));
                }
            }
        }
    }

    public void onNumberClick(View view) {
        Button button = (Button) view;
        EditText number = findViewById(R.id.editNumber);
        number.setText(number.getText().toString() + button.getText().toString());
    }

    public void add(View view) {
    }

    public void minus(View view) {
    }

    public void multiply(View view) {
    }

    public void divide(View view) {
    }

    public void saveEntry(View view) {

        EditText number = findViewById(R.id.editNumber);
        this.mEntry.setParentAccount(new Account());
        this.mEntry.getParentAccount().readFromDatabase(dbHelper, parentAccountId);
        try {
            if(mEntry.getClass().equals(Expense.class)) {
                ((Expense)this.mEntry).setMoneySpent(Double.parseDouble(number.getText().toString()));
            } else {
                ((Accrual)this.mEntry).setMoneyGained(Double.parseDouble(number.getText().toString()));
            }
        } catch (Exception e) {
            showAlert("Money Is Not Set!");
            return;
        }

        if(mEntry.getTitle() == null) {
            showAlert("Title is not selected!");
            return;
        }
        else if(mEntry.getEntryDate() == null) {
            showAlert("Date is not selected");
            return;
        }

        mEntry.writeToDatabase(dbHelper);

        if(selectedTags != null) {
            for(Tag tempTag : selectedTags) {
                EntryTagBinder binder = new EntryTagBinder(tempTag, mEntry);
                binder.writeToDatabase(dbHelper);
            }
        }

        finish();

    }

    public static class DatePickerFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
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
