package com.kacper.projektgramatematyczna;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.agog.mathdisplay.MTMathView;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Zakladka_gry extends AppCompatActivity {
    List <Pytanie> pytanie;
    TextView textViewPytanie;
    MTMathView mathViewDzialanieZPytania;
    TextView textViewPoziomTrudnosci;
    RadioGroup radioGroup;
    Button buttonZatwierdzPytanie;
    int radioButton[] = new int[]{
            R.id.odpa,
            R.id.odpb,
            R.id.odpc,
            R.id.odpd
    };
    RadioButton ButtonOdpA;
    RadioButton ButtonOdpB;
    RadioButton ButtonOdpC;
    RadioButton ButtonOdpD;
    RadioButton przyciskiOdpowiedzi[];
    TextView textViewPunkty;
    ImageView zyciaGracza[];
    ProgressBar odliczanieCzasu;
    Random random = new Random();
    int numerAktualnegoPytania = 0;
    int iloscPunktow = 0;
    int iloscZyc = 3;
    int iloscSekund = 30;

    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_zakladka_gry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        iloscPunktow = 0;
        textViewPytanie = findViewById(R.id.trescPytania);
        textViewPoziomTrudnosci = findViewById(R.id.poziomTrudnosci);
        mathViewDzialanieZPytania = findViewById(R.id.dzialanieZPytania);
        radioGroup = findViewById(R.id.odpowiedzi);
        ButtonOdpA = findViewById(R.id.odpa);
        ButtonOdpB = findViewById(R.id.odpb);
        ButtonOdpC = findViewById(R.id.odpc);
        ButtonOdpD = findViewById(R.id.odpd);
        textViewPunkty = findViewById(R.id.punkty);
        buttonZatwierdzPytanie = findViewById(R.id.zatwierdzPytanie);
        odliczanieCzasu = findViewById(R.id.odliczanieCzasu);

        zyciaGracza = new ImageView[]{
          findViewById(R.id.serce1), findViewById(R.id.serce2), findViewById(R.id.serce3)
        };

        przyciskiOdpowiedzi = new RadioButton[]{
                ButtonOdpA, ButtonOdpB, ButtonOdpC, ButtonOdpD
        };

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://scislakkacper.github.io/projektGraMatematycznaJSON/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<List<Pytanie>> call = jsonPlaceholderApi.getPytania();
        call.enqueue(new Callback<List<Pytanie>>() {
            @Override
            public void onResponse(Call<List<Pytanie>> call, Response<List<Pytanie>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Zakladka_gry.this, ""+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                pytanie = response.body();
                Collections.shuffle(pytanie);
                numerAktualnegoPytania = 0;
                wyswietlPytanie(numerAktualnegoPytania);
            }

            @Override
            public void onFailure(Call<List<Pytanie>> call, Throwable t) {
                Toast.makeText(Zakladka_gry.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        buttonZatwierdzPytanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sprawdzOdp(numerAktualnegoPytania);
            }
        });
    }
    private boolean sprawdzOdp(int ktorePytanie){
        Pytanie aktualne_pytanie = pytanie.get(ktorePytanie);
        if(radioGroup.getCheckedRadioButtonId() == radioButton[aktualne_pytanie.getPoprawna()]){
            RadioButton zaznaczonyDobrze = findViewById(radioGroup.getCheckedRadioButtonId());
            zaznaczonyDobrze.setBackgroundColor(Color.GREEN);
            if(aktualne_pytanie.getPoziom().equals("latwy")){
                iloscPunktow++;
            }
            else if(aktualne_pytanie.getPoziom().equals("sredni")){
                iloscPunktow += 2;
            }
            else{
                iloscPunktow += 3;
            }
            textViewPunkty.setText("Punkty: " + iloscPunktow);
            countDownTimer.cancel();
            nastepnePytanie();
            return true;
        }
        else{
            iloscZyc--;
            zyciaGracza[iloscZyc].setVisibility(View.INVISIBLE);
            RadioButton zaznaczonyZle = findViewById(radioGroup.getCheckedRadioButtonId());
            zaznaczonyZle.setBackgroundColor(Color.RED);
            zaznaczonyZle.setEnabled(false);
            if(iloscZyc == 0){
                koniecGry();
                buttonZatwierdzPytanie.setEnabled(false);
                countDownTimer.cancel();
                finish();
                System.exit(0);
            }
            return false;
        }
    }
    private void wyswietlPytanie(int ktorePytanie){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        buttonZatwierdzPytanie.setEnabled(false);
        radioGroup.clearCheck();
        textViewPytanie.setText(pytanie.get(ktorePytanie).getTrescPytania());
        mathViewDzialanieZPytania.setLatex(pytanie.get(ktorePytanie).getDzialanie());
        mathViewDzialanieZPytania.setFontSize(80);
        mathViewDzialanieZPytania.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mathViewDzialanieZPytania.setTextColor(Color.parseColor("#9992b0"));
        if(pytanie.get(ktorePytanie).getPoziom().equals("sredni")){
            textViewPoziomTrudnosci.setText("Poziom średni");
        }
        else if(pytanie.get(ktorePytanie).getPoziom().equals("latwy")){
            textViewPoziomTrudnosci.setText("Poziom łatwy");
        }
        else{
            textViewPoziomTrudnosci.setText("Poziom " + pytanie.get(ktorePytanie).getPoziom());
        }
        if(pytanie.get(ktorePytanie).getPoziom().equals("trudny")){
            iloscSekund = 30;
        }
        else if(pytanie.get(ktorePytanie).getPoziom().equals("sredni")){
            iloscSekund = 20;
        }
        else{
            iloscSekund = 10;
        }
        ButtonOdpA.setText(pytanie.get(ktorePytanie).getOdpa());
        ButtonOdpB.setText(pytanie.get(ktorePytanie).getOdpb());
        ButtonOdpC.setText(pytanie.get(ktorePytanie).getOdpc());
        ButtonOdpD.setText(pytanie.get(ktorePytanie).getOdpd());
        ButtonOdpA.setEnabled(true);
        ButtonOdpB.setEnabled(true);
        ButtonOdpC.setEnabled(true);
        ButtonOdpD.setEnabled(true);
        ButtonOdpA.setBackgroundColor(Color.parseColor("#393053"));
        ButtonOdpB.setBackgroundColor(Color.parseColor("#393053"));
        ButtonOdpC.setBackgroundColor(Color.parseColor("#393053"));
        ButtonOdpD.setBackgroundColor(Color.parseColor("#393053"));
        for (int i = 0; i < 4; i++) {
            przyciskiOdpowiedzi[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                    buttonZatwierdzPytanie.setEnabled(true);
                }
            });
        }
        odliczanieCzasu.setMax(600);
        odliczanieCzasu.setProgressDrawable(getDrawable(R.drawable.progress_bar_zielony));
        odliczanieCzasu.setProgress(600);
        countDownTimer = new CountDownTimer(iloscSekund * 1000, 1000) {
            @Override
            public void onFinish() {
                iloscZyc--;
                zyciaGracza[iloscZyc].setVisibility(View.INVISIBLE);
                if(iloscZyc == 0){
                    koniecGry();
                    finish();
                    System.exit(0);
                }
                else{
                    nastepnePytanie();
                }
            }
            @Override
            public void onTick(long millisUntilFinished) {
                if(odliczanieCzasu.getProgress() > odliczanieCzasu.getMax() * 0.6){
                    odliczanieCzasu.setProgressDrawable(getDrawable(R.drawable.progress_bar_zielony));
                }
                else if(odliczanieCzasu.getProgress() > odliczanieCzasu.getMax() * 0.3){
                    odliczanieCzasu.setProgressDrawable(getDrawable(R.drawable.progress_bar_zolty));
                }
                else{
                    odliczanieCzasu.setProgressDrawable(getDrawable(R.drawable.progress_bar_czerwony));
                }
                int progress = (int) (odliczanieCzasu.getMax() * millisUntilFinished / (iloscSekund * 1000));
                odliczanieCzasu.setProgress(progress);
            }
        };
        countDownTimer.start();
    }
    private void nastepnePytanie() {
        numerAktualnegoPytania++;

        if (numerAktualnegoPytania >= pytanie.size()) {
            koniecGry();
            return;
        }

        wyswietlPytanie(numerAktualnegoPytania);
    }
    private void koniecGry(){
        Toast.makeText(this, "Koniec gry!", Toast.LENGTH_SHORT).show();
        Intent koniec_gry = new Intent(getApplicationContext(), Koniec_gry.class);
        koniec_gry.putExtra("wynikGracza", iloscPunktow);
        startActivity(koniec_gry);
    }
}