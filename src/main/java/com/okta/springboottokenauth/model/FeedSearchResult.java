package com.okta.springboottokenauth.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedSearchResult {

    private String description;
    @SerializedName("feedId")
    private String feedUrl;
    private String iconUrl;
    private String language;
    private Date lastUpdated;
    private long subscribers;
    private List<Tag> tags;
    private String websiteTitle;
    @SerializedName("website")
    private String websiteUrl;

    public void validateFeedUrl() {
        feedUrl = feedUrl.replace("feed/", "");
    }

    public void addTags(JsonElement jsonElement) {
        Gson gson = new Gson();
        LinkedTreeMap<String, Integer> list = gson.fromJson(jsonElement.getAsJsonObject(),
                new TypeToken<LinkedTreeMap<String, Integer>>() {}.getType());

        this.tags = list.keySet().stream().map(Tag::new).collect(Collectors.toList());
    }
}
