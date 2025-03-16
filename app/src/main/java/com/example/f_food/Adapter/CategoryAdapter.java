package com.example.f_food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.f_food.Entity.Category;
import com.example.f_food.R;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private final LayoutInflater inflater;
    private final List<Category> categoryList;
    private final int resource;

    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<Category> categoryList) {
        super(context, resource, categoryList);
        this.inflater = LayoutInflater.from(context);
        this.resource = resource;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }
        TextView tvCategoryName = (TextView) convertView;
        Category category = categoryList.get(position);
        tvCategoryName.setText(category.getName());
        return convertView;
    }
}
