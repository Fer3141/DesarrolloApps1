package com.apps1.cocinapp.Admin;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MenuAdmin extends AppCompatActivity {

    private LinearLayout mainContainer;
    private Button btnRecetas;
    private Button btnCursos;

    private List<Receta> recetasPendientes;
    private List<Curso> cursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout raíz vertical
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.parseColor("#FFF7E6"));
        rootLayout.setPadding(20, 40, 20, 20);

        // Botones para cambiar de vista
        btnRecetas = new Button(this);
        btnRecetas.setText("Recetas");
        btnRecetas.setBackgroundColor(Color.parseColor("#E58C23"));
        btnRecetas.setTextColor(Color.WHITE);

        btnCursos = new Button(this);
        btnCursos.setText("Cursos");
        btnCursos.setBackgroundColor(Color.parseColor("#F5F5F5"));
        btnCursos.setTextColor(Color.BLACK);

        LinearLayout topButtons = new LinearLayout(this);
        topButtons.setOrientation(LinearLayout.HORIZONTAL);
        topButtons.addView(btnRecetas);
        topButtons.addView(btnCursos);

        // Contenedor principal dinámico
        mainContainer = new LinearLayout(this);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setPadding(10, 30, 10, 10);

        // Añadir al layout
        rootLayout.addView(topButtons);
        rootLayout.addView(mainContainer);

        setContentView(rootLayout);

        // Listeners
        btnRecetas.setOnClickListener(v -> showRecetasMenu());
        btnCursos.setOnClickListener(v -> showCursosMenu());

        // Mock data simulando datos del backend
        cargarDatosSimulados();

        // Vista inicial
        showRecetasMenu();
    }

    private void cargarDatosSimulados() {
        recetasPendientes = new ArrayList<>();
        recetasPendientes.add(new Receta(1, "Pizza Italiana", "Intermedio", 45, null));
        recetasPendientes.add(new Receta(2, "Pasta Carbonara", "Fácil", 20, null));

        cursos = new ArrayList<>();
        List<Cronograma> cronos1 = new ArrayList<>();
        cronos1.add(new Cronograma("Sede Centro", "2025-07-10", "2025-07-20", 18, 20, "https://api.qrserver.com/v1/create-qr-code/?data=Curso1"));
        cronos1.add(new Cronograma("Sede Norte", "2025-08-01", "2025-08-15", 20, 20, "https://api.qrserver.com/v1/create-qr-code/?data=Curso2"));
        cursos.add(new Curso("Cocina Italiana Básica", cronos1));
    }

    private void showRecetasMenu() {
        mainContainer.removeAllViews();

        TextView title = new TextView(this);
        title.setText("ADMINISTRACIÓN - Aprobar Recetas");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#5A4635"));
        mainContainer.addView(title);

        //La idea es añadir una columna de 'Aprobado' en la tabla de recetas
        //que pueda quedar vacio (null) y que desaparezcan de esta lista a
        //medida que se aprueben o rechacen.
        for (Receta receta : new ArrayList<>(recetasPendientes)) {
            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.HORIZONTAL);
            item.setPadding(10, 20, 10, 20);

            TextView tv = new TextView(this);
            tv.setText(receta.nombre + " - " + receta.dificultad + " - " + receta.tiempo + " min");
            tv.setPadding(0, 0, 20, 0);

            Button btnAprobar = new Button(this);
            btnAprobar.setText("✓");
            btnAprobar.setOnClickListener(v -> {
                receta.aprobado = 1;
                recetasPendientes.remove(receta);
                showRecetasMenu();
            });

            Button btnRechazar = new Button(this);
            btnRechazar.setText("✕");
            btnRechazar.setOnClickListener(v -> {
                receta.aprobado = 0;
                recetasPendientes.remove(receta);
                showRecetasMenu();
            });

            item.addView(tv);
            item.addView(btnAprobar);
            item.addView(btnRechazar);

            mainContainer.addView(item);
        }

        // Cambiar colores pestañas
        btnRecetas.setBackgroundColor(Color.parseColor("#E58C23"));
        btnRecetas.setTextColor(Color.WHITE);
        btnCursos.setBackgroundColor(Color.parseColor("#F5F5F5"));
        btnCursos.setTextColor(Color.BLACK);
    }

    private void showCursosMenu() {
        mainContainer.removeAllViews();

        TextView title = new TextView(this);
        title.setText("GESTIÓN DE CURSOS");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#5A4635"));
        mainContainer.addView(title);

        Button btnCrearCurso = new Button(this);
        btnCrearCurso.setText("+ Crear un curso");
        btnCrearCurso.setBackgroundColor(Color.parseColor("#E58C23"));
        btnCrearCurso.setTextColor(Color.WHITE);
        btnCrearCurso.setOnClickListener(v -> {
            Toast.makeText(this, "Crear curso (no implementado)", Toast.LENGTH_SHORT).show();
        });
        mainContainer.addView(btnCrearCurso);

        for (Curso curso : cursos) {
            TextView cursoNombre = new TextView(this);
            cursoNombre.setText("📖 " + curso.nombre);
            cursoNombre.setTextSize(18);
            cursoNombre.setPadding(0, 20, 0, 10);
            mainContainer.addView(cursoNombre);

            for (Cronograma cron : curso.cronogramas) {
                LinearLayout cronItem = new LinearLayout(this);
                cronItem.setOrientation(LinearLayout.VERTICAL);
                cronItem.setPadding(20,10,20,10);
                cronItem.setBackgroundColor(Color.parseColor("#F0F0F0"));

                TextView cronInfo = new TextView(this);
                cronInfo.setText("Sede: " + cron.sede + "\nFechas: " + cron.fechaInicio + " - " + cron.fechaFin
                        + "\nInscritos: " + cron.inscritos + "/" + cron.capacidad);
                cronItem.addView(cronInfo);

                ImageView qr = new ImageView(this);
                Glide.with(this).load(cron.urlQr).into(qr);
                qr.setAdjustViewBounds(true);
                qr.setMaxHeight(300);
                cronItem.addView(qr);

                Button btnCancelar = new Button(this);
                btnCancelar.setText("Cancelar curso");
                btnCancelar.setBackgroundColor(Color.RED);
                btnCancelar.setTextColor(Color.WHITE);
                btnCancelar.setOnClickListener(v -> {
                    Toast.makeText(this, "Curso cancelado en backend (simulado)", Toast.LENGTH_SHORT).show();
                    curso.cronogramas.remove(cron);
                    showCursosMenu();
                });
                cronItem.addView(btnCancelar);

                mainContainer.addView(cronItem);
            }
        }

        btnCursos.setBackgroundColor(Color.parseColor("#E58C23"));
        btnCursos.setTextColor(Color.WHITE);
        btnRecetas.setBackgroundColor(Color.parseColor("#F5F5F5"));
        btnRecetas.setTextColor(Color.BLACK);
    }

    // MODELOS SIMPLES
    static class Receta {
        int id;
        String nombre;
        String dificultad;
        int tiempo;
        Integer aprobado;

        Receta(int id, String nombre, String dificultad, int tiempo, Integer aprobado) {
            this.id = id;
            this.nombre = nombre;
            this.dificultad = dificultad;
            this.tiempo = tiempo;
            this.aprobado = aprobado;
        }
    }

    static class Curso {
        String nombre;
        List<Cronograma> cronogramas;
        Curso(String nombre, List<Cronograma> cronogramas) {
            this.nombre = nombre;
            this.cronogramas = cronogramas;
        }
    }

    static class Cronograma {
        String sede;
        String fechaInicio;
        String fechaFin;
        int inscritos;
        int capacidad;
        String urlQr;

        Cronograma(String sede, String fechaInicio, String fechaFin, int inscritos, int capacidad, String urlQr) {
            this.sede = sede;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            this.inscritos = inscritos;
            this.capacidad = capacidad;
            this.urlQr = urlQr;
        }
    }
}

