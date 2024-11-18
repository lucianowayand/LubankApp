package io.github.lucianoawayand.lubank_app.Main;

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
import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.Register.RegisterActivity;
import io.github.lucianoawayand.lubank_app.shared.config.RetrofitClient;
import io.github.lucianoawayand.lubank_app.shared.domain.user.LoginRequestDTO;
import io.github.lucianoawayand.lubank_app.shared.domain.user.LoginResponseDTO;
import io.github.lucianoawayand.lubank_app.shared.domain.user.User;
import io.github.lucianoawayand.lubank_app.shared.services.UserService;
import io.github.lucianoawayand.lubank_app.shared.utils.MaskEditUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText govRegCodeInput;
    private EditText passwordInput;
    private RadioGroup radioGroupCpfCnpj;
    private RadioButton radioButtonCpf;
    private RadioButton radioButtonCnpj;
    private FrameLayout progressBarOverlay;
    private Button loginButton;
    private UserService userService;
    private TextWatcher textWatcher;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialSetup();

        userService = RetrofitClient.getClient().create(UserService.class);

        govRegCodeInput = findViewById(R.id.govRegCode_input);
        radioGroupCpfCnpj = findViewById(R.id.radioGroupCpfCnpj);
        radioButtonCpf = findViewById(R.id.radioButtonCpf);
        radioButtonCnpj = findViewById(R.id.radioButtonCnpj);
        progressBarOverlay = findViewById(R.id.progressOverlay);

        setMaskAndLength(MaskEditUtil.FORMAT_CPF, 14);

        radioGroupCpfCnpj.setOnCheckedChangeListener((group, checkedId) -> {
            govRegCodeInput.removeTextChangedListener(textWatcher);

            if (checkedId == R.id.radioButtonCpf) {
                setMaskAndLength(MaskEditUtil.FORMAT_CPF, 14);
                govRegCodeInput.setHint("CPF");
            } else if (checkedId == R.id.radioButtonCnpj) {
                setMaskAndLength(MaskEditUtil.FORMAT_CNPJ, 18);
                govRegCodeInput.setHint("CNPJ");
            }

            govRegCodeInput.setText(""); // Clear the input field when switching
            govRegCodeInput.addTextChangedListener(textWatcher);  // Re-add the TextWatcher
        });

        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_btn);
        
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String govRegCode = govRegCodeInput.getText().toString().replaceAll("[.\\-]","");

                performLogin(govRegCode, passwordInput.getText().toString());
            }
        });

        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initialSetup(){
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setMaskAndLength(String mask, int maxLength) {
        // Remove existing TextWatcher and add the new one
        govRegCodeInput.removeTextChangedListener(textWatcher);
        textWatcher = MaskEditUtil.mask(govRegCodeInput, mask);
        govRegCodeInput.addTextChangedListener(textWatcher);

        // Set the max length for the EditText
        govRegCodeInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    private void performLogin(String email, String password) {
        progressBarOverlay.setVisibility(View.VISIBLE);

        LoginRequestDTO loginRequest = new LoginRequestDTO(email, password);

        Call<LoginResponseDTO> call = userService.login(loginRequest);
        call.enqueue(new Callback<LoginResponseDTO>() {
            @Override
            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseDTO loginResponse = response.body();
                    String token = loginResponse.getToken();
                    User user = loginResponse.getUser();
                    Log.i("LUBANK", user.name);

                    // Store JWT token and user info in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("jwt_token", token);
                    editor.putString("user", user.toString());
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        // Log the error response body (if available) as a string
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("LUBANK", "Error response: " + errorBody);
                        progressBarOverlay.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Login failed: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("LUBANK", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure (Call < LoginResponseDTO > call, Throwable t){
                Log.e("LUBANK", "Network error:" + t.getMessage());
                progressBarOverlay.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}