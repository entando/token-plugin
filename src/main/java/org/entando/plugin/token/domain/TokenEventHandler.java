package org.entando.plugin.token.domain;

import org.entando.plugin.token.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import java.time.ZonedDateTime;

@RepositoryEventHandler
public class TokenEventHandler {

    private final ApplicationProperties applicationProperties;

    @Autowired
    public TokenEventHandler(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @HandleBeforeCreate
    public void handleTokenCreate(Token token) {
       if (hasNoExpirationTime(token))  {
           token.setExpirationTime(getDefaultExpirationDate());
       }
    }

    private ZonedDateTime getDefaultExpirationDate() {
        return ZonedDateTime.now().plusHours(this.applicationProperties.getTokenExpirationInHours());
    }

    private boolean hasNoExpirationTime(Token token){
        return null == token.getExpirationTime();
    }

}

