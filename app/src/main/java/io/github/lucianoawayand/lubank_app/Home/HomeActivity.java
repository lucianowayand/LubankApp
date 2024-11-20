package io.github.lucianoawayand.lubank_app.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.google.gson.Gson;


import io.github.lucianoawayand.lubank_app.Home.model.Action;
import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.Home.adapter.ActionsAdapter;
import io.github.lucianoawayand.lubank_app.UnderDevelopment.UnderDevelopmentActivity;
import io.github.lucianoawayand.lubank_app.shared.config.RetrofitClient;
import io.github.lucianoawayand.lubank_app.shared.domain.User;
import io.github.lucianoawayand.lubank_app.shared.services.TransactionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private TextView txtAccountBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TransactionService transactionService = RetrofitClient.getClient(this).create(TransactionService.class);

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        String userJson = sharedPreferences.getString("user", null);
        User user = (userJson != null) ? gson.fromJson(userJson, User.class) : null;
        if (user != null) {
            Log.d("User", "User details: " + user.toString());
        }

        TextView txtHelloUser = findViewById(R.id.txtHelloUser);
        String greeting = (user != null) ? "Olá " + user.getName() + "!" : "Olá, visitante!";
        txtHelloUser.setText(greeting);

        txtAccountBalance = findViewById(R.id.txtAccountBalance);
        setAccountBalance(transactionService);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewActions);
        ArrayList<Action> actions = createActionsList();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        ActionsAdapter actionsAdapter = new ActionsAdapter(this, actions);
        recyclerView.setAdapter(actionsAdapter);

        View cardClickableLayout = findViewById(R.id.cardClickableLayout);
        cardClickableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UnderDevelopmentActivity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Action> createActionsList() {
        ArrayList<Action> actions = new ArrayList<>();

        actions.add(new Action(R.drawable.ic_dollasign, "Fazer depósito", UnderDevelopmentActivity.class));
        actions.add(new Action(R.drawable.barcode, "Pagar boleto", UnderDevelopmentActivity.class));
        actions.add(new Action(R.drawable.withdraw, "Sacar dinheiro", UnderDevelopmentActivity.class));

        return actions;
    }

    private void setAccountBalance(TransactionService transactionService) {
        Call<Double> call = transactionService.getBalance();
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(@NonNull Call<Double> call, @NonNull Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double accountBalance = response.body();
                    String accountBalanceText = String.format("Conta \n\n R$%.2f", accountBalance);
                    txtAccountBalance.setText(accountBalanceText);
                } else {
                    Log.d("GetBalance", "Error getting balance");

                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Log.d("GetBalance", "Error getting balance");
            }
        });
    }
}
