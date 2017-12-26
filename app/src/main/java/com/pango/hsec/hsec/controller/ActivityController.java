package com.pango.hsec.hsec.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ActivityController extends AsyncTask<String,Void,Void> {
    ProgressDialog progressDialog;
    IActivity activity;
    String url;
    //String url_token;
    int con_status;
    String token_auth;
    String opcion;
    String respstring;
    View v;
    public ActivityController(String opcion, String url, IActivity activity) {
        this.activity = activity;
        this.url = url;
        //this.url_token = url_token;
        this.opcion = opcion;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            //generarToken(url_token);
            HttpResponse response;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            //GlobalVariables.token_auth=token_auth;
            //if (opcion == "get" && GlobalVariables.token_auth.length() > 40) {
            get.setHeader("Authorization", "Bearer " + GlobalVariables.token_auth);
            //}
            response = httpClient.execute(get);
            GlobalVariables.con_status = response.getStatusLine().getStatusCode();
            respstring = EntityUtils.toString(response.getEntity());

            //JSONObject respJSON = new JSONObject(respstring);

        } catch (Throwable e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        if (opcion == "get"&&GlobalVariables.isFragment==false) {
            super.onPreExecute();
            progressDialog = ProgressDialog.show((Context) activity, "", "Iniciando sesión");
        }  else if (GlobalVariables.count==1){
            super.onPreExecute();
            progressDialog = ProgressDialog.show(GlobalVariables.view_fragment.getContext(), "", "Cargando");

        } else if(opcion=="get"&&GlobalVariables.flag_up_toast){
            super.onPreExecute();

            GlobalVariables.flag_up_toast=false;
            //if(GlobalVariables.noticias2.size()<GlobalVariables.num_vid) {
            //Toast.makeText((Context) activity,"Actualizando, por favor espere...",Toast.LENGTH_SHORT).show();

       /* }   else if(opcion == "get"&&GlobalVariables.flagDownSc){
            super.onPreExecute();
*/
        }


    }

    @Override
    protected void onPostExecute(Void result) {

        switch (GlobalVariables.con_status) {
            case 401:
                //Toast.makeText((Context) activity,"Ocurrio un error de conexion",Toast.LENGTH_SHORT).show();
                activity.error("Ocurrio un error de conexion");
                break;
            case 404:
                //Toast.makeText((Context) activity,"Not Found",Toast.LENGTH_SHORT).show();
                activity.error("Not Found");

                break;
            case 307:
                //Toast.makeText((Context) activity,"Se perdio la conexion al servidor",Toast.LENGTH_SHORT).show();
                activity.error("Se perdio la conexion al servidor");

                break;
            case 500:
                //Toast.makeText((Context) activity,"Ocurrio un error interno en el servidor",Toast.LENGTH_SHORT).show();
                activity.error("Ocurrio un error interno en el servidor");
                break;

            default:
                activity.success(respstring);
        }





            if(GlobalVariables.count==1) {
                GlobalVariables.count++;
                progressDialog.dismiss();

            }
        //mainActivity.success();

    }



/*  GENERAR TOKEN */
/*
public void generarToken(String url_token){

    if(url_token.equals("")){
        token_auth = "";
    }else {
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(url_token);
            get.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(get);
            String respstring2 = EntityUtils.toString(response.getEntity());
            con_status = response.getStatusLine().getStatusCode();
            if (respstring2.equals("")) {
                token_auth = "";
                // GlobalVariables.con_status =0;
            } else {
                token_auth = respstring2.substring(1, respstring2.length() - 1);
                //Utils.token=respstring2.substring(1, respstring2.length() - 1);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    }
*/

}
