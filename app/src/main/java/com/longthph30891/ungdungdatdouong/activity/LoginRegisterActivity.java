package com.longthph30891.ungdungdatdouong.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.fragment.login_register.LoginFragment;


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