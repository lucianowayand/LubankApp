package io.github.lucianoawayand.lubank_app.Register.domain;

public class CreateUserRequestDto {
    public String govRegCode;
    public String password;
    public String email;
    public String name;

    public CreateUserRequestDto(String govRegCode, String password, String email, String name) {
        this.govRegCode = govRegCode;
        this.password = password;
        this.name = name;
        this.email = email;
    }
}
