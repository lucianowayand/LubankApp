package io.github.lucianoawayand.lubank_app.Transactions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.github.lucianoawayand.lubank_app.Home.HomeActivity;
import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.Transactions.domain.TransactionRequestDto;
import io.github.lucianoawayand.lubank_app.shared.config.RetrofitClient;
import io.github.lucianoawayand.lubank_app.shared.services.TransactionService;
import io.github.lucianoawayand.lubank_app.shared.utils.MaskEditUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendTransactionActivity extends AppCompatActivity {
    private Button backButton;
    private Button sendTransactionButton;
    private EditText amountInput;
    private EditText govRegCodeInput;
    private TextView balanceAmount;
    private FrameLayout progressBarOverlay;
    private TransactionService transactionService;
    private Double accountBalance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialSetup();

        transactionService = RetrofitClient.getClient(this).create(TransactionService.class);

        transactionService = RetrofitClient.getClient(this).create(TransactionService.class);
        setAccountBalance();

        backButton = findViewById(R.id.return_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToHome();
            }
        });

        amountInput = findViewById(R.id.ammount_input);
        amountInput.addTextChangedListener(MaskEditUtil.mask(amountInput, MaskEditUtil.FORMAT_MONEY));
        govRegCodeInput = findViewById(R.id.govRegCode_input);

        progressBarOverlay = findViewById(R.id.progressOverlay);
        sendTransactionButton = findViewById(R.id.submit_transaction);
        sendTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTransaction();
            }
        });


    }

    private void redirectToHome() {
        Intent intent = new Intent(SendTransactionActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void submitTransaction() {
        runOnUiThread(() -> {
            String receiverGovRegCode = govRegCodeInput.getText().toString().replaceAll("[.\\-]", "");
            Double transferAmount = MaskEditUtil.parseMoneyToDouble(amountInput.getText().toString());

            if (transferAmount > accountBalance) {
                Toast.makeText(SendTransactionActivity.this, "Não é possivel transferir mais que o saldo atual.", Toast.LENGTH_SHORT).show();
                return;
            } else if (transferAmount == 0) {
                Toast.makeText(SendTransactionActivity.this, "Não é possivel transferir valores nulos.", Toast.LENGTH_SHORT).show();
                return;
            } else if (receiverGovRegCode.isEmpty()) {
                Toast.makeText(SendTransactionActivity.this, "Preencha o campo de identificação do remetente.", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBarOverlay.setVisibility(View.VISIBLE);

            TransactionRequestDto transactionRequestDto = new TransactionRequestDto(receiverGovRegCode, transferAmount);

            Call<String> call = transactionService.sendTransaction(transactionRequestDto);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String rawResponse = response.body();

                        if ("OK".equalsIgnoreCase(rawResponse.trim())) {
                            Log.d("LUBANK", "Server responded with OK");
                            Toast.makeText(SendTransactionActivity.this, "Transferencia feita com sucesso!", Toast.LENGTH_LONG).show();

                            redirectToHome();
                        } else {
                            Log.e("LUBANK", "Unexpected response: " + rawResponse);
                            Toast.makeText(SendTransactionActivity.this, "Erro: " + rawResponse, Toast.LENGTH_LONG).show();
                            progressBarOverlay.setVisibility(View.GONE);
                        }
                    } else {
                        Log.e("LUBANK", "Response code: " + response.code());
                        Toast.makeText(SendTransactionActivity.this, "Erro: Remetente não existente ou saldo insuficiente.", Toast.LENGTH_LONG).show();
                        progressBarOverlay.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("LUBANK", "Network error:" + t.getMessage());
                    progressBarOverlay.setVisibility(View.GONE);
                    Toast.makeText(SendTransactionActivity.this, "Transferencia falhou: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setAccountBalance() {
        Call<Double> call = transactionService.getBalance();
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(@NonNull Call<Double> call, @NonNull Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    accountBalance = response.body();
                    String moneyAmount = (String) MaskEditUtil.formatMoney(accountBalance);

                    balanceAmount = findViewById(R.id.balance_ammount);
                    balanceAmount.setText(moneyAmount);
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