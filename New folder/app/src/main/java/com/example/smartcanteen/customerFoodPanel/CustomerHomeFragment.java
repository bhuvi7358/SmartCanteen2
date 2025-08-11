package com.example.smartcanteen.customerFoodPanel;
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
import com.example.smartcanteen.chefFoodPanel.FoodItem;
import com.example.smartcanteen.customerFoodPanel.FoodItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<FoodItem> foodItems;
    private FoodItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_home, container, false);

        recyclerView = view.findViewById(R.id.food_items_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodItems = new ArrayList<>();
        adapter = new FoodItemAdapter(getContext(), foodItems);
        recyclerView.setAdapter(adapter);

        // Fetch food items from Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodItems");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodItems.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FoodItem item = dataSnapshot.getValue(FoodItem.class);
                    if (item != null) {
                        foodItems.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load items.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Method to add item to cart (this will be triggered when user clicks on an item)
    public void addItemToCart(FoodItem foodItem) {
        // Save the item to Firebase under the user's cart
        String userId = "example_user_id";  // Use the actual user ID
        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child("Cart")
                .push();  // Push a unique ID for each item added to the cart
        cartRef.setValue(foodItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Item added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to add item to cart.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
