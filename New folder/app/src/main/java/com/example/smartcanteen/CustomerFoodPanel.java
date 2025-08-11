package com.example.smartcanteen;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.smartcanteen.customerFoodPanel.CustomerCartFragment;
import com.example.smartcanteen.customerFoodPanel.CustomerHomeFragment;
import com.example.smartcanteen.customerFoodPanel.CustomerOrdersFragment;
import com.example.smartcanteen.customerFoodPanel.CustomerProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerFoodPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_panel);

        // Set up the BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // Load the default fragment (HomeFragment) when the app starts
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CustomerHomeFragment())
                    .commit();
        }
    }

    private final BottomNavigationView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                // Determine which fragment to display based on the selected menu item
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new CustomerHomeFragment();
                        break;
                    case R.id.nav_cart:
                        selectedFragment = new CustomerCartFragment();
                        break;
                    case R.id.nav_orders:
                        selectedFragment = new CustomerOrdersFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new CustomerProfileFragment();
                        break;
                }

                // Replace the current fragment with the selected fragment
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            };
   
}
