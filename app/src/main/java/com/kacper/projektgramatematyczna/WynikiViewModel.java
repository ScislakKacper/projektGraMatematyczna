package com.kacper.projektgramatematyczna;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

public class WynikiViewModel extends AndroidViewModel {
    private final WynikiDAO wynikiDAO;

    public WynikiViewModel(@NonNull Application application) {
        super(application);
        wynikiDAO = BazaDanychWynikow.zwrocInstancjeBazyDanych(application).zwrocWynikiDao();
    }
    public LiveData<List<Wyniki>> getWszystkieWyniki(){
        return wynikiDAO.wyswietlWszystkieWyniki();
    }
    public LiveData<Wyniki> getWynik(String nick) {
        return wynikiDAO.wyswietlWynik(nick);
    }
    public void zapiszWynikLubAktualizuj(String nick, int wynik){
        Executors.newSingleThreadExecutor().execute(() -> {
            Wyniki istnieje = wynikiDAO.pobierzWynik(nick);

            if(istnieje == null){
                wynikiDAO.wstawWynik(new Wyniki(nick, wynik));
            }
            else if(wynik > istnieje.getPunktyGracza()){
                istnieje.setPunktyGracza(wynik);
                wynikiDAO.aktualizujWynik(istnieje);
            }
        });
    }
}
