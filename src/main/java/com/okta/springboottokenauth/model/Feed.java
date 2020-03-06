package com.okta.springboottokenauth.model;

import com.rometools.rome.feed.synd.SyndFeed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @OneToMany(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Author> authors;
    @Column( length = 10000 )
    private String description;
    @OneToMany(targetEntity = Entry.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Entry> entries;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Image image;
    private String link;
    private String title;

    public Feed(SyndFeed syndFeed) {
        this.authors = syndFeed.getAuthors().stream().map(Author::new).collect(Collectors.toList());
        this.description = syndFeed.getDescription();
        this.entries = syndFeed.getEntries().stream().map(Entry::new).collect(Collectors.toList());
        this.image = new Image(syndFeed.getImage());
        this.link = syndFeed.getLink();
        this.title = syndFeed.getTitle();
    }
}
