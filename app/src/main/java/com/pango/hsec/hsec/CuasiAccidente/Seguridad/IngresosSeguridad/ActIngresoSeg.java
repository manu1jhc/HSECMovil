package com.pango.hsec.hsec.CuasiAccidente.Seguridad.IngresosSeguridad;

import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
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

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.EquipoModel;
import com.pango.hsec.hsec.model.ObsInspAddModel;
import com.pango.hsec.hsec.obs_archivos;
import com.pango.hsec.hsec.obs_planaccion;
import com.pango.hsec.hsec.util.ProgressRequestBody;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ActIngresoSeg extends FragmentActivity implements IActivity, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ProgressRequestBody.UploadCallbacks {
    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codObs="";
    String urlObs="";
    int pos=0;
    HorizontalScrollView horizontalscroll;
    Button btn_Salvar;
    ArrayList<Integer> Actives=new ArrayList();
    String CodMACuasi,Errores="";
    TextView tx_titulo;
    ActivityController activityTask;

    Call<String> request;
    ConstraintLayout ll_bar_carga;
    ProgressBar progressBar;
    ImageButton btncancelar;
    TextView txt_percent;
    Boolean cancel, enableSave=true;
    long L,G,T;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ingreso_seg);
        reiniciadata();

        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        horizontalscroll=findViewById(R.id.horizontalscroll);
        btn_Salvar=findViewById(R.id.btnguardar);

        progressBar = findViewById(R.id.progressBar2);
        ll_bar_carga=findViewById(R.id.ll_bar_carga);
        ll_bar_carga.setVisibility(View.GONE);
        btncancelar= (ImageButton)findViewById(R.id.cancel_upload);
        txt_percent= (TextView)findViewById(R.id.txt_percent);

        Bundle datos = this.getIntent().getExtras();
        codObs=datos.getString("codObs");
