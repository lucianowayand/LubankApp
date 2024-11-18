package io.github.lucianoawayand.lubank_app.Home;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.adapter.ActionsAdapter;
import io.github.lucianoawayand.lubank_app.databinding.ActivityHomeBinding;
import io.github.lucianoawayand.lubank_app.model.Action;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private ArrayList<Action> actionsList;
    private ActionsAdapter actionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_home);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewActions);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        actionsAdapter = new ActionsAdapter(this, actionsList);
        recyclerView.setAdapter(actionsAdapter);
        createActionsList();
    }

    private void createActionsList() {
        Action action1 = new Action(R.drawable.ic_dollasign, "Fazer transferência");
        actionsList.add(action1);

        Action action2 = new Action(R.drawable.ic_dollasign, "Fazer transferência 1123");
        actionsList.add(action1);

        Action action3 = new Action(R.drawable.ic_dollasign, "Fazer transferência asdasdas");
        actionsList.add(action1);
    }
}
