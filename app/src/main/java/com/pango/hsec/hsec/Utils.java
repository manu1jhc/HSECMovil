package com.pango.hsec.hsec;

import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.NoticiasModel;
import com.pango.hsec.hsec.model.ObsInspDetModel;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.model.PlanModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Andre on 19/12/2017.
 */

public class Utils {



    public static  String ChangeUrl(String url){

        String urlOk=url.replaceAll("\\s","%20").replaceAll("ó","%f3").replaceAll("á","%e1").replaceAll("é","%e9")
                .replaceAll("í","%ed").replaceAll("ú","%fa").replaceAll("ñ","%f1").replaceAll("Ñ","%d1")
                .replaceAll("Á","%c1").replaceAll("É","%c9").replaceAll("Í","%cd").replaceAll("Ó","%d3")
                .replaceAll("Ú","%da");

        return urlOk;
    }



    public static String getTicketProperty(ObservacionModel observacionModel, String s) {
        DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        DateFormat formatoHora = new SimpleDateFormat("h:mm a");
        Date temp= null;
        try {
            temp= formatoInicial.parse(observacionModel.Fecha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String cad;
        switch (s){
            case "CodObservacion":
                return observacionModel.CodObservacion;
            case "CodAreaHSEC":
                //return observacionModel.CodAreaHSEC;
                return GlobalVariables.getDescripcion(GlobalVariables.Area_obs,observacionModel.CodAreaHSEC);

            case "CodNivelRiesgo":
                //return observacionModel.CodNivelRiesgo;
                return GlobalVariables.getDescripcion(GlobalVariables.NivelRiesgo_obs,observacionModel.CodNivelRiesgo);

            case "ObservadoPor":
                return observacionModel.ObservadoPor;

            case "Fecha":
                return formatoRender.format(temp);

            case "Hora":
                return formatoHora.format(temp).replace(". ","").replace(".","");
            case "Gerencia":

                return GlobalVariables.getDescripcion(GlobalVariables.Gerencia,observacionModel.Gerencia).trim().replace("=","");



            case "Superint":
                //return observacionModel.Superint;


                return GlobalVariables.getDescripcion(GlobalVariables.SuperIntendencia,observacionModel.Gerencia+"."+observacionModel.Superint).trim().replace("=","");


            case "CodUbicacion":
                String[] parts = new String[0];
                cad=observacionModel.CodUbicacion;
                if (cad==null) {
                    //parts[0]=("");
                    return "";
                }else {
                    parts = cad.split("\\.");
                    String a = parts[0];
                    return GlobalVariables.getDescripcion(GlobalVariables.Ubicaciones_obs,parts[0]);

                }

            case "CodSubUbicacion":
                cad=observacionModel.CodUbicacion;
                String[] parts2=cad.split("\\.");
                String b = parts2[0]+"."+parts2[1];
                return GlobalVariables.getDescripcion(GlobalVariables.Ubicaciones_obs,parts2[0]+"."+parts2[1]);
            case "UbicacionEsp":
                cad=observacionModel.CodUbicacion;
                return GlobalVariables.getDescripcion(GlobalVariables.Ubicaciones_obs,cad);



            case "Lugar":
                return observacionModel.Lugar;
            case "CodTipo":
                return GlobalVariables.getDescripcion(GlobalVariables.Tipo_obs,observacionModel.CodTipo);

            default:
                return "";
        }
    }







    public static String getDataIzq(ObservacionModel observacionModel, String s) {
       /* DateFormat formatoInicial = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        DateFormat formatoHora = new SimpleDateFormat("h:mm a");
        Date temp= null;*/
       /* try {
            //temp= formatoInicial.parse(observacionModel.FECHA+"T"+observacionModel.HORA);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        //public static String[] obsDetListIzq ={"Codigo","Area","Nivel de riesgo","Observado Por","Fecha",
        // "Gerencia","Superintendencia","Ubicacion","Lugar","Tipo"};


        switch (s){
            case "CodObservacion":
                return "Codigo";
            case "CodAreaHSEC":
               // observacionModel.CodAreaHSEC
                return "Area";
            case "CodNivelRiesgo":
                return "Nivel de riesgo";
            case "ObservadoPor":
                return "Observado Por";
            case "Fecha":
                return "Fecha";
            case "Hora":
                return "Hora";
            case "Gerencia":
                return "Gerencia";
            case "Superint":
                return observacionModel.Superint;
            case "CodUbicacion":
                return observacionModel.CodUbicacion;

            case "CodSubUbicacion":
                return "Sub Ubicación";
            case "UbicacionEsp":
                return "Ubicación específica";
            case "Lugar":
                return observacionModel.Lugar;
            case "CodTipo":
                return observacionModel.CodTipo;

            default:
                return "";
        }
    }






    public static String getPlan(PlanModel planModel, String s) {
        DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        DateFormat formatoHora = new SimpleDateFormat("h:mm a");
        Date temp= null;
        Date tempIn= null;
        Date tempFin= null;

        try {
            temp= formatoInicial.parse(planModel.FechaSolicitud);
            tempIn=formatoInicial.parse(planModel.FecComprometidaInicial);
            tempFin=formatoInicial.parse(planModel.FecComprometidaFinal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String cad;
        switch (s){
            case "CodAccion":
                return planModel.CodAccion;
            case "NroDocReferencia":
                //return observacionModel.CodAreaHSEC;
                return planModel.NroDocReferencia;

            case "CodAreaHSEC":
                //return observacionModel.CodNivelRiesgo;
                return GlobalVariables.getDescripcion(GlobalVariables.Area_obs,planModel.CodAreaHSEC);

            case "CodNivelRiesgo":
                return GlobalVariables.getDescripcion(GlobalVariables.NivelRiesgo_obs,planModel.CodNivelRiesgo);

            case "DesPlanAccion":
                return planModel.DesPlanAccion;

            case "FechaSolicitud":
                return formatoRender.format(temp);
                //return planModel.FechaSolicitud;


            case "CodEstadoAccion":



                return GlobalVariables.getDescripcion(GlobalVariables.Estado_Plan,planModel.CodEstadoAccion);


            case "CodSolicitadoPor":
                return planModel.CodSolicitadoPor;


            case "CodResponsable":

                return planModel.CodResponsable;
            case "CodActiRelacionada":

                //return planModel.CodActiRelacionada;
                return GlobalVariables.getDescripcion(GlobalVariables.Actv_Relacionada,planModel.CodActiRelacionada);


            case "CodReferencia":
                return GlobalVariables.getDescripcion(GlobalVariables.Referencia_Plan,planModel.CodReferencia);

           // return planModel.CodReferencia;

            case "CodTipoAccion":
                //return planModel.CodTipoAccion;
                return GlobalVariables.getDescripcion(GlobalVariables.Tipo_Plan,planModel.CodTipoAccion);

            case "FecComprometidaInicial":
                return formatoRender.format(tempIn);
            //return planModel.FecComprometidaInicial;

            case "FecComprometidaFinal":
                return formatoRender.format(tempFin);

            //return planModel.FecComprometidaFinal;


            default:
                return "";
        }


    }

    public static String getInspeccionData(InspeccionModel inspeccionModel, String s) {
        DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        DateFormat formatoHora = new SimpleDateFormat("h:mm a");
        Date temp= null;
        Date tempP= null;
        try {
            tempP= formatoInicial.parse(inspeccionModel.FechaP);
            temp= formatoInicial.parse(inspeccionModel.Fecha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String cad;
        switch (s){
            case "CodInspeccion":
                return inspeccionModel.CodInspeccion;
            case "Gerencia":
                //return observacionModel.CodAreaHSEC;
                return GlobalVariables.getDescripcion(GlobalVariables.Gerencia,inspeccionModel.Gerencia).trim().replace("=","");

            case "SuperInt":
                //return observacionModel.CodNivelRiesgo;
                return GlobalVariables.getDescripcion(GlobalVariables.SuperIntendencia,inspeccionModel.Gerencia+"."+inspeccionModel.SuperInt).trim().replace("=","");

            case "CodContrata":
                return inspeccionModel.CodContrata;

            case "FechaP":
                return formatoRender.format(tempP);

            case "Fecha":

                return formatoRender.format(temp);

            case "Hora":
                return formatoHora.format(temp).replace(". ","").replace(".","");


            case "CodUbicacion":
                String[] parts = new String[0];
                cad=inspeccionModel.CodUbicacion;
                if (cad==null) {
                    //parts[0]=("");
                    return "";
                }else {
                    parts = cad.split("\\.");
                    String a = parts[0];
                    return GlobalVariables.getDescripcion(GlobalVariables.Ubicaciones_obs,parts[0]);

                }

            case "CodSubUbicacion":
                cad=inspeccionModel.CodUbicacion;
                String[] parts2=cad.split("\\.");
                String b = parts2[0]+"."+parts2[1];
                return GlobalVariables.getDescripcion(GlobalVariables.Ubicaciones_obs,parts2[0]+"."+parts2[1]);


            case "CodTipo":
                return GlobalVariables.getDescripcion(GlobalVariables.Tipo_insp,inspeccionModel.CodTipo);

            default:
                return "";
        }
    }



    public static String getObsDetData(ObsInspDetModel obsInspDetModel, String s) {
        DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        DateFormat formatoHora = new SimpleDateFormat("h:mm a");
        Date temp= null;
        Date tempP= null;
        try {
            tempP= formatoInicial.parse(inspeccionModel.FechaP);
            temp= formatoInicial.parse(inspeccionModel.Fecha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String cad;

        switch (s){
            case "CodInspeccion":
                return obsInspDetModel.CodInspeccion;
            case "NroDetInspeccion":
                return obsInspDetModel.NroDetInspeccion;
                //return GlobalVariables.getDescripcion(GlobalVariables.Gerencia,inspeccionModel.Gerencia).trim().replace("=","");

            case "Lugar":
                return obsInspDetModel.Lugar;
                //return GlobalVariables.getDescripcion(GlobalVariables.SuperIntendencia,inspeccionModel.SuperInt).trim().replace("=","");

            case "CodUbicacion":
                String[] parts = new String[0];
                cad=obsInspDetModel.CodUbicacion;
                if (cad==null) {
                    //parts[0]=("");
                    return "";
                }else {
                    parts = cad.split("\\.");
                    String a = parts[0];
                    return GlobalVariables.getDescripcion(GlobalVariables.Ubicaciones_obs,parts[0]);

                }

            case "CodAspectoObs":
               /* cad=inspeccionModel.CodUbicacion;
                String[] parts2=cad.split("\\.");
                String b = parts2[0]+"."+parts2[1];
                return GlobalVariables.getDescripcion(GlobalVariables.Ubicaciones_obs,parts2[0]+"."+parts2[1]);*/
                return GlobalVariables.getDescripcion(GlobalVariables.Aspecto_Obs,obsInspDetModel.CodAspectoObs);

                        //obsInspDetModel.CodAspectoObs;

            case "CodActividadRel":

            return GlobalVariables.getDescripcion(GlobalVariables.Actv_Relacionada,obsInspDetModel.CodActividadRel);
            //return obsInspDetModel.CodActividadRel;

            case "CodNivelRiesgo":
                //return obsInspDetModel.CodNivelRiesgo;
                return GlobalVariables.getDescripcion(GlobalVariables.NivelRiesgo_obs,obsInspDetModel.CodNivelRiesgo);

            case "Observacion":
                return obsInspDetModel.Observacion;

            default:
                return "";
        }
    }







    public static ArrayList<String> tempObs=new ArrayList<String>();
    public static ObservacionModel observacionModel=new ObservacionModel();
    public static InspeccionModel inspeccionModel=new InspeccionModel();

    public static NoticiasModel noticiasModel=new NoticiasModel();

    public static String fecha_inicio="";

    public static String fecha_fin="";

    public static boolean isActivity=false;
}
