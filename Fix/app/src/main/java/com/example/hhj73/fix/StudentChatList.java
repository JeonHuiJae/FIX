package com.example.hhj73.fix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class StudentChatList extends AppCompatActivity {
ListView chatList;
    ArrayList<String> users;
    ArrayAdapter arrayAdapter;
    DatabaseReference databaseReference;
    String curUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_chat_list);

        init();
    }

    private void init()
    {
        Intent intent = getIntent();
        curUser = intent.getStringExtra("id");
        chatList = (ListView) findViewById(R.id.MyChatList);
        users = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);
        chatList.setAdapter(arrayAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("chats");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                while(child.hasNext()) {
                    String roomName = child.next().getKey().toString();
                    int idx = roomName.indexOf("+");
                    String StudentId = roomName.substring(0, idx);

                    if(StudentId.equals(curUser)) { //내가 속한 방
                        users.add(roomName.substring(idx+1));
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(StudentChatList.this, str, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("myID", curUser);
                intent.putExtra("urID", str);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }

    //뒤로가기
    public void back(View view) {
        Intent intent = new Intent(this, MatchingActivity.class);
        intent.putExtra("curUser",curUser);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
