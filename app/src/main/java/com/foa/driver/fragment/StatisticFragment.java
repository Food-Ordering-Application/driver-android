package com.foa.driver.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.foa.driver.R;
import com.foa.driver.entity.StatisticItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class StatisticFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistic, container, false);
        BarChart chart = (BarChart) root.findViewById(R.id.chart);
        List<BarEntry> entries = new ArrayList<>();
        List<StatisticItem> list = StatisticItem.getSampleList();
        for (StatisticItem i:list) {
            entries.add(new BarEntry(i.getId(), i.getValue()));
        }
        BarDataSet barDataSet= new BarDataSet(entries,"Thu nhập");
        BarData barData = new BarData(barDataSet);
        chart.setData(barData);
        chart.invalidate();
        return root;
    }
}