package com.example.f_food.Screen.admin_management;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Adapter.PolicyManagementAdapter;
import com.example.f_food.Entity.Policy;
import com.example.f_food.R;
import com.example.f_food.Repository.PolicyRepository;

import java.util.List;

public class Policy_Management extends AppCompatActivity {
private RecyclerView recyclerView;
private List<Policy> policyList;
private PolicyRepository policyRepository;
private PolicyManagementAdapter policyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_policy_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerViewPolicies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Init();
        policyAdapter = new PolicyManagementAdapter(policyList);
        recyclerView.setAdapter(policyAdapter);
    }
    public void Init(){
        policyRepository= new PolicyRepository(this);
        policyList=policyRepository.getAllPolicies();
    }
}