package com.pango.hsec.hsec.Ficha;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.DocumentoAdapter;
import com.pango.hsec.hsec.adapter.GaleriaAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.AccionMejoraModel;
import com.pango.hsec.hsec.model.GaleriaModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RegistroAtencion extends AppCompatActivity implements IActivity {
    RecyclerView gridView,listView;
    AccionMejoraModel accionMejoraModel;
    TextView tx_responsable,tx_fecha,tx_avance,tx_tarea;
    GaleriaAdapter galeriaAdapter;
    private static final short REQUEST_CODE = 6545;

    ArrayList<GaleriaModel> DataDocs=new ArrayList<GaleriaModel>();
    ArrayList<GaleriaModel> DataImg=new ArrayList<GaleriaModel>();
    RelativeLayout rel_otros;
    LinearLayout ll_galeria;
    DocumentoAdapter documentoAdapter;
    boolean permiso=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_atencion);
        tx_responsable=findViewById(R.id.tx_responsable);
        tx_fecha=findViewById(R.id.tx_fecha);
        tx_avance=findViewById(R.id.tx_avance);
        tx_tarea=findViewById(R.id.tx_tarea);
        gridView = (RecyclerView) findViewById(R.id.rec_galeria);
        listView = (RecyclerView) findViewById(R.id.list_docs);
        ll_galeria=findViewById(R.id.ll_galeria);

        rel_otros=findViewById(R.id.rel_otros);

        Bundle datos = getIntent().getExtras();
        String url= GlobalVariables.Url_base+"AccionMejora/GetId/"+datos.getString("Correlativo");
        ActivityController obj = new ActivityController("get", url, RegistroAtencion.this,this);
        obj.execute("");

    }

    public void close(View view){finish();}

    public void setData(){


    }

    private static boolean isDownloadManagerAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }


    public void checkSelfPermission() {

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);

        } else {

            executeDownload();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    executeDownload();
                } else {
                    // permission denied!
                    Toast.makeText(this, "Please give permissions ", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void executeDownload() {


        permiso=true;

/*
        // registrer receiver in order to verify when download is complete
        //registerReceiver(new DonwloadCompleteReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(cadMod));
        //request.setDescription("Downloading file " + NAME_FILE);
        //request.setTitle("Downloading");
        // in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nombre_doc);

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
*/
    }


    @Override
    public void success(String data, String Tipo) {

        Gson gson = new Gson();
        accionMejoraModel = gson.fromJson(data, AccionMejoraModel.class);

        DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

        try {
            tx_fecha.setText(formatoRender.format(formatoInicial.parse(accionMejoraModel.Fecha)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DataDocs.clear();
        DataImg.clear();
        tx_responsable.setText(accionMejoraModel.Responsable);
        tx_avance.setText(accionMejoraModel.PorcentajeAvance);
        tx_tarea.setText(accionMejoraModel.Descripcion);

        int count=accionMejoraModel.Files.Count;
        if(count!=0){
            if(data.contains("TP01") ||data.contains("TP02")){
                ll_galeria.setVisibility(View.VISIBLE);
            }else {
                ll_galeria.setVisibility(View.GONE);
            }

            if(data.contains("TP03") ){
                rel_otros.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    checkSelfPermission();
                    //executeDownload();

                } else {
                    Toast.makeText(this, "Download manager is not available", Toast.LENGTH_LONG).show();
                }

            }else {
                rel_otros.setVisibility(View.GONE);
            }

            for(int i=0;i<accionMejoraModel.Files.Data.size();i++){
                if(accionMejoraModel.Files.Data.get(i).TipoArchivo.equals("TP03")){
                    rel_otros.setVisibility(View.VISIBLE);
                    DataDocs.add(accionMejoraModel.Files.Data.get(i));
                }else{
                    DataImg.add(accionMejoraModel.Files.Data.get(i));
                }
            }

            GlobalVariables.listaGaleria=DataImg;

            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            gridView.setLayoutManager(layoutManager);
            galeriaAdapter = new GaleriaAdapter(this,DataImg );
            gridView.setAdapter(galeriaAdapter);

            LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(horizontalManager);
            documentoAdapter = new DocumentoAdapter(this, DataDocs,permiso);
            listView.setAdapter(documentoAdapter);
            listView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL));

        }else{
            //mensaje.setVisibility(View.VISIBLE);
            rel_otros.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            ll_galeria.setVisibility(View.GONE);
        }
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
