package org.example;

public class Veloshop {
    int id;
    String Artikel;
    String Preis;
    String Bestand;

    public Veloshop(int id, String artikel, String preis, String bestand) {
        this.id = id;
        Artikel = artikel;
        Preis = preis;
        Bestand = bestand;
    }
    public Veloshop() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtikel() {
        return Artikel;
    }

    public void setArtikel(String artikel) {
        Artikel = artikel;
    }

    public String getPreis() {
        return Preis;
    }

    public void setPreis(String preis) {
        Preis = preis;
    }

    public String getBestand() {
        return Bestand;
    }

    public void setBestand(String bestand) {
        Bestand = bestand;
    }
}
