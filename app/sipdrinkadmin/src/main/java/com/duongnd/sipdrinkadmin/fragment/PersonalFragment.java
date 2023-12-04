package com.duongnd.sipdrinkadmin.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetBillsList;
import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetUsersList;
import com.duongnd.sipdrinkadmin.activity.LoginRegisterActivity;
import com.duongnd.sipdrinkadmin.databinding.FragmentPersonalBinding;

public class PersonalFragment extends Fragment {
    private FragmentPersonalBinding binding;
    public PersonalFragment() {}

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

        });
        binding.btnLogout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Bạn có chắn muốn đăng xuất không ?");
            builder.setNegativeButton("Trở lại",null);
            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                startActivity( new Intent(getActivity(), LoginRegisterActivity.class));
                getActivity().finish();
            });
            builder.create().show();
        });
        return binding.getRoot();
    }
}