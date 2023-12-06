package com.duongnd.sipdrinkadmin.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetBillsList;
import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetUsersList;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.activity.ChatActivity;
import com.duongnd.sipdrinkadmin.activity.LoginRegisterActivity;
import com.duongnd.sipdrinkadmin.databinding.FragmentPersonalBinding;
import com.duongnd.sipdrinkadmin.model.Admin;
import com.duongnd.sipdrinkadmin.model.Khachang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalFragment extends Fragment {
    private FragmentPersonalBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    public PersonalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(inflater, container, false);

        binding = FragmentPersonalBinding.inflate(inflater,container,false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("admin").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Admin admin = snapshot.getValue(Admin.class);
                binding.tvName.setText(admin.getFullName());

                if(admin.getImg().equals("img")){
                    binding.imgAvata.setImageResource(R.drawable.profilebkg);
                }else {
                    Glide.with(requireActivity()).load(admin.getImg()).error(R.drawable.profilebkg).into(binding.imgAvata);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.btnQlyHoaDon.setOnClickListener(view -> {
            BottomSheetBillsList bottomSheetBillsList = new BottomSheetBillsList();
            bottomSheetBillsList.show(getChildFragmentManager(), "BottomSheetBillsList");
        });
        binding.btnQlyNguoiDung.setOnClickListener(view -> {
            BottomSheetUsersList bottomSheetUsersList = new BottomSheetUsersList();
            bottomSheetUsersList.show(getChildFragmentManager(), "BottomSheetUsersList");
        });
        binding.btnTaiKoanVaBaoMat.setOnClickListener(view -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UserPassFragment())
                    .addToBackStack(UserPassFragment.class.getName())
                    .commit();
        });
        binding.btnChat.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), ChatActivity.class));
        });
        binding.btnLogout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Bạn có chắn muốn đăng xuất không ?");
            builder.setNegativeButton("Trở lại", null);
            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
                getActivity().finish();
            });
            builder.create().show();
        });
        return binding.getRoot();
    }
}