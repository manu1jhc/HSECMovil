package com.pango.hsec.hsec.model;

/**
 * Created by Andre on 02/02/2018.
 */

public class ObsInspDetModel {
    public String Correlativo;
    public String CodInspeccion;
    public String NroDetInspeccion;
    public String Lugar;
    public String CodUbicacion;
    public String CodAspectoObs;
    public String CodActividadRel;
    public String CodNivelRiesgo;
    public String Observacion;
    public String Estado;


    public ObsInspDetModel() {
    }

    public ObsInspDetModel(String correlativo, String codInspeccion, String nroDetInspeccion, String lugar, String codUbicacion, String codAspectoObs, String codActividadRel, String codNivelRiesgo, String observacion, String estado) {
        Correlativo = correlativo;
        CodInspeccion = codInspeccion;
        NroDetInspeccion = nroDetInspeccion;
        Lugar = lugar;
        CodUbicacion = codUbicacion;
        CodAspectoObs = codAspectoObs;
        CodActividadRel = codActividadRel;
        CodNivelRiesgo = codNivelRiesgo;
        Observacion = observacion;
        Estado = estado;
    }
}
