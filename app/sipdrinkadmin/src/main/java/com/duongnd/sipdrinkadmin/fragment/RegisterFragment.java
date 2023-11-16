package com.duongnd.sipdrinkadmin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.duongnd.sipdrinkadmin.databinding.FragmentRegisterBinding;
import com.duongnd.sipdrinkadmin.model.Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    FirebaseDatabase database;
    DatabaseReference refernce;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(getLayoutInflater());
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        binding.layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return binding.getRoot();
    }

    public Boolean validate(){
        String name = binding.edtNameSignup.getText().toString().trim();
        String user = binding.edtUsernameSignup.getText().toString().trim();
        String pass = binding.edtPasswordSignup.getText().toString().trim();
        String repass = binding.edtRepasswordSignup.getText().toString().trim();


        if (name.isEmpty() || user.isEmpty() || repass.isEmpty() || pass.isEmpty()) {
            binding.edtNameSignup.setError("Tên không được để trống");
            binding.edtUsernameSignup.setError("UserName không được để trống");
            binding.edtPasswordSignup.setError("Mật khẩu không được để trống");
            binding.edtRepasswordSignup.setError("Nhập lại mật khẩu không được để trống");

            return false;
        }else  if (!pass.equals(repass)) {
            binding.edtRepasswordSignup.setError("Mật khẩu nhập lại không trùng khớp");

            return false;
        }else {
            checkUser(name, user, pass);
            return true;
        }


    }

    public void checkUser(String name, String user, String pass){
        database = FirebaseDatabase.getInstance();
        refernce = database.getReference("admins");

        String id = refernce.push().getKey();

        Query usersQuery = refernce.child(user);
        usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Nếu dữ liệu tài khoản có email tương tự tồn tại, thì hiển thị thông báo lỗi và không cho đăng ký
                    Toast.makeText(getContext(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Admin admin = new Admin(id, user, pass, name);
                    refernce.child(user).setValue(admin);
                    Toast.makeText(getContext(), "đăng ký thành công", Toast.LENGTH_SHORT).show();

//                    getParentFragmentManager().popBackStack();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // ...
            }
        });

    }
}