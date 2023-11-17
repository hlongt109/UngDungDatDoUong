package com.duongnd.sipdrinkadmin.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.R;

import com.duongnd.sipdrinkadmin.Utilities.Category_interface;
import com.duongnd.sipdrinkadmin.databinding.UpdateTypedrinkLayoutBinding;
import com.duongnd.sipdrinkadmin.fragment.DrinksListFragment;
import com.duongnd.sipdrinkadmin.fragment.UpdateTypeDrinkFragment;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoaiDoUongAdapter extends RecyclerView.Adapter<LoaiDoUongAdapter.viewHolder> {
    private final Context context;
    private final ArrayList<LoaiDoUong> list;
    private DatabaseReference databaseReference;
    Category_interface categoryInterface;

    public LoaiDoUongAdapter(Context context, ArrayList<LoaiDoUong> list, DatabaseReference database) {
        this.context = context;
        this.list = list;
        this.databaseReference = database;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_loai_do_uong, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        LoaiDoUong loaiDoUong = list.get(position);
        holder.tvTenLoai.setText(list.get(position).getTypeName());
        Glide.with(context).load(loaiDoUong.getTypeImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.imgLoai).getRequest();
        holder.itemView.setOnClickListener(view -> {
            showListDoUong(loaiDoUong);
        });
        holder.btnTienIch.setOnClickListener(view -> {
            categoryInterface.onClickMenu(loaiDoUong,view);
//            showPopupMenu(view, loaiDoUong);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imgLoai, btnTienIch;
        TextView tvTenLoai;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imgLoai = itemView.findViewById(R.id.image_Loai_item);
            tvTenLoai = itemView.findViewById(R.id.tvTenLoai_item);
            btnTienIch = itemView.findViewById(R.id.imv_tien_ich);
        }
    }

    private void showListDoUong(LoaiDoUong loaiDoUong) {
        DrinksListFragment drinksListFragment = new DrinksListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("maLoai", loaiDoUong.getTypeId());
        drinksListFragment.setArguments(bundle);
        //
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, drinksListFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showPopupMenu(View view, LoaiDoUong loaiDoUong) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.option_menu);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_Update) {
                openDialogUpdate(loaiDoUong);
//                openUpdateFragment(loaiDoUong);
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

    private void openDiaLogDelete(LoaiDoUong loaiDoUong) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("Warning !");
        builder.setMessage("Do you want to delete ?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            String name = loaiDoUong.getTypeName();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LoaiDoUong");
            databaseReference.child(name).removeValue()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Lỗi Xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
        builder.create().show();
    }

    private void openDialogUpdate(LoaiDoUong loaiDoUong) {
        final DialogPlus dialogPlus = DialogPlus.newDialog(context)
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
        Glide.with(context).load(img)
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
                        Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error Updating", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss();

                    });
        });
    }

    private void openUpdateFragment(LoaiDoUong loaiDoUong) {
        UpdateTypeDrinkFragment updateTypeDrinkFragment = new UpdateTypeDrinkFragment();
        Bundle bundle = new Bundle();
        bundle.putString("image", loaiDoUong.getTypeImage());
        bundle.putString("maLoai", loaiDoUong.getTypeId());
        bundle.putString("name", loaiDoUong.getTypeName());
        updateTypeDrinkFragment.setArguments(bundle);

        AppCompatActivity activity = (AppCompatActivity) context;
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, updateTypeDrinkFragment)
                .addToBackStack(null)
                .commit();
    }
    public void showMenu(Category_interface categoryInterface){
        this.categoryInterface = categoryInterface;
    }
}
