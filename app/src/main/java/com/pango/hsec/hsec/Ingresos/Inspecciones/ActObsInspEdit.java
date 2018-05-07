package com.pango.hsec.hsec.Ingresos.Inspecciones;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.ObsInspAddModel;
import com.pango.hsec.hsec.model.ObsInspDetModel;
import com.pango.hsec.hsec.model.PlanModel;
import com.pango.hsec.hsec.obs_archivos;
import com.pango.hsec.hsec.obs_planaccion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActObsInspEdit extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codObs, correlativo;
    String urlObs;
    String json_osbinsp;
    public static boolean editar;
    int pos=0;
    HorizontalScrollView horizontalscroll;
    Button btnguardar_obs;
    //static ObsInspDetModel obsInspDetModel=new ObsInspDetModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_obs_det_edit);
        btnguardar_obs=findViewById(R.id.btnguardar_obs);
        btnguardar_obs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int contpos=GlobalVariables.countObsInsp-2;
                if(editar){
                    GlobalVariables.countObsInsp=Integer.parseInt(GlobalVariables.obsInspDetModel.NroDetInspeccion);
                }else{
                    GlobalVariables.countObsInsp=GlobalVariables.countObsInsp+1;

                }

                GlobalVariables.editar_list=false;
                Toast.makeText(ActObsInspEdit.this, "code: "+GlobalVariables.countObsInsp, Toast.LENGTH_LONG).show();
///objeto obsInspAddModel
                GlobalVariables.obsInspAddModel=new ObsInspAddModel();
                GlobalVariables.obsInspAddModel.obsInspDetModel=GlobalVariables.obsInspDetModel;
                GlobalVariables.obsInspAddModel.listaGaleria=GlobalVariables.listaGaleria;
                GlobalVariables.obsInspAddModel.listaArchivos=GlobalVariables.listaArchivos;
                GlobalVariables.obsInspAddModel.Planes=GlobalVariables.Planes;



                //GlobalVariables.obsInspDetModel,
                //GlobalVariables.listaGaleria,GlobalVariables.listaArchivos,GlobalVariables.Planes);


                Gson gson = new Gson();
                json_osbinsp = gson.toJson(GlobalVariables.obsInspAddModel);



                Intent intent = getIntent();

                intent.putExtra("JsonObsInsp", json_osbinsp);


                /*
                intent.putExtra("codigo", "-1");
                intent.putExtra("Ninspeccion",obsInspDetModel.CodInspeccion);
                intent.putExtra("lugar",obsInspDetModel.Lugar);
                intent.putExtra("ub_especifica",obsInspDetModel.CodUbicacion);
                intent.putExtra("aspecto",obsInspDetModel.CodAspectoObs);
                intent.putExtra("actividad",obsInspDetModel.CodActividadRel);
                intent.putExtra("nivel",obsInspDetModel.CodNivelRiesgo);
                intent.putExtra("observacion",obsInspDetModel.Observacion);
                */
                setResult(RESULT_OK, intent);
                //obsInspDetModel=new ObsInspDetModel();
                finish();

            }
        });
        //GlobalVariables.countObsInsp=1;
        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //GlobalVariables loaddata = new GlobalVariables();
        //loaddata.LoadData();
        horizontalscroll=findViewById(R.id.horizontalscroll);
        Bundle datos = this.getIntent().getExtras();
        codObs=datos.getString("codObs");
        editar=datos.getBoolean("Editar");
        GlobalVariables.editar_list=editar;
        //pos=datos.getInt("posTab");
        correlativo=datos.getString("correlativo");
        //urlObs=datos.getString("UrlObs");
        //GlobalVariables.CodObs=codObs;

        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(ActObsInspEdit.this);

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
        FragmentObsInsAdd f1 = FragmentObsInsAdd.newInstance(correlativo);
        obs_archivos f2=obs_archivos.newInstance(codObs,pos);
        //FragmentGaleriaDet f2 = FragmentGaleriaDet.newInstance(codObs);

        obs_planaccion f3 = obs_planaccion.newInstance(codObs);
        //FragmentPlandet f3 = FragmentPlandet.newInstance(codObs);
        //MySampleFragment f4 = MySampleFragment.newInstance("Sample Fragment 4");
        //FragmentComentIns f4=FragmentComentIns.newInstance(codObs);

        //ObsFragment f4 = ObsFragment.newInstance("","");

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








}
