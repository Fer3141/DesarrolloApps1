package com.apps1.cocinapp.recetas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.text.TextUtils;
import android.view.Gravity;
import android.content.Context;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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

    private EditText nombreInput, descripcionInput, porcionesInput, personasInput;

    private Spinner tipoSpinner;
    private LinearLayout contenedorIngredientes, contenedorPasos;
    private Button btnAgregarIngrediente, btnAgregarPaso, btnPublicar;

    private ActivityResultLauncher<Intent> galeriaLauncher;
    private View pasoSeleccionadoParaImagen; // aca guardamos el paso al que le agregamos imagen

    private ImageView imagenPreviewReceta;
    private String urlMockImagenReceta = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        // linkeo todos los inputs con el xml
        nombreInput = findViewById(R.id.nombreInput);
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

        imagenPreviewReceta.setOnClickListener(v -> {
            // cuando toco la imagen grande, abro galeria
            pasoSeleccionadoParaImagen = null; // esto evita que asocie con un paso
            abrirGaleria(); // usamos el mismo launcher que ya tenes
        });

        // le cargo opciones al spinner, por ahora trucho
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Seleccionar tipo...", "Entrada", "Principal", "Postre"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoSpinner.setAdapter(adapter);

        // cuando toco agregar ingrediente me suma uno
        btnAgregarIngrediente.setOnClickListener(v -> agregarIngrediente());
        btnAgregarPaso.setOnClickListener(v -> agregarPaso());

        // boton final, manda todo
        btnPublicar.setOnClickListener(v -> enviarReceta());


        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imagenSeleccionada = result.getData().getData();
                        if (pasoSeleccionadoParaImagen != null && imagenSeleccionada != null) {
                            ImageView preview = pasoSeleccionadoParaImagen.findViewById(R.id.imagenPreviewPaso);
                            preview.setVisibility(View.VISIBLE);
                            preview.setImageURI(imagenSeleccionada);

                            // guardo la uri en una tag para despues convertirla en un MultimediaDTO
                            preview.setTag(imagenSeleccionada.toString());
                        }
                    }
                }
        );

    }

    // agrega un layout con inputs para un ingrediente
    private void agregarIngrediente() {
        View item = getLayoutInflater().inflate(R.layout.item_ingrediente, null);
        contenedorIngredientes.addView(item);
    }

    // lo mismo pero para los pasos
    private void agregarPaso() {
        View item = getLayoutInflater().inflate(R.layout.item_paso_crear, null);
        contenedorPasos.addView(item);

        Button btnAgregarFoto = item.findViewById(R.id.btnAgregarFotoPaso);
        btnAgregarFoto.setOnClickListener(v -> {
            pasoSeleccionadoParaImagen = item; // este es el paso que disparo la accion
            abrirGaleria();
        });
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galeriaLauncher.launch(intent);
    }

    private void enviarReceta() {
        // agarro todos los datos principales
        String nombre = nombreInput.getText().toString();
        String descripcion = descripcionInput.getText().toString();
        String porcionesStr = porcionesInput.getText().toString();
        String personasStr = personasInput.getText().toString();
        int tipoPos = tipoSpinner.getSelectedItemPosition();

        // chequeo que no falte nada
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(descripcion)
                || TextUtils.isEmpty(porcionesStr) || TextUtils.isEmpty(personasStr) || tipoPos == 0) {
            Toast.makeText(this, "completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // paso los strings a enteros
        int porciones = Integer.parseInt(porcionesStr);
        int personas = Integer.parseInt(personasStr);
        int idTipo = tipoPos; // en mi caso ya coincide con lo que espera el back

        // ingredientes
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
                ing.cantidad = Integer.parseInt(cantidad.getText().toString());
                ing.unidad = unidad.getText().toString();
                ing.observaciones = obs.getText().toString();
                ingredientes.add(ing);
            }
        }

        // pasos
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
                    media.url = "https://mock.miapp.com/" + uriString.hashCode() + ".jpg"; // url falsa porque en realidad hay que mandar al back el archivo y ahi alojarlo en la bd del server como url
                    media.extension = "jpg";
                    paso.multimedia.add(media);
                }

                pasos.add(paso);

            }
        }

        // antes de armar el objeto
        Long userIdLong = SharedPreferencesHelper.obtenerIdUsuario(this);
        String rawToken = SharedPreferencesHelper.obtenerToken(this);
        Log.d("TOKEN_DEBUG", "Token guardado: " + rawToken);
        if (userIdLong == null) {
            Toast.makeText(this, "no se pudo obtener el usuario", Toast.LENGTH_SHORT).show();
            return;
        }
        int usuarioId = userIdLong.intValue(); // el dto espera int


        // armo el objeto con todo
                NuevaRecetaDTO nueva = new NuevaRecetaDTO();
                nueva.nombreReceta = nombre;
                nueva.descripcionReceta = descripcion;

                nueva.fotoPrincipal = urlMockImagenReceta != null ? urlMockImagenReceta : "app/src/main/res/drawable/pholder.jpg"; // si no eligio imagen le mando una default
                nueva.porciones = porciones;
                nueva.cantidadPersonas = personas;
                nueva.idTipo = idTipo;
                nueva.idUsuario = usuarioId; // ahora lo saco del token
                nueva.ingredientes = ingredientes;
                nueva.pasos = pasos;

        // pego al backend
        ApiService api = RetrofitClient.getInstance().getApi();
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
