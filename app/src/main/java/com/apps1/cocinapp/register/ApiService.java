package com.apps1.cocinapp.register;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/auth/verificar")
    Call<Void> verificarUsuario(@Body RegistroRequest request);

    @POST("/api/verificar-codigo")
    Call<ResponseBody> verificarCodigo(@Body CodigoVerificacionRequest body);

}
