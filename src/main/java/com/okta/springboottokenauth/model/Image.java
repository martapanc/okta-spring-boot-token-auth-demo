package com.okta.springboottokenauth.model;

import com.rometools.rome.feed.synd.SyndImage;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String description;

    private int height;

    @Column(nullable = false)
    private String url;

    private int weight;

    public Image() {}

    public Image(SyndImage syndImage) {
        this.description = syndImage.getDescription();
        this.height = syndImage.getHeight();
        this.url = syndImage.getUrl();
        this.weight = syndImage.getWidth();
    }
}
