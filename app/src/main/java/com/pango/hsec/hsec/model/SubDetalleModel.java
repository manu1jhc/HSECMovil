package com.pango.hsec.hsec.model;

/**
 * Created by Andre on 19/02/2018.
 */

public class SubDetalleModel {
    public String CodTipo;
    public String CodSubtipo; /// esta null
    public String Descripcion;
    public boolean estado;


    public SubDetalleModel(String CodTipo, String CodSubtipo, String Descripcion) {
        this.CodTipo=CodTipo;
        this.CodSubtipo=CodSubtipo;
        this.Descripcion=Descripcion;

    }


    public SubDetalleModel (String CodTipo, String CodSubtipo, String Descripcion, boolean estado){
        this.CodTipo = CodTipo;
        this.CodSubtipo = CodSubtipo;
        this.Descripcion = Descripcion;
        this.estado = estado;
    }


}
