package com.example.hhj73.fix;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class ChatActivity extends AppCompatActivity implements ContractAdapter.ListBtnClickListener {

    EditText editChat;
    TextView urName;

    ArrayList<ChatData> chats;
    // ArrayAdapter arrayAdapter;
    ListView chatList;
    // RecyclerView.LayoutManager layoutManager;
    ChatAdapter chatAdapter;
    String myName;
    String myID;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_user;
    DatabaseReference databaseReference_family;
    DatabaseReference databaseReference_contract;
    String urID;
    String users[];
    String room;
    User you;
    User me;
    Uri urNumber;
    final int callRequest = 123;

    ImageView urPro;
    ContractData contractData;
    ArrayList<ContractData> contractArrayList;
    ListView contractlistView;
    ContractAdapter contractAdapter;

    final static int CONDITION = R.id.conditionBtn;
    final static int FEE = R.id.monthlyFeeBtn;
    final static int PERIOD = R.id.periodMonthBtn;
    final static int EDATE = R.id.effectiveDateBtn;
    final static int SPECAIL = R.id.extraspecialBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    public void init() {
        urPro = (ImageView)findViewById(R.id.urProfile);
        urName = (TextView)findViewById(R.id.UrName);
        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        editChat = (EditText) findViewById(R.id.chatText);
        chats = new ArrayList<>();
        // arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chats);
        chatList = (ListView) findViewById(R.id.chatList);

        // layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // chatList.setLayoutManager(layoutManager);


        final Intent intent = getIntent();

        myID = intent.getStringExtra("myID");
        // 상대방 senior
        urID = intent.getStringExtra("urID");

        chatAdapter = new ChatAdapter(getApplicationContext(), chats, myID);
        chatList.setAdapter(chatAdapter);

        // 채팅방 생성
        users = new String[2];
        users[0] = myID;
        users[1] = urID;

        room = users[0]+"+"+users[1];

//        String msg = users[0]+"님이 입장하셨습니다.";
//        chats.add(msg);
//
//        msg = users[1]+"님이 입장하셨습니다.";
//        chats.add(msg);

        chatAdapter.notifyDataSetChanged();

        databaseReference.child(room).child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                if(chatData.getUserName() == urID) {
                    // 상대가 보낸 메시지면 배경색 바꾸기
                    // chatAdapter 에서 접근해야할 문제라 어떻게 해야할지 모르겠어요
                    // OnBindViewHolder 에서 조건문 설정해서 이렇게 해야하는데 ㅠㅠ
                    // holder.itemView.setBackgroundColor(Color.WHITE);

                }
                chats.add(chatData);
                chatAdapter.notifyDataSetChanged();
                // chatList.smoothScrollToPosition(chatAdapter.getItemCount() - 1); // 아래로 스크롤

                //상대 프로필
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");
                //사진 검사
                StorageReference pathRef = storageReference.child("Profile/Senior/"+urID+".JPG");
                pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//있음
                    @Override
                    public void onSuccess(Uri uri) {//있음
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .centerCrop()
                                .into(urPro);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

                urPro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(getApplicationContext(), SeniorDetail.class);
                        intent1.putExtra("myID", myID);
                        intent1.putExtra("urID", urID);
                        intent1.putExtra("type", false);
                        startActivity(intent1);
                    }
                });

                urPro.setBackground(new ShapeDrawable(new OvalShape()));
                if(Build.VERSION.SDK_INT>=21)
                    urPro.setClipToOutline(true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(ChatActivity.this, "삭제됨 ㄷㄷ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference_user = FirebaseDatabase.getInstance().getReference("users");
        databaseReference_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                me = dataSnapshot.child(myID).getValue(User.class);
                you =  dataSnapshot.child(urID).getValue(User.class);
                urName.setText(you.getName()); // 이름
                TextView roomName = (TextView) findViewById(R.id.roomName);
                roomName.setText(you.getName()+" 어르신");
                urNumber = Uri.parse(you.getPhone());// 전화번호
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference_contract = FirebaseDatabase.getInstance().getReference("contracts");

        databaseReference_contract.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),"finding..",Toast.LENGTH_SHORT).show();
                contractData = dataSnapshot.child(room).getValue(ContractData.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }
        });


        contractArrayList = new ArrayList<>();
        contractlistView = (ListView)findViewById(R.id.detailContractList);

    }

    public void submit(View view) {
        // 채팅 보내기
        String chat = editChat.getText().toString();
        String str = myID + ": " + chat;

//        chats.add(str);
//        arrayAdapter.notifyDataSetChanged();

        editChat.setText("");

        // 내가 보낸 메시지 DB에 저장
        // 시간
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("MM-dd hh:mm");
        String timeStr = dayTime.format(new Date(time));

        ChatData chatData = new ChatData(myID, str, timeStr);
        databaseReference.child(room).child("chat").push().setValue(chatData);
    }


    public void contractLayoutBtn(View view) { // 계약서 버튼
        FrameLayout  layout = (FrameLayout)findViewById(R.id.contractLayout);
        layout.setVisibility(View.VISIBLE);
        if (contractData == null){
            Toast.makeText(getApplicationContext(),"don't exist",Toast.LENGTH_SHORT).show();

            //학생이름, 어르신이름, 월세, 주소, 어르신흡연, 학생흡연, 어르신펫, 학생펫, 어르신통금, 학생통금, 어르신도움, 학생 도움
            contractData= new ContractData(me.getName(),you.getName(),you.getCost(),you.getAddress(),you.getSmoking(),me.getSmoking(),
                    you.getPet(),me.getPet(),you.getCurfew(),me.getCurfew(),you.getHelp(),me.getHelp(),you.getUnique(),me.getUnique());
            Toast.makeText(this,contractData.getStartdate(),Toast.LENGTH_SHORT).show();
            databaseReference_contract.child(room).setValue(contractData);
        }else{
            Toast.makeText(getApplicationContext(),"exist",Toast.LENGTH_SHORT).show();
        }
        contractArrayList.add(contractData);
        contractAdapter = new ContractAdapter(this,R.layout.contract_row,contractArrayList,this);
        contractlistView.setAdapter(contractAdapter);

        //계약시작날짜 - 사용자입력 수정가능
        //계약자들 - 오토 수정불가
        //계약기간 - 사용자입력 수정가능
        //주소 - 오토 수정불가
        //월세 - 오토 수정가능
        //어르신 and 학생 나누어서
        //아침, 흡연, 펫, 통금, 도움, 특이합의사항

    }

    public void backChat(View view) { // 채팅으로 돌어가기
        FrameLayout layout = (FrameLayout)findViewById(R.id.contractLayout);
        layout.setVisibility(View.GONE);
    }

    public void back(View view) { // 채팅목록으로 뒤로가기
        Intent intent = new Intent(this, StudentChatList.class);
        intent.putExtra("id",myID);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void call(View view) { // 전화걸기
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+urNumber));
        if(checkAppPermission(new String[]{android.Manifest.permission.CALL_PHONE}))//call phone 체크
            startActivity(callIntent);
        else
            askPermission(new String[]{android.Manifest.permission.CALL_PHONE}, callRequest);//요구

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
            case callRequest :
                if (checkAppPermission(permissions)) {
                    Toast.makeText(this, "승인완료",Toast.LENGTH_SHORT).show();
                    // 퍼미션 동의했을 때 할 일
                } else {
                    Toast.makeText(this, "사용 불가",Toast.LENGTH_SHORT).show();
                    // 퍼미션 동의하지 않았을 때 할일
                    finish();
                }
                break;
        }
    }

    public void contractSubmit(View view) {
        // you 어르신 me 학생
        databaseReference_family = FirebaseDatabase.getInstance().getReference("families");
        databaseReference_family.child(room).child("studentAgree").setValue(true);
        Toast.makeText(this, "Agree Ok", Toast.LENGTH_SHORT).show();
        databaseReference_family.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(room).child("seniorAgree").exists()){
                    Family family = new Family(me.getPhone(), you.getPhone(), me.getName(), you.getName(), me.getId(), you.getId());
                    databaseReference_family.child(room).setValue(family);
                    Toast.makeText(ChatActivity.this, "Now We are Family!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onListBtnClick(int position) {
        ContractData c = (ContractData)contractArrayList.get(0);
        switch (position){
            case CONDITION://흡연 등등의 조건
                Toast.makeText(this,"CONDITION",Toast.LENGTH_SHORT).show();
                break;
            case FEE: //월세 ok
                setFeeDialog();
                Toast.makeText(this,"FEE",Toast.LENGTH_SHORT).show();
                break;
            case PERIOD: //계약기간 월단위 ok
                Toast.makeText(this,"PERIOD",Toast.LENGTH_SHORT).show();
                setPeroidDialog();
                break;
            case EDATE: // 실효날짜 ok
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,onDateSetListener,2018,6-1,10);
                datePickerDialog.show();
                Toast.makeText(this,"EDATE",Toast.LENGTH_SHORT).show();
                break;
            case SPECAIL: //특이사항 추가 및 제거
                Toast.makeText(this,"SPECIAL",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"DEFAULT",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void  setFeeDialog(){
        final EditText feeInput = new EditText(this);
        feeInput.setGravity(Gravity.RIGHT);

        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle("월세 변경(원 단위)")
                .setView(feeInput)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),feeInput.getText(),Toast.LENGTH_SHORT).show();
                        contractData.setMonthlyfee(feeInput.getText().toString());
                        contractAdapter.notifyDataSetChanged();
                        databaseReference_contract.child(room).setValue(contractData);
                    }
                })
                .show();
    }
    private void setPeroidDialog(){
        final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(this)
                .minValue(6)
                .maxValue(60)
                .defaultValue(1)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle("계약기간(월 단위)")
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contractData.setMonthperiod(numberPicker.getValue());
                        contractAdapter.notifyDataSetChanged();
                        databaseReference_contract.child(room).setValue(contractData);
                    }
                })
                .show();
    }
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            int mon = month+1;
            String tempDate = year+"년 "+mon+"월 "+dayOfMonth+"일";
            contractData.setStartdate(tempDate);
            contractAdapter.notifyDataSetChanged();
            databaseReference_contract.child(room).setValue(contractData);
        }
    };
}
