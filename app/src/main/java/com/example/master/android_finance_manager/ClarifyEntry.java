package com.example.master.android_finance_manager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.cunoraz.tagview.TagView;

import java.util.List;

import entities.Category;
import entities.Tag;

public class ClarifyEntry extends AppCompatActivity{

    private String selectedDate;
    private int Importance;
    private List<Tag> selectedTags;
    private Category selectedCategory;
    private String comment;
    private TagView tagView;
    private TagView globalCurrentTagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.clarify_entry);

        tagView = (TagView) findViewById(R.id.tag_group);
        tagView.addTags(new String[]{"mda", "heh"});

        tagView.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(com.cunoraz.tagview.Tag tag, int i) {
                TagView currentTags = findViewById(R.id.currentTags);
                currentTags.addTag(new com.cunoraz.tagview.Tag(tag.toString()));
            }
        });

        //set delete listener
        tagView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, com.cunoraz.tagview.Tag tag, int i) {

            }
        });

        globalCurrentTagView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, com.cunoraz.tagview.Tag tag, int i) {
                TagView currentTags = findViewById(R.id.currentTags);
                currentTags.remove(currentTags.getTags().indexOf(tag));
            }
        });


    }


    public void saveClarifying(View view) {

    }
}
