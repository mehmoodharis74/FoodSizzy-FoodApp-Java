package com.harismehmood.finalproject.activities.forgotPassword;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.harismehmood.finalproject.R;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otp_verification_activity extends AppCompatActivity {
    TextView otpDescriptionView, resend_otp_textView;
    ImageView bachArrowBtn;
    Button verifyOTPBtn;
    PinView pinView;
    FirebaseAuth mAuth;
    String verificationId;
    String enteredOTP_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_otp_verification);
        otpDescriptionView = findViewById(R.id.otpDescriptionTextView);
        verifyOTPBtn = findViewById(R.id.BtnVerifyOTP);
        pinView = findViewById(R.id.pinView);
        resend_otp_textView = findViewById(R.id.resendOtp);
        bachArrowBtn = findViewById(R.id.backArrow);


        //back arrow button call function of onBackPressed
        bachArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //get phone number from previous activities
        Intent intent = getIntent();
        String method = intent.getStringExtra("method");  //get phone number from previous activities
        String phone_no = intent.getStringExtra("phone");  //get phone number from previous activities
        if(method.equals("phone")){
            otpDescriptionView.setText(otpDescriptionView.getText().toString() +" Phone number ****"+ phone_no.charAt(phone_no.length()-3)+ phone_no.charAt(phone_no.length()-2)+ phone_no.charAt(phone_no.length()-1));
        }
        else if(method.equals("email")){
            otpDescriptionView.setText(otpDescriptionView.getText().toString() +"Email ");
        }
        mAuth = FirebaseAuth.getInstance();


        //reset otp text view
        resend_otp_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoading(true);
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(phone_no)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(otp_verification_activity.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        //set focus listener on pin view
        pinView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    pinView.setLineColor(getResources().getColor(R.color.blue));
                    pinView.setItemBackgroundColor(getResources().getColor(R.color.pinViewBackground));
                    isLoading(false);
                }
            }
        });
        //remove focus of pinview if user click on any other view
        findViewById(R.id.otpVerificationMainLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinView.clearFocus();
                pinView.setLineColor(getResources().getColor(R.color.blue));
                pinView.setItemBackgroundColor(getResources().getColor(R.color.pinViewBackground));
            }
        });
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(pinView.getText().toString().isEmpty()||pinView.getText().toString().length()<6){
                    pinView.setLineColor(getResources().getColor(R.color.red));
                    pinView.setItemBackgroundColor(getResources().getColor(R.color.pinViewErrorBackground));
                    Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake_animation);
                    pinView.setAnimation(animation);
                    pinView.startAnimation(animation);
                    return;
                }
                isLoading(true);

                enteredOTP_Btn = pinView.getText().toString();
                Intent intent = new Intent(otp_verification_activity.this, newPassword_activity.class);
                startActivity(intent);
                finish();
                verifyCode(enteredOTP_Btn);

            }
        });

        sendVerificationCode(phone_no);

    }
    private void sendVerificationCode(String phone) {
        isLoading(true);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {

                verifyCode(code);
                pinView.setText(code);
                isLoading(false);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            isLoading(false);
            Toast.makeText(otp_verification_activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            isLoading(false);
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //create a builder
                            isLoading(false);
//                            Dialog builder = new Dialog(otp_verification_activity.this);
//                            builder.setContentView(R.layout.signup_success_dialog);
//                            builder.show();
                            //dismiss dialog builder after 3 seconds

                                            Intent intent = new Intent(otp_verification_activity.this, newPassword_activity.class);
                                            intent.putExtra("phone", getIntent().getStringExtra("phone"));
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();

                        } else {
                            Toast.makeText(otp_verification_activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(otp_verification_activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void isLoading(Boolean isLoading){
        LottieAnimationView progressBar = findViewById(R.id.otpWaitingProgressBar);
        if(isLoading){

            verifyOTPBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            verifyOTPBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}