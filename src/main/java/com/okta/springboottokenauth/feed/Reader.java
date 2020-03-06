package com.okta.springboottokenauth.feed;

import com.okta.springboottokenauth.model.Feed;
import com.okta.springboottokenauth.model.FeedSearchResult;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    public static Feed readFeed(String url) throws IOException, FeedException {
        URL feedSource = new URL(url);
        SyndFeedInput feedInput = new SyndFeedInput();
        return new Feed(feedInput.build(new XmlReader(feedSource)), url);
    }

    public static Feed readFeed(FeedSearchResult feedSearchResult) throws IOException, FeedException {
        String feedUrl = feedSearchResult.getFeedUrl();
        URL feedSource = new URL(feedUrl);
        SyndFeedInput feedInput = new SyndFeedInput();
        return new Feed(feedInput.build(new XmlReader(feedSource)), feedUrl);
    }

    public static List<String> findFeedUrlsInWebsite(String url) throws IOException {
        List<String> feedUrlList = new ArrayList<>();

        Document document = Jsoup.connect(url).get();
        Elements rssUrls = document.select("link[type=application/rss+xml]");
        Elements atom = document.select("link[type=application/atom+xml]");

        rssUrls.forEach(u -> feedUrlList.add(u.attr("abs:href")));
        atom.forEach(u -> feedUrlList.add(u.attr("abs:href")));

        return feedUrlList;
    }
}
