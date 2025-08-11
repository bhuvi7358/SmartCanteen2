package com.example.smartcanteen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.smartcanteen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Random;

public class OtpSend extends AppCompatActivity {
    Button sendotp;
    int randomNumber;
    String _txtPhone;
    private FirebaseAuth FAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_send);
        sendotp=(Button)findViewById(R.id.Verify);
        FAuth = FirebaseAuth.getInstance();
      _txtPhone= Objects.requireNonNull(getIntent().getStringExtra("phonenum")).trim();
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
// Construct data
                    String apiKey = "apikey=" + "NGY1NTU3Njc2YzQ0NTk0MTczNTM2NjZmNmU2YzZlNzU=";
                    Random random= new Random();
                    randomNumber=random.nextInt(999999);
                    String message = "&amp;message=" + "Hey, Your OTP IS "+randomNumber;
                    String sender = "&amp;sender=" + "Bhuvi";
                    String numbers = "&amp;numbers=" + _txtPhone.trim();

// Send data
                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
                    String data = apiKey + numbers + message + sender;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    final StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    rd.close();
                    Toast.makeText(getApplicationContext(), "OTP SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();

//return stringBuffer.toString();
                } catch (Exception e) {
//System.out.println("Error SMS "+e);
///return "Error "+e;
                    Toast.makeText(getApplicationContext(), "ERROR SMS "+e, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "ERROR "+e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}