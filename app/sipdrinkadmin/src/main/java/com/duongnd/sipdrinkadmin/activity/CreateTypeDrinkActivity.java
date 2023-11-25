package com.duongnd.sipdrinkadmin.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.ActivityCreateTypeDrinkBinding;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CreateTypeDrinkActivity extends AppCompatActivity {
    private ActivityCreateTypeDrinkBinding binding;
    private String encodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTypeDrinkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        binding.btnBack.setOnClickListener(view -> onBackPressed());
        binding.btnAdd.setOnClickListener(view -> {
            if (isValidDetail()) {
                addNewData();
            }
        });
        binding.imageLoaiCreate.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void addNewData() {
        loading(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LoaiDoUong");
        String id = databaseReference.push().getKey();
        String name = binding.edTenLoai.getText().toString();
        if(id != null){
            uploadImageToStorage(id,name,encodeImage,databaseReference);
        }else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Key error!")
                    .show();
            resetFields();
        }

    }
    private void uploadImageToStorage(String id, String name, String encodeImage,DatabaseReference databaseReference){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("LoaiDoUongImages").child(id + ".jpg");
        byte[] imageBytes = Base64.decode(encodeImage, Base64.DEFAULT);
        storageReference.putBytes(imageBytes)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        LoaiDoUong loaiDoUong = new LoaiDoUong(id, name, uri.toString());
                        databaseReference.child(id).setValue(loaiDoUong)
                                .addOnSuccessListener(unused -> {
                                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Good job!")
                                            .setContentText("Successful!")
                                            .show();
                                    resetFields();
                                })
                                .addOnFailureListener(e -> {
                                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Failed!")
                                            .show();
                                    resetFields();
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Error uploading image!")
                            .show();
                    resetFields();
                });
    }
    private Boolean isValidDetail() {
        if (encodeImage == null) {
            Toast.makeText(this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.edTenLoai.getText().toString().trim().isEmpty()) {
            binding.tilTenLoai.setError("Không được để trống tên");
            return false;
        } else {
            binding.tilTenLoai.setError(null);
            return true;
        }
    }

    private void resetFields() {
        loading(false);
        encodeImage = null;
        binding.edTenLoai.setText("");
        binding.imageLoaiCreate.setImageResource(R.drawable.ic_image);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnAdd.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnAdd.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), o -> {
                if (o.getResultCode() == RESULT_OK) {
                    if (o.getData() != null) {
                        Uri imageUri = o.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageLoaiCreate.setImageBitmap(bitmap);
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