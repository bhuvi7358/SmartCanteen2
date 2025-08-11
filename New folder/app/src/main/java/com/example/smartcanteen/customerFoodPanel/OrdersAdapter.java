package com.example.smartcanteen.customerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;
    private OnOrderCancelListener cancelListener;

    public interface OnOrderCancelListener {
        void onCancelOrder(String orderId);
    }

    public OrdersAdapter(Context context, List<Order> orders, OnOrderCancelListener cancelListener) {
        this.context = context;
        this.orders = orders;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Set order details
        holder.orderId.setText("Order ID: " + order.getOrderId());
        holder.orderStatus.setText("Status: " + order.getStatus());
        holder.totalPrice.setText("Total Price: ₹" + order.getTotalPrice());

        // Format timestamp to a readable date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        String formattedDate = sdf.format(order.getTimestamp());
        holder.orderTimestamp.setText("Date: " + formattedDate);

        // Display items in the order
        StringBuilder itemsDescription = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsDescription.append(item.getName())
                    .append(" (x")
                    .append(item.getQuantity())
                    .append("), ₹")
                    .append(item.getPrice())
                    .append("\n");
        }
        holder.orderItems.setText(itemsDescription.toString().trim());
        // Check if the order status is "Processing"
        if ("Processing".equalsIgnoreCase(order.getStatus()) || "Completed".equalsIgnoreCase(order.getStatus())) {
            // Disable the cancel button if the status is "Processing" or "Completed"
            holder.cancelButton.setEnabled(false);
            holder.cancelButton.setText(order.getStatus()); // Display the current status
        } else {
            // Enable the cancel button if the status is not "Processing" or "Completed"
            holder.cancelButton.setEnabled(true);
            holder.cancelButton.setText("Cancel Order");
        }

        // Cancel order button
        holder.cancelButton.setOnClickListener(v -> {
            if (holder.cancelButton.isEnabled()) {
                cancelListener.onCancelOrder(order.getOrderId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderStatus, totalPrice, orderTimestamp, orderItems;
        Button cancelButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderStatus = itemView.findViewById(R.id.order_status);
            totalPrice = itemView.findViewById(R.id.total_price);
            orderTimestamp = itemView.findViewById(R.id.order_timestamp);
            orderItems = itemView.findViewById(R.id.order_items);
            cancelButton = itemView.findViewById(R.id.cancel_order_button);
        }
    }
}
