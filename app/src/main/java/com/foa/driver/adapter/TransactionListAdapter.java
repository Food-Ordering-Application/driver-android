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
import com.foa.driver.model.DriverTransaction;
import com.foa.driver.model.enums.TransactionType;
import com.foa.driver.util.Helper;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;


public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {

    private Context context;
    private List<DriverTransaction> transactionList;

    public TransactionListAdapter(Context context, List<DriverTransaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DriverTransaction transaction  = transactionList.get(position);

        //set data
        if (transaction.getType()== TransactionType.PAYIN){
            holder.transactionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_money_green));
            holder.transactionSign.setText("+");
            holder.transactionType.setText("Nạp tiền");
        }else{
            holder.transactionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_money_red));
            holder.transactionSign.setText("-");
            holder.transactionType.setText("Rút tiền");

        }

        holder.transactionAmount.setText(Helper.formatMoney(transaction.getAmount()));

        holder.transactionTime.setText(Helper.getTimeFormUTC(transaction.getCreatedAt()).getFull());


        holder.itemView.setTag(transaction);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView transactionIcon;
        TextView  transactionAmount;
        TextView transactionType;
        TextView transactionTime;
        TextView  transactionSign;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionIcon = itemView.findViewById(R.id.transactionIcon);
             transactionAmount = itemView.findViewById(R.id.transactionAmount);
             transactionType = itemView.findViewById(R.id.transactionType);
             transactionTime = itemView.findViewById(R.id.transactionTime);
             transactionSign = itemView.findViewById(R.id.transactionSign);
        }
    }
}
