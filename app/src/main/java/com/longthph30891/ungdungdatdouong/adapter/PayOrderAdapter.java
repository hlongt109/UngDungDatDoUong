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
import com.longthph30891.ungdungdatdouong.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PayOrderAdapter extends RecyclerView.Adapter<PayOrderAdapter.ViewHolder> {

    private Context context;
    private List<Cart> selectedItems;

    public PayOrderAdapter(Context context, List<Cart> selectedItems) {
        this.context = context;
        this.selectedItems = selectedItems;
    }

    @NonNull
    @Override
    public PayOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayOrderAdapter.ViewHolder holder, int position) {
        Cart cart = selectedItems.get(position);
        holder.txtSelectedItemName.setText(cart.getProductName());
        Locale vn = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(vn);
        String price = format.format(cart.getProductPrice());
        holder.txtSelectedItemPrice.setText(price);
        holder.txtTotalItems.setText("x" + cart.getSoLuong());

        Picasso.get().load(cart.getProductImage()).into(holder.img_order);
    }

    @Override
    public int getItemCount() {
        return selectedItems.size();
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (Cart item : selectedItems) {
            totalQuantity += item.getSoLuong();
        }
        return totalQuantity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSelectedItemName, txtSelectedItemPrice, txtTotalItems;
        ImageView img_order;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSelectedItemName = itemView.findViewById(R.id.txtSelectedItemName);
            txtSelectedItemPrice = itemView.findViewById(R.id.txtSelectedItemPrice);
            txtTotalItems = itemView.findViewById(R.id.txtTotalItems);
            img_order = itemView.findViewById(R.id.img_order);
        }
    }
}
