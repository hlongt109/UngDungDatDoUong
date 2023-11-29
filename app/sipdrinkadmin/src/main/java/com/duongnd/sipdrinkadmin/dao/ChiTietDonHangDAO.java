package com.duongnd.sipdrinkadmin.dao;

import androidx.annotation.NonNull;

import com.duongnd.sipdrinkadmin.model.ChiTietDonHang;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChiTietDonHangDAO {
    private DatabaseReference databaseReference;

    public ChiTietDonHangDAO() {
        databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietDonHang");
    }

    public interface OnDataReceivedListener {
        void onDataReceived(ArrayList<ChiTietDonHang> list);
    }
    public ArrayList<ChiTietDonHang> selectAll(String orderID,ChiTietDonHangDAO.OnDataReceivedListener onDataReceivedListener) {
        ArrayList<ChiTietDonHang> list = new ArrayList<>();
        databaseReference.orderByChild("idDonHang").equalTo(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChiTietDonHang chiTietDonHang = dataSnapshot.getValue(ChiTietDonHang.class);
                    list.add(chiTietDonHang);
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
