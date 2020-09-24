package com.pango.hsec.hsec.model;

public class ControlCriticoModel {
    public int CodRespuesta;
    public String CodVcc; //Criterio
    public String CodigoCriterio;
    public String CodigoCC;
    public String Respuesta;
    public String Justificacion;
    public String AccionCorrectiva;
    public String Estado; // ControlCritico

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
