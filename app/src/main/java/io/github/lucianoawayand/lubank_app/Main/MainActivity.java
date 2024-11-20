package io.github.lucianoawayand.lubank_app.Main;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricManager;

import com.google.gson.Gson;

import io.github.lucianoawayand.lubank_app.Home.HomeActivity;
import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.Register.RegisterActivity;
import io.github.lucianoawayand.lubank_app.shared.config.RetrofitClient;
import io.github.lucianoawayand.lubank_app.Main.domain.LoginRequestDto;
import io.github.lucianoawayand.lubank_app.Main.domain.LoginResponseDto;
import io.github.lucianoawayand.lubank_app.shared.domain.User;
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
    private boolean hasBiometricAccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialSetup();
        handleBiometrics();

        userService = RetrofitClient.getClient(this).create(UserService.class);

        govRegCodeInput = findViewById(R.id.govRegCode_input);
        radioGroupCpfCnpj = findViewById(R.id.radioGroupCpfCnpj);
        radioButtonCpf = findViewById(R.id.radioButtonCpf);
        radioButtonCnpj = findViewById(R.id.radioButtonCnpj);
        progressBarOverlay = findViewById(R.id.progressOverlay);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_btn);
        registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String govRegCode = govRegCodeInput.getText().toString().replaceAll("[.\\-]", "");

                performLogin(govRegCode, passwordInput.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        handleExistingSession();

        handleCnpjCpfToggle();
    }

    private void initialSetup() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void handleBiometrics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            BiometricManager biometricManager = BiometricManager.from(this);
            switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    hasBiometricAccess = true;
                    break;
                default:
                    hasBiometricAccess = false;
                    break;
            }
        } else {
            hasBiometricAccess = false;
        }
    }

    private void handleExistingSession() {
        SharedPreferences securePrefs = getEncryptedSharedPreferences();
        String userGovRegCode = securePrefs.getString("user_gov_reg_code", null);

        govRegCodeInput.setText(userGovRegCode);

        if (hasBiometricAccess) {
            Log.i("LUBANK","Has biometric access");
            Executor executor = Executors.newSingleThreadExecutor();

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Authentication")
                    .setSubtitle("Log in using your biometric credential")
                    .setDescription("Use your fingerprint or face to authenticate")
                    .setNegativeButtonText("Cancel")
                    .build();

            BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                    new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            String password = securePrefs.getString("user_password", null);

                            performLogin(userGovRegCode, password);
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            // Inform the user authentication failed
                        }

                        @Override
                        public void onAuthenticationError(int errorCode, CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                            // Handle errors
                        }
                    });

            biometricPrompt.authenticate(promptInfo);
        }
    }

    private void redirectToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private SharedPreferences getEncryptedSharedPreferences() {
        try {
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            return EncryptedSharedPreferences.create(
                    this,
                    "secure_user_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize EncryptedSharedPreferences", e);
        }
    }

    private void setMaskAndLength(String mask, int maxLength) {
        // Remove existing TextWatcher and add the new one
        govRegCodeInput.removeTextChangedListener(textWatcher);
        textWatcher = MaskEditUtil.mask(govRegCodeInput, mask);
        govRegCodeInput.addTextChangedListener(textWatcher);

        // Set the max length for the EditText
        govRegCodeInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    private void handleCnpjCpfToggle() {
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
    }

    private void performLogin(String govRegCode, String password) {
        runOnUiThread(() -> {
            progressBarOverlay.setVisibility(View.VISIBLE);

            LoginRequestDto loginRequest = new LoginRequestDto(govRegCode, password);

            Call<LoginResponseDto> call = userService.login(loginRequest);
            call.enqueue(new Callback<LoginResponseDto>() {
                @Override
                public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponseDto loginResponse = response.body();
                        String token = loginResponse.getToken();
                        User user = loginResponse.getUser();

                        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("jwt_token", token);
                        Gson gson = new Gson();
                        String userJson = gson.toJson(user); // Serialize User to JSON
                        editor.putString("user", userJson);  // Save JSON string
                        editor.apply();

                        SharedPreferences securePrefs = getEncryptedSharedPreferences();
                        SharedPreferences.Editor securePref = securePrefs.edit();
                        securePref.putString("user_gov_reg_code", govRegCode);
                        securePref.putString("user_password", password);
                        securePref.apply();

                        redirectToHome();
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
                public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                    Log.e("LUBANK", "Network error:" + t.getMessage());
                    progressBarOverlay.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}