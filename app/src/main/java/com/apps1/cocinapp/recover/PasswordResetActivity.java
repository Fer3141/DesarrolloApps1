package com.apps1.cocinapp.recover;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.login.LoginActivity;
import com.apps1.cocinapp.main.MainActivity;

public class PasswordResetActivity extends AppCompatActivity {

    EditText newPassword, confirmPassword;
    Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(v -> {
            String pass = newPassword.getText().toString();
            String confirm = confirmPassword.getText().toString();

            if (pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(confirm)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
