package com.okta.springboottokenauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedSearchResult {

    private String description;
    private String feedUrl;
    private String iconUrl;
    private Timestamp lastUpdated;
    private long subscribers;
    private List<Tag> tags;
    private String websiteTitle;
    private String websiteUrl;

}
