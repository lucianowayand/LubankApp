package io.github.lucianoawayand.lubank_app.Transactions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.github.lucianoawayand.lubank_app.Home.HomeActivity;
import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.shared.config.RetrofitClient;
import io.github.lucianoawayand.lubank_app.shared.services.TransactionService;
import io.github.lucianoawayand.lubank_app.shared.utils.MaskEditUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendTransactionActivity extends AppCompatActivity {
    private Button backButton;
    private EditText ammountInput;
    private TextView balanceAmmount;
    private Double accountBalance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialSetup();

        TransactionService transactionService = RetrofitClient.getClient(this).create(TransactionService.class);
        setAccountBalance(transactionService);

        backButton = findViewById(R.id.return_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendTransactionActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ammountInput = findViewById(R.id.ammount_input);
        ammountInput.addTextChangedListener(MaskEditUtil.mask(ammountInput, MaskEditUtil.FORMAT_MONEY));


    }

    private void setAccountBalance(TransactionService transactionService) {
        Call<Double> call = transactionService.getBalance();
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(@NonNull Call<Double> call, @NonNull Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    accountBalance = response.body();
                    String moneyAmmount = (String) MaskEditUtil.formatMoney(accountBalance);

                    Log.e("LUBANK", accountBalance.toString() + " " + moneyAmmount);
                    balanceAmmount = findViewById(R.id.balance_ammount);
                    balanceAmmount.setText(moneyAmmount);
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

    private void initialSetup() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}