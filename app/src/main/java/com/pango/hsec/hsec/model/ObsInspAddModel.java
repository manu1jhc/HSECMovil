package com.pango.hsec.hsec.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 18/04/2018.
 */

public class ObsInspAddModel {
    public ObsInspDetModel obsInspDetModel;
    public List<GaleriaModel> listaGaleria =new ArrayList<>();
    public List<GaleriaModel> listaArchivos =new ArrayList<>();
    public ArrayList<PlanModel> Planes= new  ArrayList<>();

    public ObsInspAddModel() {
    }


    public ObsInspAddModel(ObsInspDetModel obsInspDetModel, List<GaleriaModel> listaGaleria, List<GaleriaModel> listaArchivos, ArrayList<PlanModel> planes) {
        this.obsInspDetModel = obsInspDetModel;
        this.listaGaleria = listaGaleria;
        this.listaArchivos = listaArchivos;
        Planes = planes;
    }


}
