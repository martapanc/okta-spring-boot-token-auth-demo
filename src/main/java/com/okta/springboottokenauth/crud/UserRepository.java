package com.okta.springboottokenauth.crud;

import com.okta.springboottokenauth.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);
}
