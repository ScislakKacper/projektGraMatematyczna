package com.kacper.projektgramatematyczna;

import com.google.gson.annotations.SerializedName;

public class Pytanie {
    @SerializedName("treść")
    private String trescPytania;
    private String odpa, odpb, odpc, odpd;
    private int poprawna;
    private String poziom;

    public Pytanie(String trescPytania, String odpa, String odpb, String odpc, String odpd, int poprawna, String poziom) {
        this.trescPytania = trescPytania;
        this.odpa = odpa;
        this.odpb = odpb;
        this.odpc = odpc;
        this.odpd = odpd;
        this.poprawna = poprawna;
        this.poziom = poziom;
    }

    public String getTrescPytania() {
        return trescPytania;
    }

    public void setTrescPytania(String trescPytania) {
        this.trescPytania = trescPytania;
    }

    public String getOdpa() {
        return odpa;
    }

    public void setOdpa(String odpa) {
        this.odpa = odpa;
    }

    public String getOdpb() {
        return odpb;
    }

    public void setOdpb(String odpb) {
        this.odpb = odpb;
    }

    public String getOdpc() {
        return odpc;
    }

    public void setOdpc(String odpc) {
        this.odpc = odpc;
    }

    public String getOdpd() {
        return odpd;
    }

    public void setOdpd(String odpd) {
        this.odpd = odpd;
    }

    public int getPoprawna() {
        return poprawna;
    }

    public void setPoprawna(int poprawna) {
        this.poprawna = poprawna;
    }

    public String getPoziom() {
        return poziom;
    }

    public void setPoziom(String poziom) {
        this.poziom = poziom;
    }
}
