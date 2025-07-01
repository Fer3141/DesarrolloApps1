package com.apps1.cocinapp.Cursos;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class MisCursosActivity extends Activity {

    private LinearLayout mainContainer;
    //private Long idAlumno = getIdUsuario(); // en real, esto se obtiene del login pero no e como se hace

    private List<CursoInscripto> cursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainContainer = new LinearLayout(this);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setPadding(20, 40, 20, 20);
        mainContainer.setBackgroundColor(Color.parseColor("#FFF7E6"));
        setContentView(mainContainer);

        // Simula obtener cursos inscriptos (después cambiarlo por GET de MisCursos)
        cargarDatos();

        // Arma la pantalla
        mostrarCursos();
    }

    private void cargarDatos() {
        cursos = new ArrayList<>();
        cursos.add(new CursoInscripto(1L, "Curso Cocina Italiana", "2025-07-10", "Sede Centro", 101L));
        cursos.add(new CursoInscripto(2L, "Curso Pastelería Avanzada", "2025-08-15", "Sede Norte", 102L));
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
            card.addView(btnAsistencia);

            btnAsistencia.setOnClickListener(v -> abrirScannerQR(c));

            mainContainer.addView(card);
        }
    }

    private void abrirScannerQR(CursoInscripto curso) {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, curso.idCronograma.intValue());
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Necesitás instalar un lector QR como Barcode Scanner.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Long idAlumno = 1L; // Simula el ID del alumno, en real debería obtenerse del login o SharedPreferences

        if (resultCode == RESULT_OK) {
            String qrContents = data.getStringExtra("SCAN_RESULT");
            // El requestCode tiene el idCronograma que pasamos antes
            marcarAsistenciaBackend(idAlumno, (long) requestCode, qrContents);
        }
    }

    private void marcarAsistenciaBackend(Long idAlumno, Long idCronograma, String qr) {
        // Aca parseás el qr si tiene JSON o un string "curso:ID,cronograma:ID"
        // Hacés POST al backend con idAlumno, idCurso y idCronograma
        Toast.makeText(this,
                "Asistencia marcada.\nAlumno: " + idAlumno + "\nCronograma: " + idCronograma + "\nQR: " + qr,
                Toast.LENGTH_LONG).show();
    }

    // MODELO SIMPLE
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

    // Helper para margen (solo para versiones modernas de SDK)
    private void LinearLayout_setMarginBottom(LinearLayout ll, int dp) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp);
        ll.setLayoutParams(params);
    }
}

