package com.pango.hsec.hsec.Ingresos.Inspecciones;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.controller.WebServiceAPI;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.ObsInspAddModel;
import com.pango.hsec.hsec.model.ObsInspDetModel;
import com.pango.hsec.hsec.model.PlanModel;
import com.pango.hsec.hsec.obs_archivos;
import com.pango.hsec.hsec.obs_planaccion;
import com.pango.hsec.hsec.util.Compressor;
import com.pango.hsec.hsec.util.ProgressRequestBody;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActObsInspEdit extends FragmentActivity implements IActivity,TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ProgressRequestBody.UploadCallbacks {

    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codObs,grupo,correlativo;
    String json_osbinsp;
    public static boolean editar;
    ProgressBar progressBar;
    int pos=0,indexObd;
    HorizontalScrollView horizontalscroll;
    ArrayList<Integer> Actives=new ArrayList();
    String Errores="";
    Button btn_Salvar;
    Gson gson;
    TextView tx_titulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_obs_det_edit);
        gson = new Gson();
        reiniciadata();
        //GlobalVariables.countObsInsp=1;
        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //GlobalVariables loaddata = new GlobalVariables();
        //loaddata.LoadData();
        horizontalscroll=findViewById(R.id.horizontalscroll);
        progressBar = findViewById(R.id.progressBar2);
        btn_Salvar= findViewById(R.id.btnguardar_obs);
        Bundle datos = this.getIntent().getExtras();
        grupo=datos.getString("Grupo");
        codObs=GlobalVariables.InspeccionObserbacion+"-"+grupo;
        correlativo=datos.getString("correlativo");
        editar=datos.getBoolean("Editar");
        indexObd=datos.getInt("index");
        GlobalVariables.editar_list=editar;
       if(correlativo.equals("-1")){ //load obs of local value
           ObsInspAddModel obsInspAddModel=GlobalVariables.ListobsInspAddModel.get(indexObd);
           GlobalVariables.obsInspDetModel=obsInspAddModel.obsInspDetModel;
           GlobalVariables.listaGaleria=obsInspAddModel.listaGaleria;
           GlobalVariables.listaArchivos=obsInspAddModel.listaArchivos;
           GlobalVariables.Planes=obsInspAddModel.Planes;
           GlobalVariables.ObserbacionFile=obsInspAddModel.obsInspDetModel.CodInspeccion;
           GlobalVariables.ObserbacionPlan=obsInspAddModel.obsInspDetModel.CodInspeccion;
       }

        tx_titulo=findViewById(R.id.tx_titulo);
        if(GlobalVariables.ObjectEditable){
            tx_titulo.setText("Editar Insp/Observación");
        }else{
            tx_titulo.setText("Nuevo Insp/Observación");

        }
        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(ActObsInspEdit.this);

    }
    public void reiniciadata(){
        GlobalVariables.obsInspDetModel= new ObsInspDetModel();
        GlobalVariables.listaArchivos=new ArrayList<>();
        GlobalVariables.listaGaleria=new ArrayList<>();
        GlobalVariables.ObserbacionFile=null;
        GlobalVariables.ObserbacionPlan=null;
        GlobalVariables.Planes=new ArrayList<>();
        //save data Inicial
        GlobalVariables.StrObservacion=null;
        GlobalVariables.StrPlanes=new ArrayList<>();
        GlobalVariables.StrFiles=new ArrayList<>();

    }

    public boolean ValifarFormulario(){
        String ErrorForm="";
        if(StringUtils.isEmpty(GlobalVariables.obsInspDetModel.CodAspectoObs)) ErrorForm+=" ->Aspecto observado\n";
        if(StringUtils.isEmpty(GlobalVariables.obsInspDetModel.CodActividadRel)) ErrorForm+=" ->Actividad relacionada\n";
        if(StringUtils.isEmpty(GlobalVariables.obsInspDetModel.CodNivelRiesgo)) ErrorForm+=" ->Nivel de riesgo\n";
        if(StringUtils.isEmpty(GlobalVariables.obsInspDetModel.Observacion)) ErrorForm+=" ->Observación\n";

        if(ErrorForm.isEmpty()) return true;
        else{
            String Mensaje="Complete los siguientes campos obligatorios:\n"+ErrorForm;
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Datos incorrectos");
            alertDialog.setIcon(R.drawable.warninicon);
            alertDialog.setMessage(Mensaje);

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            return false;
        }
    }

    public void SalvarInspDetalle(View view){
        Utils.closeSoftKeyBoard(this);
        if(!ValifarFormulario()) return;
        btn_Salvar.setEnabled(false);
        String Observacion=  gson.toJson(GlobalVariables.obsInspDetModel);

        if(editar){
            if(!correlativo.equals("-1")){ // edit Obsrvacion existente
                if(!GlobalVariables.StrObservacion.equals(Observacion)){
                    Actives.add(0);
                    String url= GlobalVariables.Url_base+"Inspecciones/PostObservacion";
                    final ActivityController obj = new ActivityController("post", url, ActObsInspEdit.this,this);
                    obj.execute(Observacion,"1");
                }
                else Actives.add(1);

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
                    if(!DeletePlanes.equals("")){
                        Actives.add(0);
                        String url= GlobalVariables.Url_base+"PlanAccion/deleteAll/"+DeletePlanes.substring(0,DeletePlanes.length()-1);
                        ActivityController obj = new ActivityController("get", url, ActObsInspEdit.this,this);
                        obj.execute("1");
                    }
                    else Actives.add(1);
                }else Actives.add(1);
                //edicion files
                UpdateFiles();
            }
            else // edit observacion without codInspeccion
            {
                finisActivity();
            }
            //GlobalVariables.countObsInsp=Integer.parseInt(GlobalVariables.obsInspDetModel.NroDetInspeccion);
        }
        else
            {
             if(!GlobalVariables.InspeccionObserbacion.equals("INSP000000XYZ")){ //new Observacion of CodInspeccion Existente
                 Actives.add(0);
                 obs_archivos archivos = (obs_archivos) pageAdapter.getItem(1);
                 archivos.gridViewAdapter.ProcesarImagens();
                 GlobalVariables.StrFiles= new ArrayList();

                 GlobalVariables.StrFiles.addAll(GlobalVariables.listaGaleria);
                 GlobalVariables.StrFiles.addAll(GlobalVariables.listaArchivos);

                 Retrofit retrofit = new Retrofit.Builder()
                         .baseUrl(GlobalVariables.Url_base)
                         .addConverterFactory(GsonConverterFactory.create())
                         .build();
                 WebServiceAPI service = retrofit.create(WebServiceAPI.class);
                 List<MultipartBody.Part> Files = new ArrayList<>();
                 for (GaleriaModel item:GlobalVariables.StrFiles) {
                     Files.add(createPartFromFile(item));
                 }
                 Toast.makeText(this, "Guardando Observacion, Espere..." , Toast.LENGTH_SHORT).show();

                 Call<String> request = service.insertarInspeccionObs("Bearer "+GlobalVariables.token_auth,createPartFromString(Observacion),createPartFromString(gson.toJson(GlobalVariables.Planes)),Files);
                 progressBar.setVisibility(View.VISIBLE);
                 request.enqueue(new Callback<String>() {
                     @Override
                     public void onResponse(Call<String> call, Response<String> response) {

                         if(response.isSuccessful()){
                             String respt  = response.body();
                             if(respt.equals("-1")){
                                 Actives.set(0,-1);
                                 Errores+="\nOcurrio un error al guardar observacion";
                             }
                             else {

                                 Actives.set(0,1);  // update value Cabecera
                                 Utils.DeleteCache(new Compressor(ActObsInspEdit.this).destinationDirectoryPath); //delete cache Files;
                                 String [] respts= respt.split(";");
                                 grupo=respts[0].split(":")[0];
                                 GlobalVariables.obsInspDetModel.Correlativo=respts[0].split(":")[1];
                                 GlobalVariables.StrObservacion=gson.toJson(GlobalVariables.obsInspDetModel);

                                 FragmentObsInsAdd cabecera = (FragmentObsInsAdd) pageAdapter.getItem(0);
                                 cabecera.updateCodigo(grupo);


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
                                                     item.NroAccionOrigen=grupo;
                                                     item.NroDocReferencia=GlobalVariables.InspeccionObserbacion;
                                                 }
                                                 continue;
                                             }
                                         }
                                     }
                                     obs_planaccion fragment = (obs_planaccion) pageAdapter.getItem(2);
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
                             Actives.set(0,-1);
                             Errores+="\nOcurrio un error interno de servidor";
                         }
                         if(!Actives.contains(0)) FinishSave();///////
                         progressBar.setVisibility(View.GONE);
                     }

                     @Override
                     public void onFailure(Call<String> call, Throwable t) {
                         Actives.set(0,-1);
                         Errores+="\nFallo la subida de archivos";
                         if(!Actives.contains(0)) FinishSave();////////
                         progressBar.setVisibility(View.GONE);
                     }
                 });
             }
             else { //new Observacion without codInspeccion
                 finisActivity();
             }
        }
    }

    public void finisActivity(){

        GlobalVariables.obsInspAddModel=new ObsInspAddModel();
        GlobalVariables.obsInspDetModel.Correlativo="-1";
        GlobalVariables.obsInspAddModel.obsInspDetModel=GlobalVariables.obsInspDetModel;
        GlobalVariables.obsInspAddModel.listaGaleria=GlobalVariables.listaGaleria;
        GlobalVariables.obsInspAddModel.listaArchivos=GlobalVariables.listaArchivos;
        GlobalVariables.obsInspAddModel.Planes=GlobalVariables.Planes;

        json_osbinsp = gson.toJson(GlobalVariables.obsInspAddModel);
        Intent intent = getIntent();
        intent.putExtra("JsonObsInsp", json_osbinsp); //
        intent.putExtra("index", indexObd);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void UpdateFiles (){

        obs_archivos archivos = (obs_archivos) pageAdapter.getItem(1);
        archivos.gridViewAdapter.ProcesarImagens();
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

        if(DeleteFiles.equals("")&&DataInsert.size()==0){
            if(!Actives.contains(0)){ //no hubo ningun gambio
                Actives.clear();
                btn_Salvar.setEnabled(true);
                Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
            }
        }
        else{
//Delete Files
            if(!DeleteFiles.equals("")){
                Actives.add(0);
                String url= GlobalVariables.Url_base+"media/deleteAll/"+DeleteFiles.substring(0,DeleteFiles.length()-1);
                ActivityController obj = new ActivityController("get", url, ActObsInspEdit.this,this);
                obj.execute("2");
            }
            else Actives.add(1);
//Insert Files
            if(DataInsert.size()>0){

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GlobalVariables.Url_base)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WebServiceAPI service = retrofit.create(WebServiceAPI.class);
                Actives.add(0);
                List<MultipartBody.Part> Files = new ArrayList<>();
                for (GaleriaModel item:DataInsert) {
                    Files.add(createPartFromFile(item));
                }
                Toast.makeText(this, "Subiendo Archivos, Espere..." , Toast.LENGTH_SHORT).show();

                Call<String> request = service.uploadAllFile("Bearer "+GlobalVariables.token_auth,createPartFromString(GlobalVariables.InspeccionObserbacion),createPartFromString("TOBS"),createPartFromString(grupo),Files);
                progressBar.setVisibility(View.VISIBLE);
                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if(response.isSuccessful()){
                            String respt  = response.body();
                            if(respt.contains("-1")){
                                Actives.set(3,-1);
                                Errores+="\nOcurrio un error al subir algunos archivos";
                            }
                            else  Actives.set(3,1);
                            Utils.DeleteCache(new Compressor(ActObsInspEdit.this).destinationDirectoryPath); //delete cache Files;
                            for (String file:respt.split(";")) {
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

                        }else{
                            Actives.set(3,-1);
                            Errores+="\nOcurrio un error interno de servidor";
                        }
                        if(!Actives.contains(0)) FinishSave();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Actives.set(3,-1);
                        Errores+="\nFallo la subida de archivos";
                        if(!Actives.contains(0)) FinishSave();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
            else  Actives.add(1);
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
            Mensaje="Ocurrio un error al intentar guardar los datos.";
            Titulo="Ocurrio un Error";
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
                    btn_Salvar.setEnabled(true);
                    GlobalVariables.ObjectEditable=true;
                }
            });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(ok&&!fail){
                    GlobalVariables.obsInspAddModel=new ObsInspAddModel();
                    GlobalVariables.obsInspAddModel.obsInspDetModel=GlobalVariables.obsInspDetModel;

                    json_osbinsp = gson.toJson(GlobalVariables.obsInspAddModel);
                    Intent intent = getIntent();
                    intent.putExtra("JsonObsInsp", json_osbinsp); //
                    intent.putExtra("index", indexObd);
                    setResult(RESULT_OK, intent);
                    finish();

                }
                btn_Salvar.setEnabled(true);
            }
        });
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
            obs_archivos fragment = (obs_archivos) pageAdapter.getItem(1);
            fragment.setdata();
        }

        alertDialog.show();
    }


    public void close(View view){

        finish();
    }

    // Method to add a TabHost
    private static void AddTab(ActObsInspEdit activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {


        pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // Manages the Page changes, synchronizing it with Tabs

    //numero de tab que quieres mostrar


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if(pos==2){
            //pos = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(2);
            pos=0;
        }else{
            pos = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(pos);
        }



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
    public void onPageSelected(int arg0) {
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

/*
		fList.add(Fragment.instantiate(this, ObsFragment.class.getName()));
		fList.add(Fragment.instantiate(this, GaleriaFragment.class.getName()));
		fList.add(Fragment.instantiate(this, PlanFragment.class.getName()));
		fList.add(Fragment.instantiate(this, ComentarioFragment.class.getName()));
		*/



        //GlobalVariables.ObjectEditable=false;//////////valor para editar haciendo petiion del servidor
        // TODO Put here your Fragments
        FragmentObsInsAdd f1 = FragmentObsInsAdd.newInstance(correlativo,grupo);
        obs_archivos f2=obs_archivos.newInstance(codObs);
        obs_planaccion f3 = obs_planaccion.newInstance(codObs);

        fList.add(f1);
        fList.add(f2);
        fList.add(f3);
        //fList.add(f4);



        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        ActObsInspEdit.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab1").setIndicator("OBSERVACIONES"));
        ActObsInspEdit.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab2").setIndicator("GALERÍA"));
        ActObsInspEdit.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab3").setIndicator("PLAN DE ACCIÓN"));
        /*
        ActObsInspDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab4").setIndicator("Comentarios"));*/
        mTabHost.setOnTabChangedListener(this);

    }


    @Override
    public void success(String data, String Tipo) {
        if(Tipo.equals("1")){ //delete Planes
            if(data.contains("-1")){
                Actives.set(1,-1);
                Errores+="\nNo se pudo eliminar algunos planes de accion";
            }
            else {
                GlobalVariables.StrPlanes=GlobalVariables.Planes;
                Actives.set(1,1);
            }
            if(!Actives.contains(0)) FinishSave();
        }
        else if(Tipo.equals("2")){ //delete Files
            if(data.contains("false")){
                Actives.set(2,-1);
                Errores+="\nNo se pudo eliminar algunas imagenes/archivos";
            }
            else {
                Actives.set(2,1);
                ArrayList<GaleriaModel> temp= new ArrayList<>(GlobalVariables.StrFiles);

                for (GaleriaModel item : GlobalVariables.StrFiles) {
                    if(!StringUtils.isEmpty(item.Estado)&&item.Estado.equals("E"))
                        temp.remove(item);
                }
                GlobalVariables.StrFiles=temp;
            }
            if(!Actives.contains(0)) FinishSave();
        }
    }

    @Override
    public void successpost(String data, String Tipo) {
        Gson gson = new Gson();
        if(Tipo.equals("1")){
            if(data.contains("-1"))  {
                Actives.set(0,-1);
                Errores+="Error al guardar cabecera de observación\n";
            }
            else{
                GlobalVariables.StrObservacion=gson.toJson(GlobalVariables.Obserbacion);
                Actives.set(0,1);
            }
            if(!Actives.contains(0)) FinishSave();
        }
    }

    @Override
    public void error(String mensaje, String Tipo) {

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
