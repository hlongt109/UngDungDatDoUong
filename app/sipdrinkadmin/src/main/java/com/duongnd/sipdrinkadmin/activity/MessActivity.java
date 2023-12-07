package com.duongnd.sipdrinkadmin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.duongnd.sipdrinkadmin.Adapter.MessageAdapter;
import com.duongnd.sipdrinkadmin.databinding.ActivityMessBinding;
import com.duongnd.sipdrinkadmin.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessActivity extends AppCompatActivity {
    ActivityMessBinding binding;
    public FirebaseAuth firebaseAuth;
    public DatabaseReference databaseReference, databaseReference1;
    public MessageAdapter adapter;
    public List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("messages");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("admin" + "/" +user.getUid()+"/");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("fullName").getValue(String.class);
                binding.ten.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(this, messageList);
        binding.listViewMessages.setAdapter(adapter);

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = binding.editTextMessage.getText().toString().trim();
                String userId = firebaseAuth.getCurrentUser().getUid();
                String userSt = binding.ten.getText().toString().trim();

                if (!messageText.isEmpty()) {
                    sendMessage(userSt, userId, messageText);
                    binding.editTextMessage.setText("");
                }
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                Message message = dataSnapshot.getValue(Message.class);
                messageList.add(message);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessActivity.this, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String userSt, String userId, String messageText) {
        DatabaseReference newMessageRef = databaseReference.push();

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("userId", userId);
        messageMap.put("userName", userSt);
        messageMap.put("messageText", messageText);

        newMessageRef.setValue(messageMap);

    }
}