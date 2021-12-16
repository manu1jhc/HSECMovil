package com.pango.hsec.hsec.Inspecciones;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
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
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.controller.GetTokenController;
import com.pango.hsec.hsec.model.UsuarioModel;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActInspeccionDet extends FragmentActivity implements IActivity, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{

    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codObs,codpersona;
    String urlObs;
    int pos,proviene;
    Gson gson;
    HorizontalScrollView horizontalscroll;

/*
    public static String jsonInspeccion="";
    public static String jsonEquipo="";
    public static String jsonParticipante="";

    public static String jsonObsIns="";
    public static String jsonComentario="";

*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_inspeccion_det);

        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        //GlobalVariables loaddata = new GlobalVariables();
        //loaddata.LoadData();
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
                        Intent intent = new Intent(ActInspeccionDet.this,Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    UsuarioModel UserLoged= gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
                    if(!codpersona.equals(UserLoged.CodPersona)) {
                        Toast.makeText(this, "Usuario logueado no coincide con usuario a notificar",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActInspeccionDet.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else loadData();
                }
            }
        }


        //urlObs=datos.getString("UrlObs");
        //GlobalVariables.CodObs=codObs;






    }

    public void loadData(){

        initialiseTabHost();
        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(ActInspeccionDet.this);
    }

    public void close(View view){
        finish();
    }



    // Method to add a TabHost
    private static void AddTab(ActInspeccionDet activity, TabHost tabHost,
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
        FragmentInspeccion f1 = FragmentInspeccion.newInstance(codObs);
        FragmentParticipante f2 = FragmentParticipante.newInstance(codObs);
        FragmentObsInsp f3 = FragmentObsInsp.newInstance(codObs);
        //MySampleFragment f4 = MySampleFragment.newInstance("Sample Fragment 4");
        FragmentComentIns f4=FragmentComentIns.newInstance(codObs);

        //ObsFragment f4 = ObsFragment.newInstance("","");

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
        ActInspeccionDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab1").setIndicator("Inspección"));
        ActInspeccionDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab2").setIndicator("Participantes"));
        ActInspeccionDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab3").setIndicator("Observaciones"));
        ActInspeccionDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab4").setIndicator("Comentarios"));
        mTabHost.setOnTabChangedListener(this);

    }

    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {
        data= data.substring(1,data.length()-1);
        if(data.length()>40){
            GlobalVariables.token_auth=data;
            GetTokenController objT = new GetTokenController("",ActInspeccionDet.this,null);
            objT.Obtener_dataUser();
            objT.LoadData();
            UsuarioModel UserLoged= gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
            if(!codpersona.equals(UserLoged.CodPersona)) {
                Toast.makeText(this, "Usuario logueado no coincide con usuario a notificar",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActInspeccionDet.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            else  loadData();
        }
    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
