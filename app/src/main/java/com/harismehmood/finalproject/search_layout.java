package com.harismehmood.finalproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.harismehmood.finalproject.activities.Models.resDynamicModel;
import com.harismehmood.finalproject.activities.adapters.resDetailActivityAdapter2;

import java.util.ArrayList;
import java.util.List;

public class search_layout extends AppCompatActivity {
ImageButton back,search;
RecyclerView search_rv;
resDetailActivityAdapter2 search_adapter;
LottieAnimationView progressBar;
EditText search_bar;
TextView no_result;
FirebaseFirestore db;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back = findViewById(R.id.back_button);
        search_bar = findViewById(R.id.search_editText);
        search = findViewById(R.id.search_screen_search_button);
        progressBar = findViewById(R.id.progressBar);
        search_rv = findViewById(R.id.search_recyclerView);
        no_result = findViewById(R.id.no_result_textView);

        back.setOnClickListener(v -> {
            super.onBackPressed();
        });

        search.setOnClickListener(v ->{
            searchFromDatabase(search_bar.getText().toString());
        });
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFromDatabase(s.toString());
            }
        });


    }
    private void isLoading(boolean load){
        if(load){
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            progressBar.setVisibility(View.GONE);
        }

    }
    private void searchFromDatabase(String stringToFind){
        if(stringToFind.isEmpty())return;
        isLoading(true);
        db = FirebaseFirestore.getInstance();
        String cap = stringToFind.substring(0, 1).toUpperCase() + stringToFind.substring(1);

        List<resDynamicModel> items = new ArrayList<>();
        db.collection("items").whereEqualTo("name",cap).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    items.add(new resDynamicModel(document.getString("name"),document.getString("price")));
                }
                no_result.setVisibility(View.GONE);
                search_adapter = new resDetailActivityAdapter2(items,this);
                search_rv.setAdapter(search_adapter);
                search_rv.setLayoutManager(new LinearLayoutManager(this));
                isLoading(false);
            }
            else{
                if(items.size() == 0){
                    no_result.setVisibility(View.VISIBLE);
                    isLoading(false);
                }
            }
        });
        db.collection("categories").whereEqualTo("name",cap).get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                for (QueryDocumentSnapshot document : task1.getResult()) {
                    items.add(new resDynamicModel(document.getString("name"),"category"));
                }
                no_result.setVisibility(View.GONE);
                search_adapter = new resDetailActivityAdapter2(items,this);
                search_rv.setAdapter(search_adapter);
                search_rv.setLayoutManager(new LinearLayoutManager(this));
                isLoading(false);
            }
            else{
                if(items.size() == 0){
                    no_result.setVisibility(View.VISIBLE);
                    isLoading(false);
                }
            }

        });
        db.collection("items").whereEqualTo("price",cap).get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                for (QueryDocumentSnapshot document : task1.getResult()) {
                    items.add(new resDynamicModel(document.getString("name"),document.getString("price")));
                }
                no_result.setVisibility(View.GONE);
                search_adapter = new resDetailActivityAdapter2(items,this);
                search_rv.setAdapter(search_adapter);
                search_rv.setLayoutManager(new LinearLayoutManager(this));
                isLoading(false);
            }
            else{
                if(items.size() == 0){
                    no_result.setVisibility(View.VISIBLE);
                    isLoading(false);
                }
            }
        });
    }
}