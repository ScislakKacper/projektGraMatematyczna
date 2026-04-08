package com.kacper.projektgramatematyczna;

import android.adservices.ondevicepersonalization.UserData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Ranking extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button przyciskPoprzedniaStrona, przyciskNastepnaStrona;
    private ImageButton powrotDoGlownej;
    private WynikiAdapter wynikiAdapter;
    BazaDanychWynikow bazaDanychWynikow;
    int liczbaWynikow = 0;
    int iloscWierszy = 10;
    int aktualnaStrona = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ranking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        recyclerView = findViewById(R.id.listaWynikow);
        przyciskPoprzedniaStrona = findViewById(R.id.przyciskPoprzednia);
        przyciskNastepnaStrona = findViewById(R.id.przyciskNastepna);
        powrotDoGlownej = findViewById(R.id.powrotDoStronyGlownej);
        bazaDanychWynikow = BazaDanychWynikow.zwrocInstancjeBazyDanych(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        wynikiAdapter = new WynikiAdapter(new ArrayList<>(), 0);
        recyclerView.setAdapter(wynikiAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.linia_przerwy_recyclerview));
        recyclerView.addItemDecoration(divider);

        przyciskNastepnaStrona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aktualnaStrona++;
                pokazStrone();
            }
        });

        przyciskPoprzedniaStrona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aktualnaStrona > 0){
                    aktualnaStrona--;
                    pokazStrone();
                }
            }
        });

        powrotDoGlownej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent powrotDoGlownej = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(powrotDoGlownej);
            }
        });

        pokazStrone();
    }
    public void pokazStrone(){
        liczbaWynikow = bazaDanychWynikow.zwrocWynikiDao().policzWszystkie();
        dostepnoscPrzyciskow();
        int offset = aktualnaStrona * iloscWierszy;
        List<Wyniki> lista = bazaDanychWynikow.zwrocWynikiDao().pobierzStrone(iloscWierszy, offset);
        wynikiAdapter.setLista(lista, offset);
    }
    public void dostepnoscPrzyciskow(){
        if(liczbaWynikow == 0) return;
        int maxStrona = (liczbaWynikow - 1) / iloscWierszy;
        if(aktualnaStrona > 0){
            przyciskPoprzedniaStrona.getBackground().setAlpha(255);
        }
        else{
            przyciskPoprzedniaStrona.getBackground().setAlpha(50);
        }
        if(aktualnaStrona < maxStrona){
            przyciskNastepnaStrona.getBackground().setAlpha(255);
        }
        else{
            przyciskNastepnaStrona.getBackground().setAlpha(50);
        }
        przyciskPoprzedniaStrona.setEnabled(aktualnaStrona > 0);
        przyciskNastepnaStrona.setEnabled(aktualnaStrona < maxStrona);
    }
}