package com.harismehmood.finalproject.activities.common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Constants.Constants;
import com.harismehmood.finalproject.activities.Services.PlaceOrderService;

import java.util.HashMap;


public class addToCart_frag extends BottomSheetDialogFragment {
ImageView item_image;
TextView item_price, item_name,item_total, num_of_items;
ImageButton decButton, incButton;
Button addToCart;
String name, price;
int num_of_items_int = 1, total_price_int, price_int;

;
    public addToCart_frag() {
        // Required empty public constructor
    }

    public addToCart_frag(String name, String price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_to_cart_frag, container, false);
        item_image = root.findViewById(R.id.cart_item_image);
        item_price = root.findViewById(R.id.cart_item_price);
        item_name = root.findViewById(R.id.cart_item_name);
        item_total = root.findViewById(R.id.cart_total_price);
        num_of_items = root.findViewById(R.id.cart_num_of_items);
        decButton = root.findViewById(R.id.cart_decrement_btn);
        incButton = root.findViewById(R.id.cart_increment_btn);
        addToCart = root.findViewById(R.id.addToCart_btn);

        if(num_of_items_int == 1){
            //disable decrement button
            decButton.setEnabled(false);
        }
        num_of_items_int = Integer.parseInt(num_of_items.getText().toString());
        price_int = Integer.parseInt(price);
        total_price_int = num_of_items_int * price_int;

        item_name.setText(name);
        item_price.setText(price);
        item_total.setText(total_price_int + " PKR");
        num_of_items.setText(String.valueOf(num_of_items_int));



        incButton.setOnClickListener(v -> {
            if(!decButton.isEnabled())
                decButton.setEnabled(true);
           // num_of_items.setText();
            //increment price by one
            num_of_items_int++;
            total_price_int = num_of_items_int * price_int;
            num_of_items.setText(String.valueOf(num_of_items_int));
            item_total.setText(total_price_int + " PKR");
        });
        decButton.setOnClickListener(v -> {
            if(num_of_items_int == 1){
                //disable decrement button
                decButton.setEnabled(false);
                return;
            }
            //decrement price by one
            num_of_items_int--;
            total_price_int = num_of_items_int * price_int;
            num_of_items.setText(String.valueOf(num_of_items_int));
            item_total.setText(total_price_int + " PKR");


        });

        addToCart.setOnClickListener(v -> {
            //if user is not logged in then navigate to login activity
            if(!(Constants.IS_LOGGED_IN.equals("true")||Constants.isREMEMBER_ME)){
                Intent intent = new Intent(getContext(), login_activity.class);
                startActivity(intent);
                return;
            }
            HashMap<String, Object> cartItem = new HashMap<>();
            cartItem.put("name", name);
            cartItem.put("price", total_price_int);
            cartItem.put("quantity", num_of_items_int);
            cartItem.put("email", Constants.CURRENT_USER_EMAIL);

            //add cart items to database and close bottom sheet
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(Constants.CART_KEY_COLLECTION).document().set(cartItem).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    generateNotification("CheckOut Cart",name+" is added to cart");
                    getActivity().startService(new Intent(getActivity(), PlaceOrderService.class));
                    dismiss();
                }
                else {
                    //show error
                    Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });



        return root;
    }

    private void generateNotification(String title,String description) {
        int notificationID = 1000;

        String channelID = "cart notification";
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);


        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(getActivity(),
                    channelID)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setChannelId(channelID)
                    .build();
            notificationManager.createNotificationChannel(new NotificationChannel(channelID, "cart notification", NotificationManager.IMPORTANCE_HIGH));
        }
        else{
            notification = new Notification.Builder(getActivity())
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(description)
                    .build();
        }

        notificationManager.notify(notificationID, notification);

    }
}