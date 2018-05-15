package com.example.master.android_finance_manager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import entities.Category;
import entities.Tag;

public class ClarifyEntry extends AppCompatActivity{

    private String selectedDate;
    private int Importance;
    private List<Tag> selectedTags;
    private Category selectedCategory;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.clarify_entry);
    }





}
