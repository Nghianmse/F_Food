package com.example.f_food.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Policies")
public class Policy {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "policy_id")
    private int policyId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "created_at")
    private String createdAt;



    public Policy(int id,String title, String description, String createdAt) {
        this.policyId=id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Policy() {
    }

    public int getPolicyId() {
        return policyId;
    }

    public void setPolicyId(int policyId) {
        this.policyId = policyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
