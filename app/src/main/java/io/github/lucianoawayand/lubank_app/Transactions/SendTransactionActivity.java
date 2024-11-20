package io.github.lucianoawayand.lubank_app.Transactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.github.lucianoawayand.lubank_app.Home.HomeActivity;
import io.github.lucianoawayand.lubank_app.Main.MainActivity;
import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.Register.RegisterActivity;
import io.github.lucianoawayand.lubank_app.Register.domain.CreateUserRequestDto;

public class SendTransactionActivity extends AppCompatActivity {
    private Button backButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialSetup();

        backButton = findViewById(R.id.return_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendTransactionActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
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