package com.apps1.cocinapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
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
    RecetaAdapter adapter;

    List<Receta> recetasRecientes = new ArrayList<>();
    List<Receta> recetasPopulares = new ArrayList<>();
    List<Receta> cursos = new ArrayList<>();

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

        navAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Datos de ejemplo
        recetasRecientes.add(new Receta("Pizza", "Roberto S.", R.drawable.sample_pizza, 4.5f));
        recetasRecientes.add(new Receta("Empanadas", "Clara R.", R.drawable.sample_empanadas, 3.5f));

        recetasPopulares.add(new Receta("Tarta de choclo", "Luis M.", R.drawable.sample_tarta, 5.0f));
        recetasPopulares.add(new Receta("Fideos al pesto", "Laura J.", R.drawable.sample_fideos, 4.8f));

        cursos.add(new Receta("Curso Pan Casero", "Chef Emma", R.drawable.sample_curso_pan, 4.7f));
        cursos.add(new Receta("Curso Pastas", "Chef Mateo", R.drawable.sample_curso_pastas, 4.9f));

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recetasRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecetaAdapter(this, recetasRecientes); // por defecto: recientes
        recyclerView.setAdapter(adapter);

        // Tabs
        tabRecent.setOnClickListener(v -> {
            adapter.actualizarLista(recetasRecientes);
            Toast.makeText(this, "Mostrando más recientes", Toast.LENGTH_SHORT).show();
        });

        tabRecipes.setOnClickListener(v -> {
            adapter.actualizarLista(recetasPopulares);
            Toast.makeText(this, "Mostrando recetas populares", Toast.LENGTH_SHORT).show();
        });

        tabCourses.setOnClickListener(v -> {
            adapter.actualizarLista(cursos);
            Toast.makeText(this, "Mostrando cursos", Toast.LENGTH_SHORT).show();
        });
    }
}
