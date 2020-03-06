package com.okta.springboottokenauth.model;

import com.rometools.rome.feed.synd.SyndPerson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    private String name;
    private String uri;

    public Author(SyndPerson syndPerson) {
        this.email = syndPerson.getEmail();
        this.name = syndPerson.getName();
        this.uri = syndPerson.getUri();
    }
}
