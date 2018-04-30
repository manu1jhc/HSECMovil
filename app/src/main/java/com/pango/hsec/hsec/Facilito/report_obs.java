package com.pango.hsec.hsec.Facilito;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.ImageEntry;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsFacilitoModel;
import com.pango.hsec.hsec.obs_archivos;
import com.pango.hsec.hsec.util.Picker;
import com.pango.hsec.hsec.util.Picker.PickListener;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class report_obs extends AppCompatActivity implements IActivity,Picker.PickListener  {
    ArrayList<Maestro> gerenciadata;
    ArrayList<Maestro> superintdata;
    public ObsFacilitoModel Obs;
    String codObs;
    ArrayAdapter adapterGerencia,adapterSuperInt;
    private EditText txvResult,txvUbicacion,dt_accion;
    private ImageButton mbtnmicrofono;
    private ImageButton btnFotos,mbtn_microaccion,mbtn_microubi;
    private RecyclerView gridView;
    private ArrayList<GaleriaModel> DataImg;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<ImageEntry> mSelectedImages;
    private static View mView;
    private Button mbtn_acto,mbtn_condicion,mbtn_enviar;
    SwipeRefreshLayout swipeRefreshLayout;
    Spinner spinnerGerencia,spinnerSuperInt;
    String flag="",superint,gerencia,tipo="A";
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    Gson gson;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_obs);
        Bundle datos = this.getIntent().getExtras();
        gson = new Gson();
        Obs = new ObsFacilitoModel();
        if(GlobalVariables.flagFacilito==true){
            try{
                Bundle data1 = this.getIntent().getExtras();
                codObs=data1.getString("codObs");
                url= GlobalVariables.Url_base+"ObsFacilito/GetObsFacilitoID/"+codObs;
                final ActivityController obj = new ActivityController("get", url, report_obs.this,this);
                obj.execute("");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        mbtn_acto=(Button) findViewById(R.id.btn_acto);
        mbtn_condicion=(Button) findViewById(R.id.btn_condicion);
        mbtnmicrofono=(ImageButton) findViewById(R.id.btnmicrofono);
        mbtn_enviar=(Button) findViewById(R.id.btn_enviar);
        if (GlobalVariables.flagFacilito==false){
            mbtn_enviar.setText("ENVIAR");
        }
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        txvResult=(EditText) findViewById(R.id.txvResult);
        txvUbicacion=(EditText) findViewById(R.id.txvUbicacion);
        dt_accion=(EditText) findViewById(R.id.dt_accion);
        btnFotos=(ImageButton) findViewById(R.id.btn_galeria);
        mbtn_microubi=(ImageButton) findViewById(R.id.btn_microubi);
        mbtn_microaccion=(ImageButton) findViewById(R.id.btn_microaccion);

        gridView = (RecyclerView)  findViewById(R.id.grid);
        DataImg = new ArrayList<>();
        gerenciadata= new ArrayList<>();
        gerenciadata.add(new Maestro("","-  Seleccione  -"));
        gerenciadata.addAll(GlobalVariables.Gerencia);

        superintdata=new ArrayList<>();
        superintdata.add(new Maestro("","-  Seleccione  -"));
        spinnerGerencia=(Spinner) findViewById(R.id.sp_gerencia);
        spinnerSuperInt=(Spinner) findViewById(R.id.sp_superint);
        adapterGerencia = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, gerenciadata);
        adapterGerencia.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

        adapterSuperInt = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSuperInt.setAdapter(adapterSuperInt);
        spinnerGerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /*Maestro Geren = (Maestro) ( (Spinner) view.findViewById(R.id.spinner_gerencia) ).getSelectedItem();
                gerencia=Geren.CodTipo;*///
                superint="";
                gerencia = gerenciadata.get(position).CodTipo;
                Obs.CodPosicionGer=gerencia;
                superintdata.clear();
                for (Maestro item: GlobalVariables.loadSuperInt(gerencia)
                        ) {
                    superintdata.add(item);
                }
                adapterSuperInt.notifyDataSetChanged();
                spinnerSuperInt.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gerencia="";
            }
        });
        spinnerSuperInt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    superint = superintdata.get(position).CodTipo.split("\\.")[1];
                    Obs.CodPosicionSup=superint;
                }else{
                    superint="";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                superint="";
            }
        });
        mbtnmicrofono.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag="observacion";
                askSpeechInput();
            }
        });
        mbtn_microubi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag="ubicacion";
                askSpeechInput();
            }
        });
        mbtn_microaccion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag="accion";
                askSpeechInput();
            }
        });
        mbtn_condicion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mbtn_acto.setBackgroundColor(Color.parseColor("#cdf2d8"));
                mbtn_condicion.setBackgroundColor(Color.parseColor("#ff2222"));
                tipo="C";
            }
        });
        mbtn_acto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mbtn_acto.setBackgroundColor(Color.parseColor("#22b14c"));
                mbtn_condicion.setBackgroundColor(Color.parseColor("#fce8e8"));
                tipo="A";
            }
        });
        btnFotos.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            loadImage();
                                        }
                                    }
        );
        //enviar reporte de observacion
        mbtn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalVariables.flagFacilito==false){
                    try {
//                    Obs.CodPosicionGer=((Maestro) spinnerGerencia.getSelectedItem()).CodTipo;
//                    Obs.CodPosicionSup=((Maestro) spinnerSuperInt.getSelectedItem()).CodTipo;
                        Obs.Tipo=tipo;
                        Obs.UbicacionExacta=String.valueOf(txvUbicacion.getText());
                        Obs.Observacion=String.valueOf(txvResult.getText());
                        Obs.Accion=String.valueOf(dt_accion.getText());
                        String url= GlobalVariables.Url_base+"ObsFacilito/Insertar";
                        final ActivityController obj = new ActivityController("post", url, report_obs.this,report_obs.this);
                        obj.execute(gson.toJson(Obs));
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(GlobalVariables.flagFacilito==true){
                    try {
//                    Obs.CodPosicionGer=((Maestro) spinnerGerencia.getSelectedItem()).CodTipo;
//                    Obs.CodPosicionSup=((Maestro) spinnerSuperInt.getSelectedItem()).CodTipo;
                        Obs.CodObsFacilito=codObs;
                        Obs.Tipo=tipo;
                        Obs.UbicacionExacta=String.valueOf(txvUbicacion.getText());
                        Obs.Observacion=String.valueOf(txvResult.getText());
                        Obs.Accion=String.valueOf(dt_accion.getText());
                        String url= GlobalVariables.Url_base+"ObsFacilito/Insertar";
                        final ActivityController obj = new ActivityController("post", url, report_obs.this,report_obs.this);
                        obj.execute(gson.toJson(Obs));
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

            }
        });
    }
    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable algo");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
        }
    }
    public void close(View view){
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(flag=="observacion"){
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        txvResult.setText(result.get(0));
                    }
                    break;
                }
            }
        }
        if(flag=="ubicacion"){
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        txvUbicacion.setText(result.get(0));
                    }
                    break;
                }
            }
        }
        if(flag=="accion"){
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        dt_accion.setText(result.get(0));
                    }
                    break;
                }

            }
        }
    }
    @SuppressLint("NewApi")
    public void loadImage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
        else  if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {Manifest.permission.CAMERA}, 1022);
        else  if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {android.Manifest.permission.RECORD_AUDIO}, 1033);
        else
        {
            new Picker.Builder(this, report_obs.this, R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setVideosEnabled(true)
                    .setLimit(10)
                    .build()
                    .startActivity();
        }
    }
    @Override
    public void onPickedSuccessfully(ArrayList<ImageEntry> images) {

        for (ImageEntry image:images) {
            DataImg.add(new GaleriaModel(image.path,image.isVideo?"TP02":"TP01",image.dateAdded+"",image.imageId+""));
        }
        GlobalVariables.listaGaleria=DataImg;

        mSelectedImages = images;
        //  gridView.setHasFixedSize(true);

        //set layout manager and adapter for "GridView"
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, DataImg);
        gridView.setAdapter(gridViewAdapter);
    }
    @Override
    public void onCancel() {
    }
    @Override
    public void success(String data, String Tipo) {

        if(GlobalVariables.flagFacilito==true){
            Gson gson = new Gson();
            Obs = gson.fromJson(data, ObsFacilitoModel.class);
            mbtn_enviar.setText("ENVIAR MODIFICACIÓN");
            ObsFacilitoModel obsFacilitoModel = gson.fromJson(data, ObsFacilitoModel.class);
            if(!StringUtils.isEmpty(obsFacilitoModel.CodPosicionGer))spinnerGerencia.setSelection(GlobalVariables.indexOf(GlobalVariables.Gerencia,obsFacilitoModel.CodPosicionGer));
//            if(!StringUtils.isEmpty(obsFacilitoModel.CodPosicionGer+"."+obsFacilitoModel.CodPosicionSup))spinnerSuperInt.setSelection(GlobalVariables.indexOf(GlobalVariables.SuperIntendencia,obsFacilitoModel.CodPosicionGer+"."+obsFacilitoModel.CodPosicionSup));
//            String gerencia=GlobalVariables.getDescripcion(GlobalVariables.Gerencia,obsFacilitoModel.CodPosicionGer).trim().replace("=","");
//            String superint=GlobalVariables.getDescripcion(GlobalVariables.SuperIntendencia,obsFacilitoModel.CodPosicionGer +"."+obsFacilitoModel.CodPosicionSup).trim().replace("=","");
//            spinnerGerencia.setPrompt(gerencia);
//            spinnerSuperInt.setPrompt(superint);
            txvUbicacion.setText(obsFacilitoModel.UbicacionExacta);
            txvResult.setText(obsFacilitoModel.Observacion);
            dt_accion.setText(obsFacilitoModel.Accion);
            if(obsFacilitoModel.Tipo.equals("A")){
                mbtn_acto.setBackgroundColor(Color.parseColor("#22b14c"));
                mbtn_condicion.setBackgroundColor(Color.parseColor("#fce8e8"));
                tipo="A";
            }
            else if(obsFacilitoModel.Tipo.equals("C")){
                mbtn_acto.setBackgroundColor(Color.parseColor("#cdf2d8"));
                mbtn_condicion.setBackgroundColor(Color.parseColor("#ff2222"));
                tipo="C";
            }
        }
        if(GlobalVariables.flagFacilito==false){
            Gson gson = new Gson();
            Obs = gson.fromJson(data, ObsFacilitoModel.class);
            mbtn_enviar.setText("ENVIAR");
        }
    }
    public void FinishSave(){
        Intent intent = getIntent();
        intent.putExtra("observacion",gson.toJson(Obs));
        setResult(RESULT_OK, intent);
        finish();
    }
    @Override
    public void successpost(String data, String Tipo) {
        if(data.equals("-1"))
            Toast.makeText(this,"Ocurrio un error interno al intentar guardar",Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(this,"Se envió la observación",Toast.LENGTH_SHORT).show();
            FinishSave();
        }
    }
    @Override
    public void error(String mensaje, String Tipo) {
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
}
