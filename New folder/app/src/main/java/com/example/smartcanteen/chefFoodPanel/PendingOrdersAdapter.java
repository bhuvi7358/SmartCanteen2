package com.example.smartcanteen.chefFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.R;
import com.example.smartcanteen.customerFoodPanel.Order;
import com.example.smartcanteen.customerFoodPanel.OrderItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.ViewHolder> {

    private final Context context;
    private final List<Order> pendingOrdersList;
    private final OrderStatusUpdateListener statusUpdateListener;
    private final FirebaseFirestore db;

    // Interface to handle status updates
    public interface OrderStatusUpdateListener {
        void updateOrderStatus(String userId, String orderId, String newStatus);
    }

    public PendingOrdersAdapter(Context context, List<Order> pendingOrdersList, OrderStatusUpdateListener statusUpdateListener) {
        this.context = context;
        this.pendingOrdersList = pendingOrdersList;
        this.statusUpdateListener = statusUpdateListener;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pending_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = pendingOrdersList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        String formattedDate = sdf.format(order.getTimestamp());

        holder.orderIdTextView.setText("Order ID: " + order.getOrderId());
        holder.totalPriceTextView.setText("Total Price: ₹" + order.getTotalPrice());
        holder.timestampTextView.setText("Date: " + formattedDate);

        StringBuilder itemsDescription = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsDescription.append(item.getName())
                    .append(" (x")
                    .append(item.getQuantity())
                    .append("), ₹")
                    .append(item.getPrice())
                    .append("\n");
        }
        holder.itemfood.setText(itemsDescription.toString().trim());

        // Button to update order status to "Processing"
        holder.processOrderButton.setOnClickListener(v -> {
            // Update order status to "Processing"
            statusUpdateListener.updateOrderStatus(order.getCustomerId(), order.getOrderId(), "Processing");
        });

        // Button to update order status to "Cancelled"
        holder.cancelOrderButton.setOnClickListener(v -> {
            statusUpdateListener.updateOrderStatus(order.getCustomerId(), order.getOrderId(), "Cancelled");
        });
    }

    @Override
    public int getItemCount() {
        return pendingOrdersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, totalPriceTextView, timestampTextView, itemfood;
        Button processOrderButton, cancelOrderButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.text_order_id);
            itemfood = itemView.findViewById(R.id.text_item);
            totalPriceTextView = itemView.findViewById(R.id.text_total_price);
            timestampTextView = itemView.findViewById(R.id.text_timestamp);
            processOrderButton = itemView.findViewById(R.id.button_process_order);
            cancelOrderButton = itemView.findViewById(R.id.button_cancel_order);
        }
    }
}
