package com.okta.springboottokenauth.crud;

import com.okta.springboottokenauth.model.Feed;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends CrudRepository<Feed, Long> {

    Feed findByFeedUrl(String feedUrl);

    @Query("SELECT DISTINCT f FROM Feed f WHERE f.siteUrl LIKE CONCAT('%',:siteUrl,'%')")
    List<Feed> findBySimilarFeedSite(@Param("siteUrl")String siteUrl);
}
