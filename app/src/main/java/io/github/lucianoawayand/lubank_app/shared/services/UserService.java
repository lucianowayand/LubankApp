package io.github.lucianoawayand.lubank_app.shared.services;
import io.github.lucianoawayand.lubank_app.Main.domain.LoginRequestDto;
import io.github.lucianoawayand.lubank_app.Main.domain.LoginResponseDto;
import io.github.lucianoawayand.lubank_app.Register.domain.CreateUserRequestDto;
import io.github.lucianoawayand.lubank_app.Register.domain.CreateUserResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("api/v1/users/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto request);

    @POST("api/v1/users/register")
    Call<CreateUserResponseDto> register(@Body CreateUserRequestDto request);
}


