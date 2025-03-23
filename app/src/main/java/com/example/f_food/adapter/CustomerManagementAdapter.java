package com.example.f_food.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.entity.User;
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

        // Kiểm tra trạng thái của isDeleted và cập nhật giao diện
        if (customer.getIsDelete()) {
            holder.tvFullName.setText("Banned: " + customer.getFullName());
        } else {
            holder.tvFullName.setText("FullName: " + customer.getFullName());
        }

        // Cập nhật trạng thái của nút Ban (Ban/Active)
        if (customer.getIsDelete()) {
            holder.btnban.setText("Active");
            holder.btnban.setTextColor(Color.GREEN); // Text màu xanh khi đã bị banned
        } else {
            holder.btnban.setText("Ban");
            holder.btnban.setTextColor(Color.RED); // Text màu đỏ khi chưa bị banned
        }

        holder.btnban.setOnClickListener(v -> {
            // Hiển thị hộp thoại xác nhận trước khi thay đổi trạng thái
            new AlertDialog.Builder(context)
                    .setTitle("Confirm ")
                    .setMessage("Are you sure you want to  "+customer.getIsDelete().toString() + customer.getFullName() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Thay đổi trạng thái isDeleted
                        if (position >= 0 && position < customerList.size()) {
                            customer.setIsDelete(!customer.getIsDelete()); // Toggle trạng thái
                            notifyItemChanged(position); // Cập nhật lại giao diện cho phần tử đó
                            Toast.makeText(context, customer.getIsDelete() ? "Banned: " + customer.getFullName() : "Activated: " + customer.getFullName(), Toast.LENGTH_SHORT).show();
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
        Button btnban;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserID = itemView.findViewById(R.id.txtUser_Management_Id);
            tvFullName = itemView.findViewById(R.id.txtUser_Management_Name);
            tvEmail = itemView.findViewById(R.id.txtUser_Management_Email);
            tvPhone = itemView.findViewById(R.id.txtUser_Management_Phone);
            tvCreateAt = itemView.findViewById(R.id.txtUser_Management_CreateAt);
            btnban = itemView.findViewById(R.id.btnUser_Management_Delete);
        }
    }
}