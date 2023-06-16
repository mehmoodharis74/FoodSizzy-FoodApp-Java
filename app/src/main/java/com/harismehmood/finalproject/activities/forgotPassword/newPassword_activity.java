package com.harismehmood.finalproject.activities.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.common.login_activity;

public class newPassword_activity extends AppCompatActivity {
Button updatePasswordBtn;
EditText newPasswordEditText, confirmPasswordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        updatePasswordBtn = findViewById(R.id.updatePasswordButton);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.newConfirmPasswordEditText);
        LinearLayout linearLayout = findViewById(R.id.newPasswordMainLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPasswordEditText.clearFocus();
                confirmPasswordEditText.clearFocus();
                overridePendingTransition(R.anim.slide_out_top_animation, R.anim.slide_in_top_animation);
                TextView errorTextView = findViewById(R.id.errorShowTextView);
                errorTextView.setVisibility(View.GONE);
            }
        });
        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateInput()){
                    Intent intent = new Intent(newPassword_activity.this, login_activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public Boolean validateInput(){
        //set Animation
       // Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_top_animation);
        TextView errorTextView = findViewById(R.id.errorShowTextView);
        if(newPasswordEditText.getText().toString().isEmpty()){
            errorTextView.setText("Enter your new password");
            errorTextView.setVisibility(View.VISIBLE);
            //errorTextView.startAnimation(animation);
            overridePendingTransition(R.anim.slide_in_top_animation, R.anim.slide_out_top_animation);
            return false;
        }
        else if(confirmPasswordEditText.getText().toString().isEmpty()){
            errorTextView.setText("Enter your confirm password");
            errorTextView.setVisibility(View.VISIBLE);
            //errorTextView.startAnimation(animation);
            overridePendingTransition(R.anim.slide_in_top_animation, R.anim.slide_out_top_animation);
            return false;
        }
        if(!newPasswordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString()) &&
                !newPasswordEditText.getText().toString().isEmpty() &&
                !confirmPasswordEditText.getText().toString().isEmpty()){
            //confirmPasswordEditText.setError("Passwords do not match");
            errorTextView.setText("Passwords do not match");
            errorTextView.setVisibility(View.VISIBLE);
            //errorTextView.startAnimation(animation);
            overridePendingTransition(R.anim.slide_in_top_animation, R.anim.slide_out_top_animation);

            return false;
        }
        //if Password length is less than 8
        if(newPasswordEditText.getText().toString().length() < 8){
           // newPasswordEditText.setError("Password must be at least 8 characters long");
            errorTextView.setText("Password must be at least 8 characters long");
            errorTextView.setVisibility(View.VISIBLE);
            //errorTextView.startAnimation(animation);
            overridePendingTransition(R.anim.slide_in_top_animation, R.anim.slide_out_top_animation);
            return false;
        }

        return true;
    }
}