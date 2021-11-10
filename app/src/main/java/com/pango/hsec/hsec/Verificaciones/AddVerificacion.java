package com.pango.hsec.hsec.Verificaciones;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Ingresos.Inspecciones.ActObsInspEdit;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.controller.WebServiceAPI;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.PlanModel;
import com.pango.hsec.hsec.model.VerificacionModel;
import com.pango.hsec.hsec.obs_archivos;
import com.pango.hsec.hsec.obs_planaccion;
import com.pango.hsec.hsec.util.Compressor;
import com.pango.hsec.hsec.util.ProgressRequestBody;
import com.pango.hsec.hsec.utilitario.UnsafeOkHttpClient;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddVerificacion extends FragmentActivity implements IActivity, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ProgressRequestBody.UploadCallbacks {
    MyPageAdapter pageAdapter;

    ImageButton close,btncancelar;
    TextView tx_titulo,txt_percent;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    String codObs="";
    int pos=0;
    ProgressBar progressBar;
    ArrayList<Integer> Actives=new ArrayList();
    String Errores="";
    ConstraintLayout ll_bar_carga;
    Call<String> request;
    Boolean cancel, enableSave=true;
    long L,G,T;
    HorizontalScrollView horizontalscroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_verificacion);
        close=findViewById(R.id.imageButton);
        horizontalscroll=findViewById(R.id.horizontalscroll);

        Bundle datos = this.getIntent().getExtras();
        codObs=datos.getString("codObs");
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        progressBar = findViewById(R.id.progressBar2);
        btncancelar= (ImageButton)findViewById(R.id.cancel_upload);
        txt_percent= (TextView)findViewById(R.id.txt_percent);
        ll_bar_carga=findViewById(R.id.ll_bar_carga);
        ll_bar_carga.setVisibility(View.GONE);

        tx_titulo=findViewById(R.id.tx_titulo);
        if(GlobalVariables.ObjectEditable){
            tx_titulo.setText("Editar Verificación");
        }else{
            tx_titulo.setText("Nueva Verificación");
        }

        initialiseTabHost();

        reiniciadata();
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(AddVerificacion.this);

    }


    public void reiniciadata(){
        GlobalVariables.Verificacion= new VerificacionModel();
        GlobalVariables.listaArchivos=new ArrayList<>();
        GlobalVariables.listaGaleria=new ArrayList<>();
        GlobalVariables.ObserbacionFile=null;
        GlobalVariables.ObserbacionPlan=null;
        GlobalVariables.Planes=new ArrayList<>();
        //save data Inicial
        GlobalVariables.StrVerificacion=null;
        GlobalVariables.StrPlanes=new ArrayList<>();
        GlobalVariables.StrFiles=new ArrayList<>();
        //inicialice options
        ActObsInspEdit.editar=true;

    }

    public void close(View view){
        outObservacion();
    }
    @Override
    public void onBackPressed() {
        outObservacion();
        //super.onBackPressed();
    }
    // Method to add a TabHost
    private static void AddTab(AddVerificacion activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String tabId) {
        pos = this.mTabHost.getCurrentTab();
        if(pos==1){
            DetalleVer detalle = (DetalleVer) pageAdapter.getItem(1);
            detalle.setdata();
        }
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);

        View tabView = mTabHost.getTabWidget().getChildAt(position);
        if (tabView != null)
        {
            final int width = horizontalscroll.getWidth();
            final int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
            horizontalscroll.scrollTo(scrollPos, 0);
        } else {
            horizontalscroll.scrollBy(positionOffsetPixels, 0);
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<>();


        // TODO Put here your Fragments
        FragmentVerificacion f1 = FragmentVerificacion.newInstance(codObs);
        DetalleVer f2 = new DetalleVer();
        obs_archivos f3 = obs_archivos.newInstance(codObs);
        obs_planaccion f4=obs_planaccion.newInstance(codObs);
//
        fList.add(f1);
        fList.add(f2);
        fList.add(f3);
        fList.add(f4);

        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        AddVerificacion.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("Verificación"));
        AddVerificacion.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("Detalle"));
        AddVerificacion.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab3").setIndicator("Archivos"));
        AddVerificacion.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab4").setIndicator("Plan Accion"));

