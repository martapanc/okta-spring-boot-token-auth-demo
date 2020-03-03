package com.okta.springboottokenauth.controller;

import com.okta.springboottokenauth.feed.Reader;
import com.rometools.rome.io.FeedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class FeedReaderController {

    @RequestMapping(value = "/read", method = POST)
    public Object readFeed(@RequestBody Map<String, Object> payload) throws IOException, FeedException {
        Object feed_url = payload.get("feed_url");

        return Reader.readFeed(feed_url.toString());
    }

    @RequestMapping(value = "/find", method = POST)
    public Object findFeedUrl(@RequestBody Map<String, Object> payload) throws IOException, FeedException {
        Object feed_url = payload.get("feed_url");

        return Reader.findFeedUrlsInWebsite(feed_url.toString());
    }
}
