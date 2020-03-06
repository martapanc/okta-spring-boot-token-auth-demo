package com.okta.springboottokenauth.model;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @OneToMany(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)    private List<Author> authors;
    @ElementCollection
    private List<String> categories;
    @ElementCollection
    @Column( length = 100000 )
    private List<String> contents;
    @Column( length = 10000 )
    private String description;
    private String title;
    private String url;

    public Entry(SyndEntry syndEntry) {
        this.authors = syndEntry.getAuthors().stream().map(Author::new).collect(Collectors.toList());
        this.categories = syndEntry.getCategories().stream().map(SyndCategory::getName).collect(Collectors.toList());
        this.contents = syndEntry.getContents().stream().map(SyndContent::getValue).collect(Collectors.toList());
        this.description = syndEntry.getDescription().getValue();
        this.title = syndEntry.getTitle();
        this.url = syndEntry.getLink();
    }
}
