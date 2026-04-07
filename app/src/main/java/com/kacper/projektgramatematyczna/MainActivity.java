package com.kacper.projektgramatematyczna;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button przyciskGraj;
    private Button przyciskRanking;
    private ImageButton zamknijApke;
    private ImageButton infoGra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        zamknijApke = findViewById(R.id.zamknij_apke);
        infoGra = findViewById(R.id.info_gra);
        przyciskGraj = findViewById(R.id.przycisk_graj);
        przyciskRanking = findViewById(R.id.przycisk_ranking);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        przyciskGraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zagrajWGre = new Intent(getApplicationContext(), Zakladka_gry.class);
                startActivity(zagrajWGre);
            }
        });

        zamknijApke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        infoGra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AlertDialogCustom));
                builder.setTitle("Zasady gry \"Mistrzowie Liczb\":");
                builder.setMessage("\n\n1. Kazdy gracz posiada okreslony czas na rozwiazanie wylosowanego pytania z dziedziny matematycznej.\n\n2. Za poprawna odpowiedz gracz otrzymuje 1 punkt i przechodzi do następnego zadania, w ktorym czas odlicza sie od nowa.\n\n3. Gra konczy się gdy gracz nie odpowie na pytanie w limicie czasowym lub poda błedna odpowiedz.\n\n4. Wszystkie wyniki graczy zapisywane sa w ogolnodostępnym rankingu.");
                builder.setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(1000, 1200);
            }
        });

        przyciskRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zobaczRanking = new Intent(getApplicationContext(), Ranking.class);
                startActivity(zobaczRanking);
            }
        });
    }
}