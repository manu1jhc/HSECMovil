package com.pango.hsec.hsec.model;

import java.util.ArrayList;

/**
 * Created by Andre on 09/01/2018.
 */

public class GetGaleriaModel {

    public ArrayList<GaleriaModel> Data;
    public int Count;

    public GetGaleriaModel() {
        Data= new ArrayList<>();
        Count=0;
    }

    public GetGaleriaModel(ArrayList<GaleriaModel> data) {
        Data = data;
        Count=Data.size();
    }
}


