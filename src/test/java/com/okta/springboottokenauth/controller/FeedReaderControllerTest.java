package com.okta.springboottokenauth.controller;

import com.okta.springboottokenauth.Application;
import com.okta.springboottokenauth.utils.Authorization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FeedReaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnListOfFeeds() throws Exception {
        String requestBody = "{\"feed_url\": \"http://feeds.feedburner.com/Disinformatico\"}";
        String responseBody = "{\"feedUrl\":\"http://feeds.feedburner.com/Disinformatico\"}";
        mockPostRequest("/read", requestBody, responseBody);
    }

    @Test
    public void shouldFindFeedUrlGivenWebsite() throws Exception {
        String requestBody = "{\"site_url\": \"https://riccardodalferro.com/\"}";
        String responseBody = "[\"https://riccardodalferro.com/feed/\",\"https://riccardodalferro.com/comments/feed/\"]";
        mockPostRequest("/find", requestBody, responseBody);
    }

    private void mockPostRequest(String endpoint, String requestBody, String responseBody) throws Exception {
        this.mockMvc.perform(
                post(endpoint)
                        .header("Authorization", "Bearer " + Authorization.obtainAccessToken())
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody));
    }
}
