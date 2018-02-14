
package com.pango.hsec.hsec;

        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.view.ViewPager;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.HorizontalScrollView;
        import android.widget.ImageButton;
        import android.widget.TabHost;
        import android.widget.Toast;

        import com.pango.hsec.hsec.Observaciones.FragmentComent;
        import com.pango.hsec.hsec.Observaciones.FragmentGaleria;
        import com.pango.hsec.hsec.Observaciones.FragmentObs;
        import com.pango.hsec.hsec.Observaciones.FragmentPlan;
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
    //TabHost tabHost;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacion_edit);
        close=findViewById(R.id.imageButton);
        horizontalsv=findViewById(R.id.HorizontalObsedit);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        GlobalVariables loaddata = new GlobalVariables();
        loaddata.LoadData();
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
    private static void AddTab(observacion_edit activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
        View dView=this.mViewPager.getRootView();
        if(pos==1){ //cambios solo cuando ingresemos al tab de detalle de obs.
            if(GlobalVariables.TipoObservacion=="TO01"){
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
        int pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);

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

/*
		fList.add(Fragment.instantiate(this, ObsFragment.class.getName()));
		fList.add(Fragment.instantiate(this, GaleriaFragment.class.getName()));
		fList.add(Fragment.instantiate(this, PlanFragment.class.getName()));
		fList.add(Fragment.instantiate(this, ComentarioFragment.class.getName()));
		*/

        // TODO Put here your Fragments
        obs_cabecera f1 = obs_cabecera.newInstance("OBSXYZ000000");
        obs_detalle1 f2 = obs_detalle1.newInstance("Sample Fragment 1");
        obs_archivos f4 = obs_archivos.newInstance("Sample Fragment 2");
        obs_planaccion f5=obs_planaccion.newInstance("Sample Fragment 3");

        //ObsFragment f4 = ObsFragment.newInstance("","");

        fList.add(f1);
        fList.add(f2);
        fList.add(f4);
        fList.add(f5);

/*
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Tab1Fragment.class.getName()));
		fragments.add(Fragment.instantiate(this, Tab2Fragment.class.getName()));
		fragments.add(Fragment.instantiate(this, Tab3Fragment.class.getName()));

		this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
		//
		this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
*/
        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab1").setIndicator("Cabecera"));
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab2").setIndicator("Detalle"));
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab3").setIndicator("Archivos"));
        observacion_edit.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab4").setIndicator("Plan Accion"));
        mTabHost.setOnTabChangedListener(this);
    }
}
