package com.apps1.cocinapp.api;

import com.apps1.cocinapp.dto.CalificacionDTO;
import com.apps1.cocinapp.dto.CalificacionVistaDTO;
import com.apps1.cocinapp.dto.CursoConCronogramasDTO;
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
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // --- ADMIN ---

    @GET("/admin/recetas/pendientes")
    Call<List<RecetaDetalleDTO>> getRecetasPendientes(@Query("idUsuario") Long id);

    @PUT("/admin/recetas/{id}/aprobar")
    Call<Void> aprobarReceta(@Path("id") Long idReceta, @Query("idUsuario") Long idUsuario);

    @PUT("/admin/recetas/{id}/rechazar")
    Call<Void> rechazarReceta(@Path("id") Long idReceta, @Query("idUsuario") Long idUsuario, @Body Map<String, String> motivo);


    // --- RECETAS ---

    @GET("/recetas/ultimas")
    Call<List<RecetaResumenDTO>> getUltimasRecetas();

    @GET("/recetas/mejores")
    Call<List<RecetaResumenDTO>> getMejoresRecetas();

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

    @GET("/recetas/verificar-nombre")
    Call<Map<String, Object>> verificarNombreReceta(
            @Query("idUsuario") Long idUsuario,
            @Query("nombre") String nombre
    );

    @DELETE("/recetas")
    Call<Void> eliminarReceta(
            @Query("idUsuario") Long idUsuario,
            @Query("idReceta") Long idReceta
    );

    @GET("/recetas")
    Call<List<RecetaResumenDTO>> buscarPorNombre(@Query("nombre") String nombre);

    @GET("/mis-recetas")
    Call<List<RecetaResumenDTO>> getRecetasPorUsuario(@Query("idUsuario") Long idUsuario);



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

    @POST("/cursos/crearCurso")
    <CursoDisponibleDTO>
    Call<Void> crearCurso(
            @Body CursoDisponibleDTO curso
    );

    @POST("/cursos/crear-cronograma")
    Call<Void> crearCronograma(
            @Query("idCurso") Long idCurso,
            @Query("idSede") Long idSede,
            @Query("fechaInicio") String fechaInicio,
            @Query("fechaFin") String fechaFin,
            @Query("vacantes") int vacantes
    );

    @GET("/cursos/admin/cursos-con-cronogramas")
    Call<List<CursoConCronogramasDTO>> getCursosConCronogramas();


    @FormUrlEncoded
    @POST("cursos/asistencia/checkin-qr")
    Call<String> marcarAsistenciaQR(
            @Field("idAlumno") Long idAlumno,
            @Field("qr") String qr
    );




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

