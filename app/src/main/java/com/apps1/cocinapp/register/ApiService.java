package com.apps1.cocinapp.register;

import com.apps1.cocinapp.data.usuarios;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import okhttp3.ResponseBody;
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

}

