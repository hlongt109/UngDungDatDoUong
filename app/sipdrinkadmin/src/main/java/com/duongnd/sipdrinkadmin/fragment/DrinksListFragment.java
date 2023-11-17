package com.duongnd.sipdrinkadmin.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.duongnd.sipdrinkadmin.adapter.DoUongAdapter;
import com.duongnd.sipdrinkadmin.databinding.FragmentDrinksListBinding;
import com.duongnd.sipdrinkadmin.model.DoUong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class DrinksListFragment extends Fragment {
    public DrinksListFragment() {}
    private FragmentDrinksListBinding binding;
    private ArrayList<DoUong>listDoUong = new ArrayList<>();
    private DoUongAdapter adapter;
    private DatabaseReference databaseReference;
    String maLoai = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            maLoai = getArguments().getString("maLoai");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDrinksListBinding.inflate(inflater, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("DoUong");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listDoUong.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DoUong doUong = dataSnapshot.getValue(DoUong.class);

                    if (doUong != null && doUong.getMaLoai().equals(maLoai)) {
                        listDoUong.add(doUong);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adapter = new DoUongAdapter(getActivity(), listDoUong, databaseReference);
        binding.gridViewDrinks.setAdapter(adapter);
        //
        binding.btnBack.setOnClickListener(view -> getActivity().onBackPressed());
        return binding.getRoot();
    }
}