package com.foa.driver.fragment;

import android.annotation.SuppressLint;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.foa.driver.R;
import com.foa.driver.adapter.RevenueListAdapter;
import com.foa.driver.api.UserService;
import com.foa.driver.caching.StatisticMonthlyCatching;
import com.foa.driver.caching.StatisticWeeklyCatching;
import com.foa.driver.model.StatisticItem;
import com.foa.driver.model.enums.StatisticType;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.util.Helper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class StatisticFragment extends Fragment {

    private View root;
    private  BarChart dayOfWeekChart;
    private  BarChart dayOfMonthChart;
    private LottieAnimationView driverLoadingView;
    private RevenueListAdapter adapter;
    private LinearLayout detailDayStatisticCart;
    private TextView dayDetailTextView;
    private TextView numberOrdersTextView;
    private TextView totalShippingFee;
    private TextView commissionTextView;
    private TextView incomeTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_statistic, container, false);
        dayOfWeekChart = root.findViewById(R.id.dayOfWeekChart);
        dayOfMonthChart = root.findViewById(R.id.dayOfMonthChart);
        driverLoadingView = root.findViewById(R.id.driverLoadingView);
        detailDayStatisticCart = root.findViewById(R.id.detailDayStatisticCart);
        dayDetailTextView = root.findViewById(R.id.dayTextView);
        numberOrdersTextView = root.findViewById(R.id.numOrderFinishedTextView);
        totalShippingFee = root.findViewById(R.id.totalShippingFeeTextView);
        commissionTextView = root.findViewById(R.id.commissionTextView);
        incomeTextView = root.findViewById(R.id.incomeTextView);

        initDayOfWeekChart();
        initDayOfMonthChart();

        initTimeRadioGroup();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(R.id.todayRadioButton);
    }
    private void loadDetailStatistic(StatisticItem item){
        dayDetailTextView.setText("");
        numberOrdersTextView.setText(item.getNumOrderFinished());
        totalShippingFee.setText(Helper.formatMoney(item.getTotalShippingFee()));
        commissionTextView.setText(Helper.formatMoney(item.getCommission()));
        incomeTextView.setText(Helper.formatMoney(item.getIncome()));
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
        List<StatisticItem> cachingList;
        switch (radioButtonId){
            case R.id.todayRadioButton:
                cachingList =StatisticWeeklyCatching.getOrderCatching();
                if (cachingList!=null){
                    dayOfWeekChart.setData(createBarEntry(StatisticWeeklyCatching.getOrderCatching()));
                    loadDetailStatistic(cachingList.get(0));
                }else{
                    driverLoadingView.setVisibility(View.VISIBLE);
                    UserService.getStatisticWeekly((success,data) -> {
                        if (success){
                            //dayOfWeekChart.setData(createBarEntry(data));
                            StatisticWeeklyCatching.setInstance(data);
                            driverLoadingView.setVisibility(View.GONE);
                        }

                    });
                }
                dayOfWeekChart.setVisibility(View.GONE);
                dayOfMonthChart.setVisibility(View.GONE);
                break;
            case R.id.thisWeekRadioButton:
                if (StatisticWeeklyCatching.getOrderCatching()!=null){
                    dayOfWeekChart.setData(createBarEntry(StatisticWeeklyCatching.getOrderCatching()));
                }else{
                    driverLoadingView.setVisibility(View.VISIBLE);
                    UserService.getStatisticWeekly((success,data) -> {
                        if (success){
                            dayOfWeekChart.setData(createBarEntry(data));
                            StatisticWeeklyCatching.setInstance(data);
                            driverLoadingView.setVisibility(View.GONE);

                        }

                    });
                }
                XAxis xAxis = dayOfWeekChart.getXAxis();
                dayOfWeekChart.setVisibleXRangeMaximum(7);
                xAxis.setValueFormatter(new DateForFormatter());
                dayOfWeekChart.setVisibility(View.VISIBLE);
                dayOfMonthChart.setVisibility(View.GONE);
                break;
            case R.id.thisMonthRadioButton:
                if (StatisticMonthlyCatching.getOrderCatching()!=null){
                    dayOfWeekChart.setData(createBarEntry(StatisticMonthlyCatching.getOrderCatching()));
                }else{
                    driverLoadingView.setVisibility(View.VISIBLE);
                    UserService.getStatisticMonthly((success,data) -> {
                        if (success){
                            dayOfWeekChart.setData(createBarEntry(data));
                            StatisticMonthlyCatching.setInstance(data);
                            driverLoadingView.setVisibility(View.GONE);

                        }

                    });
                }
                dayOfMonthChart.setVisibleXRangeMaximum(10);
                dayOfWeekChart.setVisibility(View.GONE);
                dayOfMonthChart.setVisibility(View.VISIBLE);
                break;
        }
        dayOfWeekChart.invalidate();
    }

    private BarData createBarEntry(List<StatisticItem> statisticItems){
        List<BarEntry> entries = new ArrayList<>();
        for (int i =0; i< statisticItems.size();i++) {
            entries.add(new BarEntry(i+1, statisticItems.get(i).getIncome()));
        }
        BarDataSet barDataSet = new BarDataSet(entries,"Thu nhập");
        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new MoneyFormatter());
        return  barData;
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

        dayOfWeekChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                loadDetailStatistic(StatisticWeeklyCatching.getOrderCatching().get((int)e.getX()));
            }

            @Override
            public void onNothingSelected() {

            }
        });
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

        dayOfMonthChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                loadDetailStatistic(StatisticMonthlyCatching.getOrderCatching().get((int)e.getX()));
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    class DateForFormatter implements IAxisValueFormatter {
        String[] days = {"","Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "CN"};
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