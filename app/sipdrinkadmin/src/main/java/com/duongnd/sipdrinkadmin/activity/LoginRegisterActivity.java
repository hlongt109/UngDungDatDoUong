package com.duongnd.sipdrinkadmin.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.fragment.LoginFragment;
import com.duongnd.sipdrinkadmin.fragment.RegisterFragment;

public class LoginRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view_admin, new LoginFragment());
        transaction.commit();
    }
}