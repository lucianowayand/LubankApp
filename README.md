# Lubank - Bank App

## Lubank
Lubank is an android application that simulates a digital bank. The project uses Java 17.

### Main features
You can create an account using CPF or CNPJ unique codes. The user has the possibility to login using its configured digital. 

By simulating a deposit, you can get money on your Lubank account. Once done, swipe down on the home screen to refresh your balance.

If you want to send money to another account, just use the feature "Transferência". Later, you can check all transactions by the "Extrato" button.

## Build & Run
### Prerequisites
* Java 17 or higher
* Gradle

### Installation
1. Clone the repository:
```shell
git clone https://github.com/lucianowayand/LubankApp.git
cd LubankApi
```

2. Build the project:
```shell
./gradlew build
```

3. Run the application:
```shell
./gradlew bootRun
```

## Dependencies
The project uses several key dependencies:

* SwipeRefreshLayout: Provides pull-to-refresh functionality for views, such as RecyclerView or ScrollView.
* AppCompat v7: Offers backward-compatible versions of Android components and design patterns.
* RecyclerView v7: Efficiently displays large sets of data in a scrollable list or grid format.
* Retrofit: A type-safe HTTP client for Android and Java to simplify API communication.
* Gson Converter (Retrofit Integration): Converts JSON data to Java objects and vice versa for API interactions.
* OkHttp: A robust HTTP & HTTP/2 client for making network requests.
* Gson: A library for serializing and deserializing Java objects to and from JSON.
* Security Crypto: Provides APIs for secure cryptographic operations.
* Biometric API: Enables secure user authentication using biometrics like fingerprint or facial recognition.
* AppCompat: Ensures consistent look-and-feel for Android components across various API levels.
* Material Design Components: Implements Material Design guidelines for a modern and consistent UI.
* Activity API: Simplifies activity management and lifecycle handling.
* ConstraintLayout: A flexible layout manager for creating complex UI designs without nesting views.
