package io.github.lucianoawayand.lubank_app.services;
import io.github.lucianoawayand.lubank_app.domain.user.LoginRequestDTO;
import io.github.lucianoawayand.lubank_app.domain.user.LoginResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("api/v1/users/login")
    Call<LoginResponseDTO> login(@Body LoginRequestDTO request);
}


