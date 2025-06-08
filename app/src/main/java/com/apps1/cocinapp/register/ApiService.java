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

    @POST("/api/verificar-codigo")
    Call<ResponseBody> verificarCodigo(@Body CodigoVerificacionRequest body);

    @POST("/login")
    Call<usuarios> login(@Body LoginRequest request);

    @GET("d082edca-c848-4c4f-8738-863ed8501458")
    Call<usuarios> login();
}
