package com.harismehmood.finalproject.activities.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Constants.Constants;
import com.harismehmood.finalproject.activities.Models.shoppingModel;
import com.harismehmood.finalproject.activities.adapters.DynamicRvAdapter;
import com.harismehmood.finalproject.activities.adapters.shopping_adapter;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class shopping_cart extends AppCompatActivity {
ArrayList<shoppingModel> items;
RecyclerView recyclerView;
shopping_adapter adapter;
TextView subTotalText,shippingText, totalText, emptyCart;
ImageView backArrow;
LottieAnimationView progressBar;
LinearLayout shopping_bottom_bar;
Button placeOrderBtn;
int subtotal =0, total=0, shipping=0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

subTotalText = findViewById(R.id.shopping_subtotal);
shippingText = findViewById(R.id.shopping_shipping);
totalText  = findViewById(R.id.shopping_total);
backArrow = findViewById(R.id.backArrow);
recyclerView = findViewById(R.id.shoppingCartRecyclerView);
progressBar = findViewById(R.id.loginProgressLoader);
shopping_bottom_bar = findViewById(R.id.shopping_bottom_bar);
emptyCart  = findViewById(R.id.noCartItems);
placeOrderBtn = findViewById(R.id.placeOrder_btn);

items = new ArrayList<>();
isLoading(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.CART_KEY_COLLECTION).whereEqualTo(Constants.USER_EMAIL,Constants.CURRENT_USER_EMAIL).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(task.getResult().size()>0) {
                    for (int i = 0; i < task.getResult().size(); i++) {

                        items.add(new shoppingModel(task.getResult().getDocuments().get(i).get("name").toString(), task.getResult().getDocuments().get(i).get("price").toString()
                                , task.getResult().getDocuments().get(i).get("quantity").toString()));
                        subtotal = subtotal + Integer.parseInt(task.getResult().getDocuments().get(i).get("price").toString());
                    }
                    isLoading(false);
                    shipping = (int) (Math.random() * 100) + 50;
                    total = subtotal + shipping;
                    subTotalText.setText(subtotal + " PKR");
                    shippingText.setText(shipping + " PKR");
                    totalText.setText(total + " PKR");
                    shopping_bottom_bar.setVisibility(View.VISIBLE);
                }
                else{
                    isLoading(false);
                    emptyCart.setVisibility(View.VISIBLE);
                    shopping_bottom_bar.setVisibility(View.GONE);
                }
            }

        });


        adapter = new shopping_adapter(this,items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


        backArrow.setOnClickListener(v -> {
            super.onBackPressed();
            finish();
        });

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            String title = "Order Placed";
            String message = "Your order has been placed successfully. Your order will be delivered in 2-3 days. Thank you for shopping with us.";
            JSONObject json;
            @Override
            public void onClick(View view) {
                String playerId = OneSignal.getDeviceState().getUserId();
                try {
// json=new JSONObject("{'contents':{'en':'vdvdvdvd'}, 'headings':{'en':'vdvdvdvd'}, 'include_player_ids':['f81724c8-1e00-4de4-9766-ecc40a62bb84']}");
                    json = new JSONObject("{'contents': {'en':'" +
                            message +
                            "'}, 'headings':{'en':'"
                            + title +
                            "'}, 'include_player_ids': ['" +
                            playerId + "']}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OneSignal.postNotification(json, new OneSignal.PostNotificationResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        // Log.d("response-success",jsonObject.toString());
                    }

                    @Override
                    public void onFailure(JSONObject jsonObject) {
                        Toast.makeText(shopping_cart.this, "Notification Failure", Toast.LENGTH_LONG);
                    }
                });

                //delete the collection cart from database
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(Constants.CART_KEY_COLLECTION).whereEqualTo(Constants.USER_EMAIL,Constants.CURRENT_USER_EMAIL).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(task.getResult().size()>0) {
                            for (int i = 0; i < task.getResult().size(); i++) {
                                db.collection(Constants.CART_KEY_COLLECTION).document(task.getResult().getDocuments().get(i).getId()).delete();
                            }
                        }
                        onBackPressed();
                    }
                });


            }
        });

    }
    private void isLoading(boolean load){

        if(load){
            //show the loading screen
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            //hide the loading screen
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}