package com.kacper.projektgramatematyczna;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Ranking extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button przyciskPoprzedniaStrona, przyciskNastepnaStrona;
    private WynikiAdapter wynikiAdapter;
    BazaDanychWynikow bazaDanychWynikow;

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
        bazaDanychWynikow = BazaDanychWynikow.zwrocInstancjeBazyDanych(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wynikiAdapter = new WynikiAdapter(new ArrayList<>(), 0);
        recyclerView.setAdapter(wynikiAdapter);

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
        pokazStrone();
    }
    public void pokazStrone(){
        int offset = aktualnaStrona * iloscWierszy;
        List<Wyniki> lista = bazaDanychWynikow.zwrocWynikiDao().pobierzStrone(iloscWierszy, offset);
        wynikiAdapter.setLista(lista, offset);
    }
}