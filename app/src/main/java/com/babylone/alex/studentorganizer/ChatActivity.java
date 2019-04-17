package com.babylone.alex.studentorganizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.babylone.alex.studentorganizer.Adapters.MessageAdapter;
import com.babylone.alex.studentorganizer.Classes.Message;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    String receiverId, receiverPhoto;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("Chat");
    EditText messageText;
    ImageButton sendMessage;
    MessageAdapter adapter;
    ArrayList<Message> arrayList;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("userName"));
        arrayList = new ArrayList<>();
        receiverId = getIntent().getStringExtra("userId");
        receiverPhoto = getIntent().getStringExtra("userImage");
        messageText = findViewById(R.id.chatMessageText);
        listView = findViewById(R.id.chatMessagesListView);

        sendMessage = findViewById(R.id.chatMessageSend);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(messageText.getText().toString())) sendMessage(mAuth.getUid(),receiverId,messageText.getText().toString());
                messageText.setText("");
            }
        });

        refreshList();


    }

    private void refreshList() {
        adapter = new MessageAdapter(this,arrayList,receiverPhoto);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child("receiver").getValue().toString().equals(mAuth.getUid()) && data.child("sender").getValue().toString().equals(receiverId)||
                            data.child("sender").getValue().toString().equals(mAuth.getUid()) && data.child("receiver").getValue().toString().equals(receiverId)){
                        arrayList.add(new Message(data.child("sender").getValue().toString(),data.child("receiver").getValue().toString(),data.child("message").getValue().toString()));
                    }
                }

                listView.setAdapter(adapter);
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    listView.setSelection(adapter.getCount() - 1);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        chatRef.push().setValue(hashMap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
