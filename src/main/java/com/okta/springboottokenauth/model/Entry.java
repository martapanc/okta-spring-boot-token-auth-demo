package com.okta.springboottokenauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.net.URL;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ElementCollection
    private List<String> authors;
    @ElementCollection
    private List<String> categories;
    @ElementCollection
    private List<String> contents;
    private String description;
    private String title;
    private URL uri;
}
