package com.apps1.cocinapp.login;

import org.json.JSONException;
import org.json.JSONObject;

public class JwtUtils {
    public static JSONObject decodePayload(String jwt) throws JSONException {
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException("Token inválido");

        String payloadJson = new String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT));
        return new JSONObject(payloadJson);
    }
}
