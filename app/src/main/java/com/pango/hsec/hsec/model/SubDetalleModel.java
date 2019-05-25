package com.pango.hsec.hsec.model;

/**
 * Created by Andre on 19/02/2018.
 */

public class SubDetalleModel {
    public String Codigo;
    public String CodTipo;
    public String CodSubtipo; /// esta null
    public String Descripcion;
    public String Estado;
    public Boolean Check;

    public SubDetalleModel(String CodTipo, String CodSubtipo, String Descripcion) {
        this.CodTipo=CodTipo;
        this.CodSubtipo=CodSubtipo;
        this.Descripcion=Descripcion;
        this.Check=false;
    }


    public SubDetalleModel (String Codigo,String CodTipo, String CodSubtipo, String Descripcion){
        this.CodTipo = CodTipo;
        this.CodSubtipo = CodSubtipo;
        this.Descripcion = Descripcion;
        this.Codigo = Codigo;
        this.Check=false;
    }


}
