package com.example.smartcanteen.customerFoodPanel;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerOrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private ArrayList<Order> ordersList;
    private OrdersAdapter ordersAdapter;

    public CustomerOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        ordersRecyclerView = view.findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the order list and adapter
        ordersList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(getContext(), ordersList, this::cancelOrder);
        ordersRecyclerView.setAdapter(ordersAdapter);

        // Get Firebase reference for current user orders
        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ordersReference = FirebaseDatabase.getInstance().getReference("Orders").child(customerId);

        // Fetch orders from Firebase
        fetchOrders(ordersReference);
    }

    private void fetchOrders(DatabaseReference ordersReference) {
        ordersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        // Assign the order ID (Firebase key)
                        order.setOrderId(orderSnapshot.getKey());
                        ordersList.add(order);
                    }
                }
                // Notify the adapter of data changes
                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Display an error message
                Toast.makeText(getContext(), "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelOrder(String orderId) {
        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(customerId).child(orderId);
        orderRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Order cancelled successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to cancel order", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
