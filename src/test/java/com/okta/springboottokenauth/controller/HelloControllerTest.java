package com.okta.springboottokenauth.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.okta.springboottokenauth.Application;
import com.rometools.rome.io.impl.Base64;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private static final String ID = "0oa2yaplcvc3pw5aa4x6";
    private static final String SECRET = "KsGMIteBpeTai5g2-0BWYuJeIfwVnoPt5uvl5JPp";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void shouldReturnForbidden_whenNoAuthIsUsed() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString(
                        "{\"error\":\"unauthorized\"," +
                                "\"error_description\":\"Full authentication is required to access this resource\"}")));
    }

    @Test
    public void shouldReturnUsername_WhenOauthIsUsed() throws Exception {
        this.mockMvc.perform(
                get("/")
                        .header("Authorization", "Bearer " + obtainAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello " + ID)));
    }

    private String obtainAccessToken() throws Exception {
        String userPass = ID + ":" + SECRET;

        byte[] postDataBytes = getPostDataBytes();

        HttpURLConnection con = (HttpURLConnection) new URL("https://dev-506887.okta.com/oauth2/default/v1/token").openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        con.setRequestProperty("Authorization", "Basic " + new String(Base64.encode(userPass.getBytes())));
        con.setDoOutput(true);
        con.getOutputStream().write(postDataBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String content = in.lines().collect(Collectors.joining());

        con.disconnect();
        in.close();

        return new Gson().fromJson(content, JsonObject.class).get("access_token").getAsString();
    }

    private byte[] getPostDataBytes() {
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