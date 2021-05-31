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
import com.foa.driver.entity.Item;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

public class OrderListFragment extends Fragment {
    View root;
    ListView theListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order_list, container, false);
        theListView = root.findViewById(R.id.mainListView);
        final ArrayList<Item> items = Item.getTestingList();

        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(getActivity(), items);

        adapter.setDefaultRequestBtnClickListener(v -> Toast.makeText(getActivity(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show());

        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener((adapterView, view, pos, l) -> {
            ((FoldingCell) view).toggle(false);
            adapter.registerToggle(pos);
        });
        return root;
    }
}