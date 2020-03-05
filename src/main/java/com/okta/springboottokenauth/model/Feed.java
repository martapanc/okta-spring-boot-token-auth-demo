package com.okta.springboottokenauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.net.URL;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ElementCollection
    private List<String> authors;
    private String description;
    @OneToMany(targetEntity = Entry.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Entry> entries;
    @OneToOne
    private Image image;
    private URL link;
    private String title;

}
