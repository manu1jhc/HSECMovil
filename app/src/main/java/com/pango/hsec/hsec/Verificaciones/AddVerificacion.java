package com.pango.hsec.hsec.Verificaciones;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.util.ProgressRequestBody;

import java.util.ArrayList;
import java.util.List;

public class AddVerificacion extends FragmentActivity implements IActivity, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ProgressRequestBody.UploadCallbacks {
    MyPageAdapter pageAdapter;
    ImageButton close;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    TextView tx_titulo;
    String codObs="";
    int pos=0;
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



        tx_titulo=findViewById(R.id.tx_titulo);
        if(GlobalVariables.ObjectEditable){
            tx_titulo.setText("Editar Verificación");
        }else{
            tx_titulo.setText("Nueva Verificación");
        }

        initialiseTabHost();


        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(AddVerificacion.this);

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
//        FragmentAddParticipante f2 = FragmentAddParticipante.newInstance(codObs);
//        FragmentAddObservacion f3 = FragmentAddObservacion.newInstance(codObs);
//
        fList.add(f1);
//        fList.add(f2);
//        fList.add(f3);

        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        AddVerificacion.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("Verificación"));
        //AddVerificacion.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("Detalle"));


//        AddVerificacion.AddTab(this, this.mTabHost,
//                this.mTabHost.newTabSpec("Tab3").setIndicator("Observaciones"));
        mTabHost.setOnTabChangedListener(this);

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

    @Override
    public void onProgressUpdate(long percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }


    public void close(View view){
//        outInspeccion();
    finish();

    }

    public void SaveVerificacion(View view) {


    }

    public void cancelUpload(View view) {
//        if(request!=null){
//            request.cancel();
//            ll_bar_carga.setVisibility(View.GONE); enableSave=true;
//            cancel=true;
//        }
    }








    }
