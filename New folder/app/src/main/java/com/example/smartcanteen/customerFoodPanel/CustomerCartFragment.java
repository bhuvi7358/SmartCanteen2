package com.example.smartcanteen.customerFoodPanel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.HashMap;

public class CustomerCartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartItemAdapter adapter;
    private TextView totalPriceText;
    private Button placeOrderButton;
    private ArrayList<CartItem> cartItemList;
    private DatabaseReference cartReference, orderReference;
    private String customerId;

    private static final String TAG = "CustomerCartFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_cart, container, false);

        // Get the current logged-in user ID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            customerId = mAuth.getCurrentUser().getUid(); // Get the dynamic user ID
        } else {
            Toast.makeText(getContext(), "User is not logged in", Toast.LENGTH_SHORT).show();
            customerId = ""; // Handle this case differently if needed
        }

        // Initialize RecyclerView and Adapter
        recyclerView = view.findViewById(R.id.cart_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItemList = new ArrayList<>();
        adapter = new CartItemAdapter(getContext(), cartItemList);
        recyclerView.setAdapter(adapter);

        // Set up Firebase reference to the user's cart and orders
        cartReference = FirebaseDatabase.getInstance().getReference("Cart").child(customerId);
        orderReference = FirebaseDatabase.getInstance().getReference("Orders").child(customerId);

        fetchCartItems();

        // Initialize UI elements
        totalPriceText = view.findViewById(R.id.total_price_text);
        placeOrderButton = view.findViewById(R.id.place_order_button);

        // Button listener for placing an order
        placeOrderButton.setOnClickListener(v -> placeOrder());

        return view;
    }

    private void fetchCartItems() {
        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear();
                double totalPrice = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItemList.add(cartItem);
                        totalPrice += cartItem.getTotalPrice();
                    }
                }
                totalPriceText.setText("Total Price: ₹" + totalPrice);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load cart items.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void placeOrder() {
        if (cartItemList.isEmpty()) {
            Toast.makeText(getContext(), "Cart is empty. Add items to place an order.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create order data
        String orderId = orderReference.push().getKey();
        if (orderId != null) {
            HashMap<String, Object> orderData = new HashMap<>();
            orderData.put("items", cartItemList);
            orderData.put("totalPrice", calculateTotalPrice());
            orderData.put("status", "Placed");
            orderData.put("timestamp", System.currentTimeMillis());

            // Save order data to Firebase
            orderReference.child(orderId).setValue(orderData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Clear the cart after placing the order
                            cartReference.removeValue();
                            Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to place the order. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Failed to generate order ID.", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateTotalPrice() {
        double totalPrice = 0;
        for (CartItem cartItem : cartItemList) {
            totalPrice += cartItem.getTotalPrice();
        }
        return totalPrice;
    }
}
