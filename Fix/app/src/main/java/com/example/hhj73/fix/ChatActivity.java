package com.example.hhj73.fix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    EditText editChat;
    ArrayList<String> chats;
    ArrayAdapter arrayAdapter;
    ListView chatList;
    String myName;
    String myID;
    DatabaseReference databaseReference;
    String urName;
    String urID;
    String users[];
    String room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    public void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        editChat = (EditText) findViewById(R.id.chatText);
        chats = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chats);
        chatList = (ListView) findViewById(R.id.chatList);
        chatList.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        myName = intent.getStringExtra("name");
        myID = intent.getStringExtra("id");

        // 상대방 아이디 일단 아무렇게나
        urName = "honghjin";
        urID = "honghjin";


        // 채팅방 생성
        users = new String[2];
        users[0] = myID;
        users[1] = urID;
        Arrays.sort(users);

        room = users[0]+"+"+users[1];

        String msg = users[0]+"님이 입장하셨습니다.";
        chats.add(msg);

        msg = users[1]+"님이 입장하셨습니다.";
        chats.add(msg);

        arrayAdapter.notifyDataSetChanged();

        databaseReference.child(room).child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                String msg = chatData.getMessage();
                chats.add(msg);
                arrayAdapter.notifyDataSetChanged();
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

    }

    public void submit(View view) {
        // 채팅 보내기
        String chat = editChat.getText().toString();
        String str = myName + ": " + chat;

        chats.add(str);
        arrayAdapter.notifyDataSetChanged();

        editChat.setText("");

        // 내가 보낸 메시지 DB에 저장
        ChatData chatData = new ChatData(myID, str);
        databaseReference.child(room).child("chat").push().setValue(chatData);
    }


}
