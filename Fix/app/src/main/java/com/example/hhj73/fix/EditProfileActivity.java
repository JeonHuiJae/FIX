package com.example.hhj73.fix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProfileActivity extends Activity {
    final static int PHOTO=111;
    private ImageView profileImageView;
    private Bitmap photo;
    private EditText message;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
    }
    public void init(){
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        message = (EditText)findViewById(R.id.profileMessageInput);
        message.setText(intent.getStringExtra("preMessage"));
        profileImageView = (ImageView)findViewById(R.id.profilePhotoInput);
        profileImageView.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21)
            profileImageView.setClipToOutline(true);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");
        //사진 검사
        StorageReference pathRef = storageReference.child("Profile/Senior/"+id);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음
            @Override
            public void onSuccess(Uri uri) {//있음
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .into(profileImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }


    public void editPhoto(View view) {
        Intent intent = new Intent(this, SelectPhotoMode.class);
        intent.putExtra("id",id);
        intent.putExtra("type",true);
        startActivityForResult(intent,PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");
        //사진 검사
        StorageReference pathRef = storageReference.child("Profile/Senior/"+id);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음

            @Override
            public void onSuccess(Uri uri) {//있음
                profileImageView.setImageURI(null);
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .into(profileImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void saveProfile(View view) {
        message = (EditText)findViewById(R.id.profileMessageInput);
        String tmp = message.getText().toString();
        Intent saveIntent = new Intent(this,SeniorMain.class);
        saveIntent.putExtra("profileMessage",tmp);
        setResult(RESULT_OK,saveIntent);
        startActivity(saveIntent);
    }
}
