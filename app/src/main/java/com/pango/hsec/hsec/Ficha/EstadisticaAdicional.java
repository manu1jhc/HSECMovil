package com.pango.hsec.hsec.Ficha;

import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.Adap_Img;
import com.pango.hsec.hsec.adapter.AdicionalAdapter;
import com.pango.hsec.hsec.adapter.DocumentoAdapter;
import com.pango.hsec.hsec.adapter.GaleriaAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;

import java.util.ArrayList;

public class EstadisticaAdicional extends AppCompatActivity implements IActivity {
    TextView tx_ref,tx_autor,tx_fecha,tx_descripcion,tx_titulo;
    AdicionalAdapter adicionalAdapter;
    int position;
    String descripcion="";

    ////galeria

    ArrayList<GaleriaModel> DataDocs=new ArrayList<GaleriaModel>();
    ArrayList<GaleriaModel> DataImg=new ArrayList<GaleriaModel>();
    GetGaleriaModel getImg;
    private static View mView;
    String codObs="";
    String jsonGaleria="";
    Adap_Img adaptador;
    DownloadManager downloadManager;
    private static final short REQUEST_CODE = 6545;

    boolean permiso=false;
    DocumentoAdapter documentoAdapter;
    GaleriaAdapter galeriaAdapter;
    //////
    TextView txGaleria;
    //GridView grid_gal;
    ConstraintLayout cl_otros;
    FrameLayout frame_otros;
    //ListView list_docs;
    RelativeLayout rel_otros;
    LinearLayout galeria_foto;
    RecyclerView gridView,listView;
    String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadistica_adicional);
        Bundle datos = getIntent().getExtras();
        position=datos.getInt("position");
        descripcion=datos.getString("descripcion");

        tx_titulo=findViewById(R.id.tx_titulo);
        tx_ref=findViewById(R.id.tx_ref);
        tx_autor=findViewById(R.id.tx_autor);
        tx_fecha=findViewById(R.id.tx_fecha);
        tx_descripcion=findViewById(R.id.tx_descripcion);

        //galeria//////////////////////////////////////////////////////////
        gridView = (RecyclerView) findViewById(R.id.rec_galeria);
        listView = (RecyclerView) findViewById(R.id.list_docs);
        txGaleria=(TextView) findViewById(R.id.tx_gal);
        //grid_gal=(GridView) mView.findViewById(R.id.grid_gal);
        cl_otros=(ConstraintLayout) findViewById(R.id.cl_otros);
        frame_otros=(FrameLayout) findViewById(R.id.frame_otros);
        //list_docs=(ListView) mView.findViewById(R.id.list_docs);
        rel_otros=(RelativeLayout) findViewById(R.id.rel_otros);
        galeria_foto=(LinearLayout) findViewById(R.id.galeria_foto);

        ////////////////////////////////////////////////////////////////////////////////

//getEstadisticaDetModel
        final RecyclerView rec_adicional = (RecyclerView) findViewById(R.id.rec_adicional);
        rec_adicional.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rec_adicional.setLayoutManager(llm);
        rec_adicional.setFocusableInTouchMode(false);

        tx_ref.setText(GlobalVariables.dataAdicional.get(position).NroDocReferencia);
        tx_descripcion.setText(GlobalVariables.dataAdicional.get(position).Descripcion);

        if (GlobalVariables.dataAdicional.get(position).Responsable==null){
            tx_autor.setText(GlobalVariables.userLoaded.Nombres);
        }else {
            tx_autor.setText(GlobalVariables.dataAdicional.get(position).Responsable);
        }

        tx_fecha.setText(Utils.Obtenerfecha(GlobalVariables.dataAdicional.get(position).Fecha));
        tx_titulo.setText(descripcion);

        String[] DatosAd = GlobalVariables.dataAdicional.get(position).DatosAdicionales.split(";");

        adicionalAdapter = new AdicionalAdapter(DatosAd);
        rec_adicional.setAdapter(adicionalAdapter);

        //galeria

        url= GlobalVariables.Url_base+"media/GetMultimedia/"+GlobalVariables.dataAdicional.get(position).NroDocReferencia;

        final ActivityController obj = new ActivityController("get", url, EstadisticaAdicional.this,this);
        obj.execute("");


        /////////////////////////////////////////////////////////////////////
        txGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gridView.getVisibility()==View.GONE){
                    gridView.setVisibility(View.VISIBLE);
                }else{
                    gridView.setVisibility(View.GONE);
                }
                //grid_gal.setVisibility(View.GONE);
            }
        });

        cl_otros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listView.getVisibility()==View.GONE){
                    listView.setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.GONE);
                }
            }
        });




    }


    public void close(View view){
        finish();
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




    }




    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {
        DataDocs.clear();
        DataImg.clear();
        //int resultado=data.indexOf("TP03");
        Gson gson = new Gson();
        GetGaleriaModel getGaleriaModel = gson.fromJson(data, GetGaleriaModel.class);
        int count=getGaleriaModel.Count;
        //GlobalVariables.listaGaleria=getGaleriaModel.Data;
        if(count!=0){
            if(data.contains("TP01") ||data.contains("TP02")){
                galeria_foto.setVisibility(View.VISIBLE);
            }else {
                galeria_foto.setVisibility(View.GONE);
            }

            if(data.contains("TP03")){
                rel_otros.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    checkSelfPermission();
                    //executeDownload();

                } else {
                    Toast.makeText(this, "Download manager is not available", Toast.LENGTH_LONG).show();
                }

            }else{
                rel_otros.setVisibility(View.GONE);
            }

            for(int i=0;i<getGaleriaModel.Data.size();i++){
                if(getGaleriaModel.Data.get(i).TipoArchivo.equals("TP03")){
                    rel_otros.setVisibility(View.VISIBLE);
                    DataDocs.add(getGaleriaModel.Data.get(i));
                }else{
                    DataImg.add(getGaleriaModel.Data.get(i));
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



        }else{
            rel_otros.setVisibility(View.GONE);
            galeria_foto.setVisibility(View.GONE);
        }
    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
