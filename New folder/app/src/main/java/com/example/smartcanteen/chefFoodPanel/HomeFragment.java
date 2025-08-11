package com.example.smartcanteen.chefFoodPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView totalItems, pendingOrders;
    private RecyclerView recyclerViewFoodItems;
    private DatabaseReference databaseReference;
    private FoodItemAdapter foodItemAdapter;
    private List<FoodItem> foodItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        totalItems = view.findViewById(R.id.total_items);
        pendingOrders = view.findViewById(R.id.pending_orders);
        recyclerViewFoodItems = view.findViewById(R.id.recycler_view_food_items);

        foodItemList = new ArrayList<>();
        foodItemAdapter = new FoodItemAdapter(getContext(), foodItemList);
        recyclerViewFoodItems.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFoodItems.setAdapter(foodItemAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Fetch food items from Firebase
        databaseReference.child("FoodItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodItemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FoodItem foodItem = dataSnapshot.getValue(FoodItem.class);
                    if (foodItem != null) {
                        foodItemList.add(foodItem);
                    }
                }
                foodItemAdapter.notifyDataSetChanged();
                totalItems.setText("Items Available: " + foodItemList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Fetch pending orders from Firebase
        databaseReference.child("Orders").orderByChild("status").equalTo("Pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingOrders.setText("Pending Orders: " + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }
}
