package com.duongnd.sipdrinkadmin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }
    private void setListener() {
        onTextChange();
        binding.tvSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
        binding.tvPorgotPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotActivity.class)));
        binding.btnLogin.setOnClickListener(v -> {
            if (ValidLoginDetails()) {
                logIn();
            }
        });
    }
    private void logIn() {
        loading(true);
        // ....
    }
    private Boolean ValidLoginDetails() {
        String usn = binding.edUsernameLg.getText().toString().trim();
        String pass = binding.edPasswordLg.getText().toString().trim();
        if (usn.isEmpty() || pass.isEmpty()) {
            if (usn.isEmpty()) {
                binding.tilUsenameLg.setError("Please enter your username");
            } else {
                binding.tilUsenameLg.setError(null);
            }
            if (pass.isEmpty()) {
                binding.tilPasswordLg.setError("Please enter your password");
            } else {
                binding.tilPasswordLg.setError(null);
            }
            return false;
        } else {
            return true;
        }
    }
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnLogin.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void onTextChange(){
        binding.edUsernameLg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String username =charSequence.toString();
                if(username.isEmpty()){
                    binding.tilUsenameLg.setError("Please enter your username");
                }else {
                    binding.tilUsenameLg.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        binding.edPasswordLg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password =charSequence.toString();
                if (password.isEmpty()) {
                    binding.tilPasswordLg.setError("Please enter your password");
                } else {
                    binding.tilPasswordLg.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}