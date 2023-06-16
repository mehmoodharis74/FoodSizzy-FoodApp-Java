package com.harismehmood.finalproject.activities.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Models.DynamicRvModel;
import com.harismehmood.finalproject.activities.Models.StaticRvModel;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class StaticRvAdapter extends RecyclerView.Adapter<StaticRvAdapter.StaticRVViewHolder> {
    private ArrayList<StaticRvModel> items;
    int row_index = -1;
    UpdateRecyclerView updateRecyclerView;
    Activity activity;
    boolean check = true;
    boolean select = true;
    public StaticRvAdapter(ArrayList<StaticRvModel> items, Activity activity, UpdateRecyclerView updateRecyclerView) {
        this.activity = activity;
        this.updateRecyclerView = updateRecyclerView;
        this.items = items;
    }

    @NonNull
    @Override
    public StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_rv_item, parent, false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRVViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StaticRvModel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getName());
        if(check){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            ArrayList<DynamicRvModel> items = new ArrayList<DynamicRvModel>();
            db.collection("restaurants").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    int image;
                    for (int i = 0; i < task.getResult().size(); i++) {
                        if(i == 0)
                            image = R.drawable.res1;
                        else if(i == 1)
                            image = R.drawable.res2;
                        else if(i == 2)
                            image = R.drawable.res3;
                        else if(i == 3)
                            image = R.drawable.res4;
                        else
                            image = R.drawable.res5;

                        items.add(new DynamicRvModel(task.getResult().getDocuments().get(i).getId(), image,
                                task.getResult().getDocuments().get(i).get("id").toString(),task.getResult().getDocuments().get(i).get("isParking").toString(),
                                task.getResult().getDocuments().get(i).get("ratings").toString(),task.getResult().getDocuments().get(i).get("reviews").toString(),
                                task.getResult().getDocuments().get(i).get("location").toString()));
                    }
                    updateRecyclerView.callback(position, items);
                    check = false;
                }
            });

        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String load = currentItem.getName();
                row_index = position;
                notifyDataSetChanged();
                //load all the items having name = load from firestore

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                ArrayList<DynamicRvModel> items = new ArrayList<DynamicRvModel>();
                //int i ;
                db.collection("items").whereEqualTo("category", load).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (int i =0 ; i < task.getResult().size(); i++) {
                            String resName = task.getResult().getDocuments().get(i).get("restaurant").toString();
                            //select all from db collection restaurants where document.id = task.getResult().getDocuments().get(i).get("restaurant").toString()
                            db.collection("restaurants").whereEqualTo("name", resName)
                                    .get().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            int image;
                                            if (task1.getResult().getDocuments().get(0).get("id").toString().equalsIgnoreCase( "1"))
                                                image = R.drawable.res1;
                                            else if (task1.getResult().getDocuments().get(0).get("id").toString().equalsIgnoreCase( "2"))
                                                image = R.drawable.res2;
                                            else if (task1.getResult().getDocuments().get(0).get("id").toString().equalsIgnoreCase( "3"))
                                                image = R.drawable.res3;
                                            else if (task1.getResult().getDocuments().get(0).get("id").toString().equalsIgnoreCase( "4"))
                                                image = R.drawable.res4;
                                            else
                                                image = R.drawable.res5;

                                            items.add(new DynamicRvModel(task1.getResult().getDocuments().get(0).getId(), image,
                                                    task1.getResult().getDocuments().get(0).get("id").toString(), task1.getResult().getDocuments().get(0).get("isParking").toString(),
                                                    task1.getResult().getDocuments().get(0).get("ratings").toString(), task1.getResult().getDocuments().get(0).get("reviews").toString(),
                                                    task1.getResult().getDocuments().get(0).get("location").toString()));


                                        }

                                        updateRecyclerView.callback(position, items);
                                    });
                            }
                    }

                });

            }
        });

            if (row_index == position)
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected_bg);
            else
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_bg);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;



        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.staticRv_image);
            textView = itemView.findViewById(R.id.staticRv_text);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }


}
