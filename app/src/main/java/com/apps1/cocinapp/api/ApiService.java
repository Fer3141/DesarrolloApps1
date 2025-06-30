package com.apps1.cocinapp.api;

import com.apps1.cocinapp.dto.CalificacionDTO;
import com.apps1.cocinapp.dto.CalificacionVistaDTO;
import com.apps1.cocinapp.dto.CursoDisponibleDTO;
import com.apps1.cocinapp.dto.NuevaRecetaDTO;
import com.apps1.cocinapp.dto.RecetaDetalleDTO;
import com.apps1.cocinapp.dto.RecetaResumenDTO;
import com.apps1.cocinapp.register.CodigoVerificacionRequest;
import com.apps1.cocinapp.register.EmailRequest;
import com.apps1.cocinapp.register.LoginRequest;
import com.apps1.cocinapp.register.PasswordResetRequest;
import com.apps1.cocinapp.register.RegistroRequest;
import com.apps1.cocinapp.register.RegistroUsuarioRequest;
import com.apps1.cocinapp.usuario.AlumnoRequest;
import com.apps1.cocinapp.usuario.PerfilDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    // --- RECETAS ---
    @GET("/recetas")
    Call<List<RecetaResumenDTO>> getRecetas();

    @GET("/recetas/{id}")
    Call<RecetaDetalleDTO> getDetalleReceta(@retrofit2.http.Path("id") Long id);

    @GET("/recetas")
    Call<List<RecetaResumenDTO>> buscarRecetasPorNombre(@Query("nombre") String nombre);

    @POST("/recetas")
    Call<Void> crearReceta(@Body NuevaRecetaDTO receta);

    @GET("/recetas/favoritos")
    Call<List<RecetaResumenDTO>> getFavoritos(@Query("usuario") Long idUsuario);

    @POST("/recetas/favoritos")
    Call<Void> agregarFavorito(
            @Query("idUsuario") Long idUsuario,
            @Query("idReceta") Long idReceta
    );

    @DELETE("/recetas/favoritos")
    Call<Void> eliminarFavorito(
            @Query("idUsuario") Long idUsuario,
            @Query("idReceta") Long idReceta
    );

    // --- CALIFICACIONES ---
    @POST("/calificaciones")
    Call<Void> enviarCalificacion(@Body CalificacionDTO calificacion);

    @GET("/calificaciones")
    Call<List<CalificacionVistaDTO>> obtenerCalificaciones(@Query("idReceta") Long idReceta);


    // --- CURSOS ---
    @GET("/cursos")
    Call<List<CursoDisponibleDTO>> getCursos();

    @POST("/cursos/inscribirse")
    Call<Void> inscribirCurso(
            @Query("idAlumno") Long idAlumno,
            @Query("idCronograma") Long idCronograma
    );

    @GET("/cursos/mis-cursos")
    Call<List<CursoDisponibleDTO>> getMisCursos(@Query("idAlumno") Long idAlumno);



    // --- CIRCUITO LOGIN/REGISTER/RECOVER ---

    // paso 1 – registro inicial
    @POST("/api/registro-inicial")
    Call<ResponseBody> registroInicial(@Body RegistroRequest request);

    // paso 2 – confirmación del código
    @POST("/api/confirmar-codigo")
    Call<ResponseBody> confirmarCodigo(@Body CodigoVerificacionRequest request);

    // paso 3 – registro final (usuario o alumno)
    @POST("/api/register")
    Call<ResponseBody> registrarUsuario(@Body RegistroUsuarioRequest request);

    // login
    @POST("/api/login")
    Call<ResponseBody> login(@Body LoginRequest request);

    // recuperación paso 1 – enviar código
    @POST("/api/auth/recover-password")
    Call<ResponseBody> enviarCodigoRecuperacion(@Body EmailRequest request);

    // recuperación paso 2 – resetear contraseña
    @POST("/api/auth/reset-password")
    Call<ResponseBody> resetPassword(@Body PasswordResetRequest request);
    @POST("/api/auth/verificar-codigo-recuperacion")
    Call<ResponseBody> verificarCodigoRecuperacion(@Body CodigoVerificacionRequest request);

    @GET("api/obtener-perfil")
    Call<PerfilDTO> obtenerPerfil(@Header("Authorization") String authHeader);

    @PUT("api/editar-biografia")
    Call<Void> actualizarBiografia(@Header("Authorization") String token, @Query("biografia") String biografia);

    @PUT("/api/hacer-alumno")
    Call<Void> hacerseAlumno(@Header("Authorization") String authHeader, @Body AlumnoRequest datos);


}

