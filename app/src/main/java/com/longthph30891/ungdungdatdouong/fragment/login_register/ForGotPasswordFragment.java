package com.longthph30891.ungdungdatdouong.fragment.login_register;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.longthph30891.ungdungdatdouong.databinding.FragmentForGotPasswordBinding;

public class ForGotPasswordFragment extends Fragment {
    private FragmentForGotPasswordBinding binding;
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