//        AddVerificacion.AddTab(this, this.mTabHost,
//                this.mTabHost.newTabSpec("Tab3").setIndicator("Observaciones"));
        mTabHost.setOnTabChangedListener(this);

    }

// method to edit and save Verificacio

    public void outObservacion(){
        boolean Nochangues=true;
        Gson gson = new Gson();
        Utils.closeSoftKeyBoard(this);
        String Observacion=  gson.toJson(GlobalVariables.Verificacion);

        if(!GlobalVariables.StrVerificacion.equals(Observacion))Nochangues=false;
        if(Nochangues&&!GlobalVariables.ObjectEditable&&GlobalVariables.Planes.size()>0) Nochangues=false;
        else if(Nochangues&&GlobalVariables.StrPlanes.size()>0){
            String DeletePlanes="";
            for (PlanModel item:GlobalVariables.StrPlanes) {
                boolean pass=true;
                for (PlanModel item2:GlobalVariables.Planes) {
                    if(item.CodAccion.equals(item2.CodAccion)){
                        pass=false;
                        continue;
                    }
                }
                if(pass){
                    DeletePlanes+=item.CodAccion+";";
                }
            }
            if(!DeletePlanes.equals(""))Nochangues=false;
        }
        if(Nochangues)
        {
            ArrayList<GaleriaModel> DataInsert=new ArrayList<>();
            ArrayList<GaleriaModel> DataAll=new ArrayList<>();

            DataAll.addAll(GlobalVariables.listaGaleria);
            DataAll.addAll(GlobalVariables.listaArchivos);

            //delete files
            String DeleteFiles="";
            for (GaleriaModel item:GlobalVariables.StrFiles) {
                boolean pass=true;
                for (GaleriaModel item2:DataAll) {
                    if(item.Correlativo==item2.Correlativo){
                        pass=false;
                        continue;
                    }
                }
                if(pass){
                    DeleteFiles+=item.Correlativo+";";
                    item.Estado="E";
                }
            }
//Insert Files
            for (GaleriaModel item:DataAll) {
                boolean pass=false;
                for(GaleriaModel item2:GlobalVariables.StrFiles)
                    if(item.Descripcion.equals(item2.Descripcion))
                        pass=true;
                if(item.Correlativo==-1) {
                    DataInsert.add(item);
                    if(!pass)GlobalVariables.StrFiles.add(item);
                }
            }
            if(!DeleteFiles.equals("")||DataInsert.size()>0)Nochangues=false;
        }
        if(!Nochangues)
        {
            String Mensaje="Esta seguro de salir sin guardar cambios?\n";
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            //alertDialog.setCancelable(false);
            alertDialog.setTitle("Datos sin guardar");
            alertDialog.setIcon(R.drawable.warninicon);
            alertDialog.setMessage(Mensaje);

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.show();
        }
        else finish();
    }
    public boolean ValifarFormulario(View view){

        String ErrorForm="";
        if(StringUtils.isEmpty(GlobalVariables.Verificacion.CodVerificacionPor)) {ErrorForm+="Observado Por";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.Verificacion.CodAreaHSEC)) {ErrorForm+="Area HSEC";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.Verificacion.CodNivelRiesgo)) {ErrorForm+="Nivel de riesgo";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.Verificacion.Fecha)) {ErrorForm+="Fecha";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.Verificacion.CodUbicacion)) {ErrorForm+="Ubicación";pos=0;}

        else if(StringUtils.isEmpty(GlobalVariables.Verificacion.Observacion.trim())) {ErrorForm+="Observacion";pos=1;}
        else if(StringUtils.isEmpty(GlobalVariables.Verificacion.Accion.trim())) {ErrorForm+="Accion";pos=1;}
        else if(StringUtils.isEmpty(GlobalVariables.Verificacion.StopWork)) {ErrorForm+="Stop Work";pos=1;}

        if(ErrorForm.isEmpty()) return true;
        else{

            Snackbar.make(view, "El campo "+ErrorForm+" no puede estar vacío", Snackbar.LENGTH_LONG).setActionTextColor(Color.CYAN).setAction("Ver pestaña", new View.OnClickListener() {
                //public TabHost mTabHost;

                @Override
                public void onClick(View v) {
                    //initialiseTabHost();
                    //pos=1;
                    mTabHost.setCurrentTab(pos);
                    //onPageScrolled(1, 0, 0);
                }
            }).show();
            return false;
        }
    }

    public void SaveVerificacion(View view){
        if(!enableSave)return;
        Gson gson = new Gson();
        Utils.closeSoftKeyBoard(this);
        if(!ValifarFormulario(view)) return;
        enableSave=(false);
        cancel=false;
        btncancelar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        txt_percent.setText("");
        String Verificacion=  gson.toJson(GlobalVariables.Verificacion);

        Errores="";
        Actives.clear();
        if(GlobalVariables.ObjectEditable){

            String Cabecera, PlanesDelete, FilesDelete;
            Cabecera=PlanesDelete=FilesDelete="-";

            if(!GlobalVariables.StrVerificacion.equals(Verificacion)) Cabecera=Verificacion;

            //delete planes
            if(GlobalVariables.StrPlanes.size()>0){
                String DeletePlanes="";
                for (PlanModel item:GlobalVariables.StrPlanes) {
                    boolean pass=true;
                    for (PlanModel item2:GlobalVariables.Planes) {
                        if(item.CodAccion.equals(item2.CodAccion)){
                            pass=false;
                            continue;
                        }
                    }
                    if(pass){
                        DeletePlanes+=item.CodAccion+";";
                    }
                }
                if(!DeletePlanes.equals("")) PlanesDelete= DeletePlanes.substring(0,DeletePlanes.length()-1);
            }

            obs_archivos archivos = (obs_archivos) pageAdapter.getItem(2);
            if (archivos.gridViewAdapter !=null)archivos.gridViewAdapter.ProcesarImagens();

            ArrayList<GaleriaModel> DataAll=new ArrayList<>();
            ArrayList<GaleriaModel> DataInsert=new ArrayList<>();
            DataAll.addAll(GlobalVariables.listaGaleria);
            DataAll.addAll(GlobalVariables.listaArchivos);

            //delete files
            if(GlobalVariables.StrFiles.size()>0){
                String DeleteFiles="";
                for (GaleriaModel item:GlobalVariables.StrFiles) {
                    boolean pass=true;
                    for (GaleriaModel item2:DataAll) {
                        if(item.Correlativo==item2.Correlativo){
                            pass=false;
                            continue;
                        }
                    }
                    if(pass && item.Correlativo>0){
                        DeleteFiles+=item.Correlativo+";";
                        item.Estado="E";
                    }
                }
                if(!DeleteFiles.equals("")) FilesDelete= DeleteFiles.substring(0,DeleteFiles.length()-1);
            }
            //insert files
            for (GaleriaModel item:DataAll) {
                boolean pass=false;
                for(GaleriaModel item2:GlobalVariables.StrFiles)
                    if(item.Descripcion.equals(item2.Descripcion))
                        pass=true;
                if(item.Correlativo==-1) {
                    DataInsert.add(item);
                    if(!pass)GlobalVariables.StrFiles.add(item);
                }
            }

            if(DataInsert.size()==0 && FilesDelete.equals("-")&& PlanesDelete.equals("-")&& Cabecera.equals("-"))
            {
                enableSave=(true);
                Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
            }
            else{
                Actives.add(0);
                ll_bar_carga.setVisibility(View.VISIBLE);
                /*final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();*/
                OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GlobalVariables.Url_base)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WebServiceAPI service = retrofit.create(WebServiceAPI.class);
                G=T=L=0;
                List<MultipartBody.Part> Files = new ArrayList<>();
                for (GaleriaModel item:DataInsert) {
                    T+=Long.parseLong(item.Tamanio);
                    Files.add(createPartFromFile(item));
                }
                request = service.actualizarVerificacion("Bearer "+GlobalVariables.token_auth,createPartFromString(Cabecera),createPartFromString(PlanesDelete),createPartFromString(FilesDelete),createPartFromString(codObs),Files);
                if(T==0)onProgressUpdate();
                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        onFinish();
                        if(response.isSuccessful()){
                            Actives.set(0,1);
                            String respt[]  = response.body().split(";"); //"-;-;-;-;Carbones_de_Colombia9513.gif:57758"
                            for(int i =0;i<4;i++){
                                String rpt=respt[i];
                                if(!rpt.equals("-")){
                                    if(rpt.contains("-1")){
                                        Actives.add(-1);
                                        switch (i){
                                            case 0:
                                                Errores+="\nOcurrio un error al guardar cabezera";
                                                break;
                                            case 1:
                                                Errores+="\nOcurrio un error al guardar detalle";
                                                break;
                                            case 2:
                                                Errores+="\nOcurrio un error al eliminar planes de acción";
                                                break;
                                            case 3:
                                                Errores+="\nOcurrio un error al eliminar imagenes/archivos";
                                                break;
                                        }
                                    }
                                    switch (i){
                                        case 0:
                                            GlobalVariables.StrVerificacion=gson.toJson(GlobalVariables.Verificacion);
                                            break;
                                        case 1:
                                            GlobalVariables.StrPlanes=GlobalVariables.Planes;
                                            break;
                                        case 2:
                                            ArrayList<GaleriaModel> temp= new ArrayList<>(GlobalVariables.StrFiles);

                                            for (GaleriaModel item : GlobalVariables.StrFiles) {
                                                if(!StringUtils.isEmpty(item.Estado)&&item.Estado.equals("E"))
                                                    temp.remove(item);
                                            }
                                            GlobalVariables.StrFiles=temp;
                                            break;
                                    }
                                }
                            }

                            if(!respt[3].equals("-")){
                                Utils.DeleteCache(new Compressor(AddVerificacion.this).destinationDirectoryPath); //delete cache Files;
                                for (String file:respt[3].split(",")) {
                                    String[] datosf= file.split(":");
                                    for (GaleriaModel item:GlobalVariables.StrFiles) {
                                        if(item.Descripcion.equals(datosf[0]))
                                        {
                                            item.Correlativo=Integer.parseInt(datosf[1]);
                                            if(item.Correlativo==-1) item.Estado="E";
                                            else {
                                                item.Estado="A";
                                                if(item.TipoArchivo.equals("TP01")) item.Url= "/Media/getImage/"+datosf[1]+"/Image.jpg";
                                                else if(item.TipoArchivo.equals("TP02")) item.Url= "/Media/Play/"+datosf[1]+"/Video.mp4";
                                                else item.Url= "/Media/Getfile/"+datosf[1]+"/"+datosf[0];
                                            }
                                        }
                                    }
                                }
                            }
                        }else{
                            if(response.code()==401){
                                Utils.reloadTokenAuth(AddVerificacion.this,AddVerificacion.this);
                                progressBar.setProgress(0);
                                txt_percent.setText(0+"%");
                            }
                            else{
                                Actives.set(0,-1);
                                Errores+="\nOcurrio un error interno de servidor";
                            }
                        }
                        if(!Actives.contains(0)) FinishSave();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if(!cancel){
                            Actives.set(0,-1);
                            if(t.getMessage().equals("timeout"))Errores+="\nConexión a servidor perdida, intente de nuevo";
                            else {
                                Errores+="\nOcurrio un error al intentar guardar los datos.";

                            }
                            if(!Actives.contains(0)) FinishSave();
                        }
                        else{
                            for(GaleriaModel item:GlobalVariables.StrFiles)
                                if(item.Correlativo==-1)item.Estado="E";
                            loaddata();
                        }
                    }
                });
            }
        }
        else{  //Insert new Observacion
            Actives.add(0);
            ll_bar_carga.setVisibility(View.VISIBLE);
            // GridViewAdapter gridViewAdapter = new GridViewAdapter(this,GlobalVariables.listaGaleria);
            obs_archivos archivos = (obs_archivos) pageAdapter.getItem(2);
            archivos.gridViewAdapter.ProcesarImagens();
            GlobalVariables.StrFiles= new ArrayList();

            GlobalVariables.StrFiles.addAll(GlobalVariables.listaGaleria);
            GlobalVariables.StrFiles.addAll(GlobalVariables.listaArchivos);
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalVariables.Url_base)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            WebServiceAPI service = retrofit.create(WebServiceAPI.class);
            List<MultipartBody.Part> Files = new ArrayList<>();
            G=T=L=0;
            for (GaleriaModel item:GlobalVariables.StrFiles) {
                Files.add(createPartFromFile(item));
                T+=Long.parseLong(item.Tamanio);
            }
            //Toast.makeText(this, "Guardando Observacion, Espere..." , Toast.LENGTH_SHORT).show();

            request = service.insertarVerificacion("Bearer "+GlobalVariables.token_auth,createPartFromString(Verificacion),createPartFromString(gson.toJson(GlobalVariables.Planes)),Files);
            if(T==0)onProgressUpdate();
            request.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    onFinish();
                    if(response.isSuccessful()){
                        String respt  = response.body();
                        if(respt.equals("-1")){
                            Actives.set(0,-1);
                            Errores+="\nOcurrio un error al guardar observación.";
                        }
                        else {
                            Actives.set(0,1);  // update value Cabecera
                            Utils.DeleteCache(new Compressor(AddVerificacion.this).destinationDirectoryPath); //delete cache Files;
                            String [] respts= respt.split(";");
                            GlobalVariables.Verificacion.CodVerificacion=respts[0];
                            GlobalVariables.StrVerificacion=gson.toJson(GlobalVariables.Verificacion);
                            GlobalVariables.ObserbacionFile= respts[0];
                            GlobalVariables.ObserbacionPlan= respts[0];

                            FragmentVerificacion cabecera = (FragmentVerificacion) pageAdapter.getItem(0);
                            cabecera.updateCodigo(respts[0]);

                            // update Planes de accion
                            boolean passPlan=false;
                            if(!respts[1].equals("0"))
                            {
                                for(PlanModel item:GlobalVariables.Planes){
                                    for (String planid:respts[1].split(",")) {
                                        String[] value= planid.split(":");
                                        if(item.CodAccion.equals(value[0])){
                                            if(value[1].equals("-1")){
                                                item.Estado="E";
                                                passPlan=true;
                                            }
                                            else {
                                                item.CodAccion=value[1];
                                                item.NroDocReferencia=respts[0];
                                            }
                                            continue;
                                        }
                                    }
                                }
                                obs_planaccion fragment = (obs_planaccion) pageAdapter.getItem(3);
                                fragment.setdata();
                                GlobalVariables.StrPlanes=GlobalVariables.Planes;

                                if(passPlan) {
                                    Actives.add(-1);
                                    Errores+="Error al guardar algunos planes de accion";
                                }
                            }

                            //update file
                            if(!respts[2].equals("0"))
                            {
                                for (String file:respts[2].split(",")) {
                                    String[] datosf= file.split(":");
                                    for (GaleriaModel item:GlobalVariables.StrFiles) {
                                        if(item.Descripcion.equals(datosf[0]))
                                        {
                                            item.Correlativo=Integer.parseInt(datosf[1]);
                                            if(item.Correlativo==-1) item.Estado="E";
                                            else {
                                                if(item.TipoArchivo.equals("TP01")) item.Url= "/Media/getImage/"+datosf[1]+"/Image.jpg";
                                                else if(item.TipoArchivo.equals("TP02")) item.Url= "/Media/Play/"+datosf[1]+"/Video.mp4";
                                                else item.Url= "/Media/Getfile/"+datosf[1]+"/"+datosf[0];
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        if(response.code()==401){
                            Utils.reloadTokenAuth(AddVerificacion.this,AddVerificacion.this);
                            progressBar.setProgress(0);
                            txt_percent.setText(0+"%");
                        }
                        else {
                            Actives.set(0,-1);
                            Errores+="\nOcurrio un error interno de servidor";
                        }
                    }
                    if(!Actives.contains(0)) FinishSave();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if(!cancel)
                    {
                        Actives.set(0,-1);
                        if(t.getMessage().equals("timeout"))Errores+="\nConexión a servidor perdida, intente de nuevo";
                        else Errores+="\nOcurrio un error al intentar guardar los datos.";
                        if(!Actives.contains(0)) FinishSave();
                    }
                    else{ //GlobalVariables.StrFiles=temp;
                        for(GaleriaModel item:GlobalVariables.StrFiles)
                            if(item.Correlativo==-1)item.Estado="E";
                        loaddata();
                    }
                }
            });
        }
    }

    @NonNull
    private RequestBody createPartFromString(String data){
        return RequestBody.create(MultipartBody.FORM,data);
    }

    @NonNull
    private MultipartBody.Part createPartFromFile(GaleriaModel item){
        ProgressRequestBody fileBody = new ProgressRequestBody(item, this,this);
        return  MultipartBody.Part.createFormData("image", item.Descripcion, fileBody);
    }

    boolean ok,fail;
    public void FinishSave(){

        String Mensaje="Se guardo los datos correctamente";
        String Titulo="Desea Finalizar?";
        int icon=R.drawable.confirmicon;
        ok=false;
        fail=false;
        if(Actives.contains(1))ok=true;
        if(Actives.contains(-1)){
            fail=true;
            icon=R.drawable.erroricon;
            Mensaje=Errores;
            Titulo="Error!";
        }
        if(fail&&ok){
            Titulo="Intente de nuevo";
            Mensaje="Se guardo con los siguientes errores:\n\n";
            Mensaje+=Errores;//.replace("@","\n");
            icon=R.drawable.warninicon;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(Titulo);
        alertDialog.setIcon(icon);
        alertDialog.setMessage(Mensaje);
        if(ok&&!fail)
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    enableSave=(true);
                    ll_bar_carga.setVisibility(View.GONE);
                    progressBar.setProgress(0);
                    GlobalVariables.ObjectEditable=true;
                }
            });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(ok&&!fail){
                    finish();
                }
                ll_bar_carga.setVisibility(View.GONE);
                progressBar.setProgress(0);
                enableSave=(true);
            }
        });
        loaddata();
        alertDialog.show();
    }

    public void loaddata(){
        if(GlobalVariables.StrFiles.size()>0)
        {
            GlobalVariables.listaGaleria.clear();
            GlobalVariables.listaArchivos.clear();
            for (int i = 0; i < GlobalVariables.StrFiles.size(); i++) {
                if (GlobalVariables.StrFiles.get(i).TipoArchivo.equals("TP03")) {
                    GlobalVariables.listaArchivos.add(GlobalVariables.StrFiles.get(i));
                } else {
                    GlobalVariables.listaGaleria.add(GlobalVariables.StrFiles.get(i));
                }
            }
            obs_archivos fragment = (obs_archivos) pageAdapter.getItem(2);
            fragment.setdata();
        }
    }



    @Override
    public void success(String data, String Tipo) {

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    public void onProgressUpdate(){

        progressBar.setProgress(50);
        txt_percent.setText(50+"%");

    }
    @Override
    public void onProgressUpdate(long percentage) {
        if(percentage==0)L=G;
        G=L + percentage;
        int percent=(int)Math.round(100 * (double)G / (double)T);
        progressBar.setProgress(percent);
        txt_percent.setText(percent+"%");//String.format("%.2f", 100*(double)G / (double)T)+"%");
        if(percent==100){
            btncancelar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }


    public void cancelUpload(View view) {
//        if(request!=null){
//            request.cancel();
//            ll_bar_carga.setVisibility(View.GONE); enableSave=true;
//            cancel=true;
//        }
    }








    }
