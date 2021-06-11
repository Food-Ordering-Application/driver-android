package com.foa.driver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foa.driver.R;
import com.foa.driver.entity.StatisticItem;
import com.foa.driver.model.DriverTransaction;
import com.foa.driver.model.enums.StatisticType;
import com.foa.driver.model.enums.TransactionType;
import com.foa.driver.util.Helper;

import java.util.List;


public class RevenueListAdapter extends RecyclerView.Adapter<RevenueListAdapter.ViewHolder> {

    private Context context;
    private List<StatisticItem> revenueList;
    private StatisticType type;

    public RevenueListAdapter(Context context, List<StatisticItem> revenueList) {
        this.context = context;
        this.revenueList = revenueList;
    }
    public RevenueListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<StatisticItem> revenueList, StatisticType type){
        this.revenueList = revenueList;
        this.type = type;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.revenue_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StatisticItem item  = revenueList.get(position);

        holder.revenueTime.setText(String.valueOf(item.getDay()));
        holder.revenueAmount.setText(Helper.formatMoney((long)item.getValue()));
        switch (type){
            case DAY:
                holder.dayTextView.setText("");
                break;
            case WEEK:
                holder.dayTextView.setText("Thứ ");
                break;
            case MONTH:
                holder.dayTextView.setText("Ngày ");
                break;
        }

        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return revenueList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView revenueAmount;
        TextView revenueTime;
        TextView dayTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             revenueAmount = itemView.findViewById(R.id.revenueAmount);
             revenueTime = itemView.findViewById(R.id.revenueTime);
            dayTextView = itemView.findViewById(R.id.dayTextView);
        }
    }
}
