package com.longthph30891.ungdungdatdouong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.databinding.ActivityMainBinding;
import com.longthph30891.ungdungdatdouong.fragment.LoginFragment;
import com.longthph30891.ungdungdatdouong.fragment.PersonalFragment;
import com.longthph30891.ungdungdatdouong.fragment.ProfileFragment;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_mani, new PersonalFragment());
        transaction.commit();

    }
}