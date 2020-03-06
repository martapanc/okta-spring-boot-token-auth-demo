package com.okta.springboottokenauth.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.okta.springboottokenauth.crud.FeedRepository;
import com.okta.springboottokenauth.model.Feed;
import com.okta.springboottokenauth.model.FeedSearchResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SearchFeed {

    private static final int TIMEOUT = 3000;
    private static final String BASE_URL = "https://cloud.feedly.com/v3/";
    private static final String SEARCH_ENDPOINT = "search/feeds?query=%s";
    private static final String GET = "GET";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String RESPONSE_TAG_COUNTS = "tagCounts";
    private static final String RESPONSE_RESULTS = "results";

    public String searchFeedly(String query) throws IOException {

        try {
            HttpURLConnection connection = sendRequest(query);
            return readResponse(connection);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    public List<Feed> searchFeed(String query, FeedRepository feedRepository) throws IOException {

        return feedRepository.findBySimilarFeedSite(query);
    }

    private List<FeedSearchResult> deserialiseResponseToObject(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .registerTypeAdapter(Date.class,
                        (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        Gson gson = gsonBuilder.create();

        JsonArray results = gson.fromJson(response, JsonObject.class).getAsJsonArray(RESPONSE_RESULTS);

        List<FeedSearchResult> feedSearchResultList = new ArrayList<>();
        for (JsonElement jsonElement : results) {
            feedSearchResultList.add(getFeedSearchResult(gson, jsonElement));
        }

        return feedSearchResultList;
    }

    private FeedSearchResult getFeedSearchResult(Gson gson, JsonElement jsonElement) {
        FeedSearchResult feedSearchResult = gson.fromJson(jsonElement, FeedSearchResult.class);

        feedSearchResult.validateFeedUrl();
        feedSearchResult.addTags(jsonElement.getAsJsonObject().get(RESPONSE_TAG_COUNTS));

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
            con.setRequestMethod(GET);
            con.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            return con;

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }
}
