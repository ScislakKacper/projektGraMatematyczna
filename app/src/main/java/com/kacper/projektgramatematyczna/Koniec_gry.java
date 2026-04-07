package com.kacper.projektgramatematyczna;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

import java.util.ArrayList;
import java.util.List;

public class Koniec_gry extends AppCompatActivity {
    private TextView wynikGry;
    private ImageButton przejdzDoStronyGlownej;
    private ImageButton zagrajPonownie;
    private ImageButton przejdzDoRankingu;
    AutoCompleteTextView textViewNickGracza;
    TextView najlepszyWynik;
    BazaDanychWynikow bazaDanychWynikow;
    Button zapiszWynik;
    int ostatniWynik;
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
        zapiszWynik = findViewById(R.id.zapiszWynik);
        zapiszWynik.setEnabled(false);
        textViewNickGracza = findViewById(R.id.nickGracza);
        najlepszyWynik = findViewById(R.id.najlepszyWynik);
        wynikGry = findViewById(R.id.wynikGry);
        Intent ostatniaGra = getIntent();
        ostatniWynik = ostatniaGra.getIntExtra("wynikGracza", 0);
        wynikGry.setText("Wynik gry: " + ostatniWynik);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        bazaDanychWynikow = BazaDanychWynikow.zwrocInstancjeBazyDanych(Koniec_gry.this);
        List<Wyniki> listaWynikow = bazaDanychWynikow.zwrocWynikiDao().wyswietlWszystkieWyniki();
        List<String> listaNickow = new ArrayList<>();
        for (Wyniki w : listaWynikow) {
            listaNickow.add(w.getNickGracza());
        }
        ArrayAdapter<String> arrayAdapterNicki = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaNickow);
        textViewNickGracza.setAdapter(arrayAdapterNicki);
        textViewNickGracza.setThreshold(1);
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

                Wyniki zapisanyWynik = null;
                for(Wyniki wynik : listaWynikow){
                    if (wynik.getNickGracza().equalsIgnoreCase(wpisanyNick)){
                        zapisanyWynik = wynik;
                        break;
                    }
                }
                if(zapisanyWynik != null){
                    najlepszyWynik.setText("Najlepszy wynik: " + zapisanyWynik.getPunktyGracza());
                    if(ostatniWynik > zapisanyWynik.getPunktyGracza()) {
                        najlepszyWynik.setText("Najlepszy wynik: " + ostatniWynik + " (NOWY!!!)");
                    }
                }
                else{
                    najlepszyWynik.setText("Najlepszy wynik: -");
                }
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
                Wyniki zapisanyWynik = bazaDanychWynikow.zwrocWynikiDao().wyswietlWynik(zatwierdzonyNick);
                if(zapisanyWynik == null){
                    Wyniki nowyGracz = new Wyniki(zatwierdzonyNick, ostatniWynik);
                    bazaDanychWynikow.zwrocWynikiDao().wstawWynik(nowyGracz);
                }
                else{
                    if (ostatniWynik > zapisanyWynik.getPunktyGracza()) {
                        zapisanyWynik.setPunktyGracza(ostatniWynik);
                        bazaDanychWynikow.zwrocWynikiDao().aktualizujWynik(zapisanyWynik);
                    }
                }
                zapisanyWynik = bazaDanychWynikow.zwrocWynikiDao().wyswietlWynik(zatwierdzonyNick);
                najlepszyWynik.setText("Najlepszy wynik: "+ zapisanyWynik.getPunktyGracza());
                textViewNickGracza.setEnabled(false);
                zapiszWynik.setEnabled(false);
                Toast.makeText(Koniec_gry.this, "Wynik zapisany!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}