package com.longthph30891.ungdungdatdouong.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Objects;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.activity.MainActivity;
import com.longthph30891.ungdungdatdouong.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    String emailStr, passStr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();

        dialog= new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("loading...");
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        }
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInUser();
            }
        });

        binding.txtforgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view_admin, new ForGotPasswordFragment())
                        .addToBackStack(ForGotPasswordFragment.class.getName())
                        .commit();
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
        }else if(!val.matches("^[a-zA-Z0-9_]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+){1,2}$")) {
            binding.edtLoginUsername.setError("Nhập đúng định dạng email");
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

    private void logInUser() {
        if(!validateUsername() || !validatePass()){
            return;

        }

        emailStr = binding.edtLoginUsername.getText().toString();
        passStr = binding.edtLoginPassword.getText().toString();
        dialog.show();

        logInUsers(emailStr, passStr);
    }
    private void logInUsers(String emailStr, String passStr) {
        emailStr = binding.edtLoginUsername.getText().toString();
        passStr = binding.edtLoginPassword.getText().toString();
        dialog.show();
        auth.signInWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}