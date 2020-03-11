package com.okta.springboottokenauth.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Authorization {

    private static final String USERPASS = "MG9hMnlhcGxjdmMzcHc1YWE0eDY6S3NHTUl0ZUJwZVRhaTVnMi0wQldZdUplSWZ3Vm5vUHQ1dXZsNUpQcA==";
    private static final String AUTH_URL = "https://dev-506887.okta.com/oauth2/default/v1/token";

    public static String obtainAccessToken() throws Exception {
        byte[] postDataBytes = getPostDataBytes();

        HttpURLConnection con = (HttpURLConnection) new URL(AUTH_URL).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        con.setRequestProperty("Authorization", "Basic " + USERPASS);
        con.setDoOutput(true);
        con.getOutputStream().write(postDataBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String content = in.lines().collect(Collectors.joining());

        con.disconnect();
        in.close();

        return new Gson().fromJson(content, JsonObject.class).get("access_token").getAsString();
    }

    private static byte[] getPostDataBytes() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("scope", "customScope");

        StringBuilder postData = new StringBuilder();
        params.forEach((key, value) -> {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8));
        });
        return postData.toString().getBytes(StandardCharsets.UTF_8);
    }
}
