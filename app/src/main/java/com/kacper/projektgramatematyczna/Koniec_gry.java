package com.kacper.projektgramatematyczna;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class Koniec_gry extends AppCompatActivity {
    private TextView wynikGry;
    private ImageButton przejdzDoStronyGlownej;
    private ImageButton zagrajPonownie;
    private ImageButton przejdzDoRankingu;
    AutoCompleteTextView textViewNickGracza;
    TextView najlepszyWynik;
    Button zapiszWynik;
    int ostatniWynik;
    private WynikiViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_koniec_gry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        zagrajPonownie = findViewById(R.id.zagrajPonownie);
        przejdzDoStronyGlownej = findViewById(R.id.przejdzDoStronyGlownej);
        przejdzDoRankingu = findViewById(R.id.przejdzDoRankingu);

        zapiszWynik = findViewById(R.id.zapiszWynik);
        zapiszWynik.setEnabled(false);

        textViewNickGracza = findViewById(R.id.nickGracza);
        najlepszyWynik = findViewById(R.id.najlepszyWynik);
        wynikGry = findViewById(R.id.wynikGry);

        Intent ostatniaGra = getIntent();
        ostatniWynik = ostatniaGra.getIntExtra("wynikGracza", 0);
        wynikGry.setText("Wynik gry: " + ostatniWynik);

        viewModel = new ViewModelProvider(this).get(WynikiViewModel.class);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        Intent stronaGlowna = new Intent(getApplicationContext(), MainActivity.class);
        Intent ranking = new Intent(getApplicationContext(), Ranking.class);
        Intent ponownaGra  = new Intent(getApplicationContext(), Zakladka_gry.class);

        przejdzDoStronyGlownej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(stronaGlowna);
            }
        });
        przejdzDoRankingu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ranking);
            }
        });
        zagrajPonownie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ponownaGra);
            }
        });

        List<String> listaNickow = new ArrayList<>();
        ArrayAdapter<String> arrayAdapterNicki = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaNickow);
        textViewNickGracza.setAdapter(arrayAdapterNicki);
        textViewNickGracza.setThreshold(1);

        viewModel.getWszystkieWyniki().observe(this, wynikis -> {
            listaNickow.clear();

            for(Wyniki w : wynikis){
                listaNickow.add(w.getNickGracza());
            }

            arrayAdapterNicki.notifyDataSetChanged();
        });

        textViewNickGracza.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String wpisanyNick = s.toString().trim();
                if(wpisanyNick.isEmpty()){
                    najlepszyWynik.setText("Najlepszy wynik: -");
                    zapiszWynik.setEnabled(false);
                    return;
                }
                zapiszWynik.setEnabled(true);

                viewModel.getWynik(wpisanyNick).observe(Koniec_gry.this, wyniki -> {
                    if(wyniki != null){
                        if(ostatniWynik > wyniki.getPunktyGracza()){
                            najlepszyWynik.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            najlepszyWynik.setText("Najlepszy wynik: " + ostatniWynik + "\n\n(NOWY!!!)");
                        }
                        else{
                            najlepszyWynik.setText("Najlepszy wynik: " + wyniki.getPunktyGracza());
                        }
                    }
                    else{
                        najlepszyWynik.setText("Najlepszy wynik: -");
                    }
                });
            }
        });
        zapiszWynik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String zatwierdzonyNick = textViewNickGracza.getText().toString().trim();
                if(zatwierdzonyNick.isEmpty()){
                    Toast.makeText(Koniec_gry.this, "Wpisz nick!", Toast.LENGTH_SHORT).show();
                    textViewNickGracza.setEnabled(true);
                    zapiszWynik.setEnabled(false);
                    return;
                }
                viewModel.zapiszWynikLubAktualizuj(zatwierdzonyNick, ostatniWynik);

                textViewNickGracza.setEnabled(false);
                zapiszWynik.setEnabled(false);

                Toast.makeText(Koniec_gry.this, "Wynik zapisany!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}