package com.example.f_food.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Entity.User;
import com.example.f_food.R;

import java.util.List;

public class CustomerManagementAdapter extends RecyclerView.Adapter<CustomerManagementAdapter.ViewHolder> {
    private Context context;
    private List<User> customerList;

    public CustomerManagementAdapter(Context context, List<User> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer_management, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User customer = customerList.get(position);
        holder.tvUserID.setText(String.valueOf(customer.getUserId()));
        holder.tvFullName.setText("FullName: " + customer.getFullName());
        holder.tvEmail.setText("EMAIL: " + customer.getEmail());
        holder.tvPhone.setText("Phone: " + customer.getPhone());
        holder.tvCreateAt.setText("Create_at: " + customer.getCreatedAt());

        holder.btnDelete.setOnClickListener(v -> {
            // Hiển thị hộp thoại xác nhận trước khi xóa
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete " + customer.getFullName() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (position >= 0 && position < customerList.size()) {
                            customerList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, customerList.size());
                            Toast.makeText(context, "Deleted: " + customer.getFullName(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Đóng hộp thoại nếu chọn "No"
                    .show();
        });
    }


    @Override
    public int getItemCount() {
        return customerList != null ? customerList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserID, tvFullName, tvEmail, tvPhone, tvCreateAt;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserID = itemView.findViewById(R.id.txtUser_Management_Id);
            tvFullName = itemView.findViewById(R.id.txtUser_Management_Name);
            tvEmail = itemView.findViewById(R.id.txtUser_Management_Email);
            tvPhone = itemView.findViewById(R.id.txtUser_Management_Phone);
            tvCreateAt = itemView.findViewById(R.id.txtUser_Management_CreateAt);
            btnDelete = itemView.findViewById(R.id.btnUser_Management_Delete);
        }
    }
}
