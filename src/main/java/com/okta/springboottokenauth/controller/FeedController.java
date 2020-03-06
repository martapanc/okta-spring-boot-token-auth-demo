package com.okta.springboottokenauth.controller;

import com.okta.springboottokenauth.crud.FeedRepository;
import com.okta.springboottokenauth.feed.Reader;
import com.okta.springboottokenauth.model.Feed;
import com.rometools.rome.io.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(path="/feed")
public class FeedController {

    @Autowired
    private FeedRepository feedRepository;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public @ResponseBody String createFeed(@RequestBody Map<String, String> payload) throws IOException, FeedException {

        Feed feed = Reader.readFeed(payload.get("feed_url"));
        feedRepository.save(feed);
        return "New feed created: ";
    }
}
