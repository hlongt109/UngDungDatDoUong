package com.duongnd.sipdrinkadmin.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duongnd.sipdrinkadmin.Adapter.UserAdpter;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.model.Khachang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity{

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdpter adapter;
    FirebaseDatabase database;
    ArrayList<Khachang> usersArrayList;
    ImageView img_back_chat_1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        getSupportActionBar().hide();

        img_back_chat_1 = findViewById(R.id.img_back_chat_1);
        database=FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference().child("users");

        usersArrayList = new ArrayList<>();

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdpter(ChatActivity.this,usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);


        img_back_chat_1.setOnClickListener(v -> {
            onBackPressed();
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot: snapshot.getChildren())
               {
                   Khachang admin = dataSnapshot.getValue(Khachang.class);
                   usersArrayList.add(admin);
               }
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (auth.getCurrentUser() == null){
            Intent intent = new Intent(ChatActivity.this,LoginRegisterActivity.class);
            startActivity(intent);
        }

    }
}