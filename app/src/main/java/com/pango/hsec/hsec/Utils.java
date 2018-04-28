package com.pango.hsec.hsec;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.NoticiasModel;
import com.pango.hsec.hsec.model.ObsInspDetModel;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.model.PlanModel;
import com.pango.hsec.hsec.util.Compressor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
                return GlobalVariables.getDescripcion(GlobalVariables.Tipo_obs2,observacionModel.CodTipo);

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


            case "SolicitadoPor":
                return planModel.SolicitadoPor;


           /* case "Responsables":
                return planModel.Responsables;
*/
            case "CodActiRelacionada":

                //return planModel.CodActiRelacionada;
                return GlobalVariables.getDescripcion(GlobalVariables.Actividad_obs,planModel.CodActiRelacionada);


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

            return GlobalVariables.getDescripcion(GlobalVariables.Actividad_obs,obsInspDetModel.CodActividadRel);
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

/*
    public static void getListAñio(int anio){
        for(int i=2015;i<anio;i++){
            GlobalVariables.busqueda_anio
        }
    }
    */

    public static String Obtenerfecha(String tempcom_fecha) {
        DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        String fecha="";
        try {
            fecha=formatoRender.format(formatoInicial.parse(tempcom_fecha));
        } catch (ParseException e) {
            e.printStackTrace();
            fecha=tempcom_fecha;
        }

        return fecha;

    }


    public static ArrayList<String> tempObs=new ArrayList<String>();
    public static ObservacionModel observacionModel=new ObservacionModel();
    public static InspeccionModel inspeccionModel=new InspeccionModel();

    public static NoticiasModel noticiasModel=new NoticiasModel();

    public static String fecha_inicio="";

    public static String fecha_fin="";

    public static boolean isActivity=false;


    @Nullable
    public static GaleriaModel getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;

        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
               // return Environment.getExternalStorageDirectory() + "/" + split[1];
                File myFile = new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
                return new GaleriaModel(Environment.getExternalStorageDirectory() + "/" + split[1],"TP03", myFile.length()+"", myFile.getName());
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            } else if (isGoogleDriveFile(uri)) {
                String mimeType = context.getContentResolver().getType(uri);
                Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
                if(returnCursor.moveToFirst()){
                    String fileName = returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    String SizeDoc = returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.SIZE));
                    GaleriaModel temp=new GaleriaModel(uri.getPath(),"TP03", SizeDoc, fileName);
                    temp.uri=uri;
                    return temp;
                }
            }
        }

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null,cursor2=null;
            try {
                cursor2 = context.getContentResolver().query(uri, null, null, null, null);
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()&&cursor2.moveToFirst()) {
                    String fileName = cursor2.getString(cursor2.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    String SizeDoc = cursor2.getString(cursor2.getColumnIndex(OpenableColumns.SIZE));
                    return  new GaleriaModel(cursor.getString(column_index),"TP03", SizeDoc, fileName);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            File myFile = new File(uri.getPath());
            return new GaleriaModel(uri.getPath(),"TP03", myFile.length()+"", myFile.getName());
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGoogleDriveFile(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority());
    }

    public static void copyStreamToFile(File file, InputStream input) {
        try {
            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void DeleteCache(String DiRroot){
        File file = new File(DiRroot);
        String[] files;
        files = file.list();
        for (int i=0; i<files.length; i++) {
            File myFile = new File(file, files[i]);
            myFile.delete();
        }
    }

    public static void closeSoftKeyBoard(Activity context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }

}
