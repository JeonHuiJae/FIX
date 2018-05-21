package com.example.hhj73.fix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditProfileActivity extends Activity {
    final static int PHOTO=111;
    private ImageView profileImageView;
    private Bitmap photo;
    private EditText message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
    }
    public void init(){
        Intent intent = getIntent();
        message = (EditText)findViewById(R.id.profileMessageInput);
        message.setText(intent.getStringExtra("preMessage"));
        profileImageView = (ImageView)findViewById(R.id.profilePhotoInput);
        profileImageView.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21)
            profileImageView.setClipToOutline(true);

    }


    public void editPhoto(View view) {
        Intent intent = new Intent(this, SelectPhotoMode.class);
        startActivityForResult(intent,PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PHOTO&&requestCode==RESULT_OK){

            byte[] bytes = data.getByteArrayExtra("profilePhoto");
            Bitmap bytesTobitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            //photo = (Bitmap)data.getParcelableExtra("profilePhoto");

            if(bytesTobitmap!=null)
                profileImageView.setImageBitmap(bytesTobitmap);
            else
                Toast.makeText(getApplicationContext(),"Loading Fail",Toast.LENGTH_SHORT).show();
        }
    }

    public void saveProfile(View view) {
        message = (EditText)findViewById(R.id.profileMessageInput);
        String tmp = message.getText().toString();
        Intent saveIntent = new Intent(this,SeniorMain.class);
        saveIntent.putExtra("profileMessage",tmp);
        saveIntent.putExtra("profilePhoto",photo);
        setResult(RESULT_OK,saveIntent);
        finish();
    }
}
