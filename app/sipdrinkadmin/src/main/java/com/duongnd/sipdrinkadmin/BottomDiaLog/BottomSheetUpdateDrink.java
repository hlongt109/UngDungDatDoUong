package com.duongnd.sipdrinkadmin.BottomDiaLog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.adapter.ListTyeDrinkAdapter;
import com.duongnd.sipdrinkadmin.dao.LoaiDoUongDAO;
import com.duongnd.sipdrinkadmin.databinding.BottomSheetUpdateDrinkBinding;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetUpdateDrink extends BottomSheetDialogFragment {
    private BottomSheetUpdateDrinkBinding binding;
    private String id = "",name = "",idType = "",price = "",status = "",image="";
    private ArrayList<LoaiDoUong>listTypeDrinks;
    private LoaiDoUongDAO dao;
    private ListTyeDrinkAdapter listTyeDrinkAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetUpdateDrinkBinding.inflate(inflater,container,false);
        if (getArguments() != null){
            id = getArguments().getString("id");
            name = getArguments().getString("name");
            idType = getArguments().getString("idType");
            price = getArguments().getString("price");
            status = getArguments().getString("status");
            image = getArguments().getString("image");
        }
        setView();
        return binding.getRoot();
    }

    private void setView() {
        Log.d("DataDrink","id : "+id);
        Log.d("DataDrink","name : "+name);
        Log.d("DataDrink","idType : "+idType);
        Glide.with(getContext()).load(image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.imageDrinkUpdate);
        binding.edTen.setText(name);
        binding.edGia.setText(price);
        if(status =="DangBan"){
            binding.rdoDangBan.setChecked(true);
        }else if(status=="HetHang"){
            binding.rdoHetHang.setChecked(true);
        }
        listTypeDrinks = new ArrayList<>();
        dao = new LoaiDoUongDAO();
        dao.selectAll(list -> {
            listTypeDrinks= list;
            listTyeDrinkAdapter = new ListTyeDrinkAdapter(getActivity(),listTypeDrinks);
            binding.spinerLoai.setAdapter(listTyeDrinkAdapter);
            for (int i = 0; i <list.size();i++){
                if(listTypeDrinks.get(i).getTypeId() == idType){
                    binding.spinerLoai.setSelection(i);
                    break;
                }
            }
        });
    }
}
