package com.example.master.android_finance_manager.EntriesActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.cunoraz.tagview.TagView;
import com.example.master.android_finance_manager.R;
import com.xw.repo.BubbleSeekBar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import data.FinancialManagerDbHelper;
import entities.Account;
import entities.Accrual;
import entities.Category;
import entities.Expense;
import entities.FinancialEntry;
import entities.Tag;

import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_ACCOUNT_ID;
import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_APP;

public class EditEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public int parentAccountId;
    public FinancialEntry mEntry;
    private FinancialManagerDbHelper dbHelper;

    private String selectedDate;

    private ArrayList<Tag> selectedTags;
    private ArrayList<Category> categories;
    private Category selectedCategory;

    private TagView tagView;
    private TagView currentTagsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new FinancialManagerDbHelper(this);
        String entryType = getIntent().getStringExtra("ENTRY_TYPE");
        if(entryType.equals("EXPENSE")) {
            setContentView(R.layout.edit_expense);
            mEntry = new Expense();
        } else {
            setContentView(R.layout.edit_accrual);
            mEntry = new Accrual();
        }
        mEntry.setEntryType(getIntent().getStringExtra("ENTRY_TYPE"));
        mEntry.setEntryId(getIntent().getIntExtra("ENTRY_ID", 1));

        tagView = findViewById(R.id.tag_group);
        currentTagsView = findViewById(R.id.currentTags);

        dbHelper = new FinancialManagerDbHelper(this);
        SharedPreferences preferences = getSharedPreferences(CURRENT_APP, MODE_PRIVATE);

        parentAccountId = preferences.getInt(CURRENT_ACCOUNT_ID, 1);

        categories = Category.readAllFromDatabase(dbHelper, parentAccountId);

        selectedTags = new ArrayList<>();

        tagView.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(com.cunoraz.tagview.Tag tag, int i) {
                currentTagsView.addTag(new com.cunoraz.tagview.Tag(tag.text));
            }
        });

        //set delete listener
        tagView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, com.cunoraz.tagview.Tag tag, int i) {

            }
        });

        currentTagsView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, com.cunoraz.tagview.Tag tag, int i) {
                TagView currentTags = findViewById(R.id.currentTags);
                currentTags.remove(currentTags.getTags().indexOf(tag));
            }
        });
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

    public void saveClarifying(View view) {

        BubbleSeekBar currentBar = findViewById(R.id.importanceSeekBar);
        EditText commentInput = findViewById(R.id.commentClarifyInput);
        EditText sourceInput = findViewById(R.id.enterSource);
        EditText titleInput = findViewById(R.id.clarifyTitleEdit);
        EditText moneySpent = findViewById(R.id.setNewMoneyInput);

        if(mEntry.getEntryType().equals("EXPENSE")) {
            ((Expense) this.mEntry).setImportance(currentBar.getProgress());
            ((Expense) this.mEntry).setMoneySpent(Double.parseDouble(moneySpent.getText().toString()));
        } else {
            ((Accrual) this.mEntry).setSource(sourceInput.getText().toString());
            ((Accrual) this.mEntry).setMoneyGained(Double.parseDouble(moneySpent.getText().toString()));
        }
        mEntry.setComment(commentInput.getText().toString());
        mEntry.setTitle(titleInput.getText().toString());

        Account parentAccount = new Account();
        parentAccount.readFromDatabase(dbHelper, parentAccountId);
        mEntry.setParentAccount(parentAccount);
        mEntry.updateToDatabase(dbHelper, mEntry.getEntryId());

        finish();

    }

    public void addTagGlobally(View view) {

        TextView tagText = findViewById(R.id.newTagEdit);
        Tag addedTag = new Tag(tagText.getText().toString(), "true", selectedCategory);
        addedTag.writeToDatabase(dbHelper);
        selectedTags.add(addedTag);
        currentTagsView.addTag(new com.cunoraz.tagview.Tag(addedTag.getTagTitle()));
    }

    public void selectCategory(View view) {
        showPopupMenu(view);
    }

    public void selectDate(View view) {
        AddEntryActivity.DatePickerFragment fragment = new AddEntryActivity.DatePickerFragment();

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


}
