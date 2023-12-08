package com.longthph30891.ungdungdatdouong.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.adapter.UserAdpter;
import com.longthph30891.ungdungdatdouong.model.Admin;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity{

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdpter adapter;
    FirebaseDatabase database;
    ArrayList<Admin> usersArrayList;
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

        DatabaseReference reference = database.getReference().child("admin");

        usersArrayList = new ArrayList<>();

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainUserRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        adapter = new UserAdpter(ChatActivity.this,usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);



        img_back_chat_1.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot: snapshot.getChildren())
               {
                   Admin admin = dataSnapshot.getValue(Admin.class);
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