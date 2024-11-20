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

import io.github.lucianoawayand.lubank_app.Deposits.DepositsActivity;
import io.github.lucianoawayand.lubank_app.Home.model.Action;
import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.Home.adapter.ActionsAdapter;
import io.github.lucianoawayand.lubank_app.Transactions.SendTransactionActivity;
import io.github.lucianoawayand.lubank_app.UnderDevelopment.UnderDevelopmentActivity;
import io.github.lucianoawayand.lubank_app.shared.config.RetrofitClient;
import io.github.lucianoawayand.lubank_app.shared.domain.User;
import io.github.lucianoawayand.lubank_app.shared.services.TransactionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class HomeActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TransactionService transactionService;
    private TextView txtAccountBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        transactionService = RetrofitClient.getClient(this).create(TransactionService.class);

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
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);


        // Setup RecyclerView for Action cards
        RecyclerView recyclerView = findViewById(R.id.recyclerViewActions);
        ArrayList<Action> actions = createActionsList();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        ActionsAdapter actionsAdapter = new ActionsAdapter(this, actions);
        recyclerView.setAdapter(actionsAdapter);

        setupCardClickListener();

        setAccountBalance();

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
    }

    private void refreshData() {
        setAccountBalance();

        Log.d("Refreshing", "Data updated!");

        swipeRefreshLayout.setRefreshing(false);
    }

    private void setupCardClickListener() {
        View cardClickableLayout = findViewById(R.id.cardClickableLayout);
        cardClickableLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UnderDevelopmentActivity.class);
            startActivity(intent);
        });
    }

    private ArrayList<Action> createActionsList() {
        ArrayList<Action> actions = new ArrayList<>();

        actions.add(new Action(R.drawable.transfer, "Transferir", SendTransactionActivity.class));
        actions.add(new Action(R.drawable.ic_dollasign, "Fazer depósito", DepositsActivity.class));
        actions.add(new Action(R.drawable.barcode, "Pagar boleto", UnderDevelopmentActivity.class));
        actions.add(new Action(R.drawable.withdraw, "Sacar dinheiro", UnderDevelopmentActivity.class));

        return actions;
    }

    private void setAccountBalance() {
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
