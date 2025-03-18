package com.example.f_food.adapter;

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

import com.example.f_food.entity.Policy;
import com.example.f_food.R;

import java.util.List;

public class PolicyManagementAdapter extends RecyclerView.Adapter<PolicyManagementAdapter.ViewHolder> {
    private List<Policy> policyList;
    private Context context;
    public PolicyManagementAdapter(List<Policy> policyList,Context context) {
        this.policyList = policyList;
        this.context=context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, createdAtTextView;
        Button btnDelete;


        public ViewHolder(View view) {
            super(view);
            titleTextView = itemView.findViewById(R.id.txtPolicy_Management_Title);
            descriptionTextView = itemView.findViewById(R.id.txtPolicy_Management_Description);
            createdAtTextView = itemView.findViewById(R.id.txtPolicy_Management_Createat);
            btnDelete= itemView.findViewById(R.id.btnManagement_Delete_Policy);
        }
    }
    @NonNull
    @Override
    public PolicyManagementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_policy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyManagementAdapter.ViewHolder holder, int position) {
        Policy policy = policyList.get(position);
        holder.titleTextView.setText("Title: " + policy.getTitle());
        holder.descriptionTextView.setText("Description: " + policy.getDescription());
        holder.createdAtTextView.setText("Created At: " + policy.getCreatedAt());
        holder.btnDelete.setOnClickListener(v -> {
            // Hiển thị hộp thoại xác nhận trước khi xóa
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (position >= 0 && position < policyList.size()) {
                            policyList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, policyList.size());
                            Toast.makeText(context, "Deleted SUCCESS" , Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Đóng hộp thoại nếu chọn "No"
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return policyList.size();
    }


}
