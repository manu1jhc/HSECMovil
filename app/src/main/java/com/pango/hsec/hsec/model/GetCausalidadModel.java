package com.pango.hsec.hsec.model;
import java.util.ArrayList;

public class GetCausalidadModel {
    public ArrayList<CausalidadModel> Data;
    public int Count;

    public GetCausalidadModel(){
        this.Data= new ArrayList<>();
        this.Count=0;
    }

    public int size(){
        return Data.size();
    }

}