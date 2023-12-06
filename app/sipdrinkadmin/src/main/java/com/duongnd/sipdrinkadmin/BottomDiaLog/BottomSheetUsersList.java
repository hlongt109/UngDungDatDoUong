package com.duongnd.sipdrinkadmin.BottomDiaLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duongnd.sipdrinkadmin.adapter.UserAdapter;
import com.duongnd.sipdrinkadmin.databinding.BottomSheetUsersListBinding;
import com.duongnd.sipdrinkadmin.model.Khachang;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BottomSheetUsersList extends BottomSheetDialogFragment {
    private BottomSheetUsersListBinding binding;
    FirebaseDatabase database;
    ArrayList<Khachang> recyclerList;
    UserAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        recyclerList = new ArrayList<>();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetUsersListBinding.inflate(inflater,container,false);

        adapter = new UserAdapter(recyclerList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        binding.recyclerUser.setLayoutManager(layoutManager);
        binding.recyclerUser.setNestedScrollingEnabled(false);
        binding.recyclerUser.setAdapter(adapter);
        binding.seach.clearFocus();

        database.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Khachang model = dataSnapshot.getValue(Khachang.class);

                        recyclerList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.seach.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                seachList(newText);


                return true;
            }
        });

        return binding.getRoot();
    }
    public void seachList(String text){
        ArrayList <Khachang> seachList = new ArrayList<>();
        for (Khachang khachang: recyclerList){
            if (khachang.getFullName().toLowerCase().contains(text.toLowerCase())){
                seachList.add(khachang);
            }
        }adapter.seachDatalist(seachList);
    }
}