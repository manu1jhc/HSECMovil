package com.pango.hsec.hsec.model;

/**
 * Created by Andre on 19/12/2017.
 */

public class Area {
    public String CodArea;
    public String Nombre;

    public Area(){

    }

    public Area(String codArea, String nombre) {
        CodArea = codArea;
        Nombre = nombre;
    }

    public String getCodArea() {
        return CodArea;
    }

    public void setCodArea(String codArea) {
        CodArea = codArea;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
