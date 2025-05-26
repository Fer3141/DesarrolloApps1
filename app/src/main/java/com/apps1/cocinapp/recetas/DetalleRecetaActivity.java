package com.apps1.cocinapp.recetas;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

public class DetalleRecetaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_receta);

        ImageView imagen = findViewById(R.id.detalleImagen);
        TextView titulo = findViewById(R.id.detalleTitulo);
        TextView autor = findViewById(R.id.detalleAutor);
        RatingBar rating = findViewById(R.id.detalleRating);

        // Recibir datos del intent
        String tituloStr = getIntent().getStringExtra("titulo");
        String autorStr = getIntent().getStringExtra("autor");
        float ratingVal = getIntent().getFloatExtra("rating", 0f);
        int imagenRes = getIntent().getIntExtra("imagen", R.drawable.logo_cocinapp);

        // Mostrar
        titulo.setText(tituloStr);
        autor.setText("Por " + autorStr);
        rating.setRating(ratingVal);
        imagen.setImageResource(imagenRes);
    }
}
