package com.pango.hsec.hsec;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.webkit.URLUtil;
import android.widget.AbsListView;
import android.widget.AdapterView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;
import com.pango.hsec.hsec.Facilito.obsFacilitoDet;
import com.pango.hsec.hsec.Facilito.report_obs;
import com.pango.hsec.hsec.Ingresos.Inspecciones.AddInspeccion;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Noticias.ActNoticiaDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.adapter.SearchAdapter;
import com.pango.hsec.hsec.adapter.SpinnerAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.PublicacionModel;
import com.pango.hsec.hsec.model.UsuarioModel;

import java.lang.reflect.Field;
import java.util.ArrayList;

import layout.FragmentCapacitaciones;
import layout.FragmentObsFacilito;
import layout.FragmentConfiguracion;
import layout.FragmentContactenos;
import layout.FragmentFichaPersonal;
import layout.FragmentInspecciones;
import layout.FragmentMuro;
import layout.FragmentObservaciones;
import layout.FragmentPlanPendiente;
import layout.FragmentAvanzado;
import layout.FragmentNoticias;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity
        implements
        IActivity,
        NavigationView.OnNavigationItemSelectedListener,
        FragmentMuro.OnFragmentInteractionListener,
        FragmentCapacitaciones.OnFragmentInteractionListener,
        FragmentFichaPersonal.OnFragmentInteractionListener,
        FragmentObservaciones.OnFragmentInteractionListener,
        FragmentInspecciones.OnFragmentInteractionListener,

        //FragmentRegistroIO.OnFragmentInteractionListener,
        FragmentAvanzado.OnFragmentInteractionListener,
        FragmentConfiguracion.OnFragmentInteractionListener,
        FragmentContactenos.OnFragmentInteractionListener,
        FragmentPlanPendiente.OnFragmentInteractionListener,
        FragmentObsFacilito.OnFragmentInteractionListener,
        FragmentNoticias.OnFragmentInteractionListener,
        SearchView.OnQueryTextListener
{

    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView; // menu inferior
    private FragmentManager fragmentManager;
    public static Context contextOfApplication;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    DrawerLayout drawerLayout;
    public ImageButton buscar;
    TextView Title_txt,user_data;
    String lastTag;
    //NavigationView navigation_drawer_container;

    ArrayList<Maestro> spdatasearch = new ArrayList<>();
    ArrayList<PublicacionModel> dataSeach = new ArrayList<>();
    CardView card_result;
    TextView txt_result;
    ListView list_result;
    SearchAdapter adSearch;
    ConstraintLayout constraintLayout,constrainSearch;
    RelativeLayout rl1;
    LinearLayout Lrly;
    public SearchView searchView;
    boolean upFlag;
    boolean downFlag;
    boolean listenerFlag;
    int contPublicacion;
    boolean flag_enter=true;
    int pagination;
    public static int countFacilito;
    public static boolean flag_Facilito=false;

    public static int countObservacion;
    public static boolean flag_observacion=false;

    public static int countInspeccion;
    public static boolean flag_inspeccion=false;

    public static int countNoticia;
    public static boolean flag_noticia=false;


    String TipoSearch;
    String txtSearch;
    String oldTipo="";
    String oldtxtSearch="";

// activr NFC
    public NfcAdapter nfcAdapter;
    public PendingIntent pendingIntent;
    public boolean activeScan, existNFC;
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Fragment fragment=GlobalVariables.fragmentStack.get(GlobalVariables.fragmentStack.size()-1);
        if ( fragment instanceof FragmentFichaPersonal) {
            if(activeScan) {
                setIntent(intent);
                resolveIntent(intent);
            }
        }
    }

    public void showNFC(boolean pass){
        if(pass){
            nfcAdapter=null;
            pendingIntent =null;
        }
        else{
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);

            if(!nfcAdapter.isEnabled())
                activarnfc();
            pendingIntent= PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
        }
        activeScan=!pass;

    }

    public void activarnfc(){
        Toast.makeText(this, "Se necesita activar NFC.", Toast.LENGTH_LONG).show();
        Intent intent= new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    public void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if(nfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || nfcAdapter.ACTION_TECH_DISCOVERED.equals(action)|| nfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(nfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if(rawMsgs != null){
                msgs= new NdefMessage[rawMsgs.length];
                for(int i=0;i<rawMsgs.length;i++){
                    msgs[i]=(NdefMessage) rawMsgs[i];
                }
            }
            else{
                byte[] empty = new byte[0];
                byte[] id= intent.getByteArrayExtra(nfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(nfcAdapter.EXTRA_TAG);
                byte[] idnfc= tag.getId();
                String[] code=toHex(idnfc).split(" ");
                int len=code.length;
                String CodNFC=":"+code[len-3]+code[len-2]+code[len-1];

                String url = GlobalVariables.Url_base + "FichaPersonal/Informaciongeneral?id="+CodNFC;
                Fragment fragment=GlobalVariables.fragmentStack.get(GlobalVariables.fragmentStack.size()-1);
                FragmentFichaPersonal my = (FragmentFichaPersonal) fragment;
                my.loadScan(url);
            }

        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(int i= bytes.length-1;i>=0;--i){
            int b= bytes[i] &0xff;
            if(b<0x10) sb.append("0");
            sb.append(Integer.toHexString(b));
            if(i>0) sb.append(" ");
        }
        return sb.toString();
    }
    @Override
    public void onResume() {
        super.onResume();
        Fragment fragment=GlobalVariables.fragmentStack.get(GlobalVariables.fragmentStack.size()-1);
        if ( fragment instanceof FragmentFichaPersonal) {
            FragmentFichaPersonal my = (FragmentFichaPersonal) fragment;
            if (nfcAdapter != null) {
                if (!nfcAdapter.isEnabled())
                    activarnfc();
                nfcAdapter.enableForegroundDispatch(this,pendingIntent, null, null);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Fragment fragment=GlobalVariables.fragmentStack.get(GlobalVariables.fragmentStack.size()-1);
        if ( fragment instanceof FragmentFichaPersonal) {
            FragmentFichaPersonal my = (FragmentFichaPersonal) fragment;
            if (nfcAdapter != null) {
                nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
            }
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void menuLateral(View view) {

        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void lupaBuscar(View view) {

        if(lastTag=="E"){
            Fragment fragment=GlobalVariables.fragmentStack.get(GlobalVariables.fragmentStack.size()-1);
            if ( fragment instanceof FragmentAvanzado) {
                FragmentAvanzado my = (FragmentAvanzado) fragment;
                my.SendFeedback();
            }
            return;
        }

        layoutInflater =(LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_search, null);

        popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(buscar, Gravity.TOP, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
        popupWindow.setOutsideTouchable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        dataSeach.clear();
        pagination=1;
        TipoSearch="0";
        txtSearch="";
        oldTipo="";
        oldtxtSearch="";

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if(adSearch!=null && adSearch.popupWindow!=null)adSearch.popupWindow.dismiss();
            }
        });

        ImageButton btn_closep=(ImageButton) popupView.findViewById(R.id.btn_closep);
        ImageButton btn_filtro=(ImageButton) popupView.findViewById(R.id.btn_filtro);
        searchView=(SearchView) popupView.findViewById(R.id.searchView);

        card_result=(CardView) popupView.findViewById(R.id.cardSearch);
        txt_result=(TextView) popupView.findViewById(R.id.txt_mensaje);
        list_result=(ListView) popupView.findViewById(R.id.list_result);
        constraintLayout=(ConstraintLayout) popupView.findViewById(R.id.const_main2);
        constrainSearch=(ConstraintLayout) popupView.findViewById(R.id.constrainSearch);
        Spinner sptipo= (Spinner) popupView.findViewById(R.id.spinnerTipo);
        rl1=(RelativeLayout) popupView.findViewById(R.id.rl1);
        Lrly=(LinearLayout)  popupView.findViewById(R.id.linearLayout2);
        card_result.setVisibility(View.GONE);

        list_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"Click en "+position,Toast.LENGTH_SHORT).show();
                String Codigo=dataSeach.get(position).Codigo;
                Intent intent;
                switch (Codigo.substring(0,3)){
                    case "OBS":
                        String tipoObs=GlobalVariables.listaGlobal.get(position).Tipo;
                        intent = new Intent(MainActivity.this, ActMuroDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("posTab",0);
                        intent.putExtra("tipoObs",tipoObs);
                        startActivity(intent);
                        break;
                    case "INS":
                        intent = new Intent(MainActivity.this, ActInspeccionDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("posTab",0);
                        //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                        startActivity(intent);
                        break;
                    case "NOT":
                        intent = new Intent(MainActivity.this, ActNoticiaDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("posTab",0);
                        //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                        startActivity(intent);

                        break;
                    case "OBF":
                        intent = new Intent(MainActivity.this, obsFacilitoDet.class);
                        intent.putExtra("codObs",Codigo);
                        //intent.putExtra("posTab",0);
                        //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                        startActivity(intent);
                        break;
                }
                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
            }
        });

        list_result.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //     if(is_swipe) {

                if (scrollState == SCROLL_STATE_IDLE) {
                    listenerFlag = false;
                }
                if (upFlag && scrollState == SCROLL_STATE_IDLE) {
                    upFlag = false;
                   // swipeRefreshLayout.setEnabled(true);
                }
                if (downFlag && scrollState == SCROLL_STATE_IDLE) {
                    downFlag = false;
                    if (adSearch.getCount() != contPublicacion && flag_enter) {
                        GlobalVariables.istabs=false;// para que no entre al flag de tabs

                        //progressBarMain.setVisibility(View.VISIBLE);
                        flag_enter = false;
                        constraintLayout.setVisibility(View.VISIBLE);
                        Utils.isActivity=false;

                        pagination+=1;
                        String url = GlobalVariables.Url_base + "Muro/Search/" +txtSearch+"/"+TipoSearch+"/"+ pagination + "/" + "7";

                        ActivityController obj = new ActivityController("get-"+pagination, url, MainActivity.this,MainActivity.this);
                        obj.execute("1");
                    }
                }
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    listenerFlag = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (listenerFlag && !view.canScrollVertically(1)) {
                    downFlag = true;
                    upFlag = false;
                }
                if (listenerFlag && !view.canScrollVertically(-1)) {
                    upFlag = true;
                    downFlag = false;
                }  }
        });

            SpinnerAdapter adapter=new SpinnerAdapter(this, R.layout.item_spinner,R.id.txt,spdatasearch);
            sptipo.setAdapter(adapter);

        if(lastTag.equals("A"))  {
            sptipo.setVisibility(View.VISIBLE);
            btn_filtro.setVisibility(View.GONE);
        }
        else {
            sptipo.setVisibility(View.GONE);
            btn_filtro.setVisibility(View.VISIBLE);
            if(lastTag.equals("I")) TipoSearch="1";
            else if(lastTag.equals("C")) TipoSearch="2";
            else if(lastTag.equals("D")) TipoSearch="3";
            else TipoSearch="4";
        }

            sptipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) popupView.findViewById(R.id.spinnerTipo) ).getSelectedItem();
                TipoSearch=Tipo.CodTipo;
                if(!txtSearch.isEmpty()&&(!txtSearch.equals(oldtxtSearch)||!oldTipo.equals(TipoSearch))){
                    oldTipo=TipoSearch;
                    oldtxtSearch=txtSearch;
                    String url = GlobalVariables.Url_base + "Muro/Search/" +txtSearch+"/"+TipoSearch+"/1/" + "7";
                    ActivityController obj = new ActivityController("get", url, MainActivity.this,MainActivity.this);
                    obj.execute("0");
                    searchView.clearFocus();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

            searchView.setOnQueryTextListener(this);
            searchView.setFocusable(false);
                rl1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        popupWindow.dismiss();
                    }
                });
                btn_closep.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        popupWindow.dismiss();
                    }
                });
                btn_filtro.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        if(lastTag.equals("C")) // observacion
                        {
                            FragmentObservaciones temp =(FragmentObservaciones)GlobalVariables.fragmentSave.get(0);
                            temp.Filtro_Obs();
                            popupWindow.dismiss();
                        }
                        else if(lastTag.equals("D")) // inspe
                        {
                            FragmentInspecciones temp =(FragmentInspecciones)GlobalVariables.fragmentSave.get(1);
                            temp.Filtro_Insp();
                            popupWindow.dismiss();
                        }
                        else if(lastTag.equals("I")) // obsFac
                        {
                            FragmentObsFacilito temp =(FragmentObsFacilito)GlobalVariables.fragmentSave.get(2);
                            temp.Filtro_Facilito();
                            popupWindow.dismiss();
                        }else if(lastTag.equals("N"))//noticias
                        {
                            FragmentNoticias temp =(FragmentNoticias)GlobalVariables.fragmentSave.get(4);
                            temp.Filtro_Noticias();
                            popupWindow.dismiss();
                        }

                    }
                });

        }

    @Override
    public boolean onQueryTextSubmit(String query) {
        txtSearch=query.trim();
        if(!txtSearch.isEmpty()&&(!txtSearch.equals(oldtxtSearch)||!oldTipo.equals(TipoSearch))){
            oldTipo=TipoSearch;
            oldtxtSearch=txtSearch;
            String url = GlobalVariables.Url_base + "Muro/Search/" +txtSearch+"/"+TipoSearch+"/1/" + "7";
            ActivityController obj = new ActivityController("get", url, MainActivity.this,MainActivity.this);
            obj.execute("0");
            searchView.clearFocus();
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        txtSearch=newText;
        return false;
    }

    public void LogOut(View view) {
        //navigation_drawer_container.setVisibility(View.GONE);
        //drawerLayout.closeDrawers();

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        //alertDialog.setCancelable(false);
        alertDialog.setTitle("Cerrar sesión");
        alertDialog.setIcon(R.drawable.warninicon);
        alertDialog.setMessage("¿Seguro que quieres salir?");

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GlobalVariables.listaGlobalObservacion.clear();;
                GlobalVariables.listaGlobal.clear();
                GlobalVariables.listaGlobalInspeccion.clear();
                GlobalVariables.listaGlobalNoticias.clear();
                GlobalVariables.listaGlobalFacilito.clear();
                GlobalVariables.listaPlanMin.clear();

                GlobalVariables.token_auth="";

                Save_status(false);
                Save_Datalogin("","");
                Intent intent=new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();

            }
        });
        alertDialog.show();

    }

    public void FichaPersonal(View view) {
        //drawerLayout.openDrawer(GravityCompat.END);
        drawerLayout.closeDrawers();
        //navigation_drawer_container.setVisibility(View.GONE);
        Gson gson = new Gson();
        GlobalVariables.userLoaded=gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
        GlobalVariables.dniUser=GlobalVariables.userLoaded.NroDocumento;
        ClickMenuFicha();
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_ficha).setChecked(true);
    }

    public enum NavigationFragment{
        Muro,
        Capacitaciones,
        FichaPersonal,
        ObsFacilito,
        Observaciones,
        Inspecciones,
        Feedback,
        Configuracion,
        Contactenos,
        PlanPendiente,
        Noticias
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        disableShiftMode(bottomNavigationView);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        buscar=findViewById(R.id.btn_buscar);
        Title_txt= (TextView)findViewById(R.id.txtTitleMain);
        user_data=findViewById(R.id.user_data);
        //navigation_drawer_container=findViewById(R.id.navigation_drawer_container);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        contextOfApplication = getApplicationContext();
        GlobalVariables.fragmentSave.push(new FragmentObservaciones()); //0
        GlobalVariables.fragmentSave.push(new FragmentInspecciones()); //1
        GlobalVariables.fragmentSave.push(new FragmentObsFacilito()); //2
        GlobalVariables.fragmentSave.push(new FragmentPlanPendiente()); //3
        GlobalVariables.fragmentSave.push(new FragmentNoticias()); //4

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter!=null)existNFC=true;
        ChangeFragment(NavigationFragment.Muro);
        uncheckItemsMenu();
        spdatasearch.add(new Maestro(R.drawable.ic_all_options,"0","Todos"));
        spdatasearch.add(new Maestro(R.drawable.ic_facilito,"1","Rep. SOS"));
        spdatasearch.add(new Maestro(R.drawable.ic_iobservacion,"2","Observación"));
        spdatasearch.add(new Maestro(R.drawable.ic_iinspeccion,"3","Inspección"));
        spdatasearch.add(new Maestro(R.drawable.ic_inoticia,"4","Noticia"));

        bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/noticias");
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //navigationView.setItemIconTintList(null);

        if(!GlobalVariables.desdeBusqueda){
            ChangeFragment(NavigationFragment.Muro);
            uncheckItemsMenu();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);
            GlobalVariables.desdeBusqueda=false;
        }else{
            ChangeFragment(NavigationFragment.FichaPersonal);
            uncheckItemsMenu();
            bottomNavigationView.getMenu().findItem(R.id.navigation_ficha).setChecked(true);
        }
        //GlobalVariables.fragmentSave.push(new FragmentObservaciones()); //2
        String nom_user="";
        if(!org.apache.commons.lang3.StringUtils.isEmpty(GlobalVariables.json_user))
        {
            Gson gson = new Gson();
            GlobalVariables.userLoaded=gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
            GlobalVariables.userLogin= GlobalVariables.userLoaded;
            GlobalVariables.dniUser= GlobalVariables.userLoaded.NroDocumento;
           nom_user=GlobalVariables.userLoaded.Nombres;
        }

        String[] DataUser= new String[0];
        String[] nombre = new String[0];
        String[] apellido=new String[0];
        //String[] a = new String[0];
        DataUser = nom_user.split(",");

        apellido=DataUser[0].trim().split(" ");
        nombre=DataUser[1].trim().split(" ");

        user_data.setText(nombre[0]+" "+apellido[0]);


        String versionName = BuildConfig.VERSION_NAME;
        if(Float.parseFloat(obtener_version())<=Float.parseFloat(versionName)){
            hideItem();
        }
        if(GlobalVariables.userLoaded.Rol.equals("1")||GlobalVariables.userLoaded.Rol.equals("4")||GlobalVariables.userLoaded.Rol.equals("5"))
            showCapacitacion();

    }
    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_actualizar).setVisible(false);
    }

    private void showCapacitacion()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_capacitacion).setVisible(true);
        uncheckItems(nav_Menu);
    }

    public static Context getContextOfApplication(){ return contextOfApplication; }
    //int  backpress=0;
    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        boolean passdismis=true;
        try {

            //drawerLayout.closeDrawers();
            //drawerLayout.openDrawer(GravityCompat.START);
            //drawerLayout.isDrawerOpen();

            if(drawerLayout.isDrawerOpen(GravityCompat.START)) {//detecta si el menu lateral esta abierto
                //drawer is open
                drawerLayout.closeDrawers();
            }else if (GlobalVariables.fragmentStack.size() == 2) {

                if(lastTag.equals("C")) // Obs
                {
                    FragmentObservaciones temp =(FragmentObservaciones)GlobalVariables.fragmentSave.get(0);
                    if(adSearch!=null &&adSearch.popupWindow!=null &&adSearch.popupWindow.isShowing())adSearch.popupWindow.dismiss();
                    else if(popupWindow!=null &&popupWindow.isShowing())popupWindow.dismiss();
                    else  if(temp.ca.popupWindow!=null&&temp.ca.popupWindow.isShowing())  temp.ca.popupWindow.dismiss();
                    else passdismis=false;

                }
                else if(lastTag.equals("D")) // inspe
                {
                    FragmentInspecciones temp =(FragmentInspecciones)GlobalVariables.fragmentSave.get(1);
                    if(adSearch!=null &&adSearch.popupWindow!=null &&adSearch.popupWindow.isShowing())adSearch.popupWindow.dismiss();
                    else if(popupWindow!=null &&popupWindow.isShowing())popupWindow.dismiss();
                    else if(temp.ca.popupWindow!=null&&temp.ca.popupWindow.isShowing())temp.ca.popupWindow.dismiss();
                    else passdismis=false;
                }
                else if(lastTag.equals("H")) // planes
                {
                    FragmentPlanPendiente temp =(FragmentPlanPendiente)GlobalVariables.fragmentSave.get(3);
                    if(adSearch!=null &&adSearch.popupWindow!=null &&adSearch.popupWindow.isShowing())adSearch.popupWindow.dismiss();
                    else if(popupWindow!=null &&popupWindow.isShowing())popupWindow.dismiss();
                    else if(temp.pma.popupWindow!=null&&temp.pma.popupWindow.isShowing())temp.pma.popupWindow.dismiss();
                    else passdismis=false;
                }
                else if(lastTag.equals("I")) // obsFac
                {
                    FragmentObsFacilito temp =(FragmentObsFacilito)GlobalVariables.fragmentSave.get(2);
                    if(adSearch!=null &&adSearch.popupWindow!=null &&adSearch.popupWindow.isShowing())adSearch.popupWindow.dismiss();
                    else if(popupWindow!=null &&popupWindow.isShowing())popupWindow.dismiss();
                    else if(temp.ca.popupWindow!=null&&temp.ca.popupWindow.isShowing())temp.ca.popupWindow.dismiss();
                    else passdismis=false;
                }

                else if(lastTag.equals("N")) // noticias
                {
                    FragmentNoticias temp =(FragmentNoticias)GlobalVariables.fragmentSave.get(4);
                    if(adSearch!=null &&adSearch.popupWindow!=null &&adSearch.popupWindow.isShowing())adSearch.popupWindow.dismiss();
                    else if(popupWindow!=null &&popupWindow.isShowing())popupWindow.dismiss();
                    else if(temp.ca.popupWindow!=null&&temp.ca.popupWindow.isShowing())temp.ca.popupWindow.dismiss();
                    else passdismis=false;
                }


                else passdismis=false;

                if(!passdismis){
                    Title_txt.setText("HSEC");

                    fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    GlobalVariables.fragmentStack.get(1).onPause();

                    ft.remove(GlobalVariables.fragmentStack.get(1));
                    GlobalVariables.fragmentStack.remove(1);
                    GlobalVariables.fragmentStack.get(0).onResume();
                    uncheckItemsMenu();

                    //Fragment fra=GlobalVariables.fragmentStack.lastElement();
                    bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);
                    buscar.setVisibility(View.VISIBLE);
                    ft.show(GlobalVariables.fragmentStack.lastElement());
                    lastTag="A";
                    ft.commit();
                }
            }else {
                //super.onBackPressed();
                FragmentMuro temp =(FragmentMuro)GlobalVariables.fragmentStack.get(0);

                if(adSearch!=null && adSearch.popupWindow!=null &&adSearch.popupWindow.isShowing())adSearch.popupWindow.dismiss();
                else if(popupWindow!=null &&popupWindow.isShowing())popupWindow.dismiss();
                else if(temp.ca.popupWindow!=null&&temp.ca.popupWindow.isShowing())temp.ca.popupWindow.dismiss();
                else if (exit) {
                    //GlobalVariables.fragmentStack.clear();
                    super.onBackPressed(); // finish activity
                    //this.finish();

                } else {
                    Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 3 * 1000);
                }
            }
        }catch (Throwable e){
            Log.d("error_frag", e.getLocalizedMessage());
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void DeleteObject(String Url, int index){
        String url= GlobalVariables.Url_base+Url;
        ActivityController obj = new ActivityController("get", url, this,this);
        obj.execute(""+index);
    }

    public void openFichaPersona(){

        ChangeFragment(NavigationFragment.FichaPersonal);
        GlobalVariables.isUserlogin=false;
        uncheckItemsMenu();
        ClickMenuFicha();
        if(popupWindow!=null)popupWindow.dismiss();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_reporte) {

            GlobalVariables.flagFacilito=false;
            Intent myIntent = new Intent(this, report_obs.class);
            startActivity(myIntent);
        }

        if(id==R.id.nav_sisap){
            try{
                Intent sendIntent =   getPackageManager().getLaunchIntentForPackage("com.base.app.donnyadrian.sisap008");
                sendIntent.putExtra("dataApp", true);
                startActivity(sendIntent);
            }
            catch (Throwable e){
            }
        }

        if(id == R.id.nav_gamePacto){
            String appPackageName = "com.Cerv.PactoPorLaVida";
            try {
                Intent sendIntent =   getPackageManager().getLaunchIntentForPackage(appPackageName);
                startActivity(sendIntent);
            } catch (Exception e){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        }

        else if (id == R.id.nav_observacion) {

            GlobalVariables.ObjectEditable=false;
            Intent obserbacion_edit = new Intent(this,observacion_edit.class);
            obserbacion_edit.putExtra("codObs", "OBS000000XYZ");
            obserbacion_edit.putExtra("tipoObs","TO01");
            obserbacion_edit.putExtra("posTab", 0);
            startActivity(obserbacion_edit);

        } else if (id == R.id.nav_inspeccion) {
            GlobalVariables.ObjectEditable=false;
            Intent addInspeccion = new Intent(this,AddInspeccion.class);
            addInspeccion.putExtra("codObs","INSP000000XYZ");
            startActivity(addInspeccion);


           // Toast.makeText(this, "nav_inspeccion", Toast.LENGTH_SHORT).show();

        }
        else if (id == R.id.nav_capacitacion) {
            Menu menu = navigationView.getMenu();
            uncheckItems(menu);
            ClickCursoCapacitacion();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);
        }
            else if (id == R.id.nav_noticias){
            Menu menu = navigationView.getMenu();
            uncheckItems(menu);
            ClickNoticias();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);
        }
        else if (id == R.id.nav_pendientes){
            Menu menu = navigationView.getMenu();
            uncheckItems(menu);
            ClickPendientes();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);
        }
        else if (id == R.id.nav_feedback) {

            Menu menu = navigationView.getMenu();
            uncheckItems(menu);
            ClickMenuFeedBack();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);
        }
        else if (id == R.id.nav_Contactenos){
            Menu menu = navigationView.getMenu();
            uncheckItems(menu);
            ClickMenuContactenos();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);

        }
        else if (id == R.id.nav_configuracion){
            Menu menu = navigationView.getMenu();
            uncheckItems(menu);

            ClickMenuConfiguracion();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);
        }
        /*
        else if(id == R.id.nav_cerrar){

            Save_status(false);
            Save_Datalogin("","");
            Intent intent=new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }*/
        else if(id == R.id.nav_actualizar){
            final String urlPlay = "https://play.google.com/store/apps/details?id=com.pango.hsec.hsec";

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPlay)));
            } catch (Exception e) {
                Log.e(TAG, "Aplicación no instalada.");
                //Abre url de pagina.
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPlay)));
            }

            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //menu inferior
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_muro:
                    ChangeFragment(NavigationFragment.Muro);
                    //bottomNavigationView.setVisibility(View.GONE);
                    uncheckItemsMenu();
                    return true;

                /*case R.id.navigation_aprob:

                    uncheckItemsMenu();
                    //navigationView.getMenu().findItem(R.id.nav_imagenes).setChecked(true);
                    ClickMenuAprobaciones();
                    return true;
                    */
                case R.id.navigation_ficha:
                    GlobalVariables.isUserlogin=true;
                    GlobalVariables.barTitulo=true;
                    uncheckItemsMenu();
                    ClickMenuFicha();

                    //navigationView.getMenu().findItem(R.id.nav_videos).setChecked(true);
                    return true;
            /*    case R.id.navigation_registro:

                    uncheckItemsMenu();
                    ClickMenuRegistro();
                    return true;
                    */
                case R.id.navigation_observacion:
                    uncheckItemsMenu();
                    //navigationView.getMenu().findItem(R.id.nav_imagenes).setChecked(true);
                    ClickMenuObservacion();
                    return true;

                case R.id.navigation_inspeccion:
                    uncheckItemsMenu();
                    //navigationView.getMenu().findItem(R.id.nav_imagenes).setChecked(true);
                    ClickMenuInspeccion();
                    return true;

                case R.id.navigation_avanzado:
                    uncheckItemsMenu();
                    ClickMenuAvanzado();
                    return true;

            }
            return false;
        }

    };

    public void ClickMenuObservacion() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_observacion).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.Observaciones);

    }

    public void ClickMenuInspeccion() {

        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_inspeccion).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.Inspecciones);
    }

    public void ClickMenuFicha() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_ficha).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        //navigationView.getMenu().findItem(R.id.nav_ficha).setChecked(true);

        ChangeFragment(NavigationFragment.FichaPersonal);

    }

    public void ClickMenuAvanzado() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_avanzado).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.ObsFacilito);

    }

    private void ClickMenuConfiguracion() {
        uncheckItemsMenu();
        ChangeFragment(NavigationFragment.Configuracion);

    }

    private void ClickMenuFeedBack() {
        uncheckItemsMenu();
        ChangeFragment(NavigationFragment.Feedback);

    }

    private void ClickMenuContactenos() {
        uncheckItemsMenu();
        ChangeFragment(NavigationFragment.Contactenos);
    }
    private void ClickNoticias() {
        uncheckItemsMenu();
        ChangeFragment(NavigationFragment.Noticias);

    }

    public void ClickPendientes(){
        uncheckItemsMenu();
        ChangeFragment(NavigationFragment.PlanPendiente);
    }

    public void ClickCursoCapacitacion(){
        uncheckItemsMenu();
        ChangeFragment(NavigationFragment.Capacitaciones);
    }
    public void uncheckItemsMenu() {

        try {
            // limpiamos todos los seleccionados
            Menu menu = navigationView.getMenu();
            uncheckItems(menu);

            menu = bottomNavigationView.getMenu();
            uncheckItems(menu);
        }catch (Exception e){
            System.out.print(e);
        }

    }


    private void uncheckItems(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    }


    public void ChangeFragment(NavigationFragment value){
        Fragment fragment = null;
        String Tipo="A",Title="HSEC";
        switch (value) {
            case Muro:    fragment = new FragmentMuro(); break;
            case FichaPersonal: fragment = FragmentFichaPersonal.newInstance("0","0"); Tipo="B";  Title="Ficha"; break;
            case Observaciones: fragment = new FragmentObservaciones(); Tipo="C"; Title="Observaciones"; break;
            case Inspecciones: fragment = new FragmentInspecciones();  Tipo="D";Title="Inspecciones"; break;
            case ObsFacilito: fragment = new FragmentObsFacilito(); Tipo="I"; Title="Reportes SOS"; break;
            case Feedback: fragment = new FragmentAvanzado(); Tipo="E"; Title="Feedback"; break;
            case Configuracion: fragment = new FragmentConfiguracion();  Tipo="F";Title="Configuración"; break;
            case Contactenos: fragment = new FragmentContactenos();  Tipo="G";Title="Contactenos"; break;
            case PlanPendiente: fragment = new FragmentPlanPendiente();  Tipo="H";Title="Planes de acción"; break;
            case Capacitaciones: fragment = new FragmentCapacitaciones(); Tipo="J";  Title="Capacitaciones"; break;
            case Noticias: fragment = new FragmentNoticias();  Tipo="N";Title="Noticias"; break;


        }
        lastTag=Tipo;
        // button Search
        Title_txt.setText(Title);
        if(Tipo.equals("A")||Tipo.equals("C")||Tipo.equals("D")||Tipo.equals("I")||Tipo.equals("N")||Tipo.equals("E"))
        {
            buscar.setVisibility(View.VISIBLE);
            if(Tipo.equals("E"))
                buscar.setImageResource(R.drawable.ic_send);
            else buscar.setImageResource(R.drawable.ic_search);
        }
        else buscar.setVisibility(View.INVISIBLE);

        //changue values of menu title

        if(!Tipo.equals("A")) {
            GlobalVariables.passHome=true;
            FragmentMuro temp= (FragmentMuro)GlobalVariables.fragmentStack.get(0);
            if(temp.List_muro!=null)
            GlobalVariables.stateMuro=temp.List_muro.onSaveInstanceState();
        }
        if(!Tipo.equals("B")){
            GlobalVariables.isUserlogin=false;
        }
        if(!Tipo.equals("C")){
            GlobalVariables.passObs=true;
            FragmentObservaciones temp= (FragmentObservaciones)GlobalVariables.fragmentSave.get(0);
            if(temp.list_busqueda!=null)
                GlobalVariables.stateObs=temp.list_busqueda.onSaveInstanceState();
        }
        if(!Tipo.equals("D")){
            GlobalVariables.passInsp=true;
            FragmentInspecciones temp= (FragmentInspecciones)GlobalVariables.fragmentSave.get(1);
            if(temp.list_busqueda!=null)
                GlobalVariables.stateInsp=temp.list_busqueda.onSaveInstanceState();
        }
        if(!Tipo.equals("I")){
            GlobalVariables.passFac=true;
            FragmentObsFacilito temp= (FragmentObsFacilito)GlobalVariables.fragmentSave.get(2);
            if(temp.list_busqueda!=null)
                GlobalVariables.stateFac=temp.list_busqueda.onSaveInstanceState();
        }
       //showFragment(R.id.content,fragment,Tipo,false);
        if(fragment!=null&&GlobalVariables.fragmentStack.size()==0){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, fragment)
                    .commit();

       }else if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, fragment)
                    .hide(GlobalVariables.fragmentStack.lastElement())
                    .commit();
        }
        GlobalVariables.apilarFrag(fragment,Tipo);
    }


    protected void showFragment( int resId, Fragment fragment, String tag, boolean addToBackStack ) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if ( lastTag != null ) {
            Fragment lastFragment = fragmentManager.findFragmentByTag( lastTag );
            if ( lastFragment != null ) {
                transaction.hide( lastFragment );
            }
        }

        if ( fragment.isAdded() ) {
            transaction.show( fragment );
        }
        else {
            lastTag=tag;
            transaction.add( resId, fragment, tag ).setBreadCrumbShortTitle( tag );
        }

        if ( addToBackStack ) {
            transaction.addToBackStack( tag );
        }

        transaction.commit();
    }


    @Override
    public void success(String data,String Tipo) {
//data add
        Gson gson = new Gson();
        GetPublicacionModel getPublicacionModel = gson.fromJson(data, GetPublicacionModel.class);
        if(Tipo.equals("0")){ // from login
            String sms="";
            if(getPublicacionModel.Count==0)sms="No hubo ocurrencias";
            else sms="Total registros encontrados: "+getPublicacionModel.Count;
            dataSeach=getPublicacionModel.Data;
            contPublicacion=getPublicacionModel.Count;
            txt_result.setText(sms);
            adSearch = new SearchAdapter(this,dataSeach,this);
            list_result.setAdapter(adSearch);
            rl1.setVisibility(View.GONE);
            card_result.setVisibility(View.VISIBLE);
            DisplayMetrics dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            Lrly.setMinimumHeight(dm.heightPixels);
            list_result.setMinimumHeight(dm.heightPixels);
            card_result.setMinimumHeight(dm.heightPixels);
            constrainSearch.setMinimumHeight(dm.heightPixels);
            //popupWindow.setHeight(dm.heightPixels);
            //popupWindow.update();
        }
        else if(Tipo.equals("1")){ //from refresh data

            constraintLayout.setVisibility(View.GONE);
            flag_enter=true;
            for(PublicacionModel item:getPublicacionModel.Data)
                adSearch.add(item);
            adSearch.notifyDataSetChanged();
        }
        else
        {   if(data.contains("-1")) Toast.makeText(this, "Ocurrio un error al eliminar registro.",    Toast.LENGTH_SHORT).show();
        else adSearch.remove(Integer.parseInt(Tipo)-3);
        }
    }

    @Override
    public void successpost(String data,String Tipo) {

    }

    @Override
    public void error(String mensaje,String Tipo) {

    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }


    public void Save_status(boolean ischecked){
        SharedPreferences check_status = this.getSharedPreferences("checked", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_estado = check_status.edit();
        editor_estado.putBoolean("check", ischecked);
        editor_estado.commit();
    }
    public void Save_Datalogin(String user,String password){
        SharedPreferences user_login = this.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_user = user_login.edit();
        editor_user.putString("user", user);
        editor_user.putString("password",password);
        editor_user.commit();
    }

    public  String  obtener_version(){
        SharedPreferences check_version = this.getSharedPreferences("versiones", Context.MODE_PRIVATE);
        String version = check_version.getString("version","");
        return version;
    }


}
