package com.harismehmood.finalproject.activities.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Constants.Constants;
import com.harismehmood.finalproject.activities.adapters.LoginAdapter;
import com.harismehmood.finalproject.activities.forgotPassword.choose_forget_password_method_activity;
import com.harismehmood.finalproject.activities.forgotPassword.forgot_password_activity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.HashMap;

public class login_activity extends AppCompatActivity {
    Button loginButton;
    TabLayout tabLayout;
    ViewPager viewPager;
    Float v = 0f;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        loginButton = findViewById(R.id.loginButton);

        //add tabs of login and signup in tab layout
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter) ;

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTranslationY(300);
        tabLayout.setAlpha(v);


        /*loginButton.setOnClickListener(v -> {
            if(isValidateFields()){
            isLoading(true);
            new Handler().postDelayed(() -> {

                isLoading(false);
            }, 2000);
            }
        });*/



    }
    private void isLoading(Boolean load){
      /*  LottieAnimationView progressBar = findViewById(R.id.loginProgressLoader);
        loginButton = findViewById(R.id.loginButton);
        if(load){
            //show the loading screen
            loginButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            //hide the loading screen
            loginButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }*/
    }
    private Boolean isValidateFields(){
       /* TextView invalidCredentials = findViewById(R.id.invalidCredentialsTextView);
        EditText email = findViewById(R.id.login_email);
        EditText password = findViewById(R.id.login_password);
        //if email is empty set visibility fo invalidCredientials to visible and set it text to
        // "Please enter your email"
        if(email.getText().toString().trim().isEmpty() ){
            //show error
            invalidCredentials.setText("Please enter your email");
            invalidCredentials.setVisibility(View.VISIBLE);
            return false;
        } else if(password.getText().toString().trim().isEmpty()) {
            //login
            invalidCredentials.setText("Please enter your password");
            invalidCredentials.setVisibility(View.VISIBLE);
            return false;
        }*/
        return true;
    }
}