package com.longthph30891.ungdungdatdouong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.model.OrderDetail;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    Context context;
    List<OrderDetail> orderDetails;

    @NonNull
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.ViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetails.get(position);
        holder.txtNameProduct.setText(orderDetail.getNameProduct());
        holder.txtQuantity.setText(orderDetail.getQuantity());
        Picasso.get().load(orderDetail.getImageProduct()).into(holder.imgProduct);
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String price = numberFormat.format(orderDetail.getPrice());
        holder.txtPrice.setText(price);
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameProduct, txtPrice, txtQuantity;
        ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.tvNameProduct);
            txtPrice = itemView.findViewById(R.id.tvPrice);
            txtQuantity = itemView.findViewById(R.id.tvQuantity);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
