package com.harismehmood.finalproject.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Models.DynamicRvModel;
import com.harismehmood.finalproject.activities.Models.resDynamicModel;
import com.harismehmood.finalproject.activities.common.addToCart_frag;
import com.harismehmood.finalproject.activities.common.res_detail;

import java.util.List;

public class resDetailActivityAdapter2 extends RecyclerView.Adapter<resDetailActivityAdapter2.StaticRVViewHolder> {
    private List<resDynamicModel> items;
    Context context;
    int row_index = -1;

    public resDetailActivityAdapter2(List<resDynamicModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_dynamic_item, parent, false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRVViewHolder holder, int position) {
        resDynamicModel currentItem = items.get(position);
        holder.textView.setText(currentItem.getName());
        if(currentItem.getPrice().equalsIgnoreCase("category")){
            holder.textPrice.setText(currentItem.getPrice());
        }
        else{
            holder.textPrice.setText("Price: " + currentItem.getPrice() + " PKR");
        }


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart_frag bottomView = new addToCart_frag(currentItem.getName(), currentItem.getPrice());
                bottomView.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomView.getTag());

            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder{
        TextView textView,textPrice;
        ConstraintLayout constraintLayout;

        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.dynamicRv_name);
            textPrice = itemView.findViewById(R.id.d_price);
            constraintLayout = itemView.findViewById(R.id.res_dynamic_constraintLayout);

        }
    }


}
