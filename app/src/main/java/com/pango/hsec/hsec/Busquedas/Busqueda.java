package com.pango.hsec.hsec.Busquedas;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Noticias.ActNoticiaDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.Observaciones.FragmentComent;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.InspeccionAdapter;
import com.pango.hsec.hsec.adapter.NoticiasAdapter;
import com.pango.hsec.hsec.adapter.PublicacionAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetObservacionModel;
import com.pango.hsec.hsec.model.GetPersonaModel;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.NoticiasModel;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.model.PublicacionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import layout.FragmentInspecciones;
import layout.FragmentObservaciones;

import static com.pango.hsec.hsec.GlobalVariables.*;

public class Busqueda extends AppCompatActivity implements IActivity {
    Spinner sp_busqueda;
    String tipo_filtro="";
    Button btn_filtro;
    ImageButton close;
    public static final int REQUEST_CODE = 1;
    String url="";
    int contPublicacion;
    ListView list_busqueda;
    int paginacion2=1;
    boolean flagObsFiltro=true;
    boolean upFlag;
    boolean downFlag;
    boolean listenerFlag;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean flag_enter=true;
    ConstraintLayout constraintLayout;
    int tipo_busqueda;
    //ConstraintLayout constraintLayout;
    boolean loadingTop=false;
    TextView tx_texto, tx_mensajeb;
    ImageView lupabuscar;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    boolean flagpopup=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        close=findViewById(R.id.imageButton);

        tx_texto =(TextView) findViewById(R.id.tx_texto);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        constraintLayout=(ConstraintLayout) findViewById(R.id.const_main);
        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        lupabuscar=(ImageView) findViewById(R.id.lupabuscar);
        GlobalVariables loaddata = new GlobalVariables();
        //loaddata.LoadData();
        list_busqueda=(ListView) findViewById(R.id.list_busqueda);
        sp_busqueda=(Spinner) findViewById(R.id.sp_busqueda);
        tx_mensajeb=findViewById(R.id.tx_mensajeb);

        ArrayAdapter adapterBusObs = new ArrayAdapter(this.getBaseContext(),R.layout.custom_spinner_item, busqueda_tipo);
        adapterBusObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_busqueda.setAdapter(adapterBusObs);

