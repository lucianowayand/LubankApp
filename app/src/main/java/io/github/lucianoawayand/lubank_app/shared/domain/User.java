package io.github.lucianoawayand.lubank_app.shared.domain;

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
}
