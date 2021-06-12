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
import com.foa.driver.model.OrderItem;
import com.foa.driver.model.enums.TransactionType;
import com.foa.driver.util.Helper;

import java.util.ArrayList;
import java.util.List;


public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.ViewHolder> {

    private Context context;
    private List<OrderItem> orderItemList;

    public OrderItemListAdapter(Context context, List<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    public void setOrderItems(List<OrderItem> orderItemList){
        this.orderItemList = orderItemList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_items_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem  = orderItemList.get(position);
        holder.menuItemNameTextView.setText(orderItem.getMenuItemName());
        holder.quantityTextView.setText(String.valueOf(orderItem.getQuantity()));
        holder.subtotalTextView.setText(Helper.formatMoney(orderItem.getSubTotal()));

        final StringBuilder toppingNamesBuilder = new StringBuilder();
        orderItem.getOrderItemToppings().forEach(item->
                toppingNamesBuilder.append(item.getName()).append("\n"));
        holder.toppingListTextView.setText(toppingNamesBuilder.toString().trim());

        holder.itemView.setTag(orderItem);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView menuItemNameTextView;
        TextView  toppingListTextView;
        TextView quantityTextView;
        TextView subtotalTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menuItemNameTextView = itemView.findViewById(R.id.menuItemNameTextView);
            toppingListTextView = itemView.findViewById(R.id.toppingItemListTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            subtotalTextView = itemView.findViewById(R.id.subTotalTextView);
        }
    }
}
