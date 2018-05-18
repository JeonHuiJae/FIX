package com.example.hhj73.fix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class SeniorMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_main);
    }

    public void howTo(View view) {
       FrameLayout frameLayout = (FrameLayout)findViewById(R.id.howTo);
       frameLayout.setVisibility(View.VISIBLE);
    }

    public void offHowTo(View view) {
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.howTo);
        frameLayout.setVisibility(View.GONE);
    }
}
