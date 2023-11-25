package com.duongnd.sipdrinkadmin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duongnd.sipdrinkadmin.dao.ChiTietDonHangDAO;
import com.duongnd.sipdrinkadmin.dao.userDAO;
import com.duongnd.sipdrinkadmin.databinding.ItemOrderDetailsBinding;
import com.duongnd.sipdrinkadmin.model.ChiTietDonHang;
import com.duongnd.sipdrinkadmin.model.DonHang;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.viewHolder> {
    userDAO dao;
    ChiTietDonHangDAO chiTietDonHangDAO;
    private final ArrayList<DonHang> list;
    private final Context context;
    private ArrayList<ChiTietDonHang>listChiTiet = new ArrayList<>();

    public OrderAdapter(ArrayList<DonHang> listDonHang, Context context) {
        this.list = listDonHang;
        this.context = context;
    }
    public void updateChiTietDonHangList(ArrayList<ChiTietDonHang> newList) {
        listChiTiet = newList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailsBinding binding = ItemOrderDetailsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ItemOrderDetailsBinding binding;

        viewHolder(ItemOrderDetailsBinding itemOrderBinding) {
            super(itemOrderBinding.getRoot());
            binding = itemOrderBinding;
        }

        void setData(DonHang donHang) {
            dao = new userDAO();
            chiTietDonHangDAO = new ChiTietDonHangDAO();
            chiTietDonHangDAO.selectAll(donHang.getIdDonHang(), list ->updateChiTietDonHangList(list));
            binding.rcvDrinkOder.setLayoutManager(new LinearLayoutManager(context));
            binding.rcvDrinkOder.setAdapter(new OrderDetailsAdapter(listChiTiet,context));
//            binding.tvTotalQuantity.setText();
//            binding.tvTotalPrice.setText();
            if (donHang.getTrangThai().equals("choxacnhan")) {
                binding.tvTrangThaiOrder.setText("Đơn hàng đang chờ xác nhận");
            }
            binding.tvOderCreateTime.setText(donHang.getNgayMua());
            dao.getNameUserById(donHang.getIdKH(), tenKH ->{
                binding.tvTenKh.setText(tenKH);
            });
            dao.getAddress(donHang.getIdKH(), address -> {
                binding.tvDiaChi.setText(address);
            });
        }
    }
}
