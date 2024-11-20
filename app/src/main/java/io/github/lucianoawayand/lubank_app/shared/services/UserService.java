package io.github.lucianoawayand.lubank_app.shared.services;

import io.github.lucianoawayand.lubank_app.Main.domain.LoginRequestDto;
import io.github.lucianoawayand.lubank_app.Main.domain.LoginResponseDto;
import io.github.lucianoawayand.lubank_app.Register.domain.CreateUserRequestDto;
import io.github.lucianoawayand.lubank_app.Register.domain.CreateUserResponseDto;
import io.github.lucianoawayand.lubank_app.shared.domain.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @POST("api/v1/users/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto request);

    @POST("api/v1/users/register")
    Call<CreateUserResponseDto> register(@Body CreateUserRequestDto request);

    @GET("api/v1/users")
    Call<User> getUserInformation();

    @GET("/api/v1/transactions/balance")
    Call<Double> getBalance();
}


