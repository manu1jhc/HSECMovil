package com.pango.hsec.hsec.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.GetMaestroModel;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.UsuarioModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by Andre on 13/12/2017.
 */

public class GetTokenController extends AsyncTask<String,Void,Void> {
    IActivity activity;
    String url_token;
    ProgressBar progressDialog;

    //int con_status;
    String token_auth;

    public GetTokenController(String url_token, IActivity activity, ProgressBar prog) {
        this.activity = activity;
        this.url_token = url_token;
        this.progressDialog=prog;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        if(progressDialog!= null)progressDialog.setVisibility(View.VISIBLE);
      //  progressDialog = ProgressDialog.show((Context) activity, "", "Iniciando sesión");
       /* if (opcion == "get") {
            super.onPreExecute();
            progressDialog = ProgressDialog.show((Context) activity, "", "Iniciando sesión");
        }*/
    }

    @Override
    protected Void doInBackground(String... strings) {
        String json=strings[0];
        HttpClient httpClient = Utils.getNewHttpClient();
        InputStream inputStream = null;
        String result = "";

        try {

            HttpPost httpPost = new HttpPost (url_token);

            StringEntity se = new StringEntity(json,"UTF-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            GlobalVariables.con_status=httpResponse.getStatusLine().getStatusCode();

            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null&&GlobalVariables.con_status==200)
                result = convertInputStreamToString(inputStream);
            else  result = "Error al obtener token!";

            String responsepost= GlobalVariables.reemplazarUnicode(result);
            GlobalVariables.token_auth = responsepost.substring(1, responsepost.length() - 1);

            //conseguir la data de usuario
            if(GlobalVariables.token_auth.length()>40 && progressDialog!= null) {
                Obtener_dataUser();
                LoadData();
                LoadDataMuro();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void result) {

        switch (GlobalVariables.con_status) {
            case 0:
                Toast.makeText((Context) activity,"No hay conexion a internet",Toast.LENGTH_SHORT).show();
                break;
            case 401:
                Toast.makeText((Context) activity,"Ocurrio un error de conexion",Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Toast.makeText((Context) activity,"Not Found",Toast.LENGTH_SHORT).show();
                break;
            case 307:
                Toast.makeText((Context) activity,"Se perdio la conexion al servidor",Toast.LENGTH_SHORT).show();
                break;
            case 500:
                Toast.makeText((Context) activity,"Ocurrio un error interno en el servidor",Toast.LENGTH_SHORT).show();
                break;

            default:
                if(GlobalVariables.token_auth.length()>40){
                    try {
                        activity.success(""+GlobalVariables.con_status,"1");
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                }else{
                    activity.error(GlobalVariables.token_auth,"");
                    //Toast.makeText((Context) activity,GlobalVariables.token_auth,Toast.LENGTH_SHORT).show();
                    break;
                }
               // activity.success(""+GlobalVariables.con_status,"");

                //activity.success(respstring);
        }
        if(progressDialog!=null)progressDialog.setVisibility(View.INVISIBLE);
        //mainActivity.success();

    }


    public void Obtener_dataUser(){

        try {

            HttpClient httpClient = Utils.getNewHttpClient();
            HttpGet get = new HttpGet(GlobalVariables.Url_base+"usuario/getdata/");
            get.setHeader("Authorization", "Bearer " +GlobalVariables.token_auth);

            //get.setHeader("Authorization", "Bearer " + GlobalVariables.token_auth);
            HttpResponse response;

            response = httpClient.execute(get);
            String respstring2 = EntityUtils.toString(response.getEntity());
            int con_status = response.getStatusLine().getStatusCode();
            GlobalVariables.json_user=respstring2;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadDataMuro() {

       String url=GlobalVariables.Url_base+"Muro/GetMuro/"+"1"+"/"+"7";
        try {
            HttpClient httpClient = Utils.getNewHttpClient();
            HttpGet get = new HttpGet(url);
            get.setHeader("Authorization", "Bearer " +GlobalVariables.token_auth);

            //get.setHeader("Authorization", "Bearer " + GlobalVariables.token_auth);
            HttpResponse response;

            response = httpClient.execute(get);
            String respstring2 = EntityUtils.toString(response.getEntity());
            int con_status = response.getStatusLine().getStatusCode();
            Gson gson = new Gson();
            GlobalVariables.listaGlobal = gson.fromJson(respstring2, GetPublicacionModel.class).Data;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void LoadData(){
        GlobalVariables.loadObs_Detalles();
        SharedPreferences VarMaestros = ((Context)activity).getSharedPreferences("HSEC_Maestros", Context.MODE_PRIVATE);
        String ListaMaestro = VarMaestros.getString("MaestroAll","");

        if(ListaMaestro=="" || ListaMaestro.contains("Count\":-1")){
            try {
                HttpClient httpClient = Utils.getNewHttpClient();
                HttpGet get = new HttpGet(GlobalVariables.Url_base+"Maestro/GetTipoMaestro/ALL");
                get.setHeader("Authorization", "Bearer " +GlobalVariables.token_auth);
                //get.setHeader("Authorization", "Bearer " + GlobalVariables.token_auth);
                HttpResponse response;

                response = httpClient.execute(get);
                if(response.getStatusLine().getStatusCode()==200){
                    ListaMaestro = EntityUtils.toString(response.getEntity());
                    SharedPreferences.Editor editor = VarMaestros.edit();
                    editor.putString("MaestroAll", String.valueOf(ListaMaestro));
                    editor.commit();
                    Setdata(ListaMaestro);
                }
                else  Toast.makeText((Context) activity,"Ocurrio un error al cargar datos del sistemas",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            if(GlobalVariables.Ubicaciones_obs.isEmpty()) Setdata(ListaMaestro);
        }
    }

    public void Setdata(String data){
        Gson gson = new Gson();
        GetMaestroModel getMaestroModel = gson.fromJson(data, GetMaestroModel.class);
        for (Maestro item: getMaestroModel.Data ) {
            switch (item.Codigo)
            {
                case "UBIC":
                    GlobalVariables.Ubicaciones_obs.add(item);
                    break;
                case "GERE":
                    GlobalVariables.Gerencia.add(item);
                    break;
                case "SUPE":
                    GlobalVariables.SuperIntendencia.add(item);
                    break;
                case "PROV":
                    GlobalVariables.Contrata.add(item);
                    break;
                //observacion
                case "HHAR":
                    GlobalVariables.HHA_obs.add(item);
                    break;
                case "ACTR":
                    GlobalVariables.Actividad_obs.add(item);
                    break;
                case "TPOB":
                    GlobalVariables.Tipo_obs2.add(item);
                    break;
                case "ESOB":
                    GlobalVariables.Estado_obs.add(item);
                    break;
                case "EROB":
                    GlobalVariables.Error_obs.add(item);
                    break;
                case "ACSE":
                    GlobalVariables.Acto_obs.add(item);
                    break;
                case "COSE":
                    GlobalVariables.Condicion_obs.add(item);
                    break;
                //inspecciones
                case "ASPO":
                    GlobalVariables.Aspecto_Obs.add(item);
                    break;
                case "TPIN":
                    GlobalVariables.Tipo_insp.add(item);
                    break;
                //Plan de Accion
                case "AREA":
                    GlobalVariables.Area_obs.add(item);
                    break;
                case "TPAC":
                    GlobalVariables.Tipo_Plan.add(item);
                    break;
                case "TROL":
                    GlobalVariables.Roles.add(item);
                    break;

                case "CEMP":
                    GlobalVariables.C_Empresa.add(item);
                    break;
                case "CLUG":
                    GlobalVariables.C_Lugar.add(item);
                    break;
                case "CTEM":
                    GlobalVariables.C_Tema.add(item);
                    break;
                case "CTIP":
                    GlobalVariables.C_Tipo.add(item);
                    break;
                case "CSAL":
                    GlobalVariables.C_Tipo.add(item);
                    break;
                case "OBCO":
                    GlobalVariables.Correccion_obs.add(item);
                    break;
                case "PELF":
                    GlobalVariables.Peligro_fatal.add(item);
                    break;
                case "UEPP":
                    GlobalVariables.EPP.add(item);
                    if(!item.CodTipo.contains("."))
                        GlobalVariables.TipoEpp_obs.add(item);
                    break;
                case "COVI":
                    GlobalVariables.Covid.add(item);
                    break;
                case "CLAR":
                    GlobalVariables.ClasReal.add(item);
                    break;
                case "CLAP":
                    GlobalVariables.ClasPotencial.add(item);
                    break;
                case "GRIE":
                    GlobalVariables.GrupRiesgo.add(item);
                    break;
                case "RIES":
                    GlobalVariables.Riesgo.add(item);
                    break;
                case "TINC":
                    GlobalVariables.TituIncidente.add(item);
                    break;
                case "TURN":
                    GlobalVariables.Turno.add(item);
                    break;
                case "TCAU":
                    GlobalVariables.TipoCausa.add(item);
                    break;
                case "TCON":
                    GlobalVariables.TipoCond.add(item);
                    break;
                case "TPNC":
                    GlobalVariables.Tipo_Inc.add(item);
                    break;


                /*default:
                    break;*/
            }
        }

        GlobalVariables.Ubicacion_obs=GlobalVariables.loadUbicacion("",1);
    }

}
