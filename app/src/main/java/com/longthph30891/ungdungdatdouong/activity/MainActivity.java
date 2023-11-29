package com.longthph30891.ungdungdatdouong.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.databinding.ActivityMainBinding;
import com.longthph30891.ungdungdatdouong.fragment.main_home.CartFragment;
import com.longthph30891.ungdungdatdouong.fragment.main_home.HomeFragment;
import com.longthph30891.ungdungdatdouong.fragment.main_home.ProducDetailFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        );
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());


        binding.bottomNavCustomer.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_customer_home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.nav_customer_cart) {
                replaceFragment(new CartFragment());
                hideBottomNav();
            } else if (item.getItemId() == R.id.nav_customer_profile) {
                replaceFragment(new HomeFragment());
            }

            return true;
        });

    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_view_customer, fragment).commit();
    }

    public void hideBottomNav() {
        binding.bottomNavCustomer.setVisibility(View.GONE);
    }

    public void showBottomNav() {
        binding.bottomNavCustomer.setVisibility(View.VISIBLE);
    }

    public void showProductDetail(Fragment fragment) {
        hideBottomNav();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_view_customer, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void showBottomNavOnBackPressed() {
        showBottomNav();
        getSupportFragmentManager().popBackStack();
    }

}