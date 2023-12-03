package com.longthph30891.ungdungdatdouong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.interfaces.CartInterface;
import com.longthph30891.ungdungdatdouong.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    Context context;
    List<Cart> cartList;
    CartInterface cartInterface;


    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
//        this.cartInterface = cartInterface;
    }

    public void cartClick(CartInterface cartInterface) {
        this.cartInterface = cartInterface;
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.layout_cart, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.txt_cart_name_product.setText(cart.getProductName());

        Locale vn = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(vn);
        String price = format.format(cart.getProductPrice());
        holder.txt_cart_price_product.setText(price);

        Picasso.get().load(cart.getProductImage()).into(holder.imgCartProduct);

        holder.txt_cart_count_product.setText(cart.getSoLuong() + "");

        holder.img_cong.setOnClickListener(v -> {
            if (cartInterface != null) {
                cartInterface.onIncreaseClick(position);
            }
        });

        holder.img_tru.setOnClickListener(v -> {
            if (cartInterface != null) {
                cartInterface.onDecreaseClick(position);
            }
        });

        holder.checkBox_cart.setOnCheckedChangeListener((v, isChecked) -> {
            if (cartInterface != null) {
                cartInterface.checkItem(position, isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCartProduct, img_cong, img_tru;
        TextView txt_cart_name_product, txt_cart_count_product, txt_cart_price_product;
        CheckBox checkBox_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_cart_name_product = itemView.findViewById(R.id.txtProductName);
            txt_cart_count_product = itemView.findViewById(R.id.txtQuantity);
            txt_cart_price_product = itemView.findViewById(R.id.txtProductPrice);
            imgCartProduct = itemView.findViewById(R.id.img_cart_product);
            img_cong = itemView.findViewById(R.id.imgIncrease);
            img_tru = itemView.findViewById(R.id.imgDecrease);
            checkBox_cart = itemView.findViewById(R.id.checkBoxCart);
        }
    }
}
