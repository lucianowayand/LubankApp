package io.github.lucianoawayand.lubank_app.Transactions;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.github.lucianoawayand.lubank_app.Home.HomeActivity;
import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.Transactions.adapter.ListTransactionsAdapter;
import io.github.lucianoawayand.lubank_app.shared.config.RetrofitClient;
import io.github.lucianoawayand.lubank_app.shared.domain.Transaction;
import io.github.lucianoawayand.lubank_app.shared.domain.User;
import io.github.lucianoawayand.lubank_app.shared.services.TransactionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTransactionsActivity extends AppCompatActivity {
    private Button backButton;
    private RecyclerView recyclerView;
    private View progressOverlay;
    private ListTransactionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialSetup();

        recyclerView = findViewById(R.id.recyclerViewActions);
        progressOverlay = findViewById(R.id.progressOverlay);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchTransactions();

        backButton = findViewById(R.id.return_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToHome();
            }
        });
    }

    private void redirectToHome() {
        Intent intent = new Intent(ListTransactionsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void fetchTransactions() {
        showLoading(true);

        TransactionService transactionService = RetrofitClient.getClient(this).create(TransactionService.class);
        Call<List<Transaction>> call = transactionService.getTransactions();

        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(@NonNull Call<List<Transaction>> call, @NonNull Response<List<Transaction>> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

                    String userJson = sharedPreferences.getString("user", null);
                    User user = (userJson != null) ? gson.fromJson(userJson, User.class) : null;
                    if (user != null) {
                        Log.d("User", "User details: " + user.toString());
                    }
                    String currentUserId = user.getId();

                    List<Transaction> transactions = response.body();

                    // Sort transactions by date, most recent first
                    transactions.sort((t1, t2) -> {
                        try {
                            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date date1 = isoFormat.parse(t1.getCreatedAt());
                            Date date2 = isoFormat.parse(t2.getCreatedAt());
                            return date2.compareTo(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0; // If parsing fails, consider them equal
                        }
                    });

                    adapter = new ListTransactionsAdapter(transactions, currentUserId);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Transaction>> call, @NonNull Throwable t) {
                showLoading(false);
                // Handle failure
            }
        });
    }

    private void showLoading(boolean show) {
        progressOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void initialSetup() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_extract);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}