package com.foa.driver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foa.driver.R;
import com.foa.driver.model.Order;
import com.foa.driver.util.Helper;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends BaseAdapter {

    private Context context;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private List<Order> orderList;

    public FoldingCellListAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public FoldingCellListAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
    }

    public void setOrders(List<Order> orderList){
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Order order = orderList.get(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(context);
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.shippingFee = cell.findViewById(R.id.shippingFeeTextView);
            viewHolder.grandTotal = cell.findViewById(R.id.grandTotalTextView);
            viewHolder.deliveredDate = cell.findViewById(R.id.deliveryDateTextView);
            viewHolder.fromAddress = cell.findViewById(R.id.restaurantAddressTextView);
            viewHolder.toAddress = cell.findViewById(R.id.customerAddressTextView);
            viewHolder.distance = cell.findViewById(R.id.deliveryDistanceTextView);

            viewHolder.shippingFee2 = cell.findViewById(R.id.shippingFeeTextView2);
            viewHolder.grandTotal2 = cell.findViewById(R.id.grandTotalTextView2);
            viewHolder.deliveredDate2 = cell.findViewById(R.id.deliveryDateTextView2);
            viewHolder.fromAddress2 = cell.findViewById(R.id.restaurantAddressTextView2);
            viewHolder.toAddress2 = cell.findViewById(R.id.customerAddressTextView2);
            viewHolder.distance2 = cell.findViewById(R.id.deliveryDistanceTextView2);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == order)
            return cell;

        // bind data from selected element to view through view holder
        viewHolder.shippingFee.setText(Helper.formatMoney(order.getDelivery().getShippingFee()));
        viewHolder.grandTotal.setText(Helper.formatMoney(order.getGrandTotal()));
        viewHolder.deliveredDate.setText(order.getDelivery().getDeliveredAt());
        viewHolder.fromAddress.setText(order.getDelivery().getCustomerAddress());
        viewHolder.toAddress.setText(order.getDelivery().getRestaurantAddress());
        viewHolder.distance.setText(Helper.formatDistance(order.getDelivery().getDistance()));

        viewHolder.shippingFee2.setText(Helper.formatMoney(order.getDelivery().getShippingFee()));
        viewHolder.grandTotal2.setText(Helper.formatMoney(order.getGrandTotal()));
        viewHolder.deliveredDate2.setText(order.getDelivery().getDeliveredAt());
        viewHolder.fromAddress2.setText(order.getDelivery().getCustomerAddress());
        viewHolder.toAddress2.setText(order.getDelivery().getRestaurantAddress());
        viewHolder.distance2.setText(Helper.formatDistance(order.getDelivery().getDistance()));

        return cell;
    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView shippingFee;
        TextView fromAddress;
        TextView toAddress;
        TextView distance;
        TextView deliveredDate;
        TextView grandTotal;
        TextView shippingFee2;
        TextView fromAddress2;
        TextView toAddress2;
        TextView distance2;
        TextView deliveredDate2;
        TextView grandTotal2;
    }
}