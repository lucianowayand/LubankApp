package io.github.lucianoawayand.lubank_app.shared.services;

import io.github.lucianoawayand.lubank_app.Transactions.domain.TransactionRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TransactionService {
    @GET("/api/v1/transactions/balance")
    Call<Double> getBalance();

    @POST("/api/v1/transactions")
    Call<String> sendTransaction(@Body TransactionRequestDto dto);
}
