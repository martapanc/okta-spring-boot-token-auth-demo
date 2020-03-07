package com.okta.springboottokenauth.crud;

import com.okta.springboottokenauth.model.Feed;
import com.okta.springboottokenauth.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends CrudRepository<Feed, Long> {

    Feed findByFeedUrl(String feedUrl);

    @Query("SELECT DISTINCT f FROM Feed f WHERE f.siteUrl LIKE CONCAT('%',:siteUrl,'%')")
    List<Feed> findBySimilarFeedSite(@Param("siteUrl")String siteUrl);

    List<Feed> findByUsers_email(String email);

//    @Query("select f from Feed f join User u where u.email = :email")
//    List<Feed> findByUserEmail(@Param("email") String email);


}
