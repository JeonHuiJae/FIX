package com.example.hhj73.fix;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ChatActivitySenior extends AppCompatActivity {
    EditText editChat;
    ArrayList<ChatData> chats;
    // ArrayAdapter arrayAdapter;
    RecyclerView chatList;
    RecyclerView.LayoutManager layoutManager;
    ChatAdapter chatAdapter;
    String myName;
    String myID;
    DatabaseReference databaseReference;
    TextView urName;
    String urID;
    String users[];
    String room;

    ImageView urPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_senior);
        init();
    }

    public void init() {
        urPro = (ImageView)findViewById(R.id.urProfile);
        urName = (TextView)findViewById(R.id.UrName);
        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        editChat = (EditText) findViewById(R.id.chatText);
        chats = new ArrayList<>();
        // arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chats);
        chatList = (RecyclerView) findViewById(R.id.chatList);
        chatAdapter = new ChatAdapter(chats);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);

        Intent intent = getIntent();
        //     myName = intent.getStringExtra("name");
        myID = intent.getStringExtra("myID");

        // 상대방
        //     urName = "honghjin";
        urID = intent.getStringExtra("urID");


        // 채팅방 생성
        users = new String[2];
        users[0] = myID;
        users[1] = urID;
        Arrays.sort(users);

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
                chatList.smoothScrollToPosition(chatAdapter.getItemCount() - 1); // 아래로 스크롤

                //상대 프로필
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl("gs://xylophone-house.appspot.com");
                //사진 검사
                StorageReference pathRef = storageReference.child("Profile/Student/"+urID);
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
                urPro.setBackground(new ShapeDrawable(new OvalShape()));
                if(Build.VERSION.SDK_INT>=21)
                    urPro.setClipToOutline(true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "삭제됨 ㄷㄷ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TextView roomName = (TextView) findViewById(R.id.roomName);
        urName.setText(urID);
        roomName.setText(urID);

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


    public void contractLayoutBtn(View view) { //계약서 버튼
        FrameLayout layout = (FrameLayout)findViewById(R.id.contractLayout);
        layout.setVisibility(View.VISIBLE);
    }

    public void backChat(View view) { //채팅으로 돌어가기
        FrameLayout layout = (FrameLayout)findViewById(R.id.contractLayout);
        layout.setVisibility(View.GONE);
    }

    public void back(View view) { //채팅목록으로 뒤로가기
        Intent intent = new Intent(this, SeniorChatList.class);
        intent.putExtra("id",myID);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}