package com.example.hhj73.fix;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by hhj73 on 2018-04-09.
 */

public class SeniorJoinActivity extends Activity {
    LinearLayout zero;
    LinearLayout first;
    LinearLayout second;
    LinearLayout third;
    LinearLayout forth;
    LinearLayout fifth;
    LinearLayout sixth;
    LinearLayout seventh;
    DatabaseReference databaseReference;
    String id;
    String pw;
    EditText joinID;
    EditText pw1;
    EditText pw2;
    EditText add2;


    String strt;        //xml 중 address2 에 들어가는 string
    boolean addcount;   //화면 다시 시작한 위치
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_senior);

        init();
    }

    public void init() {


        Intent intent =getIntent();
        strt = intent.getStringExtra("ADDRESS");
        addcount=intent.getBooleanExtra("ADDCOUNT",false);

        zero = (LinearLayout)findViewById(R.id.zero);
        first = (LinearLayout)findViewById(R.id.first);
        second = (LinearLayout)findViewById(R.id.second);
        third = (LinearLayout)findViewById(R.id.third);
        forth = (LinearLayout)findViewById(R.id.forth);
        fifth = (LinearLayout)findViewById(R.id.fifth);
        sixth = (LinearLayout)findViewById(R.id.sixth);
        seventh = (LinearLayout)findViewById(R.id.seventh);

        TextView agree = (TextView)findViewById(R.id.agreeOk);
        agree.setMovementMethod(new ScrollingMovementMethod());

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        joinID = (EditText) findViewById(R.id.JoinID);
        pw1 = (EditText) findViewById(R.id.pw1);
        pw2 = (EditText) findViewById(R.id.pw2);

    }

    public void seniorJoinSuccess(View view) {
        // 노인회원 가입 완료
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent); //액티비티 이동
        overridePendingTransition(0, 0);
        Toast.makeText(this, "가입완료!",Toast.LENGTH_SHORT).show();
    }


    public void next(View view) { //다음버튼


        if (addcount == true) {

            zero.setVisibility(view.GONE);// ???????????????????????????????????????
            first.setVisibility(view.GONE);
            second.setVisibility(view.GONE);
            third.setVisibility(view.GONE);
            forth.setVisibility(view.VISIBLE);
            add2= (EditText)findViewById(R.id.address);
            add2.setText(strt);
               // Toast.makeText(this, strt, Toast.LENGTH_SHORT).show();   //test intent values
            addcount=false;

        } else {
            switch (view.getId()) {
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
                case R.id.next2:
                    second.setVisibility(view.GONE);
                    third.setVisibility(view.VISIBLE);
                    break;

                case R.id.next3:
                    third.setVisibility(view.GONE);
                    forth.setVisibility(view.VISIBLE);
                    break;

                case R.id.next4:
                    forth.setVisibility(view.GONE);
                    fifth.setVisibility(view.VISIBLE);
                    break;
                case R.id.next5:
                    fifth.setVisibility(view.GONE);
                    sixth.setVisibility(view.VISIBLE);
                    break;
                case R.id.next6:
                    sixth.setVisibility(view.GONE);
                    seventh.setVisibility(view.VISIBLE);
                    break;
                case R.id.next7:
                    seventh.setVisibility(view.GONE);
                    break;
            }
        }
    }

    public void IDcheck(View view) { //아이디 중복확인

        // 아이디 가져오기
        id = joinID.getText().toString();

        // 중복 확인
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                while(child.hasNext()) {
                    if(child.next().getKey().equals(id)) {
                        Toast.makeText(SeniorJoinActivity.this, "중복되는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        joinID.setText("");
                        id = null;
                        databaseReference.removeEventListener(this);
                        return;
                    }
                }
                Toast.makeText(SeniorJoinActivity.this, "사용할 수 있는 ID입니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void search(View view) { //주소 API 검색

       // Toast.makeText(this, "주소 불러왔음",Toast.LENGTH_SHORT).show();      //delete
        Intent address = new Intent(this,Address.class);
        startActivity(address);

    }

    public void takePhoto(View view) { //사진찍어 신분증 인증
        Toast.makeText(this, "사진찍어 인증함",Toast.LENGTH_SHORT).show();
    }

    public void loadPhote(View view) { //앨범에서 신분증 가져오기
        Toast.makeText(this, "앨범에서 가져와 인증함",Toast.LENGTH_SHORT).show();
    }
}
