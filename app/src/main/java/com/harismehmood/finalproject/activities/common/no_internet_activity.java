package com.harismehmood.finalproject.activities.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.harismehmood.finalproject.R;

public class no_internet_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        TextView retry = findViewById(R.id.retryConnectionButton);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoading(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                public void run() {
                        if(isConnectionAvailable()){
                            Intent intent = new Intent(no_internet_activity.this, mainDashBoard.class);
                            startActivity(intent);
                            finish();
                        }
                        isLoading(false);
                        retry.setText("Try Again");
                        retry.setTextColor(getResources().getColor(R.color.red));
                    }

            }, 2000);


                //sleep for 2 seconds


            }
        });
    }
    private Boolean isConnectionAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.isConnected() || mobile.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    private void isLoading(Boolean load){
        LottieAnimationView progressBar = findViewById(R.id.noInternetProgressLoader);
        TextView retry = findViewById(R.id.retryConnectionButton);
        if(load){
            //show the loading screen
            retry.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            //hide the loading screen
            retry.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}