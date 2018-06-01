package com.example.master.android_finance_manager.EntriesActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cunoraz.tagview.TagView;
import com.example.master.android_finance_manager.R;
import com.xw.repo.BubbleSeekBar;

import java.io.Console;
import java.util.ArrayList;

import data.FinancialManagerDbHelper;
import entities.Category;
import entities.Tag;

import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_ACCOUNT_ID;
import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_APP;

public class ClarifyEntryActivity extends AppCompatActivity{

    private FinancialManagerDbHelper dbHelper;

    private String selectedDate;
    private int importance;
    private ArrayList<Tag> tagList;

    private ArrayList<Tag> selectedTags;

    private ArrayList<Category> categories;
    private Category selectedCategory;
    private String comment;

    private TagView tagView;
    private TagView currentTagsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.clarify_entry);

        tagView = findViewById(R.id.tag_group);
        currentTagsView = findViewById(R.id.currentTags);

        dbHelper = new FinancialManagerDbHelper(this);
        SharedPreferences preferences = getSharedPreferences(CURRENT_APP, MODE_PRIVATE);

        categories = Category.readAllFromDatabase(dbHelper, preferences.getInt(CURRENT_ACCOUNT_ID, 1));

        selectedTags = new ArrayList<>();

        tagView.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(com.cunoraz.tagview.Tag tag, int i) {
                com.cunoraz.tagview.Tag newTag = new com.cunoraz.tagview.Tag(tag.text);
                newTag.isDeletable = true;
                currentTagsView.addTag(newTag);
                selectedTags.add(tagList.get(i));
            }
        });

        //set delete listener
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

        EditText searchTagEdit = findViewById(R.id.searchTagEdit);
        searchTagEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setTags(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
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

        this.importance = currentBar.getProgress();
        this.comment = commentInput.getText().toString();

        Intent answerIntent = new Intent();

        String source = "";
        try {
            source = sourceInput.getText().toString();
        } catch (Exception e) {

        }

        answerIntent.putExtra("comment", this.comment);
        answerIntent.putExtra("date", this.selectedDate);
        answerIntent.putExtra("importance", this.importance);
        answerIntent.putExtra("source", source);
        answerIntent.putExtra("tags", this.selectedTags);

        setResult(RESULT_OK, answerIntent);
        finish();

    }

    public void addTagGlobally(View view) {
        TextView tagText = findViewById(R.id.newTagEdit);
        try {
            Tag addedTag = new Tag(tagText.getText().toString(), "true", selectedCategory);
            if(addedTag.getTagTitle().equals("")) {
                throw new Exception();
            }
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

    public void loadTags() {
        tagView.getTags().clear();
        tagList = Tag.readAllFromDatabaseWhereCategory(dbHelper, selectedCategory.getCategoryId());
        for(Tag tempTag : tagList) {
            com.cunoraz.tagview.Tag newTag = new com.cunoraz.tagview.Tag(tempTag.getTagTitle());
            newTag.isDeletable = true;
            tagView.addTag(newTag);
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

    public void selectCategory(View view) {
        showPopupMenu(view);
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
