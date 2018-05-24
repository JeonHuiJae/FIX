package com.example.hhj73.fix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SeniorMain extends AppCompatActivity {

    final static int Edit_PROFILE = 1234;
    TextView message;
    ImageView photo;
    String id;

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
            photo.setImageBitmap((Bitmap) data.getParcelableExtra("profliePhoto"));
        }
    }
}
