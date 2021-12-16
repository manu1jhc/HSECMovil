package com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.IngresosMA;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.obs_archivos;
import com.pango.hsec.hsec.obs_planaccion;
import com.pango.hsec.hsec.util.ProgressRequestBody;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ActIngresoMA extends FragmentActivity implements IActivity, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ProgressRequestBody.UploadCallbacks {
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
        setContentView(R.layout.act_ingreso_ma);
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
            tx_titulo.setText("Editar Inc. Medio Ambiente");
        }else{
            tx_titulo.setText("Nueva Inc. Medio Ambiente");
        }

        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(ActIngresoMA.this);




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
      //  outInspeccion();
    }

    public void outIncidenteMA(){
        finish();
    }
    public boolean ValifarFormulario(View view){
        return true;
    }
    public void SaveMACuasiAccidente(View view){

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        // TODO Put here your Fragments
        FragmentAddCabeceraMA f1 = FragmentAddCabeceraMA.newInstance(codObs);
        FragmentAddDetalleMA f2 = FragmentAddDetalleMA.newInstance(codObs);
        obs_archivos f3=obs_archivos.newInstance(codObs);
        obs_planaccion f4 = obs_planaccion.newInstance(codObs);

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
        ActIngresoMA.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab1").setIndicator("SEGURIDAD"));
        ActIngresoMA.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab2").setIndicator("DETALLE"));
        ActIngresoMA.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab3").setIndicator("GALERIA"));
        ActIngresoMA.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab4").setIndicator("PLAN DE ACCIÓN"));

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
        outIncidenteMA();
    }
    private static void AddTab(ActIngresoMA activity, TabHost tabHost,
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