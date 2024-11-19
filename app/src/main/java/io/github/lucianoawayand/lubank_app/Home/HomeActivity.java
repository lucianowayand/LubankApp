package io.github.lucianoawayand.lubank_app.Home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.Home.adapter.ActionsAdapter;
import io.github.lucianoawayand.lubank_app.model.Action;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewActions);

        ArrayList<Action> actions = createActionsList();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        ActionsAdapter actionsAdapter = new ActionsAdapter(this, actions);
        recyclerView.setAdapter(actionsAdapter);
    }

    private ArrayList<Action> createActionsList() {
        ArrayList<Action> actions = new ArrayList<>();

        actions.add(new Action(R.drawable.ic_dollasign, "Fazer depósito"));
        actions.add(new Action(R.drawable.barcode, "Pagar boleto"));
        actions.add(new Action(R.drawable.withdraw, "Sacar dinheiro"));

        return actions;
    }
}
