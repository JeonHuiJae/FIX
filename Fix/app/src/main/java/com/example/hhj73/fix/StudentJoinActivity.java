package com.example.hhj73.fix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_student);
        zero = (LinearLayout)findViewById(R.id.zero);
        first = (LinearLayout)findViewById(R.id.first);
        second = (LinearLayout)findViewById(R.id.second);
        //emailInit();
    }
//    public void emailInit(){
//        emailText = (EditText)findViewById(R.id.email);
//        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_CLASS_TEXT);
//    }
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
                zero.setVisibility(view.GONE);
                first.setVisibility(view.VISIBLE);
                break;
        }
    }

    public void search(View view) { //주소 검색
    }

    public void IDcheck(View view) { //아이디 중복 체크
    }

    public void sendEmail(View view) { //이메일 보내기
        String email = emailText.getText().toString();
        if(isValidEmail(email)){
           // Intent intent = new Intent(this,EmailCertifActivity.class);
            //intent.putExtra("client_email",email);
            //startActivity(intent);
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
        }
    }
}

