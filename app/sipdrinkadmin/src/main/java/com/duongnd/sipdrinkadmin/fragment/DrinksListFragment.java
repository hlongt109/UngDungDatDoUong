package com.duongnd.sipdrinkadmin.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.adapter.DoUongAdapter;
import com.duongnd.sipdrinkadmin.adapter.ListTyeDrinkAdapter;
import com.duongnd.sipdrinkadmin.dao.LoaiDoUongDAO;
import com.duongnd.sipdrinkadmin.databinding.FragmentDrinksListBinding;
import com.duongnd.sipdrinkadmin.databinding.UpdateDrinkLayoutBinding;
import com.duongnd.sipdrinkadmin.model.DoUong;
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

public class DrinksListFragment extends Fragment {
    public DrinksListFragment() {
    }

    private FragmentDrinksListBinding binding;
    private UpdateDrinkLayoutBinding bindingUpdate;
    private ArrayList<DoUong> listDoUong = new ArrayList<>();
    private DoUongAdapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<LoaiDoUong> listTypeDrinks;
    private LoaiDoUongDAO dao;
    private ListTyeDrinkAdapter listTyeDrinkAdapter;
    private String maLoai = "", tenLoai = "", encodeImage, selectedTypeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            maLoai = getArguments().getString("maLoai");
            tenLoai = getArguments().getString("tenLoai");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDrinksListBinding.inflate(inflater, container, false);
        binding.tvListTitle.setText("Danh sách " + tenLoai);
        databaseReference = FirebaseDatabase.getInstance().getReference("DoUong");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listDoUong.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DoUong doUong = dataSnapshot.getValue(DoUong.class);

                    if (doUong != null && doUong.getMaLoai().equals(maLoai)) {
                        listDoUong.add(doUong);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adapter = new DoUongAdapter(getActivity(), listDoUong, databaseReference);
        binding.gridViewDrinks.setAdapter(adapter);
        //
        adapter.showMenu((doUong, view) -> {
            showPopupMenu(doUong, view);
        });
        //
        binding.btnBack.setOnClickListener(view -> getActivity().onBackPressed());
        return binding.getRoot();
    }

