package com.foa.driver.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.foa.driver.R;
import com.foa.driver.adapter.ViewPagerAdapter;
import com.foa.driver.model.enums.OrderStatusQuery;
import com.google.android.material.tabs.TabLayout;

public class OrdersFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListView theListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        theListView = root.findViewById(R.id.mainListView);
        viewPager = root.findViewById(R.id.viewpager);
        tabLayout = root.findViewById(R.id.tabs);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter((getActivity().getSupportFragmentManager()));
        adapter.addFragment(new OrderListFragment(OrderStatusQuery.ACTIVE), "Đang thực hiện");
        adapter.addFragment(new OrderListFragment(OrderStatusQuery.COMPLETED), "Đã hoàn tất");

        viewPager.setAdapter(adapter);
    }
}