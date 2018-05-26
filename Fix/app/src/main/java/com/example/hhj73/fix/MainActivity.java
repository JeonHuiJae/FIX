package com.example.hhj73.fix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    EditText id;
    EditText pw;
    Button loginButton;

    String inputID;
    String inputPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        id = (EditText) findViewById(R.id.inputID);
        pw = (EditText) findViewById(R.id.inputPW);

        loginButton = (Button) findViewById(R.id.loginButton);
    }


    public void login(View view) {
        inputID = id.getText().toString();
        inputPW = pw.getText().toString();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                
                while(child.hasNext()) {
                    if(child.next().getKey().equals(inputID)) {
                        // 아이디 존재
                        String userPW = dataSnapshot.child(inputID).child("pw").getValue().toString();
                        
                        if(inputPW.equals(userPW)) {
                            // 비밀번호 똑같아
                            Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                            // 성공하면 어디로 가야함
                            /// 여기다가 하세여

                            String name = dataSnapshot.child(inputID).child("name").getValue().toString();
                            Boolean type = Boolean.parseBoolean(dataSnapshot.child(inputID).child("type").getValue().toString());
//                            intent.putExtra("name", name);
//                            intent.putExtra("id", inputID);
                            if(type){ //어르신
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");

                                //사진 검사
                                StorageReference pathRef = storageReference.child("Room/"+inputID);
                                pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음
                                    @Override
                                    public void onSuccess(Uri uri) {//있음
                                        Intent intent = new Intent(getApplicationContext(), SeniorMain.class);
                                        intent.putExtra("curUser", inputID);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {//없음
                                    @Override
                                    public void onFailure(@NonNull Exception e) {//없음
                                        Intent intent = new Intent(getApplicationContext(),SeniorFirst.class);
                                        intent.putExtra("curUser",inputID);
                                        startActivity(intent);
                                    }
                                });

                            }else{ //학생
                                Intent intent = new Intent(getApplicationContext(), MatchingActivity.class);
                                intent.putExtra("curUser", inputID);
                                startActivity(intent);
                            }
                            return;
                        }
                        else {
                            // 비밀번호 틀렸어
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            id.setText("");
                            pw.setText("");
                            return;
                        }
                  }
                }
                Toast.makeText(MainActivity.this, "로그인 정보를 확인하세요.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void join(View view) {
        Intent intent = new Intent(MainActivity.this, Join.class);
        startActivity(intent); //액티비티 이동
        overridePendingTransition(0, 0);
    }
}
