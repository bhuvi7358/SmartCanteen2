package com.example.smartcanteen.customerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import com.example.smartcanteen.R;
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private Context context;
    private ArrayList<CartItem> cartList;
    private DatabaseReference cartReference;

    public CartItemAdapter(Context context, ArrayList<CartItem> cartList) {
        this.context = context;
        this.cartList = cartList;

        // Get the current logged-in user ID
        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartReference = FirebaseDatabase.getInstance().getReference("Cart").child(customerId);
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = cartList.get(position);
        holder.cartItemName.setText(cartItem.getName());
        holder.cartItemPrice.setText("₹" + cartItem.getPrice());
        holder.cartItemQuantity.setText("Quantity: " + cartItem.getQuantity());

        // Increase quantity
        holder.increaseQuantityButton.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() + 1;
            updateQuantity(cartItem, newQuantity);
        });

        // Decrease quantity
        holder.decreaseQuantityButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                int newQuantity = cartItem.getQuantity() - 1;
                updateQuantity(cartItem, newQuantity);
            } else {
                Toast.makeText(context, "Quantity can't be less than 1", Toast.LENGTH_SHORT).show();
            }
        });

        // Remove from cart
        holder.removeFromCartButton.setOnClickListener(v -> {
            cartReference.child(cartItem.getName()).removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show());
        });
    }

    private void updateQuantity(CartItem cartItem, int newQuantity) {
        cartItem.setQuantity(newQuantity);
        cartItem.setTotalPrice(newQuantity * Double.parseDouble(cartItem.getPrice()));  // Update total price

        // Update the Firebase cart item
        cartReference.child(cartItem.getName()).setValue(cartItem)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Quantity updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView cartItemName, cartItemPrice, cartItemQuantity;
        ImageButton increaseQuantityButton, decreaseQuantityButton, removeFromCartButton;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemName = itemView.findViewById(R.id.cart_item_name);
            cartItemPrice = itemView.findViewById(R.id.cart_item_price);
            cartItemQuantity = itemView.findViewById(R.id.cart_item_quantity);
            increaseQuantityButton = itemView.findViewById(R.id.increase_quantity_button);
            decreaseQuantityButton = itemView.findViewById(R.id.decrease_quantity_button);
            removeFromCartButton = itemView.findViewById(R.id.remove_from_cart_button);
        }
    }
}
