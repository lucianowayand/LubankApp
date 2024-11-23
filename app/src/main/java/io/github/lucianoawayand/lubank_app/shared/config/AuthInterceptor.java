package io.github.lucianoawayand.lubank_app.shared.config;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    List<String> tokenlessRequestUrls = Arrays.asList("/api/v1/users/login", "/api/v1/users/register");

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = getJwtToken(chain);

        Request originalRequest = chain.request();
        if (token != null && !token.isEmpty()) {
            Request newRequest = originalRequest.newBuilder().addHeader("Authorization", "Bearer " + token)  // Add Authorization header
                    .build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(originalRequest);
    }

    private String getJwtToken(Chain chain) {
        if (tokenlessRequestUrls.contains(chain.request().url().toString().replace(RetrofitClient.BASE_URL, ""))) {
            return "";
        }

        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getString("jwt_token", null);
    }
}
