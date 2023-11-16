package com.duongnd.sipdrinkadmin.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.activity.MainActivity;
import com.duongnd.sipdrinkadmin.databinding.FragmentLoginBinding;
import com.google.common.base.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateUsername() | !validatePass()){

                }else {
                    checkUser();
                }
            }
        });
        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view_admin, new RegisterFragment())
                        .addToBackStack(RegisterFragment.class.getName())
                        .commit();
            }
        });



        return binding.getRoot();
    }


    public Boolean validateUsername(){
        String val = binding.edtLoginUsername.getText().toString().trim();
        if(val.isEmpty()){
            binding.edtLoginUsername.setError("Tên đăng nhập không được để trống");
            return false;
        }else {
            binding.edtLoginUsername.setError(null);
            return true;
        }

    }

    public Boolean validatePass(){
        String val = binding.edtLoginPassword.getText().toString().trim();
        if(val.isEmpty()){
            binding.edtLoginPassword.setError("Mật khẩu không được để trống");
            return false;
        }else {
            binding.edtLoginPassword.setError(null);
            return true;
        }

    }


    public void checkUser(){
        String userUsername = binding.edtLoginUsername.getText().toString().trim();
        String userPass = binding.edtLoginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admins");
        Query checkUserDatabase = reference.orderByChild("userName").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    binding.edtLoginUsername.setError(null);
                    String passFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passFromDB.equals(userPass)){
                        binding.edtLoginUsername.setError(null);

                        String id = snapshot.child(userUsername).child("id").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("userName").getValue(String.class);

                        Intent intent = new Intent(getActivity(), MainActivity.class);

                        intent.putExtra("id", id);
                        intent.putExtra("userName", usernameFromDB);
                        intent.putExtra("password", passFromDB);


                        // Đóng hết màn fragment lại
                        getActivity().finishAffinity();

                        // Bắt đầu activity mới
                        startActivity(intent);



                        // hàm chuyển mà
                    }else {
                        binding.edtLoginPassword.setError("Thông tin xác thực không hợp lệ");
                        binding.edtLoginPassword.requestFocus();
                    }
                }else {
                    binding.edtLoginUsername.setError("Tên đăng nhập không tồn tại");
                    binding.edtLoginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}