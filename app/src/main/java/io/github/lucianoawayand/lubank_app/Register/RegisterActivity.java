package io.github.lucianoawayand.lubank_app.Register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.github.lucianoawayand.lubank_app.Home.HomeActivity;
import io.github.lucianoawayand.lubank_app.Main.MainActivity;
import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.Register.domain.CreateUserRequestDto;
import io.github.lucianoawayand.lubank_app.Register.domain.CreateUserResponseDto;
import io.github.lucianoawayand.lubank_app.shared.config.RetrofitClient;
import io.github.lucianoawayand.lubank_app.shared.domain.User;
import io.github.lucianoawayand.lubank_app.shared.services.UserService;
import io.github.lucianoawayand.lubank_app.shared.utils.MaskEditUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private Button returnButton;
    private FrameLayout progressBarOverlay;
    private EditText govRegCodeInput;
    private EditText passwordInput;
    private EditText emailInput;
    private EditText nameInput;
    private TextWatcher textWatcher;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialSetup();

        userService = RetrofitClient.getClient(this).create(UserService.class);

        registerButton = findViewById(R.id.register_button);
        returnButton = findViewById(R.id.return_button);
        govRegCodeInput = findViewById(R.id.govRegCode_input);
        passwordInput = findViewById(R.id.password_input);
        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        progressBarOverlay = findViewById(R.id.progressOverlay);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String govRegCode = govRegCodeInput.getText().toString().replaceAll("[.\\-]", "");
                CreateUserRequestDto createUserDto = new CreateUserRequestDto(govRegCode, passwordInput.getText().toString(), emailInput.getText().toString(), nameInput.getText().toString());

                performRegistration(createUserDto);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initialSetup() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void performRegistration(CreateUserRequestDto dto) {
        progressBarOverlay.setVisibility(View.VISIBLE);

        Call<CreateUserResponseDto> call = userService.register(dto);
        call.enqueue(new Callback<CreateUserResponseDto>() {
            @Override
            public void onResponse(Call<CreateUserResponseDto> call, Response<CreateUserResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CreateUserResponseDto createResponse = response.body();
                    String token = createResponse.getToken();
                    User user = createResponse.getUser();

                    // Store JWT token and user info in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("jwt_token", token);
                    editor.putString("user", user.toString());
                    editor.apply();

                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        // Log the error response body (if available) as a string
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("LUBANK", "Error response: " + errorBody);
                        progressBarOverlay.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "Register failed: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("LUBANK", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateUserResponseDto> call, Throwable t) {
                Log.e("LUBANK", "Network error:" + t.getMessage());
                progressBarOverlay.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Register failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}