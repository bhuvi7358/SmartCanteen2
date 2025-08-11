package com.example.smartcanteen.customerFoodPanel;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import com.example.smartcanteen.chefFoodPanel.FoodItem;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodViewHolder> {

    private Context context;
    private ArrayList<FoodItem> foodList;
    private DatabaseReference cartReference;
    private String customerId;

    public FoodItemAdapter(Context context, ArrayList<FoodItem> foodList) {
        this.context = context;
        this.foodList = foodList;

        // Get the current logged-in user ID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            customerId = mAuth.getCurrentUser().getUid();  // Get the dynamic user ID
        } else {
            // Handle the case where the user is not logged in
            Toast.makeText(context, "User is not logged in", Toast.LENGTH_SHORT).show();
            customerId = "";  // Optionally set to an empty string or handle this case differently
        }

        // Reference to the cart in the Firebase database for this user
        cartReference = FirebaseDatabase.getInstance().getReference("Cart").child(customerId);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_item_layout, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);
        holder.foodName.setText(foodItem.getName());
        holder.foodPrice.setText("Price: ₹" + foodItem.getPrice());
        holder.foodQuantity.setText("Available: " + foodItem.getQuantity());

        holder.addToCartButton.setOnClickListener(v -> {
            if (customerId.isEmpty()) {
                Toast.makeText(context, "Please log in to add to cart", Toast.LENGTH_SHORT).show();
                return;
            }

            CartItem cartItem = new CartItem(
                    foodItem.getName(),
                    String.valueOf(foodItem.getPrice()),
                    1, // Default quantity is 1
                    foodItem.getPrice()
            );

            cartReference.child(foodItem.getName()).setValue(cartItem)
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice, foodQuantity;
        Button addToCartButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodQuantity = itemView.findViewById(R.id.food_quantity);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}
