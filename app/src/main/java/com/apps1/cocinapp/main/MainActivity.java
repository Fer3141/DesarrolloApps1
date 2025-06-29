package com.apps1.cocinapp.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.apps1.cocinapp.recetas.CrearRecetaActivity;
import com.apps1.cocinapp.session.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import com.apps1.cocinapp.usuario.PerfilActivity;
import com.apps1.cocinapp.utils.JwtUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;
public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView navLogin, navLogout, navAbout, tabRecent, tabRecipes, tabCourses, mensajeSinConexion, visitorMessage;
    EditText searchBox;
    RecetaAdapter adapter;
    RecyclerView recyclerView;

    List<Receta> recetasRecientes = new ArrayList<>();
    List<Receta> recetasPopulares = new ArrayList<>();
    List<Receta> cursos = new ArrayList<>();

    BottomNavigationView bottomNav;

    private boolean estaLogueado;
    LinearLayout menuOpciones;

    Button btnCrearReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navLogin = findViewById(R.id.navLogin);
        navLogout = findViewById(R.id.navLogout);
        navAbout = findViewById(R.id.navAbout);
        tabRecent = findViewById(R.id.tabRecent);
        tabRecipes = findViewById(R.id.tabRecipes);
        tabCourses = findViewById(R.id.tabCourses);
        searchBox = findViewById(R.id.searchBox);
        mensajeSinConexion = findViewById(R.id.mensajeSinConexion);
        visitorMessage = findViewById(R.id.visitorMessage);
        recyclerView = findViewById(R.id.recetasRecyclerView);
        bottomNav = findViewById(R.id.bottomNavigation);
        menuOpciones = findViewById(R.id.menuOpciones);
        btnCrearReceta = findViewById(R.id.btnCrearReceta);

        estaLogueado = false;
        SharedPreferences prefs = getSharedPreferences("miapp", MODE_PRIVATE);


        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Mostrar u ocultar elementos según sesión
        if (SharedPreferencesHelper.hayToken(this)) {
            navLogin.setVisibility(View.GONE);
            navLogout.setVisibility(View.VISIBLE);
            visitorMessage.setVisibility(View.GONE);
        } else {
            navLogin.setVisibility(View.VISIBLE);
            navLogout.setVisibility(View.GONE);
            visitorMessage.setVisibility(View.VISIBLE);
        }

        if (!hayInternet()) {
            mensajeSinConexion.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            mensajeSinConexion.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        // Datos de ejemplo
        recetasRecientes.add(new Receta("Pizza", "Roberto S.", R.drawable.sample_pizza, 4.5f));
        recetasRecientes.add(new Receta("Empanadas", "Clara R.", R.drawable.sample_empanadas, 3.5f));
        recetasPopulares.add(new Receta("Tarta de choclo", "Luis M.", R.drawable.sample_tarta, 5.0f));
        recetasPopulares.add(new Receta("Fideos al pesto", "Laura J.", R.drawable.sample_fideos, 4.8f));
        cursos.add(new Receta("Curso Pan Casero", "Chef Emma", R.drawable.sample_curso_pan, 4.7f));
        cursos.add(new Receta("Curso Pastas", "Chef Mateo", R.drawable.sample_curso_pastas, 4.9f));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecetaAdapter(this, recetasRecientes);
        recyclerView.setAdapter(adapter);

        navLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        navAbout.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        navLogout.setOnClickListener(v -> {
            SharedPreferencesHelper.eliminarToken(this);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

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

        TextView userWelcome = findViewById(R.id.userWelcome);
        String token = SharedPreferencesHelper.obtenerToken(this);

        if (token != null && !token.isEmpty()) {
            estaLogueado = true;
            JSONObject payload = JwtUtils.decodificarPayload(token);
            if (payload != null) {
                String nombre = payload.optString("nombre", "");
                userWelcome.setText("Hola, " + nombre);
                userWelcome.setVisibility(View.VISIBLE);
            }
        }

        if (!estaLogueado) {
            bottomNav.getMenu().removeItem(R.id.nav_menu);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Siempre va al inicio
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            } else if (itemId == R.id.nav_menu) {
                // Si está logueado, va a opciones
                if (estaLogueado) {
                    Toast.makeText(this, "Ir a opciones", Toast.LENGTH_SHORT).show();
                    if (menuOpciones.getVisibility() == View.GONE) {
                        menuOpciones.setVisibility(View.VISIBLE);
                    } else {
                        menuOpciones.setVisibility(View.GONE);
                    }
                }
                return true;

            } else if (itemId == R.id.nav_perfil) {
                if (!estaLogueado) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Ir a perfil", Toast.LENGTH_SHORT).show();
                    String rol = SharedPreferencesHelper.obtenerRol(this);
                    if (rol.equals("alumno")) {
                        //Toast.makeText(this, "alumno", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(this, "usuario", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(this, "usuario", Toast.LENGTH_SHORT).show();
                    }
                     startActivity(new Intent(this, PerfilActivity.class));
                }
                return true;
            }

            return false;
        });

        btnCrearReceta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrearRecetaActivity.class);
            startActivity(intent);
        });

    }

    public boolean hayInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }
}