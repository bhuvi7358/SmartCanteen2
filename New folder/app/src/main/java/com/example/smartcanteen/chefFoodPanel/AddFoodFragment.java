package com.example.smartcanteen.chefFoodPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.smartcanteen.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddFoodFragment extends Fragment {

    private EditText foodName, foodQuantity, foodPrice;
    private Button addFoodButton, updateFoodButton,deleteFoodButton, dailyReportButton, monthlyReportButton;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_food, container, false);

        foodName = view.findViewById(R.id.food_name);
        foodQuantity = view.findViewById(R.id.food_quantity);
        foodPrice = view.findViewById(R.id.food_price);
        addFoodButton = view.findViewById(R.id.add_food_button);
        updateFoodButton = view.findViewById(R.id.update_food_button);
        deleteFoodButton = view.findViewById(R.id.delete_food_button);

        databaseReference = FirebaseDatabase.getInstance().getReference("FoodItems");

        // Add food item to the database
        addFoodButton.setOnClickListener(v -> {
            String name = foodName.getText().toString();
            String quantityStr = foodQuantity.getText().toString();
            String priceStr = foodPrice.getText().toString();

            if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    double price = Double.parseDouble(priceStr);

                    if (quantity <= 0 || price <= 0) {
                        Toast.makeText(getActivity(), "Quantity and price must be greater than 0", Toast.LENGTH_SHORT).show();
                    } else {
                        String id = databaseReference.push().getKey();
                        FoodItem foodItem = new FoodItem(id, name, quantity, price);
                        databaseReference.child(id).setValue(foodItem);

                        Toast.makeText(getActivity(), "Food item added successfully", Toast.LENGTH_SHORT).show();
                        foodName.setText("");
                        foodQuantity.setText("");
                        foodPrice.setText("");
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Please enter valid numbers for quantity and price", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Update food item in the database
        updateFoodButton.setOnClickListener(v -> {
            String name = foodName.getText().toString();
            String quantityStr = foodQuantity.getText().toString();
            String priceStr = foodPrice.getText().toString();

            if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    double price = Double.parseDouble(priceStr);

                    if (quantity <= 0 || price <= 0) {
                        Toast.makeText(getActivity(), "Quantity and price must be greater than 0", Toast.LENGTH_SHORT).show();
                    } else {
                        databaseReference.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        snapshot.getRef().child("quantity").setValue(quantity);
                                        snapshot.getRef().child("price").setValue(price);
                                        Toast.makeText(getActivity(), "Food item updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Food item not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getActivity(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Please enter valid numbers for quantity and price", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Delete food item from the database
        deleteFoodButton.setOnClickListener(v -> {
            String name = foodName.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter the food name", Toast.LENGTH_SHORT).show();
            } else {
                databaseReference.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue();
                                Toast.makeText(getActivity(), "Food item deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Food item not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }
}
