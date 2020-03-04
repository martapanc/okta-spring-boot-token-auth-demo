package com.okta.springboottokenauth.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.okta.springboottokenauth.model.FeedSearchResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class SearchFeed {

    private static final int TIMEOUT = 3000;
    private static final String BASE_URL = "https://cloud.feedly.com/v3/";
    private static final String SEARCH_ENDPOINT = "search/feeds?query=%s";

    public String searchFeed(String query) throws IOException {

        try {
            HttpURLConnection connection = sendRequest(query);
            String response = readResponse(connection);

            FeedSearchResult feedSearchResult = deserialiseResponseToObject(response);

            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    private FeedSearchResult deserialiseResponseToObject(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Date.class,
                (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        Gson gson = gsonBuilder.create();

        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

        FeedSearchResult feedSearchResult = gson.fromJson(jsonObject.getAsJsonArray("results").get(0), FeedSearchResult.class);

        return feedSearchResult;
    }

    private URL createUrl(String query) throws MalformedURLException {

        try {
            return new URL(String.format(BASE_URL + SEARCH_ENDPOINT, query));

        } catch (MalformedURLException e) {
            System.out.println("Error parsing URL: " + e.getMessage());
            throw new MalformedURLException();
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {

        try {
            int status = connection.getResponseCode();

            Reader streamReader;
            if (status > 299) {
                streamReader = new InputStreamReader(connection.getErrorStream());
            } else {
                streamReader = new InputStreamReader(connection.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            connection.disconnect();
            in.close();

            return content.toString();

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    private HttpURLConnection sendRequest(String query) throws IOException {

        try {
            URL url = createUrl(query);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            return con;

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }
}
