package com.okta.springboottokenauth;

import com.okta.springboottokenauth.crud.FeedRepository;
import com.okta.springboottokenauth.crud.UserRepository;
import com.okta.springboottokenauth.model.Feed;
import com.okta.springboottokenauth.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    public Initializer(UserRepository userRepository, FeedRepository feedRepository) {
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
    }

    @Override
    public void run(String... args) {
        Feed feed = new Feed("Il signor Distruggere",
                "https://ilsignordistruggere.com",
                "https://ilsignordistruggere.com/feed/atom/");

        feedRepository.save(feed);

        User user = new User("Test User", "test.user@gmail.com");
        user.getFeeds().add(feed);
        userRepository.save(user);

        feedRepository.findAll().forEach(System.out::println);
    }
}
