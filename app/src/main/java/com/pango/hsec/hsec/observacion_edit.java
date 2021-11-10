
package com.pango.hsec.hsec;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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
import com.pango.hsec.hsec.Ingresos.Inspecciones.ActObsInspEdit;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.controller.WebServiceAPI;
import com.pango.hsec.hsec.model.ControlCriticoModel;
import com.pango.hsec.hsec.model.EquipoModel;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.ObsComentModel;
import com.pango.hsec.hsec.model.ObsDetalleModel;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.model.PlanModel;
import com.pango.hsec.hsec.model.SubDetalleModel;
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

public class observacion_edit extends FragmentActivity implements IActivity,TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ProgressRequestBody.UploadCallbacks{
    public MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    public TabHost mTabHost;
    ImageButton close,btncancelar;
    Button btn_Salvar;
    TextView tx_titulo,txt_percent;
    ProgressBar progressBar;
    HorizontalScrollView horizontalsv;
    int pos=0;
    ArrayList<Integer> Actives=new ArrayList();
    String CodObservacion,CodTipo,Errores="";
    ConstraintLayout ll_bar_carga;
    Call<String> request;
    Boolean cancel, enableSave=true;
    List<Fragment> fragments;
    int posionOld=0;
    String OldTipoSelect;
    long L,G,T;
    //TabHost tabHost;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacion_edit);

        reiniciadata();
        close=findViewById(R.id.imageButton);
        btn_Salvar=(Button)findViewById(R.id.btn_Salvar);
        horizontalsv=findViewById(R.id.HorizontalObsedit);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        progressBar = findViewById(R.id.progressBar2);
        btncancelar= (ImageButton)findViewById(R.id.cancel_upload);
        txt_percent= (TextView)findViewById(R.id.txt_percent);
        ll_bar_carga=findViewById(R.id.ll_bar_carga);
        ll_bar_carga.setVisibility(View.GONE);
        Bundle datos = this.getIntent().getExtras();
        CodObservacion=datos.getString("codObs");
        CodTipo=datos.getString("tipoObs");
        pos=datos.getInt("posTab");
        OldTipoSelect=CodTipo;
        //if(GlobalVariables.ObjectEditable) CodObservacion=datos.getString("Observacion");
        tx_titulo=findViewById(R.id.tx_titulo);
        if(GlobalVariables.ObjectEditable){
            tx_titulo.setText("Editar Observación");
        }else{
            tx_titulo.setText("Nueva Observación");
        }

