package com.harismehmood.finalproject.activities.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.adapters.OnBoardSliderAdapter;


public class onboarding extends AppCompatActivity {
ViewPager viewPager;
LinearLayout dotsLayout;
TextView dots[],skipText,splashAppNameText,copyRightText;
ImageButton nextBtn;
Button getStartedBtn;
OnBoardSliderAdapter onBoardSliderAdapter;
Animation animation;
ImageView splashImg;
LottieAnimationView splashLogo;

public static int SKIP_BUTTON_VISIBLE_SLIDE_LIMIT = 2;
public static int TOTAL_ON_BOARDING_SLIDES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.onBoardViewPager);
        dotsLayout = findViewById(R.id.onBoardingDots);
        nextBtn = findViewById(R.id.onBoardNextBtn);
        getStartedBtn = findViewById(R.id.OnBoardGetStartBtn);
        //getStartedBtn.setVisibility(View.INVISIBLE);
        skipText = findViewById(R.id.skipText);
        //skipText.setVisibility(View.INVISIBLE);

//        splashImg = findViewById(R.id.splash_image);
//        splashLogo = findViewById(R.id.splashMainLogoAnimation);
//        splashAppNameText = findViewById(R.id.splashScreenAppName);
//        copyRightText = findViewById(R.id.splashScreenCopyright);
//
//        splashImg.animate().translationX(-1600).setDuration(1000).setStartDelay(5000);
//        splashLogo.animate().translationY(-1400).setDuration(1000).setStartDelay(5000);
//        splashAppNameText.animate().translationX(-1400).setDuration(1000).setStartDelay(5000);
//        copyRightText.animate().translationX(-1400).setDuration(1000).setStartDelay(5000);

        onBoardSliderAdapter = new OnBoardSliderAdapter(this);
        viewPager.setAdapter(onBoardSliderAdapter);



        //setting dotes on the bottom
        dotAdd(0);
        viewPager.addOnPageChangeListener(viewListener);

        //skip text on click to skip all the slides
        skipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(onboarding.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                viewPager.setCurrentItem(TOTAL_ON_BOARDING_SLIDES-1);
            }
        });

    }
    private void dotAdd(int position){
        dots = new TextView[TOTAL_ON_BOARDING_SLIDES];
        dotsLayout.removeAllViews();
        for(int i = 0; i < TOTAL_ON_BOARDING_SLIDES; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

           dotsLayout.addView(dots[i]);
        }
        //set the color of the current dot
        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.purple_500));
        }


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(position+1);
            }
        });

    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {


            if(position >= SKIP_BUTTON_VISIBLE_SLIDE_LIMIT-1){
                skipText.setVisibility(View.VISIBLE);
            }
            else{
                skipText.setVisibility(View.INVISIBLE);
            }
            //if the slide is the last one then change the visibility of get started button and on click listener to open the main activities
            if(position == TOTAL_ON_BOARDING_SLIDES-1){
                skipText.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.INVISIBLE);
                dotsLayout.setVisibility(View.INVISIBLE);
                //setting animation of the get started button
                animation = AnimationUtils.loadAnimation(onboarding.this,R.anim.onboard_getstartedbtn_anim);
                getStartedBtn.setAnimation(animation);
                getStartedBtn.setVisibility(View.VISIBLE);
                getStartedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isConnectionAvailable()){
                            Intent intent = new Intent(onboarding.this, mainDashBoard.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent = new Intent(onboarding.this, no_internet_activity.class);
                            startActivity(intent);

                        }
                    }
                });
            }
            else{
                nextBtn.setVisibility(View.VISIBLE);
                dotsLayout.setVisibility(View.VISIBLE);
                getStartedBtn.setVisibility(View.INVISIBLE);
            }

            dotAdd(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
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