package com.okta.springboottokenauth.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedSearchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long feedResultId;

    private String description;

    @SerializedName("feedId")
    private String feedUrl;

    private String iconUrl;

    private String language;

    private Date lastUpdated;

    private long subscribers;

    @ElementCollection
    private List<String> tags;

    @Column(nullable = false)
    private String websiteTitle;

    @Column(nullable = false)
    @SerializedName("website")
    private String websiteUrl;

    public void validateFeedUrl() {
        feedUrl = feedUrl.replace("feed/", "");
    }

    public void addTags(JsonElement jsonElement) {
        Gson gson = new Gson();
        LinkedTreeMap<String, Integer> list = gson.fromJson(jsonElement.getAsJsonObject(),
                new TypeToken<LinkedTreeMap<String, Integer>>() {}.getType());

        this.tags = new ArrayList<>(list.keySet());
    }

    public String getFeedUrl() {
        return feedUrl;
    }
}
