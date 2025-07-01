package com.apps1.cocinapp.session;

import android.content.Context;
import android.content.SharedPreferences;
import com.auth0.android.jwt.JWT;


import com.apps1.cocinapp.login.LoginActivity;
import com.apps1.cocinapp.usuario.PerfilActivity;

import org.json.JSONObject;

public class SharedPreferencesHelper {
    private static final String PREF_NAME = "cocinapp_prefs";
    private static final String KEY_TOKEN = "auth_token";

    public static void guardarToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public static String obtenerToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }

    public static void eliminarToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_TOKEN).apply();
    }

    public static boolean hayToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, null);
        return token != null && !token.isEmpty();
    }

    public static String obtenerRolDelToken(Context context) {
        String token = obtenerToken(context);
        if (token == null || token.isEmpty()) return null;

        try {
            JWT jwt = new JWT(token);
            return jwt.getClaim("rol").asString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long obtenerIdUsuario(Context context) {
        String token = obtenerToken(context);
        if (token == null || token.isEmpty()) return null;

        try {
            JWT jwt = new JWT(token);
            return jwt.getClaim("id").asLong();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String obtenerTokenLimpio(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String jsonToken = prefs.getString("token", "");

        try {
            JSONObject jsonObject = new JSONObject(jsonToken);
            return jsonObject.getString("token");
        } catch (Exception e) {
            return ""; // en caso de error se devuelve vacio
        }
    }


}
