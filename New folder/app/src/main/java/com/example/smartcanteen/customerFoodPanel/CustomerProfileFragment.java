package com.example.smartcanteen.customerFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartcanteen.MainMenu;
import com.example.smartcanteen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CustomerProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private TextView nameTextView, emailTextView;
    private Button logoutButton;
    private DatabaseReference userReference;
    private String customerId;

    public CustomerProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_profile, container, false);
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_customer_profile, container, false);
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        //nameTextView = view.findViewById(R.id.name_text_view);
        emailTextView = view.findViewById(R.id.emailTextView);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Get the current user
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Set the user's display name and email
            //String name = user.getPhoneNumber();
            String email = user.getEmail();


            if (email != null && !email.isEmpty()) {
                emailTextView.setText(email);
            } else {
                emailTextView.setText("No email available");
            }
        } else {
            // If the user is not logged in, show a Toast message
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

        // Logout button functionality
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Redirect to login screen (You can specify your login activity here)
            Intent intent = new Intent(getContext(), MainMenu.class);
            startActivity(intent);
            getActivity().finish(); // Close the current activity
        });
        return view;
    }
}