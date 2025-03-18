package com.example.f_food.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.entity.Policy;
import com.example.f_food.R;

import java.util.List;

public class PolicyManagementAdapter extends RecyclerView.Adapter<PolicyManagementAdapter.ViewHolder> {
    private List<Policy> policyList;
    public PolicyManagementAdapter(List<Policy> policyList) {
        this.policyList = policyList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, createdAtTextView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = itemView.findViewById(R.id.txtPolicy_Management_Title);
            descriptionTextView = itemView.findViewById(R.id.txtPolicy_Management_Description);
            createdAtTextView = itemView.findViewById(R.id.txtPolicy_Management_Createat);
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
    }

    @Override
    public int getItemCount() {
        return policyList.size();
    }


}
