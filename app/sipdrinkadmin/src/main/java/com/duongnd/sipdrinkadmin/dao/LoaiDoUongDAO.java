package com.duongnd.sipdrinkadmin.dao;

import androidx.annotation.NonNull;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoaiDoUongDAO {
    private DatabaseReference databaseReference;

    public LoaiDoUongDAO() {
        databaseReference = FirebaseDatabase.getInstance().getReference("LoaiDoUong");
    }

    public interface OnDataReceivedListener {
        void onDataReceived(ArrayList<LoaiDoUong> list);
    }
    public ArrayList<LoaiDoUong> selectAll(OnDataReceivedListener onDataReceivedListener) {
        ArrayList<LoaiDoUong> list = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiDoUong loaiDoUong = dataSnapshot.getValue(LoaiDoUong.class);
                    list.add(loaiDoUong);
                }
                if (onDataReceivedListener != null) {
                    onDataReceivedListener.onDataReceived(list);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return list;
    }
}
