package com.duongnd.sipdrinkadmin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.fragment.PersonalFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_mani, new PersonalFragment());
        transaction.commit();
    }
}