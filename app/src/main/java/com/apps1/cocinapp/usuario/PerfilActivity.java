package com.apps1.cocinapp.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps1.cocinapp.main.MainActivity;
import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.session.SharedPreferencesHelper;


import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerfilActivity extends AppCompatActivity {

    private TextView nombrePerfil;
    private EditText biografiaInput, editTramiteDNI, editNroTarjeta;
    private Button btnGuardarBiografia, btnCerrarSesion, btnQuieroSerAlumno, btnGuardarAlumno;
    private LinearLayout seccionAlumno, formAlumnoLayout;


    Long idUsuario;
    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nombrePerfil = findViewById(R.id.nombrePerfil);
        biografiaInput = findViewById(R.id.biografiaInput);
        btnGuardarBiografia = findViewById(R.id.btnGuardarBiografia);
        seccionAlumno = findViewById(R.id.seccionAlumno);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnQuieroSerAlumno = findViewById(R.id.btnQuieroSerAlumno);
        formAlumnoLayout = findViewById(R.id.formAlumnoLayout);
        formAlumnoLayout.setVisibility(View.GONE);
        editTramiteDNI = findViewById(R.id.editTramiteDNI);
        editNroTarjeta = findViewById(R.id.editNroTarjeta);
        btnGuardarAlumno = findViewById(R.id.btnGuardarAlumno);


        idUsuario = SharedPreferencesHelper.obtenerIdUsuario(this);  // Usá el ID, no el token
        String rol = SharedPreferencesHelper.obtenerRol(this);

        // Referencias comunes
        TextView nombreText = findViewById(R.id.nombrePerfil);
        Button btnQuieroSerAlumno = findViewById(R.id.btnQuieroSerAlumno);
        LinearLayout seccionAlumno = findViewById(R.id.seccionAlumno);

        // Mostrar según rol
        if (rol.equals("alumno")) {
            nombreText.setText("Alumno");
            btnQuieroSerAlumno.setVisibility(View.GONE);
            seccionAlumno.setVisibility(View.VISIBLE);
        } else {
            nombreText.setText("Usuario");
            btnQuieroSerAlumno.setVisibility(View.VISIBLE);
            seccionAlumno.setVisibility(View.GONE);
        }

        obtenerPerfil();

        btnGuardarBiografia.setOnClickListener(v -> guardarBiografia());

        btnCerrarSesion.setOnClickListener(v -> {
            SharedPreferencesHelper.eliminarToken(this);
            Toast.makeText(PerfilActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia el backstack
            startActivity(intent);
            finish(); // Cierra la actividad actual
        });


        btnQuieroSerAlumno.setOnClickListener(v -> {
            btnQuieroSerAlumno.setVisibility(View.GONE);
            formAlumnoLayout.setVisibility(View.VISIBLE);
        });

        btnGuardarAlumno.setOnClickListener(v -> {
            String tramiteDNI = editTramiteDNI.getText().toString().trim();
            String nroTarjeta = editNroTarjeta.getText().toString().trim();

            if (tramiteDNI.isEmpty() || nroTarjeta.isEmpty()) {
                Toast.makeText(PerfilActivity.this, "Completar todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            AlumnoRequest alumnoRequest = new AlumnoRequest(idUsuario, tramiteDNI, nroTarjeta);
            ApiService api = RetrofitClient.getInstance().getApi();
            String token = SharedPreferencesHelper.obtenerToken(this);

            Call<Void> call = api.hacerseAlumno("Bearer " + token, alumnoRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PerfilActivity.this, "Solicitud enviada correctamente", Toast.LENGTH_SHORT).show();
                        formAlumnoLayout.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(PerfilActivity.this, "Error al enviar solicitud", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PerfilActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }


    private void obtenerPerfil() {
        ApiService api = RetrofitClient.getInstance().getApi();
        String token = SharedPreferencesHelper.obtenerToken(this);

        Call<PerfilDTO> call = api.obtenerPerfil("Bearer " + token);
        call.enqueue(new Callback<PerfilDTO>() {
            @Override
            public void onResponse(Call<PerfilDTO> call, Response<PerfilDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    nombrePerfil.setText(response.body().getNombre());
                    biografiaInput.setText(response.body().getBiografia());
                } else {
                    Toast.makeText(PerfilActivity.this, "Error al obtener perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilDTO> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Error al conectar", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void guardarBiografia() {
        String bio = biografiaInput.getText().toString().trim();
        ApiService api = RetrofitClient.getInstance().getApi();
        String token = SharedPreferencesHelper.obtenerToken(this);

        Call<Void> call = api.actualizarBiografia("Bearer " + token, bio);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PerfilActivity.this, "Biografía guardada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PerfilActivity.this, "Error al guardar biografía", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
    }


}


