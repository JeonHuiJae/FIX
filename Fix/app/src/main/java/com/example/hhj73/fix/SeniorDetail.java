package com.example.hhj73.fix;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Iterator;

public class SeniorDetail extends AppCompatActivity {
String urId;
String myId;

TextView title;
ImageView roomImage;
TextView rent;
TextView address;
TextView uniqueness;
ImageView smoke;
ImageView help;
ImageView curfew;
ImageView pet;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_detail);
        Intent intent = getIntent();
        myId = intent.getStringExtra("myID");
        urId = intent.getStringExtra("urID");

        init();
    }

    private void init() {
        title = (TextView)findViewById(R.id.Dtitle);
        roomImage = (ImageView)findViewById(R.id.roomImage);
        rent = (TextView)findViewById(R.id.rent);
        address = (TextView)findViewById(R.id.Daddress);
        uniqueness = (TextView)findViewById(R.id.Duniqueness);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");

        //사진 검사
        StorageReference pathRef = storageReference.child("Room/"+urId);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음
            @Override
            public void onSuccess(Uri uri) {//있음
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .into(roomImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                String add;
                if(dataSnapshot.child(urId).child("gender").getValue().toString().equals("female"))
                    add = " 할머니댁";
                else
                    add = " 할아버지댁";

                title.setText(dataSnapshot.child(urId).child("name").getValue().toString()+add);
                rent.setText("월 "+dataSnapshot.child(urId).child("cost").getValue().toString()+"원");
                address.setText(dataSnapshot.child(urId).child("address").getValue().toString());
                uniqueness.setText(dataSnapshot.child(urId).child("unique").getValue().toString());

                if(dataSnapshot.child(urId).child("smoking").getValue().toString()=="true")
                    smoke = (ImageView)findViewById(R.id.smokeX);
                else
                    smoke = (ImageView)findViewById(R.id.smokeO);
                smoke.setVisibility(View.INVISIBLE);
                if(dataSnapshot.child(urId).child("curfew").getValue().toString()=="true")
                    curfew = (ImageView)findViewById(R.id.curfewX);
                else
                    curfew = (ImageView)findViewById(R.id.curfewO);
                curfew.setVisibility(View.INVISIBLE);

                if(dataSnapshot.child(urId).child("pet").getValue().toString()=="true")
                    pet = (ImageView)findViewById(R.id.petX);
                else
                    pet = (ImageView)findViewById(R.id.petO);
                pet.setVisibility(View.INVISIBLE);

                if(dataSnapshot.child(urId).child("help").getValue().toString()=="true")
                    help = (ImageView)findViewById(R.id.helpX);
                else
                    help = (ImageView)findViewById(R.id.helpO);
                help.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void talk(View view) {//채팅으로
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("myID", myId);
        intent.putExtra("urID", urId);
        startActivity(intent);
    }

    public void back(View view) { //그냥 뒤로가기
        Intent intent = new Intent(this, MatchingActivity.class);
        intent.putExtra("curUser",myId);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
