package com.example.hhj73.fix;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MatchedMain extends AppCompatActivity {
    String myID;
    String urID;
    Boolean type;
    ImageView p_stu;
    ImageView p_sin;
    TextView title;
    Family family;

    DatabaseReference databaseReference;
    StorageReference pathRef;
    Intent intentProfile; // 프로필 누를 때
    final  int CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_main);
        init();
    }

    private void init() {
        final Intent intent = getIntent();
        myID = intent.getStringExtra("myID");
        urID = intent.getStringExtra("urID");
        type = intent.getBooleanExtra("type",true);

        p_sin = (ImageView)findViewById(R.id.p_senior);
        p_stu = (ImageView)findViewById(R.id.p_student);
        title = (TextView)findViewById(R.id.matchedTitle);

        String senior,student;
        if(type){
            senior = myID;
            student = urID;
        }else{
            senior = urID;
            student = myID;
        }
        final String room = student+"+"+senior;

        databaseReference = FirebaseDatabase.getInstance().getReference("families");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                family = dataSnapshot.child(room).getValue(Family.class); // 객체 받아옴
                title.setText(family.getName_senior()+" X " +family.getName_student()); // 타이틀
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //프로필사진
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");

        // 어르신 사진 검사
        pathRef = storageReference.child("Profile/Senior/"+senior);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음
            @Override
            public void onSuccess(Uri uri) {//있음
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .into(p_sin);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        // 어르신 사진 검사
        pathRef = storageReference.child("Profile/Student/"+student);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음
            @Override
            public void onSuccess(Uri uri) {//있음
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .into(p_stu);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        // 둥글게 출력
        p_sin.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21)
            p_sin.setClipToOutline(true);
        p_stu.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21)
            p_stu.setClipToOutline(true);

        // 클릭이벤트

        if(myID.equals(senior)){ // 내가 어르신이면
            // 내사진 누르면 프로필 수정으로
            // 상대사진 누르면 정보보기로
            p_sin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentProfile = new Intent(getApplicationContext(), EditProfileActivity.class);
                    intentProfile.putExtra("id", myID);
                    intentProfile.putExtra("type", true);
                    startActivityForResult(intentProfile, CODE);
                }
            });

            p_stu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentProfile = new Intent(getApplicationContext(), StudentDetail.class);
                    intentProfile.putExtra("urId", urID);
                    startActivityForResult(intentProfile, CODE);
                }
            });

        }else{ // 학생이면
            p_sin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentProfile = new Intent(getApplicationContext(), SeniorDetail.class);
                    intentProfile.putExtra("myID", myID);
                    intentProfile.putExtra("urID", urID);
                    intentProfile.putExtra("type", false);
                    startActivityForResult(intentProfile, CODE);
                }
            });

            p_stu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentProfile = new Intent(getApplicationContext(), StudentEditProfile.class);
                    intentProfile.putExtra("id", urID);
                    intentProfile.putExtra("type", true);
                    startActivityForResult(intentProfile, CODE);
                }
            });
        }
    }

    public void goChat(View view) { //==========================================구현해주셈
        Intent intent; // 매칭후 채팅 엑티비티 만들고 연결.
        //if(type)
            //intent = new Intent(this ,);
        //else
            //intent = new Intent(this, ChatActivity.class);
        //intent.putExtra("myID", myID);
        //intent.putExtra("urID", urID);

    }

    public void goContract(View view) { // 계약서 목록 출력 =========================구현해 주라긔

    }
}
