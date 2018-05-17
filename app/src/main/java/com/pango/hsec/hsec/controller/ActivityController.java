package com.pango.hsec.hsec.controller;

import android.app.Activity;
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
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ActivityController extends AsyncTask<String,Void,Void> {
    ProgressDialog progressDialog;
    IActivity activity;
    Activity ActContext;
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
    public ActivityController(String opcion, String url, IActivity activity, Activity ActContext) {
        this.activity = activity;
        this.url = url;
        this.ActContext = ActContext;
        this.opcion = opcion;
    }

    @Override
    protected void onPreExecute() {

        String paginas[]= opcion.split("-");
        int pag=1;
        if(paginas.length>1) pag=Integer.parseInt(paginas[1]);
        if(pag==1){
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ActContext, "", "Cargando");
        }

    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            String json=strings[0];

            if(opcion.contains("get")) {

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

            }else if(opcion.contains("post")){

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
    protected void onPostExecute(Void result) {

    if(cargaData) {

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
                    if(opcion.contains("post"))
                        try {
                            activity.successpost(Resultado,Tipo);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    else try {
                        activity.success(respstring, Tipo);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        String paginas[]= opcion.split("-");
        int pag=1;
        if(paginas.length>1) pag=Integer.parseInt(paginas[1]);
        if(pag==1) progressDialog.dismiss();
    }
    else {
        progressDialog.dismiss();
        activity.error("Ocurrio un error al intentar enviar peticion.", Tipo);
    }

    }

}
