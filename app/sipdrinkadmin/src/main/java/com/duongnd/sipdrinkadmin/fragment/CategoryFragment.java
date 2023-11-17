package com.duongnd.sipdrinkadmin.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.Utilities.Category_interface;
import com.duongnd.sipdrinkadmin.adapter.LoaiDoUongAdapter;
import com.duongnd.sipdrinkadmin.databinding.FragmentCategoryBinding;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryFragment extends Fragment {
    public CategoryFragment() {}
    private FragmentCategoryBinding binding;
    private ArrayList<LoaiDoUong> list = new ArrayList<>();
    private LoaiDoUongAdapter adapter;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater,container,false);
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
        adapter = new LoaiDoUongAdapter(getActivity(),list,databaseReference);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        binding.rcvLoai.setLayoutManager(layout);
        binding.rcvLoai.setAdapter(adapter);
        //
       adapter.showMenu(new Category_interface() {
           @Override
           public void onClickMenu(LoaiDoUong loaiDoUong, View view) {
               showPopupMenu(view,loaiDoUong);
           }
       });
        return binding.getRoot();
    }
    private void showPopupMenu(View view, LoaiDoUong loaiDoUong) {
        PopupMenu popupMenu =  new PopupMenu(getContext(), view);
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
                .setExpanded(true, 1250).create();
        //
        View view = dialogPlus.getHolderView();
        TextInputLayout til_ten = view.findViewById(R.id.til_TenUpdate);
        TextInputEditText ten = view.findViewById(R.id.ed_Ten_update);
        ImageView imageView = view.findViewById(R.id.imageAnh_update);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        // setData on View
        ten.setText(loaiDoUong.getTypeName());
        String img = loaiDoUong.getTypeImage();;
        Glide.with(getContext()).load(img)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView);
        dialogPlus.show();
        // btn
        btnUpdate.setOnClickListener(view1 -> {
            String name = ten.getText().toString();
            String doiTuong = loaiDoUong.getTypeName();
            Map<String, Object> map = new HashMap<>();
            map.put("typeImage", img);
            map.put("typeName", name);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LoaiDoUong");
            databaseReference.child(doiTuong).updateChildren(map)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error Updating", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss();

                    });
        });
    }

    private void openDiaLogDelete(LoaiDoUong loaiDoUong) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("Warning !");
        builder.setMessage("Do you want to delete ?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            String name = loaiDoUong.getTypeName();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LoaiDoUong");
            databaseReference.child(name).removeValue()
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
}