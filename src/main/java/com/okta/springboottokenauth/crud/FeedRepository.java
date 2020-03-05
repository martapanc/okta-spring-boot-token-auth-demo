package com.okta.springboottokenauth.crud;

import com.okta.springboottokenauth.model.Feed;
import org.springframework.data.repository.CrudRepository;

public interface FeedRepository extends CrudRepository<Feed, Long> {
}
