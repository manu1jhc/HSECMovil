package com.pango.hsec.hsec.model;

/**
 * Created by Andre on 16/01/2018.
 */

public class PersonaModel {
    public String CodPersona;
    public String Nombres;
    public String NroDocumento;
    public String Cargo;

    public PersonaModel(){}
    public PersonaModel(String CodPersona,String Nombres, String NroDocumento,String Cargo) {
        this.CodPersona = CodPersona;
        this.Nombres = Nombres;
        this.NroDocumento = NroDocumento;
        this.Cargo = Cargo;
    }
}


