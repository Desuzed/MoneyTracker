package com.example.moneytracker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.moneytracker.R.id.list;

public class ItemListActivity extends AppCompatActivity {
    private static final String TAG = "ItemListActivity";
    private RecyclerView recyclerView;
    private List<ItemRecord> data  = new ArrayList<>();
    private ItemsAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        recyclerView = findViewById(list);
        adapter = new ItemsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }



}