//        GlobalVariables.ListResponsables=new ArrayList<>();
//        GlobalVariables.ListAtendidos=new ArrayList<>();
//        GlobalVariables.ListobsInspAddModel=new ArrayList<>();
//        GlobalVariables.countObsInsp=1;
        tx_titulo=findViewById(R.id.tx_titulo);
        if(GlobalVariables.ObjectEditable){
            tx_titulo.setText("Editar Incidente-Seguridad");
        }else{
            tx_titulo.setText("Nueva Incidente-Seguridad");
        }

        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(ActIngresoSeg.this);
    }

    public void reiniciadata(){
//        GlobalVariables.AddInspeccion=new InspeccionModel(); //cabecera
//        GlobalVariables.StrInspeccion=null;
//
//        GlobalVariables.ListResponsables=new ArrayList<>();
//        GlobalVariables.StrResponsables=new ArrayList<>();
//
//        GlobalVariables.ListAtendidos=new ArrayList<>();
//        GlobalVariables.StrAtendidos=new ArrayList<>();
//
//        GlobalVariables.ListobsInspAddModel=new ArrayList<>();
//        GlobalVariables.StrtobsInspAddModel=new ArrayList<>();
//        GlobalVariables.InspeccionObserbacion=null;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //  outIncSeguridad();
    }

    public void outIncSeguridad(){
        boolean Nochangues=true;
        Gson gson = new Gson();
        Utils.closeSoftKeyBoard(this);
        // String Observacion=  gson.toJson(GlobalVariables.Obserbacion);
//        String IncSeg=  gson.toJson(GlobalVariables.AddInspeccion);

//        if(!GlobalVariables.StrInspeccion.equals(IncSeg)) Nochangues=false;
//        if(Nochangues&&!GlobalVariables.ObjectEditable&&GlobalVariables.ListResponsables.size()>0) Nochangues=false;
//        else if(GlobalVariables.ObjectEditable&&GlobalVariables.ListResponsables.size()>0){
//            // get Lider
//            String LiderPer="",LiderOld="";
//            for (EquipoModel item:GlobalVariables.StrResponsables) {
//                if(item.Lider.equals("1")) LiderOld=item.CodPersona;
//            }
//            for (EquipoModel item:GlobalVariables.ListResponsables) {
//                if(item.Lider.equals("1")) LiderPer=item.CodPersona;
//            }
//
//            ArrayList<EquipoModel> updateResponsables = new ArrayList<>();
//            //Insert equipo
//            for (EquipoModel item:GlobalVariables.ListResponsables) {
//                boolean pass=false;
//                for(EquipoModel item2:GlobalVariables.StrResponsables)
//                    if(item.CodPersona.equals(item2.CodPersona))
//                        pass=true;
//                if(!StringUtils.isEmpty(item.NroReferencia)&&item.NroReferencia.equals("-1")) {
//                    updateResponsables.add(new EquipoModel(item.CodPersona,"A"));
//                    if(!pass)GlobalVariables.StrResponsables.add(item);
//                }
//            }
//            if(updateResponsables.size()>0||!LiderPer.equals(LiderOld))Nochangues=false;
//        }
//        if(Nochangues&&GlobalVariables.StrResponsables.size()>0){
//            String DeletePlanes="";
//            for (EquipoModel item:GlobalVariables.StrResponsables) {
//                boolean pass=true;
//                for (EquipoModel item2:GlobalVariables.ListResponsables) {
//                    if(item.CodPersona.equals(item2.CodPersona)){
//                        pass=false;
//                        continue;
//                    }
//                }
//                if(pass){
//                    DeletePlanes+=item.CodPersona+";";
//                }
//            }
//            if(!DeletePlanes.equals(""))Nochangues=false;
//        }
//        if(Nochangues&&!GlobalVariables.ObjectEditable&&GlobalVariables.ListAtendidos.size()>0) Nochangues=false;
//        else if(GlobalVariables.ObjectEditable&&GlobalVariables.ListAtendidos.size()>0){
//            ArrayList<EquipoModel> updateAtentidos = new ArrayList<>();
//            //Insert Atentidos
//            for (EquipoModel item:GlobalVariables.ListAtendidos) {
//                boolean pass=false;
//                for(EquipoModel item2:GlobalVariables.StrAtendidos)
//                    if(item.CodPersona.equals(item2.CodPersona))
//                        pass=true;
//                if(!StringUtils.isEmpty(item.NroReferencia)&&item.NroReferencia.equals("-1")) {
//                    updateAtentidos.add(new EquipoModel(item.CodPersona,"A"));
//                    if(!pass)GlobalVariables.StrAtendidos.add(item);
//                }
//            }
//            if(updateAtentidos.size()>0)Nochangues=false;
//        }
//        if(Nochangues&&GlobalVariables.StrAtendidos.size()>0){
//            String DeletePlanes="";
//            for (EquipoModel item:GlobalVariables.StrAtendidos) {
//                boolean pass=true;
//                for (EquipoModel item2:GlobalVariables.ListAtendidos) {
//                    if(item.CodPersona.equals(item2.CodPersona)){
//                        pass=false;
//                        continue;
//                    }
//                }
//                if(pass){
//                    DeletePlanes+=item.CodPersona+";";
//                }
//            }
//            if(!DeletePlanes.equals(""))Nochangues=false;
//        }
//        if(Nochangues) {
//            if (!GlobalVariables.ObjectEditable&&GlobalVariables.ListobsInspAddModel.size() > 0) Nochangues=false;
//            else if (GlobalVariables.StrtobsInspAddModel.size() > 0) {
//                String DeleteObservaciones = "";
//                for (ObsInspAddModel item : GlobalVariables.StrtobsInspAddModel) {
//                    boolean pass = true;
//                    for (ObsInspAddModel item2 : GlobalVariables.ListobsInspAddModel) {
//                        if (item.obsInspDetModel.Correlativo.equals(item2.obsInspDetModel.Correlativo)) {
//                            pass = false;
//                            continue;
//                        }
//                    }
//                    if (pass) {
//                        DeleteObservaciones += item.obsInspDetModel.Correlativo + ";";
//                    }
//                }
//                if (!DeleteObservaciones.equals("")) Nochangues = false;
//            }
//        }
//        if(!Nochangues)
//        {
//            String Mensaje="Esta seguro de salir sin guardar cambios?\n";
//            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//            //alertDialog.setCancelable(false);
//            alertDialog.setTitle("Datos sin guardar");
//            alertDialog.setIcon(R.drawable.warninicon);
//            alertDialog.setMessage(Mensaje);
//
//            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });
//            alertDialog.show();
//        }
//        else
            finish();

    }

    public boolean ValifarFormulario(View view){
        return true;
    }
    public void SaveIncSeguridad(View view){

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        // TODO Put here your Fragments
        FragmentAddCabeceraSeg f1 = FragmentAddCabeceraSeg.newInstance(codObs);
        FragmentAddDetalleSeg f2 = FragmentAddDetalleSeg.newInstance(codObs);
        FragmentAddCausalidad f3 = FragmentAddCausalidad.newInstance(codObs);
        obs_archivos f4=obs_archivos.newInstance(codObs);
        obs_planaccion f5 = obs_planaccion.newInstance(codObs);

        fList.add(f1);
        fList.add(f2);
        fList.add(f3);
        fList.add(f4);
        fList.add(f5);

        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        ActIngresoSeg.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab1").setIndicator("SEGURIDAD"));
        ActIngresoSeg.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab2").setIndicator("DETALLE"));
        ActIngresoSeg.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab3").setIndicator("Causalidad"));
        ActIngresoSeg.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab4").setIndicator("GALERIA"));
        ActIngresoSeg.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab5").setIndicator("PLAN DE ACCIÓN"));

        mTabHost.setOnTabChangedListener(this);

    }






    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void error(String mensaje, String Tipo) {

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

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void close(View view){
        outIncSeguridad();
    }
    private static void AddTab(ActIngresoSeg activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String s) {
        pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
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









}