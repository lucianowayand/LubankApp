package io.github.lucianoawayand.lubank_app.Deposits;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.lucianoawayand.lubank_app.Home.HomeActivity;
import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.Transactions.SendTransactionActivity;
import io.github.lucianoawayand.lubank_app.UnderDevelopment.UnderDevelopmentActivity;
import io.github.lucianoawayand.lubank_app.shared.utils.MaskEditUtil;

public class DepositsActivity extends AppCompatActivity {
    private Button backButton;
    private Button continueButton;
    private Button cancelButton;
    private Button submitDepositButton;
    private EditText moneyInput;
    private TextView depositValueText;
    private TextView depositDate;
    private Double depositValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialSetup();

        moneyInput = findViewById(R.id.money_input);
        moneyInput.addTextChangedListener(MaskEditUtil.mask(moneyInput, MaskEditUtil.FORMAT_MONEY));

        backButton = findViewById(R.id.return_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToHome();
            }
        });

        continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositValue = MaskEditUtil.parseMoneyToDouble(moneyInput.getText().toString());

                if (depositValue > 0) {
                    setContentView(R.layout.activity_deposits_second_step);
                    setupStepTwo();
                } else {
                    Toast.makeText(DepositsActivity.this, "O valor de depósito deve ser maior que zero.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void redirectToHome() {
        Intent intent = new Intent(DepositsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void initialSetup() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deposits_first_step);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupStepTwo() {
        depositValueText = findViewById(R.id.deposit_value);
        depositValueText.setText(MaskEditUtil.formatMoney(depositValue));

        depositDate = findViewById(R.id.deposit_due_date);
        Date today = new Date();
        Date dueDate = new Date(today.getTime() + (1000 * 60 * 60 * 24 * 7));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(dueDate);
        depositDate.setText(formattedDate);

        submitDepositButton = findViewById(R.id.generate_deposit);
        submitDepositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepositsActivity.this, UnderDevelopmentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToHome();
            }
        });
    }
}