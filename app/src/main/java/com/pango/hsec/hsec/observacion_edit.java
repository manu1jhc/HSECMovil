
package com.pango.hsec.hsec;

        import android.content.Intent;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.view.ViewPager;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.HorizontalScrollView;
        import android.widget.ImageButton;
        import android.widget.TabHost;
        import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
        import com.pango.hsec.hsec.Observaciones.MyTabFactory;

        import java.util.ArrayList;
        import java.util.List;

public class observacion_edit extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    HorizontalScrollView horizontalsv;
    String CodObservacion;
    int pos;
    ;
    //TabHost tabHost;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacion_edit);
        close=findViewById(R.id.imageButton);
        horizontalsv=findViewById(R.id.HorizontalObsedit);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        Bundle datos = this.getIntent().getExtras();
        CodObservacion="OBS000000XYZ";
        CodObservacion=datos.getString("codObs");
        pos=datos.getInt("posTab");
        //if(GlobalVariables.ObjectEditable) CodObservacion=datos.getString("Observacion");


        GlobalVariables.activity= this;
        GlobalVariables.loadObs_Detalles();
        GlobalVariables.LoadData();

        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);

        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(observacion_edit.this);

    }
    public void close(View view){
        finish();
    }

    // Method to add a TabHost
    private static void AddTab(observacion_edit activity, TabHost tabHost,TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
        View dView=this.mViewPager.getRootView();
        if(pos==1){ //cambios solo cuando ingresemos al tab de detalle de obs.
            if(GlobalVariables.Obserbacion.CodTipo=="TO01"){
                dView.findViewById(R.id.id_Condicion).setVisibility(View.GONE);
                dView.findViewById(R.id.id_Acto).setVisibility(View.VISIBLE);
                dView.findViewById(R.id.id_Estado).setVisibility(View.VISIBLE);
                dView.findViewById(R.id.id_Error).setVisibility(View.VISIBLE);
            }
            else{
                dView.findViewById(R.id.id_Condicion).setVisibility(View.VISIBLE);
                dView.findViewById(R.id.id_Acto).setVisibility(View.GONE);
                dView.findViewById(R.id.id_Estado).setVisibility(View.GONE);
                dView.findViewById(R.id.id_Error).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // Manages the Page changes, synchronizing it with Tabs
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
     /*   int pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
*/
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

        // TODO Put here your Fragments
        obs_cabecera f1 = obs_cabecera.newInstance(CodObservacion);
        obs_detalle1 f2 = obs_detalle1.newInstance(CodObservacion);
        obs_archivos f4 = obs_archivos.newInstance(CodObservacion,pos);
        obs_planaccion f5=obs_planaccion.newInstance(CodObservacion);


        fList.add(f1);
        fList.add(f2);
        fList.add(f4);
        fList.add(f5);

        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab1").setIndicator("Observación"));
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab2").setIndicator("Detalle"));
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab3").setIndicator("Archivos"));
        observacion_edit.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab4").setIndicator("Plan Accion"));
        mTabHost.setOnTabChangedListener(this);
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this,"Su data fue actualizada "+ requestCode ,Toast.LENGTH_SHORT).show();
    }*/
}
