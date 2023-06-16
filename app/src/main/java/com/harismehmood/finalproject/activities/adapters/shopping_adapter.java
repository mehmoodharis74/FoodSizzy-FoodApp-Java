package com.harismehmood.finalproject.activities.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Constants.Constants;
import com.harismehmood.finalproject.activities.Models.DynamicRvModel;
import com.harismehmood.finalproject.activities.Models.shoppingModel;

import java.util.ArrayList;

public class shopping_adapter extends RecyclerView.Adapter<shopping_adapter.ViewHolder> {
    Context context;
 ArrayList<shoppingModel> items;
    public shopping_adapter(Context context, ArrayList<shoppingModel> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(context).inflate(R.layout.shopping_item, parent,false);
        ViewHolder view_holder=new ViewHolder(item);
        return view_holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int pricePerItem = Integer.parseInt(items.get(position).getPrice()) / Integer.parseInt(items.get(position).getQuantity());

        holder.name.setText(items.get(position).getName());
        holder.price.setText(String.valueOf(pricePerItem));
        holder.quantity.setText(items.get(position).getQuantity());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(Constants.CART_KEY_COLLECTION).whereEqualTo(Constants.USER_EMAIL,Constants.CURRENT_USER_EMAIL).whereEqualTo("name",items.get(position).getName()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (int i = 0; i < task.getResult().size(); i++) {
                            db.collection(Constants.CART_KEY_COLLECTION).document(task.getResult().getDocuments().get(i).getId()).delete();
                            items.remove(position);
                            notifyItemRemoved(position);
                            //refresh the current activity

                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity,remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shoppingItemName);
            price = itemView.findViewById(R.id.shoppingItemPrice);
            quantity = itemView.findViewById(R.id.shoppingItemQuantity);
            remove = itemView.findViewById(R.id.shoppingItemRemove);
        }
    }
}
