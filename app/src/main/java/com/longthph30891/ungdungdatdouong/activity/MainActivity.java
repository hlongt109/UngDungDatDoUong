package com.longthph30891.ungdungdatdouong.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.databinding.ActivityMainBinding;
import com.longthph30891.ungdungdatdouong.fragment.main_home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());


        binding.bottomNavCustomer.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_customer_home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.nav_customer_order) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.nav_customer_profile) {
                replaceFragment(new HomeFragment());
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_view_customer, fragment).commit();
    }

    public void hideBottomNav() {
        binding.bottomNavCustomer.setVisibility(View.GONE);
    }
}