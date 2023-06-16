package com.harismehmood.finalproject.activities.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Models.DynamicRvModel;
import com.harismehmood.finalproject.activities.Models.StaticRvModel;
import com.harismehmood.finalproject.activities.Models.resDynamicModel;
import com.harismehmood.finalproject.activities.Models.resStaticModel;
import com.harismehmood.finalproject.activities.adapters.DynamicRvAdapter;
import com.harismehmood.finalproject.activities.adapters.StaticRvAdapter;
import com.harismehmood.finalproject.activities.adapters.UpdateRecyclerView;
import com.harismehmood.finalproject.activities.adapters.UpdateRecyclerView2;
import com.harismehmood.finalproject.activities.adapters.resDetailActivityAdapter1;
import com.harismehmood.finalproject.activities.adapters.resDetailActivityAdapter2;

import java.util.ArrayList;
import java.util.List;

public class res_detail extends AppCompatActivity implements UpdateRecyclerView2 {
    private RecyclerView recyclerView, recyclerView2;
    private resDetailActivityAdapter1 staticAdapter;
    List<resDynamicModel> items = new ArrayList<>();
    resDetailActivityAdapter2 dynamicRVAdapter;
    int pos;
    TextView resName, resLocation, resParking, resNum_of_reviews,resRatingCount;
    RatingBar resRatingStars;
    String resId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_detail);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        resName = findViewById(R.id.res_name);
        resLocation = findViewById(R.id.location_text);
        resParking = findViewById(R.id.parking_text);
        resNum_of_reviews = findViewById(R.id.num_of_reviews);
        resRatingCount = findViewById(R.id.review_count);
        resRatingStars = findViewById(R.id.review_stars);

        String parking = intent.getStringExtra("parking");
        if(parking.equalsIgnoreCase("yes"))
            parking = "Parking Available";
        else
            parking = "No Parking";
               //get intent and save it in a variable
        resId = intent.getStringExtra("id");
        resName.setText(intent.getStringExtra("name"));
        resLocation.setText(intent.getStringExtra("location"));
        resParking.setText(parking);
        resNum_of_reviews.setText("("+intent.getStringExtra("no_of_reviews")+" reviews)");
        resRatingStars.setRating(intent.getFloatExtra("ratings",3.0f));
        resRatingCount.setText(String.valueOf(intent.getFloatExtra("ratings",3.0f)));




        ArrayList<resStaticModel> item = new ArrayList<>();
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
                                item.add(new resStaticModel(document.getString("name"), image, pos));

                            }
                            //set static recycler view
                            recyclerView = findViewById(R.id.rv_1);

                            staticAdapter = new resDetailActivityAdapter1(item,  res_detail.this, res_detail.this);
                            //set horizontal layout of recycler view
                            recyclerView.setLayoutManager(new LinearLayoutManager(res_detail.this, LinearLayoutManager.HORIZONTAL, false));
                            recyclerView.setAdapter(staticAdapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                      //  isLoading(false);
                    }

                });


        recyclerView2 = findViewById(R.id.rv_2);
        dynamicRVAdapter = new resDetailActivityAdapter2(items,this);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView2.setAdapter(dynamicRVAdapter);
    }

    @Override
    public void callback2(int position, ArrayList<resDynamicModel> items) {
        dynamicRVAdapter = new resDetailActivityAdapter2(items,this);
        dynamicRVAdapter.notifyDataSetChanged();
        recyclerView2.setAdapter(dynamicRVAdapter);
    }
}