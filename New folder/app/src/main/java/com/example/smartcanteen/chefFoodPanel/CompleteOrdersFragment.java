package com.example.smartcanteen.chefFoodPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class CompleteOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private CompleteOrdersAdapter adapter;
    private List<Order> processingOrdersList;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_orders, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_complete_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        processingOrdersList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");

        adapter = new CompleteOrdersAdapter(getContext(), processingOrdersList);
        recyclerView.setAdapter(adapter);

        // Fetch "Processing" orders
        fetchProcessingOrders();

        return view;
    }

    private void fetchProcessingOrders() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                processingOrdersList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) { // Iterate over user IDs
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) { // Iterate over orders
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null && "Processing".equals(order.getStatus())) {
                            order.setOrderId(orderSnapshot.getKey()); // Set order ID
                            order.setCustomerId(userSnapshot.getKey()); // Set customer ID
                            processingOrdersList.add(order);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load processing orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
