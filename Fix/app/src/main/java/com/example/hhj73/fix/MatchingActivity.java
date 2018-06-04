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

        if(requestCode == FILTER && requestCode == RESULT_OK){ //데이터값 입력받을때 예외처리 필요.
            lot = data.getFloatExtra("lot",0);// 기준점 위치
            lat = data.getFloatExtra("lat",0);
            limit = data.getFloatExtra("miter",0);//미터제한

            pet = data.getFloatExtra("pet",0); //생활 속성
            help = data.getFloatExtra("help",0);
            smoke = data.getFloatExtra("smoke",0);
            curfew = data.getFloatExtra("curfew",0);

            minCost = data.getIntExtra("minCost",0);//가격
            maxCost = data.getIntExtra("maxCost",0);

            //======================================================▼구현해 주세요^^▼===============================================================//

            //단계별로 점수를 계산을 한 다음 최종 점수로 순서를 가린다.
            // 총점 100점중 거리 40, 생활 30, 가격 30 으로 나눠진다.

            //★ 1. 거리 계산 (기준접으로부터 미터제한까지 그 안에 들어가는 어르신만 나온다.)
            if(lot != 0 && lat !=0){
                // null값 아니면 기준점으로부터 어르신데이터 위도경도 -> 거리계산 (<어르신아이디, 거리차>해쉬맵 쓰는게 간단할듯)
                if(limit>0) {
                    // 거리제한 있으면 제한 넘어가는것 제외
                }
                //오름차순 정렬
            }
            //정렬된 거리차해쉬맵 에서 차이0~300m는 0점 300~600m 는 -5점 300m 단위로 5씩 내려가게 점수준다.
            //점수 최저값이 -20이라면 모든 점수에 20을 더한다.
            //20(최고점)으로 나누고 40을 곱한다. (최고점을 40으로 맞추기 위해)

            //★ 2. 생활 궁합 (same, diff 함수 이용)
            float sum = 0;
            if(pet !=0){//필터링 선택했을 때만.
                // 어르신과 비교
                //같을때
                sum += same(pet);
                //다를때
                sum += diff(pet);
            }
            if(help !=0){
                // 어르신과 비교
                //같을때
                sum += same(help);
                //다를때
                sum += diff(help);
            }
            if(curfew !=0){
                // 어르신과 비교
                //같을때
                sum += same(curfew);
                //다를때
                sum += diff(curfew);
            }
            if(smoke !=0){
                // 어르신과 비교
                //같을때
                sum += same(smoke);
                //다를때
                sum += diff(smoke);
            }

            //4가지 모두 합계한 결과에 최고점으로 나누고 30을 곱한다.

            //★ 3. 가격 계산 (<어르신아이디, 가격>해쉬맵 사용)
            if(maxCost != 0){//가격 제한 뒀을 때.
                //가격 제한 안의 값만 추려낸다.
            }
            // 오름차순으로 정렬
            // 가장 높은가격 0으로 시작해서 낮아질수록 1점씩더 큰 값으로 더한다.
            // 최고점으로 나누고 30을 곱한다.

            //★ 4. 최종 계산
            // 3가지 점수를 ID 별로 하나의 해쉬맵(최종점수)으로 합산한다.
            // 내림차순으로 정렬하고 출력한다.
        }
    }

    public float same(float imp){ //속성 같을때
        return (Math.abs(3-imp) + 1) * (4f);
    }
    public float diff(float imp){ //속성 다를때
        return (Math.abs(5 - imp)) * (1.5f);
    }
}
