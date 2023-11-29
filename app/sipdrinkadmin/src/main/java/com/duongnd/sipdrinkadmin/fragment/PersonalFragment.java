package com.duongnd.sipdrinkadmin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.activity.LoginRegisterActivity;
import com.duongnd.sipdrinkadmin.databinding.FragmentPersonalBinding;
import com.duongnd.sipdrinkadmin.model.Admin;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(inflater,container,false);
        binding.btnQlyHoaDon.setOnClickListener(view -> {
            BottomSheetBillsList bottomSheetBillsList = new BottomSheetBillsList();
            bottomSheetBillsList.show(getChildFragmentManager(),"BottomSheetBillsList");
        });
        binding.btnQlyNguoiDung.setOnClickListener(view -> {
            BottomSheetUsersList bottomSheetUsersList = new BottomSheetUsersList();
            bottomSheetUsersList.show(getChildFragmentManager(),"BottomSheetUsersList");
        });
        binding.btnQlyVoucher.setOnClickListener(view -> {

        });
        binding.btnTaiKoanVaBaoMat.setOnClickListener(view -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_mani, new ProfileFragment())
                    .addToBackStack(ProfileFragment.class.getName())
                    .commit();

        });
        binding.btnLogout.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(getContext(), LoginRegisterActivity.class);
            startActivity(intent);

            // Hiển thị thông báo đăng xuất
            Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();

        });
        return binding.getRoot();
    }




}