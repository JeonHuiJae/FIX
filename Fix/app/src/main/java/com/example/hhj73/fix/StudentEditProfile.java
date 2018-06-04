package com.example.hhj73.fix;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StudentEditProfile extends AppCompatActivity {
    String id;
    final int PHOTO = 123;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        imageView = (ImageView)findViewById(R.id.profilePhotoInput);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");
        //사진 검사
        StorageReference pathRef = storageReference.child("Profile/Student/"+id);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음
            @Override
            public void onSuccess(Uri uri) {//있음
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void StudentEditPhoto(View view) {
        Intent intent = new Intent(this, SelectPhotoMode.class);
        intent.putExtra("id",id);
        intent.putExtra("type",false);
        startActivityForResult(intent,PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");
        //사진 검사
        StorageReference  pathRef= storageReference.child("Profile/Student/"+id);

        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음

            @Override
            public void onSuccess(Uri uri) {//있음
                imageView.setImageURI(null);
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentEditProfile.this, "로딩지연", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveProfile(View view) {
        Intent intent = new Intent(this, MatchingActivity.class);
        intent.putExtra("curUser",id);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void logOut(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
