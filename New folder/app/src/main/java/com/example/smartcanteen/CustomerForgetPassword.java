package com.example.smartcanteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerForgetPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton,backbutton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextInputLayout emailInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_forget_password);

        emailInputLayout = findViewById(R.id.emailInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        backbutton=findViewById(R.id.backbutton);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginemail  = new Intent(CustomerForgetPassword.this,Login.class);
                startActivity(loginemail);
                finish();
            }
        });
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailInputLayout.setError("Enter your registered email");
            return;
        } else {
            emailInputLayout.setError(null);
        }

        resetPasswordButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    resetPasswordButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        Toast.makeText(CustomerForgetPassword.this, "Password reset email sent!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CustomerForgetPassword.this, "Failed to send reset email", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
