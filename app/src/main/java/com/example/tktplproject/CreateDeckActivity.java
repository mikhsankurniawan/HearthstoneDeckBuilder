package com.example.tktplproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CreateDeckActivity extends AppCompatActivity {
    AppViewModel viewModel;
    List<Card> cardLists;
    List<Deck> deckLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        cardLists = viewModel.getAllCards().getValue();
        setContentView(R.layout.activity_create_deck);
        RecyclerView recyclerView = findViewById(R.id.create_deck_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AppAdapter(this, cardLists));

        String className = getIntent().getStringExtra("class_name");

        Button createDeck = (Button) findViewById(R.id.create_deck_button);
        TextView totalCards = (TextView) findViewById(R.id.total_temporary_text);
        EditText deckNameEditText = (EditText) findViewById(R.id.deck_name_edit);

        createDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(totalCards.getText().toString()) != 30) {
                    Context context = getApplicationContext();
                    CharSequence text = "Deck must contain exact 30 cards.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else if (deckNameEditText.getText().toString().isEmpty()) {
                    Context context = getApplicationContext();
                    CharSequence text = "Deck name must not be null.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    try {
                        getApplicationContext().startService(new Intent(getApplicationContext(), CreateDeckService.class));
                        viewModel.addDeck(deckNameEditText.getText().toString(), className);
                        getApplicationContext().stopService(new Intent(getApplicationContext(), CreateDeckService.class));
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void onCardSelected() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.activity_create_deck, CardDetailsFragment.newInstance(), "cardDetails")
                .commit();
    }

//    @Override
//    public void onBackPressed() {
//        String identifier = getIntent().getStringExtra("identifier");
//        Bundle bundle = new Bundle();
//        bundle.putString("identifier", identifier);
//        Fragment fragment = HeroDetailsFragment.newInstance();
//        fragment.setArguments(bundle);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .addToBackStack(null)
//                .add(R.id.activity_create_deck, fragment, "heroDetails")
//                .commit();
//    }
}
