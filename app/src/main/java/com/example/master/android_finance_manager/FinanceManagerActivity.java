package com.example.master.android_finance_manager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.master.android_finance_manager.EntriesActivities.AddEntryActivity;
import com.example.master.android_finance_manager.AccountsActivities.CurrentAccountActivity;
import com.example.master.android_finance_manager.EntriesActivities.EntriesHistoryActivity;

import java.util.Calendar;

import data.FinancialManagerDbHelper;
import entities.Account;
import entities.Accrual;
import entities.BillReminder;
import entities.Constraint;
import entities.Goal;

public class FinanceManagerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Accrual accrual;
    private Goal goal;
    private BillReminder billReminder;
    private Constraint constraint;

    private FinancialManagerDbHelper mDbHelper;
    private CardView mLatestEntryCard;
    private CardView mLatestGoalCard;
    private CardView mLatestAccrualCard;

    public Account mCurrentAccount;

    private SharedPreferences mSharedPreferences;
    public final static String CURRENT_ACCOUNT_ID = "current_account_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Expense", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                Intent intent = new Intent(FinanceManagerActivity.this, AddEntryActivity.class);
                intent.putExtra("ENTRY_TYPE", "EXPENSE");
                startActivity(intent);

            }
        });

        mDbHelper = new FinancialManagerDbHelper(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDbHelper = new FinancialManagerDbHelper(this);
        mCurrentAccount = new Account();
        this.mLatestAccrualCard = findViewById(R.id.latestAccrual);
        this.mLatestEntryCard = findViewById(R.id.latestEntryCard);
        this.mLatestGoalCard = findViewById(R.id.latestGoal);
        this.mSharedPreferences = this.getSharedPreferences("com.example.app", MODE_PRIVATE);

        mCurrentAccount.readFromDatabase(mDbHelper, mSharedPreferences.getInt(CURRENT_ACCOUNT_ID, 1));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.finance_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionAddGoal) {
            addGoalDialog();
            return true;
        } else if(id == R.id.actionAddAccrual) {
            Intent intent = new Intent(FinanceManagerActivity.this, AddEntryActivity.class);
            intent.putExtra("ENTRY_TYPE", "ACCRUAL");
            startActivity(intent);
            return true;
        } else if(id == R.id.actionAddBillReminder) {
            addBillReminderDialog();
            return true;
        } else if(id == R.id.actionAddConstraint) {
            addConstraintDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accounts) {
            Intent intent = new Intent(FinanceManagerActivity.this, CurrentAccountActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_expenses) {
            Intent intent = new Intent(FinanceManagerActivity.this, EntriesHistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_accruals) {
            Intent intent = new Intent(FinanceManagerActivity.this, AddEntryActivity.class);
            intent.putExtra("currentAccountId", 1);
            startActivity(intent);
        } else if (id == R.id.nav_goals) {

        } else if (id == R.id.nav_reminders) {

        } else if (id == R.id.nav_constraints) {

        }

        // Выделяем выбранный пункт меню в шторке
        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addGoalDialog() {
        goal = new Goal();

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_goal, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final EditText titleInput = (EditText) promptsView.findViewById(R.id.targetTitleInput);
        final EditText sumToReachInput = (EditText) promptsView.findViewById(R.id.sumToReachInput);
        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder
                .setCancelable(true)
                .setPositiveButton("ADD GOAL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                goal.setTargetTitle(titleInput.getText().toString());
                                goal.setSumToReach(Double.parseDouble(sumToReachInput.getText().toString()));
                                goal.setCurrentSum(0.0);
                                goal.setParentAccount(mCurrentAccount);
                                goal.setReached("false");
                                goal.writeToDatabase(mDbHelper);
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        //и отображаем его:
        alertDialog.show();
    }

    private void addBillReminderDialog() {

        billReminder = new BillReminder();

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_bill_reminder, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(promptsView);

        final EditText titleInput = promptsView.findViewById(R.id.billTitleInput);
        final EditText sumToPayInput = promptsView.findViewById(R.id.sumToPayInput);
        final Button selectBillDate = promptsView.findViewById(R.id.selectDateBill);

        selectBillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendar now = Calendar.getInstance();
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                selectBillDate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                dpd.show();


            }
        });

        mDialogBuilder
                .setCancelable(true)
                .setPositiveButton("ADD BILL REMINDER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                billReminder.setBillTitle(titleInput.getText().toString());
                                billReminder.setSumToPay(Double.parseDouble(sumToPayInput.getText().toString()));
                                billReminder.setParentAccount(mCurrentAccount);
                                billReminder.setIsPaid("false");
                                billReminder.setDateTimeToPay(selectBillDate.getText().toString());
                                billReminder.writeToDatabase(mDbHelper);
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

    private void addConstraintDialog() {

        constraint = new Constraint();

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_constraint, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(promptsView);

        final EditText constraintMoneyLimit = findViewById(R.id.sumConstraintInput);
        final EditText firstBorder = findViewById(R.id.firstBorderInput);
        final Button selectDateBegin = findViewById(R.id.selectBeginDateConstraint);
        final Button selectDateEnd = findViewById(R.id.selectEndDateConstraint);

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
                //Calendar now = Calendar.getInstance();
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
                .setPositiveButton("ADD CONSTRAINT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                constraint.setMoneyLimit(Double.parseDouble(constraintMoneyLimit.getText().toString()));
                                constraint.setWarningMoneyBorder(Double.parseDouble(firstBorder.getText().toString()));
                                constraint.setDateOfBegin(selectDateBegin.getText().toString());
                                constraint.setDateOfEnd(selectDateEnd.getText().toString());
                                constraint.setIsFinished("false");
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
