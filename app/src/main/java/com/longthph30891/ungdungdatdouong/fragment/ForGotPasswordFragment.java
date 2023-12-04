package com.longthph30891.ungdungdatdouong.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.databinding.FragmentForGotPasswordBinding;
import com.longthph30891.ungdungdatdouong.databinding.FragmentLoginBinding;

import java.util.UUID;
import java.util.regex.Pattern;

public class ForGotPasswordFragment extends Fragment {
    FragmentForGotPasswordBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForGotPasswordBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");

        binding.btnResetRepass.setOnClickListener(view -> {
            forgirPass();
        });
        binding.btnCanleRepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }

    private void forgirPass() {
        dialog.show();
        auth.sendPasswordResetEmail(binding.edtEmailRepass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    getParentFragmentManager().popBackStack();
                    Toast.makeText(getContext(), "Check email", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "nhập lại email", Toast.LENGTH_SHORT).show();
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