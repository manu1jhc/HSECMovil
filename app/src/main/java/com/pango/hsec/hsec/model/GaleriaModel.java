package com.pango.hsec.hsec.model;

import android.net.Uri;

/**
 * Created by Andre on 08/01/2018.
 */

public class GaleriaModel {

    public int Correlativo;
    public String Url;
    public String Tamanio;
    public String TipoArchivo;
    public String Descripcion;
    public String Estado;
    public Uri uri;

    public GaleriaModel(){}
    public GaleriaModel(String Url,String TipoArchivo, String Tamanio,String Descripcion) {
        this.Correlativo=-1;
        this.Url = Url;
        this.Tamanio = Tamanio;
        this.TipoArchivo = TipoArchivo;
        this.Descripcion = Descripcion;
    }
}
