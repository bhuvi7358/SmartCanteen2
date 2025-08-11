package com.example.smartcanteen.chefFoodPanel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.R;
import com.example.smartcanteen.customerFoodPanel.Order;
import com.example.smartcanteen.customerFoodPanel.OrderItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CompleteOrdersAdapter extends RecyclerView.Adapter<CompleteOrdersAdapter.ViewHolder> {

    private final Context context;
    private final List<Order> processingOrdersList;
    private final DatabaseReference customersReference;

    public CompleteOrdersAdapter(Context context, List<Order> processingOrdersList) {
        this.context = context;
        this.processingOrdersList = processingOrdersList;
        this.customersReference = FirebaseDatabase.getInstance().getReference("Customer");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complete_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = processingOrdersList.get(position);

        holder.orderIdTextView.setText("Order ID: " + order.getOrderId());
        holder.customerIdTextView.setText("Customer ID: " + order.getCustomerId());
        holder.statusTextView.setText("Status: " + order.getStatus());

        StringBuilder itemsDescription = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsDescription.append(item.getName())
                    .append(" (x")
                    .append(item.getQuantity())
                    .append("), ₹")
                    .append(item.getPrice())
                    .append("\n");
        }
        holder.foodname.setText(itemsDescription.toString().trim());

        // Button to mark order as "Completed"
        holder.completeOrderButton.setOnClickListener(v -> {
            updateOrderStatus(order.getCustomerId(), order.getOrderId(), "Completed");
            fetchCustomerPhoneNumber(order.getCustomerId(), order.getOrderId());
        });
    }

    @Override
    public int getItemCount() {
        return processingOrdersList.size();
    }

    // Update order status in Firebase
    private void updateOrderStatus(String userId, String orderId, String newStatus) {
        DatabaseReference ordersReference = FirebaseDatabase.getInstance().getReference("Orders");
        ordersReference.child(userId).child(orderId).child("status").setValue(newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Order marked as Completed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                });
    }

    // Fetch customer's phone number from Firebase Realtime Database
    private void fetchCustomerPhoneNumber(String customerId, String orderId) {
        customersReference.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String customerPhoneNumber = snapshot.child("Mobile No").getValue(String.class);
                    if (customerPhoneNumber != null && !customerPhoneNumber.isEmpty()) {
                        sendSms(customerPhoneNumber, orderId);
                    } else {
                        Toast.makeText(context, "Customer phone number not found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Customer document does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error fetching phone number: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Send SMS notification to the customer
    private void sendSms(String phoneNumber, String orderId) {
        String message = "Your order with ID " + orderId + " is now completed. Thank you for choosing us!";

        // Check SMS permission before sending
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
            Toast.makeText(context, "SMS permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(context, "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to send SMS", Toast.LENGTH_SHORT).show();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, customerIdTextView, statusTextView, foodname;
        Button completeOrderButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.text_order_id);
            customerIdTextView = itemView.findViewById(R.id.text_customer_id);
            foodname = itemView.findViewById(R.id.food_name);
            statusTextView = itemView.findViewById(R.id.text_status);
            completeOrderButton = itemView.findViewById(R.id.button_complete_order);
        }
    }
}