package com.longthph30891.ungdungdatdouong.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.interfaces.ProductInterface;
import com.longthph30891.ungdungdatdouong.model.Khachang;
import com.longthph30891.ungdungdatdouong.model.Product;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<Product> productList;

    ProductInterface productInterface;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }


    public void clickProduct(ProductInterface productInterface) {
        this.productInterface = productInterface;
    }


    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_product, null);
        return new ProductViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtNameProduct.setText(product.getTenDoUong());

        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String price = numberFormat.format(product.getGia());
        holder.txtPriceProduct.setText(price);

        switch (product.getTrangThai()) {
            case "Moi":
                holder.img_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_new));
                break;
            case "DangBan":
                holder.img_sold_out_product.setVisibility(View.GONE);
                holder.img_status.setVisibility(View.GONE);
                break;
            case "HetHang":
                holder.img_sold_out_product.setImageDrawable(context.getResources().getDrawable(R.drawable.sold_out));
                float alphaValue = 0.4f;
                holder.imgProduct.setAlpha(alphaValue);
                holder.img_status.setVisibility(View.GONE);
                break;
        }


        Picasso.get().load(product.getImage()).into(holder.imgProduct, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Log.e("Error", e.getMessage());
            }
        });


        holder.layout.setOnClickListener(v -> {
            productInterface.clickProduct(product);
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void seachDatalist(ArrayList<Product> sacrhList){
        productList = sacrhList;
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameProduct, txtPriceProduct;
        ImageView imgProduct, img_status, img_sold_out_product;
        ProgressBar progressBar;

        RelativeLayout layout;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.txt_name_product);
            txtPriceProduct = itemView.findViewById(R.id.txt_price_product);
            imgProduct = itemView.findViewById(R.id.img_product);
            progressBar = itemView.findViewById(R.id.progress_product);
            layout = itemView.findViewById(R.id.item_product);
            img_status = itemView.findViewById(R.id.img_status_product);
            img_sold_out_product = itemView.findViewById(R.id.img_sold_out_product);
        }
    }
}
