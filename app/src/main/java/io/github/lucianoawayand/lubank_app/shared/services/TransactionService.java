package io.github.lucianoawayand.lubank_app.shared.services;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TransactionService {
    @GET("/api/v1/transactions/balance")
    Call<Double> getBalance();
}
