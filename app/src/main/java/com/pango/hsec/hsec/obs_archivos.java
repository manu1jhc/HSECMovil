package com.pango.hsec.hsec;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.FontRequest;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Observaciones.ActVidDet;
import com.pango.hsec.hsec.Observaciones.Galeria_detalle;
import com.pango.hsec.hsec.R;

import android.app.ProgressDialog;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.adapter.Adap_Img;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.ImageEntry;
import com.pango.hsec.hsec.util.Picker;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.adapter.ListViewAdapter;


import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class obs_archivos extends Fragment implements IActivity,Picker.PickListener {

    private static View mView;
    /*MyGridView grid_gal;
    Adap_Img adaptador;
    private RecyclerView mImageSampleRecycler;*/

    private RecyclerView listView;
    private RecyclerView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;

    ProgressDialog progressDialog;
    int position=0;
    final String[] ACCEPT_MIME_TYPES = {
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/msword",
            "application/odt",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-excel"
    };

    public static final obs_archivos newInstance(String sampleText, int pos) {
        obs_archivos f = new obs_archivos();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        b.putInt("bPos", pos);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_archivos,  container, false);
        String codigo_obs = getArguments().getString("bString");
        if(position==0)position = getArguments().getInt("bPos");
        if (position==2){
            position=1;
            loadImage();
        }

        ImageButton btnFotos=(ImageButton) mView.findViewById(R.id.btn_addfotos);
        ImageButton btnFiles=(ImageButton) mView.findViewById(R.id.btn_addfiles);

        gridView = (RecyclerView)  mView.findViewById(R.id.grid);
        listView = (RecyclerView) mView.findViewById(R.id.list);



        if(GlobalVariables.ObjectEditable){ // load data of server

            if(GlobalVariables.ObserbacionFile==null)
            {
                GlobalVariables.ObserbacionFile=codigo_obs;
                String url=GlobalVariables.Url_base+"media/GetMultimedia/"+codigo_obs;
                final ActivityController obj = new ActivityController("get", url, obs_archivos.this,getActivity());
                obj.execute("");
            }
            else setdata();
        }
        else if(GlobalVariables.editar_list){////editar galeria no almacenada en el servidor

            setdata();


        }
        else // new Obserbacion
        {
            if(GlobalVariables.ObserbacionFile==null){
                GlobalVariables.ObserbacionFile=codigo_obs;
                GlobalVariables.listaGaleria= new ArrayList<>();
                GlobalVariables.listaArchivos= new ArrayList<>();
            }
            setdata();
        }

        //load data adapter

        btnFotos.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            loadImage();
                                        }
                                    }
        );
        btnFiles.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent();
                                            intent.setType("application/pdf");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                                            intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
                                            startActivityForResult(Intent.createChooser(intent, "Seleccione Documento"), 1);
                                        }
                                    }
        );
        return mView;
    }

    public void setdata(){
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(getActivity(), GlobalVariables.listaGaleria);
        gridView.setAdapter(gridViewAdapter);

        LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(horizontalManager);
        listViewAdapter = new ListViewAdapter(getActivity(), GlobalVariables.listaArchivos);
        listView.setAdapter(listViewAdapter);
    }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data)
   {
       if(resultCode==RESULT_CANCELED){ // action cancelled
            }
       if(resultCode==RESULT_OK)
       {
           Uri uri = data.getData();
           GaleriaModel temp= null;
           try {
               temp = Utils.getFilePath(this.getContext(),uri);
           } catch (URISyntaxException e) {
               e.printStackTrace();
           }
           String [] exts=temp.Descripcion.split("\\.");
           switch (exts[exts.length-1]){
               case "pdf":case "doc":case "docx":case "ppt":case "pptx":case "xls":case "xlsx":case "odt":
                   listViewAdapter.add(temp);
                   break;
               default: Toast.makeText(getActivity(), "Archivo no permitido", Toast.LENGTH_LONG).show();
           }
       }
   }


   /* private static boolean isDownloadManagerAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }*/

    public void loadImage(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
        else  if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {Manifest.permission.CAMERA}, 1022);
        else  if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {Manifest.permission.RECORD_AUDIO}, 1033);
        else
        {
            new Picker.Builder(getActivity(), obs_archivos.this, R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setVideosEnabled(true)
                    .setLimit(10)
                    .build()
                    .startActivity();
        }
    }
    @SuppressLint("ValidFragment")
    @Override

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1011: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(getActivity(), "Para poder agregar archivos multimedia, necesita otorgar los persmisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 1022: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(getActivity(), "Para poder utilizar la camara, necesita otorgar los permisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 1033: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(getActivity(), "Para poder grabar un video, necesita otorgar los permisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* final int id = item.getItemId();

        if (id == R.id.action_about) {
            showAbout();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPickedSuccessfully(ArrayList<ImageEntry> images) {

        for (ImageEntry image:images) {
            gridViewAdapter.add(new GaleriaModel(image.path,image.isVideo?"TP02":"TP01",new File(image.path).length()+"",new File(image.path).getName())); //image.path.split("/")[image.path.split("/").length-1]
        }
    }

    @Override
    public void onCancel() {
        //Log.i(TAG, "User canceled picker activity");
       // Toast.makeText(getActivity(), "User canceld picker activtiy", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void success(String data, String Tipo) {

        Gson gson = new Gson();
        GetGaleriaModel getGaleriaModel = gson.fromJson(data, GetGaleriaModel.class);
        int count = getGaleriaModel.Count;
        GlobalVariables.StrFiles=getGaleriaModel.Data;
        GlobalVariables.listaGaleria= new ArrayList<>();
        GlobalVariables.listaArchivos= new ArrayList<>();
        if (count != 0) {

            for (int i = 0; i < getGaleriaModel.Data.size(); i++) {
                if (getGaleriaModel.Data.get(i).TipoArchivo.equals("TP03")) {
                    GlobalVariables.listaArchivos.add(getGaleriaModel.Data.get(i));
                } else {
                    GlobalVariables.listaGaleria.add(getGaleriaModel.Data.get(i));
                }
            }
        }
        setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}