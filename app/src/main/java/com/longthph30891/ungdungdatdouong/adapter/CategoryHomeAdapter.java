package com.longthph30891.ungdungdatdouong.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.interfaces.CategoryInterface;
import com.longthph30891.ungdungdatdouong.model.Category;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryHomeAdapter extends RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder> {

    Context context;
    List<Category> categoryList;

    CategoryInterface categoryInterface;

    public CategoryHomeAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public void clickCategory(CategoryInterface categoryInterface) {
        this.categoryInterface = categoryInterface;
    }

    @NonNull
    @Override
    public CategoryHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_category_home, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHomeAdapter.ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txt_name.setText(category.getTypeName());
        Picasso.get().load(category.getTypeImage()).into(holder.img_category, new Callback() {
            @Override
            public void onSuccess() {
                holder.progress.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Log.e("Error", e.getMessage());
            }
        });

        holder.cardView.setOnClickListener(view -> {
            categoryInterface.clickCategory(category);
        });
    }

    @Override
    public int getItemCount() {
        if (categoryList == null) {
            return View.inflate(context, R.layout.item_placeholder, null).getVisibility();
        }
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        ImageView img_category;
        ProgressBar progress;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name =  itemView.findViewById(R.id.txt_name_category);
            img_category = itemView.findViewById(R.id.img_category);
            progress = itemView.findViewById(R.id.progress);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
