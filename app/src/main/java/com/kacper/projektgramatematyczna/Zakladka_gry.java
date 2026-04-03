package com.kacper.projektgramatematyczna;

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

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Zakladka_gry extends AppCompatActivity {
    List <Pytanie> pytanie;
    MTMathView textViewPytanie;
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
    int minimalnePytanie = 0;
    int mnoznikCiezkosci = 1;
    //int numerAktualnegoPytania = random.nextInt(3 * mnoznikCiezkosci) + minimalnePytanie;
    int numerAktualnegoPytania;
    int ilePytan = 0;
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
                .baseUrl("https://my-json-server.typicode.com/ScislakKacper/projektGraMatematyczna/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<List<Pytanie>> call = jsonPlaceholderApi.getPytania();
        call.enqueue(new Callback<List<Pytanie>>() {
            @Override
            public void onResponse(Call<List<Pytanie>> call, Response<List<Pytanie>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Zakladka_gry.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                pytanie = response.body();
                numerAktualnegoPytania = random.nextInt(3);
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
            ilePytan++;
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
            numerAktualnegoPytania = random.nextInt(3);
            wyswietlPytanie(numerAktualnegoPytania);
            return true;
        }
        else{
            iloscZyc--;
            zyciaGracza[iloscZyc].setVisibility(View.INVISIBLE);
            if(iloscZyc == 0){
                Toast.makeText(this, "Przegrałeś", Toast.LENGTH_SHORT).show();
                buttonZatwierdzPytanie.setEnabled(false);
                countDownTimer.cancel();
            }
            return false;
        }
    }
    private void wyswietlPytanie(int ktorePytanie){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        if(ilePytan == 10 || ilePytan == 20){
            minimalnePytanie += 10;
            mnoznikCiezkosci++;
        }
        buttonZatwierdzPytanie.setEnabled(false);
        radioGroup.clearCheck();
        textViewPytanie.setLatex("\\text{"+pytanie.get(ktorePytanie).getTrescPytania()+"} \\\\ \\\\" + "x = \\frac{-b \\pm \\sqrt{b^2-4ac}}{2a}");
        textViewPytanie.setFontSize(60);
        textViewPytanie.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewPoziomTrudnosci.setText("Poziom " + pytanie.get(ktorePytanie).getPoziom());

        if(pytanie.get(ktorePytanie).getPoziom().equals("latwy")){
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
        for (int i = 0; i < 4; i++) {
            przyciskiOdpowiedzi[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                    buttonZatwierdzPytanie.setEnabled(true);
                }
            });
        }
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
                    Toast.makeText(Zakladka_gry.this, "Przegrałeś", Toast.LENGTH_SHORT).show();
                }
                else{
                    numerAktualnegoPytania = random.nextInt(3);
                    wyswietlPytanie(numerAktualnegoPytania);
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
}