package com.pango.hsec.hsec.model;

import java.util.ArrayList;

public class GetControlCriticoModel {
    public ArrayList<ControlCriticoModel> Data;
    public int Count;

    public GetControlCriticoModel(){
        this.Data= new ArrayList<>();
        this.Count=0;
    }

    public int size(){
        return Data.size();
    }

}

