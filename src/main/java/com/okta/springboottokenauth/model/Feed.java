package com.okta.springboottokenauth.model;

import com.rometools.rome.feed.synd.SyndFeed;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "feed")
@AllArgsConstructor
public class Feed {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @OneToMany(targetEntity = Author.class, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Author> authors;

    @Column(length = 10000)
    private String description;

    @OneToMany(targetEntity = Entry.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Entry> entries;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Image image;

    @Column(nullable = false)
    private String siteUrl;

    @Column(nullable = false)
    private String feedUrl;

    @ManyToMany(mappedBy = "feeds", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public Feed() {
    }

    public Feed(String title, String siteUrl, String feedUrl) {
        this.title = title;
        this.siteUrl = siteUrl;
        this.feedUrl = feedUrl;
    }

    public Feed(SyndFeed syndFeed, String feedUrl) {
        this.authors = syndFeed.getAuthors().stream().map(Author::new).collect(Collectors.toList());
        this.description = syndFeed.getDescription();
        this.entries = syndFeed.getEntries().stream().map(Entry::new).collect(Collectors.toList());
        this.image = Optional.ofNullable(syndFeed.getImage()).map(Image::new).orElse(null);
        this.siteUrl = syndFeed.getLink();
        this.feedUrl = feedUrl;
        this.title = syndFeed.getTitle();
    }

    @Override
    public String toString() {
        return "Feed{" +
                ", authors=" + authors +
                ", description='" + description + '\'' +
                ", entries=" + entries +
                ", image=" + image +
                ", feedUrl='" + feedUrl + '\'' +
                ", siteUrl='" + siteUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getFeedUrl() {
        return feedUrl;
    }
}
