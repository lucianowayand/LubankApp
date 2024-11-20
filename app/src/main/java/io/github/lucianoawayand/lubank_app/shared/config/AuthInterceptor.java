package io.github.lucianoawayand.lubank_app.shared.config;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        // Retrieve the JWT token from SharedPreferences (or another secure storage)
        String token = getJwtToken();

        // If the token exists, add it to the request headers
        Request originalRequest = chain.request();
        if (token != null && !token.isEmpty()) {
            Request newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)  // Add Authorization header
                    .build();
            return chain.proceed(newRequest);
        }

        // If no token, proceed with the request without the Authorization header
        return chain.proceed(originalRequest);
    }

    // A method to retrieve the JWT token from SharedPreferences (or any secure storage)
    private String getJwtToken() {
        // Assuming you store the JWT token in SharedPreferences
        // Modify this to suit your app's token storage
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("jwt_token", null);
    }
}
