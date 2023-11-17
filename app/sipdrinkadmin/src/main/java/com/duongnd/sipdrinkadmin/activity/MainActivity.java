package com.duongnd.sipdrinkadmin.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.ActivityMainBinding;
import com.duongnd.sipdrinkadmin.databinding.AddnewLayoutBinding;
import com.duongnd.sipdrinkadmin.fragment.CategoryFragment;
import com.duongnd.sipdrinkadmin.fragment.LoginFragment;
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
            } else if (v.getItemId() == R.id.nav_addProduct) {
                openView();
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
    }
    private void replace(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
    private void openView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AddnewLayoutBinding binding1;
        binding1 = AddnewLayoutBinding.inflate(LayoutInflater.from(this));
        View view = binding1.getRoot();
        builder.setView(view);
        Dialog dialog = builder.create();
        binding1.btnThemLoai.setOnClickListener(view1 ->startActivity(new Intent(MainActivity.this, CreateTypeDrinkActivity.class)));
        binding1.btnThemDoUong.setOnClickListener(view1 -> startActivity(new Intent(MainActivity.this, CreateDrinkActivity.class)));
        dialog.show();

    }
}