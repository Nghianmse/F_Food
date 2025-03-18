package com.example.f_food.repository;

import android.content.Context;
import com.example.f_food.dao.PolicyDAO;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Policy;
import java.util.Arrays;
import java.util.List;

public class PolicyRepository {
    private PolicyDAO policyDAO;

    public PolicyRepository(Context context) {
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        policyDAO = db.policyDAO();

        // Kiểm tra nếu chưa có dữ liệu, thì thêm dữ liệu mẫu
        if (policyDAO.getAllPolicies().isEmpty()) {
            insertSampleData();
        }
    }

    public List<Policy> getAllPolicies() {
        return policyDAO.getAllPolicies();
    }

    public Policy getPolicyById(int id) {
        return policyDAO.getPolicyById(id);
    }

    public void deleteById(int id) {
        policyDAO.deleteById(id);
    }



    public void insert(Policy policy) {
        policyDAO.insert(policy);
    }

    public void insertAll(List<Policy> policyList) {
        policyDAO.insertAll(policyList);
    }

    public void update(Policy policy) {
        policyDAO.update(policy);
    }

    private void insertSampleData() {
        List<Policy> samplePolicies = Arrays.asList(
                new Policy(1, "Privacy Policy", "Details about privacy", "2024-03-05"),
                new Policy(2, "Terms of Service", "Details about terms", "2024-03-04"),
                new Policy(3, "Refund Policy", "Details about refunds", "2024-03-03")
        );

        for (Policy policy : samplePolicies) {
            policyDAO.insert(policy);
        }
    }
}