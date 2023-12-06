package com.duongnd.sipdrinkadmin.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.duongnd.sipdrinkadmin.activity.MainActivity;
import com.duongnd.sipdrinkadmin.databinding.FragmentRegisterBinding;
import com.duongnd.sipdrinkadmin.model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;
    private  String userStr, emailStr, passStr, repassStr, nameStr;
    ProgressDialog dialog;
    private String deviceId;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(getLayoutInflater());

        deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        auth = FirebaseAuth.getInstance();
        dialog= new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("loading...");

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });


        return binding.getRoot();
    }


    public  Boolean validate(){
        nameStr = binding.edtNameSignup.getText().toString().trim();
        passStr = binding.edtPasswordSignup.getText().toString().trim();
        repassStr = binding.edtRepasswordSignup.getText().toString().trim();
        emailStr = binding.edtEmailSignup.getText().toString().trim();
        if (nameStr.isEmpty() || userStr.isEmpty() || repassStr.isEmpty() || passStr.isEmpty() || emailStr.isEmpty()){
            binding.edtNameSignup.setError("Tên không được để trống");
            binding.edtPasswordSignup.setError("Mật khẩu không được để trống");
            binding.edtRepasswordSignup.setError("Nhập lại mật khẩu không được để trống");
            binding.edtEmailSignup.setError("Email không được để trống");
            return false;

        }else if (nameStr.isEmpty()) {
            binding.edtNameSignup.setError("Tên không được để trống");
            return false;
        }else if(passStr.isEmpty()) {
            binding.edtPasswordSignup.setError("Mật khẩu không được để trống");
            return false;
        }else if(repassStr.isEmpty()) {
            binding.edtRepasswordSignup.setError("Nhập lại mật khẩu không được để trống");
            return false;
        }else if(emailStr.isEmpty()) {
            binding.edtEmailSignup.setError("Email không được để trống");
            return false;
        }else  if (!passStr.matches("^(?=.*[A-Z]).{6,}$")) {
            binding.edtPasswordSignup.setError("Mật khẩu phải có 5 ký tự trở lên, Ít nhất 1 chữ in hoa và 1 chữ thường !");
            return false;
        }else  if (!passStr.equals(repassStr)) {
            binding.edtRepasswordSignup.setError("Mật khẩu nhập lại không trùng khớp");
            return false;
        }else if(!emailStr.matches("^[a-zA-Z0-9_]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+){1,2}$")) {
            binding.edtEmailSignup.setError("Nhập đúng định dạng email");
            return false;
        }else {
            binding.edtNameSignup.setError(null);
            binding.edtPasswordSignup.setError(null);
            binding.edtRepasswordSignup.setError(null);
            binding.edtEmailSignup.setError(null);
            registerUser();
            return true;
        }

    }


    private void registerUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("admin");
        final FirebaseUser user = auth.getCurrentUser();

        nameStr = binding.edtNameSignup.getText().toString().trim();
        passStr = binding.edtPasswordSignup.getText().toString().trim();
        repassStr = binding.edtRepasswordSignup.getText().toString().trim();
        emailStr = binding.edtEmailSignup.getText().toString().trim();
        Query query= reference.orderByChild("diviceId").equalTo(deviceId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(getContext(), "thiết bị đã đc đang ký", Toast.LENGTH_SHORT).show();
                }
                else {
                    signUpUser(user, userStr, nameStr, emailStr, passStr);
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void signUpUser(FirebaseUser user, String userStr, String nameStr, String emailStr, String passStr) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("admin");
        auth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String Id = auth.getCurrentUser().getUid();

                    Admin admin = new Admin(Id, emailStr, passStr, nameStr, "","");


                    reference.child(Id).setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finishAffinity();

                        }
                    });

                }
            }

        });
    }
}