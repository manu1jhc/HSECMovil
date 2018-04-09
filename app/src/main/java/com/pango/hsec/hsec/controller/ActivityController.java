package com.pango.hsec.hsec.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Utils;

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
import java.net.URLEncoder;

public class ActivityController extends AsyncTask<String,Void,Void> {
    ProgressDialog progressDialog;
    IActivity activity;
    public String url;
    //String url_token;
    int con_status;
    String token_auth;
    String opcion;
    String respstring;
    String Resultado="";
    String Tipo="";
    boolean cargaData=true;

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
            String json=strings[0];

            if(opcion == "get") {

                Tipo=json;
                //generarToken(url_token);
                HttpResponse response;
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                //GlobalVariables.token_auth=token_auth;
                //if (opcion == "get" && GlobalVariables.token_auth.length() > 40) {
                get.setHeader("Authorization", "Bearer " + GlobalVariables.token_auth);
                get.setHeader("Content-type", "application/json");

                //}
                //get.abort();
                response = httpClient.execute(get);
                GlobalVariables.con_status = response.getStatusLine().getStatusCode();
                respstring = EntityUtils.toString(response.getEntity());
                //JSONObject respJSON = new JSONObject(respstring);

            }else if(opcion == "post"){

                Tipo= strings.length>1? Tipo=strings[1]:"";
                HttpResponse response;
                InputStream inputStream = null;
                String result = "";

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost (url);

                StringEntity se = new StringEntity(json,"UTF-8");
                httpPost.setEntity(se);
                httpPost.setHeader("Authorization", "Bearer " + GlobalVariables.token_auth);
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);

                GlobalVariables.con_status_post=httpResponse.getStatusLine().getStatusCode();

                inputStream = httpResponse.getEntity().getContent();
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
                String responsepost= GlobalVariables.reemplazarUnicode(result);
                Resultado=responsepost;
            }

        } catch (Throwable e) {
            Log.d("InputStream", e.getLocalizedMessage());
            cargaData=false;

        }
        return null;
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
           if(opcion == "get"){

        if(GlobalVariables.istabs){
            super.onPreExecute();
            progressDialog = ProgressDialog.show(GlobalVariables.view_fragment.getContext(), "", "Cargando");

        } else if (GlobalVariables.isFragment==false) {
            super.onPreExecute();
            progressDialog = ProgressDialog.show((Context) activity, "", "Cargando...");
        }  else if (GlobalVariables.count==1){
            super.onPreExecute();
            progressDialog = ProgressDialog.show(GlobalVariables.view_fragment.getContext(), "", "Cargando");

        } else if(GlobalVariables.flag_up_toast){
            super.onPreExecute();
            GlobalVariables.flag_up_toast=false;
            //if(GlobalVariables.noticias2.size()<GlobalVariables.num_vid) {
            //Toast.makeText((Context) activity,"Actualizando, por favor espere...",Toast.LENGTH_SHORT).show();
       /* }   else if(opcion == "get"&&GlobalVariables.flagDownSc){
            super.onPreExecute();
*/
        }else if(Utils.isActivity){
            super.onPreExecute();
            progressDialog = ProgressDialog.show((Context) activity, "", "Cargando");

        }

        }else if(opcion == "post"){
            if (GlobalVariables.isFragment) {
                progressDialog = ProgressDialog.show(GlobalVariables.view_fragment.getContext(), "", "Enviando");
            }else if(Utils.isActivity){
                super.onPreExecute();
                progressDialog = ProgressDialog.show((Context) activity, "", "Cargando");

            }
        }

    }

    @Override
    protected void onPostExecute(Void result) {
    if(opcion=="get"&&cargaData) {

            switch (GlobalVariables.con_status) {
                case 401:
                    //Toast.makeText((Context) activity,"Ocurrio un error de conexion",Toast.LENGTH_SHORT).show();
                    activity.error("Ocurrio un error de conexion", Tipo);
                    break;
                case 404:
                    //Toast.makeText((Context) activity,"Not Found",Toast.LENGTH_SHORT).show();
                    activity.error("Not Found", Tipo);
                    break;
                case 307:
                    //Toast.makeText((Context) activity,"Se perdio la conexion al servidor",Toast.LENGTH_SHORT).show();
                    activity.error("Se perdio la conexion al servidor", Tipo);
                    break;
                case 500:
                    //Toast.makeText((Context) activity,"Ocurrio un error interno en el servidor",Toast.LENGTH_SHORT).show();
                    activity.error("Ocurrio un error interno en el servidor", Tipo);
                    break;
                default:
                    activity.success(respstring, Tipo);
            }
     //   }

            //progressDialog.dismiss();
            int a = GlobalVariables.count;

            if (GlobalVariables.istabs) {
                //GlobalVariables.istabs=false;//no borrar
                progressDialog.dismiss();

            } else if (GlobalVariables.count == 1 || GlobalVariables.count == 2 || GlobalVariables.count == 3 || GlobalVariables.count == 4) {
                GlobalVariables.count++;
                //GlobalVariables.count=5;
                progressDialog.dismiss();
            } else if (Utils.isActivity) {
                progressDialog.dismiss();
                Utils.isActivity = false;
            }else if(GlobalVariables.isFragment==false){
                GlobalVariables.isFragment=true;

            }

        //mainActivity.success();
    }
    //POST
    else if(opcion=="post"&&cargaData){

        if(GlobalVariables.isFragment){
            progressDialog.dismiss();
            GlobalVariables.isFragment=false;
        }else if(Utils.isActivity){
            progressDialog.dismiss();
            Utils.isActivity=false;
        }

        Resultado=Resultado.substring(1,Resultado.length()-1);
        activity.successpost(Resultado,Tipo);




    }else {
        activity.error("Se perdio la conexión a internet", Tipo);

    }


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
