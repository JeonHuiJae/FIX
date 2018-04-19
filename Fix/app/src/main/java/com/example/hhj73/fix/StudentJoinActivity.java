package com.example.hhj73.fix;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by hhj73 on 2018-04-09.
 */

public class StudentJoinActivity extends Activity {
    EditText emailText;
    LinearLayout zero;
    LinearLayout first;
    LinearLayout second;
    EditText addEdit;
    static final int REQUEST_IMAGE_CAPTURE=123;

    String strt;        //xml 중 address2 에 들어가는 string
    boolean addcount=false;   //화면 다시 시작한 위치
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_student);
        zero = (LinearLayout)findViewById(R.id.zero);
        first = (LinearLayout)findViewById(R.id.first);
        second = (LinearLayout)findViewById(R.id.second);
        emailInit();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        strt = intent.getStringExtra("ADDRESS");
        addcount=intent.getBooleanExtra("ADDCOUNT",false);


        if(addcount==true){
            zero.setVisibility(view.GONE);
            first.setVisibility(view.VISIBLE);
            addEdit= (EditText)findViewById(R.id.address);
            addEdit.setText(strt);
            // Toast.makeText(this, strt, Toast.LENGTH_SHORT).show();   //test intent values
            addcount=false;
        }
    }

    public void emailInit(){
        emailText = (EditText)findViewById(R.id.email);
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_CLASS_TEXT);
    }
    public void studentJoinSuccess(View view) {
        // 학생회원 가입 완료


    }
    public boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);

        if(m.matches())
            err = true;
        return err;
    }

    public void next(View view) {
        switch (view.getId()){
            case R.id.next0:
                CheckBox agree = (CheckBox) findViewById(R.id.agree);
                if (agree.isChecked()) {
                    zero.setVisibility(view.GONE);
                    first.setVisibility(view.VISIBLE);
                } else
                    Toast.makeText(this, "동의하셔야 가입이 가능합니다", Toast.LENGTH_SHORT).show();
                break;
            case R.id.next1:
                first.setVisibility(view.GONE);
                second.setVisibility(view.VISIBLE);
                break;
        }
    }

    public void search(View view) { //주소 API 검색

        // Toast.makeText(this, "주소 불러왔음",Toast.LENGTH_SHORT).show();      //delete
        Intent address = new Intent(this,Address.class);
        address.putExtra("activity",true);
        startActivity(address);

    }

    public void IDcheck(View view) { //아이디 중복 체크
    }

    public void sendEmail(View view) { //이메일 보내기
        String email = emailText.getText().toString();
        if(isValidEmail(email)){
            Intent intent = new Intent(this,EmailCertifActivity.class);
            intent.putExtra("client_email",email);
            startActivityForResult(intent,1);
        }else{
            Toast.makeText(this,"유효하지 않은 형식입니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK){
            if (data.getBooleanExtra("email_certification", false)) {
                emailText.setEnabled(false);
                Button btn_email = (Button) findViewById(R.id.btn_email);
                btn_email.setVisibility(View.INVISIBLE);
            }
        }else if(requestCode==REQUEST_IMAGE_CAPTURE){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            Matrix matrix = new Matrix();
            matrix.postRotate(90); //90도 회전
            Bitmap.createBitmap(imageBitmap, 0,0,
                    imageBitmap.getWidth(),imageBitmap.getHeight(),matrix,true);
            ((ImageView)findViewById(R.id.imageview)).setImageBitmap(imageBitmap);
        }
    }

    public void takePhoto(View view) { //사진찍어 신분증 인증
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(checkAppPermission(new String[]{android.Manifest.permission.CAMERA})){
            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
        }
        else
            askPermission(new String[]{android.Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);
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
                    takePhoto(view);
                    // 퍼미션 동의했을 때 할 일
                } else {
                    Toast.makeText(this, "사용 불가",Toast.LENGTH_SHORT).show();
                    // 퍼미션 동의하지 않았을 때 할일
                    finish();
                }
                break;
        }
    }

}

