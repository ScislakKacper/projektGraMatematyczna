package com.kacper.projektgramatematyczna;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Wyniki.class}, version = 1)
public abstract class BazaDanychWynikow extends RoomDatabase {
    public abstract WynikiDAO zwrocWynikiDao();
    public static BazaDanychWynikow instancja;
    public static BazaDanychWynikow zwrocInstancjeBazyDanych(Context context){
        if(instancja == null){
            instancja = Room.databaseBuilder(context, BazaDanychWynikow.class, "wynikiDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return instancja;
    }
}
