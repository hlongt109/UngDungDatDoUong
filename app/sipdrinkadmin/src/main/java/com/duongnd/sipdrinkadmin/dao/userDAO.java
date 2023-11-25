package com.duongnd.sipdrinkadmin.dao;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class userDAO {
    public interface onTenUserListener{
        void onTenUser(String tenKH);
    }
    public interface onAddressListener{
        void onAddress(String address);
    }
    public void getNameUserById(String idUser,onTenUserListener listener){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("userId").equalTo(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snd : snapshot.getChildren()){
                        String nameUser = snd.child("fullName").getValue(String.class);
                        listener.onTenUser(nameUser);
                        break;
                    }
                }else {
                    listener.onTenUser("Khong tim thay ten");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getAddress(String idUser,onAddressListener listener){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("userId").equalTo(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snd : snapshot.getChildren()){
                        String address = snd.child("address").getValue(String.class);
                        listener.onAddress(address);
                    }
                }else {
                    listener.onAddress("Chua co dia chi");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
