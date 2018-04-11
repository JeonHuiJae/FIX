package com.example.hhj73.fix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void login(View view) {
        EditText id = (EditText) findViewById(R.id.inputID);
        EditText pw = (EditText) findViewById(R.id.inputPW);

        String strID = id.getText().toString();
        String strPW = pw.getText().toString();

        databaseReference.child("id").push().setValue(strID);
        databaseReference.child("pw").push().setValue(strPW);

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
