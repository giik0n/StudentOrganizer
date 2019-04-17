package com.babylone.alex.studentorganizer.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Adapters.ChatUsersAdapter;
import com.babylone.alex.studentorganizer.ChatActivity;
import com.babylone.alex.studentorganizer.Classes.ChatUser;
import com.babylone.alex.studentorganizer.R;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {


    public ChatFragment() {

    }
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, chatRef;
    ArrayList<ChatUser> users = new ArrayList<>();
    SwipeMenuListView listView;
    ChatUsersAdapter adapter;
    SearchView searchView;
    ArrayList<String> uniqueChats = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        chatRef = FirebaseDatabase.getInstance().getReference().child("Chat");
        searchView = view.findViewById(R.id.searchViewUsers);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                refreshList();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchChats(s);
                return false;
            }
        });
        listView = view.findViewById(R.id.chatListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userId", users.get(i).getId());
                intent.putExtra("userName", users.get(i).getName());
                intent.putExtra("userImage", users.get(i).getImage());
                startActivity(intent);
            }
        });
        adapter = new ChatUsersAdapter(getActivity(),users);
        return view;
    }

    public void refreshList(){
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uniqueChats.clear();
                users.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child("receiver").getValue().toString().equals(mAuth.getUid())){
                        readChats(data.child("sender").getValue().toString());
                    }
                    if (data.child("sender").getValue().toString().equals(mAuth.getUid())){
                        readChats(data.child("receiver").getValue().toString());
                    }
                }
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void readChats(final String s){
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data: dataSnapshot.getChildren()){
                    String profileImage = "";
                    if (data.hasChild("profileimage")){
                        profileImage = data.child("profileimage").getValue().toString();
                    }
                    if (data.getKey().equals(s) && !uniqueChats.contains(s)) {
                        users.add(new ChatUser(data.getKey().toString(),data.child("first_name").getValue().toString()+" "+data.child("last_name").getValue().toString(),profileImage));
                        uniqueChats.add(s);
                    }
                }
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void searchChats(final String s){
        Query query = usersRef.orderByChild("last_name").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    String profileImage = "";
                    if (data.hasChild("profileimage")){
                        profileImage = data.child("profileimage").getValue().toString();
                    }
                    if(!data.getKey().equals(mAuth.getUid())) users.add(new ChatUser(data.getKey().toString(),data.child("first_name").getValue().toString()+" "+data.child("last_name").getValue().toString(),profileImage));
                }
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
