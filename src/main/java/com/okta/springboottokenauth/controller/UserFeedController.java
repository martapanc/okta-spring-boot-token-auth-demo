package com.okta.springboottokenauth.controller;

import com.okta.springboottokenauth.crud.FeedRepository;
import com.okta.springboottokenauth.crud.UserRepository;
import com.okta.springboottokenauth.model.Feed;
import com.okta.springboottokenauth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/user/feed")
public class UserFeedController {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(path = "/list", method = RequestMethod.POST)
    public ResponseEntity listUserFeed(@RequestBody Map<String, String> payload) {

        String user_email = payload.get("user_email");

        User user = getUserByEmail(user_email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found: " + user_email);
        }

        List<Feed> feedList = feedRepository.findFeedsByUserEmail(user_email);

        return ResponseEntity.ok(feedList);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseEntity addUserFeed(@RequestBody Map<String, String> payload) {

        String user_email = payload.get("user_email");

        User user = getUserByEmail(user_email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found: " + user_email);
        }

        String feed_url = payload.get("feed_url");
        Feed feed = feedRepository.findByFeedUrl(feed_url);

        if(user.getFeeds().contains(feed)){
            return ResponseEntity.ok("User collection already contains feed " + feed_url);
        }

        user.getFeeds().add(feed);
        userRepository.save(user);

        return ResponseEntity.ok("Feed " + feed_url + " was added to user collection.");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public ResponseEntity deleteUserFeed(@RequestBody Map<String, String> payload) {

        String user_email = payload.get("user_email");

        User user = getUserByEmail(user_email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found: " + user_email);
        }

        String feed_url = payload.get("feed_url");
        Feed feed = feedRepository.findByFeedUrl(feed_url);

        if (user.getFeeds().contains(feed)) {
            user.getFeeds().remove(feed);
            userRepository.save(user);
            return ResponseEntity.ok("Feed " + feed_url + " was removed from user collection.");
        }
        return ResponseEntity.ok("Feed was not found in user collection");
    }

    private User getUserByEmail(String user_email) {
        return userRepository.findByEmail(user_email);
    }

}
