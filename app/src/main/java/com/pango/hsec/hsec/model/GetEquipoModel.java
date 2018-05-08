package com.pango.hsec.hsec.model;

import java.util.ArrayList;

/**
 * Created by Andre on 29/01/2018.
 */

public class GetEquipoModel {

    public ArrayList<EquipoModel> Data;
    public int Count;

    public GetEquipoModel(){
        this.Data= new ArrayList<>();
        this.Count=0;
    }

    public int size(){
        return Data.size();
    }
}
