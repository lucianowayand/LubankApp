package io.github.lucianoawayand.lubank_app.Main.domain;

public class LoginRequestDto {
    public String govRegCode;
    public String password;

    public LoginRequestDto(String govRegCode, String password) {
        this.govRegCode = govRegCode;
        this.password = password;
    }
}