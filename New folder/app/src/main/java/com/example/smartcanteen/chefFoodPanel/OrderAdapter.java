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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Set order details
        holder.orderId.setText("Order ID: " + order.getOrderId());
        holder.orderDetails.setText("Items: " + order.getItemsDescription());
        holder.totalPrice.setText("Total Price: ₹" + order.getTotalPrice());
        holder.orderStatus.setText("Status: " + order.getStatus());

        // Update status button
        holder.updateStatusButton.setOnClickListener(v -> {
            String newStatus = "Preparing"; // Example status to be updated
            updateOrderStatus(order.getOrderId(), newStatus, holder);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private void updateOrderStatus(String orderId, String newStatus, OrderViewHolder holder) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance()
                .getReference("Orders")
                .child(orderId);

        orderRef.child("status").setValue(newStatus)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Order status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                    holder.orderStatus.setText("Status: " + newStatus);
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update status.", Toast.LENGTH_SHORT).show());
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDetails, totalPrice, orderStatus;
        Button updateStatusButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            //orderId = itemView.findViewById(R.id.order_id);
            orderDetails = itemView.findViewById(R.id.order_details);
            totalPrice = itemView.findViewById(R.id.total_price);
            orderStatus = itemView.findViewById(R.id.order_status);
            //updateStatusButton = itemView.findViewById(R.id.update_status_button);
        }
    }
}
