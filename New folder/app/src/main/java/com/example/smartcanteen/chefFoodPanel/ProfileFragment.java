package com.example.smartcanteen.chefFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.smartcanteen.MainMenu;
import com.example.smartcanteen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextView nameTextView, emailTextView;
    private Button logoutButton;
    private FirebaseAuth firebaseAuth;

    public ProfileFragment() {
        super(R.layout.fragment_chef_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        profileImageView = view.findViewById(R.id.profileImageView);
        nameTextView = view.findViewById(R.id.profileName);
        emailTextView = view.findViewById(R.id.emailTextView);
        logoutButton = view.findViewById(R.id.logoutButton);

        loadUserProfile();

        logoutButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Toast.makeText(getContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainMenu.class));
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            emailTextView.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "user@example.com");

            Glide.with(this)
                    .load(currentUser.getPhotoUrl())
                    .placeholder(R.drawable.ic_profile)
                    .into(profileImageView);
        }
    }
}
