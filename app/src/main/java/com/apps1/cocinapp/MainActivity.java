package com.apps1.cocinapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView navLogin, navAbout;
    TextView tabRecent, tabRecipes, tabCourses;
    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            drawerLayout = findViewById(R.id.drawer_layout);
            navLogin = findViewById(R.id.navLogin);
            navAbout = findViewById(R.id.navAbout);
            tabRecent = findViewById(R.id.tabRecent);
            tabRecipes = findViewById(R.id.tabRecipes);
            tabCourses = findViewById(R.id.tabCourses);
            searchBox = findViewById(R.id.searchBox);

            ImageButton menuButton = findViewById(R.id.menuButton);
            menuButton.setOnClickListener(v -> drawerLayout.openDrawer(findViewById(R.id.navView)));

            navLogin.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });

            navAbout.setOnClickListener(v -> Toast.makeText(this, "Cocinapp versión visitante", Toast.LENGTH_SHORT).show());

            tabRecent.setOnClickListener(v -> Toast.makeText(this, "Mostrando más recientes", Toast.LENGTH_SHORT).show());
            tabRecipes.setOnClickListener(v -> Toast.makeText(this, "Mostrando recetas", Toast.LENGTH_SHORT).show());
            tabCourses.setOnClickListener(v -> Toast.makeText(this, "Mostrando cursos", Toast.LENGTH_SHORT).show());

        } catch (Exception e) {
            Log.e("MainActivity", "Error inicializando vistas", e);
        }
    }
}
