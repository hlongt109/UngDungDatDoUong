package com.duongnd.sipdrinkadmin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.duongnd.sipdrinkadmin.R;

public class MainActivity extends AppCompatActivity {
     private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseMessaging.getInstance().subscribeToTopic("News")
                        .addOnCompleteListener(task -> {
                            String msg = "Done";
                            if(!task.isSuccessful()){
                                msg = "Failed";
                            }
                        });
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

    @Override
    protected void onPause() {
        super.onPause();
        Intent serviceIntent = new Intent(this, OrderListenService.class);
        startForegroundService(serviceIntent);
    }
}