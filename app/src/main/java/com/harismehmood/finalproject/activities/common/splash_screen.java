package com.harismehmood.finalproject.activities.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Constants.Constants;

public class splash_screen extends AppCompatActivity {
public  static final int SPLASH_TIME_OUT = 5500;
ImageView splashImg;
LottieAnimationView splashLogo;
TextView splashAppNameText, copyRightText;
SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash_screen);
        splashImg = findViewById(R.id.splash_image);
        splashLogo = findViewById(R.id.splashMainLogoAnimation);
        splashAppNameText = findViewById(R.id.splashScreenAppName);
        copyRightText = findViewById(R.id.splashScreenCopyright);

       // splashImg.animate().translationX(-1600).setDuration(1000).setStartDelay(5000);
        splashLogo.animate().translationY(-1400).setDuration(1000).setStartDelay(5000);
        splashAppNameText.animate().translationX(-1400).setDuration(1000).setStartDelay(5000);
        copyRightText.animate().translationX(-1400).setDuration(1000).setStartDelay(5000);


        //set the splash screen to be visible for 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //start the main activity
               /* if(isConnectionAvailable()){
                    Intent intent = new Intent(splash_screen.this, ma.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(splash_screen.this, no_internet_activity.class);
                    startActivity(intent);
                    finish();
                }*/
                Intent intent;
                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);
                if (isFirstTime) {
                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    intent = new Intent(splash_screen.this, onboarding.class);

                } else {
                        if(isConnectionAvailable()){
                            intent = new Intent(splash_screen.this, mainDashBoard.class);
                        }
                        else{
                            intent = new Intent(splash_screen.this, no_internet_activity.class);
                        }
                }
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    private Boolean isConnectionAvailable() {
        //check if the internet connection is available
        //if not, show the error message
        //if yes, continue to the main activity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return wifi.isConnected() || mobile.isConnected();
    }
}