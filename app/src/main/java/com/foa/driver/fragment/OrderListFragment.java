package com.foa.driver.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.foa.driver.R;
import com.foa.driver.adapter.FoldingCellListAdapter;
import com.foa.driver.api.OrderService;
import com.foa.driver.entity.Item;
import com.foa.driver.model.Order;
import com.foa.driver.model.enums.DeliveryStatus;
import com.foa.driver.model.enums.OrderStatusQuery;
import com.foa.driver.network.IDataResultCallback;
import com.foa.driver.session.LoginSession;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {
    private View root;
    private ListView theListView;
    private FoldingCellListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order_list, container, false);
        theListView = root.findViewById(R.id.mainListView);

        adapter = new FoldingCellListAdapter(getActivity());
        theListView.setAdapter(adapter);
        theListView.setOnItemClickListener((adapterView, view, pos, l) -> {
            ((FoldingCell) view).toggle(false);
            adapter.registerToggle(pos);
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        OrderService.getAllOrder(LoginSession.getInstance().getDriver().getId(), OrderStatusQuery.COMPLETED.name(), 1,25,null, null, (success, data) -> {
            if (success){
                adapter.setOrders(data);
            }
        });
    }
}