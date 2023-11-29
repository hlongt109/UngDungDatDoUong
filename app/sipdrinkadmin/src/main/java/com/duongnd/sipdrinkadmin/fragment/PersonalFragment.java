package com.duongnd.sipdrinkadmin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetBillsList;
import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetUsersList;
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

        });
        return binding.getRoot();
    }
}