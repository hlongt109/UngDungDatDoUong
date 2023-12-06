package com.duongnd.sipdrinkadmin.BottomDiaLog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.activity.CreateDrinkActivity;
import com.duongnd.sipdrinkadmin.activity.CreateTypeDrinkActivity;
import com.duongnd.sipdrinkadmin.databinding.BottomSheetLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetAddNew extends BottomSheetDialogFragment {
    private BottomSheetLayoutBinding binding;
    CardView btnAddLoai, btnAddDoUong, btnAddVoucher;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout,container,false);
        btnAddLoai = view.findViewById(R.id.btnThemLoai);
        btnAddDoUong = view.findViewById(R.id.btnThemDoUong);
        btnAddVoucher = view.findViewById(R.id.btnThemVoucher);
        //
        btnAddLoai.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), CreateTypeDrinkActivity.class)));
        btnAddDoUong.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), CreateDrinkActivity.class)));
        return view;
    }
}
