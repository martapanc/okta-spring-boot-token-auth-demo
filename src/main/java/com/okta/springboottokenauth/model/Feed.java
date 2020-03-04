package com.okta.springboottokenauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feed {

    private String title;
    private List<String> authors;
    private String description;
    private List<Entry> entries;
    private URL link;
    private Image image;

}
