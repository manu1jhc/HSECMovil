package com.pango.hsec.hsec.model;

import java.util.ArrayList;

/**
 * Created by BOB on 28/12/2017.
 */

public class Maestro {
    public String Codigo;
    public String CodTipo;
    public String Descripcion;
   // public ArrayList<Maestro> SubTipos;
    public Maestro() {

    }

    public Maestro(String codTipo, String nombre) {
        CodTipo = codTipo;
        Descripcion = nombre;
    }

    public Maestro(int Codigo,String codTipo, String nombre) {
        this.Codigo=Codigo+"";
        CodTipo = codTipo;
        Descripcion = nombre;
    }

    public Maestro(String nombre) {
        Descripcion = nombre;
    }
    public Maestro(String codTipo, String nombre, ArrayList<Maestro> Subtipos) {
        CodTipo = codTipo;
        Descripcion = nombre;
        //SubTipos = Subtipos;
    }
// para IS


    public String toString()
    {
        return  this.Descripcion ;
    }

    public String getCodTipo() {
        return CodTipo;
    }
    public void setCodTipo(String codTipo) {
        CodTipo = codTipo;
    }
    public String getDescripcion() {
        return Descripcion;
    }
    public void setDescripcion(String nombre) {
        Descripcion = nombre;
    }
}
