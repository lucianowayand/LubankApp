package io.github.lucianoawayand.lubank_app.shared.domain;


public class Transaction {
    private String id;
    private String senderId;
    private String receiverId;
    private double amount;
    private String createdAt;
    private String userName;
    private String userGovRegCode;

    // Constructor
    public Transaction(String senderId, String receiverId, double amount, String createdAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    // Getters
    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserGovRegCode() {
        return userGovRegCode;
    }
}

