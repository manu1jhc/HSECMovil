package com.pango.hsec.hsec.model;

/**
 * Created by Andre on 19/02/2018.
 */

public class SubDetalleModel {
    public String CodTipo;
    public String CodSubtipo;
    public String Descripcion;

    public SubDetalleModel(String CodTipo, String CodSubtipo, String Descripcion) {
        this.CodTipo=CodTipo;
        this.CodSubtipo=CodSubtipo;
        this.Descripcion=Descripcion;

    }
}
