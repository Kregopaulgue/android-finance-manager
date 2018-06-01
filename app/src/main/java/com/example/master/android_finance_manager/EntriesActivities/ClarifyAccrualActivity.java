package com.example.master.android_finance_manager.EntriesActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cunoraz.tagview.TagView;
import com.example.master.android_finance_manager.R;

import java.util.ArrayList;

import data.FinancialManagerDbHelper;
import entities.Category;
import entities.Tag;

import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_ACCOUNT_ID;
import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_APP;

public class ClarifyAccrualActivity extends AppCompatActivity{

    private FinancialManagerDbHelper dbHelper;

    private String selectedDate;
    private String source;
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

        setContentView(R.layout.clarify_accrual);

        tagView = (TagView) findViewById(R.id.tag_group);
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
                currentTagsView.addTag(new com.cunoraz.tagview.Tag(tag.text));
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

        selectedCategory = categories.get(4);
        loadTags();

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

    public void saveClarifying(View view) {

        EditText sourceEdit = findViewById(R.id.enterSource);
        this.source = sourceEdit.getText().toString();
        Intent answerIntent = new Intent();

        answerIntent.putExtra("comment", this.comment);
        answerIntent.putExtra("date", this.selectedDate);
        answerIntent.putExtra("source", this.source);
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
        }
    }
}