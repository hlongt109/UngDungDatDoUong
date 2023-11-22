package com.longthph30891.ungdungdatdouong.adapter;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.interfaces.ProductInterface;
import com.longthph30891.ungdungdatdouong.model.Product;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

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

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtNameProduct.setText(product.getTenDoUong());
        holder.txtPriceProduct.setText(product.getGia());


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

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameProduct, txtPriceProduct;
        ImageView imgProduct;
        ProgressBar progressBar;

        RelativeLayout layout;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.txt_name_product);
            txtPriceProduct = itemView.findViewById(R.id.txt_price_product);
            imgProduct = itemView.findViewById(R.id.img_product);
            progressBar = itemView.findViewById(R.id.progress_product);
            layout = itemView.findViewById(R.id.item_product);
        }
    }
}
