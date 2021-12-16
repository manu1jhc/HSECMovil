package com.pango.hsec.hsec.Inspecciones;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.pango.hsec.hsec.Observaciones.FragmentGaleria;
import com.pango.hsec.hsec.Observaciones.FragmentPlan;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;

import java.util.ArrayList;
import java.util.List;

public class ActObsInspDet extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codObs, correlativo;
    String urlObs;
    int pos=0,proviene;
    HorizontalScrollView horizontalscroll;

   /* public static String jsonObsdet="";
    public static String jsonGaleriaDet="";
    public static String jsonPlandet="";
    public static boolean ismuro=false;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_insp_detalle);


        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //GlobalVariables loaddata = new GlobalVariables();
        //loaddata.LoadData();
        horizontalscroll=findViewById(R.id.horizontalscroll);
        Bundle datos = this.getIntent().getExtras();
        codObs=datos.getString("codObs");
        //pos=datos.getInt("posTab");
        correlativo=datos.getString("correlativo");
        //urlObs=datos.getString("UrlObs");
        //GlobalVariables.CodObs=codObs;

        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(ActObsInspDet.this);


    }



    public void close(View view){

        finish();
    }



    // Method to add a TabHost
    private static void AddTab(ActObsInspDet activity, TabHost tabHost,
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

        if(pos==3){
            //pos = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(3);
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




        // TODO Put here your Fragments
        FragmentObsdet f1 = FragmentObsdet.newInstance(correlativo);
        FragmentGaleria f2=FragmentGaleria.newInstance(codObs);
        //FragmentGaleriaDet f2 = FragmentGaleriaDet.newInstance(codObs);

        FragmentPlan f3 = FragmentPlan.newInstance(codObs,2);
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
        ActObsInspDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab1").setIndicator("OBSERVACIONES"));
        ActObsInspDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab2").setIndicator("GALERÍA"));
        ActObsInspDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab3").setIndicator("PLAN DE ACCIÓN"));
        /*
        ActObsInspDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab4").setIndicator("Comentarios"));*/
        mTabHost.setOnTabChangedListener(this);

    }







}