    private void showPopupMenu(DoUong doUong, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.option_menu);
        popupMenu.setGravity(Gravity.CENTER_VERTICAL);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_Update) {
                openDiaLogUpdate(doUong);
                return true;
            } else if (menuItem.getItemId() == R.id.nav_Delete) {
                openDiaLogDelete(doUong);
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    private void openDiaLogDelete(DoUong doUong) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("Thông Báo !");
        builder.setMessage("Bạn có muốn xóa không ?");
        builder.setNegativeButton("Không", null);
        builder.setPositiveButton("Có", (dialogInterface, i) -> {
            String doituong = doUong.getIdDoUong();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DoUong");
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

    private void openDiaLogUpdate(DoUong doUong) {
        final DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                .setContentHolder(new ViewHolder(R.layout.update_drink_layout))
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT).create();
        bindingUpdate = UpdateDrinkLayoutBinding.bind(dialogPlus.getHolderView());
        setDataOnFields(doUong, bindingUpdate);
        //
        bindingUpdate.spinerLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LoaiDoUong SelectedId = (LoaiDoUong) adapterView.getSelectedItem();
                selectedTypeId = SelectedId.getTypeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        bindingUpdate.imageDrinkUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
        bindingUpdate.btnUpdate.setOnClickListener(view -> {
            String doiTuong = doUong.getIdDoUong();
            String ten = bindingUpdate.edTen.getText().toString();
            String gia = bindingUpdate.edGia.getText().toString();
            String mota = bindingUpdate.edMoTa.getText().toString();
            String tthai;
            if (bindingUpdate.rdoDangBan.isChecked()) {
                tthai = "DangBan";
            } else if (bindingUpdate.rdoHetHang.isChecked()) {
                tthai = "HetHang";
            } else if (bindingUpdate.rdoMoi.isChecked()) {
                tthai = "Moi";
            } else {
                tthai = "";
            }
            if (isValidDetail(bindingUpdate)) {
                loading(true, bindingUpdate);
                if (encodeImage == null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("tenDoUong",ten);
                    map.put("gia",Double.parseDouble(gia));
                    map.put("mota",mota);
                    map.put("maLoai",selectedTypeId);
                    map.put("trangThai",tthai);
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("DoUong");
                    databaseReference1.child(doiTuong).updateChildren(map)
                            .addOnSuccessListener(unused -> {
                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Good job!")
                                        .setContentText("Update successful!!")
                                        .show();
                                resetFields(bindingUpdate);
                                dialogPlus.dismiss();
                            })
                            .addOnFailureListener(e -> {
                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Update failde!")
                                        .show();
                                resetFields(bindingUpdate);
                                dialogPlus.dismiss();
                            });
                } else {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("DoUongImages").child(ten+".jpg");
                    byte[] imageBytes = Base64.decode(encodeImage, Base64.DEFAULT);
                    storageReference.putBytes(imageBytes)
                            .addOnSuccessListener(taskSnapshot -> {
                                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("tenDoUong",ten);
                                    map.put("gia",Double.parseDouble(gia));
                                    map.put("mota",mota);
                                    map.put("maLoai",selectedTypeId);
                                    map.put("trangThai",tthai);
                                    map.put("image",uri.toString());
                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("DoUong");
                                    databaseReference1.child(doiTuong).updateChildren(map)
                                            .addOnSuccessListener(unused -> {
                                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Good job!")
                                                        .setContentText("Update successful!")
                                                        .show();
                                                resetFields(bindingUpdate);
                                                dialogPlus.dismiss();
                                            })
                                            .addOnFailureListener(e -> {
                                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Oops...")
                                                        .setContentText("Update failed!")
                                                        .show();
                                                resetFields(bindingUpdate);
                                                dialogPlus.dismiss();
                                            });
                                });
                            })
                            .addOnFailureListener(e -> {
                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Error uploading image!")
                                        .show();
                                resetFields(bindingUpdate);
                                dialogPlus.dismiss();
                            });
                }
            }

        });
        bindingUpdate.btnBack.setOnClickListener(view -> dialogPlus.dismiss());
        dialogPlus.show();
    }

    public void setDataOnFields(DoUong doUong, UpdateDrinkLayoutBinding bindingUpdate) {
        Glide.with(getContext()).load(doUong.getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA).into(bindingUpdate.imageDrinkUpdate);
        bindingUpdate.edTen.setText(doUong.getTenDoUong());
        bindingUpdate.edGia.setText(String.valueOf(doUong.getGia()));
        bindingUpdate.edMoTa.setText(doUong.getMota());
        if (doUong.getTrangThai().equals("DangBan")) {
            bindingUpdate.rdoDangBan.setChecked(true);
        } else if (doUong.getTrangThai().equals("HetHang")) {
            bindingUpdate.rdoHetHang.setChecked(true);
        } else if (doUong.getTrangThai().equals("Moi")) {
            bindingUpdate.rdoMoi.setChecked(true);
        }
        listTypeDrinks = new ArrayList<>();
        dao = new LoaiDoUongDAO();
        dao.selectAll(list -> {
            listTypeDrinks = list;
            listTyeDrinkAdapter = new ListTyeDrinkAdapter(getActivity(), listTypeDrinks);
            bindingUpdate.spinerLoai.setAdapter(listTyeDrinkAdapter);
            for (int i = 0; i < list.size(); i++) {
                if (listTypeDrinks.get(i).getTypeId().equals(doUong.getMaLoai())) {
                    bindingUpdate.spinerLoai.setSelection(i);
                    break;
                }
            }
        });
        bindingUpdate.tvChooseImg.setVisibility(View.GONE);
    }

    private Boolean isValidDetail(UpdateDrinkLayoutBinding binding) {
        if (binding.edTen.getText().toString().trim().isEmpty()) {
            binding.tilNameDrink.setError("Không để trống tên đồ uống");
            return false;
        } else if (binding.edGia.getText().toString().trim().isEmpty()) {
            binding.tilGia.setError("Không để trống giá");
            return false;
        } else if (binding.edMoTa.getText().toString().trim().isEmpty()) {
            binding.tilMoTa.setError("Không để trống mô tả");
            return false;
        } else {
            try {
                int price = Integer.parseInt(binding.edGia.getText().toString());
                if(price < 0){
                    binding.tilGia.setError("Giá phải lớn hơn 0");
                    return false;
                }
            }catch (NumberFormatException e){
                binding.tilGia.setError("Giá phải là số");
                return false;
            }
            binding.tilNameDrink.setError(null);
            binding.tilGia.setError(null);
            binding.tilMoTa.setError(null);
            return true;
        }
    }

    private void loading(Boolean isLoading, UpdateDrinkLayoutBinding binding) {
        if (isLoading) {
            binding.btnUpdate.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnUpdate.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void resetFields(UpdateDrinkLayoutBinding binding) {
        loading(false, binding);
        encodeImage = null;
        binding.edTen.setText("");
        binding.edGia.setText("");
        binding.edMoTa.setText("");
        binding.rdoDangBan.setChecked(true);
        binding.spinerLoai.setSelection(0);
        binding.imageDrinkUpdate.setImageResource(R.drawable.ic_image);

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

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), o -> {
                if (o.getResultCode() == getActivity().RESULT_OK) {
                    if (o.getData() != null) {
                        Uri imageUri = o.getData().getData();
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            encodeImage = enCodeImage(bitmap);
                            bindingUpdate.imageDrinkUpdate.setImageBitmap(bitmap);
                            bindingUpdate.tvChooseImg.setVisibility(View.GONE);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}