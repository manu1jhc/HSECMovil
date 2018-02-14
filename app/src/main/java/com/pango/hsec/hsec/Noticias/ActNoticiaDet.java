package com.pango.hsec.hsec.Noticias;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.pango.hsec.hsec.Inspecciones.FragmentInspeccion;
import com.pango.hsec.hsec.Inspecciones.FragmentObsInsp;
import com.pango.hsec.hsec.Inspecciones.FragmentParticipante;
import com.pango.hsec.hsec.Observaciones.FragmentComent;
import com.pango.hsec.hsec.Observaciones.FragmentGaleria;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;

import java.util.ArrayList;
import java.util.List;

public class ActNoticiaDet extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codNot;
    int pos;
    HorizontalScrollView horizontalscroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_noticia_det);

        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //GlobalVariables loaddata = new GlobalVariables();
        //loaddata.LoadData();
        horizontalscroll=findViewById(R.id.horizontalscroll);
        Bundle datos = this.getIntent().getExtras();
        codNot=datos.getString("codObs");
        pos=datos.getInt("posTab");
        //urlObs=datos.getString("UrlObs");
        //GlobalVariables.CodObs=codObs;



        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(ActNoticiaDet.this);




    }

    public void close(View view){
        finish();
    }



    // Method to add a TabHost
    private static void AddTab(ActNoticiaDet activity, TabHost tabHost,
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

        // TODO Put here your Fragments
        FragmentDetalle f1 = FragmentDetalle.newInstance(codNot);
        FragmentGaleria f2 = FragmentGaleria.newInstance(codNot);
        FragmentComent f3 = FragmentComent.newInstance(codNot);

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
        ActNoticiaDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab1").setIndicator("Detalle"));
        ActNoticiaDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab2").setIndicator("Archivos"));
        ActNoticiaDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab3").setIndicator("Comentarios"));
       /*
        ActInspeccionDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab4").setIndicator("Comentarios"));
        */
        mTabHost.setOnTabChangedListener(this);

    }



}
