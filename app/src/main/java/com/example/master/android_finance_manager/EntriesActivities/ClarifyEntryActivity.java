package com.example.master.android_finance_manager.EntriesActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
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

        tagView = (TagView) findViewById(R.id.tag_group);
        currentTagsView = findViewById(R.id.currentTags);

        dbHelper = new FinancialManagerDbHelper(this);
        SharedPreferences preferences = getSharedPreferences(CURRENT_APP, MODE_PRIVATE);
        categories = Category.readAllFromDatabase(dbHelper, preferences.getInt(CURRENT_ACCOUNT_ID, 1));

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
        Tag addedTag = new Tag(tagText.getText().toString(), "true", selectedCategory);
        addedTag.writeToDatabase(dbHelper);
        selectedTags.add(addedTag);
        currentTagsView.addTag(new com.cunoraz.tagview.Tag(addedTag.getTagTitle()));
    }

    public void selectCategory(View view) {
        showPopupMenu(view);
    }
}
