package com.pango.hsec.hsec.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Andre on 13/12/2017.
 */

public class GetTokenController extends AsyncTask<String,Void,Void> {
    IActivity activity;
    String url_token;
    ProgressDialog progressDialog;

    //int con_status;
    String token_auth;

    public GetTokenController(String url_token, IActivity activity) {
        this.activity = activity;
        this.url_token = url_token;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(url_token);
            get.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(get);
            String respstring2 = EntityUtils.toString(response.getEntity());
            GlobalVariables.con_status = response.getStatusLine().getStatusCode();
            if (respstring2.equals("")) {
                GlobalVariables.token_auth = "";
                // GlobalVariables.con_status =0;
            } else {
                GlobalVariables.token_auth = respstring2.substring(1, respstring2.length() - 1);
                //Utils.token=respstring2.substring(1, respstring2.length() - 1);

            }

            //conseguir la data de usuario

            Obtener_dataUser(GlobalVariables.token_auth);



        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = ProgressDialog.show((Context) activity, "", "Iniciando sesión");
       /* if (opcion == "get") {
            super.onPreExecute();
            progressDialog = ProgressDialog.show((Context) activity, "", "Iniciando sesión");
        }*/
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

                activity.success(""+GlobalVariables.con_status,"");

                //activity.success(respstring);
        }

        progressDialog.dismiss();
        //mainActivity.success();

    }


    public void Obtener_dataUser(String Token){

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(GlobalVariables.Url_base+"usuario/getdata/");
            get.setHeader("Authorization", "Bearer " +Token);

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




}
