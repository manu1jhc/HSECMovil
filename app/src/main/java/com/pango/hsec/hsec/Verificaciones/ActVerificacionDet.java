package com.pango.hsec.hsec.Verificaciones;

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
import com.pango.hsec.hsec.Observaciones.FragmentComent;
import com.pango.hsec.hsec.Observaciones.FragmentGaleria;
import com.pango.hsec.hsec.Observaciones.FragmentPlan;
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

//public class ActVerificacionDet extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_verificacion_det);
//    }
//    public void close(View view){
//        finish();
//    }
//}
public class ActVerificacionDet extends FragmentActivity implements IActivity, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codVer,tipoVer,codpersona;
    String urlVer;
    int pos;
    Gson gson;
    HorizontalScrollView horizontalsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_verificacion_det);
        gson = new Gson();
        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        horizontalsv=findViewById(R.id.horizontalsv);
//Bundle[{codObs=VER00000003, tipoObs=TV02, posTab=0}]
        GlobalVariables.jsonVerificaccion= "";
        Intent startingIntent = getIntent();
        if (startingIntent != null) {
            codVer = startingIntent.getStringExtra("codigo"); // Retrieve the id
            codpersona = startingIntent.getStringExtra("codpersona");
            tipoVer= startingIntent.getStringExtra("tipo");
            if(codVer==null && codpersona== null &&tipoVer==null){

                Bundle datos = this.getIntent().getExtras();
                codVer=datos.getString("codVer");
                pos=datos.getInt("posTab");
                tipoVer = datos.getString("tipoVer");
                loadData();
            }
            else{
                GlobalVariables.NotCodigo=codVer;
                GlobalVariables.NotCodPersona=codpersona;
                GlobalVariables.NotTipo=tipoVer;
                pos=4;
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
                        Intent intent = new Intent(ActVerificacionDet.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    UsuarioModel UserLoged= gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
                    if(!codpersona.equals(UserLoged.CodPersona)) {
                        Toast.makeText(this, "Usuario logueado no coincide con usuario a notificar",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActVerificacionDet.this, MainActivity.class);
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
        mViewPager.setOnPageChangeListener(ActVerificacionDet.this);
    }

    public void close(View view){
        finish();
    }



    // Method to add a TabHost
    private static void AddTab(ActVerificacionDet activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {


        pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);


/*
        for (int i = 0; i < tablist.length; i++) {
            if (tabId.contentEquals(tablist[i])) {
                HorizontalScrollView scroll = (HorizontalScrollView) findViewById(R.id.horScrollView);
                scroll.smoothScrollTo(((int) (getResources().getDimensionPixelSize(R.dimen.tab_width) * (i))), 0);
            }
        }
        */
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
            horizontalsv.smoothScrollTo(((int) (getResources().getDimensionPixelSize(R.dimen.tab_width) * (pos))), 0);

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

        FragmentCabVerificacion f1 = FragmentCabVerificacion.newInstance(codVer);
        FragmentVerDet f2 = FragmentVerDet.newInstance(codVer);
        FragmentGaleria f3=FragmentGaleria.newInstance(codVer);
        FragmentPlan f4 = FragmentPlan.newInstance(codVer,3);
        FragmentComent f5=FragmentComent.newInstance(codVer);

        fList.add(f1);
        fList.add(f2);
        fList.add(f3);
        fList.add(f4);
        fList.add(f5);


//        FragmentObs f1 = FragmentObs.newInstance(codObs);
//
//        FragmentGaleria f3 = FragmentGaleria.newInstance(codObs);
//        FragmentPlan f4 = FragmentPlan.newInstance(codObs,1);//cambiar a codObs
//        //MySampleFragment f4 = MySampleFragment.newInstance("Sample Fragment 4");
//        FragmentComent f5=FragmentComent.newInstance(codObs);
//
//        //ObsFragment f4 = ObsFragment.newInstance("","");
//
//        if(tipoObs.equals("TO01")){
//            FragmentObsCom f2 = FragmentObsCom.newInstance(codObs);
//            fList.add(f1);
//            fList.add(f2);
//            fList.add(f3);
//            fList.add(f4);
//            fList.add(f5);
//        }else if(tipoObs.equals("TO02")){
//            FragmentObsCond f2 = FragmentObsCond.newInstance(codObs);
//            fList.add(f1);
//            fList.add(f2);
//            fList.add(f3);
//            fList.add(f4);
//            fList.add(f5);
//        }else if(tipoObs.equals("TO03")){
//            FragmentObsTar f2 = FragmentObsTar.newInstance(codObs);
//            fList.add(f1);
//            fList.add(f2);
//            fList.add(f3);
//            fList.add(f4);
//            fList.add(f5);
//        }else {
//            FragmentObsIS f2 = FragmentObsIS.newInstance(codObs);
//            fList.add(f1);
//            fList.add(f2);
//            fList.add(f3);
//            fList.add(f4);
//            fList.add(f5);
//        }
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
        ActVerificacionDet.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("Verificación"));
        ActVerificacionDet.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("Detalle"));
        ActVerificacionDet.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("Galeria"));
        ActVerificacionDet.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab4").setIndicator("Plan de Acción"));

        ActVerificacionDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab5").setIndicator("Comentarios"));
        mTabHost.setOnTabChangedListener(this);
//        ActMuroDet.AddTab(this, this.mTabHost,
//                this.mTabHost.newTabSpec("Tab2").setIndicator("Detalle"));
//        ActMuroDet.AddTab(this, this.mTabHost,
//                this.mTabHost.newTabSpec("Tab3").setIndicator("Galería"));
//        ActMuroDet.AddTab(this, this.mTabHost,
//                this.mTabHost.newTabSpec("Tab4").setIndicator("Planes de Acción"));
//        mTabHost.setOnTabChangedListener(this);
//        ActMuroDet.AddTab(this, this.mTabHost,
//                this.mTabHost.newTabSpec("Tab5").setIndicator("Comentarios"));
//        mTabHost.setOnTabChangedListener(this);


    }

    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {
        data= data.substring(1,data.length()-1);
        if(data.length()>40){
            GlobalVariables.token_auth=data;
            GetTokenController objT = new GetTokenController("",ActVerificacionDet.this,null);
            objT.Obtener_dataUser();
            objT.LoadData();
            UsuarioModel UserLoged= gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
            if(!codpersona.equals(UserLoged.CodPersona)) {
                Toast.makeText(this, "Usuario logueado no coincide con usuario a notificar",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActVerificacionDet.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            else  loadData();
        }
    }

    @Override
    public void error(String mensaje, String Tipo) {

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
