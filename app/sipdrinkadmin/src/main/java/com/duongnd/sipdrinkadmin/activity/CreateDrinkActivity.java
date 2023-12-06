package com.duongnd.sipdrinkadmin.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.adapter.ListTyeDrinkAdapter;
import com.duongnd.sipdrinkadmin.dao.LoaiDoUongDAO;
import com.duongnd.sipdrinkadmin.databinding.ActivityCreateDrinkBinding;
import com.duongnd.sipdrinkadmin.model.DoUong;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CreateDrinkActivity extends AppCompatActivity {
    private ActivityCreateDrinkBinding binding;
    private String encodeImage;
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), o -> {
                if (o.getResultCode() == RESULT_OK) {
                    if (o.getData() != null) {
                        Uri imageUri = o.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageDrinkCreate.setImageBitmap(bitmap);
                            binding.tvChooseImg.setVisibility(View.GONE);
                            encodeImage = endcodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private ListTyeDrinkAdapter listTyeDrinkAdapter;
    private LoaiDoUongDAO dao;
    private ArrayList<LoaiDoUong> listLoai;
    private String maTheLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateDrinkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        selectLoaiDoUong();
    }

    private void setListener() {
        binding.rdoMoi.setChecked(true);
        binding.btnBack.setOnClickListener(view -> onBackPressed());
        binding.btnAdd.setOnClickListener(view -> {
            if (isValidDetails()) {
                addNewData();
            }
        });
        binding.imageDrinkCreate.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void addNewData() {
        loading(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DoUong");
        String id = databaseReference.push().getKey();
        String name = binding.edTen.getText().toString();
        String gia = binding.edGia.getText().toString();
        String mota = binding.edMoTa.getText().toString();
        String trangThai = "";
        if (binding.rdoDangBan.isChecked()) {
            trangThai = "DangBan";
        } else if (binding.rdoHetHang.isChecked()) {
            trangThai = "HetHang";
        } else if (binding.rdoMoi.isChecked()) {
            trangThai = "Moi";
        }
        if (id != null) {
            upload(id, maTheLoai, name, gia, trangThai, encodeImage, mota, databaseReference);
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Key error!")
                    .show();
            resetFields();
        }
    }

    private void upload(String id, String maTl, String ten, String gia, String trangThai, String encodeImage, String mota, DatabaseReference databaseReference) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("DoUongImages").child(id + ".jpg");
        byte[] imageBytes = Base64.decode(encodeImage, Base64.DEFAULT);
        storageReference.putBytes(imageBytes)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        DoUong doUong = new DoUong(id, maTl, ten, Integer.parseInt(gia), trangThai, uri.toString(), mota);
                        databaseReference.child(id).setValue(doUong)
                                .addOnSuccessListener(unused -> {
                                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Good job!")
                                            .setContentText("Successful!")
                                            .show();
                                    resetFields();
                                    ;
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

    private void selectLoaiDoUong() {
        listLoai = new ArrayList<>();
        dao = new LoaiDoUongDAO();
        dao.selectAll(list -> {
            listLoai = list;
            listTyeDrinkAdapter = new ListTyeDrinkAdapter(getApplicationContext(), listLoai);
            binding.spinerLoai.setAdapter(listTyeDrinkAdapter);
            binding.spinerLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LoaiDoUong selectedLoaiDoUong = (LoaiDoUong) parent.getSelectedItem();
                    maTheLoai = selectedLoaiDoUong.getTypeId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        });
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

    private Boolean isValidDetails() {
        String ten = binding.edTen.getText().toString().trim();
        String gia = binding.edGia.getText().toString().trim();
        String mota = binding.edMoTa.getText().toString().trim();
        if (encodeImage == null) {
            Toast.makeText(this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ten.isEmpty()) {
            binding.tilNameDrink.setError("Không để trống");
            return false;
        } else if (gia.isEmpty()) {
            binding.tilGia.setError("Chưa nhập giá");
            return false;
        } else if (mota.isEmpty()) {
            binding.tilMoTaDrink.setError("Chưa nhập mô tả");
            return false;
        } else {
            try {
                int price = Integer.parseInt(binding.edGia.getText().toString());
                if (price < 0) {
                    binding.tilGia.setError("Giá phải lớn hơn 0");
                    return false;
                }
            } catch (NumberFormatException e) {
                binding.tilGia.setError("Giá phải là số");
                return false;
            }
            binding.tilGia.setError(null);
            binding.tilNameDrink.setError(null);
            return true;
        }
    }

    private void resetFields() {
        loading(false);
        encodeImage = null;
        binding.edTen.setText("");
        binding.edGia.setText("");
        binding.edMoTa.setText("");
        binding.spinerLoai.setSelection(0);
        binding.groupRdo.clearCheck();
        binding.imageDrinkCreate.setImageResource(R.drawable.ic_image);
    }

    private String endcodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}