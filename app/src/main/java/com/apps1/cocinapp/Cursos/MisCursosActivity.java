package com.apps1.cocinapp.Cursos;

import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;

import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.session.SharedPreferencesHelper;
import com.journeyapps.barcodescanner.ScanOptions;
import com.journeyapps.barcodescanner.ScanContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MisCursosActivity extends AppCompatActivity {

    private LinearLayout mainContainer;
    private List<CursoInscripto> cursos = new ArrayList<>(); // ejemplo lista

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if(result.getContents() != null) {
                    Toast.makeText(this, "QR: " + result.getContents(), Toast.LENGTH_LONG).show();
                    manejarAsistenciaConQR(result.getContents());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainContainer = new LinearLayout(this);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setPadding(20, 40, 20, 20);
        mainContainer.setBackgroundColor(Color.parseColor("#FFF7E6"));
        setContentView(mainContainer);

        mostrarCursos();
    }


    private void mostrarCursos() {
        for (CursoInscripto c : cursos) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(20,20,20,20);
            card.setBackgroundColor(Color.parseColor("#F0F0F0"));

            TextView tv = new TextView(this);
            tv.setText("📖 " + c.nombre + "\nFecha: " + c.fechaInicio + "\nSede: " + c.sede);
            tv.setTextSize(16);
            card.addView(tv);

            Button btnAsistencia = new Button(this);
            btnAsistencia.setText("Marcar Asistencia");
            btnAsistencia.setBackgroundColor(Color.parseColor("#E58C23"));
            btnAsistencia.setTextColor(Color.WHITE);
            btnAsistencia.setOnClickListener(v -> abrirScannerQR());
            card.addView(btnAsistencia);

            mainContainer.addView(card);
        }
    }

    private void abrirScannerQR() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Escaneá el código QR");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        barcodeLauncher.launch(options);
    }

    private void manejarAsistenciaConQR(String qrContents) {
        Long idAlumno = SharedPreferencesHelper.obtenerIdUsuario(this);

        ApiService api = RetrofitClient.getInstance().getApi();
        Call<String> call = api.marcarAsistenciaQR(idAlumno, qrContents);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MisCursosActivity.this, "Asistencia registrada: " + response.body(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MisCursosActivity.this, "Error en respuesta del servidor", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MisCursosActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }


    static class CursoInscripto {
        Long idCurso;
        String nombre;
        String fechaInicio;
        String sede;
        Long idCronograma;

        public CursoInscripto(Long idCurso, String nombre, String fechaInicio, String sede, Long idCronograma) {
            this.idCurso = idCurso;
            this.nombre = nombre;
            this.fechaInicio = fechaInicio;
            this.sede = sede;
            this.idCronograma = idCronograma;
        }
    }
}


