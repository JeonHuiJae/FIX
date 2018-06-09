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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Iterator;

public class SeniorMain extends AppCompatActivity {

    final static int Edit_PROFILE = 1234;
    TextView message;
    ImageView photo;
    String id;
    DatabaseReference databaseReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");
    int roomNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_main);
        Intent intent = getIntent();
        id = intent.getStringExtra("curUser");
        init();

    }
    public void init(){
        message = (TextView)findViewById(R.id.myID);
        message.setText(id+" 님");
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

        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                while(child.hasNext()) {
                    String roomName = child.next().getKey().toString();
                    int idx = roomName.indexOf("+");
                    String StudentId = roomName.substring(idx+1);

                    if(StudentId.equals(id)) { //내가 속한 방
                        roomNum++;
                    }
                }
                Button roomNumB = (Button)findViewById(R.id.ChatNum);
                roomNumB.setText(roomNum+"");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        startActivityForResult(editIntent,Edit_PROFILE);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Edit_PROFILE&&resultCode==RESULT_OK){
            String tmp2 = data.getStringExtra("profileMessage");
            message.setText(tmp2);
            //사진 검사
            StorageReference pathRef = storageReference.child("Profile/Senior/"+id+".JPG");
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
