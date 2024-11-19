package io.github.lucianoawayand.lubank_app.Register.domain;

import io.github.lucianoawayand.lubank_app.shared.domain.User;

public class CreateUserResponseDto {
    public String token;
    public User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
