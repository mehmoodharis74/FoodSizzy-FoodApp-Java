package com.harismehmood.finalproject.activities.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Constants.Constants;

import java.util.HashMap;

public class signupFragment extends Fragment {

    EditText email, pass,confirm_pass,username;
    Button signup;
    LottieAnimationView progressBar;
    SharedPreferences sharedPreferences;
public signupFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment, container, false);
        email = root.findViewById(R.id.signup_email);
        pass = root.findViewById(R.id.signup_password);
        signup = root.findViewById(R.id.signup_button);
        confirm_pass = root.findViewById(R.id.signup_password_confirm);
        progressBar = root.findViewById(R.id.loginProgressLoader);
        username = root.findViewById(R.id.signup_username);

        //set sharedPreferences to new instance of PreferencesManager

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidateFields()) {
                    isLoading(true);
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                           addDataToFirebase();

                        } else {
                            isLoading(false);
                            email.setError("Email already exists");
                        }
                    });
                }
            }
        });


        return root;
    }
    private void isLoading(boolean load){

        if(load){
            //show the loading screen
            signup.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            //hide the loading screen
            signup.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
    public Boolean isValidateFields(){

        //if email is empty set visibility fo invalidCredientials to visible and set it text to
        // "Please enter your email"
        if(username.getText().toString().trim().isEmpty()) {
            //login
            username.setError("Please enter your password");
            return false;
        }
        else if(email.getText().toString().trim().isEmpty() ){
            //show error
            email.setError("Please enter your email");
            return false;
        } else if(pass.getText().toString().trim().isEmpty()) {
            //login
            pass.setError("Please enter your password");
            return false;
        }
        else if(confirm_pass.getText().toString().trim().isEmpty()) {
            //login
            confirm_pass.setError("Please enter your password");
            return false;
        }
        //if password doestnot match
        else if(pass.getText().toString().trim().length()<6){
            //show error
            pass.setError("Password must be atleast 6 characters");
            return false;
        }
        else if(!pass.getText().toString().trim().equals(confirm_pass.getText().toString().trim())){
            //show error
            confirm_pass.setError("Passwords do not match");
            return false;
        }
        return true;
    }
    public void addDataToFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.USER_NAME, username.getText().toString());
        user.put(Constants.USER_EMAIL, email.getText().toString());
        user.put(Constants.USER_PASSWORD, pass.getText().toString());
        //add data to firebase
        db.collection(Constants.USERS_KEY_COLLECTIONS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    isLoading(false);
                   /* Constants.CURRENT_USER_EMAIL = email.getText().toString();
                    Constants.CURRENT_USER_NAME = username.getText().toString();
                    Constants.CURRENT_USER_PASS = pass.getText().toString();*/
                    Intent intent1 = new Intent(getActivity(), login_activity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    // Toast.makeText(firstTime_InputData_activity.this, "Data Added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    isLoading(false);
                    Toast.makeText(getActivity(), "Unable to add data", Toast.LENGTH_SHORT).show();
                });
    }
}

