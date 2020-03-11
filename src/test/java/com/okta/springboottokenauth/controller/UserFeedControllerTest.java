package com.okta.springboottokenauth.controller;

import com.okta.springboottokenauth.Application;
import com.okta.springboottokenauth.crud.FeedRepository;
import com.okta.springboottokenauth.crud.UserRepository;
import com.okta.springboottokenauth.feed.Reader;
import com.okta.springboottokenauth.model.Feed;
import com.okta.springboottokenauth.model.User;
import com.okta.springboottokenauth.utils.Authorization;
import com.rometools.rome.io.FeedException;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserFeedControllerTest {

    private static final String FEED_URL_1 = "https://ilsignordistruggere.com/feed/atom/";
    private static final String FEED_URL_2 = "http://feeds.feedburner.com/Disinformatico/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FeedRepository feedRepository;

    @InjectMocks
    private User user = new User("Test user", "test.user@gmail.com");

    @Before
    public void setUp() throws IOException, FeedException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();

        MockitoAnnotations.initMocks(this);

        doReturn(user).when(userRepository).findByEmail("test.user@gmail.com");

        List<Feed> feedList = new ArrayList<>();
        Feed feed = Reader.readFeed(FEED_URL_1);
        feedList.add(feed);

        doReturn(feed).when(feedRepository).findByFeedUrl(FEED_URL_2);
        doReturn(feedList).when(feedRepository).findFeedsByUserEmail("test.user@gmail.com");
        when(userRepository.save(user)).thenReturn(user);
    }

    @Test
    public void shouldReturnDefaultListOfUserFeeds() throws Exception {
        String response = "[{\"feedUrl\": \"https://ilsignordistruggere.com/feed/atom/\"}]";

        String listRequestBody = "{\"user_email\": \"test.user@gmail.com\"}";

        this.mockMvc.perform(
                post("/user/feed/list")
                        .header("Authorization", "Bearer " + Authorization.obtainAccessToken())
                        .header("Content-Type", "application/json")
                        .content(listRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    public void shouldReturn400Error_whenListingFeeds_IfUserIsNotFound() throws Exception {
        String listRequestBody_UserNotFound = "{\"user_email\": \"not.found@gmail.com\"}";

        this.mockMvc.perform(
                post("/user/feed/list")
                        .header("Authorization", "Bearer " + Authorization.obtainAccessToken())
                        .header("Content-Type", "application/json")
                        .content(listRequestBody_UserNotFound))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found: not.found@gmail.com"));
    }

    @Test
    public void shouldReturnUpdatedListOfUserFeeds_afterAddingANewFeed() throws Exception {
        String feedUrl = FEED_URL_2;
        String cudRequestBody = "{\"user_email\": \"test.user@gmail.com\", \"feed_url\": \"" + feedUrl + "\"}";

        this.mockMvc.perform(
                post("/user/feed/add")
                        .header("Authorization", "Bearer " + Authorization.obtainAccessToken())
                        .header("Content-Type", "application/json")
                        .content(cudRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Feed " + feedUrl + " was added to user collection."));
    }

    @Test
    public void shouldReturn400Error_whenAddingUserFeed_IfUserIsNotFound() throws Exception {
        String cudRequestBody_UserNotFound = "{\"user_email\": \"not.found@gmail.com\", \"feed_url\": \"" + FEED_URL_2 + "\"}";

        this.mockMvc.perform(
                post("/user/feed/add")
                        .header("Authorization", "Bearer " + Authorization.obtainAccessToken())
                        .header("Content-Type", "application/json")
                        .content(cudRequestBody_UserNotFound))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found: not.found@gmail.com"));
    }

    @Test
    public void shouldReturn400Error_whenAddingUserFeed_IfUrlIsMalformed() throws Exception {
        String cudRequestBody_BadFeedUrl = "{\"user_email\": \"test.user@gmail.com\", \"feed_url\": \"feeds.feedburner.com/Disinformatico\"}";

        this.mockMvc.perform(
                post("/user/feed/add")
                        .header("Authorization", "Bearer " + Authorization.obtainAccessToken())
                        .header("Content-Type", "application/json")
                        .content(cudRequestBody_BadFeedUrl))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("IOException: no protocol: feeds.feedburner.com/Disinformatico"));
    }

    @Test
    public void shouldReturn400Error_whenAddingUserFeed_IfUrlIsNotAFeed() throws Exception {
        String cudRequestBody_UrlNotAFeed = "{\"user_email\": \"test.user@gmail.com\", \"feed_url\": \"https://attivissimo.blogspot.com/\"}";

        this.mockMvc.perform(
                post("/user/feed/add")
                        .header("Authorization", "Bearer " + Authorization.obtainAccessToken())
                        .header("Content-Type", "application/json")
                        .content(cudRequestBody_UrlNotAFeed))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error creating feed: Invalid XML: Error on line 1: DOCTYPE is disallowed when " +
                        "the feature \"http://apache.org/xml/features/disallow-doctype-decl\" set to true."));
    }

    @Test
    public void shouldReturnMessage_whenUserFeedsAlreadyContainFeed() throws Exception {
        String cudRequestBody_FeedAlreadyPresent = "{\"user_email\": \"test.user@gmail.com\", \"feed_url\": \"" + FEED_URL_1 + "\"}";

        this.mockMvc.perform(
                post("/user/feed/add")
                        .header("Authorization", "Bearer " + Authorization.obtainAccessToken())
                        .header("Content-Type", "application/json")
                        .content(cudRequestBody_FeedAlreadyPresent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("User collection already contains feed " + FEED_URL_1));
    }
}