        sp_busqueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipo_filtro= (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //tipo_filtro=GlobalVariables.busqueda_tipo[0];
            }
        });

       btn_filtro=(Button) findViewById(R.id.btn_filtro);

       btn_filtro.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(tipo_filtro.equals(busqueda_tipo[0])) {
                  /* Intent intent = new Intent(Busqueda.this, B_observaciones.class);
                   //intent.putExtra("nombreP","");
                   startActivity(intent);   */

                   Utils.observacionModel=new ObservacionModel();

                   Intent intent = new Intent(Busqueda.this, B_observaciones.class);
                   startActivityForResult(intent , REQUEST_CODE);


               }else if(tipo_filtro.equals(busqueda_tipo[1])){


                   Intent intent = new Intent(Busqueda.this, B_inspecciones.class);
                   startActivityForResult(intent , REQUEST_CODE);


               }else if(tipo_filtro.equals(busqueda_tipo[2])){
                   Intent intent = new Intent(Busqueda.this, B_noticias.class);
                   startActivityForResult(intent , REQUEST_CODE);
               }

           }
       });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {
                GlobalVariables.istabs=false;// para que no entre al flag de tabs
                //is_swipe=false;
                swipeRefreshLayout.setRefreshing(true);
                tx_texto.setVisibility(View.VISIBLE);
                flagObsFiltro=true;
                paginacion2=1;
                //   upFlag=false;
                //  downFlag=false;
                // (new Handler()).postDelayed(new Runnable() {
                //    @Override
                //    public void run() {
                //swipeRefreshLayout.setRefreshing(true);
                loadingTop=true;
                tx_texto.setVisibility(View.VISIBLE);
                GlobalVariables.listaGlobalFiltro.clear(); //crear segun el formato
                //GlobalVariables.contpublic=2;
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;
                GlobalVariables.isFragment=false;

                //GlobalVariables.isFragment=true;

                //url= GlobalVariables.Url_base+"Usuario/FiltroPersona/"+filtro+"/1/7";
                //url=GlobalVariables.Url_base+"Observaciones/GetOBservaciones/-/"+1+"/"+GlobalVariables.num_items;                final ActivityController obj = new ActivityController("post", url, Busqueda.this);
                //GlobalVariables.count=5;

                //Utils.observacionModel.CodUbicacion="5";
                //Utils.observacionModel.Lugar="1";
                String json = "";

                if(tipo_busqueda==1) {
                    //Utils.observacionModel=new ObservacionModel();
                    Utils.observacionModel.CodUbicacion = "5";
                    Utils.observacionModel.Lugar = String.valueOf(paginacion2);
                    Gson gson = new Gson();
                    json = gson.toJson(Utils.observacionModel);
                }else if(tipo_busqueda==2){
                    //Utils.inspeccionModel=new InspeccionModel();
                    Utils.inspeccionModel.Elemperpage = "5";
                    Utils.inspeccionModel.Pagenumber = String.valueOf(paginacion2);
                    Gson gson = new Gson();
                    json = gson.toJson(Utils.inspeccionModel);
                }else if(tipo_busqueda==3){
                    //Utils.noticiasModel=new NoticiasModel();
                    Utils.noticiasModel.Elemperpage = "5";
                    Utils.noticiasModel.Pagenumber = String.valueOf(paginacion2);
                    Gson gson = new Gson();
                    json = gson.toJson(Utils.noticiasModel);
                }


                //Utils.isActivity=true;
                GlobalVariables.istabs=false;
                final ActivityController obj = new ActivityController("post", url, Busqueda.this,Busqueda.this);
                obj.execute(json);

                // Toast.makeText(rootView.getContext(),"swipe",Toast.LENGTH_SHORT).show();

                //  } },0);

            } });


        list_busqueda.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == SCROLL_STATE_IDLE) {
                    listenerFlag = false;
                    Log.d("--:", "---------------------------");
                }
                if (upFlag && scrollState == SCROLL_STATE_IDLE) {
                    upFlag = false;

                    // Toast.makeText(rootView.getContext(), "ACEPTO UPFLAG", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setEnabled(true);

                }
                if (downFlag && scrollState == SCROLL_STATE_IDLE) {
                    downFlag = false;
                    // GlobalVariables.FDown=true;
                    //Toast.makeText(rootView.getContext(), "ACEPTO DOWNFLAG", Toast.LENGTH_SHORT).show();
                    /// cambiar el 100 por el total de publicaciones
                    if (GlobalVariables.listaGlobalFiltro.size() != contPublicacion && flag_enter&&flagObsFiltro) {

                        //progressBarMain.setVisibility(View.VISIBLE);
                        flag_enter = false;
                        constraintLayout.setVisibility(View.VISIBLE);
                        lupabuscar.setEnabled(false);
                        GlobalVariables.isFragment=false;

                        //GlobalVariables.isFragment=true;
                        //Utils.isActivity=true;
                        //url= GlobalVariables.Url_base+"Usuario/FiltroPersona/"+filtro+"/1/7";

                        //url =GlobalVariables.Url_base+"Usuario/FiltroPersona/"+filtro+"/"+ GlobalVariables.contpublic + "/" + "7";
                        String json2 = "";

                        //GlobalVariables.count=5;
                        paginacion2+=1;
                        if(tipo_busqueda==1) {
                            //Utils.observacionModel=new ObservacionModel();
                            Utils.observacionModel.CodUbicacion = "5";
                            Utils.observacionModel.Lugar = String.valueOf(paginacion2);
                            Gson gson = new Gson();
                            json2 = gson.toJson(Utils.observacionModel);
                        }else if(tipo_busqueda==2){
                            //Utils.inspeccionModel=new InspeccionModel();
                            Utils.inspeccionModel.Elemperpage = "5";
                            Utils.inspeccionModel.Pagenumber = String.valueOf(paginacion2);
                            Gson gson = new Gson();
                            json2 = gson.toJson(Utils.inspeccionModel);
                        }else if(tipo_busqueda==3){
                            //Utils.noticiasModel=new NoticiasModel();
                            Utils.noticiasModel.Elemperpage = "5";
                            Utils.noticiasModel.Pagenumber = String.valueOf(paginacion2);
                            Gson gson = new Gson();
                            json2 = gson.toJson(Utils.noticiasModel);
                        }


                        //GlobalVariables.isFragment=false;
                        //Utils.isActivity=true;
                        //url = GlobalVariables.Url_base + "Inspecciones/Filtroinspecciones";

                        GlobalVariables.istabs=false;// para que no entre al flag de tabs
                        final ActivityController obj = new ActivityController("post", url, Busqueda.this,Busqueda.this);
                        obj.execute(json2);

                        layoutInflater =(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        popupView = layoutInflater.inflate(R.layout.popup_blanco, null);

                        popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT ,580,false);
                        popupWindow.showAtLocation(list_busqueda, Gravity.CENTER, 0, 0);
                        flagpopup=true;



                    }

                }
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    listenerFlag = true;
                    swipeRefreshLayout.setEnabled(false);
                    Log.d("started", "comenzo");
                }
                //  }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("+1:", "" + view.canScrollVertically(1));
                Log.d("-1:", "" + view.canScrollVertically(-1));
                // Log.d("x:",""+view.getScrollX());
                if (listenerFlag && !view.canScrollVertically(1)) {
                    downFlag = true;
                    upFlag = false;
                    // Toast.makeText(rootView.getContext(), "canscroll abajo", Toast.LENGTH_SHORT).show();

                    // swipeRefreshLayout.setEnabled( false );

                }
                if (listenerFlag && !view.canScrollVertically(-1)) {
                    upFlag = true;
                    downFlag = false;
                    //  Toast.makeText(rootView.getContext(), "canscroll arriba" + upFlag + downFlag, Toast.LENGTH_SHORT).show();
                }
            }
        });
        listenerFlag = false;

        list_busqueda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"Click en "+position,Toast.LENGTH_SHORT).show();


                if(tipo_busqueda==1) {
                String CodObservacion=GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                String tipoObs=GlobalVariables.listaGlobalFiltro.get(position).Tipo;

                Intent intent = new Intent(Busqueda.this, ActMuroDet.class);
                intent.putExtra("codObs",CodObservacion);
                intent.putExtra("posTab",0);
                intent.putExtra("tipoObs",tipoObs);

                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                startActivity(intent);
                }else if(tipo_busqueda==2){
                String CodInspeccion= GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                    Intent intent = new Intent(Busqueda.this, ActInspeccionDet.class);
                    intent.putExtra("codObs",CodInspeccion);
                    intent.putExtra("posTab",0);
                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                    startActivity(intent);

                }else if(tipo_busqueda==3){
                    String CodNoticia= GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                    Intent intent = new Intent(Busqueda.this, ActNoticiaDet.class);
                    intent.putExtra("codObs",CodNoticia);
                    intent.putExtra("posTab",0);
                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                    startActivity(intent);

                }


            }
        });

        lupabuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.flagUpSc=true;
                GlobalVariables.isFragment=false;
                if(tipo_filtro.equals(busqueda_tipo[0])) {
                    Utils.observacionModel=new ObservacionModel();
                    ObservacionModel observacionModel=new ObservacionModel();
                    tipo_busqueda=1;
                    observacionModel.CodUbicacion = "5";
                    observacionModel.Lugar = "1";
                    String json = "";
                    Gson gson = new Gson();
                    json = gson.toJson(observacionModel);

                    Utils.isActivity = true;
                    url = GlobalVariables.Url_base + "Observaciones/FiltroObservaciones";
                    GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                    final ActivityController obj = new ActivityController("post", url, Busqueda.this,Busqueda.this);
                    obj.execute(json);

                }else if(tipo_filtro.equals(busqueda_tipo[1])){
                    Utils.inspeccionModel=new InspeccionModel();
                    InspeccionModel inspeccionModel=new InspeccionModel();
                    tipo_busqueda=2;
                    inspeccionModel.Elemperpage="5";
                    inspeccionModel.Pagenumber="1";
                    String json = "";

                    Gson gson = new Gson();
                    json = gson.toJson(inspeccionModel);

                    Utils.isActivity = true;
                    url = GlobalVariables.Url_base + "Inspecciones/Filtroinspecciones";
                    GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                    final ActivityController obj = new ActivityController("post", url, Busqueda.this,Busqueda.this);
                    obj.execute(json);

/*
                    Intent intent = new Intent(Busqueda.this, B_inspecciones.class);
                    startActivityForResult(intent , REQUEST_CODE);
*/
                }else if(tipo_filtro.equals(busqueda_tipo[2])) {
                    Utils.noticiasModel=new NoticiasModel();
                    NoticiasModel noticiasModel = new NoticiasModel();
                    tipo_busqueda = 3;
                    noticiasModel.Elemperpage = "5";
                    noticiasModel.Pagenumber = "1";
                    String json = "";

                    Gson gson = new Gson();
                    json = gson.toJson(noticiasModel);

                    Utils.isActivity = true;
                    url = GlobalVariables.Url_base + "Noticia/FiltroNoticias";
                    GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                    final ActivityController obj = new ActivityController("post", url, Busqueda.this,Busqueda.this);
                    obj.execute(json);

                }

            }
        });








    }
    public void close(View view){
        finish();
    }


    @Override
    public void success(String data, String Tipo) {

    }

    @Override
    public void successpost(String data1, String Tipo) {

        if(flagpopup){
            popupWindow.dismiss();
            flagpopup=false;
        }

        Gson gson = new Gson();
        GetPublicacionModel getPublicacionModel = gson.fromJson(data1, GetPublicacionModel.class);
        contPublicacion=getPublicacionModel.Count;


        if(GlobalVariables.listaGlobalFiltro.size()==0) {
            GlobalVariables.listaGlobalFiltro = getPublicacionModel.Data;
            if(getPublicacionModel.Data.size()==0){
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                tx_mensajeb.setVisibility(View.VISIBLE);
            }else{
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                tx_mensajeb.setVisibility(View.GONE);
            }
            //swipeRefreshLayout.setVisibility(View.VISIBLE);

            //GlobalVariables.listaGlobal=listaPublicaciones;
        }else{
            //listaPublicaciones.addAll(getPublicacionModel.Data);
            GlobalVariables.listaGlobalFiltro.addAll(getPublicacionModel.Data);
            swipeRefreshLayout.setVisibility(View.VISIBLE);


        }


        if(tipo_busqueda==1) {

            PublicacionAdapter ca = new PublicacionAdapter(this, GlobalVariables.listaGlobalFiltro,new FragmentObservaciones());
            list_busqueda.setAdapter(ca);
            ca.notifyDataSetChanged();

        }else if(tipo_busqueda==2){

            InspeccionAdapter ca = new InspeccionAdapter(this, GlobalVariables.listaGlobalFiltro,new FragmentInspecciones());
            list_busqueda.setAdapter(ca);
            ca.notifyDataSetChanged();
        }else if(tipo_busqueda==3){

            NoticiasAdapter ca = new NoticiasAdapter(this, GlobalVariables.listaGlobalFiltro);
            list_busqueda.setAdapter(ca);
            ca.notifyDataSetChanged();
        }



        if(GlobalVariables.flagUpSc==true){
            list_busqueda.setSelection(0);
            GlobalVariables.flagUpSc=false;
        }else
            //reemplazar el 100
            if(GlobalVariables.listaGlobalFiltro.size()>5&&GlobalVariables.listaGlobalFiltro.size()<contPublicacion) {
                //recListImag.smoothScrollToPosition(GlobalVariables.imagen2.size()-3);
                if(GlobalVariables.listaGlobalFiltro.size()%5==0) {
                    list_busqueda.setSelection(GlobalVariables.listaGlobalFiltro.size() - 6);
                }else{
                    list_busqueda.setSelection(GlobalVariables.listaGlobalFiltro.size()-1 );
                    //- GlobalVariables.listaGlobalFiltro.size()%5+1
                }

                flagObsFiltro=true;

            }else if(GlobalVariables.listaGlobalFiltro.size()==contPublicacion){
                list_busqueda.setSelection(GlobalVariables.listaGlobalFiltro.size());
                flagObsFiltro=false;

            }

        constraintLayout.setVisibility(View.GONE);
        lupabuscar.setEnabled(true);

        flag_enter=true;
        //GlobalVariables.contpublic += 1;
        // progressDialog.dismiss();
        // progressBar.setVisibility(View.GONE);

        if(loadingTop)
        {
            loadingTop=false;
            swipeRefreshLayout.setRefreshing(false);
            tx_texto.setVisibility(View.GONE);
            swipeRefreshLayout.setEnabled( false );
        }

        // GlobalVariables.FDown=false;

    }

    @Override
    public void error(String mensaje, String Tipo) {
        if(flagpopup){
            popupWindow.dismiss();
            flagpopup=false;
        }
        constraintLayout.setVisibility(View.GONE);
        paginacion2-=1;
        flag_enter=true;

        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
        
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            //Bundle datos = this.getIntent().getExtras();
            //tipo_busqueda=datos.getInt("Tipo_Busqueda");

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                GlobalVariables.flagUpSc=true;

                tipo_busqueda = data.getIntExtra("Tipo_Busqueda",0);

              /*  String nombre_obs = data.getStringExtra("nombreP");
                String codpersona_obs = data.getStringExtra("codpersona");
                id_persona.setText(nombre_obs);
                Utils.observacionModel.ObservadoPor=codpersona_obs;
                */
                if(tipo_busqueda==1) {
                    Utils.observacionModel.CodUbicacion = "5";
                    Utils.observacionModel.Lugar = "1";
                    String json = "";
                    Gson gson = new Gson();
                    json = gson.toJson(Utils.observacionModel);
                    GlobalVariables.isFragment=false;
                    Utils.isActivity = true;
                    url = GlobalVariables.Url_base + "Observaciones/FiltroObservaciones";
                    GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                    final ActivityController obj = new ActivityController("post", url, Busqueda.this,Busqueda.this);
                    obj.execute(json);

                }else if(tipo_busqueda==2){
                    Utils.inspeccionModel.Elemperpage="5";
                    Utils.inspeccionModel.Pagenumber="1";
                    String json = "";

                    Gson gson = new Gson();
                    json = gson.toJson(Utils.inspeccionModel);
                    GlobalVariables.isFragment=false;
                    Utils.isActivity = true;
                    url = GlobalVariables.Url_base + "Inspecciones/Filtroinspecciones";
                    GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                    final ActivityController obj = new ActivityController("post", url, Busqueda.this,Busqueda.this);
                    obj.execute(json);



                }else if(tipo_busqueda==3){
                    Utils.noticiasModel.Elemperpage="5";
                    Utils.noticiasModel.Pagenumber="1";
                    String json = "";

                    Gson gson = new Gson();
                    json = gson.toJson(Utils.noticiasModel);

                    GlobalVariables.isFragment=false;
                    Utils.isActivity = true;
                    url = GlobalVariables.Url_base + "Noticia/FiltroNoticias";
                    GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                    final ActivityController obj = new ActivityController("post", url, Busqueda.this,Busqueda.this);
                    obj.execute(json);

                }

            }



        } catch (Exception ex) {
            Toast.makeText(Busqueda.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


}
