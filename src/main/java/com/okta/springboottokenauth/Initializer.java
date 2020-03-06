package com.okta.springboottokenauth;

import com.okta.springboottokenauth.crud.UserRepository;
import com.okta.springboottokenauth.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    private final UserRepository repository;

    public Initializer(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        User user = new User("Test User", "test.user@gmail.com");
        repository.save(user);
        repository.findAll().forEach(System.out::println);
    }
}
