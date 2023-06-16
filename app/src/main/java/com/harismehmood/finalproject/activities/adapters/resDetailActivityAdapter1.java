package com.harismehmood.finalproject.activities.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Models.StaticRvModel;
import com.harismehmood.finalproject.activities.Models.resDynamicModel;
import com.harismehmood.finalproject.activities.Models.resStaticModel;

import java.util.ArrayList;

public class resDetailActivityAdapter1 extends RecyclerView.Adapter<resDetailActivityAdapter1.StaticRVViewHolder> {
    private ArrayList<resStaticModel> items;
    int row_index = -1;
    UpdateRecyclerView2 updateRecyclerView2;
    Activity activity;
    int pos;
    boolean check = true;
    boolean select = true;
    public resDetailActivityAdapter1(ArrayList<resStaticModel> items, Activity activity, UpdateRecyclerView2 updateRecyclerView) {
        this.activity = activity;
        this.updateRecyclerView2 = updateRecyclerView;
        this.items = items;
    }

    @NonNull
    @Override
    public StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_static_item, parent, false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRVViewHolder holder, @SuppressLint("RecyclerView") int position) {
        resStaticModel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        pos = currentItem.getPosition();
        if(check){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            ArrayList<resDynamicModel> items = new ArrayList<resDynamicModel>();
            db.collection("items").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        int image = 0;
                        if (task.getResult().getDocuments().get(i).get("category").toString().equalsIgnoreCase("Pizza")) {
                            image = R.drawable.pizza;
                        } else if (task.getResult().getDocuments().get(i).get("category").toString().equalsIgnoreCase("Burger")) {
                            image = R.drawable.burger;
                        } else if (task.getResult().getDocuments().get(i).get("category").toString().equalsIgnoreCase("Water")) {
                            image = R.drawable.water;
                        } else if (task.getResult().getDocuments().get(i).get("category").toString().equalsIgnoreCase("Shawarma")) {
                            image = R.drawable.shawarma;
                        } else if (task.getResult().getDocuments().get(i).get("category").toString().equalsIgnoreCase("Chips")) {
                            image = R.drawable.products;
                        }
                        items.add(new resDynamicModel(task.getResult().getDocuments().get(i).get("name").toString(), task.getResult().getDocuments().get(i).get("price").toString()));
                    }
                    updateRecyclerView2.callback2(position, items);
                }
                check = false;
            });

        }
        //holder.textView.setText(currentItem.getName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String load = currentItem.getName();
                row_index = position;
                notifyDataSetChanged();
                //load all the items having name = load from firestore

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                ArrayList<resDynamicModel> items = new ArrayList<resDynamicModel>();
                db.collection("items").whereEqualTo("category", load).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (int i = 0; i < task.getResult().size(); i++) {
                            int image = 0;
                            if(task.getResult().getDocuments().get(i).get("category").toString().equalsIgnoreCase("Pizza")){
                                image = R.drawable.pizza;
                            }
                            else if(task.getResult().getDocuments().get(i).get("category").toString().equalsIgnoreCase("Burger")){
                                image = R.drawable.burger;
                            }
                            else if(task.getResult().getDocuments().get(i).get("category").toString().equalsIgnoreCase("Water")){
                                image = R.drawable.water;
                            }
                            else if(task.getResult().getDocuments().get(i).get("category").toString().equalsIgnoreCase("Shawarma")){
                                image = R.drawable.shawarma;
                            }
                            else if(task.getResult().getDocuments().get(i).get("category").toString().equalsIgnoreCase("Chips")){
                                image = R.drawable.products;
                            }
                            items.add(new resDynamicModel(task.getResult().getDocuments().get(i).get("name").toString(), task.getResult().getDocuments().get(i).get("price").toString()));
                        }
                        updateRecyclerView2.callback2(position, items);
                    }
                });

            }
        });

            if (row_index == position)
                holder.linearLayout.setBackgroundResource(R.drawable.res_static_rv_selected_bg);
            else
                holder.linearLayout.setBackgroundResource(R.drawable.res_static_rv_bg);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        LinearLayout linearLayout;



        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.staticRv_image);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }


}
