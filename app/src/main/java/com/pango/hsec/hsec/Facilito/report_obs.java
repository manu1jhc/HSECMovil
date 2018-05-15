package com.pango.hsec.hsec.Facilito;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.PlanAccionEdit;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.controller.WebServiceAPI;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.ImageEntry;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsFacilitoModel;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.obs_archivos;
import com.pango.hsec.hsec.util.Compressor;
import com.pango.hsec.hsec.util.Picker;
import com.pango.hsec.hsec.util.Picker.PickListener;
import com.pango.hsec.hsec.util.ProgressRequestBody;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.microedition.khronos.opengles.GL;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class report_obs extends AppCompatActivity implements IActivity,Picker.PickListener,ViewPager.OnPageChangeListener,ProgressRequestBody.UploadCallbacks {
    ArrayList<Maestro> gerenciadata;
    ArrayList<Maestro> superintdata;
    public ObsFacilitoModel Obs;
    ObsFacilitoModel ObsPost=new ObsFacilitoModel();
    String codObs;
    ProgressBar progressBar;
    ArrayList<Integer> Actives=new ArrayList();
    ArrayList<Maestro> ObsFacilito_estado;
    MyPageAdapter pageAdapter;
    String CodObservacion,CodTipo;
    ArrayAdapter adapterGerencia,adapterSuperInt;
    private EditText txvResult,txvUbicacion,dt_accion,txv_gerencia,txv_superintendencia,dt_respaux;
    private TextView aspa1,textViewtitle;
    private ImageButton mbtnmicrofono,mButtonGuardar,btn_user,btn_deleteusr;
    public static final int REQUEST_CODE = 1;
    private ImageButton btnFotos,mbtn_microaccion,mbtn_microubi;
    private RecyclerView gridView;
    private ArrayList<GaleriaModel> DataImg;
    private ArrayList<GaleriaModel> DataImgList;
    ArrayList<GaleriaModel> Data;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<ImageEntry> mSelectedImages;
    private static View mView;
    private Button mbtn_acto,mbtn_condicion,mbtn_enviar;
    private CardView cv_respaux,cv_estado;
    SwipeRefreshLayout swipeRefreshLayout;
    Spinner spinnerGerencia,spinnerSuperInt,spinner_estado;
    String flag="",superint,gerencia,tipo="A",estado;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    Gson gson;
    String url;
    ConstraintLayout contenedor;
    String SupertInt="";
    String SupertIntfinal="";
    GetGaleriaModel getGaleriaModel;
    String obsFacilitoModel;
    String Errores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_obs);
        Bundle datos = this.getIntent().getExtras();
        boolean[] pass = {false,false};
        Integer[] itemSel = {0,0,0};
        gson = new Gson();
        Obs = new ObsFacilitoModel();
        DataImg = new ArrayList<>();
        DataImgList=new ArrayList<>();
        gridView = (RecyclerView)  findViewById(R.id.grid);

        mbtn_acto=(Button) findViewById(R.id.btn_acto);
        mbtn_condicion=(Button) findViewById(R.id.btn_condicion);
        mbtnmicrofono=(ImageButton) findViewById(R.id.btnmicrofono);
        contenedor=(ConstraintLayout) findViewById(R.id.contenedor);
        mButtonGuardar=(ImageButton) findViewById(R.id.ButtonGuardar);
