package com.okta.springboottokenauth.controller;

import com.okta.springboottokenauth.Application;
import com.okta.springboottokenauth.utils.Authorization;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private static final String ID = "0oa2yaplcvc3pw5aa4x6";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void shouldReturnForbidden_whenNoAuthIsUsed() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString(
                        "{\"error\":\"unauthorized\"," +
                                "\"error_description\":\"Full authentication is required to access this resource\"}")));
    }

    @Test
    public void shouldReturnUsername_WhenOauthIsUsed() throws Exception {
        this.mockMvc.perform(
                get("/")
                        .header("Authorization", "Bearer " + Authorization.obtainAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello " + ID)));
    }
}