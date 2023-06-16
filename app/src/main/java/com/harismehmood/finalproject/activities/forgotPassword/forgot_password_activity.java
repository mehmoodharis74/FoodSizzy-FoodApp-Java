package com.harismehmood.finalproject.activities.forgotPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.common.login_activity;
import com.hbb20.CountryCodePicker;

public class forgot_password_activity extends AppCompatActivity {
LinearLayout viaEmailLinearLayout, viaPhoneLinearLayout;
EditText mainRegisterPhoneInput;
CountryCodePicker mainRegisterCountryCodePicker;
ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        EditText forgotEmailEditText = findViewById(R.id.forgotPasswordEmailEditText);
        Button forgotPasswordButton = findViewById(R.id.forgotPasswordNextButton);
        viaEmailLinearLayout = findViewById(R.id.emailOptionLayout);
        viaPhoneLinearLayout = findViewById(R.id.phoneOptionLayout);
        mainRegisterPhoneInput = findViewById(R.id.mainRegisterPhoneInput);
        mainRegisterCountryCodePicker = findViewById(R.id.phoneCountrySpinner);
        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
        String method = intent.getStringExtra("method");  //get phone number from previous activities
        if(method.equals("phone")){
            viaPhoneLinearLayout.setVisibility(LinearLayout.VISIBLE);
           // otpDescriptionView.setText(otpDescriptionView.getText().toString() +" Phone number ****"+ phone_no.charAt(phone_no.length()-3)+ phone_no.charAt(phone_no.length()-2)+ phone_no.charAt(phone_no.length()-1));
        }
        else if(method.equals("email")){
            viaEmailLinearLayout.setVisibility(LinearLayout.VISIBLE);
          //  otpDescriptionView.setText(otpDescriptionView.getText().toString() +"Email ");
        }

        forgotPasswordButton.setOnClickListener(v -> {

            if(method.equals("email")){
            if(forgotEmailEditText.getText().toString().isEmpty()) {
                forgotEmailEditText.setError("Please enter your email");
                return;
            }
            sentVerificationEmail(forgotEmailEditText.getText().toString());
            }
            else if (method.equals("phone")) {
               if( mainRegisterPhoneInput.getText().toString().isEmpty()) {
                   mainRegisterPhoneInput.setError("Please enter your phone number");
                   return;
               }

               Intent intent1 = new Intent(forgot_password_activity.this, otp_verification_activity.class);
               intent1.putExtra("method", "phone");
               String phone = mainRegisterCountryCodePicker.getSelectedCountryCodeWithPlus() + mainRegisterPhoneInput.getText().toString();
               intent1.putExtra("phone", phone);
               startActivity(intent1);
            }


        });



    }
    protected void sentVerificationEmail(String email) {
       ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                .setHandleCodeInApp(true)
                .setIOSBundleId("com.example.ios")
                .setAndroidPackageName(
                        "com.harismehmood.finalproject",
                        true,
                        "12" )
                .build();

       //firebase authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(forgot_password_activity.this, "Email has sent successfully.\n Check your email for the verification link", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(forgot_password_activity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //send verification email to user
        Intent intent = getIntent();
        String emailLink = intent.getData().toString();

// Confirm the link is a sign-in with email link.
            if (auth.isSignInWithEmailLink(email)) {
            // Retrieve this from wherever you stored it

            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                AuthResult result = task.getResult();
                                if(result.getAdditionalUserInfo().isNewUser()) {
                                    Intent intent = new Intent(forgot_password_activity.this, login_activity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(forgot_password_activity.this, newPassword_activity.class);
                                    intent.putExtra("email", email);
                                    startActivity(intent);
                                }

                                // You can access the new user via result.getUser()
                                // Additional user info profile *not* available via:
                                // result.getAdditionalUserInfo().getProfile() == null
                                // You can check if the user is new or existing:
                                // result.getAdditionalUserInfo().isNewUser()
                            }
                            else
                                Toast.makeText(forgot_password_activity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
            }

    }
}