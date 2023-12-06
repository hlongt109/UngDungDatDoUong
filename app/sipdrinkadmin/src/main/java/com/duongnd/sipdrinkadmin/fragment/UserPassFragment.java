package com.duongnd.sipdrinkadmin.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.FragmentUserpassBinding;
import com.duongnd.sipdrinkadmin.model.Khachang;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class UserPassFragment extends Fragment {
    FragmentUserpassBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference,databaseReference;
    ProgressDialog dialog;
    Uri ImgUri;
    String userStr, nameStr, dateStr, phoneStr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("đang tải...");
        dialog.setCancelable(false);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserpassBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("admin").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Khachang khachang = snapshot.getValue(Khachang.class);
                binding.txtName.setText(khachang.getFullName());
                binding.txtUsername.setText(khachang.getUserName());
                binding.txtPhone.setText(khachang.getPhone());
                binding.txtDate.setText(khachang.getDate());
                binding.txtEmail.setText(khachang.getEmail());

                Glide.with(getContext()).load(khachang.getImg()).error(R.drawable.profilebkg).into(binding.imgAvata);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.imgAvata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PicikImage();

            }
        });

        binding.UploadInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateDate() || validatePhone()){
                    if(ImgUri != null ){
                        UploadPostFb();
                    }else {
                        UploadInfor();
                    }
                }

            }
        });
        binding.btnDoiPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPassDialog();
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

    public Boolean validateDate(){
        String val = binding.txtDate.getText().toString().trim();
        if (val.isEmpty()) {
            binding.txtDate.setError(null);
            return true;
        }else {
            if(!val.matches("^([0-9]{2})\\/([0-9]{2})\\/([0-9]{4})$")) {
                binding.txtDate.setError("Nhập đúng định dạng ngày sinh");
                return false;
            }else {
                binding.txtDate.setError(null);
                return true;
            }
        }


    }
    public Boolean validatePhone(){
        String val = binding.txtPhone.getText().toString().trim();
        if (val.isEmpty()) {
            binding.txtPhone.setError(null);
            return true;
        }else {
            if(!val.matches("^([0-9]{3})\\-([0-9]{3})\\-([0-9]{4})$")) {
                binding.txtPhone.setError("Nhập đúng định dạng số điện thoại");
                return false;
            }else {
                binding.txtPhone.setError(null);
                return true;
            }
        }

    }


    private void showPassDialog(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_pass, null);
        EditText passold = view.findViewById(R.id.edt_pass_old);
        EditText passnew = view.findViewById(R.id.edt_pass_new);
        Button btnupdate = view.findViewById(R.id.btn_update);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        AlertDialog dialogPass = builder.create();
        dialogPass.show();
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = passold.getText().toString().trim();
                String newPass = passnew.getText().toString().trim();
                if(TextUtils.isEmpty(oldPass)){
                    Toast.makeText(getContext(), "Vui lòng nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPass.matches("^(?=.*[A-Z]).{6,}$")){
                    Toast.makeText(getContext(), "Mật khẩu phải có 5 ký tự trở lên, Ít nhất 1 chữ in hoa và 1 chữ thường !", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialogPass.dismiss();
                updatePass(oldPass, newPass);
            }
        });
    }

    private void updatePass(String oldPass, String newPass) {
        dialog.show();
        FirebaseUser user1 = auth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user1.getEmail(), oldPass);
        user1.reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user1.updatePassword(newPass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        databaseReference = FirebaseDatabase.getInstance().getReference("admin").child(user.getUid());
                                        HashMap<String,Object> map = new HashMap<>();
                                        map.put("password", newPass);
                                        databaseReference.updateChildren(map);

                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Đổi mật khẩu thành công ", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void UploadPostFb() {
        dialog.show();
        nameStr = binding.txtName.getEditableText().toString();
        dateStr = binding.txtDate.getText().toString();
        phoneStr= binding.txtPhone.getText().toString();

        String file = "Photo/" + "admin" + user.getUid();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(file);
        storageReference.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String img = uri.toString();
                        databaseReference = FirebaseDatabase.getInstance().getReference("admin").child(user.getUid());
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("img",img);
                        map.put("fullName", nameStr);
                        map.put("date", dateStr);
                        map.put("phone", phoneStr);
                        databaseReference.updateChildren(map);
                        dialog.dismiss();

                    }
                });
            }
        });
    }

    private void UploadInfor(){
        dialog.show();
        userStr= binding.txtUsername.getEditableText().toString();
        nameStr = binding.txtName.getEditableText().toString();
        dateStr = binding.txtDate.getText().toString();
        phoneStr= binding.txtPhone.getText().toString();

        databaseReference = FirebaseDatabase.getInstance().getReference("admin").child(user.getUid());
        HashMap<String,Object> map = new HashMap<>();
        map.put("userName", userStr);
        map.put("fullName", nameStr);
        map.put("date", dateStr);
        map.put("phone", phoneStr);
        databaseReference.updateChildren(map);
        dialog.dismiss();
        Toast.makeText(getContext(), " Cập nhật thành công ", Toast.LENGTH_SHORT).show();



    }


    private void PicikImage() {

        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent(intent -> {
                    activityResultLauncher.launch(intent);
                    return null;

                });
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                Intent data = result.getData();
                if(data != null &&  result.getResultCode() == Activity.RESULT_OK){
                    ImgUri = data.getData();
                    binding.imgAvata.setImageURI(ImgUri);
                }else {
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }
            }
    );
    


}