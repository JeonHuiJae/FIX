package com.example.hhj73.fix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by hhj73 on 2018-04-09.
 */

public class SeniorJoinActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_senior);
    }

    public void seniorJoinSuccess(View view) {
        // 노인회원 가입 완료
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent); //액티비티 이동
    }

}
