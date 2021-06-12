package com.foa.driver.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
    private LottieAnimationView driverLoadingView;
    private OrderStatusQuery type;
    private boolean isFirst=false;

    public OrderListFragment(OrderStatusQuery type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order_list, container, false);
        theListView = root.findViewById(R.id.mainListView);
        driverLoadingView = root.findViewById(R.id.driverLoadingView);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst){
            driverLoadingView.setVisibility(View.VISIBLE);
            OrderService.getAllOrder(LoginSession.getInstance().getDriver().getId(), OrderStatusQuery.COMPLETED.name(), 1,25,null, null, (success, data) -> {
                if (success){
                    driverLoadingView.setVisibility(View.GONE);
                    if (type== OrderStatusQuery.ACTIVE && data.size()>0){
                        List<Order> list = new ArrayList<>();
                        list.add(data.get(0));
                        adapter = new FoldingCellListAdapter(getActivity(),type);
                        adapter.setOrders(list);
                        theListView.setAdapter(adapter);
                        try {
                            Thread.sleep(200);
                            adapter.registerToggle(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{

                        adapter = new FoldingCellListAdapter(getActivity(),type);
                        adapter.setOrders(data);
                        theListView.setAdapter(adapter);
                        theListView.setOnItemClickListener((adapterView, view, pos, l) -> {
                            ((FoldingCell) view).toggle(false);
                            adapter.registerToggle(pos);
                        });
                    }
                    isFirst =true;
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter!=null)
        adapter.clearUnfoldedIndexes();
    }
}