package com.apps1.cocinapp.register;

import com.apps1.cocinapp.data.usuarios;
import com.apps1.cocinapp.recetas.RecetaDTO;
import com.apps1.cocinapp.usuario.AlumnoRequest;
import com.apps1.cocinapp.usuario.DatosAlumnoDTO;
import com.apps1.cocinapp.usuario.PerfilDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import okhttp3.ResponseBody;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

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

    @POST("recetas/crear")
    Call<Void> crearReceta(@Header("Authorization") String token, @Body RecetaDTO recetaDTO);



}

