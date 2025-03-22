package com.example.f_food.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.f_food.fragment.PaidOrdersFragment;
import com.example.f_food.fragment.UnpaidOrdersFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new UnpaidOrdersFragment();
        else return new PaidOrdersFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
