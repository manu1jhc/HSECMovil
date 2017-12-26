package com.pango.hsec.hsec.model;

/**
 * Created by Andre on 19/12/2017.
 */

public class Tipo {
    public String CodTipo;
    public String Nombre;

    public Tipo() {
    }

    public Tipo(String codTipo, String nombre) {
        CodTipo = codTipo;
        Nombre = nombre;
    }
    public String getCodTipo() {
        return CodTipo;
    }
    public void setCodTipo(String codTipo) {
        CodTipo = codTipo;
    }
    public String getNombre() {
        return Nombre;
    }
    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
