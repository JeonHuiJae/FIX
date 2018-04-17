package com.example.hhj73.fix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    EditText id;
    EditText pw;
    Button loginButton;

    boolean loginCheck;

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
                        if(checkPW()) {
                            // 비번 맞아버림
                            Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            // 비밀번호 틀림
                            id.setText("");
                            pw.setText("");
                            Toast.makeText(MainActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
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

    public boolean checkPW() {
        // ㅅㅂ

        databaseReference.child(inputID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String realPW = dataSnapshot.child("pw").getValue().toString();

                Toast.makeText(MainActivity.this, realPW+", 입력한 비밀번호>>"+inputPW, Toast.LENGTH_SHORT).show();

                if(inputPW.equals(realPW)) { // 비밀번호 맞으면
                    loginCheck = true;
                }
                else { // 비밀번호 틀리면
                    loginCheck = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return loginCheck;
    }

    public void seniorJoin(View view) {
        Intent intent = new Intent(MainActivity.this, SeniorJoinActivity.class);
        startActivity(intent); //액티비티 이동
        overridePendingTransition(0, 0);
    }

    public void studentJoin(View view) {
        Intent intent = new Intent(MainActivity.this, StudentJoinActivity.class);
        startActivity(intent); //액티비티 이동
        overridePendingTransition(0, 0);
    }
}
