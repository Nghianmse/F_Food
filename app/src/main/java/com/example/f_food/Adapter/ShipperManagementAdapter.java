package com.example.f_food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Entity.Shipper;
import com.example.f_food.Entity.User;
import com.example.f_food.R;
import com.example.f_food.Repository.ShipperRepository;
import com.example.f_food.Repository.UserRepository;

import java.util.List;

public class ShipperManagementAdapter extends RecyclerView.Adapter<ShipperManagementAdapter.ViewHolder> {
    private List<Shipper> shipperList;
    private Context context;
    private ShipperRepository shipperRepository;
    private UserRepository userRepository;

    public ShipperManagementAdapter(Context context, List<Shipper> shipperList) {
        this.context = context;
        this.shipperList = shipperList;
        this.shipperRepository = new ShipperRepository(context);
        this.userRepository = new UserRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipper_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shipper shipper = shipperList.get(position);

        // Lấy thông tin User từ bảng users bằng UserID
        User user = userRepository.getUserById(shipper.getUserId());

        // Hiển thị thông tin shipper
        holder.txtShipperId.setText("ShipperID: " + shipper.getShipperId());
        holder.txtShipperName.setText("Name: " + (user != null ? user.getFullName() : "Unknown"));
        holder.txtShipperEmail.setText("Email: " + (user != null ? user.getEmail() : "Unknown"));
        holder.txtShipperPhone.setText("Phone: " + (user != null ? user.getPhone() : "Unknown"));
        holder.txtShipperCreatedAt.setText("Created At: " + (user != null ? user.getCreatedAt() : "Unknown"));

        // Xóa shipper
        holder.btnDelete.setOnClickListener(v -> {
            shipperRepository.deleteById(shipper.getShipperId());
            shipperList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, shipperList.size());
            Toast.makeText(context, "Deleted shipper " + shipper.getShipperId(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return shipperList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtShipperId, txtShipperName, txtShipperEmail, txtShipperPhone, txtShipperCreatedAt;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShipperId = itemView.findViewById(R.id.txt_Management_ShipperId);
            txtShipperName = itemView.findViewById(R.id.txtManagement_Shipper_Name);
            txtShipperEmail = itemView.findViewById(R.id.txtManagementShipperEmail);
            txtShipperPhone = itemView.findViewById(R.id.txtManagement_Shipper_Phone);
            txtShipperCreatedAt = itemView.findViewById(R.id.txtManagement_ShipperCreatedAt);
            btnDelete = itemView.findViewById(R.id.btnManagement_Delete_Shipper);
        }
    }
}
