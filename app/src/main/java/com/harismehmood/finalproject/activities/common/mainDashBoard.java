package com.harismehmood.finalproject.activities.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Constants.Constants;
import com.harismehmood.finalproject.activities.Models.DynamicRvModel;
import com.harismehmood.finalproject.activities.Models.StaticRvModel;
import com.harismehmood.finalproject.activities.adapters.DynamicRvAdapter;
import com.harismehmood.finalproject.activities.adapters.StaticRvAdapter;
import com.harismehmood.finalproject.activities.adapters.UpdateRecyclerView;
import com.harismehmood.finalproject.search_layout;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class mainDashBoard extends AppCompatActivity implements UpdateRecyclerView {

    private RecyclerView recyclerView, recyclerView2;
    private StaticRvAdapter staticAdapter;
    List<DynamicRvModel> items = new ArrayList<>();
    DynamicRvAdapter dynamicRVAdapter;
    ImageView my_profile,shopping_cart,search_icon;
    TextView username;
    LottieAnimationView progressBar;
    int pos;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board);
        username = findViewById(R.id.mainDashBoard_userName);
        //load shared prefrences
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.REMEMBER_SHARED_PREF, MODE_PRIVATE);
//fetching data from shared prefrences
        Constants.isREMEMBER_ME = sharedPreferences.getBoolean(Constants.REMEMBER_ME, false);
        Constants.CURRENT_USER_NAME = sharedPreferences.getString(Constants.USER_NAME, "");
//setting the name of the user on main screen
        if(Constants.isREMEMBER_ME){
            Constants.CURRENT_USER_EMAIL = sharedPreferences.getString(Constants.USER_EMAIL, "");
            Constants.CURRENT_USER_PASS = sharedPreferences.getString(Constants.USER_PASSWORD, "");
        }
       // loadUserData();
        if(Constants.CURRENT_USER_NAME.equals(""))
            username.setText("Foodie!");
        else
        username.setText(Constants.CURRENT_USER_NAME);
        progressBar = findViewById(R.id.staticRv_progressBar);


//creating a list of static items
        isLoading(true);
        ArrayList<StaticRvModel> item = new ArrayList<>();
FirebaseFirestore db = FirebaseFirestore.getInstance();
//select all from categories collection
        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //add data to static recycler view
                                int image = 0;
                                if(document.get("name").equals("Pizza")){
                                   image = R.drawable.pizza;
                                }
                                else if(document.get("name").equals("Burger")){
                                    image = R.drawable.burger;
                                }
                                else if(document.get("name").equals("Water")){
                                    image = R.drawable.water;
                                }
                                else if(document.get("name").equals("Shawarma")){
                                    image = R.drawable.shawarma;
                                }
                                else if(document.get("name").equals("Chips")){
                                    image = R.drawable.products;
                                }
                                item.add(new StaticRvModel(document.getString("name"), image));

                            }
                            //set static recycler view
                            recyclerView = findViewById(R.id.rv_1);

                            staticAdapter = new StaticRvAdapter(item, mainDashBoard.this, mainDashBoard.this);
                            //set horizontal layout of recycler view
                            recyclerView.setLayoutManager(new LinearLayoutManager(mainDashBoard.this, LinearLayoutManager.HORIZONTAL, false));
                            recyclerView.setAdapter(staticAdapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                        isLoading(false);
                    }

                });


        recyclerView2 = findViewById(R.id.rv_2);
        dynamicRVAdapter = new DynamicRvAdapter(items);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView2.setAdapter(dynamicRVAdapter);


        my_profile = findViewById(R.id.profile_icon);
        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = getIntent();
                if(Constants.IS_LOGGED_IN.equals("true")||Constants.isREMEMBER_ME){
                    intent = new Intent(mainDashBoard.this, myProfile.class);
                }
                else {
                    intent = new Intent(mainDashBoard.this, login_activity.class);
                }
                startActivity(intent);
            }
        });
        shopping_cart = findViewById(R.id.shopping_cart_icon);
        shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = getIntent();
                if(Constants.IS_LOGGED_IN.equals("true")||Constants.isREMEMBER_ME){
                    intent = new Intent(mainDashBoard.this, com.harismehmood.finalproject.activities.common.shopping_cart.class);
                }
                else {
                    intent = new Intent(mainDashBoard.this, login_activity.class);
                }
                startActivity(intent);
            }
        });

        search_icon = findViewById(R.id.search_icon);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainDashBoard.this, search_layout.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void callback(int position, ArrayList<DynamicRvModel> items) {

        dynamicRVAdapter = new DynamicRvAdapter(items);
        dynamicRVAdapter.notifyDataSetChanged();
        recyclerView2.setAdapter(dynamicRVAdapter);

    }
    private void isLoading(boolean load){
            if(load){
                progressBar.setVisibility(View.VISIBLE);
            }
            else{
                progressBar.setVisibility(View.GONE);
            }

    }
    public void oneStopNotification(){

    }
}