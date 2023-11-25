package com.duongnd.sipdrinkadmin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetAddNew;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.ActivityMainBinding;
import com.duongnd.sipdrinkadmin.fragment.CategoryFragment;
import com.duongnd.sipdrinkadmin.fragment.NotificationFragment;
import com.duongnd.sipdrinkadmin.fragment.PersonalFragment;
import com.duongnd.sipdrinkadmin.fragment.StatisticFragment;

public class MainActivity extends AppCompatActivity {
     private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setMenu();
    }
    private void setMenu(){
        binding.bottomNavigation.setOnItemSelectedListener(v->{
            if (v.getItemId() == R.id.nav_danhMuc) {
                replace(new CategoryFragment());
            } else if (v.getItemId() == R.id.nav_thongKe) {
                replace(new StatisticFragment());
            } else if (v.getItemId() == R.id.nav_thongBao) {
                replace(new NotificationFragment());
            } else if (v.getItemId() == R.id.nav_thongTin) {
                replace(new PersonalFragment());
            }
            return true;
        });
        binding.bottomNavigation.setSelectedItemId(R.id.nav_danhMuc);
        binding.btnAddNew.setOnClickListener(view -> {
            BottomSheetAddNew bottomSheetAddNew = new BottomSheetAddNew();
            bottomSheetAddNew.show(getSupportFragmentManager(),"BottomSheetLayout");
        });
    }
    private void replace(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}