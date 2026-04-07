package com.kacper.projektgramatematyczna;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WynikiDAO {
    @Insert
    void wstawWynik(Wyniki wyniki);
    @Update
    void aktualizujWynik(Wyniki wyniki);
    @Query("SELECT * FROM wynikiGry WHERE nickGracza = :nick LIMIT 1")
    Wyniki wyswietlWynik(String nick);
    @Query("SELECT * FROM wynikiGry ORDER BY punktyGracza DESC")
    List<Wyniki> wyswietlWszystkieWyniki();
}
