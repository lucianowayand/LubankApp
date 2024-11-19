package io.github.lucianoawayand.lubank_app.Main.domain;

import io.github.lucianoawayand.lubank_app.shared.domain.User;

public class LoginResponseDto {
    public String token;
    public User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}