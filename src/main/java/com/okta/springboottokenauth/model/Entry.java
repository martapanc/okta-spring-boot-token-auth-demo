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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
public class Entry {

    @Id
    @Column(length = 200, nullable = false)
    private String title;

    @OneToMany(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Author> authors;

    @ElementCollection
    private List<String> categories;

    @ElementCollection
    @Column(length = 100000)
    private List<String> contents;

    @Column(length = 10000)
    private String description;

    private String url;

    public Entry() {}

    public Entry(SyndEntry syndEntry) {
        this.authors = syndEntry.getAuthors().stream().map(Author::new).collect(Collectors.toList());
        this.categories = syndEntry.getCategories().stream().map(SyndCategory::getName).collect(Collectors.toList());
        this.contents = syndEntry.getContents().stream().map(SyndContent::getValue).collect(Collectors.toList());
        this.description = Optional.ofNullable(syndEntry.getDescription()).map(SyndContent::getValue).orElse("");
        this.title = syndEntry.getTitle();
        this.url = syndEntry.getLink();
    }

    @Override
    public String toString() {
        return "Entry{" +
                "authors=" + authors +
                ", categories=" + categories +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
