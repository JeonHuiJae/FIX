package com.example.hhj73.fix;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MatchedMain extends AppCompatActivity {
ImageView p_stu;
ImageView p_sin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_main);
        init();
    }

    private void init() {
        p_sin = (ImageView)findViewById(R.id.p_senior);
        p_stu = (ImageView)findViewById(R.id.p_student);
        p_sin.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21)
            p_sin.setClipToOutline(true);
        p_stu.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21)
            p_stu.setClipToOutline(true);
    }
}
