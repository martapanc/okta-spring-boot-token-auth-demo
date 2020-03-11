package com.okta.springboottokenauth.crud;

import com.okta.springboottokenauth.model.Feed;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FeedRepository extends CrudRepository<Feed, Long> {

    Feed findByFeedUrl(String feedUrl);

    @Query("SELECT DISTINCT f FROM Feed f WHERE f.siteUrl LIKE CONCAT('%',:siteUrl,'%')")
    List<Feed> findBySimilarFeedSite(@Param("siteUrl") String siteUrl);

    @Query("SELECT f FROM Feed f JOIN FETCH f.users AS u WHERE u.email=:email")
    Set<Feed> findFeedsByUserEmail(@Param("email") String email);
}
