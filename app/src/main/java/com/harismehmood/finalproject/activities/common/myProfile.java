package com.harismehmood.finalproject.activities.common;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harismehmood.finalproject.R;
import com.harismehmood.finalproject.activities.Constants.Constants;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class myProfile extends AppCompatActivity {
ImageButton logout;
TextView addPhotoTextView, name, email;
String encodedImage;
ImageView profileImage, backArrow;
boolean isPhotoAdded=false;
    DocumentSnapshot documentSnapshot;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        logout=findViewById(R.id.sign_out_button);
        profileImage=findViewById(R.id.user_Profile_image);
        name=findViewById(R.id.user_Profile_name);
        email=findViewById(R.id.user_Profile_email);
        addPhotoTextView = findViewById(R.id.add_photo_text);
        backArrow = findViewById(R.id.backArrow);

        loadUserData();

        logout.setOnClickListener(view -> {
            Constants.IS_LOGGED_IN="false";
            Constants.isREMEMBER_ME=false;
            Constants.CURRENT_USER_NAME = "";
            Constants.CURRENT_USER_EMAIL="";
            Constants.CURRENT_USER_IMAGE="";
            //clear shared preferences of Constants.SHARED_PREFS
            SharedPreferences sharedPreferences=getSharedPreferences(Constants.REMEMBER_SHARED_PREF,MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.apply();
            finish();
        });

        profileImage.setOnClickListener(v -> {
            //open gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);

        });

        backArrow.setOnClickListener(v ->{
            super.onBackPressed();
        });
    }

    public String setEncodedImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            // imageView.setImageURI(data.getData());
            Uri uri = data.getData();
            addPhotoTextView.setVisibility(View.GONE);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profileImage.setImageBitmap(bitmap);
                encodedImage = setEncodedImage(bitmap);
                addPhotoToDatabase();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                isPhotoAdded = false;









            }
        }
        else
        {
            addPhotoTextView.setVisibility(View.VISIBLE);
        }
    }
    private void addPhotoToDatabase() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("image", encodedImage);
        //add photo to fireStore of collections of users
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.USERS_KEY_COLLECTIONS).
                whereEqualTo(Constants.USER_EMAIL,Constants.CURRENT_USER_EMAIL)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                documentSnapshot = task.getResult().getDocuments().get(0);
                /*name.setText(documentSnapshot.getString("name"));
                email.setText(documentSnapshot.getString("email"));*/


                    //add image to firestore
                    //update user
                    db.collection(Constants.USERS_KEY_COLLECTIONS)
                            .document(documentSnapshot.getId())
                            .update(user);
                isPhotoAdded = true;

                /*for (int i = 0; i < task.getResult().size(); i++) {
                    db.collection("users").document(task.getResult().getDocuments().get(i).getId()).update("image",encodedImage);
                }*/
            }
            else
                isPhotoAdded = false;
                });
    }
    public void loadUserData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.USERS_KEY_COLLECTIONS).
                whereEqualTo(Constants.USER_EMAIL,Constants.CURRENT_USER_EMAIL)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        documentSnapshot = task.getResult().getDocuments().get(0);
                        name.setText(documentSnapshot.getString("name"));
                        email.setText(documentSnapshot.getString("email"));
                        if(documentSnapshot.getString("image") !=null) {
                            encodedImage = documentSnapshot.getString("image");
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            profileImage.setImageBitmap(decodedByte);
                            addPhotoTextView.setVisibility(View.GONE);
                            isPhotoAdded = true;
                        }
                        else{
                            addPhotoTextView.setVisibility(View.VISIBLE);
                            isPhotoAdded = false;
                        }

                    }
                /*for (int i = 0; i < task.getResult().size(); i++) {
                    db.collection("users").document(task.getResult().getDocuments().get(i).getId()).update("image",encodedImage);
                }*/
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}