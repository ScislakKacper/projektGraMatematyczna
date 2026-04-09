package com.kacper.projektgramatematyczna;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button przyciskGraj;
    private Button przyciskRanking;
    private Button ileDoKoncaDnia;
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
        ileDoKoncaDnia = findViewById(R.id.przycisk_ileDoKonca);

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
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View widokZasad = layoutInflater.inflate(R.layout.layout_zasady, null);

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setView(widokZasad).create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

                Button przyciskOk = widokZasad.findViewById(R.id.przycisk_ok);
                przyciskOk.setOnClickListener(v -> dialog.dismiss());
            }
        });

        przyciskRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zobaczRanking = new Intent(getApplicationContext(), Ranking.class);
                startActivity(zobaczRanking);
            }
        });

        ileDoKoncaDnia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                Calendar polnoc = Calendar.getInstance();
                polnoc.add(Calendar.DAY_OF_MONTH, 1);
                polnoc.set(Calendar.HOUR_OF_DAY, 0);
                polnoc.set(Calendar.MINUTE, 0);
                polnoc.set(Calendar.SECOND, 0);
                polnoc.set(Calendar.MILLISECOND, 0);

                long rozniaca_polnoc_teraz = polnoc.getTimeInMillis() - calendar.getTimeInMillis();
                long godziny = TimeUnit.MILLISECONDS.toHours(rozniaca_polnoc_teraz);
                long minuty = TimeUnit.MILLISECONDS.toMinutes(rozniaca_polnoc_teraz) % 60;
                long sekundy = TimeUnit.MILLISECONDS.toSeconds(rozniaca_polnoc_teraz) % 60;

                String czas = String.format(Locale.getDefault(), "Do końca dnia pozostało: %02d:%02d:%02d", godziny, minuty, sekundy);
                Toast.makeText(MainActivity.this, czas, Toast.LENGTH_SHORT).show();
            }
        });
    }
}