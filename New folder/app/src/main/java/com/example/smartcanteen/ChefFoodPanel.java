package com.example.smartcanteen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartcanteen.chefFoodPanel.AddFoodFragment;
import com.example.smartcanteen.chefFoodPanel.ChefOrderFragment;
import com.example.smartcanteen.chefFoodPanel.CompleteOrdersFragment;
import com.example.smartcanteen.chefFoodPanel.HomeFragment;
import com.example.smartcanteen.chefFoodPanel.PendingOrdersFragment;
import com.example.smartcanteen.chefFoodPanel.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChefFoodPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_food_panel);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_add_food:
                    selectedFragment = new AddFoodFragment();
                    break;
                case R.id.nav_pending_orders:
                    selectedFragment = new PendingOrdersFragment();
                    break;
                case R.id.nav_complete_orders:
                    selectedFragment = new CompleteOrdersFragment();
                    break;
                case R.id.Profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            if (selectedFragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.commit();
            }
            return true;
        });

        // Set default fragment
        if (savedInstanceState == null) {
           bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }
}
