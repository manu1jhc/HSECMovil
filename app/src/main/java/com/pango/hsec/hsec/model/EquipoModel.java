package com.pango.hsec.hsec.model;

/**
 * Created by Andre on 29/01/2018.
 */

public class EquipoModel implements Cloneable{

    public String CodPersona;
    public String NroReferencia;
    public String Nombres;
    public String NroDocumento;
    public String Cargo;
    public String Lider;
    public String Estado;


    public EquipoModel(){}
    public EquipoModel(String CodPersona,String Estado){
        this.CodPersona=CodPersona;
        this.Estado=Estado;
    }
    public EquipoModel(String CodPersona,String Nombres, String NroDocumento,String Cargo) {
        this.CodPersona = CodPersona;
        this.Nombres = Nombres;
        this.NroDocumento = NroDocumento;
        this.Cargo = Cargo;
        this.Estado="A";
        this.Lider="0";
        this.NroReferencia="-1";
    }

    public EquipoModel(PersonaModel item){
        this.CodPersona = item.CodPersona;
        this.Nombres = item.Nombres;
        this.NroDocumento = item.NroDocumento;
        this.Cargo = item.Cargo;
        this.Estado="A";
        this.Lider="0";
        this.NroReferencia="-1";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    /*  public EquipoModel clone(){
        EquipoModel temp= new EquipoModel();
        temp.CodPersona=this.CodPersona;
        temp.Lider=this.Lider;
        temp.Estado=this.Estado;
        return temp;
    } */
}
