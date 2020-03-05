package com.okta.springboottokenauth.controller;

import com.okta.springboottokenauth.crud.FeedRepository;
import com.okta.springboottokenauth.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path="/feed")
public class FeedController {

    @Autowired
    private FeedRepository feedRepository;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public @ResponseBody String createFeed(@RequestBody Map<String, Object> payload) {

        Feed feed = new Feed();
        feedRepository.save(feed);
        return "New feed created: ";
    }
}
