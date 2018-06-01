package com.example.master.android_finance_manager.EntriesActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

public class EditEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public int parentAccountId;
    public FinancialEntry mEntry;
    private FinancialManagerDbHelper dbHelper;

    private ArrayList<Tag> tagList;
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
        mEntry.readFromDatabase(dbHelper, getIntent().getIntExtra("ENTRY_ID", 1));

        tagView = findViewById(R.id.tag_group);
        currentTagsView = findViewById(R.id.currentTags);

        dbHelper = new FinancialManagerDbHelper(this);

        categories = Category.readAllFromDatabase(dbHelper, mEntry.getParentAccount().getAccountId());
        selectedTags = Tag.readAllFromDatabaseWhereEntry(dbHelper, mEntry.getEntryId());

        for(Tag tempTag : selectedTags) {
            com.cunoraz.tagview.Tag newTag = new com.cunoraz.tagview.Tag(tempTag.getTagTitle());
            newTag.isDeletable = true;
            currentTagsView.addTag(newTag);
        }

        tagView.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(com.cunoraz.tagview.Tag tag, int i) {
                com.cunoraz.tagview.Tag newTag = new com.cunoraz.tagview.Tag(tag.text);
                newTag.isDeletable = true;
                currentTagsView.addTag(newTag);
                selectedTags.add(tagList.get(i));
            }
        });

        tagView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, com.cunoraz.tagview.Tag tag, int i) {
                String tagToDelete = tag.text;
                Tag.deleteFromDatabaseWhereTitle(dbHelper, tagToDelete);
                tagView.remove(tagView.getTags().indexOf(tag));
            }


        });

        currentTagsView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, com.cunoraz.tagview.Tag tag, int i) {
                TagView currentTags = findViewById(R.id.currentTags);
                selectedTags.remove(currentTags.getTags().indexOf(tag));
                currentTags.remove(currentTags.getTags().indexOf(tag));
            }
        });

        BubbleSeekBar currentBar = findViewById(R.id.importanceSeekBar);
        EditText commentInput = findViewById(R.id.commentClarifyInput);
        EditText sourceInput = findViewById(R.id.enterSource);
        EditText titleInput = findViewById(R.id.clarifyTitleEdit);
        EditText moneySpent = findViewById(R.id.setNewMoneyInput);

        if(entryType.equals("EXPENSE")) {
            Expense expense = (Expense) mEntry;
            currentBar.setProgress(expense.getImportance());
            moneySpent.setText(expense.getMoneySpent().toString());
        } else {
            Accrual accrual = (Accrual) mEntry;
            sourceInput.setText(accrual.getSource());
            moneySpent.setText(accrual.getMoneyGained().toString());
        }
        commentInput.setText(mEntry.getComment());
        titleInput.setText(mEntry.getTitle());
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
                                loadTags();
                                return true;
                            case R.id.serviceMenuItem:
                                selectedCategory = categories.get(1);
                                loadTags();
                                return true;
                            case R.id.applianceMenuItem:
                                selectedCategory = categories.get(2);
                                loadTags();
                                return true;
                            case R.id.clothMenuItem:
                                selectedCategory = categories.get(3);
                                loadTags();
                                return true;
                            case R.id.accrualMenuItem:
                                selectedCategory = categories.get(4);
                                loadTags();
                                return true;
                            case R.id.otherMenuItem:
                                selectedCategory = categories.get(5);
                                loadTags();
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
        parentAccount.readFromDatabase(dbHelper, mEntry.getParentAccount().getAccountId());
        mEntry.setParentAccount(parentAccount);
        mEntry.updateToDatabase(dbHelper, mEntry.getEntryId());

        finish();

    }

    public void addTagGlobally(View view) {

        TextView tagText = findViewById(R.id.newTagEdit);
        try {
            Tag addedTag = new Tag(tagText.getText().toString(), "true", selectedCategory);
            addedTag.writeToDatabase(dbHelper);
            selectedTags.add(addedTag);
            com.cunoraz.tagview.Tag newTag = new com.cunoraz.tagview.Tag(addedTag.getTagTitle());
            newTag.isDeletable = true;
            tagView.addTag(newTag);
            currentTagsView.addTag(newTag);
        } catch (Exception e) {
            showAlert("Category is not selected!");
        }
    }

    public void selectCategory(View view) {
        showPopupMenu(view);
    }

    public void loadTags() {
        tagView.getTags().clear();
        tagList = Tag.readAllFromDatabaseWhereCategory(dbHelper, selectedCategory.getCategoryId());
        for(Tag tempTag : tagList) {
            com.cunoraz.tagview.Tag newTag = new com.cunoraz.tagview.Tag(tempTag.getTagTitle());
            newTag.isDeletable = true;
            tagView.addTag(newTag);
        }
    }

    public void loadCurrentTags() {
        for(Tag tempTag : selectedTags) {
            com.cunoraz.tagview.Tag tagToAdd = new com.cunoraz.tagview.Tag(tempTag.getTagTitle());
            tagToAdd.isDeletable = true;
            currentTagsView.addTag(tagToAdd);
        }
    }

    private void setTags(CharSequence cs) {
        /**
         * for empty edittext
         */
        if (cs.toString().equals("")) {
            tagView.getTags().clear();
            loadTags();
            return;
        }

        String text = cs.toString();
        ArrayList<com.cunoraz.tagview.Tag> tags = new ArrayList<>();
        com.cunoraz.tagview.Tag tag;

        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).getTagTitle().toLowerCase().startsWith(text.toLowerCase())) {
                tag = new com.cunoraz.tagview.Tag(tagList.get(i).getTagTitle());
                tag.radius = 10f;
                tag.layoutColor = Color.parseColor("GREEN");
                tag.isDeletable = true;
                tags.add(tag);
            }
        }
        tagView.addTags(tags);

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

    public void showAlert(String message) {
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
