package org.entando.plugin.token.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.plugin.token.TokenPluginApp;
import org.entando.plugin.token.domain.Token;
import org.entando.plugin.token.domain.enumeration.TokenType;
import org.entando.plugin.token.repository.RestTokenRepository;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = TokenPluginApp.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestTokenRepositoryTest {

    private final String DEFAULT_USERNAME = "myUsername";
    private final TokenType DEFAULT_TOKEN_TYPE = TokenType.REGISTRATION;
    private final String DEFAULT_TOKEN_VALUE = "123wefasdKJLijlk123";
    private final ZonedDateTime DEFAULT_EXPIRATION_TIME = ZonedDateTime.now().plusDays(6);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private RestTokenRepository tokenRepository;

    @Autowired
    ObjectMapper mapper;

    private Token token;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Before
    public void initTest() {
        token = new Token()
            .value(DEFAULT_TOKEN_VALUE)
            .type(DEFAULT_TOKEN_TYPE)
            .username(DEFAULT_USERNAME)
            .expirationTime(DEFAULT_EXPIRATION_TIME);
    }

    @Test
    @Transactional
    @WithMockUser("spring")
    public void createFullToken() throws Exception {
        int numberOfTokensBeforeCreation = (int) tokenRepository.count();

        mockMvc.perform(post("/tokens")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(mapper.writeValueAsString(token)))
            .andExpect(status().isCreated());

        Iterable<Token> tokenList = tokenRepository.findAll();
        assertThat(tokenList).hasSize(numberOfTokensBeforeCreation + 1);
        Token testToken = tokenList.iterator().next();
        assertThat(testToken.getValue()).isEqualTo(DEFAULT_TOKEN_VALUE);
        assertThat(testToken.getType()).isEqualTo(DEFAULT_TOKEN_TYPE);
        assertThat(testToken.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testToken.getExpirationTime()).isEqualTo(DEFAULT_EXPIRATION_TIME);
    }

    @Test
    @Transactional
    @WithMockUser("spring")
    public void createTokenWithoutValueAndExpiration() throws Exception {

        Token partialToken = new Token()
            .username("Test")
            .type(DEFAULT_TOKEN_TYPE);

        mockMvc.perform(post("/tokens")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(mapper.writeValueAsString(partialToken)))
            .andExpect(status().isCreated());
//            .andExpect(jsonPath("$.value").value(IsNull.notNullValue()))
//            .andExpect(jsonPath("$.expirationTime").value(IsNull.notNullValue()));

        Iterable<Token> tokens = tokenRepository.findAll();
        Token testToken = tokens.iterator().next();
        assertThat(testToken.getValue()).isNotNull();
        assertThat(testToken.getExpirationTime()).isNotNull();
    }

}
