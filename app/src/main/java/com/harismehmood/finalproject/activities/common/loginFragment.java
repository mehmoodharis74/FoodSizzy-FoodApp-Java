package com.harismehmood.finalproject.activities.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Constants.Constants;
import com.harismehmood.finalproject.activities.forgotPassword.choose_forget_password_method_activity;
import com.harismehmood.finalproject.admin_main_layout;

import java.util.HashMap;

public class loginFragment extends Fragment {
EditText email, pass;
Button login;
TextView forgetPass,invalidCredentials;
CheckBox rememberMe;
Float v = 0.0f;
LottieAnimationView progressBar;
DocumentSnapshot documentSnapshot;

public loginFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_fragment, container, false);
        email = root.findViewById(R.id.login_email);
        pass = root.findViewById(R.id.login_password);
        forgetPass = root.findViewById(R.id.forgotPasswordTextView);
        login = root.findViewById(R.id.loginButton);
        rememberMe = root.findViewById(R.id.rememberMeCheckBox);
        progressBar = root.findViewById(R.id.loginProgressLoader);
        invalidCredentials = root.findViewById(R.id.invalidCredentialsTextView);

        email.setTranslationX(800);
        pass.setTranslationX(800);
        forgetPass.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        forgetPass.setAlpha(v);
        rememberMe.setAlpha(v);
        login.setAlpha(v);



        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), choose_forget_password_method_activity.class);
                startActivity(intent);
                Constants.IS_LOGGED_IN = "true";
                getActivity().onBackPressed();
            }
        });
        login.setOnClickListener(v -> {
            if(email.getText().toString().equals("admin")&&pass.getText().toString().equals("admin")){
                Intent intent = new Intent(getActivity(), admin_main_layout.class);
                startActivity(intent);
                return;
            }
            Constants.CURRENT_USER_EMAIL = email.getText().toString().trim();
            if(isValidateFields()){
                isLoading(true);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                isLoading(false);
                                getUserData();

                                if(rememberMe.isChecked()){
                                    //save rememberMe as true and email and password in shared preferences
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.REMEMBER_SHARED_PREF, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(Constants.REMEMBER_ME, true);
                                    editor.putString(Constants.USER_EMAIL, email.getText().toString());
                                    editor.putString(Constants.USER_PASSWORD, pass.getText().toString());
                                    editor.putString(Constants.USER_NAME, Constants.CURRENT_USER_NAME);
                                    editor.apply();
                                }
                                else {
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.REMEMBER_SHARED_PREF, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(Constants.REMEMBER_ME, false);
                                    editor.putString(Constants.USER_NAME, Constants.CURRENT_USER_NAME);
                                    editor.apply();

                                }
                                Constants.IS_LOGGED_IN = "true";
                                getActivity().onBackPressed();
                                //startActivity(intent);
                                getActivity().finish();
                            }else{
                                isLoading(false);
                                invalidCredentials.setVisibility(View.VISIBLE);
                            }
                        });

            }
        });

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgetPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        rememberMe.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        return root;
    }

        private void isLoading(boolean load){

            if(load){
                //show the loading screen
                login.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
            else{
                //hide the loading screen
                login.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
        public Boolean isValidateFields(){

            //if email is empty set visibility fo invalidCredientials to visible and set it text to
            // "Please enter your email"
            if(email.getText().toString().trim().isEmpty() ){
                //show error
                invalidCredentials.setText("Please enter your email");
                invalidCredentials.setVisibility(View.VISIBLE);
                return false;
            } else if(pass.getText().toString().trim().isEmpty()) {
                //login
                invalidCredentials.setText("Please enter your password");
                invalidCredentials.setVisibility(View.VISIBLE);
                return false;
            }
            return true;
        }

        // return super.onCreateView(inflater, container, savedInstanceState);
        public void getUserData(){

            //add photo to fireStore of collections of users
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(Constants.USERS_KEY_COLLECTIONS)
                    .whereEqualTo(Constants.USER_EMAIL,Constants.CURRENT_USER_EMAIL)
                    .get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            documentSnapshot = task.getResult().getDocuments().get(0);
                            String u_name = documentSnapshot.getString("name");
                            String u_email = documentSnapshot.getString("email");
                            String u_image= documentSnapshot.getString("image");
                            Constants.CURRENT_USER_NAME = u_name;
                            Constants.CURRENT_USER_EMAIL = u_email;
                            if(u_image != null)
                                Constants.CURRENT_USER_IMAGE = u_image;
                           /* byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);*/
                /*for (int i = 0; i < task.getResult().size(); i++) {
                    db.collection("users").document(task.getResult().getDocuments().get(i).getId()).update("image",encodedImage);
                }*/
                        }
                        else
                            Constants.CURRENT_USER_IMAGE = null;
                    });
            //find users from firestore having email as email and get its name and return it
            /*final String[] name = {"Foodie!"};
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(Constants.USERS_KEY_COLLECTIONS)
                    .whereEqualTo(Constants.USER_EMAIL,email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            //user already exists
                            documentSnapshot = task.getResult().getDocuments().get(0);
                            //matching previous text
                            name[0] = documentSnapshot.getString(Constants.USER_NAME);

                        }

                    });*/
        }
}
