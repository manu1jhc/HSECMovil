package com.pango.hsec.hsec;

import com.pango.hsec.hsec.model.ObservacionModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        DateFormat formatoInicial = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        DateFormat formatoHora = new SimpleDateFormat("h:mm a");
        Date temp= null;
       /* try {
            //temp= formatoInicial.parse(observacionModel.FECHA+"T"+observacionModel.HORA);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        switch (s){
            case "CodObservacion":
                return observacionModel.CodObservacion;
            case "CodAreaHSEC":
                return observacionModel.CodAreaHSEC;
            case "CodNivelRiesgo":
                return observacionModel.CodNivelRiesgo;
            case "ObservadoPor":
                return observacionModel.ObservadoPor;

/*
            case "Fecha":
                return formatoRender.format(temp);
*/
            case "Fecha":
                return observacionModel.Fecha;
            case "Gerencia":
                return observacionModel.Gerencia;
            case "Superint":
                return observacionModel.Superint;
            case "CodUbicacion":
                return observacionModel.CodUbicacion;
            case "Lugar":
                return observacionModel.Lugar;
            case "CodTipo":
                return observacionModel.CodTipo;

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
            case "Gerencia":
                return "Gerencia";
            case "Superint":
                return observacionModel.Superint;
            case "CodUbicacion":
                return observacionModel.CodUbicacion;
            case "Lugar":
                return observacionModel.Lugar;
            case "CodTipo":
                return observacionModel.CodTipo;

            default:
                return "";
        }
    }













}
