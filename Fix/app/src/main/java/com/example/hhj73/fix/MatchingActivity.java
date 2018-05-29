package com.example.hhj73.fix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MatchingActivity extends AppCompatActivity {

    ListView matchList;
    ArrayList<String> users;
    ArrayAdapter arrayAdapter;
    DatabaseReference databaseReference;
    String curUser;
    final int FILTER = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        init();
    }

    public void init() {
        Intent intent = getIntent();
        curUser = intent.getStringExtra("curUser");
        matchList = (ListView) findViewById(R.id.matchList);
        users = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);
        matchList.setAdapter(arrayAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                String Mgender = dataSnapshot.child(curUser).child("gender").getValue().toString();
                if(Mgender.equals("true")) //어르신과 청년의 저장이 달라서 바꿔놨음^^
                    Mgender = "female";
                else
                    Mgender = "male";
                String Sgender;
                Boolean type;
                while(child.hasNext()) {
                    String id = child.next().getKey();
                    Sgender = dataSnapshot.child(id).child("gender").getValue().toString();
                    type = Boolean.parseBoolean(dataSnapshot.child(id).child("type").getValue().toString());

                    if(!id.equals(curUser) && type && Mgender.equals(Sgender)) { //나랑 성별 같은 어르신만.
                        users.add(id);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(MatchingActivity.this, str, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), SeniorDetail.class);
                intent.putExtra("myID", curUser);
                intent.putExtra("urID", str);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }

    public void profile(View view) { //프로필
        Intent intent = new Intent(this, StudentEditProfile.class);
        intent.putExtra("id", curUser);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void ChatList(View view) {
        Intent intent = new Intent(this, StudentChatList.class);
        intent.putExtra("id",curUser);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void filter(View view) {//필터
        Intent intent = new Intent(this,Filter.class);
        startActivityForResult(intent,FILTER);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        float limit;//거리제한
        float pet;
        float help;
        float smoke;
        float curfew;
        float lot, lat;
        int minCost, maxCost;

        if(requestCode == FILTER){
            lot = data.getFloatExtra("lot",0);// 기준점 위치
            lat = data.getFloatExtra("lat",0);
            limit = data.getFloatExtra("miter",0);//미터제한

            pet = data.getFloatExtra("pet",1); //생활 속성
            help = data.getFloatExtra("help",1);
            smoke = data.getFloatExtra("smoke",1);
            curfew = data.getFloatExtra("curfew",1);

            minCost = data.getIntExtra("minCost",0);//가격
            maxCost = data.getIntExtra("maxCost",0);

            // 1. 거리 계산 (기준접으로부터 미터제한까지 그 안에 들어가는 어르신만 나온다.)

            // 2. 생활 궁합 (같으면 (|3-중요도|+1)*(1.5), 다르면 |5-중요도| 만큼)*(3/4) -> 점수 내림차순으로 정렬
            //1(안중요함)-> 같: 4.5, 다: 4 / 2(별로 안중요함)-> 같:3, 다: 3, / 3(보통)->같:1.5 , 다:2 / 4(중요함)->같:3, 다:1 / 5(매우중요)->같4.5:, 다: 0

            // 3. 가격 계산
        }
    }
}
