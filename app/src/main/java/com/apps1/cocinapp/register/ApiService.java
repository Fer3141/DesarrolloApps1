package com.apps1.cocinapp.register;

import com.apps1.cocinapp.data.usuarios;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/auth/verificar")
    Call<Void> verificarUsuario(@Body RegistroRequest request);

    // para registrarse
    @POST("/api/verificar-codigo")
    Call<ResponseBody> verificarCodigo(@Body CodigoVerificacionRequest body);

    // para recuperación de contraseña
    @POST("/api/verificar-codigo-recuperacion")
    Call<ResponseBody> verificarCodigoRecuperacion(@Body CodigoVerificacionRequest body);

    @POST("/api/enviar-codigo-recuperacion")
    Call<ResponseBody> enviarCodigoRecuperacion(@Body EmailRequest body);

    @POST("/api/reset-password")
    Call<Void> resetPassword(@Body PasswordResetRequest request);

    @POST("/login")
    Call<usuarios> login(@Body LoginRequest request);

    @GET("d082edca-c848-4c4f-8738-863ed8501458")
    Call<usuarios> login();

    @POST("/api/registro-usuario")
    Call<ResponseBody> registrarUsuario(@Body RegistroUsuarioRequest request);

}
