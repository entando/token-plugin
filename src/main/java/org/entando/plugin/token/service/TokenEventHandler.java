package org.entando.plugin.token.service;

import org.entando.plugin.token.domain.Token;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

@RepositoryEventHandler
public class TokenEventHandler {

    private final TokenService tokenService;

    public TokenEventHandler(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @HandleBeforeCreate
    public void handleTokenCreate(Token token) {
        if (hasNoTokenValue(token)) {
            token.setValue(tokenService.generateToken());
        }

        if (hasNoExpirationTime(token)) {
            token.setExpirationTime(tokenService.generateExpirationTime());
        }
    }

    private boolean hasNoExpirationTime(Token token) {
        return null == token.getExpirationTime();
    }

    private boolean hasNoTokenValue(Token token) {
        return null == token.getValue() || "".equalsIgnoreCase(token.getValue());
    }

}
