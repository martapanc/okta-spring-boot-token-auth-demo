package com.okta.springboottokenauth.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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

}
