package com.duongnd.sipdrinkadmin.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetBillsList;
import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetUsersList;
import com.duongnd.sipdrinkadmin.activity.LoginRegisterActivity;
import com.duongnd.sipdrinkadmin.databinding.FragmentPersonalBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PersonalFragment extends Fragment {
    private FragmentPersonalBinding binding;

    public PersonalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        binding.btnQlyHoaDon.setOnClickListener(view -> {
            BottomSheetBillsList bottomSheetBillsList = new BottomSheetBillsList();
            bottomSheetBillsList.show(getChildFragmentManager(), "BottomSheetBillsList");
        });
        binding.btnQlyNguoiDung.setOnClickListener(view -> {
            BottomSheetUsersList bottomSheetUsersList = new BottomSheetUsersList();
            bottomSheetUsersList.show(getChildFragmentManager(), "BottomSheetUsersList");
        });
        binding.btnQlyVoucher.setOnClickListener(view -> {

        });
        binding.btnTaiKoanVaBaoMat.setOnClickListener(view -> {

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