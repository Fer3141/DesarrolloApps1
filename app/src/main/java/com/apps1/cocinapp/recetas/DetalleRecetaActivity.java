package com.apps1.cocinapp.recetas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.apps1.cocinapp.R;
import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.dto.IngredienteDTO;
import com.apps1.cocinapp.dto.MultimediaDTO;
import com.apps1.cocinapp.dto.PasoCompletoDTO;
import com.apps1.cocinapp.dto.RecetaDetalleDTO;
import com.apps1.cocinapp.api.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.apps1.cocinapp.dto.CalificacionDTO;
import com.apps1.cocinapp.dto.CalificacionVistaDTO;
import com.bumptech.glide.Glide;


public class DetalleRecetaActivity extends AppCompatActivity {

    private ImageView detalleImagen;
    private TextView detalleTitulo, detalleAutor, detalleDescripcion, detalleCantidad;
    private RatingBar detalleRating, ratingUsuario;
    private LinearLayout contenedorIngredientes, contenedorPasos, contenedorComentarios;
    private EditText inputComentario;
    private Button btnEnviarComentario;

    private Long recetaId;
    private Long usuarioId = 1L; // Reemplazar por el ID real

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_receta);

        // Bind de views
        detalleImagen = findViewById(R.id.detalleImagen);
        detalleTitulo = findViewById(R.id.detalleTitulo);
        detalleAutor = findViewById(R.id.detalleAutor);
        detalleDescripcion = findViewById(R.id.detalleDescripcion);
        detalleCantidad = findViewById(R.id.detalleCantidad);
        detalleRating = findViewById(R.id.detalleRating);
        ratingUsuario = findViewById(R.id.ratingUsuario);
        contenedorIngredientes = findViewById(R.id.contenedorIngredientes);
        contenedorPasos = findViewById(R.id.contenedorPasos);
        contenedorComentarios = findViewById(R.id.contenedorComentarios);
        inputComentario = findViewById(R.id.inputComentario);
        btnEnviarComentario = findViewById(R.id.btnEnviarComentario);

        recetaId = getIntent().getLongExtra("idReceta", -1);

        if (recetaId != -1) {
            cargarDetalleReceta();
            cargarComentarios();
        }

        btnEnviarComentario.setOnClickListener(v -> enviarComentario());
    }

    private void cargarDetalleReceta() {
        ApiService api = RetrofitClient.getInstance().getApi();
        Call<RecetaDetalleDTO> call = api.getDetalleReceta(recetaId);
        call.enqueue(new Callback<RecetaDetalleDTO>() {
            @Override
            public void onResponse(Call<RecetaDetalleDTO> call, Response<RecetaDetalleDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecetaDetalleDTO r = response.body();

                    detalleTitulo.setText(r.nombreReceta);
                    detalleAutor.setText("Por " + r.nombreUsuario);
                    detalleDescripcion.setText(r.descripcionReceta);
                    detalleCantidad.setText("Rinde " + r.porciones + " porciones para " + r.cantidadPersonas + " personas");

                    Glide.with(DetalleRecetaActivity.this)
                            .load(r.fotoPrincipal)
                            .into(detalleImagen);

                    cargarIngredientes(r);
                    cargarPasos(r);
                }
            }

            @Override
            public void onFailure(Call<RecetaDetalleDTO> call, Throwable t) {
                Toast.makeText(DetalleRecetaActivity.this, "Error al cargar receta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarIngredientes(RecetaDetalleDTO r) {
        contenedorIngredientes.removeAllViews();
        for (IngredienteDTO i : r.ingredientes) {
            TextView t = new TextView(this);
            t.setText("- " + i.nombre + ": " + i.cantidad + " " + i.unidad + (i.observaciones != null ? " (" + i.observaciones + ")" : ""));
            contenedorIngredientes.addView(t);
        }
    }

    private void cargarPasos(RecetaDetalleDTO r) {
        contenedorPasos.removeAllViews();
        for (PasoCompletoDTO paso : r.pasos) {
            TextView t = new TextView(this);
            t.setText(paso.nroPaso + ". " + paso.texto);
            contenedorPasos.addView(t);
            if (paso.multimedia != null && !paso.multimedia.isEmpty()) {
                for (MultimediaDTO media : paso.multimedia) {
                    if (media.tipo.equals("foto")) {
                        ImageView img = new ImageView(this);
                        Glide.with(this).load(media.url).into(img);
                        contenedorPasos.addView(img);
                    }
                }
            }
        }
    }

    private void cargarComentarios() {
        ApiService api = RetrofitClient.getInstance().getApi();
        Call<List<CalificacionVistaDTO>> call = api.obtenerCalificaciones(recetaId);
        call.enqueue(new Callback<List<CalificacionVistaDTO>>() {
            @Override
            public void onResponse(Call<List<CalificacionVistaDTO>> call, Response<List<CalificacionVistaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contenedorComentarios.removeAllViews();
                    for (CalificacionVistaDTO c : response.body()) {
                        TextView t = new TextView(DetalleRecetaActivity.this);
                        t.setText(c.aliasUsuario + ":\n" + c.comentario);
                        contenedorComentarios.addView(t);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CalificacionVistaDTO>> call, Throwable t) {
                Toast.makeText(DetalleRecetaActivity.this, "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarComentario() {
        String texto = inputComentario.getText().toString();
        int rating = (int) ratingUsuario.getRating();

        CalificacionDTO dto = new CalificacionDTO();
        dto.idUsuario = usuarioId; // reemplazá esto si obtenés el ID desde el token
        dto.idReceta = recetaId;
        dto.calificacion = rating;
        dto.comentarios = texto;

        ApiService api = RetrofitClient.getInstance().getApi();
        Call<Void> call = api.enviarCalificacion(dto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetalleRecetaActivity.this, "¡Gracias por comentar!", Toast.LENGTH_SHORT).show();
                    cargarComentarios();
                    inputComentario.setText("");
                    ratingUsuario.setRating(0);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetalleRecetaActivity.this, "Error al enviar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

