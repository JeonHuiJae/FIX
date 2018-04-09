package com.example.hhj73.fix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void login(View view) {
    }

    public void seniorJoin(View view) {
        Intent intent = new Intent(MainActivity.this, SeniorJoinActivity.class);
        startActivity(intent); //액티비티 이동
    }

    public void studentJoin(View view) {
        Intent intent = new Intent(MainActivity.this, StudentJoinActivity.class);
        startActivity(intent); //액티비티 이동
    }
}
