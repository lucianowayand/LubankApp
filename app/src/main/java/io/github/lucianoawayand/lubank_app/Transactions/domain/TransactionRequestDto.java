package io.github.lucianoawayand.lubank_app.Transactions.domain;

public class TransactionRequestDto {
    public String receiverGovRegCode;
    public Double amount;

    public TransactionRequestDto(String receiverGovRegCode, Double amount) {
        this.receiverGovRegCode = receiverGovRegCode;
        this.amount = amount;
    }
}