package io.github.lucianoawayand.lubank_app.shared.domain;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("govRegCode")
    public String govRegCode;
    @SerializedName("account")
    public int account;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGovRegCode() {
        return govRegCode;
    }

    public int getAccount() {
        return account;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", govRegCode='" + govRegCode + '\'' +
                ", account=" + account +
                '}';
    }
}
