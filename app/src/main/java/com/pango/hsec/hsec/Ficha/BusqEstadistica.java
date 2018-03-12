package com.pango.hsec.hsec.Ficha;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_observaciones;
import com.pango.hsec.hsec.Busquedas.Busqueda;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Noticias.ActNoticiaDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.InspeccionAdapter;
import com.pango.hsec.hsec.adapter.PublicacionAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.ObservacionModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.pango.hsec.hsec.Utils.inspeccionModel;

public class BusqEstadistica extends AppCompatActivity implements IActivity {
    Spinner sp_anio, sp_mes;
    String anio,mes,codPersona,descripcion;
    int tipo_busqueda;
    int contPublicacion;
    String url="";
    ListView list_estadistica;
    ImageButton btn_buscar_e;
    String anio_sel="";
    int mes_pos;
    String diaFin;

    int paginacion2=1;
    boolean flagObsFiltro=true;
    boolean upFlag;
    boolean downFlag;
    boolean listenerFlag;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean flag_enter=true;
    ConstraintLayout constraintLayout;
    TextView tx_texto, tx_mensajeb;
    boolean loadingTop=false;

    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    boolean flagpopup=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busq_estadistica);
        Bundle datos = getIntent().getExtras();
        anio= datos.getString("anio");
        int mesActual=Integer.parseInt(datos.getString("mes"));
        mes= (mesActual < 10 ? "0" : "")+mesActual;
        codPersona=datos.getString("codiPersona");
        descripcion=datos.getString("descripcion");


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        constraintLayout=(ConstraintLayout) findViewById(R.id.const_main);
        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        tx_mensajeb=findViewById(R.id.tx_mensajeb);
        btn_buscar_e=findViewById(R.id.btn_buscar_e);
        tx_texto =(TextView) findViewById(R.id.tx_texto);

        list_estadistica=findViewById(R.id.list_estadistica);
        sp_anio=findViewById(R.id.spinner_anio);
        sp_mes=findViewById(R.id.spinner_mes);



        ArrayAdapter adapterAnio = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, GlobalVariables.busqueda_anio);
        adapterAnio.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_anio.setAdapter(adapterAnio);
        sp_anio.setSelection(find(anio));

        sp_anio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                anio_sel= (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //tipo_filtro=GlobalVariables.busqueda_tipo[0];
            }
        });

        ArrayAdapter adapterMes = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, GlobalVariables.busqueda_mes);
        adapterMes.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_mes.setAdapter(adapterMes);
        sp_mes.setSelection(Integer.parseInt(mes));
        sp_mes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //mes_sel

                mes_pos=position;
               /*if(position!=0) {
                    mes_pos = String.valueOf(position);

                }else{
                    //revisar la posicion

                    mes_pos=String.valueOf(position);
                    //mes_pos= (String) parent.getItemAtPosition(position);
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Date fechaFin;
        Calendar calFin = Calendar.getInstance();
        calFin.set(Integer.parseInt(anio), mesActual-1, 1);
        calFin.set(Integer.parseInt(anio),mesActual-1 , calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
        //fechaFin = calFin.getTime();
        diaFin = (calFin.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "")+calFin.get(Calendar.DAY_OF_MONTH);



        String json = "";
        if(descripcion.equals("Observaciones")) {

            Utils.observacionModel = new ObservacionModel();
            ObservacionModel observacionModel = new ObservacionModel();
            //tipo_busqueda=1;
            observacionModel.CodUbicacion = "5";
            observacionModel.Lugar = "1";
            observacionModel.ObservadoPor = codPersona;

            if (mes.equals("00")) {
                observacionModel.FechaInicio = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                observacionModel.FechaFin = anio + "-" + "12" + "-" + "31";
            } else {
                observacionModel.FechaInicio = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                observacionModel.FechaFin = anio + "-" + mes + "-" + diaFin;
            }


            Gson gson = new Gson();
            json = gson.toJson(observacionModel);
            url = GlobalVariables.Url_base + "Observaciones/FiltroObservaciones";


        }else if(descripcion.equals("Inspecciones")){
            inspeccionModel = new InspeccionModel();
            InspeccionModel inspeccionModel = new InspeccionModel();
            //tipo_busqueda=1;
            inspeccionModel.Elemperpage = "5";
            inspeccionModel.Pagenumber = "1";
            inspeccionModel.CodTipo = codPersona;

            if (mes.equals("00")) {
                inspeccionModel.FechaP = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                inspeccionModel.Fecha = anio + "-" + "12" + "-" + "31";
            } else {
                inspeccionModel.FechaP = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                inspeccionModel.Fecha = anio + "-" + mes + "-" + diaFin;
            }

            Gson gson = new Gson();
            json = gson.toJson(inspeccionModel);

            url = GlobalVariables.Url_base + "Inspecciones/Filtroinspecciones";

        }else{
            //aqui va planes
        }

        GlobalVariables.flagUpSc=true;
        Utils.isActivity = true;
        GlobalVariables.listaGlobalFiltro = new ArrayList<>();

        final ActivityController obj = new ActivityController("post", url, BusqEstadistica.this);
        obj.execute(json);





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


                //Utils.observacionModel=new ObservacionModel();
                //ObservacionModel observacionModel=new ObservacionModel();
                //tipo_busqueda=1;
                String jsonR = "";

                if(descripcion.equals("Observaciones")) {

                    Utils.observacionModel.CodUbicacion = "5";
                    Utils.observacionModel.Lugar = "1";
                    Utils.observacionModel.ObservadoPor = codPersona;

                    if (mes.equals("00")) {
                        Utils.observacionModel.FechaInicio = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                        Utils.observacionModel.FechaFin = anio + "-" + "12" + "-" + "31";
                    } else {
                        Utils.observacionModel.FechaInicio = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                        Utils.observacionModel.FechaFin = anio + "-" + mes + "-" + diaFin;
                    }

                    Gson gson = new Gson();
                    jsonR = gson.toJson(Utils.observacionModel);
                    Utils.isActivity = true;
                    GlobalVariables.istabs = false;
                    url = GlobalVariables.Url_base + "Observaciones/FiltroObservaciones";

                }else if(descripcion.equals("Inspecciones")){

                    inspeccionModel.Elemperpage = "5";
                    inspeccionModel.Pagenumber = "1";
                    inspeccionModel.CodTipo = codPersona;

                    if (mes.equals("00")) {
                        Utils.inspeccionModel.FechaP = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                        Utils.inspeccionModel.Fecha = anio + "-" + "12" + "-" + "31";
                    } else {
                        Utils.inspeccionModel.FechaP = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                        Utils.inspeccionModel.Fecha = anio + "-" + mes + "-" + diaFin;
                    }

                    Gson gson = new Gson();
                    jsonR = gson.toJson(Utils.inspeccionModel);

                    Utils.isActivity = true;
                    GlobalVariables.istabs = false;
                    url = GlobalVariables.Url_base + "Inspecciones/Filtroinspecciones";

                }else {
                    // aqui va plaqn de accion
                }



                GlobalVariables.listaGlobalFiltro = new ArrayList<>();
                final ActivityController obj = new ActivityController("post", url, BusqEstadistica.this);
                obj.execute(jsonR);

/*
                //Utils.isActivity=true;
                GlobalVariables.istabs=false;
                final ActivityController obj = new ActivityController("post", url, Busqueda.this);
                obj.execute(json);
*/
                // Toast.makeText(rootView.getContext(),"swipe",Toast.LENGTH_SHORT).show();
                //  } },0);

            } });




        list_estadistica.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                        String json2 = "";

                        //GlobalVariables.count=5;
                        paginacion2+=1;




                        if(descripcion.equals("Observaciones")) {


                        Utils.observacionModel.CodUbicacion = "5";
                        Utils.observacionModel.Lugar = String.valueOf(paginacion2);
                        Utils.observacionModel.ObservadoPor=codPersona;
                        if(mes.equals("00")) {
                            Utils.observacionModel.FechaInicio = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                            Utils.observacionModel.FechaFin = anio + "-" + "12" + "-" + "31";
                        }else{
                            Utils.observacionModel.FechaInicio = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                            Utils.observacionModel.FechaFin = anio + "-" + mes + "-" + diaFin;
                        }



                        Gson gson = new Gson();
                        json2 = gson.toJson(Utils.observacionModel);



                        //GlobalVariables.isFragment=false;
                        //Utils.isActivity=true;
                        //url = GlobalVariables.Url_base + "Inspecciones/Filtroinspecciones";


                    }else if(descripcion.equals("Inspecciones")){

                        inspeccionModel.Elemperpage = "5";
                        inspeccionModel.Pagenumber = "1";
                        inspeccionModel.CodTipo = codPersona;

                        if (mes.equals("00")) {
                            Utils.inspeccionModel.FechaP = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                            Utils.inspeccionModel.Fecha = anio + "-" + "12" + "-" + "31";
                        } else {
                            Utils.inspeccionModel.FechaP = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                            Utils.inspeccionModel.Fecha = anio + "-" + mes + "-" + diaFin;
                        }

                        Gson gson = new Gson();
                        json2 = gson.toJson(Utils.inspeccionModel);
/*
                        Utils.isActivity = true;
                        GlobalVariables.istabs = false;
                        url = GlobalVariables.Url_base + "Inspecciones/Filtroinspecciones";
*/
                    }else {
                        // aqui va plaqn de accion
                    }






                    GlobalVariables.istabs=false;// para que no entre al flag de tabs
                        final ActivityController obj = new ActivityController("post", url, BusqEstadistica.this);
                        obj.execute(json2);


                        /*
                        layoutInflater =(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        popupView = layoutInflater.inflate(R.layout.popup_blanco, null);

                        popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT ,580,false);
                        popupWindow.showAtLocation(list_estadistica, Gravity.CENTER, 0, 0);
                        flagpopup=true;
*/


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





        list_estadistica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"Click en "+position,Toast.LENGTH_SHORT).show();


                if(descripcion.equals("Observaciones")) {
                    String CodObservacion=GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                    String tipoObs=GlobalVariables.listaGlobalFiltro.get(position).Tipo;

                    Intent intent = new Intent(BusqEstadistica.this, ActMuroDet.class);
                    intent.putExtra("codObs",CodObservacion);
                    intent.putExtra("posTab",0);
                    intent.putExtra("tipoObs",tipoObs);

                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                startActivity(intent);
                }else if(descripcion.equals("Inspecciones")){
                    String CodInspeccion= GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                    Intent intent = new Intent(BusqEstadistica.this, ActInspeccionDet.class);
                    intent.putExtra("codObs",CodInspeccion);
                    intent.putExtra("posTab",0);
                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                    startActivity(intent);

                }else {
                  //  String CodNoticia= GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                   // Intent intent = new Intent(Busqueda.this, ActNoticiaDet.class);
                    //intent.putExtra("codObs",CodNoticia);
                    //intent.putExtra("posTab",0);
                    //startActivity(intent);

                }


            }
        });







        btn_buscar_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.isActivity=true;
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;
                mes= (mes_pos < 10 ? "0" : "")+mes_pos;

                Calendar calFin = Calendar.getInstance();
                calFin.set(Integer.parseInt(anio), mes_pos-1, 1);
                calFin.set(Integer.parseInt(anio),mes_pos -1, calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
                //fechaFin = calFin.getTime();
                diaFin = (calFin.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "")+calFin.get(Calendar.DAY_OF_MONTH);



                String json = "";


                if(descripcion.equals("Observaciones")) {
                    Utils.observacionModel=new ObservacionModel();
                    ObservacionModel observacionModel=new ObservacionModel();
                    Utils.observacionModel.CodUbicacion = "5";
                    Utils.observacionModel.Lugar = "1";
                    Utils.observacionModel.ObservadoPor=codPersona;

                    if(mes.equals("00")) {
                        Utils.observacionModel.FechaInicio = anio_sel + "-" + "01" + "-" + "01";   //"2018-02-02"
                        Utils.observacionModel.FechaFin = anio_sel + "-" + "12" + "-" + "31";
                    }else{
                        Utils.observacionModel.FechaInicio = anio_sel + "-" + mes + "-" + "01";   //"2018-02-02"
                        Utils.observacionModel.FechaFin = anio_sel + "-" + mes + "-" + diaFin;
                    }
                    anio=anio_sel;
                    Gson gson = new Gson();
                    json = gson.toJson(Utils.observacionModel);

                    Utils.isActivity = true;
                    url = GlobalVariables.Url_base + "Observaciones/FiltroObservaciones";

                }else if(descripcion.equals("Inspecciones")){
                    Utils.inspeccionModel=new InspeccionModel();
                    //InspeccionModel inspeccionModel=new InspeccionModel();

                    Utils.inspeccionModel.Elemperpage="5";
                    Utils.inspeccionModel.Pagenumber="1";
                    inspeccionModel.CodTipo = codPersona;

                    Gson gson = new Gson();
                    json = gson.toJson(inspeccionModel);

                    Utils.isActivity = true;
                    url = GlobalVariables.Url_base + "Inspecciones/Filtroinspecciones";

/*
                    Intent intent = new Intent(Busqueda.this, B_inspecciones.class);
                    startActivityForResult(intent , REQUEST_CODE);
*/
                }else {
                    /*
                }
                    Utils.noticiasModel=new NoticiasModel();
                    NoticiasModel noticiasModel = new NoticiasModel();
                    tipo_busqueda = 3;
                    noticiasModel.Elemperpage = "5";
                    noticiasModel.Pagenumber = "1";

                    Gson gson = new Gson();
                    json = gson.toJson(noticiasModel);

                    Utils.isActivity = true;
                    url = GlobalVariables.Url_base + "Noticia/FiltroNoticias";
                    GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                    final ActivityController obj = new ActivityController("post", url, Busqueda.this);
                    obj.execute(json);
                */


            }

                GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                final ActivityController obj2 = new ActivityController("post", url, BusqEstadistica.this);
                obj2.execute(json);




            }});







    }

    public void close(View view){
        finish();
    }

    public int find(String anio){
        int pdata=0;
        for (int j=0;j<GlobalVariables.busqueda_anio.length;j++){
            if(anio.equals(GlobalVariables.busqueda_anio[j])){
                pdata=j;
            }
        }
        return pdata;
    }

    @Override
    public void success(String data, String Tipo) {

    }

    @Override
    public void successpost(String data, String Tipo) {

        data="{"+data+"}";
        Gson gson = new Gson();
        GetPublicacionModel getPublicacionModel = gson.fromJson(data, GetPublicacionModel.class);
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



       if(descripcion.equals("Observaciones")) {
           PublicacionAdapter ca = new PublicacionAdapter(this, GlobalVariables.listaGlobalFiltro);
           list_estadistica.setAdapter(ca);

       }else if(descripcion.equals("Inspecciones")){
           InspeccionAdapter ca = new InspeccionAdapter(this, GlobalVariables.listaGlobalFiltro);
           list_estadistica.setAdapter(ca);

       }else{
           // Aqui va plan de accion


       }





        if(GlobalVariables.flagUpSc==true){
            list_estadistica.setSelection(0);
            GlobalVariables.flagUpSc=false;
        }else
            //reemplazar el 100
            if(GlobalVariables.listaGlobalFiltro.size()>5&&GlobalVariables.listaGlobalFiltro.size()<contPublicacion) {
                //recListImag.smoothScrollToPosition(GlobalVariables.imagen2.size()-3);
                if(GlobalVariables.listaGlobalFiltro.size()%5==0) {
                    list_estadistica.setSelection(GlobalVariables.listaGlobalFiltro.size() - 6);
                }else{
                    list_estadistica.setSelection(GlobalVariables.listaGlobalFiltro.size()-1 );
                    //- GlobalVariables.listaGlobalFiltro.size()%5+1
                }

                flagObsFiltro=true;

            }else if(GlobalVariables.listaGlobalFiltro.size()==contPublicacion){
                list_estadistica.setSelection(GlobalVariables.listaGlobalFiltro.size());
                flagObsFiltro=false;

            }

        constraintLayout.setVisibility(View.GONE);
        btn_buscar_e.setEnabled(true);

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

    }
}
