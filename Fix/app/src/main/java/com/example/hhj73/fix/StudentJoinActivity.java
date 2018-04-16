package com.example.hhj73.fix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hhj73 on 2018-04-09.
 */

public class StudentJoinActivity extends Activity {
    EditText emailText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_student);
        emailInit();
    }
    public void emailInit(){
        emailText = (EditText)findViewById(R.id.email);
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_CLASS_TEXT);
    }
    public void studentJoinSuccess(View view) {
        // 학생회원 가입 완료
        String email = emailText.getText().toString();
        if(isValidEmail(email)){
            Toast.makeText(this,"유효한 이메일 형식.",Toast.LENGTH_SHORT).show();

            Intent emailintent = new Intent(getApplicationContext(), EmailCertifActivity.class);
            emailintent.putExtra("client_email",email);
            startActivity(emailintent); //액티비티 이동
            overridePendingTransition(0, 0);
            Toast.makeText(this, "이메일을 인증해주세요.", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this,"유효하지 않은 형식입니다.",Toast.LENGTH_SHORT).show();
        }

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

}

