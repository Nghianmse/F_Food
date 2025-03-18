package com.example.f_food.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.entity.Order;
import com.example.f_food.entity.Payment;
import com.example.f_food.entity.User;
import com.example.f_food.R;
import com.example.f_food.repository.OrderRepository;
import com.example.f_food.repository.UserRepository;

import java.util.List;

public class PaymentManagementAdapter extends RecyclerView.Adapter<PaymentManagementAdapter.PaymentViewHolder> {
    private List<Payment> paymentList;
    private OrderRepository orderRepository;
private  UserRepository userRepository;
    // Modify constructor to accept OrderRepository
    public PaymentManagementAdapter(List<Payment> paymentList, OrderRepository orderRepository,UserRepository userRepository) {
        this.paymentList = paymentList;
        this.userRepository=userRepository;
        this.orderRepository = orderRepository;  // Initialize the repository here
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_transaction_management, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = paymentList.get(position);

        // Retrieve the Order using the payment's orderId
        Order order = orderRepository.getOrderById(payment.getOrderId());
        if (order != null) {
            // Retrieve the User by order
            User user = userRepository.getUserById(order.getUserId()); // Assuming Order has userId
            if (user != null) {
                // Set the fullName from the User object
                holder.txtFullName.setText(user.getFullName());
            } else {
                holder.txtFullName.setText("User Not Found");
            }

            // Set the payment details
            holder.txtPaymentId.setText(String.valueOf(payment.getPaymentId()));
            holder.txtOrderId.setText(String.valueOf(payment.getOrderId()));
            holder.txtAmount.setText("$" + payment.getAmount());

            // Set the order creation date
            holder.txtDate.setText(order.getCreatedAt());
        } else {
            // If Order is not found
            holder.txtFullName.setText("Order Not Found");
            holder.txtPaymentId.setText(String.valueOf(payment.getPaymentId()));
            holder.txtOrderId.setText(String.valueOf(payment.getOrderId()));
            holder.txtAmount.setText("$" + payment.getAmount());
            holder.txtDate.setText("Date: N/A");
        }
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public void updateList(List<Payment> newList) {
        paymentList = newList;
        notifyDataSetChanged();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView txtPaymentId,txtFullName, txtOrderId, txtAmount, txtDate;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFullName=itemView.findViewById(R.id.txtPaymentManagement_full_name);
            txtPaymentId = itemView.findViewById(R.id.txtPaymentManagement_id);
            txtOrderId = itemView.findViewById(R.id.txtPaymentManagement_order_id);
            txtAmount = itemView.findViewById(R.id.txtPaymentManagement_amount);
            txtDate = itemView.findViewById(R.id.txtPaymentManagement_create_at);
        }
    }
}