//        mbtn_enviar=(Button) findViewById(R.id.btn_enviar);
//        if (GlobalVariables.flagFacilito==false){
//            contenedor.setVisibility(View.VISIBLE);
//            mbtn_enviar.setText("ENVIAR");
//        }
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        txvResult=(EditText) findViewById(R.id.txvResult);
        txvUbicacion=(EditText) findViewById(R.id.txvUbicacion);
        dt_accion=(EditText) findViewById(R.id.dt_accion);
        txv_gerencia=(EditText) findViewById(R.id.txv_gerencia);
        txv_superintendencia=(EditText) findViewById(R.id.txv_superintendencia);
        aspa1=(TextView) findViewById(R.id.aspa1);
        textViewtitle=(TextView) findViewById(R.id.textViewtitle);
        dt_respaux=(EditText) findViewById(R.id.dt_respaux);
        cv_respaux=(CardView) findViewById(R.id.cv_respaux);
        cv_estado=(CardView) findViewById(R.id.cv_estado);
        btn_user=(ImageButton) findViewById(R.id.btn_user);
        btn_deleteusr=(ImageButton) findViewById(R.id.btn_deleteusr);

        btnFotos=(ImageButton) findViewById(R.id.btn_galeria);
        mbtn_microubi=(ImageButton) findViewById(R.id.btn_microubi);
        mbtn_microaccion=(ImageButton) findViewById(R.id.btn_microaccion);
        progressBar = findViewById(R.id.progressBar2);

        superintdata=new ArrayList<>();
        superintdata.add(new Maestro("","-  Seleccione  -"));
        spinnerGerencia=(Spinner) findViewById(R.id.sp_gerencia);
        spinnerSuperInt=(Spinner) findViewById(R.id.sp_superint);
        spinner_estado=(Spinner) findViewById(R.id.spinner_estado);

        adapterGerencia = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, GlobalVariables.Gerencia);
        adapterGerencia.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

        ArrayList<Maestro> dataSuper=new ArrayList<>();
        adapterSuperInt = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSuperInt.setAdapter(adapterSuperInt);
        ObsFacilito_estado= new ArrayList<>();
        ObsFacilito_estado.addAll(GlobalVariables.ObsFacilito_estado);
        ArrayAdapter adapterNivelR = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, ObsFacilito_estado);
        adapterNivelR.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterNivelR);
        spinner_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(itemSel[0]!=position) {
                    itemSel[0] = position;

                    Maestro ubica = (Maestro) ((Spinner) findViewById(R.id.spinner_estado)).getSelectedItem();
                    estado = ubica.CodTipo;
                    estado = ObsFacilito_estado.get(position).CodTipo;
                    Obs.Estado = estado;
                    adapterNivelR.notifyDataSetChanged();
                    if (pass[0]) {
                        pass[0] = false;
                    } else {
                        Obs.Estado = estado;
                        spinner_estado.setSelection(position);
                    }

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                estado="";
            }
        });
        spinnerGerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(itemSel[0]!=position) {
                    itemSel[0] = position;
                    superint = "";

                    Maestro ubica = (Maestro) ((Spinner) findViewById(R.id.sp_gerencia)).getSelectedItem();
                    gerencia = ubica.CodTipo;
                    txv_gerencia.setText(ubica.Descripcion.replace("=","").replace("->","").replace("-","").trim());
                    if(!txv_gerencia.equals(null)&&!gerencia.equals(null)){
                        aspa1.setVisibility(View.INVISIBLE);
                    }
                    txv_superintendencia.setText("");
                    Obs.CodPosicionGer = gerencia;
                    superintdata.clear();
                    for (Maestro item : GlobalVariables.loadSuperInt(gerencia)
                            ) {
                        superintdata.add(item);
                    }
                    adapterSuperInt.notifyDataSetChanged();
                    if (!pass[0]) {
                        pass[0] = true;
                        if (!StringUtils.isEmpty(Obs.CodPosicionSup))
                            spinnerSuperInt.setSelection(GlobalVariables.indexOf(superintdata, Obs.CodPosicionGer + "." + Obs.CodPosicionSup));
                    } else {
                        Obs.CodPosicionGer = gerencia;
                        spinnerSuperInt.setSelection(0);
                    }

                }
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
                    txv_superintendencia.setText(superintdata.get(position).Descripcion);
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
                                    });
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(report_obs.this, B_personas.class);
                startActivityForResult(intent , REQUEST_CODE);
            }
        });
        btn_deleteusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dt_respaux.setText("");
                Obs.RespAuxiliar="";
            }
        });
        //enviar reporte de observacion
        mButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Errores="";
                    Actives.clear();
                    Gson gson = new Gson();
                    Utils.closeSoftKeyBoard(report_obs.this);
                    mButtonGuardar.setClickable(false);
                    if(GlobalVariables.flagFacilito==false){
                        try {
                            Actives.add(0);
                            Obs.Tipo=tipo;
                            Obs.UbicacionExacta=String.valueOf(txvUbicacion.getText());
                            Obs.Observacion=String.valueOf(txvResult.getText());
                            Obs.Accion=String.valueOf(dt_accion.getText());
                            if(!ValifarFormulario(v)) return;
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
                            ObsPost.CodObsFacilito=codObs;
                            Obs.CodObsFacilito=codObs;
                            Obs.Tipo=tipo;
                            Obs.UbicacionExacta=String.valueOf(txvUbicacion.getText());
                            Obs.Observacion=String.valueOf(txvResult.getText());
                            Obs.Accion=String.valueOf(dt_accion.getText());
                            String Obsstr=gson.toJson(Obs);
                            if(!Obsstr.equals(obsFacilitoModel)){
                                Actives.add(0);
                                String url= GlobalVariables.Url_base+"ObsFacilito/Insertar";
                                final ActivityController obj = new ActivityController("post", url, report_obs.this,report_obs.this);
                                obj.execute(gson.toJson(Obs));
                            }
                            else {
                                Actives.add(1);
                                UpdateFiles(true);
                            }

                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }

                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        });
        if(GlobalVariables.flagFacilito==true) {
            textViewtitle.setText("Editar reporte Observacion");
        }
        if(GlobalVariables.flagFacilito==false) {
            textViewtitle.setText("Agregar reporte Observacion");
        }

        if(GlobalVariables.flagFacilito==true){
            textViewtitle.setText("Editar reporte Observacion");
            String editable;
            Bundle data0 = this.getIntent().getExtras();
            editable=data0.getString("editable");
            if(editable.equals("3")){
                cv_respaux.setVisibility(View.VISIBLE);
                cv_estado.setVisibility(View.VISIBLE);
            }
            try{
                Bundle data1 = this.getIntent().getExtras();
                codObs=data1.getString("codObs");
                Actives.add(0);

                url= GlobalVariables.Url_base+"ObsFacilito/GetObsFacilitoID/"+codObs;
                final ActivityController obj = new ActivityController("get", url, report_obs.this,this);
                obj.execute("2");
                Actives.add(0);

                String url1=GlobalVariables.Url_base+"media/GetMultimedia/"+codObs+"-1";
                final ActivityController obj1 = new ActivityController("get", url1, report_obs.this,this);
                obj1.execute("1");


            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            GlobalVariables.StrFiles.clear();
            setdata();}
    }
    public boolean ValifarFormulario(View view){
        String ErrorForm="Detalle:\n";
        if(ErrorForm.equals("Detalle:\n")) ErrorForm="";
        if(StringUtils.isEmpty(Obs.UbicacionExacta)) ErrorForm+=" ->Ubicacion exacta\n";
        if(StringUtils.isEmpty(Obs.Observacion)) ErrorForm+=" ->Observacion detalle\n";
        if(StringUtils.isEmpty(Obs.Accion)) ErrorForm+=" ->Accion a tomar\n";
        if(StringUtils.isEmpty(Obs.CodPosicionGer)) ErrorForm+=" ->Gerencia\n";
        if(ErrorForm.isEmpty()) return true;
        else{
            Snackbar.make(view, "Complete los siguientes campos obligatorios:\n"+ErrorForm, Snackbar.LENGTH_LONG).show();
            return false;
        }
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
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) { // seleccion de solicitado por
                String name=data.getStringExtra("nombreP");

                dt_respaux.setText(name);
                Obs.RespAuxiliar=data.getStringExtra("codpersona");
            }
        } catch (Exception ex) {
            Toast.makeText(report_obs.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
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

        try {
            for (ImageEntry image:images) {
                gridViewAdapter.add(new GaleriaModel(image.path,image.isVideo?"TP02":"TP01",new File(image.path).length()+"",new File(image.path).getName())); //image.path.split("/")[image.path.split("/").length-1]
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void setdata(){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, DataImg);
        gridView.setAdapter(gridViewAdapter);
        if(GlobalVariables.flagFacilito==true){
                if(!StringUtils.isEmpty(Obs.Estado))spinner_estado.setSelection(GlobalVariables.indexOf(GlobalVariables.ObsFacilito_estado,Obs.Estado));
                if(!StringUtils.isEmpty(Obs.CodPosicionGer))spinnerGerencia.setSelection(GlobalVariables.indexOf(GlobalVariables.Gerencia,Obs.CodPosicionGer));
                txvUbicacion.setText(Obs.UbicacionExacta);
                txvResult.setText(Obs.Observacion);
                dt_accion.setText(Obs.Accion);
                if(Obs.Tipo.equals("A")){
                    mbtn_acto.setBackgroundColor(Color.parseColor("#22b14c"));
                    mbtn_condicion.setBackgroundColor(Color.parseColor("#fce8e8"));
                    tipo="A";
                }
                else if(Obs.Tipo.equals("C")){
                    mbtn_acto.setBackgroundColor(Color.parseColor("#cdf2d8"));
                    mbtn_condicion.setBackgroundColor(Color.parseColor("#ff2222"));
                    tipo="C";
                }
            contenedor.setVisibility(View.VISIBLE);

        }

    }
    public void UpdateFiles(boolean apt){
        try {
            gridViewAdapter.ProcesarImagens();
            ArrayList<GaleriaModel> DataInsert = new ArrayList<>();
            ArrayList<GaleriaModel> DataAll = new ArrayList<>();
            DataAll.addAll(DataImg);
           // delete files
            String DeleteFiles = "";
            for (GaleriaModel item : GlobalVariables.StrFiles) {
                boolean pass = true;
                for (GaleriaModel item2 : DataAll) {
                    if (item.Correlativo == item2.Correlativo) {
                        pass = false;
                        continue;
                    }
                }
                if (pass) {
                    DeleteFiles += item.Correlativo + ";";
                    item.Estado = "E";
                }
            }
            for (GaleriaModel item : DataAll) {
                boolean pass = false;
                for (GaleriaModel item2 : GlobalVariables.StrFiles)
                    if (item.Descripcion.equals(item2.Descripcion))
                        pass = true;
                if (item.Correlativo == -1) {
                    DataInsert.add(item);
                    if (!pass) GlobalVariables.StrFiles.add(item);
                }
            }
            if (apt && DeleteFiles.equals("") && DataInsert.size() == 0) {
                    Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
            } else {
//Delete Files
                if (!DeleteFiles.equals("")) {
                    Actives.add(0);
                    String url = GlobalVariables.Url_base + "media/deleteAll/" + DeleteFiles.substring(0, DeleteFiles.length() - 1);
                    ActivityController obj = new ActivityController("get", url, report_obs.this, this);
                    obj.execute("3");
                }
                else Actives.add(1);
                if(DataInsert.size()>0){
                    Actives.add(0);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(GlobalVariables.Url_base)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    WebServiceAPI service = retrofit.create(WebServiceAPI.class);
                    List<MultipartBody.Part> Files = new ArrayList<>();
                    for (GaleriaModel item : DataInsert) {
                        Files.add(createPartFromFile(item));
                    }
                    Toast.makeText(report_obs.this, "Guardando Reporte Observacion, Espere...", Toast.LENGTH_SHORT).show();
                    Call<String> request = service.uploadAllFile("Bearer " + GlobalVariables.token_auth, createPartFromString(ObsPost.CodObsFacilito), createPartFromString("OBF"), createPartFromString("1"), Files);
                    progressBar.setVisibility(View.VISIBLE);
                    request.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.isSuccessful()) {
                                String respt = response.body();
                                if (respt.contains("-1")) {
                                Actives.set(2,-1);
                                    Errores += "\nOcurrio un error al subir algunas imagenes";
                                }
                        else  Actives.set(2,1);
                                Utils.DeleteCache(new Compressor(report_obs.this).destinationDirectoryPath); //delete cache Files;
                                for (String file : respt.split(";")) {
                                    String[] datosf = file.split(":");
                                    for (GaleriaModel item : GlobalVariables.StrFiles) {
                                        if (item.Descripcion.equals(datosf[0])) {
                                            item.Correlativo = Integer.parseInt(datosf[1]);
                                            if (item.Correlativo == -1) item.Estado = "E";
                                            else {
                                                if (item.TipoArchivo.equals("TP01"))
                                                    item.Url = "/Media/getImage/" + datosf[1] + "/Image.jpg";
                                                else if (item.TipoArchivo.equals("TP02"))
                                                    item.Url = "/Media/Play/" + datosf[1] + "/Video.mp4";
                                                else
                                                    item.Url = "/Media/Getfile/" + datosf[1] + "/" + datosf[0];
                                            }
                                        }
                                    }
                                }

                            } else {
                        Actives.set(2,-1);
                                Errores += "\nOcurrio un error interno de servidor";
                            }
                            if (!Actives.contains(0)) FinishSave();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                    Actives.set(2,-1);
                            Errores += "\nFallo la subida de archivos";
                            if (!Actives.contains(0)) FinishSave();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }

            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }
    @NonNull
    private RequestBody createPartFromString(String data){
        return RequestBody.create(MultipartBody.FORM,data);
    }
    @NonNull
    private MultipartBody.Part createPartFromFile(GaleriaModel item){
        ProgressRequestBody fileBody = new ProgressRequestBody(item,report_obs.this,this);
        return  MultipartBody.Part.createFormData("image", item.Descripcion, fileBody);
    }
    @Override
    public void onCancel() {
    }
    @Override
    public void success(String data, String Tipo) {

        if(GlobalVariables.flagFacilito==true){
            if(Tipo.equals("1")){
                Actives.set(0,1);
                Gson gson = new Gson();
                DataImg=gson.fromJson(data, GetGaleriaModel.class).Data;
                DataImgList=gson.fromJson(data, GetGaleriaModel.class).Data;
                GlobalVariables.StrFiles=DataImgList;

            }
            else if (Tipo.equals("2")){
                Actives.set(1,1);
                Gson gson = new Gson();
                Obs = gson.fromJson(data, ObsFacilitoModel.class);
//                mbtn_enviar.setText("ENVIAR MODIFICACIÓN");
                obsFacilitoModel = gson.toJson(Obs);

            }
            else if(Tipo.equals("3")){ //delete Files
                if(data.contains("false")){
                    Actives.set(1,-1);
                    Errores+="\nNo se pudo eliminar algunas imagenes";
                }
                else {
                    Actives.set(1,1);
                    ArrayList<GaleriaModel> temp= new ArrayList<>(GlobalVariables.StrFiles);

                    for (GaleriaModel item : GlobalVariables.StrFiles) {
                        if(!StringUtils.isEmpty(item.Estado)&&item.Estado.equals("E"))
                            temp.remove(item);
                    }
                    GlobalVariables.StrFiles=temp;
                }
                if(!Actives.contains(0)) FinishSave();
            }
            if(!Actives.contains(0)){
                setdata();
            }
        }
//        if(GlobalVariables.flagFacilito==false){
//            Gson gson = new Gson();
//            Obs = gson.fromJson(data, ObsFacilitoModel.class);
//            mbtn_enviar.setText("ENVIAR");
//        }
    }
    boolean ok,fail;
    public void FinishSave(){
        int icon=R.drawable.confirmicon;
        String Mensaje="Se guardaron los datos correctamente";
        ok=false;
        fail=false;
        if(Actives.contains(1))ok=true;
        if(Actives.contains(-1)){
            fail=true;
            icon=R.drawable.erroricon;
            Mensaje="Ocurrio algun error interno, No se pudo guardar los datos.";
        }
        if(fail&&ok){
            Mensaje="Se guardo con los siguientes errores:\n";
            Mensaje+=Errores;//.replace("@","\n");
            icon=R.drawable.warninicon;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Desea Finalizar?");
        alertDialog.setIcon(icon);
        alertDialog.setMessage(Mensaje);
        if(ok&&!fail)
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    btn_Salvar.setEnabled(true);
                    GlobalVariables.ObjectEditable=true;
                }
            });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(ok&&!fail){
                    finish();
                }
//                btn_Salvar.setEnabled(true);
            }
        });

        alertDialog.show();

        DataImg.clear();
        for (GaleriaModel item: GlobalVariables.StrFiles) {
            DataImg.add(item);
        }
        setdata();
       /* Intent intent = getIntent();
        intent.putExtra("AccionMejora",gson.toJson(AddAccionMejora));
        setResult(RESULT_OK, intent);
        finish();*/
    }
//    public String getDataIma(String codObs){
//        String url1=GlobalVariables.Url_base+"media/GetMultimedia/"+codObs;
//        final ActivityController obj1 = new ActivityController("get", url1, report_obs.this,this);
//       return obj1.execute("").toString();
//    }
    @Override
    public void successpost(String data, String Tipo) {
        Gson gson = new Gson();
        ObsPost.CodObsFacilito = data.substring(1, data.length() - 1);

        if(data.equals("-1")){
            Errores+="\nOcurrio un error al guardar cabezera";
            Actives.set(0,-1);
        }
        else{
            UpdateFiles(false);
            Actives.set(0,1);
        }
    }
    @Override
    public void error(String mensaje, String Tipo) {
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressBar.setProgress(percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        progressBar.setProgress(100);
    }
}
