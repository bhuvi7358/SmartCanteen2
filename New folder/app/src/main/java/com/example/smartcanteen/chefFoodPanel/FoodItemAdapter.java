package com.example.smartcanteen.chefFoodPanel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartcanteen.R;

import java.util.List;
public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {
    private Context context;
    private List<FoodItem> foodItems;

    public FoodItemAdapter(Context context, List<FoodItem> foodItems) {
        this.context = context;
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);

        holder.foodName.setText(foodItem.getName());
        holder.foodQuantity.setText("Quantity: " + foodItem.getQuantity());
        holder.foodPrice.setText("Price: Rs." + foodItem.getPrice());

        // Load the image using Glide or Picasso
        //Glide.with(context).load(foodItem.getImageUrl()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    static class FoodItemViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodQuantity, foodPrice;
        ImageView foodImage;

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodQuantity = itemView.findViewById(R.id.food_quantity);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodImage = itemView.findViewById(R.id.food_image);
        }
    }
}

