package io.github.lucianoawayand.lubank_app.shared.domain.user;

public class LoginResponseDTO {
    public String token;
    public User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}