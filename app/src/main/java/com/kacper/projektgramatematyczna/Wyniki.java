package com.kacper.projektgramatematyczna;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wynikiGry")
public class Wyniki {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nickGracza;
    private int punktyGracza;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickGracza() {
        return nickGracza;
    }

    public void setNickGracza(String nickGracza) {
        this.nickGracza = nickGracza;
    }

    public int getPunktyGracza() {
        return punktyGracza;
    }

    public void setPunktyGracza(int punktyGracza) {
        this.punktyGracza = punktyGracza;
    }

    public Wyniki(String nickGracza, int punktyGracza) {
        this.id = 0;
        this.nickGracza = nickGracza;
        this.punktyGracza = punktyGracza;
    }
}
