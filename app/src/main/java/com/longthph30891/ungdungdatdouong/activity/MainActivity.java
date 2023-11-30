package com.longthph30891.ungdungdatdouong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.databinding.ActivityMainBinding;
import com.longthph30891.ungdungdatdouong.fragment.main_home.CartFragment;
import com.longthph30891.ungdungdatdouong.fragment.main_home.HomeFragment;
import com.longthph30891.ungdungdatdouong.fragment.main_home.PersonalFragment;
import com.longthph30891.ungdungdatdouong.utilities.SessionManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    SessionManager sessionManager;

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

        sessionManager = new SessionManager(this);


        binding.bottomNavCustomer.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_customer_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_customer_cart) {
                replaceFragment(new CartFragment());
                hideBottomNav();
                return true;
            } else if (item.getItemId() == R.id.nav_customer_profile) {
                replaceFragment(new PersonalFragment());
                return true;
            }
            return true;
        });

        showQuantityCart();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main_view_customer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void hideBottomNav() {
        binding.bottomNavCustomer.setVisibility(View.GONE);
    }

    public void showBottomNav() {
        binding.bottomNavCustomer.setVisibility(View.VISIBLE);
        binding.bottomNavCustomer.setSelectedItemId(R.id.nav_customer_home);
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

    public void showQuantityCart() {
        String idKhachHang = sessionManager.getLoggedInCustomerId();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Cart").child(idKhachHang);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long quantity = snapshot.getChildrenCount();
                Log.d("TAG", "onDataChange: " + quantity);
                if (quantity > 0) {
                    binding.bottomNavCustomer.getOrCreateBadge(R.id.nav_customer_cart).setNumber((int) quantity);
                } else {
                    binding.bottomNavCustomer.removeBadge(R.id.nav_customer_cart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Cart", "onCancelled: " + error.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}