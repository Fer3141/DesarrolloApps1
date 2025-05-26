package com.apps1.cocinapp.main;

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
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.data.Receta;
import com.apps1.cocinapp.login.LoginActivity;
import com.apps1.cocinapp.receta.RecetaAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView navLogin, navAbout;
    TextView tabRecent, tabRecipes, tabCourses;
    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navLogin = findViewById(R.id.navLogin);
        navAbout = findViewById(R.id.navAbout);
        tabRecent = findViewById(R.id.tabRecent);
        tabRecipes = findViewById(R.id.tabRecipes);
        tabCourses = findViewById(R.id.tabCourses);
        searchBox = findViewById(R.id.searchBox);

        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        navAbout.setOnClickListener(v -> Toast.makeText(this, "Cocinapp versión visitante", Toast.LENGTH_SHORT).show());
        tabRecent.setOnClickListener(v -> Toast.makeText(this, "Mostrando más recientes", Toast.LENGTH_SHORT).show());
        tabRecipes.setOnClickListener(v -> Toast.makeText(this, "Mostrando recetas", Toast.LENGTH_SHORT).show());
        tabCourses.setOnClickListener(v -> Toast.makeText(this, "Mostrando cursos", Toast.LENGTH_SHORT).show());

        RecyclerView recyclerView = findViewById(R.id.recetasRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Receta> listaDeRecetas = new ArrayList<>();
        listaDeRecetas.add(new Receta("Pizza", "Roberto S.", R.drawable.sample_pizza, 4.5f));
        listaDeRecetas.add(new Receta("Albóndigas Chili con huevo", "María G.", R.drawable.sample_albondigas, 4.0f));

        RecetaAdapter adapter = new RecetaAdapter(this, listaDeRecetas);
        recyclerView.setAdapter(adapter);

        TextView navAbout = findViewById(R.id.navAbout);
        navAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

    }


}