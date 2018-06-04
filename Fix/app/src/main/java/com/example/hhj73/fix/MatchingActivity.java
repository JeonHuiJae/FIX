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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;


class ValueComparator implements Comparator<String> {

    Map<String, Double> base;

    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) { //반대로 하면 오름차순 <=
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

public class MatchingActivity extends AppCompatActivity {

    ListView matchList;
    ArrayList<String> users;
    ArrayAdapter arrayAdapter;
    DatabaseReference databaseReference;
    String curUser;
    final int FILTER = 123;

    double limit;//거리제한
    float pet;
    float help;
    float smoke;
    float curfew;
    double lot, lat;
    int minCost, maxCost;

    HashMap<String, Double> score;
    HashMap<String, String> scoreData;

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
        score = new HashMap<>();
        scoreData = new HashMap<>();

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

    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if(requestCode == FILTER && resultCode == RESULT_OK) { //데이터값 입력받을때 예외처리 필요.
            lot = data.getDoubleExtra("lot", 0);// 기준점 위치
            lat = data.getDoubleExtra("lat", 0);
            limit = data.getDoubleExtra("miter", Double.MAX_VALUE);//미터제한

            pet = data.getFloatExtra("pet", 0); //생활 속성
            help = data.getFloatExtra("help", 0);
            smoke = data.getFloatExtra("smoke", 0);
            curfew = data.getFloatExtra("curfew", 0);

            minCost = data.getIntExtra("minCost", 0); // 가격
            maxCost = data.getIntExtra("maxCost", -1);

            //======================================================▼구현해 주세요^^▼===============================================================//


            //단계별로 점수를 계산을 한 다음 최종 점수로 순서를 가린다.
            // 총점 100점중 거리 40, 생활 30, 가격 30 으로 나눠진다.

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double DminScore = Double.MAX_VALUE; // 거리 최소
                    double AmaxScore = Double.MIN_VALUE; // 속성점수 최대
                    double CmaxScore = Double.MIN_VALUE; // 가격 최대

                    Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                    ArrayList keySet = new ArrayList(score.keySet());

                    for (int i = 0; i < users.size(); i++) {
                        User user = dataSnapshot.child(users.get(i)).getValue(User.class);

                        // 거리 계산
                        // 거리최소 DminScore, 거리 저장 D_score
                        double D_score = 0;
                        if (lot != 0 && lat != 0) {
                            String str = user.location;
                            StringTokenizer stringTokenizer = new StringTokenizer(str, "/");
                            float _lat = Float.parseFloat(stringTokenizer.nextToken());
                            float _lot = Float.parseFloat(stringTokenizer.nextToken());
                            double dis = Math.sqrt((lat - _lat) * (lat - _lat) + (lot - _lot) * (lot - _lot));

                            D_score = dis / 300 * (-5); // 거리 점수 저장
                            if (D_score > limit)
                                D_score = -1; // 범위 넘어갈 때 -1
                            else {
                                if (D_score < DminScore) { // 거리 최소 저장됨
                                    DminScore = D_score;
                                }
                            }
                        }
                        Toast.makeText(MatchingActivity.this, "거리완료", Toast.LENGTH_SHORT).show();
                        // 속성 계산
                        // 속성 최대 AmaxScore 속성점수 저장 A_score

                        double A_score = 0; // 속성점수 저장
                        User me = dataSnapshot.child(curUser).getValue(User.class);
                        if (curfew != 0) {
                            if (me.getCurfew() == user.getCurfew()) {
                                A_score += same(curfew);
                            } else {
                                A_score += diff(curfew);
                            }
                        }
                        if (help != 0) {
                            if (me.getHelp() == user.getHelp()) {
                                A_score += same(help);
                            } else {
                                A_score += diff(help);
                            }
                        }
                        if (pet != 0) {
                            if (me.getPet() == user.getPet()) {
                                A_score += same(pet);
                            } else {
                                A_score += diff(pet);
                            }
                        }
                        if (smoke != 0) {
                            if (me.getSmoking() == user.getSmoking()) {
                                A_score += same(smoke);
                            } else {
                                A_score += diff(smoke);
                            }
                        }

                        if (AmaxScore < A_score) // 속성 최고저장.
                            AmaxScore = A_score;
                        Toast.makeText(MatchingActivity.this, "속성완료", Toast.LENGTH_SHORT).show();

                        // 가격 계산
                        // 가격 최대 CmaxScore , 가격 저장 C_score

                        double userCost = Double.parseDouble(user.cost);
                        double C_score = 0;

                        C_score = (userCost / 1000);// 1000원 단워로 점수화
                        if (CmaxScore < C_score) // 속성 최고저장.
                            CmaxScore = C_score;

                        if (maxCost != -1 || minCost != 0) {
                            if (userCost > minCost && userCost < maxCost)
                                C_score = C_score / 2; // 범위안에 들어가면 반으로
                            if (maxCost == -1 && userCost > minCost) {
                                C_score = C_score / 2;
                            } else { // 범위에 안들어감
                                C_score = -1;
                            }
                        }
                        Toast.makeText(MatchingActivity.this, "가격완료", Toast.LENGTH_SHORT).show();

                        scoreData.put(user.getId(), D_score + "/" + A_score + "/" + C_score);//총 데이터 저장
                    }


                    for (int i = 0; i < scoreData.size(); i++) { // 최종점수 계산
                        String str = scoreData.get(i);
                        StringTokenizer stringTokenizer = new StringTokenizer(str, "/");
                        double D_s = Double.parseDouble(stringTokenizer.nextToken()); // 거리 점수
                        double A_s = Double.parseDouble(stringTokenizer.nextToken()); // 속성 점수
                        double C_s = Double.parseDouble(stringTokenizer.nextToken()); // 가격 점수

                        double DmaxScore = DminScore * (-1);
                        if (D_s != -1) {// 선택했을 떄만
                            D_s = ((D_s + DmaxScore) / DmaxScore) * 40; // 거리점수 계산완료
                        } else
                            continue;

                        A_s = A_s / AmaxScore * 30; // 속성점수 계산완료

                        if (C_s != -1) {// 범위안에 있을 때만
                            C_s = (Math.sqrt((C_s - CmaxScore)) / CmaxScore) * 30;
                            score.put((String) keySet.get(i), D_s + A_s + C_s); //총점 100점 점수 넣기
                        }
                    }
                    //점수 내림차순으로 정렬한 후 에 리스트 출력
                    ValueComparator vc = new ValueComparator(score);
                    TreeMap<String, Double> sorted_score = new TreeMap<String, Double>(vc); // 정렬

                    for (Map.Entry<String, Double> entry : sorted_score.entrySet()) {
                        //정렬한 리스트에서 순번을 배열번호로 변경하여 원본 리스트에서 추출

                        users.add(entry.getKey() + ": " + score.get(entry.getKey()));
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public float same(float imp){ //속성 같을때
        return (Math.abs(3-imp) + 1) * (4f);
    }
    public float diff(float imp){ //속성 다를때
        return (Math.abs(5 - imp)) * (1.5f);
    }
}
