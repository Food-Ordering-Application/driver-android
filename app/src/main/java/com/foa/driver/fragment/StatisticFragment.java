package com.foa.driver.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foa.driver.R;
import com.foa.driver.adapter.RevenueListAdapter;
import com.foa.driver.entity.StatisticItem;
import com.foa.driver.model.enums.StatisticType;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class StatisticFragment extends Fragment {

    private View root;
    private  BarChart dayOfWeekChart;
    private  BarChart dayOfMonthChart;
    private BarDataSet barDataSet;
    private RecyclerView revenueRecyclerView;
    private RevenueListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_statistic, container, false);
        dayOfWeekChart = root.findViewById(R.id.dayOfWeekChart);
        dayOfMonthChart = root.findViewById(R.id.dayOfMonthChart);

        initRevenueRecycler();

        initDayOfWeekChart();
        initDayOfMonthChart();

        loadData(R.id.todayRadioButton);

        initTimeRadioGroup();
        return root;
    }

    private void initRevenueRecycler(){
        revenueRecyclerView = root.findViewById(R.id.revenueRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        revenueRecyclerView.setLayoutManager(layoutManager);
        adapter = new RevenueListAdapter(getActivity());
        revenueRecyclerView.setAdapter(adapter);
    }

    @SuppressLint("NonConstantResourceId")
    private void initTimeRadioGroup(){
        RadioGroup radioGroup = root.findViewById(R.id.timeRadioGroup);
        radioGroup.setOnCheckedChangeListener((rd, i) -> {
           switch (rd.getCheckedRadioButtonId()){
               case R.id.todayRadioButton:
                   loadData(R.id.todayRadioButton);
                   break;
               case R.id.thisWeekRadioButton:
                   loadData(R.id.thisWeekRadioButton);
                   break;
               case R.id.thisMonthRadioButton:
                   loadData(R.id.thisMonthRadioButton);
                   break;

           }
        });
    }
    private void loadData(int radioButtonId){
        List<BarEntry> entries = new ArrayList<>();
        List<StatisticItem> list = StatisticItem.getSampleList(radioButtonId);
        for (StatisticItem i:list) {
            entries.add(new BarEntry(i.getId(), i.getValue()));
        }
        BarDataSet barDataSet = new BarDataSet(entries,"Thu nhập");
        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new MoneyFormatter());
        switch (radioButtonId){
            case R.id.todayRadioButton:
                adapter.setData(list,StatisticType.DAY);
                dayOfWeekChart.setVisibility(View.GONE);
                dayOfMonthChart.setVisibility(View.GONE);
                break;
            case R.id.thisWeekRadioButton:
                adapter.setData(list,StatisticType.WEEK);
                dayOfWeekChart.setData(barData);
                XAxis xAxis = dayOfWeekChart.getXAxis();
                barDataSet= new BarDataSet(new ArrayList<>(),"Thu nhập");
                dayOfWeekChart.setVisibleXRangeMaximum(7);
                xAxis.setValueFormatter(new DateForFormatter());
                dayOfWeekChart.setVisibility(View.VISIBLE);
                dayOfMonthChart.setVisibility(View.GONE);
                break;
            case R.id.thisMonthRadioButton:
                adapter.setData(list, StatisticType.MONTH);
                dayOfMonthChart.setData(barData);
                dayOfMonthChart.setVisibleXRangeMaximum(10);
                dayOfWeekChart.setVisibility(View.GONE);
                dayOfMonthChart.setVisibility(View.VISIBLE);
                break;
        }
        dayOfWeekChart.invalidate();
    }

    private void initDayOfWeekChart(){
        dayOfWeekChart.setVisibility(View.GONE);
        dayOfWeekChart.getXAxis().setDrawGridLines(false);
        dayOfWeekChart.setBackgroundColor(getResources().getColor(R.color.bgContentTop));
        dayOfWeekChart.setDrawGridBackground(false);
        XAxis xAxis = dayOfWeekChart.getXAxis();
        xAxis.setDrawGridLines(true);
        dayOfWeekChart.getAxisLeft().setDrawGridLines(false);
        dayOfWeekChart.getAxisLeft().setEnabled(false);
        dayOfWeekChart.getAxisRight().setDrawGridLines(false);
        dayOfWeekChart.getAxisRight().setEnabled(false);
        dayOfWeekChart.getXAxis().setDrawGridLines(false);
        dayOfWeekChart.getLegend().setEnabled(false);
        dayOfWeekChart.getAxisRight().setDrawLabels(false);
        dayOfWeekChart.getAxisLeft().setDrawLabels(true);
        dayOfWeekChart.setTouchEnabled(true);
        dayOfWeekChart.setDoubleTapToZoomEnabled(false);
        dayOfWeekChart.getXAxis().setEnabled(true);
        dayOfWeekChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        dayOfWeekChart.setPinchZoom(false);
        dayOfWeekChart.setDrawBarShadow(false);
        dayOfWeekChart.setDrawGridBackground(false);
        dayOfWeekChart.setOverScrollMode(View.SCROLL_AXIS_HORIZONTAL);
        dayOfWeekChart.getDescription().setEnabled(false);
        dayOfWeekChart.setScaleEnabled(false);
        dayOfWeekChart.setVisibleXRangeMaximum(7);
        dayOfWeekChart.animateY(1500);
        dayOfWeekChart.invalidate();
    }

    private void initDayOfMonthChart(){
        dayOfMonthChart.setVisibility(View.GONE);
        dayOfMonthChart.getXAxis().setDrawGridLines(false);
        dayOfMonthChart.setBackgroundColor(getResources().getColor(R.color.bgContentTop));
        dayOfMonthChart.setDrawGridBackground(false);
        XAxis xAxis = dayOfMonthChart.getXAxis();
        xAxis.setDrawGridLines(true);
        dayOfMonthChart.getAxisLeft().setDrawGridLines(false);
        dayOfMonthChart.getAxisLeft().setEnabled(false);
        dayOfMonthChart.getAxisRight().setDrawGridLines(false);
        dayOfMonthChart.getAxisRight().setEnabled(false);
        dayOfMonthChart.getXAxis().setDrawGridLines(false);
        dayOfMonthChart.getLegend().setEnabled(false);
        dayOfMonthChart.getAxisRight().setDrawLabels(false);
        dayOfMonthChart.getAxisLeft().setDrawLabels(true);
        dayOfMonthChart.setTouchEnabled(true);
        dayOfMonthChart.setDoubleTapToZoomEnabled(false);
        dayOfMonthChart.getXAxis().setEnabled(true);
        dayOfMonthChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        dayOfMonthChart.setPinchZoom(false);
        dayOfMonthChart.setDrawBarShadow(false);
        dayOfMonthChart.setDrawGridBackground(false);
        dayOfMonthChart.setOverScrollMode(View.SCROLL_AXIS_HORIZONTAL);
        dayOfMonthChart.getDescription().setEnabled(false);
        dayOfMonthChart.setScaleEnabled(false);
        dayOfMonthChart.setVisibleXRangeMaximum(7);
        dayOfMonthChart.animateY(1500);
        dayOfMonthChart.invalidate();
    }

    class DateForFormatter implements IAxisValueFormatter {
        String[] days = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "CN"};
        private boolean isDate;
        public DateForFormatter(){}

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return isDate?  days[(int)value] : String.valueOf((int)value);
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }


    class MoneyFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

            return (int)value/1000 + "K";
        }
    }



}