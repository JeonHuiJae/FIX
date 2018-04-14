package com.example.hhj73.fix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

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
        overridePendingTransition(0, 0);
    }

    public void next(View view) { //다음버튼
        switch (view.getId()){
            case R.id.next0:
                LinearLayout zero = (LinearLayout)findViewById(R.id.zero);
                zero.setVisibility(view.GONE);
                break;
            case R.id.next1:
                LinearLayout first = (LinearLayout)findViewById(R.id.first);
                first.setVisibility(view.GONE);
                break;
            case R.id.next2:
                LinearLayout second = (LinearLayout)findViewById(R.id.second);
                second.setVisibility(view.GONE);
                break;
            case R.id.next3:
                LinearLayout third = (LinearLayout)findViewById(R.id.third);
                third.setVisibility(view.GONE);
                break;
            case R.id.next4:
                LinearLayout forth = (LinearLayout)findViewById(R.id.forth);
                forth.setVisibility(view.GONE);
                break;
            case R.id.next5:
                LinearLayout fifth = (LinearLayout)findViewById(R.id.fifth);
                fifth.setVisibility(view.GONE);
                break;
            case R.id.next6:
                LinearLayout sixth = (LinearLayout)findViewById(R.id.sixth);
                sixth.setVisibility(view.GONE);
                break;
            case R.id.next7:
                LinearLayout seventh = (LinearLayout)findViewById(R.id.seventh);
                seventh.setVisibility(view.GONE);
                break;
        }
    }
}
