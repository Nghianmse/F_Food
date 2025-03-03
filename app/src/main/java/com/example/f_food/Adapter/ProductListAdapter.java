/*
package com.example.f_food.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp1.Entity.Product;
import com.example.myapp1.R;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.WordViewHolder> {
    private Context context;
    private List<Product> productList;
    public ProductListAdapter(Context context, List<Product> productList){
        this.context = context;
        this.productList = productList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Product> getProductListList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout,parent, false);
        return new WordViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductId.setText(""+ product.getId());
        holder.tvProductName.setText(product.getName());
        String price = String.valueOf(product.getPrice());
        holder.tvProductPrice.setText(price);
        holder.tvProductDescription.setText(product.getDescription());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProductId;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvProductDescription;
        private TextView tvAction;
        private ProductListAdapter productListAdapter;
        public WordViewHolder(@NonNull View itemView, ProductListAdapter productListAdapter) {
            super(itemView);
            this.tvProductId = itemView.findViewById(R.id.tvProductId);
            this.tvProductName = itemView.findViewById(R.id.tvProductName);
            this.tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            this.tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            this.productListAdapter = productListAdapter;
            this.tvAction = itemView.findViewById(R.id.tvAction);
            tvAction.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Word");
            builder.setMessage("Are you sure want to delete this word?");
            builder.setPositiveButton("yes",(dialog, which) -> {
                int Position = getAdapterPosition();
                productListAdapter.productList.remove(Position);
                productListAdapter.notifyItemRemoved(Position);
            });
            builder.setNegativeButton("no", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.show();
        }
    }
}*/
