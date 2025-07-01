// actualizado para conectar al backend y cargar recetas reales al iniciar
package com.apps1.cocinapp.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.Admin.MenuAdmin;
import com.apps1.cocinapp.R;
import com.apps1.cocinapp.dto.RecetaResumenDTO;
import com.apps1.cocinapp.login.LoginActivity;
import com.apps1.cocinapp.receta.RecetaAdapter;
import com.apps1.cocinapp.recetas.VerificarNombreRecetaActivity;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.session.SharedPreferencesHelper;
import com.apps1.cocinapp.usuario.PerfilActivity;
import com.apps1.cocinapp.utils.JwtUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView navLogin, navLogout, navAbout, tabRecent, tabRecipes, tabCourses, mensajeSinConexion, visitorMessage;
    EditText searchBox;
    RecetaAdapter adapter;
    RecyclerView recyclerView;
    List<RecetaResumenDTO> recetasRecientes = new ArrayList<>();
    BottomNavigationView bottomNav;
    private boolean estaLogueado;
    LinearLayout menuOpciones;
    Button btnCrearReceta ,btnCerrarFiltros;

    LinearLayout menuFiltros;

    ImageButton searchIcon;

    Spinner spinnerTipo;

    RelativeLayout rootLayout;

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
        searchIcon = findViewById(R.id.searchIcon);
        menuFiltros = findViewById(R.id.menuFiltros);
        rootLayout = findViewById(R.id.rootLayout);
        btnCerrarFiltros = findViewById(R.id.btnCerrarFiltros);
        spinnerTipo = findViewById(R.id.spinnerTipo);

        estaLogueado = false;

        TextView tabRecent = findViewById(R.id.tabRecent);   // Mejores feed
        TextView tabRecipes = findViewById(R.id.tabRecipes); // Nuevo feed



        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecetaAdapter(this, recetasRecientes);
        recyclerView.setAdapter(adapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Seleccionar tipo...", "Entrada", "Principal", "Postre"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        navLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        navAbout.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        navLogout.setOnClickListener(v -> {
            SharedPreferencesHelper.eliminarToken(this);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        tabRecent.setOnClickListener(v -> {
            cargarRecetasMejores();
            Toast.makeText(this, "mostrando recetas populares", Toast.LENGTH_SHORT).show();
        });

        tabRecipes.setOnClickListener(v -> {
            cargarRecetasUltimas();
            Toast.makeText(this, "mostrando recetas nuevas", Toast.LENGTH_SHORT).show();
        });

        tabCourses.setOnClickListener(v -> {
            Toast.makeText(this, "vista de cursos todavia no conectada", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_menu) {
                if (estaLogueado) {
                    menuOpciones.setVisibility(menuOpciones.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                }
                return true;
            } else if (itemId == R.id.nav_perfil) {
                if (!estaLogueado) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    String rol = SharedPreferencesHelper.obtenerRolDelToken(this);
                    Toast.makeText(this, "Rol:" + rol, Toast.LENGTH_SHORT).show();

                    if (rol.equals("ADMIN")) {
                        startActivity(new Intent(this, MenuAdmin.class));
                    } else if (rol.equals("ALUMNO")) {
                        startActivity(new Intent(this, PerfilActivity.class));
                    } else {
                        // si no hay rol definido, redirigir al perfil por defecto
                        startActivity(new Intent(this, PerfilActivity.class));
                    }
                    startActivity(new Intent(this, PerfilActivity.class));
                }
                return true;
            }
            return false;
        });

        btnCrearReceta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VerificarNombreRecetaActivity.class);
            startActivity(intent);
        });

        cargarRecetas();


        searchIcon.setOnClickListener(v -> {
            menuFiltros.setVisibility(View.VISIBLE);
           // String query = searchBox.getText().toString().trim();
            /*if (!query.isEmpty()) {
               // buscarRecetasPorNombre(query);
            } else {
                Toast.makeText(MainActivity.this, "Ingrese un nombre para buscar", Toast.LENGTH_SHORT).show();
            }*/
        });

        rootLayout.setOnClickListener(v -> {
            if (menuFiltros.getVisibility() == View.VISIBLE) {
                menuFiltros.setVisibility(View.GONE);
            }
        });

        btnCerrarFiltros.setOnClickListener(v -> {
            menuFiltros.setVisibility(View.GONE);
        });


    }

    private void cargarRecetas() {
        RetrofitClient.getInstance().getApi().getRecetas().enqueue(new Callback<List<RecetaResumenDTO>>() {
            @Override
            public void onResponse(Call<List<RecetaResumenDTO>> call, Response<List<RecetaResumenDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recetasRecientes = response.body();
                    adapter.actualizarLista(recetasRecientes);
                } else {
                    Toast.makeText(MainActivity.this, "no se pudo cargar recetas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecetaResumenDTO>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void cargarRecetasUltimas() {
        RetrofitClient.getInstance().getApi().getUltimasRecetas().enqueue(new Callback<List<RecetaResumenDTO>>() {
            @Override
            public void onResponse(Call<List<RecetaResumenDTO>> call, Response<List<RecetaResumenDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recetasRecientes = response.body();
                    adapter.actualizarLista(recetasRecientes);
                } else {
                    Toast.makeText(MainActivity.this, "no se pudieron cargar recetas nuevas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecetaResumenDTO>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarRecetasMejores() {
        RetrofitClient.getInstance().getApi().getMejoresRecetas().enqueue(new Callback<List<RecetaResumenDTO>>() {
            @Override
            public void onResponse(Call<List<RecetaResumenDTO>> call, Response<List<RecetaResumenDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recetasRecientes = response.body();
                    adapter.actualizarLista(recetasRecientes);
                } else {
                    Toast.makeText(MainActivity.this, "no se pudieron cargar recetas mejores", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecetaResumenDTO>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error de red", Toast.LENGTH_SHORT).show();
            }
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
