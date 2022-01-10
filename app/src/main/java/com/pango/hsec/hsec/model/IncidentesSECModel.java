package com.pango.hsec.hsec.model;

public class IncidentesSECModel {
    public String CodIncidente;
    public String CodAreaHsec;
    public String CodTipoIncidente;
    public String CodSubTipoIncidente;
    public String CodPerReporta;
    public String CodPosicionGer;
    public String CodPosicionSup;
    public String CodClasificaInci;//clasiReal
    public String CodClasiPotencial;
    public String CodActiRelacionada;
    public String CodHha;
    public String CodGrupoRiesgo;
    public String CodRiesgo;

    public String FechaDelSuceso;
    public String HoraDelSuceso;
    public String CodUbicacion;
    public String CodSubUbicacion;
    public String CodUbicacionEspecifica;
    public String DesUbicacion;
    //Detalle
    public String CodTituloInci;
    public String DescripcionIncidente;
    public String CodTurno;
    public String CodContrata;
    public String Conclusiones;
    public String Aprendizajes;
    public String ResumenInfMedico;
    //Afectados
    public String DesSuceso;
    public String AccInmediatas;


    //Causalidad no hay
    public String TipoCausa;
    public String TipoCondicion;

    //Añadidos
    //public String CodUbicacion;
    public String Lugar;
}