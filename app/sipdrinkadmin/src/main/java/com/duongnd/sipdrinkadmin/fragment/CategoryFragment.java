package com.duongnd.sipdrinkadmin.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.adapter.LoaiDoUongAdapter;
import com.duongnd.sipdrinkadmin.databinding.FragmentCategoryBinding;
import com.duongnd.sipdrinkadmin.databinding.UpdateTypedrinkLayoutBinding;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CategoryFragment extends Fragment {
    public CategoryFragment() {
    }

    private FragmentCategoryBinding binding;
    private UpdateTypedrinkLayoutBinding bindingUpdate;

    private ArrayList<LoaiDoUong> list = new ArrayList<>();
    private LoaiDoUongAdapter adapter;
    private DatabaseReference databaseReference;
    private String encodeImage;
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), o -> {
                if (o.getResultCode() == getActivity().RESULT_OK) {
                    if (o.getData() != null) {
                        Uri imageUri = o.getData().getData();
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            encodeImage = enCodeImage(bitmap);
                            bindingUpdate.imageAnhUpdate.setImageBitmap(bitmap);
                            bindingUpdate.tvChooseImg.setVisibility(View.GONE);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("LoaiDoUong");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiDoUong loai = dataSnapshot.getValue(LoaiDoUong.class);
                    list.add(loai);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adapter = new LoaiDoUongAdapter(getActivity(), list, databaseReference);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        binding.rcvLoai.setLayoutManager(layout);
        binding.rcvLoai.setAdapter(adapter);

        //
        adapter.showMenu((loaiDoUong, view) ->
                showPopupMenu(view, loaiDoUong));
        return binding.getRoot();
    }

    private void showPopupMenu(View view, LoaiDoUong loaiDoUong) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.option_menu);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_Update) {
                openDiaLogUpdate(loaiDoUong);

                return true;
            } else if (menuItem.getItemId() == R.id.nav_Delete) {
                openDiaLogDelete(loaiDoUong);
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    private void openDiaLogUpdate(LoaiDoUong loaiDoUong) {
        final DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                .setContentHolder(new ViewHolder(R.layout.update_typedrink_layout))
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT).create();
        //
        bindingUpdate = UpdateTypedrinkLayoutBinding.bind(dialogPlus.getHolderView());
        // setData on View
        bindingUpdate.edTenUpdate.setText(loaiDoUong.getTypeName());
        String img = loaiDoUong.getTypeImage();
        Glide.with(getContext()).load(img)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(bindingUpdate.imageAnhUpdate);
        dialogPlus.show();
        bindingUpdate.tvChooseImg.setVisibility(View.GONE);
        // btn
        bindingUpdate.imageAnhUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
        bindingUpdate.btnUpdate.setOnClickListener(view1 -> {
            String doiTuong = loaiDoUong.getTypeId();
            String name = bindingUpdate.edTenUpdate.getText().toString();
            if (isValidDetail(bindingUpdate)) {
                if (encodeImage == null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("typeName", name);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LoaiDoUong");
                    databaseReference.child(doiTuong).updateChildren(map)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                resetFields();
                                dialogPlus.dismiss();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Lỗi " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                resetFields();
                                dialogPlus.dismiss();

                            });
                } else {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("LoaiDoUongImages").child(doiTuong + ".jpg");
                    byte[] imageBytes = Base64.decode(encodeImage, Base64.DEFAULT);
                    storageReference.putBytes(imageBytes)
                            .addOnSuccessListener(taskSnapshot -> {
                                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("typeImage", uri.toString());
                                    map.put("typeName", name);
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LoaiDoUong");
                                    databaseReference.child(doiTuong).updateChildren(map)
                                            .addOnSuccessListener(unused -> {
                                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Good job!")
                                                        .setContentText("Update successful!")
                                                        .show();
                                                resetFields();
                                                dialogPlus.dismiss();
                                            })
                                            .addOnFailureListener(e -> {
                                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Oops...")
                                                        .setContentText("Update failed!")
                                                        .show();
                                                resetFields();
                                                dialogPlus.dismiss();

                                            });
                                });
                            })
                            .addOnFailureListener(e -> {
                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Error uploading image!")
                                        .show();
                                resetFields();
                                dialogPlus.dismiss();
                            });
                }

            }

        });
        bindingUpdate.btnBack.setOnClickListener(view -> dialogPlus.dismiss());
    }

    private void openDiaLogDelete(LoaiDoUong loaiDoUong) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("Thông Báo !");
        builder.setMessage("Bạn có muốn xóa không ?");
        builder.setNegativeButton("Không", null);
        builder.setPositiveButton("Có", (dialogInterface, i) -> {
            String doituong = loaiDoUong.getTypeId();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LoaiDoUong");
            databaseReference.child(doituong).removeValue()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Lỗi Xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
        builder.create().show();
    }

    private Boolean isValidDetail(UpdateTypedrinkLayoutBinding binding) {
        if (binding.edTenUpdate.getText().toString().trim().isEmpty()) {
            binding.tilTenUpdate.setError("Không được để trống tên");
            return false;
        } else {
            binding.tilTenUpdate.setError(null);
            return true;
        }
    }

    private void resetFields() {
        encodeImage = null;
        bindingUpdate.edTenUpdate.setText("");
        bindingUpdate.imageAnhUpdate.setImageResource(R.drawable.ic_image);
    }

    private String enCodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}