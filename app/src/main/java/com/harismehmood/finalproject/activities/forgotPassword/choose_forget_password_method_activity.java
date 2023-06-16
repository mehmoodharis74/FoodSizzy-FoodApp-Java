package com.harismehmood.finalproject.activities.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.forgotPassword.otp_verification_activity;

public class choose_forget_password_method_activity extends AppCompatActivity {
LinearLayout viaEmailLinearLayout, viaPhoneLinearLayout;
ImageView backArrowBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_forget_password_method);
        //on click of emaillinearlayout create intent to otp_verification_activity and put string of email
        //on click of phonelinearlayout create intent to otp_verification_activity and put string of phone

        viaEmailLinearLayout = findViewById(R.id.chooseMethodEmailLayout);
        viaPhoneLinearLayout = findViewById(R.id.chooseMethodPhoneLayout);
        backArrowBtn = findViewById(R.id.backArrow);
        backArrowBtn.setOnClickListener(v -> {
            super.onBackPressed();
        });

        viaEmailLinearLayout.setOnClickListener(v -> {
            //create intent to otp_verification_activity and put string of email
            Intent intent = new Intent(this, forgot_password_activity.class);
            intent.putExtra("method", "email");
            startActivity(intent);
        });

        viaPhoneLinearLayout.setOnClickListener(v -> {
            //create intent to otp_verification_activity and put string of phone
            Intent intent = new Intent(this, forgot_password_activity.class);
            intent.putExtra("method", "phone");
            startActivity(intent);
        });
    }
}