package com.apps1.cocinapp.utils;

import android.util.Base64;

import org.json.JSONObject;

public class JwtUtils {

    public static JSONObject decodificarPayload(String token) {
        try {
            String[] partes = token.split("\\.");
            if (partes.length != 3) return null;

            String payload = partes[1];
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
            String json = new String(decodedBytes, "UTF-8");
            return new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
