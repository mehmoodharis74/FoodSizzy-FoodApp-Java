package com.harismehmood.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.harismehmood.finalproject.activities.Models.StaticRvModel;
import com.harismehmood.finalproject.activities.adapters.StaticRvAdapter;
import com.harismehmood.finalproject.activities.adapters.adminAdapter;

import java.util.ArrayList;
import java.util.List;

public class admin_main_layout extends AppCompatActivity {
RecyclerView recyclerView;
List<StaticRvModel> items;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        items = new ArrayList<>();
        StaticRvModel model = new StaticRvModel("Add Category",R.drawable.burger);
        items.add(model);
        model = new StaticRvModel("Add Product",R.drawable.pizza);
        items.add(model);
        model = new StaticRvModel("Add Restaurant",R.drawable.res1);
        items.add(model);


        recyclerView = findViewById(R.id.admin_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(new adminAdapter(items, this));


    }
}