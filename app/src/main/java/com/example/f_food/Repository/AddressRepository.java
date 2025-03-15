package com.example.f_food.Repository;

import android.content.Context;

import com.example.f_food.DAO.AddressDAO;
import com.example.f_food.DAO.RestaurantRoomDatabase;
import com.example.f_food.Entity.Address;

import java.util.List;

public class AddressRepository {
    private AddressDAO addressDAO;

    public AddressRepository(Context context) {
        // Lấy instance của database Room
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        addressDAO = db.addressDAO();

        // Kiểm tra nếu chưa có dữ liệu, thì thêm dữ liệu mẫu
        if (getAllAddresses().isEmpty()) {
            addSampleData();
        }
    }

    // Lấy tất cả địa chỉ
    public List<Address> getAllAddresses() {
        return addressDAO.getAllAddresses();
    }

    // Lấy địa chỉ theo ID người dùng
    public List<Address> getAddressesByUserId(int userId) {
        return addressDAO.getAddressesByUserId(userId);
    }

    // Xóa địa chỉ
    public void delete(Address address) {
        addressDAO.delete(address);
    }

    // Cập nhật địa chỉ
    public void update(Address address) {
        addressDAO.update(address);
    }

    // Xóa địa chỉ theo ID
    public void deleteById(int id) {
        addressDAO.deleteById(id);
    }

    // Thêm địa chỉ mới
    public void insert(Address address) {
        addressDAO.insert(address);
    }

    // Thêm dữ liệu mẫu
    private void addSampleData() {
        // Tạo các địa chỉ mẫu
        Address sampleAddress1 = new Address(1, "123 Main St", "Apt 4B", true, "Home", 21.0285, 105.8542);
        Address sampleAddress2 = new Address(1, "456 Oak Ave", "Apt 3A", false, "Work", 21.0299, 105.8556);
        Address sampleAddress3 = new Address(1, "789 Pine Rd", "Unit 2", false, "Other", 21.0300, 105.8560);

        // Thêm các địa chỉ vào database
        insert(sampleAddress1);
        insert(sampleAddress2);
        insert(sampleAddress3);
    }
}
