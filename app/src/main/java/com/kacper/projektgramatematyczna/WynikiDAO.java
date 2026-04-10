package com.kacper.projektgramatematyczna;

import androidx.lifecycle.LiveData;
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
    Wyniki pobierzWynik(String nick);
    @Query("SELECT * FROM wynikiGry WHERE nickGracza = :nick LIMIT 1")
    LiveData<Wyniki> wyswietlWynik(String nick);
    @Query("SELECT * FROM wynikiGry ORDER BY punktyGracza DESC")
    LiveData<List<Wyniki>> wyswietlWszystkieWyniki();
    @Query("SELECT * FROM wynikiGry ORDER BY punktyGracza DESC LIMIT :limit OFFSET :offset")
    List<Wyniki> pobierzStrone(int limit, int offset);
    @Query("SELECT COUNT(*) FROM wynikiGry")
    int policzWszystkie();
}
