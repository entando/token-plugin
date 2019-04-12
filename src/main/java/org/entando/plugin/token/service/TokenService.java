package org.entando.plugin.token.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.entando.plugin.token.config.ApplicationProperties;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final ApplicationProperties applicationProperties;
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public TokenService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public ZonedDateTime generateExpirationTime() {
        return ZonedDateTime.now().plusHours(this.applicationProperties.getTokenExpirationInHours());
    }

    public String generateToken() throws TokenGenerationException {
        MessageDigest salt;
        try {
            salt = MessageDigest.getInstance("SHA-256");
            salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
            return bytesToHex(salt.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new TokenGenerationException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
        return new String(hexChars);
    }
    
    public static class TokenGenerationException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public TokenGenerationException(Throwable throwable) {
            super(throwable);
        }
    }
}