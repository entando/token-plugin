package org.entando.plugin.token.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.entando.plugin.token.domain.enumeration.TokenType;

/**
 * A Token.
 */
@Entity
@Table(name = "token")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    
    @Column(name = "jhi_value", unique = true)
    private String value;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private TokenType type;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "expiration_time")
    private ZonedDateTime expirationTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public Token value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public Token type(TokenType type) {
        this.type = type;
        return this;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public Token username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ZonedDateTime getExpirationTime() {
        return expirationTime;
    }

    public Token expirationTime(ZonedDateTime expirationTime) {
        this.expirationTime = expirationTime;
        return this;
    }

    public void setExpirationTime(ZonedDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Token token = (Token) o;
        if (token.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), token.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Token{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", type='" + getType() + "'" +
            ", username='" + getUsername() + "'" +
            ", expirationTime='" + getExpirationTime() + "'" +
            "}";
    }
}
