package com.okta.springboottokenauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entry {

    private List<String> authors;
    private List<String> categories;
    private List<String> contents;
    private String description;
    private String title;
    private URL uri;
}
