package com.duongnd.sipdrinkadmin.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.FragmentUpdateTypeDrinkBinding;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateTypeDrinkFragment extends Fragment {
    private FragmentUpdateTypeDrinkBinding binding;
    private String img, matl, tentl;
    private String encodeImage = matl;

    public UpdateTypeDrinkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateTypeDrinkBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            img = getArguments().getString("image");
            matl = getArguments().getString("maLoai");
            tentl = getArguments().getString("name");
        }
        getData();
        setListener();
        return binding.getRoot();
    }

    private void getData() {
        Glide.with(getActivity())
                .load(img)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.imageLoaiUpdate);
        binding.tvChooseImg.setVisibility(View.GONE);
        binding.edTenLoai.setText(tentl);
    }

    private void setListener() {
        binding.btnBack.setOnClickListener(view -> getActivity().onBackPressed());
        binding.btnUpdate.setOnClickListener(view -> {
            if (isValidDetail()) {
                update();
            }
        });
        binding.imageLoaiUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void update() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LoaiDoUong");
        String name = binding.edTenLoai.getText().toString();
        if (encodeImage != null){
            upload(matl,name,encodeImage,databaseReference);
        }else {
            uploadNotImage(matl,name,databaseReference);
        }
    }

    private void upload(String id, String name, String encodeImage, DatabaseReference databaseReference) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("LoaiDoUongImages").child(name + ".jpg");
        byte[] imageBytes = Base64.decode(encodeImage, Base64.DEFAULT);
        storageReference.putBytes(imageBytes)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        LoaiDoUong loaiDoUong = new LoaiDoUong();
                        loaiDoUong.setTypeId(id);
                        loaiDoUong.setTypeImage(encodeImage);
                        loaiDoUong.setTypeName(name);
                        databaseReference.child(name).removeValue()
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    resetFields();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getActivity(), "Lỗi lưu Realtime Database", Toast.LENGTH_SHORT).show();
                                    resetFields();
                                });

                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Lỗi upload ảnh lên Storage", Toast.LENGTH_SHORT).show();
                    resetFields();
                });
    }
    private void uploadNotImage(String id, String name, DatabaseReference databaseReference){
        LoaiDoUong loaiDoUong = new LoaiDoUong();
        loaiDoUong.setTypeId(id);
        loaiDoUong.setTypeImage(encodeImage);
        loaiDoUong.setTypeName(name);
        databaseReference.child(loaiDoUong.getTypeId()).setValue(loaiDoUong)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    resetFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Lỗi lưu Realtime Database", Toast.LENGTH_SHORT).show();
                    resetFields();
                });
    }
    private Boolean isValidDetail() {
         if (binding.edTenLoai.getText().toString().trim().isEmpty()) {
            binding.tilTenLoai.setError("Không được để trống tên");
            return false;
        } else {
            binding.tilTenLoai.setError(null);
            return true;
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnUpdate.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnUpdate.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void resetFields() {
        loading(false);
        encodeImage = null;
        binding.edTenLoai.setText("");
        binding.imageLoaiUpdate.setImageBitmap(null);
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), o -> {
                if (o.getResultCode() == getActivity().RESULT_OK) {
                    if (o.getData() != null) {
                        Uri imageUri = o.getData().getData();
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageLoaiUpdate.setImageBitmap(bitmap);
                            binding.tvChooseImg.setVisibility(View.GONE);
                            encodeImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}