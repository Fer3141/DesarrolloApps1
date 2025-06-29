package com.apps1.cocinapp.usuario;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps1.cocinapp.register.ApiService;
import com.apps1.cocinapp.register.RetrofitClient;
import com.apps1.cocinapp.session.SharedPreferencesHelper;


import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerfilActivity extends AppCompatActivity {

    private TextView nombrePerfil;
    private EditText biografiaInput;
    private Button btnGuardarBiografia;
    private LinearLayout seccionAlumno;

    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nombrePerfil = findViewById(R.id.nombrePerfil);
        biografiaInput = findViewById(R.id.biografiaInput);
        btnGuardarBiografia = findViewById(R.id.btnGuardarBiografia);
        seccionAlumno = findViewById(R.id.seccionAlumno);

        Long idUsuario = SharedPreferencesHelper.obtenerIdUsuario(this);  // Usá el ID, no el token
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

        obtenerBiografia();

        btnGuardarBiografia.setOnClickListener(v -> guardarBiografia());

    }


    private void obtenerBiografia() {
        ApiService api = RetrofitClient.getInstance().getApi();
        String token = SharedPreferencesHelper.obtenerToken(this);

        Call<ResponseBody> call = api.obtenerBiografia("Bearer " + token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String texto = response.body().string(); // ¡acá sí!
                        biografiaInput.setText(texto);
                    } else {
                        Toast.makeText(PerfilActivity.this, "Error inesperado", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PerfilActivity.this, "Error al leer respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Error al obtener biografía", Toast.LENGTH_SHORT).show();
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


