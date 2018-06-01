package com.example.hhj73.fix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SeniorMain extends AppCompatActivity {

    final static int Edit_PROFILE = 1234;
    TextView message;
    ImageView photo;
    String id;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_main);
        Intent intent = getIntent();
        id = intent.getStringExtra("curUser");
        init();

    }
    public void init(){
        message = (TextView)findViewById(R.id.profileMessage);
        photo = (ImageView)findViewById(R.id.profilePhoto);
        photo.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21)
            photo.setClipToOutline(true);
        //사진 검사
        StorageReference pathRef = storageReference.child("Profile/Senior/"+id);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음
            @Override
            public void onSuccess(Uri uri) {//있음
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .into(photo);
            }
        });
    }

    public void howTo(View view) {
       FrameLayout frameLayout = (FrameLayout)findViewById(R.id.howTo);
       frameLayout.setVisibility(View.VISIBLE);
    }

    public void offHowTo(View view) {
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.howTo);
        frameLayout.setVisibility(View.GONE);
    }

    public void editProfile(View view) {
        Intent editIntent = new Intent(this, EditProfileActivity.class);
        editIntent.putExtra("id",id);
        message = (TextView)findViewById(R.id.profileMessage);
        String str = message.getText().toString();
        if(!str.isEmpty())
            editIntent.putExtra("preMessage",str);
        else
            Toast.makeText(this,"no string",Toast.LENGTH_SHORT).show();
        startActivityForResult(editIntent,Edit_PROFILE);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Edit_PROFILE&&resultCode==RESULT_OK){
            String tmp2 = data.getStringExtra("profileMessage");
            message.setText(tmp2);
            //사진 검사
            StorageReference pathRef = storageReference.child("Profile/Senior/"+id);
            pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음
                @Override
                public void onSuccess(Uri uri) {//있음
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .centerCrop()
                            .into(photo);
                }
            });
        }
    }

    public void chatList(View view) {//채팅리스트
        Intent intent = new Intent(this, SeniorChatList.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
