package com.pango.hsec.hsec.Observaciones;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TabHost;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;

import java.util.ArrayList;
import java.util.List;

public class ActMuroDet extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codObs,tipoObs;
    String urlObs;
    int pos;
    HorizontalScrollView horizontalsv;

    //public static ObservacionModel observacionData=new ObservacionModel();
    /*public static String jsonObservacion="";
    public static String jsonGaleria="";
    public static String jsonPlan="";
    public static String jsonComentario="";
    public static boolean ismuro=true;
*/
    //TabHost tabHost;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_muro_det);

        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        horizontalsv=findViewById(R.id.horizontalsv);
        Bundle datos = this.getIntent().getExtras();
        codObs=datos.getString("codObs");
        pos=datos.getInt("posTab");
        tipoObs= datos.getString("tipoObs");
        //urlObs=datos.getString("UrlObs");
        //GlobalVariables.CodObs=codObs;

        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(ActMuroDet.this);


    }
    public void close(View view){
        finish();
    }



    // Method to add a TabHost
    private static void AddTab(ActMuroDet activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {


        pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);

       // horizontalsv.scrollTo(pos, 0);
      //  horizontalsv.refreshDrawableState();

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        //horizontalsv.scrollTo(5, 0);
        //horizontalsv.setSelected(true);
    }

    // Manages the Page changes, synchronizing it with Tabs
    //numero de tab que quieres mostrar


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if(pos==4){
            //pos = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(4);
            pos=0;
        }else{
        pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
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

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        //codObs="OBS00210123";
/*
		fList.add(Fragment.instantiate(this, ObsFragment.class.getName()));
		fList.add(Fragment.instantiate(this, GaleriaFragment.class.getName()));
		fList.add(Fragment.instantiate(this, PlanFragment.class.getName()));
		fList.add(Fragment.instantiate(this, ComentarioFragment.class.getName()));
		*/

        // TODO Put here your Fragments
        //codObs




        FragmentObs f1 = FragmentObs.newInstance(codObs);

        FragmentGaleria f3 = FragmentGaleria.newInstance(codObs);
        FragmentPlan f4 = FragmentPlan.newInstance(codObs);//cambiar a codObs
        //MySampleFragment f4 = MySampleFragment.newInstance("Sample Fragment 4");
        FragmentComent f5=FragmentComent.newInstance(codObs);

        //ObsFragment f4 = ObsFragment.newInstance("","");

        if(tipoObs.equals("TO01")){
            FragmentObsCom f2 = FragmentObsCom.newInstance(codObs);
            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
            fList.add(f4);
            fList.add(f5);
        }else if(tipoObs.equals("TO02")){
            FragmentObsCond f2 = FragmentObsCond.newInstance(codObs);
            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
            fList.add(f4);
            fList.add(f5);
        }else if(tipoObs.equals("TO03")){
            FragmentObsTar f2 = FragmentObsTar.newInstance(codObs);
            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
            fList.add(f4);
            fList.add(f5);
        }else {
            FragmentObsIS f2 = FragmentObsIS.newInstance(codObs);
            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
            fList.add(f4);
            fList.add(f5);
        }

/*
        FragmentObsCond f2 = FragmentObsCond.newInstance(codObs);

        fList.add(f1);
        fList.add(f2);
        fList.add(f3);
        fList.add(f4);
        fList.add(f5);
*/

        return fList;
    }





    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        ActMuroDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab1").setIndicator("Observación"));
        ActMuroDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab2").setIndicator("Detalle"));
        ActMuroDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab3").setIndicator("Galería"));
        ActMuroDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab4").setIndicator("Planes de Acción"));
        mTabHost.setOnTabChangedListener(this);
        ActMuroDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab5").setIndicator("Comentarios"));
        mTabHost.setOnTabChangedListener(this);

    }

/*
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //check if popupwindow is open
        //Log.e(TAG, "Check if it runs through this section");
        if (popupWindow != null) {

            popupWindow.dismiss();
            popupWindow = null;
            //myWebView = null;

        }else{
            super.onBackPressed();
        }
    }
*/



}
