package com.example.smartcanteen.chefFoodPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.R;
import com.example.smartcanteen.customerFoodPanel.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private PendingOrdersAdapter adapter;
    private List<Order> pendingOrdersList;
    private DatabaseReference ordersReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pending_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_pending_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pendingOrdersList = new ArrayList<>();
        adapter = new PendingOrdersAdapter(getContext(), pendingOrdersList, this::updateOrderStatus);
        recyclerView.setAdapter(adapter);

        ordersReference = FirebaseDatabase.getInstance().getReference("Orders");

        fetchPendingOrders();
    }

    private void fetchPendingOrders() {
        ordersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingOrdersList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) { // Iterate over user IDs
                        for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) { // Iterate over orders
                            Order order = orderSnapshot.getValue(Order.class);
                            if (order != null) {
                                order.setOrderId(orderSnapshot.getKey()); // Set the order key
                                order.setCustomerId(userSnapshot.getKey());  // Set the user ID
                                if ("Placed".equals(order.getStatus()))
                                {
                                    pendingOrdersList.add(order);
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No pending orders found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load orders: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateOrderStatus(String userId, String orderId, String newStatus) {
        ordersReference.child(userId).child(orderId).child("status").setValue(newStatus)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Order status updated to " + newStatus, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update status: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
