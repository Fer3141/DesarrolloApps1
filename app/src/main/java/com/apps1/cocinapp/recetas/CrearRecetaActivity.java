package com.apps1.cocinapp.recetas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.dto.*;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.dto.*;
import com.apps1.cocinapp.session.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearRecetaActivity extends AppCompatActivity {

    private EditText descripcionInput, porcionesInput, personasInput;
    private Spinner tipoSpinner;

    private LinearLayout contenedorIngredientes, contenedorPasos;
    private Button btnAgregarIngrediente, btnAgregarPaso, btnPublicar;
    private ImageView imagenPreviewReceta;

    private String urlMockImagenReceta = null;
    private ActivityResultLauncher<Intent> galeriaLauncher;
    private View pasoSeleccionadoParaImagen;

    private String modo; // "nuevo", "editar", "reemplazar"
    private String nombreReceta;
    private Long idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        idUsuario = SharedPreferencesHelper.obtenerIdUsuario(this);

        EditText nombreInput = findViewById(R.id.nombreInput);
        String nombreDesdeVerificacion = getIntent().getStringExtra("nombreReceta");
        if (nombreDesdeVerificacion != null) {
            nombreInput.setText(nombreDesdeVerificacion);
            nombreInput.setEnabled(false); // opcional: evitar que lo editen
        }



        descripcionInput = findViewById(R.id.descripcionInput);
        porcionesInput = findViewById(R.id.porcionesInput);
        personasInput = findViewById(R.id.personasInput);
        tipoSpinner = findViewById(R.id.tipoSpinner);
        contenedorIngredientes = findViewById(R.id.contenedorIngredientes);
        contenedorPasos = findViewById(R.id.contenedorPasos);
        btnAgregarIngrediente = findViewById(R.id.btnAgregarIngrediente);
        btnAgregarPaso = findViewById(R.id.btnAgregarPaso);
        btnPublicar = findViewById(R.id.btnPublicar);
        imagenPreviewReceta = findViewById(R.id.imagenPreviewReceta);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Seleccionar tipo...", "Entrada", "Principal", "Postre"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoSpinner.setAdapter(adapter);

        btnAgregarIngrediente.setOnClickListener(v -> agregarIngrediente());
        btnAgregarPaso.setOnClickListener(v -> agregarPaso());
        btnPublicar.setOnClickListener(v -> enviarReceta());

        imagenPreviewReceta.setOnClickListener(v -> {
            pasoSeleccionadoParaImagen = null;
            abrirGaleria();
        });

        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imagenSeleccionada = result.getData().getData();
                        if (pasoSeleccionadoParaImagen != null && imagenSeleccionada != null) {
                            ImageView preview = pasoSeleccionadoParaImagen.findViewById(R.id.imagenPreviewPaso);
                            preview.setVisibility(View.VISIBLE);
                            preview.setImageURI(imagenSeleccionada);
                            preview.setTag(imagenSeleccionada.toString());
                        } else if (imagenPreviewReceta != null && imagenSeleccionada != null) {
                            imagenPreviewReceta.setImageURI(imagenSeleccionada);
                            urlMockImagenReceta = "https://mock.miapp.com/" + imagenSeleccionada.hashCode() + ".jpg";
                        }
                    }
                }
        );

        // Recuperar datos del Intent
        modo = getIntent().getStringExtra("modo");
        nombreReceta = getIntent().getStringExtra("nombreReceta");

        if ("editar".equals(modo)) {
            cargarRecetaExistente();
        }
    }

    private void cargarRecetaExistente() {
        ApiService api = RetrofitClient.getInstance().getApi();
        Call<List<RecetaResumenDTO>> call = api.buscarRecetasPorNombre(nombreReceta);

        call.enqueue(new Callback<List<RecetaResumenDTO>>() {
            @Override
            public void onResponse(Call<List<RecetaResumenDTO>> call, Response<List<RecetaResumenDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    RecetaResumenDTO resumen = response.body().get(0); // tomo el primero
                    Long idReceta = resumen.getIdReceta(); // o resumen.idReceta según tu DTO

                    api.getDetalleReceta(idReceta);
                } else {
                    Toast.makeText(CrearRecetaActivity.this, "No se encontró la receta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecetaResumenDTO>> call, Throwable t) {
                Toast.makeText(CrearRecetaActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void agregarIngrediente() {
        View item = getLayoutInflater().inflate(R.layout.item_ingrediente, null);
        contenedorIngredientes.addView(item);
    }

    private void agregarPaso() {
        View item = getLayoutInflater().inflate(R.layout.item_paso_crear, null);
        contenedorPasos.addView(item);

        Button btnAgregarFoto = item.findViewById(R.id.btnAgregarFotoPaso);
        btnAgregarFoto.setOnClickListener(v -> {
            pasoSeleccionadoParaImagen = item;
            abrirGaleria();
        });
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galeriaLauncher.launch(intent);
    }

    private void enviarReceta() {
        String descripcion = descripcionInput.getText().toString();
        String porcionesStr = porcionesInput.getText().toString();
        String personasStr = personasInput.getText().toString();
        int tipoPos = tipoSpinner.getSelectedItemPosition();

        if (TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(porcionesStr) || TextUtils.isEmpty(personasStr) || tipoPos == 0) {
            Toast.makeText(this, "completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int porciones = Integer.parseInt(porcionesStr);
        int personas = Integer.parseInt(personasStr);

        // Convertir tipo para el backend
        String tipoTexto = tipoSpinner.getSelectedItem().toString();
        String tipoBack;
        switch (tipoTexto) {
            case "Entrada": tipoBack = "ENTRADA"; break;
            case "Principal": tipoBack = "PLATO_PRINCIPAL"; break;
            case "Postre": tipoBack = "POSTRE"; break;
            default:
                Toast.makeText(this, "Tipo inválido", Toast.LENGTH_SHORT).show();
                return;
        }

        Long idUsuario = SharedPreferencesHelper.obtenerIdUsuario(this);
        if (idUsuario == null || idUsuario == -1) {
            Toast.makeText(this, "No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        List<IngredienteDTO> ingredientes = new ArrayList<>();
        for (int i = 0; i < contenedorIngredientes.getChildCount(); i++) {
            View v = contenedorIngredientes.getChildAt(i);
            EditText nombreIng = v.findViewById(R.id.inputNombreIngrediente);
            EditText cantidad = v.findViewById(R.id.inputCantidadIngrediente);
            EditText unidad = v.findViewById(R.id.inputUnidadIngrediente);
            EditText obs = v.findViewById(R.id.inputObsIngrediente);

            if (!TextUtils.isEmpty(nombreIng.getText()) && !TextUtils.isEmpty(cantidad.getText()) && !TextUtils.isEmpty(unidad.getText())) {
                IngredienteDTO ing = new IngredienteDTO();
                ing.nombre = nombreIng.getText().toString();
                ing.cantidad = (int) Double.parseDouble(cantidad.getText().toString());
                ing.unidad = unidad.getText().toString();
                ing.observaciones = obs.getText().toString();
                ingredientes.add(ing);
            }
        }

        List<PasoCompletoDTO> pasos = new ArrayList<>();
        for (int i = 0; i < contenedorPasos.getChildCount(); i++) {
            View v = contenedorPasos.getChildAt(i);
            EditText textoPaso = v.findViewById(R.id.inputTextoPaso);

            if (!TextUtils.isEmpty(textoPaso.getText())) {
                PasoCompletoDTO paso = new PasoCompletoDTO();
                paso.nroPaso = i + 1;
                paso.texto = textoPaso.getText().toString();

                ImageView imgPreview = v.findViewById(R.id.imagenPreviewPaso);
                String uriString = (imgPreview.getTag() != null) ? imgPreview.getTag().toString() : null;

                paso.multimedia = new ArrayList<>();
                if (uriString != null) {
                    MultimediaDTO media = new MultimediaDTO();
                    media.tipo = "foto";
                    media.url = "https://mock.miapp.com/" + uriString.hashCode() + ".jpg";
                    media.extension = "jpg";
                    paso.multimedia.add(media);
                }

                pasos.add(paso);
            }
        }

        NuevaRecetaDTO nueva = new NuevaRecetaDTO();
        nueva.nombreReceta = nombreReceta;
        nueva.descripcionReceta = descripcion;
        nueva.fotoPrincipal = urlMockImagenReceta != null ? urlMockImagenReceta : "https://www.recetasnestlecam.com/sites/default/files/srh_recipes/369562012750bd46ceaeef5d59a23229.jpg";
        nueva.porciones = porciones;
        nueva.cantidadPersonas = personas;
        nueva.tipo = tipoBack;
        nueva.idUsuario = idUsuario;
        nueva.ingredientes = ingredientes;
        nueva.pasos = pasos;

        ApiService api = RetrofitClient.getInstance().getApi();
        guardarReceta(api, nueva); //
    }


    private void guardarReceta(ApiService api, NuevaRecetaDTO nueva) {
        Call<Void> call = api.crearReceta(nueva);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearRecetaActivity.this, "receta enviada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CrearRecetaActivity.this, "error al guardar receta", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CrearRecetaActivity.this, "fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