        fragments =  getFragments(); //CodTipoChange.equals("TO01")|| CodTipoChange.equals("TO02")? getFragments():
        initialiseTabHost();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mTabHost.setCurrentTab(pos);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(observacion_edit.this);

    }

    public void reiniciadata(){
        GlobalVariables.Obserbacion= new ObservacionModel();
        GlobalVariables.ObserbacionDetalle= new ObsDetalleModel();
        GlobalVariables.listaArchivos=new ArrayList<>();
        GlobalVariables.listaGaleria=new ArrayList<>();
        GlobalVariables.ObserbacionFile=null;
        GlobalVariables.ObserbacionPlan=null;
        GlobalVariables.ObserbacionInvolucrados=null;
        GlobalVariables.ObserbacionSubdetalle=null;
        GlobalVariables.Planes=new ArrayList<>();
        GlobalVariables.SubDetalleTa=new ArrayList<>();
        GlobalVariables.SubDetalleIS=new ArrayList<>();
        GlobalVariables.CriteriosEvalCC=new ArrayList<>();
        GlobalVariables.ListResponsables=new ArrayList<>();
        GlobalVariables.ListAtendidos=new ArrayList<>();
        //save data Inicial
        GlobalVariables.StrObservacion=null;
        GlobalVariables.StrObsDetalle=null;
        GlobalVariables.StrPlanes=new ArrayList<>();
        GlobalVariables.StrFiles=new ArrayList<>();
        GlobalVariables.StrResponsables=new ArrayList<>();
        GlobalVariables.StrAtendidos=new ArrayList<>();
        GlobalVariables.StrSubDetalleTa=new ArrayList<>();
        GlobalVariables.StrSubDetalleIS=new ArrayList<>();
        GlobalVariables.StrCriteriosEvalCC=new ArrayList<>();
        //inicialize options
        GlobalVariables.obsInspDetModel=null;
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
    private static void AddTab(observacion_edit activity, TabHost tabHost,TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {

        int pos = this.mTabHost.getCurrentTab();
        if((GlobalVariables.Obserbacion.CodTipo.equals("TO02")|| GlobalVariables.Obserbacion.CodTipo.equals("TO01"))&&pos==2)
        {
            if(pos>posionOld) pos+=1;
            else pos-=1;
        }
        this.mViewPager.setCurrentItem(pos);
        if(pos==1 ){ //cambios solo cuando ingresemos al tab de detalle de obs. //&& !GlobalVariables.Obserbacion.CodTipo.equals(OldTipoSelect)
            if(!GlobalVariables.Obserbacion.CodTipo.equals(OldTipoSelect)){
                GlobalVariables.ObserbacionDetalle.CodHHA=null;
                GlobalVariables.ObserbacionDetalle.Accion=null;
                GlobalVariables.ObserbacionDetalle.CodActiRel=null;
                GlobalVariables.ObserbacionDetalle.CodSubEstandar=null;
                OldTipoSelect=GlobalVariables.Obserbacion.CodTipo;
            }
            obs_detalle1 detalle = (obs_detalle1) pageAdapter.getItem(1);
            detalle.changueTipo(GlobalVariables.Obserbacion.CodTipo,GlobalVariables.Obserbacion.CodSubTipo);
        }
        else if(pos==2 && (GlobalVariables.Obserbacion.CodTipo.equals("TO03")|| GlobalVariables.Obserbacion.CodTipo.equals("TO04"))){ //&&!GlobalVariables.Obserbacion.CodTipo.equals(OldTipoSelect)
            obs_detalle2 detalle = (obs_detalle2) pageAdapter.getItem(2);
            detalle.changueTipo(GlobalVariables.Obserbacion.CodTipo,GlobalVariables.Obserbacion.CodSubTipo);
            OldTipoSelect=GlobalVariables.Obserbacion.CodTipo;
        }
        posionOld=pos;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // Manages the Page changes, synchronizing it with Tabs
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if(pos==2){
            //pos = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(pos);
            pos=0;
        }else{
            int posi = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(posi);
        }

        View tabView = mTabHost.getTabWidget().getChildAt(position);
        if (tabView != null)
        {
            final int width = horizontalsv.getWidth();
            final int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
            horizontalsv.scrollTo(scrollPos, 0);
        } else {
            horizontalsv.scrollBy(positionOffsetPixels, 0);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
    }

    public List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        // TODO Put here your Fragments
        obs_cabecera f1 = obs_cabecera.newInstance(CodObservacion);
        obs_detalle1 f2 = obs_detalle1.newInstance(CodObservacion,CodTipo);
        obs_detalle2 f3 = obs_detalle2.newInstance(CodObservacion,CodTipo);
        obs_archivos f4 = obs_archivos.newInstance(CodObservacion);
        obs_planaccion f5=obs_planaccion.newInstance(CodObservacion);


        fList.add(f1);
        fList.add(f2);
        fList.add(f3);
        fList.add(f4);
        fList.add(f5);
        //fList.remove(f4);


        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab1").setIndicator("Observación"));
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab2").setIndicator("Detalle"));
        //if(CodTipoC.equals("TO03") || CodTipoC.equals("TO04"))
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab3").setIndicator("Detalle 2"));
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab4").setIndicator("Archivos"));
        observacion_edit.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab5").setIndicator("Plan Accion"));
        //observacion_edit.
        mTabHost.setOnTabChangedListener(this);
    }

    public void outObservacion(){
        boolean Nochangues=true;
        Gson gson = new Gson();
        Utils.closeSoftKeyBoard(this);
        String Observacion=  gson.toJson(GlobalVariables.Obserbacion);

        if(GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO01")){
          //  GlobalVariables.ObserbacionDetalle.ComOpt1=null;
            GlobalVariables.ObserbacionDetalle.ComOpt2=null;
            GlobalVariables.ObserbacionDetalle.ComOpt3=null;
        }
        else if(GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO02")){
            GlobalVariables.ObserbacionDetalle.CodError=null;
            GlobalVariables.ObserbacionDetalle.CodEstado=null;
           // GlobalVariables.ObserbacionDetalle.ComOpt1=null;
            GlobalVariables.ObserbacionDetalle.ComOpt2=null;
            GlobalVariables.ObserbacionDetalle.ComOpt3=null;
        }
        else if(GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO04")){
            for(SubDetalleModel item:GlobalVariables.SubDetalleIS){
                if(item.CodSubtipo.equals("COMCON11")) GlobalVariables.ObserbacionDetalle.ComOpt1=item.Descripcion;
                else if(item.CodSubtipo.equals("19"))GlobalVariables.ObserbacionDetalle.ComOpt2=item.Descripcion;
            }
            GlobalVariables.ObserbacionDetalle.CodEstado=GlobalVariables.ListAtendidos.size()>0?GlobalVariables.ListAtendidos.get(0).CodPersona:null;
            GlobalVariables.ObserbacionDetalle.ComOpt3=null;
        }

        String DetalleObs=  gson.toJson(GlobalVariables.ObserbacionDetalle);
        if(!GlobalVariables.StrObservacion.equals(Observacion))Nochangues=false;
        if(Nochangues&&!GlobalVariables.StrObsDetalle.equals(DetalleObs)) Nochangues=false;
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
        else if(Nochangues){
            //update Tarea e IS
            if(GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO03") || GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO04")){
                //update involucrados
                ArrayList<EquipoModel> updateAtentidos = new ArrayList<>();
                ArrayList<EquipoModel> PerInvolucrados=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.ListResponsables:GlobalVariables.ListAtendidos;
                ArrayList<EquipoModel> StrPerInvolucrados=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.StrResponsables:GlobalVariables.StrAtendidos;

                // get Lider
                String LiderPer="",LiderOld="";
                for (EquipoModel item:StrPerInvolucrados) {
                    if(item.Lider.equals("1")) LiderOld=item.CodPersona;
                }
                for (EquipoModel item:PerInvolucrados) {
                    if(item.Lider.equals("1")) LiderPer=item.CodPersona;
                }

                //Insert involucrados
                for (EquipoModel item:PerInvolucrados) {
                    if(!item.Lider.equals("1")){
                        boolean pass=false;
                        for(EquipoModel item2:StrPerInvolucrados)
                            if(item.CodPersona.equals(item2.CodPersona))
                                pass=true;
                        if(!StringUtils.isEmpty(item.NroReferencia)&&item.NroReferencia.equals("-1")) {
                            updateAtentidos.add(new EquipoModel(item.CodPersona,"A"));
                            if(!pass)StrPerInvolucrados.add(item);
                        }
                    }
                }
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO03")) GlobalVariables.StrResponsables=StrPerInvolucrados;
                else  GlobalVariables.StrAtendidos=StrPerInvolucrados;
                //Delete involucrados
                for (EquipoModel item:StrPerInvolucrados) {
                    if(!item.Lider.equals("1")){
                        boolean pass=true;
                        for (EquipoModel item2:PerInvolucrados) {
                            if(item.CodPersona.equals(item2.CodPersona)){
                                pass=false;
                                continue;
                            }
                        }
                        if(pass){
                            item.Estado="E";
                            updateAtentidos.add(new EquipoModel(item.CodPersona,"E"));
                        }
                    }
                }
                if(!LiderOld.equals(LiderPer)){
                    boolean pass=true;
                    for(EquipoModel item:updateAtentidos)
                        if(item.CodPersona.equals(LiderOld)) {
                            pass=false;
                            break;
                        }//agregamos el lider antiguo, que no haya sido eliminado
                    if(pass)updateAtentidos.add(new EquipoModel(LiderPer,"A"));
                }
                if(updateAtentidos.size()>0)Nochangues=false;

                // update SubDetalle
                ArrayList<SubDetalleModel> updateSubDetalle = new ArrayList<>();
                List<SubDetalleModel> SubDetalle=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.SubDetalleTa:GlobalVariables.SubDetalleIS;
                List<SubDetalleModel> StrSubDetalle=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.StrSubDetalleTa:GlobalVariables.StrSubDetalleIS;
                //Insert SubDetalle
                for (SubDetalleModel item:SubDetalle) {
                    boolean pass=true;
                    for(SubDetalleModel item2:StrSubDetalle)
                    {
                        if(GlobalVariables.Obserbacion.CodTipo.equals("TO03") && item.Codigo.equals(item2.Codigo) && item.CodSubtipo.equals(item2.CodSubtipo) && item.Descripcion.equals(item2.Descripcion)){
                            pass=false;
                            continue;
                        }
                        else if(GlobalVariables.Obserbacion.CodTipo.equals("TO04") && item.CodTipo.equals(item2.CodTipo) && item.CodSubtipo.equals(item2.CodSubtipo)){
                            pass=false;
                            continue;
                        }
                    }
                    if(pass){
                        item.Estado="A";
                        updateSubDetalle.add(item);
                        if(StringUtils.isEmpty(item.Codigo)) StrSubDetalle.add(item);
                    }
                }
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO03")) GlobalVariables.StrSubDetalleTa=StrSubDetalle;
                else  GlobalVariables.StrSubDetalleIS=StrSubDetalle;
                //Delete Subdetalle
                for (SubDetalleModel item:StrSubDetalle) {
                    boolean pass=true;
                    for (SubDetalleModel item2:SubDetalle) {
                        if(GlobalVariables.Obserbacion.CodTipo.equals("TO03") && item.Codigo.equals(item2.Codigo)){
                            pass=false;
                            continue;
                        }
                        else if(item.CodTipo.equals(item2.CodTipo) && item.CodSubtipo.equals(item2.CodSubtipo)){
                            pass=false;
                            continue;
                        }
                    }
                    if(pass){
                        item.Estado="E";
                        updateSubDetalle.add(item);
                    }
                }
                if(updateSubDetalle.size()>0)Nochangues=false;
            }
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
        int subtipo= 0;
        if(GlobalVariables.Obserbacion.CodTipo.equals("TO04")) subtipo=Integer.parseInt(GlobalVariables.Obserbacion.CodSubTipo);
        if(StringUtils.isEmpty(GlobalVariables.Obserbacion.CodObservadoPor)) {ErrorForm+="Observado Por";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.Obserbacion.CodAreaHSEC)) {ErrorForm+="Area HSEC";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.Obserbacion.CodNivelRiesgo)) {ErrorForm+="Nivel de riesgo";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.Obserbacion.Fecha)) {ErrorForm+="Fecha";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.Obserbacion.CodUbicacion)) {ErrorForm+="Ubicación";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.Obserbacion.Lugar)) {ErrorForm+="Lugar";pos=0;}
        else if(subtipo<3&&StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.Observacion.trim())) {ErrorForm+="Observacion";pos=1;}
        else if(!GlobalVariables.Obserbacion.CodTipo.equals("TO04") && StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.StopWork)) {ErrorForm+="Stopwork";pos=1;}
        else if(GlobalVariables.Obserbacion.CodTipo.equals("TO01")){
            String DescCov= GlobalVariables.getDescripcion(GlobalVariables.Acto_obs,GlobalVariables.ObserbacionDetalle.CodSubEstandar);
            if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.Accion.trim())) {ErrorForm+="Accion";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodActiRel)) {ErrorForm+="Actividad Relacionada";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodHHA)) {ErrorForm+="HHA Relacionada";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar)) {ErrorForm+="Acto SubEstandar";pos=1;}
            else if(DescCov.contains("EPP") && StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.ComOpt1)) {ErrorForm+="EPP";pos=1;}
            else if(DescCov.contains("COVID") && StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.ComOpt1)) {ErrorForm+="Sub Tipo Covid";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodEstado)) {ErrorForm+="Estado";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodError)) {ErrorForm+="Error";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodCorreccion)) {ErrorForm+="Correccion";pos=1;}
        }
        else if (GlobalVariables.Obserbacion.CodTipo.equals("TO02")){
            String DescCov= GlobalVariables.getDescripcion(GlobalVariables.Condicion_obs,GlobalVariables.ObserbacionDetalle.CodSubEstandar);
            if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.Accion.trim())) {ErrorForm+="Accion";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodActiRel)) {ErrorForm+="Actividad Relacionada";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodHHA)) {ErrorForm+="HHA Relacionada";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar)) {ErrorForm+="Condicion SubEstandar";pos=1;}
            else if(DescCov.contains("COVID") && StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.ComOpt1)) {ErrorForm+="Sub Tipo Covid";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodCorreccion)) {ErrorForm+="Correccion";pos=1;}
        }
        else if (GlobalVariables.Obserbacion.CodTipo.equals("TO03")){
            if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.Accion.trim())) {ErrorForm+="Codigo PET";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodActiRel)) {ErrorForm+="Actividad Relacionada";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodHHA)) {ErrorForm+="HHA Relacionada";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodEstado)) {ErrorForm+="Estado";pos=1;}
            else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodError)) {ErrorForm+="Error";pos=1;}
            else if(GlobalVariables.ListResponsables.isEmpty()) {ErrorForm+="Personas observadas";pos=2;}
        }
        else if (GlobalVariables.Obserbacion.CodTipo.equals("TO04")){
            if(subtipo>1){
                if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.Accion.trim())) {ErrorForm+="Gerencia Observada";pos=1;}
                else if(GlobalVariables.CriteriosEvalCC.isEmpty()) {ErrorForm+="Subproceso";pos=2;}
            }
            else{
                if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodHHA.trim())) {ErrorForm+="Equipo involucrado";pos=1;}
                else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.StopWork)) {ErrorForm+="Stopwork";pos=1;}
                else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar)) {ErrorForm+="Interaccion de seguridad";pos=1;}
                else if(GlobalVariables.ListAtendidos.isEmpty()) {ErrorForm+="Realizado por";pos=2;}
            }
        }
        if(ErrorForm.isEmpty()) return true;
        else{
            Snackbar.make(view, "El campo "+ErrorForm+" no puede estar vacío", Snackbar.LENGTH_LONG).setActionTextColor(Color.CYAN).setAction("Ver pestaña", new View.OnClickListener() {
                //public TabHost mTabHost;

                @Override
                public void onClick(View v) {
                    mTabHost.setCurrentTab(pos);
                }
            }).show();
            return false;
        }
    }
    public void SalvarObservacion(View view){
        if(!enableSave)return;
        Gson gson = new Gson();
        Utils.closeSoftKeyBoard(this);
        if(!ValifarFormulario(view)) return;
        enableSave=(false);
        cancel=false;
        btncancelar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        txt_percent.setText("");
        String Observacion=  gson.toJson(GlobalVariables.Obserbacion);

        if(GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO01")){
            //GlobalVariables.ObserbacionDetalle.ComOpt1=null;
            GlobalVariables.ObserbacionDetalle.ComOpt2=null;
            GlobalVariables.ObserbacionDetalle.ComOpt3=null;
        }
        else if(GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO02")){
            GlobalVariables.ObserbacionDetalle.CodError=null;
            GlobalVariables.ObserbacionDetalle.CodEstado=null;
            //GlobalVariables.ObserbacionDetalle.ComOpt1=null;
            GlobalVariables.ObserbacionDetalle.ComOpt2=null;
            GlobalVariables.ObserbacionDetalle.ComOpt3=null;
        }
        else if(GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO04")){
            int subtipo= Integer.parseInt(GlobalVariables.Obserbacion.CodSubTipo);
            if(subtipo>1){
                GlobalVariables.ObserbacionDetalle.CodHHA= GlobalVariables.Obserbacion.CodSubTipo;
                GlobalVariables.ObserbacionDetalle.CodSubEstandar= GlobalVariables.Obserbacion.CodObservadoPor;
                GlobalVariables.ObserbacionDetalle.ComOpt2=null;
                GlobalVariables.ObserbacionDetalle.ComOpt1=null;
                GlobalVariables.ObserbacionDetalle.StopWork=null;
                GlobalVariables.ObserbacionDetalle.CodCorreccion=null;
            }
            else {
                for(SubDetalleModel item:GlobalVariables.SubDetalleIS){
                    if(item.CodSubtipo.equals("COMCON11")) GlobalVariables.ObserbacionDetalle.ComOpt1=item.Descripcion;
                    else if(item.CodSubtipo.equals("19"))GlobalVariables.ObserbacionDetalle.ComOpt2=item.Descripcion;
                }
                GlobalVariables.ObserbacionDetalle.CodEstado=GlobalVariables.ListAtendidos.get(0).CodPersona;
            }
            GlobalVariables.ObserbacionDetalle.ComOpt3=null;
        }
        String DetalleObs=  gson.toJson(GlobalVariables.ObserbacionDetalle);
        Errores="";
        Actives.clear();
        if(GlobalVariables.ObjectEditable){

            String Cabecera,Detalle, PlanesDelete, FilesDelete,Involucrados,subdetalle;
            Cabecera=Detalle=PlanesDelete=FilesDelete=Involucrados=subdetalle="-";

            if(!GlobalVariables.StrObservacion.equals(Observacion)) Cabecera=Observacion;
            if(!GlobalVariables.StrObsDetalle.equals(DetalleObs)) Detalle=DetalleObs;

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

            obs_archivos archivos = (obs_archivos) pageAdapter.getItem(3);
            if(archivos.gridViewAdapter !=null)archivos.gridViewAdapter.ProcesarImagens();

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
            //update Tarea e IS
            if(GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO03") || GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO04")){
                //update involucrados
                ArrayList<EquipoModel> updateAtentidos = new ArrayList<>();
                ArrayList<EquipoModel> PerInvolucrados=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.ListResponsables:GlobalVariables.ListAtendidos;
                ArrayList<EquipoModel> StrPerInvolucrados=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.StrResponsables:GlobalVariables.StrAtendidos;

                //Insert involucrados
                for (EquipoModel item:PerInvolucrados) {
                    if(item.Lider ==null || !item.Lider.equals("1")){
                        boolean pass=false;
                        for(EquipoModel item2:StrPerInvolucrados)
                            if(item.CodPersona.equals(item2.CodPersona))
                                pass=true;
                        if(!StringUtils.isEmpty(item.NroReferencia)&&item.NroReferencia.equals("-1")) {
                            updateAtentidos.add(new EquipoModel(item.CodPersona,"A"));
                            if(!pass)try {
                                StrPerInvolucrados.add((EquipoModel)item.clone());
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO03")) GlobalVariables.StrResponsables=StrPerInvolucrados;
                else  GlobalVariables.StrAtendidos=StrPerInvolucrados;
                //Delete involucrados
                for (EquipoModel item:StrPerInvolucrados) {
                    if(item.Lider ==null ||!item.Lider.equals("1")){
                        boolean pass=true;
                        for (EquipoModel item2:PerInvolucrados) {
                            if(item.CodPersona.equals(item2.CodPersona)){
                                pass=false;
                                continue;
                            }
                        }
                        if(pass){
                            item.Estado="E";
                            updateAtentidos.add(new EquipoModel(item.CodPersona,"E"));
                        }
                    }
                }

                // get Lider
              /*  if(GlobalVariables.Obserbacion.CodTipo.equals("TO04")){
                    String LiderPer="";
                    EquipoModel LiderOld=new EquipoModel();
                    boolean exEquipo=false;
                    for (EquipoModel item:PerInvolucrados) {
                        if(item.Lider.equals("1")) LiderPer=item.CodPersona;
                    }
                    for (EquipoModel item:StrPerInvolucrados) {
                        if(item.Lider.equals("1")) LiderOld=item;
                        else if(item.CodPersona.equals(LiderPer))exEquipo=true;
                    }
                    if(!LiderOld.CodPersona.equals(LiderPer)&&!LiderOld.equals("")){
                        boolean pass=true;
                        for(EquipoModel item:updateAtentidos){
                            if(item.CodPersona.equals(LiderOld)&& item.Estado.equals("E")) {
                                pass=false;
                                break;
                            }//agregamos el lider antiguo, que no haya sido eliminado
                        }
                        if(pass){
                            updateAtentidos.add(new EquipoModel(LiderOld.CodPersona,"A"));
                            LiderOld.Lider="0";
                        }
                        if(exEquipo)updateAtentidos.add(new EquipoModel(LiderPer,"E"));
                    }
                }*/

                if(GlobalVariables.Obserbacion.CodTipo.equals("TO03") && updateAtentidos.size()>0){
                    Involucrados=gson.toJson(updateAtentidos);
                }

                // update SubDetalle
                ArrayList<SubDetalleModel> updateSubDetalle = new ArrayList<>();
                List<SubDetalleModel> SubDetalle=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.SubDetalleTa:GlobalVariables.SubDetalleIS;
                List<SubDetalleModel> StrSubDetalle=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.StrSubDetalleTa:GlobalVariables.StrSubDetalleIS;
                //Insert SubDetalle
                for (SubDetalleModel item:SubDetalle) {
                    boolean pass=true;
                    for(SubDetalleModel item2:StrSubDetalle)
                    {
                        if( GlobalVariables.Obserbacion.CodTipo.equals("TO03") && item.Codigo.equals(item2.Codigo) && item.CodSubtipo.equals(item2.CodSubtipo) && item.Descripcion.equals(item2.Descripcion)){
                            pass=false;
                            continue;
                        }
                        else if(GlobalVariables.Obserbacion.CodTipo.equals("TO04") && !item.CodTipo.equals("OBVE") && item.CodTipo.equals(item2.CodTipo) && item.CodSubtipo.equals(item2.CodSubtipo)){
                            pass=false;
                            continue;
                        }
                    }
                    if(pass){
                        item.Estado="A";
                        updateSubDetalle.add(item);

                    }
                }

                //Delete Subdetalle
                for (SubDetalleModel item:StrSubDetalle) {
                    boolean pass=true;
                    for (SubDetalleModel item2:SubDetalle) {
                        if(GlobalVariables.Obserbacion.CodTipo.equals("TO03") && item.Codigo.equals(item2.Codigo)){
                            pass=false;
                            continue;
                        }
                        else if(GlobalVariables.Obserbacion.CodTipo.equals("TO04") && item.CodTipo.equals(item2.CodTipo) && item.CodSubtipo.equals(item2.CodSubtipo)){
                            pass=false;
                            continue;
                        }
                    }
                    if(pass){
                        item.Estado="E";
                        updateSubDetalle.add(item);
                    }
                }
                if(updateSubDetalle.size()>0){
                    int cont=0;
                    for(SubDetalleModel item: updateSubDetalle){
                        if(StringUtils.isEmpty(item.Codigo)){
                            cont--;
                            item.Codigo=cont+"";
                            try {
                                StrSubDetalle.add((SubDetalleModel) item.clone());
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if(GlobalVariables.Obserbacion.CodTipo.equals("TO03")) GlobalVariables.StrSubDetalleTa=StrSubDetalle;
                    else  GlobalVariables.StrSubDetalleIS=StrSubDetalle;
                    subdetalle=gson.toJson(updateSubDetalle);
                }
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO04")&&(GlobalVariables.Obserbacion.CodSubTipo.equals("2") || GlobalVariables.Obserbacion.CodSubTipo.equals("3"))){
                    if(GlobalVariables.CriteriosEvalCC.size()>0){
                        for(ControlCriticoModel item : GlobalVariables.CriteriosEvalCC){
                            String Descripcion = "";
                            if(item.Respuesta.equals("R002"))Descripcion = item.Justificacion+'@'+item.AccionCorrectiva;
                            updateSubDetalle.add(new SubDetalleModel(item.CodigoCC,"CARE",item.CodigoCriterio,Descripcion,item.Respuesta));
                        }
                        subdetalle=gson.toJson(updateSubDetalle);
                    }
                }
            }

            if(DataInsert.size()==0 && FilesDelete.equals("-")&& PlanesDelete.equals("-")&& Detalle.equals("-")&& Cabecera.equals("-")&& Involucrados.equals("-")&& subdetalle.equals("-"))
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
                request = service.actualizarObservacion("Bearer "+GlobalVariables.token_auth,createPartFromString(Cabecera),createPartFromString(Detalle),createPartFromString(PlanesDelete),createPartFromString(FilesDelete),createPartFromString(CodObservacion),createPartFromString(Involucrados),createPartFromString(subdetalle),Files);
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
                                            GlobalVariables.StrObservacion=gson.toJson(GlobalVariables.Obserbacion);
                                            break;
                                        case 1:
                                            GlobalVariables.StrObsDetalle=gson.toJson(GlobalVariables.ObserbacionDetalle);
                                            break;
                                        case 2:
                                            GlobalVariables.StrPlanes=GlobalVariables.Planes;
                                            break;
                                        case 3:
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

                            if(!respt[4].equals("-")){
                                Utils.DeleteCache(new Compressor(observacion_edit.this).destinationDirectoryPath); //delete cache Files;
                                for (String file:respt[4].split(",")) {
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

                            //update Involucrados
                            if(respt[5].contains(":-1")){
                                Actives.add(-1);
                                Errores+="\nError al guardar algunas personas involucradas";
                            }
                            // update Atendidos
                            if(!respt[5].equals("-"))
                            {
                                ArrayList<EquipoModel> PerInvolucrados=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.ListResponsables:GlobalVariables.ListAtendidos;
                                ArrayList<EquipoModel> StrPerInvolucrados=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.StrResponsables:GlobalVariables.StrAtendidos;
                                ArrayList<EquipoModel> NewInvolucrados= new ArrayList<>();
                                for(EquipoModel item:StrPerInvolucrados){
                                    boolean pass=true;
                                    for (String equipoid:respt[5].split(","))
                                    {
                                        String[] value= equipoid.split(":");
                                        if(item.CodPersona.equals(value[0])){
                                            if(value[1].equals("-1")) pass=false;
                                            else item.NroReferencia=null;
                                            continue;
                                        }
                                    }
                                    if(pass && (item.Estado==null || item.Estado.equals("A"))){
                                        NewInvolucrados.add(item);
                                    }
                                    else if(!pass&&item.Estado!=null&&item.Estado.equals("E")){ //persona que no se pudo eliminar
                                        NewInvolucrados.add(item);
                                        try {
                                            PerInvolucrados.add((EquipoModel)item.clone());
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else { // personas fallidas de insertar
                                        for(EquipoModel item2:PerInvolucrados)
                                            if(item2.CodPersona.equals(item.CodPersona)) item2.Estado="E";
                                    }
                                }

                                if(GlobalVariables.Obserbacion.CodTipo.equals("TO03"))GlobalVariables.StrResponsables=NewInvolucrados;
                                else GlobalVariables.StrAtendidos=NewInvolucrados;
                            }
                            //update Subdetalle
                            if(respt[6].contains(":-1")){
                                Actives.add(-1);
                                Errores+="\nError al guardar: ";
                            }
                            // update Subdetalle
                            if(!respt[6].equals("-"))
                            {
                                List<SubDetalleModel> SubDetalle=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.SubDetalleTa:GlobalVariables.SubDetalleIS;
                                List<SubDetalleModel> StrSubDetalle=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.StrSubDetalleTa:GlobalVariables.StrSubDetalleIS;
                                ArrayList<SubDetalleModel> updateSubDetalle = new ArrayList<>();
                                String ErrorSubDeta="";
                                for(SubDetalleModel item:StrSubDetalle){
                                    boolean pass=true;
                                    for (String equipoid:respt[6].split(","))
                                    {
                                        String[] value= equipoid.split(":");
                                        if(item.Codigo.equals(value[0])){
                                            if(value[1].equals("-1")){
                                                String DescTipo= GlobalVariables.getDescripcion(GlobalVariables.SubDetalleTipoDesc,item.CodTipo);
                                                if(!ErrorSubDeta.contains(DescTipo))ErrorSubDeta+=DescTipo+", ";
                                                pass=false;
                                            }
                                            else item.Codigo=value[1];
                                            continue;
                                        }
                                    }
                                    if(pass){
                                        try {
                                            StrSubDetalle.add((SubDetalleModel)item.clone());
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if(pass && item.Estado==null ){
                                        updateSubDetalle.add(item);
                                    }
                                    else if(!pass&& item.Estado!=null&&item.Estado.equals("E")){ //Subdetalle que no se pudo eliminar
                                        updateSubDetalle.add(item);
                                        try {
                                            SubDetalle.add((SubDetalleModel) item.clone());
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else { // Subdetalle fallidas de insertar
                                        for(SubDetalleModel item2:SubDetalle)
                                            if(item2.Codigo.equals(item.Codigo)) item2.Estado="E";
                                    }
                                }
                                if(!ErrorSubDeta.equals(""))Errores+=ErrorSubDeta.substring(0,ErrorSubDeta.length()-2);

                                if(GlobalVariables.Obserbacion.CodTipo.equals("TO03"))GlobalVariables.StrSubDetalleTa=StrSubDetalle;
                                else GlobalVariables.StrSubDetalleIS=StrSubDetalle;
                            }

                        }else{
                            if(response.code()==401){
                                Utils.reloadTokenAuth(observacion_edit.this,observacion_edit.this);
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
            obs_archivos archivos = (obs_archivos) pageAdapter.getItem(3);
            if(archivos.gridViewAdapter !=null)archivos.gridViewAdapter.ProcesarImagens();
            GlobalVariables.StrFiles= new ArrayList();

            GlobalVariables.StrFiles.addAll(GlobalVariables.listaGaleria);
            GlobalVariables.StrFiles.addAll(GlobalVariables.listaArchivos);

            //add Tarea e IS, :: Involucrados y subdetalle
            List<EquipoModel> PerInvolucrados= new ArrayList<>();
            List<SubDetalleModel> Subdetalle= new ArrayList<>();
            List<ControlCriticoModel> ControlCritico= new ArrayList<>();

            if(GlobalVariables.Obserbacion.CodTipo.equals("TO03")) {
                PerInvolucrados=GlobalVariables.ListResponsables;
               // GlobalVariables.StrResponsables.addAll(PerInvolucrados);
                Subdetalle=GlobalVariables.SubDetalleTa;
                //GlobalVariables.StrSubDetalleTa.addAll(Subdetalle);
            }
            else if(GlobalVariables.Obserbacion.CodTipo.equals("TO04")) {
                Subdetalle=GlobalVariables.SubDetalleIS;
                int subtipo= Integer.parseInt(GlobalVariables.Obserbacion.CodSubTipo);
               if(subtipo>1) ControlCritico= GlobalVariables.CriteriosEvalCC;
            }

            if(Subdetalle.size()>0){
                int cont=0;
                for(SubDetalleModel item: Subdetalle){
                    if(StringUtils.isEmpty(item.Codigo)){
                        cont--;
                        item.Codigo=cont+"";
                    }
                }
            }

            if(ControlCritico.size()>0){
                for(ControlCriticoModel item : ControlCritico){
                    String Descripcion = "";
                    if(item.Respuesta.equals("R002"))Descripcion = item.Justificacion+'@'+item.AccionCorrectiva;
                    Subdetalle.add(new SubDetalleModel(item.CodigoCC,"CARE",item.CodigoCriterio,Descripcion,item.Respuesta));
                }
            }

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
            String subDetalle= gson.toJson(Subdetalle);
            request = service.insertarObservacion("Bearer "+GlobalVariables.token_auth,createPartFromString(Observacion),createPartFromString(DetalleObs),createPartFromString(gson.toJson(GlobalVariables.Planes)),createPartFromString(gson.toJson(PerInvolucrados)),createPartFromString(gson.toJson(Subdetalle)),Files);
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
                            Utils.DeleteCache(new Compressor(observacion_edit.this).destinationDirectoryPath); //delete cache Files;
                            String [] respts= respt.split(";");
                            GlobalVariables.Obserbacion.CodObservacion=respts[0];
                            GlobalVariables.ObserbacionDetalle.CodObservacion= respts[0];
                            GlobalVariables.StrObservacion=gson.toJson(GlobalVariables.Obserbacion);
                            GlobalVariables.StrObsDetalle=gson.toJson(GlobalVariables.ObserbacionDetalle);
                            GlobalVariables.ObserbacionFile= respts[0];
                            GlobalVariables.ObserbacionPlan= respts[0];
                            GlobalVariables.ObserbacionSubdetalle= respts[0];

                            obs_cabecera cabecera = (obs_cabecera) pageAdapter.getItem(0);
                            cabecera.updateCodigo(respts[0]);

                            //update value Detalle
                            if(respts[1].equals("-1")){
                                Actives.add(-1);
                                Errores+="Ocurrio un error al guardar detalle";
                            }
                            // update Planes de accion
                            boolean passPlan=false;
                            if(!respts[2].equals("0"))
                            {
                                for(PlanModel item:GlobalVariables.Planes){
                                    for (String planid:respts[2].split(",")) {
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
                                obs_planaccion fragment = (obs_planaccion) pageAdapter.getItem(4);
                                fragment.setdata();
                                GlobalVariables.StrPlanes=GlobalVariables.Planes;

                                if(passPlan) {
                                    Actives.add(-1);
                                    Errores+="Error al guardar algunos planes de accion";
                                }
                            }
                            //update file
                            if(!respts[3].equals("0"))
                            {
                                for (String file:respts[3].split(",")) {
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

                            //update Involucrados
                            if(respts[4].contains(":-1")){
                                Actives.add(-1);
                                Errores+="\nError al guardar algunas personas involucradas";
                            }
                            // update Atendidos
                            if(!respts[4].equals("0"))
                            {
                                ArrayList<EquipoModel> PerInvolucrados=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.ListResponsables:GlobalVariables.ListAtendidos;
                                ArrayList<EquipoModel> StrPerInvolucrados= new ArrayList<>();

                                for(EquipoModel item:PerInvolucrados){
                                    boolean pass=true;
                                    for (String equipoid:respts[4].split(","))
                                    {
                                        String[] value= equipoid.split(":");
                                        if(item.CodPersona.equals(value[0])){
                                            if(value[1].equals("-1")){
                                                item.Estado="E";
                                                pass=false;
                                            }
                                            else item.NroReferencia=null;
                                            continue;
                                        }
                                    }
                                    if(pass){
                                        try {
                                            StrPerInvolucrados.add((EquipoModel)item.clone());
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                if(GlobalVariables.Obserbacion.CodTipo.equals("TO03"))GlobalVariables.StrResponsables=StrPerInvolucrados;
                                else GlobalVariables.StrAtendidos=StrPerInvolucrados;
                            }
                            //update Subdetalle
                            if(respts[5].contains(":-1")){
                                Actives.add(-1);
                                Errores+="\nError al guardar: ";
                            }
                            // update Subdetalle
                            if(!respts[5].equals("0"))
                            {
                                List<SubDetalleModel> SubDetalle=GlobalVariables.Obserbacion.CodTipo.equals("TO03")?GlobalVariables.SubDetalleTa:GlobalVariables.SubDetalleIS;
                                List<SubDetalleModel> StrSubDetalle=new ArrayList<>();
                                String ErrorSubDeta="";
                                for(SubDetalleModel item:SubDetalle){
                                    boolean pass=true;
                                    for (String equipoid:respts[5].split(","))
                                    {
                                        String[] value= equipoid.split(":");
                                        if(item.Codigo.equals(value[0])){
                                            if(value[1].equals("-1")){
                                                item.Estado="E";
                                                String DescTipo= GlobalVariables.getDescripcion(GlobalVariables.SubDetalleTipoDesc,item.CodTipo);
                                                if(!ErrorSubDeta.contains(DescTipo))ErrorSubDeta+=DescTipo+", ";
                                                pass=false;
                                            }
                                            else item.Codigo=value[1];
                                            continue;
                                        }
                                    }
                                    if(pass){
                                        try {
                                            StrSubDetalle.add((SubDetalleModel)item.clone());
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                if(!ErrorSubDeta.equals(""))Errores+=ErrorSubDeta.substring(0,ErrorSubDeta.length()-2);

                                if(GlobalVariables.Obserbacion.CodTipo.equals("TO03"))GlobalVariables.StrSubDetalleTa=StrSubDetalle;
                                else GlobalVariables.StrSubDetalleIS=StrSubDetalle;
                            }
                        }
                    }else{
                        if(response.code()==401){
                            Utils.reloadTokenAuth(observacion_edit.this,observacion_edit.this);
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
        for(SubDetalleModel item: GlobalVariables.SubDetalleIS)
        {
            if(item.CodTipo.equals("CORE")) GlobalVariables.SubDetalleIS.remove(item);
        }
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
            obs_archivos fragment = (obs_archivos) pageAdapter.getItem(3);
            fragment.setdata();
        }
    }

    @Override
    public void success(String data, String Tipo) {
        if(Tipo.equals("1")){ //delete Planes
            if(data.contains("-1")){
                Actives.set(2,-1);
                Errores+="\nNo se pudo eliminar algunos planes de accion";
            }
            else {
                GlobalVariables.StrPlanes=GlobalVariables.Planes;
                Actives.set(2,1);
            }
            if(!Actives.contains(0)) FinishSave();
        }
        else if(Tipo.equals("2")){ //delete Files
            if(data.contains("false")){
                Actives.set(3,-1);
                Errores+="\nNo se pudo eliminar algunas imagenes/archivos";
            }
            else {
                Actives.set(3,1);
                ArrayList<GaleriaModel> temp= new ArrayList<>(GlobalVariables.StrFiles);

                for (GaleriaModel item : GlobalVariables.StrFiles) {
                    if(!StringUtils.isEmpty(item.Estado)&&item.Estado.equals("E"))
                        temp.remove(item);
                }
                GlobalVariables.StrFiles=temp;
            }
            if(!Actives.contains(0)) FinishSave();
        }
        else { // tipo reloadToken
            enableSave=(true);
            SalvarObservacion(null);
        }
    }

    @Override
    public void successpost(String data, String Tipo) {

        Gson gson = new Gson();
        if(Tipo.equals("1")){
            if(data.contains("-1"))  {
                Actives.set(0,-1);
                Errores+="Error al guardar cabecera\n";
            }
            else{
                GlobalVariables.StrObservacion=gson.toJson(GlobalVariables.Obserbacion);
                Actives.set(0,1);
            }
            if(!Actives.contains(0)) FinishSave();
        }
        else if(Tipo.equals("2")){
            if(data.contains("-1")) {
                Actives.set(1,-1);
                Errores+="Error al guardar detalle\n";
            }
            else{
                GlobalVariables.StrObsDetalle=gson.toJson(GlobalVariables.ObserbacionDetalle);
                Actives.set(1,1);
            }
            if(!Actives.contains(0)) FinishSave();
        }
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
        btncancelar.setVisibility(View.GONE);
        progressBar.setProgress(100);
        txt_percent.setText("100%");
    }

    public void cancelUpload(View view) {
        if(request!=null){
            request.cancel();
            ll_bar_carga.setVisibility(View.GONE); enableSave=true;
            cancel=true;
        }
    }


    public void UpdateFiles (){
        GridViewAdapter gridViewAdapter = new GridViewAdapter(this,GlobalVariables.listaGaleria);
        gridViewAdapter.ProcesarImagens();
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
            if(!Actives.contains(0)){ // no hubo ningun gambio
                Actives.clear();
                enableSave=(true);
                Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
            }
        }
        else{
//Delete Files
            if(!DeleteFiles.equals("")){
                Actives.add(0);
                String url= GlobalVariables.Url_base+"media/deleteAll/"+DeleteFiles.substring(0,DeleteFiles.length()-1);
                ActivityController obj = new ActivityController("get", url, observacion_edit.this,this);
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
                //Toast.makeText(this, "Subiendo Archivos, Espere..." , Toast.LENGTH_SHORT).show();

                request = service.uploadAllFile("Bearer "+GlobalVariables.token_auth,createPartFromString(CodObservacion),createPartFromString("TOBS"),createPartFromString("1"),Files);
                ll_bar_carga.setVisibility(View.VISIBLE);

                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if(response.isSuccessful()){
                            String respt  = response.body();
                            if(respt.contains("-1")){
                                Actives.set(4,-1);
                                Errores+="\nOcurrio un error al subir algunos archivos";
                            }
                            else  Actives.set(4,1);
                            Utils.DeleteCache(new Compressor(observacion_edit.this).destinationDirectoryPath); //delete cache Files;
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
                            Actives.set(4,-1);
                            Errores+="\nOcurrio un error interno de servidor";
                        }
                        if(!Actives.contains(0)) FinishSave();
                        ll_bar_carga.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Actives.set(4,-1);
                        Errores+="\nFallo la subida de archivos";
                        if(!Actives.contains(0)) FinishSave();
                        ll_bar_carga.setVisibility(View.GONE);

                    }
                });
            }
            else  Actives.add(1);
        }
    }

}