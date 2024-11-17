package io.github.lucianoawayand.lubank_app.domain.user;

public class LoginRequestDTO {
    public String govRegCode;
    public String password;

    public LoginRequestDTO(String govRegCode, String password) {
        this.govRegCode = govRegCode;
        this.password = password;
    }
}