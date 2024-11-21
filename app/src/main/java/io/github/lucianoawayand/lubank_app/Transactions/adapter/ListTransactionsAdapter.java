package io.github.lucianoawayand.lubank_app.Transactions.adapter;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.shared.domain.Transaction;
import io.github.lucianoawayand.lubank_app.shared.utils.MaskEditUtil;

public class ListTransactionsAdapter extends RecyclerView.Adapter<ListTransactionsAdapter.ViewHolder> {

    private final List<Transaction> transactionList;
    private final String currentUserId;
    private TextWatcher textWatcher;


    public ListTransactionsAdapter(List<Transaction> transactionList, String currentUserId) {
        this.transactionList = transactionList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        boolean isIncoming = transaction.getReceiverId().equals(currentUserId);
        boolean isCNPJ = transaction.getUserGovRegCode().length() > 11;
        String userGovRegCode = isCNPJ ? MaskEditUtil.formatCNPJ(transaction.getUserGovRegCode()) :
                MaskEditUtil.formatCPF(transaction.getUserGovRegCode());
        CharSequence amount = MaskEditUtil.formatMoney(transaction.getAmount());

        holder.card.setBackgroundColor(isIncoming ? Color.parseColor("#DFF0D8") : Color.parseColor("#F2DEDE"));
        holder.icon.setImageResource(isIncoming ? R.drawable.ic_received : R.drawable.ic_sent);
        holder.name.setText(isIncoming ? "De: " + transaction.getUserName() : "Para: " + transaction.getUserName());
        holder.govRegCode.setText(isCNPJ ? "CNPJ: " + userGovRegCode : "CPF: " + userGovRegCode);
        holder.value.setText(isIncoming ? amount : "- " + amount);
        holder.date.setText(MaskEditUtil.formatIsoDate(transaction.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        ImageView icon;
        TextView name, value, date, govRegCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.transactionCard);
            icon = itemView.findViewById(R.id.transactionIcon);
            name = itemView.findViewById(R.id.transactionName);
            govRegCode = itemView.findViewById(R.id.transactionGovRegCode);
            value = itemView.findViewById(R.id.transactionValue);
            date = itemView.findViewById(R.id.transactionDate);
        }
    }
}

