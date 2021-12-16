package com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.DetalleMACuasi;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Login;
import com.pango.hsec.hsec.MainActivity;
import com.pango.hsec.hsec.Observaciones.FragmentComent;
import com.pango.hsec.hsec.Observaciones.FragmentGaleria;
import com.pango.hsec.hsec.Observaciones.FragmentPlan;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.UsuarioModel;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MACuasiDetalle extends FragmentActivity implements IActivity, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{

    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codObs,codpersona;
    String urlObs;
    int pos,proviene;
    Gson gson;
    HorizontalScrollView horizontalscroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_cuasi_detalle);

        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        //GlobalVariables loaddata = new GlobalVariables();
        horizontalscroll=findViewById(R.id.horizontalscroll);

        gson=  new Gson();
        Intent startingIntent = getIntent();
        if (startingIntent != null) {
            codObs = startingIntent.getStringExtra("codigo"); // Retrieve the id
            codpersona = startingIntent.getStringExtra("codpersona");
            if(codObs==null && codpersona== null){

                Bundle datos = this.getIntent().getExtras();
                codObs=datos.getString("codObs");
                pos=datos.getInt("posTab");
                loadData();
            }
            else{
                GlobalVariables.NotCodigo=codObs;
                GlobalVariables.NotCodPersona=codpersona;
                GlobalVariables.NotTipo="";
                pos=3;
                if(StringUtils.isEmpty(GlobalVariables.token_auth)){ // open app in OBF
                    if(Utils.obtener_status(this)){

                        String url_token=GlobalVariables.Url_base+"membership/authenticate";//?"+"username="+user+"&password="+pass+"&domain="+dom;
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.accumulate("username",Utils.obtener_usuario(this));
                            jsonObject.accumulate("password",Utils.obtener_pass(this));
                            jsonObject.accumulate("domain","anyaccess");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final ActivityController obj1 = new ActivityController("post-2", url_token, this,this);
                        obj1.execute(jsonObject.toString());
                        return;
                    }
                    else {
                        GlobalVariables.pasnotification=true;
                        Intent intent = new Intent(MACuasiDetalle.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    UsuarioModel UserLoged= gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
                    if(!codpersona.equals(UserLoged.CodPersona)) {
                        Toast.makeText(this, "Usuario logueado no coincide con usuario a notificar",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MACuasiDetalle.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else loadData();
                }
            }
        }
    }

    public void loadData(){

        initialiseTabHost();
        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(MACuasiDetalle.this);
    }


    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        MACuasiDetalle.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab1").setIndicator("INCIDENTE-MA"));
        MACuasiDetalle.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab2").setIndicator("DETALLE"));
        MACuasiDetalle.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab3").setIndicator("GALERIA"));
        MACuasiDetalle.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab4").setIndicator("PLANES DE ACCIÓN"));
        MACuasiDetalle.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab5").setIndicator("COMENTARIOS"));
        mTabHost.setOnTabChangedListener(this);

    }

    public void close(View view){
        finish();
    }

    // Method to add a TabHost
    private static void AddTab(MACuasiDetalle activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(pos==3){
            //pos = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(3);
            horizontalscroll.smoothScrollTo(((int) (getResources().getDimensionPixelSize(R.dimen.tab_width) * (pos))), 0);

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

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        // TODO Put here your Fragments
        FragmentMACuasiDetalle1 f1 = FragmentMACuasiDetalle1.newInstance(codObs);
        FragmentMACuasiDetalle2 f2 = FragmentMACuasiDetalle2.newInstance(codObs);
       // FragmentGaleriaMa f3 = FragmentGaleriaMa.newInstance(codObs);
        FragmentGaleria f3 = FragmentGaleria.newInstance(codObs);
        //FragmentPlanesMA f4 = FragmentPlanesMA.newInstance(codObs);
        FragmentPlan f4 = FragmentPlan.newInstance(codObs,1);
        //FragmentComentMA f5 = FragmentComentMA.newInstance(codObs);
        FragmentComent f5 = FragmentComent.newInstance(codObs);

        fList.add(f1);
        fList.add(f2);
        fList.add(f3);
        fList.add(f4);
        fList.add(f5);

        return fList;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
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
}