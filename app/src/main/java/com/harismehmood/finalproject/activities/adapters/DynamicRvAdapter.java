package com.harismehmood.finalproject.activities.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Models.DynamicRvModel;
import com.harismehmood.finalproject.activities.Models.StaticRvModel;
import com.harismehmood.finalproject.activities.common.res_detail;

import java.util.ArrayList;
import java.util.List;

public class DynamicRvAdapter extends RecyclerView.Adapter<DynamicRvAdapter.StaticRVViewHolder> {
    private List<DynamicRvModel> items;
    int row_index = -1;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
    public DynamicRvAdapter(List<DynamicRvModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_rv_item, parent, false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRVViewHolder holder, int position) {
        DynamicRvModel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getName());
        holder.textDetails.setText(currentItem.getLocation());
        holder.ratingBar.setRating(currentItem.getRatings());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), res_detail.class);
                intent.putExtra("name", currentItem.getName());
                intent.putExtra("location", currentItem.getLocation());
                intent.putExtra("ratings", currentItem.getRatings());
                intent.putExtra("no_of_reviews", currentItem.getNo_of_reviews());
                intent.putExtra("parking", currentItem.getParking());
                intent.putExtra("id", currentItem.getId());
                v.getContext().startActivity(intent);

                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder{
        TextView textView,textDetails;
        ImageView imageView;
        ConstraintLayout constraintLayout;
        RatingBar ratingBar;

        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.dynamicRv_image);
            textView = itemView.findViewById(R.id.dynamicRv_name);
            textDetails = itemView.findViewById(R.id.dynamicRv_details);
            constraintLayout = itemView.findViewById(R.id.dynamicConstraintLayout);
            ratingBar = itemView.findViewById(R.id.dynamicRv_ratingBar);
        }
    }


}
