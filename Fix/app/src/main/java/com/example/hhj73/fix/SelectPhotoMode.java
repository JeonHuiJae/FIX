package com.example.hhj73.fix;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by skrud on 2018-05-21.
 */

public class SelectPhotoMode extends Activity {
    static final int REQUEST_IMAGE_CAPTURE=123;
    static final int REQUEST_STORAGE =234;
    private View view;
    private static String imagePath = "";
    Bitmap bitmapPhoto;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_photo_mode);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE)
            return false;
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap image = null;
        if(requestCode == REQUEST_STORAGE && resultCode==RESULT_OK){
            try{
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                Matrix matrix = new Matrix();
                matrix.postRotate(90); //90도 회전
                Bitmap.createBitmap(image, 0,0,
                        image.getWidth(),image.getHeight(),matrix,true);
                if(image!=null){
                    Intent intent = getIntent();
                    intent.putExtra("profilePhoto",image);
                    setResult(RESULT_OK,intent);
                    Toast.makeText(getApplicationContext(),"Photo Selected!",Toast.LENGTH_SHORT).show();

                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Didn't Selected Any Photo!",Toast.LENGTH_SHORT).show();
                }
            }
            catch (IOException ex){}
        }else if(requestCode==REQUEST_IMAGE_CAPTURE){
            loadPhoto();
            Toast.makeText(getApplicationContext(),"Hmmm",Toast.LENGTH_SHORT).show();
            if(bitmapPhoto!=null){
                Intent intent = getIntent();
                intent.putExtra("profilePhoto",bitmapPhoto);
                setResult(RESULT_OK,intent);
                Toast.makeText(getApplicationContext(),"Photo Selected!",Toast.LENGTH_SHORT).show();

                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Didn't Selected Any Photo!",Toast.LENGTH_SHORT).show();
            }
        }


    }
    public void loadPhoto(){
        if(imagePath.isEmpty()) return;
        bitmapPhoto = BitmapFactory.decodeFile(imagePath);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP_MR1) {
            bitmapPhoto = Bitmap.createScaledBitmap(bitmapPhoto,bitmapPhoto.getWidth(),bitmapPhoto.getHeight(),true);
        }
    }

    boolean checkAppPermission(String[] requestPermission){
        boolean[] requestResult = new boolean[requestPermission.length];
        for(int i=0; i< requestResult.length; i++){
            requestResult[i] = (ContextCompat.checkSelfPermission(this,
                    requestPermission[i]) == PackageManager.PERMISSION_GRANTED );
            if(!requestResult[i]){
                return false;
            }
        }
        return true;
    }
    void askPermission(String[] requestPermission, int REQ_PERMISSION) {
        ActivityCompat.requestPermissions(
                this,
                requestPermission,
                REQ_PERMISSION
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE :
                if (checkAppPermission(permissions)) {
                    Toast.makeText(this, "승인완료",Toast.LENGTH_SHORT).show();
                    goCamera(view);
                    // 퍼미션 동의했을 때 할 일
                } else {
                    Toast.makeText(this, "사용 불가",Toast.LENGTH_SHORT).show();
                    // 퍼미션 동의하지 않았을 때 할일
                    finish();
                }
                break;
            case REQUEST_STORAGE:
                if (checkAppPermission(permissions)) {
                    Toast.makeText(this, "승인완료",Toast.LENGTH_SHORT).show();
                    goGallery(view);
                    // 퍼미션 동의했을 때 할 일
                } else {
                    Toast.makeText(this, "사용 불가",Toast.LENGTH_SHORT).show();
                    // 퍼미션 동의하지 않았을 때 할일
                    finish();
                }
                break;
        }
    }
    public void goCamera(View view) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir.getAbsolutePath()+"/my_picture.jpg");
        imagePath = image.getAbsolutePath();
        Log.d("take Photo","Photo will be saved at: "+imagePath);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(checkAppPermission(new String[]{android.Manifest.permission.CAMERA})){
            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
        }
        else
            askPermission(new String[]{android.Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);

    }
    public void goGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(checkAppPermission(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE})){
            if(intent.resolveActivity(getPackageManager())!=null)
                startActivityForResult(intent,REQUEST_STORAGE);
        }
        else
            askPermission(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_STORAGE);
    }
}
