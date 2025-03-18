package com.example.f_food.repository;

import android.content.Context;

import com.example.f_food.dao.PaymentDAO;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Payment;

import java.util.Arrays;
import java.util.List;

public class PaymentRepository {
    private PaymentDAO paymentDAO;

    public PaymentRepository(Context context) {
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        paymentDAO = db.paymentDAO();

        // Check if there is no data, then insert sample data
        if (paymentDAO.getAllPayments().isEmpty()) {
            insertSampleData();
        }
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

    public Payment getPaymentById(int id) {
        return paymentDAO.getPaymentById(id);
    }

    public List<Payment> getPaymentsByOrderId(int orderId) {
        return paymentDAO.getPaymentsByOrderId(orderId);
    }

    public void insert(Payment payment) {
        paymentDAO.insert(payment);
    }

    public void insertAll(List<Payment> payments) {
        paymentDAO.insertAll(payments);
    }

    public void update(Payment payment) {
        paymentDAO.update(payment);
    }

    public void delete(Payment payment) {
        paymentDAO.delete(payment);
    }

    public void deleteById(int id) {
        paymentDAO.deleteById(id);
    }

    public void deleteAll() {
        paymentDAO.deleteAll();
    }

    private void insertSampleData() {
        List<Payment> samplePayments = Arrays.asList(
                new Payment(1, 1001,"bank transfer" , "Pending"),
                new Payment(2, 1002, "bank transfer", "Pending"),
                new Payment(3, 1003, "Cash", "Pending")
        );

        for (Payment payment : samplePayments) {
            paymentDAO.insert(payment);
        }
    }
}
