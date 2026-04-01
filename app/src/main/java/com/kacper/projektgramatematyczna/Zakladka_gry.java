package com.kacper.projektgramatematyczna;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Zakladka_gry extends AppCompatActivity {
    List <Pytanie> pytanie;
    TextView textViewPytanie;
    RadioGroup radioGroup;
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

        textViewPytanie = findViewById(R.id.trescPytania);
        radioGroup = findViewById(R.id.odpowiedzi);
        ButtonOdpA = findViewById(R.id.odpa);
        ButtonOdpB = findViewById(R.id.odpb);
        ButtonOdpC = findViewById(R.id.odpc);
        ButtonOdpD = findViewById(R.id.odpd);

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
                List<Pytanie> pytania = response.body();
                Toast.makeText(Zakladka_gry.this, pytania.get(0).getTrescPytania(), Toast.LENGTH_SHORT).show();
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
    }
}