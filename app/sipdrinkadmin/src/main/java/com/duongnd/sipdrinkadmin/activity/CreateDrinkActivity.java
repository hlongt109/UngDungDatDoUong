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
import android.widget.AdapterView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateDrinkActivity extends AppCompatActivity {
    private ActivityCreateDrinkBinding binding;
    private String encodeImage;
    private ListTyeDrinkAdapter listTyeDrinkAdapter;
    private LoaiDoUongDAO dao;
    private ArrayList<LoaiDoUong>listLoai;
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
        binding.btnBack.setOnClickListener(view -> onBackPressed());
        binding.btnAdd.setOnClickListener(view -> {
            if(isValidDetails()){
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
        String trangThai = "";
        if(binding.rdoDangBan.isChecked()){
            trangThai = "DangBan";
        } else if (binding.rdoHetHang.isChecked()){
            trangThai = "HetHang";
        }
       if(id != null){
           upload(id,maTheLoai,name,gia,trangThai,encodeImage,databaseReference);
       }else {
           Toast.makeText(this, "Không thể tạo key", Toast.LENGTH_SHORT).show();
           resetFields();
       }
    }
    private void upload(String id, String maTl, String ten, String gia, String trangThai ,String encodeImage,DatabaseReference databaseReference){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("DoUongImages").child(id + ".jpg");
        byte[] imageBytes = Base64.decode(encodeImage, Base64.DEFAULT);
        storageReference.putBytes(imageBytes)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        DoUong doUong = new DoUong(id,maTl,ten,gia,trangThai,uri.toString());
                        databaseReference.child(ten).setValue(doUong)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    resetFields();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Lỗi lưu Realtime Database", Toast.LENGTH_SHORT).show();
                                    resetFields();
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi upload ảnh lên Storage", Toast.LENGTH_SHORT).show();
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
        if(encodeImage == null){
            Toast.makeText(this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
            return false;
        }else if (ten.isEmpty()){
            binding.tilNameDrink.setError("Không để trống");
            return false;
        }else if (gia.isEmpty()){
            binding.tilGia.setError("Chưa nhập giá");
            return false;
        }else {
            binding.tilGia.setError(null);
            binding.tilNameDrink.setError(null);
            return true;
        }
    }
    private void resetFields(){

    }
    private String endcodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
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
}