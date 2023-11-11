package com.duongnd.sipdrinkadmin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import com.duongnd.sipdrinkadmin.databinding.ActivityLoginBinding;
import com.duongnd.sipdrinkadmin.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        onTextChange();
        binding.tvSignIn.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        binding.btnSignUp.setOnClickListener(v -> {
            if (ValidSignUpDetails()) {
                signUp();
            }
        });
    }

    private void signUp() {
        loading(true);
        // ....
    }

    private Boolean ValidSignUpDetails() {
        String email = binding.edEmailSu.getText().toString().trim();
        String name = binding.edNameSu.getText().toString().trim();
        String username = binding.edUsernameSu.getText().toString().trim();
        String password = binding.edPasswordSu.getText().toString().trim();
        String cfPass = binding.edConfirmPasswordSu.getText().toString().trim();
        if (email.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty() || cfPass.isEmpty()) {
            if (email.isEmpty()) {
                binding.tilEmailSu.setError("Please enter your email");
            } else {
                binding.tilEmailSu.setError(null);
            }
            if (name.isEmpty()) {
                binding.tilNameSu.setError("Please enter your name");
            } else {
                binding.tilNameSu.setError(null);
            }
            if (username.isEmpty()) {
                binding.tilUsernameSu.setError("Please enter a username");
            } else {
                binding.tilUsernameSu.setError(null);
            }
            if (password.isEmpty()) {
                binding.tilPasswordSu.setError("Please enter a password");
            } else {
                binding.tilPasswordSu.setError(null);
            }
            if (cfPass.isEmpty()) {
                binding.tilConfirmPassSu.setError("Please enter a password");
            } else {
                binding.tilConfirmPassSu.setError(null);
            }
            return false;
        } else{
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmailSu.setError("Please enter a valid email address");
                return false;
            }else {
                binding.tilEmailSu.setError(null);
            }
            if (!password.equals(cfPass)) {
                binding.tilPasswordSu.setError("Password and Confirm password do not match");
                binding.tilConfirmPassSu.setError("Password and Confirm Password do not match");
                return false;
            } else {
                binding.tilPasswordSu.setError(null);
                binding.tilConfirmPassSu.setError(null);
                return true;
            }
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void onTextChange() {
        binding.edEmailSu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               String text = charSequence.toString();
               if (!text.isEmpty()) {
                   binding.tilEmailSu.setError(null);
               }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        binding.edNameSu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               String text = charSequence.toString();
               if(!text.isEmpty()){
                   binding.tilNameSu.setError(null);
               }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        binding.edUsernameSu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if(!text.isEmpty()){
                    binding.tilUsernameSu.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        binding.edPasswordSu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if(!text.isEmpty()){
                    binding.tilPasswordSu.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        binding.edConfirmPasswordSu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if(!text.isEmpty()){
                    binding.tilConfirmPassSu.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}