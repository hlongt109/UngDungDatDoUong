package com.duongnd.sipdrinkadmin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.activity.LoginRegisterActivity;
import com.duongnd.sipdrinkadmin.databinding.FragmentPersonalBinding;
import com.duongnd.sipdrinkadmin.model.Admin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PersonalFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    private FragmentPersonalBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("admin").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Admin admin = snapshot.getValue(Admin.class);
                binding.tvName.setText(admin.getFullName());

                if (admin.getImage().equals("img")) {
                    binding.imgAvata.setImageResource(R.drawable.profilebkg);
                } else {
                    Glide.with(getContext()).load(admin.getImage()).error(R.drawable.profilebkg).into(binding.imgAvata);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.btnQlyHoaDon.setOnClickListener(view -> {

        });
        binding.btnQlyNguoiDung.setOnClickListener(view -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_mani, new UserFragment())
                    .addToBackStack(UserFragment.class.getName())
                    .commit();

        });
        binding.btnQlyVoucher.setOnClickListener(view -> {

        });
        binding.btnTaiKoanVaBaoMat.setOnClickListener(view -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_mani, new ProfileFragment())
                    .addToBackStack(ProfileFragment.class.getName())
                    .commit();

        });
        binding.btnLogout.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(getContext(), LoginRegisterActivity.class);
            startActivity(intent);

            // Hiển thị thông báo đăng xuất
            Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();

        });
        return binding.getRoot();
    }


}