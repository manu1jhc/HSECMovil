package com.pango.hsec.hsec.Ficha;

import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
//import com.pango.hsec.hsec.Busquedas.Busqueda;
import com.pango.hsec.hsec.Facilito.obsFacilitoDet;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.Verificaciones.ActVerificacionDet;
import com.pango.hsec.hsec.adapter.FichaInspecionAdapter;
import com.pango.hsec.hsec.adapter.FichaObsFacilitoAdapter;
import com.pango.hsec.hsec.adapter.FichaObservacionAdapter;
import com.pango.hsec.hsec.adapter.FichaPlanAdapter;
import com.pango.hsec.hsec.adapter.FichaVerificacionAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetObsFacilitoModel;
import com.pango.hsec.hsec.model.GetPlanMinModel;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.ObsFacilitoMinModel;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.model.PlanMinModel;
import com.pango.hsec.hsec.model.PublicacionModel;
import com.pango.hsec.hsec.model.VerificacionModel;

import java.util.ArrayList;
import java.util.Calendar;

import static com.pango.hsec.hsec.Utils.inspeccionModel;

public class BusqEstadistica extends AppCompatActivity implements IActivity {
    Spinner sp_anio, sp_mes;
    String anio,mes,codPersona,descripcion,aniop;
    int codselected;
    int tipo_busqueda;
    int contPublicacion;
    String url="";
    ListView list_estadistica;
    ImageView btn_buscar_e;
    String anio_sel="";
    int mes_pos;
    String diaFin;

    int paginacion2=1;
    int paginacion=1;
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
    TextView tipo_estadistica;
    FichaPlanAdapter pma;
    FichaObsFacilitoAdapter obfa;
    FichaInspecionAdapter inspa;
    FichaObservacionAdapter obsa;
    FichaVerificacionAdapter ver;
    String Elemperpage="7";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busq_estadistica);
        Bundle datos = getIntent().getExtras();
        anio= datos.getString("anio");
        final int mesActual=Integer.parseInt(datos.getString("mes"));
        mes= (mesActual < 10 ? "0" : "")+mesActual;
        codPersona=datos.getString("codiPersona");
        descripcion=datos.getString("descripcion");
        codselected=datos.getInt("codselected");
        tipo_estadistica=findViewById(R.id.tipo_estadistica);
        tipo_estadistica.setText(descripcion);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        constraintLayout=(ConstraintLayout) findViewById(R.id.const_main);
        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        tx_mensajeb=findViewById(R.id.tx_mensajeb);
        btn_buscar_e=findViewById(R.id.btn_buscar_e);
        tx_texto =(TextView) findViewById(R.id.tx_texto);

        list_estadistica=findViewById(R.id.list_estadistica);

        sp_anio=findViewById(R.id.spinner_anio);
        sp_mes=findViewById(R.id.spinner_mes);

        ArrayAdapter adapterAnio = new ArrayAdapter(this.getBaseContext(),R.layout.custom_spinner_item, GlobalVariables.busqueda_anio);
        adapterAnio.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
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

        ArrayAdapter adapterMes = new ArrayAdapter(this.getBaseContext(),R.layout.custom_spinner_item, GlobalVariables.busqueda_mes);
        adapterMes.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_mes.setAdapter(adapterMes);
        sp_mes.setSelection(Integer.parseInt(mes));
        sp_mes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //mes_sel

                mes_pos=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Date fechaFin;

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                tx_texto.setVisibility(View.VISIBLE);
                loadingTop=true;
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;
                flagObsFiltro=true;
                paginacion=2;
                getdada();

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
                    GlobalVariables.istabs=false;
                    int totalData=0;
                    if(codselected>0) totalData=GlobalVariables.listaGlobalFiltro.size();
                    else if(codselected==0)totalData=GlobalVariables.listaPlanMin.size();
                    else totalData=GlobalVariables.listaGlobalObsFacilito.size();

                    if (flag_enter && totalData != contPublicacion  ) {
                        flag_enter = false;
                        constraintLayout.setVisibility(View.VISIBLE);
                        String json2 = "";
                        paginacion2+=1;

                        if(codselected==12) {
                            Utils.verificacionModel.CodUbicacion = "5";
                            Utils.verificacionModel.Lugar = String.valueOf(paginacion2);
                            Utils.verificacionModel.ObservadoPor=codPersona;
                            if(mes.equals("00")) {
                                Utils.verificacionModel.FechaInicio = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                                Utils.verificacionModel.FechaFin = aniop + "-" + "12" + "-" + "31";
                            }else{
                                Utils.verificacionModel.FechaInicio = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                                Utils.verificacionModel.FechaFin = aniop + "-" + mes + "-" + diaFin;
                            }

                            Gson gson = new Gson();
                            json2 = gson.toJson(Utils.verificacionModel);

                            GlobalVariables.istabs=false;// para que no entre al flag de tabs
                            final ActivityController obj = new ActivityController("post-2", url, BusqEstadistica.this,BusqEstadistica.this);
                            obj.execute(json2,"1");

                        }else  if(codselected==1) {
                        Utils.observacionModel.CodUbicacion = "5";
                        Utils.observacionModel.Lugar = String.valueOf(paginacion2);
                        Utils.observacionModel.ObservadoPor=codPersona;
                        if(mes.equals("00")) {
                            Utils.observacionModel.FechaInicio = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                            Utils.observacionModel.FechaFin = aniop + "-" + "12" + "-" + "31";
                        }else{
                            Utils.observacionModel.FechaInicio = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                            Utils.observacionModel.FechaFin = aniop + "-" + mes + "-" + diaFin;
                        }

                        Gson gson = new Gson();
                        json2 = gson.toJson(Utils.observacionModel);

                            GlobalVariables.istabs=false;// para que no entre al flag de tabs
                            final ActivityController obj = new ActivityController("post-2", url, BusqEstadistica.this,BusqEstadistica.this);
                            obj.execute(json2,"1");

                    }else if(codselected==2){

                        inspeccionModel.Elemperpage = "5";
                        inspeccionModel.Pagenumber = String.valueOf(paginacion2);
                        inspeccionModel.CodTipo = codPersona;

                        if (mes.equals("00")) {
                            Utils.inspeccionModel.FechaP = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                            Utils.inspeccionModel.Fecha = aniop + "-" + "12" + "-" + "31";
                        } else {
                            Utils.inspeccionModel.FechaP = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                            Utils.inspeccionModel.Fecha = aniop + "-" + mes + "-" + diaFin;
                        }

                        Gson gson = new Gson();
                        json2 = gson.toJson(Utils.inspeccionModel);

                            GlobalVariables.istabs=false;// para que no entre al flag de tabs
                            final ActivityController obj = new ActivityController("post-2", url, BusqEstadistica.this,BusqEstadistica.this);
                            obj.execute(json2,"1");

                    }else if(codselected==0){
                        // aqui va plaqn de accion

                        if(Integer.parseInt(mes)==0) {
                            url = GlobalVariables.Url_base + "PlanAccion/GetPlanes?CodPersonaF="+codPersona+"&&Fecha=" + anio + "%7C"+ "&Pagenumber=" + paginacion2 + "&Elemperpage=" + "7";
                        }else{
                            url = GlobalVariables.Url_base + "PlanAccion/GetPlanes?CodPersonaF="+codPersona+"&&Fecha=" + anio + "%7C" + Integer.parseInt(mes) + "&Pagenumber=" + paginacion2 + "&Elemperpage=" + "7";
                        }
                        final ActivityController obj = new ActivityController("get-"+paginacion2, url, BusqEstadistica.this,BusqEstadistica.this);
                        obj.execute("1");
                        }
                      else{// codselected==-1  Reporte facilito
                            //aqui va planes
                            String anho="-";
                            if(!anio.equals("*")) anho=anio;
                            if(Integer.parseInt(mes)==0) {
                                url = GlobalVariables.Url_base + "ObsFacilito/GetObservacionFacFicha/"+codPersona+"/" + anho + "%7C/"+paginacion2+"/5";
                            }else{
                                url = GlobalVariables.Url_base + "ObsFacilito/GetObservacionFacFicha/"+codPersona+"/" + anho + "%7C"+mes+"/"+paginacion2+"/5";
                            }

                            final ActivityController obj = new ActivityController("get-"+paginacion2, url, BusqEstadistica.this,BusqEstadistica.this);
                            obj.execute("1");
                        }
                    }
                }
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    listenerFlag = true;
                    swipeRefreshLayout.setEnabled(false);
                    Log.d("started", "comenzo");
                }
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
                //GlobalVariables.istabs=false;

                if(codselected==1) {
                    String CodObservacion=GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                    String tipoObs=GlobalVariables.listaGlobalFiltro.get(position).Tipo;

                    Intent intent = new Intent(BusqEstadistica.this, ActMuroDet.class);
                    intent.putExtra("codObs",CodObservacion);
                    intent.putExtra("posTab",0);
                    intent.putExtra("tipoObs",tipoObs);

                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                startActivity(intent);
                }else if(codselected==2){
                    String CodInspeccion= GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                    Intent intent = new Intent(BusqEstadistica.this, ActInspeccionDet.class);
                    intent.putExtra("codObs",CodInspeccion);
                    intent.putExtra("posTab",0);
                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                    startActivity(intent);

                }else if(codselected==0){

                    boolean  pass=Integer.parseInt(GlobalVariables.listaPlanMin.get(position).Editable)>1?true:false;
                    String CodAccion= GlobalVariables.listaPlanMin.get(position).CodAccion;
                    Intent intent = new Intent(BusqEstadistica.this, PlanAccionDet.class);
                    intent.putExtra("codAccion",CodAccion);
                    intent.putExtra("jsonPlan","");
                    intent.putExtra("verBoton",pass);
                    intent.putExtra("proviene",3);
                    startActivity(intent);
                }else if(codselected==12) {
                    String CodVerificacion=GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                    String tipoVer=GlobalVariables.listaGlobalFiltro.get(position).Tipo;

                    Intent intent = new Intent(BusqEstadistica.this, ActVerificacionDet.class);
                    intent.putExtra("codVer",CodVerificacion);
                    intent.putExtra("posTab",0);
                    intent.putExtra("tipoVer",tipoVer);

                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                    startActivity(intent);
                } else{
                    String Codigo= GlobalVariables.listaGlobalObsFacilito.get(position).CodObsFacilito;
                    String Editable=GlobalVariables.listaGlobalObsFacilito.get(position).Editable;

                    Intent intent = new Intent(BusqEstadistica.this, obsFacilitoDet.class);
                    intent.putExtra("codObs",Codigo);
                    intent.putExtra("verBoton",Editable);
                    startActivity(intent);
                }
            }
        });

        btn_buscar_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.isActivity=true;
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;
                mes= (mes_pos < 10 ? "0" : "")+mes_pos;
                anio=anio_sel;
                paginacion=1;
                getdada();
            }});

        getdada();

    }

    public void getdada(){
        String json = "";
        GlobalVariables.listaGlobalFiltro = new ArrayList<>();
        GlobalVariables.listaPlanMin=new ArrayList<>();
        GlobalVariables.listaGlobalObsFacilito=new ArrayList<>();


        if(codselected==12){  ///verificaciones
            aniop=anio;
            if(anio.equals("*")) {
                anio="2000";
                aniop=GlobalVariables.busqueda_anio[1];
            }
            Utils.verificacionModel = new VerificacionModel();
            VerificacionModel verificacionModel = new VerificacionModel();
            //tipo_busqueda=1;
            verificacionModel.CodUbicacion =Elemperpage;
            verificacionModel.Lugar = "1";
            verificacionModel.ObservadoPor = codPersona;
            int mesActual=Integer.parseInt(mes);
            Calendar calFin = Calendar.getInstance();
            calFin.set(Integer.parseInt(anio), mesActual-1, 1);
            calFin.set(Integer.parseInt(anio),mesActual-1 , calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
            diaFin = (calFin.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "")+calFin.get(Calendar.DAY_OF_MONTH);
            if (mes.equals("00")) {
                verificacionModel.FechaInicio = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                verificacionModel.FechaFin = aniop + "-" + "12" + "-" + "31";
            } else {
                verificacionModel.FechaInicio = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                verificacionModel.FechaFin = aniop + "-" + mes + "-" + diaFin;
            }


            Gson gson = new Gson();
            json = gson.toJson(verificacionModel);
            url = GlobalVariables.Url_base + "Verificacion/Filtro";
            GlobalVariables.flagUpSc=true;
            Utils.isActivity = true;

            final ActivityController obj = new ActivityController("post-"+paginacion, url, BusqEstadistica.this,this);
            obj.execute(json);


        }else if(codselected==1 ) {  // Observcacion
            aniop=anio;
            if(anio.equals("*")) {
                anio="2000";
                aniop=GlobalVariables.busqueda_anio[1];
            }
            Utils.observacionModel = new ObservacionModel();
            ObservacionModel observacionModel = new ObservacionModel();
            //tipo_busqueda=1;
            observacionModel.CodUbicacion =Elemperpage;
            observacionModel.Lugar = "1";
            observacionModel.ObservadoPor = codPersona;
            int mesActual=Integer.parseInt(mes);
            Calendar calFin = Calendar.getInstance();
            calFin.set(Integer.parseInt(anio), mesActual-1, 1);
            calFin.set(Integer.parseInt(anio),mesActual-1 , calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
            diaFin = (calFin.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "")+calFin.get(Calendar.DAY_OF_MONTH);
            if (mes.equals("00")) {
                observacionModel.FechaInicio = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                observacionModel.FechaFin = aniop + "-" + "12" + "-" + "31";
            } else {
                observacionModel.FechaInicio = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                observacionModel.FechaFin = aniop + "-" + mes + "-" + diaFin;
            }


            Gson gson = new Gson();
            json = gson.toJson(observacionModel);
            url = GlobalVariables.Url_base + "Observaciones/FiltroObservaciones";
            GlobalVariables.flagUpSc=true;
            Utils.isActivity = true;

            final ActivityController obj = new ActivityController("post-"+paginacion, url, BusqEstadistica.this,this);
            obj.execute(json);

        }else if(codselected==2){  // Inspeccion
            aniop=anio;
            if(anio.equals("*")) {
                anio="2000";
                aniop=GlobalVariables.busqueda_anio[1];
            }
            inspeccionModel = new InspeccionModel();
            InspeccionModel inspeccionModel = new InspeccionModel();
            //tipo_busqueda=1;
            inspeccionModel.Elemperpage = Elemperpage;
            inspeccionModel.Pagenumber = "1";
            inspeccionModel.CodTipo = codPersona;

            int mesActual=Integer.parseInt(mes);
            Calendar calFin = Calendar.getInstance();
            calFin.set(Integer.parseInt(anio), mesActual-1, 1);
            calFin.set(Integer.parseInt(anio),mesActual-1 , calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
            diaFin = (calFin.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "")+calFin.get(Calendar.DAY_OF_MONTH);

            if (mes.equals("00")) {
                inspeccionModel.FechaP = anio + "-" + "01" + "-" + "01";   //"2018-02-02"
                inspeccionModel.Fecha = aniop + "-" + "12" + "-" + "31";
            } else {
                inspeccionModel.FechaP = anio + "-" + mes + "-" + "01";   //"2018-02-02"
                inspeccionModel.Fecha = aniop + "-" + mes + "-" + diaFin;
            }

            Gson gson = new Gson();
            json = gson.toJson(inspeccionModel);

            url = GlobalVariables.Url_base + "Inspecciones/Filtroinspecciones";

            // GlobalVariables.flagUpSc=true;
            // Utils.isActivity = true;
            GlobalVariables.listaGlobalFiltro = new ArrayList<>();

            final ActivityController obj = new ActivityController("post-"+paginacion, url, BusqEstadistica.this,this);
            obj.execute(json);



        }else if(codselected==0){//PLANES DE ACCION
            //aqui va planes

            if(mes.equals("00")) {
                url = GlobalVariables.Url_base + "PlanAccion/GetPlanes?CodPersonaF="+codPersona+"&&Fecha=" + anio + "%7C"+ "&Pagenumber=" + "1" + "&Elemperpage=" + Elemperpage;
            }else{
                url = GlobalVariables.Url_base + "PlanAccion/GetPlanes?CodPersonaF="+codPersona+"&&Fecha=" + anio + "%7C" + mes + "&Pagenumber=" + "1" + "&Elemperpage=" + Elemperpage;
            }

            final ActivityController obj = new ActivityController("get-"+paginacion, url, BusqEstadistica.this,this);
            obj.execute("");
        }
        else{// codselected==-1  Reporte facilito
            //aqui va planes
            String anho="-";
            if(!anio.equals("*")) anho=anio;
            if(mes.equals("00")) {
                url = GlobalVariables.Url_base + "ObsFacilito/GetObservacionFacFicha/"+codPersona+"/" + anho + "%7C/1/"+Elemperpage;
            }else{
                url = GlobalVariables.Url_base + "ObsFacilito/GetObservacionFacFicha/"+codPersona+"/" + anho + "%7C"+mes+"/1/"+Elemperpage;
            }

            final ActivityController obj = new ActivityController("get-"+paginacion, url, BusqEstadistica.this,this);
            obj.execute("");
        }
    }

    public void close(View view){
        finish();
    }
    @Override
    public void onBackPressed() {
        boolean passdismis=true;
        try {
           if(obsa!=null && obsa.popupWindow!=null && obsa.popupWindow.isShowing()) obsa.popupWindow.dismiss();
           else if(inspa!=null && inspa.popupWindow!=null && inspa.popupWindow.isShowing()) inspa.popupWindow.dismiss();
           else if(obfa!=null && obfa.popupWindow!=null && obfa.popupWindow.isShowing()) obfa.popupWindow.dismiss();
           else if(pma!=null && pma.popupWindow!=null && pma.popupWindow.isShowing()) pma.popupWindow.dismiss();
           else passdismis=false;
           if(!passdismis) finish();
        }catch (Throwable e){
            Log.d("error_frag", e.getLocalizedMessage());
        }
    }

    public void DeleteObject(String Url, int index){
        String url= GlobalVariables.Url_base+Url;
        ActivityController obj = new ActivityController("get", url, this,this);
        obj.execute(""+index);
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
        Gson gson = new Gson();
        if(Tipo.equals("1")) // adapter add
        {
            if(codselected==0){
                GetPlanMinModel getPlanMinModel = gson.fromJson(data, GetPlanMinModel.class);
                for(PlanMinModel item:getPlanMinModel.Data)
                    pma.add(item);
                pma.notifyDataSetChanged();
            }
            else{
                GetObsFacilitoModel getPlanMinModel = gson.fromJson(data, GetObsFacilitoModel.class);
                for(ObsFacilitoMinModel item:getPlanMinModel.Data)
                    obfa.add(item);
                obfa.notifyDataSetChanged();
            }
        }
        else if(Tipo.equals("")){ // re create adapter and reset pagination
            paginacion2=1;

            if(codselected==0){ // pland de accion
                GetPlanMinModel getPlanMinModel = gson.fromJson(data, GetPlanMinModel.class);
                contPublicacion=getPlanMinModel.Count;
                GlobalVariables.listaPlanMin.clear();
                GlobalVariables.listaPlanMin=getPlanMinModel.Data;
                if(contPublicacion>0){
                    pma = new FichaPlanAdapter(this, GlobalVariables.listaPlanMin);
                    list_estadistica.setAdapter(pma);
                }
            }
            else{  // observacion facilito
                GetObsFacilitoModel getPlanMinModel = gson.fromJson(data, GetObsFacilitoModel.class);
                contPublicacion=getPlanMinModel.Count;
                GlobalVariables.listaGlobalObsFacilito.clear();
                GlobalVariables.listaGlobalObsFacilito=getPlanMinModel.Data;
                if(contPublicacion>0){
                    obfa = new FichaObsFacilitoAdapter(this, GlobalVariables.listaGlobalObsFacilito);
                    list_estadistica.setAdapter(obfa);
                }
            }

            if(contPublicacion>0){
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                tx_mensajeb.setVisibility(View.GONE);
            }
            else {
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                tx_mensajeb.setVisibility(View.VISIBLE);
            }
            tipo_estadistica.setText(descripcion+" ("+contPublicacion+")");
        }
        else
        {   if(data.contains("-1")) Toast.makeText(this, "Ocurrio un error al eliminar registro.",    Toast.LENGTH_SHORT).show();
            else {
                int index=Integer.parseInt(Tipo)-2;
                switch (codselected){
                    case -1:
                        obfa.remove(index);
                    break;
                    case 0:
                        pma.remove(index);
                        break;
                    case 1:
                        obsa.remove(index);
                        break;
                    case 2:
                        inspa.remove(index);
                        break;
                }
            }
        }
        constraintLayout.setVisibility(View.GONE);
        btn_buscar_e.setEnabled(true);

        flag_enter=true;

        if(loadingTop)
        {
            loadingTop=false;
            swipeRefreshLayout.setRefreshing(false);
            tx_texto.setVisibility(View.GONE);
            swipeRefreshLayout.setEnabled( false );
        }

    }

    @Override
    public void successpost(String data, String Tipo) {

        Gson gson = new Gson();
        GetPublicacionModel getPublicacionModel = gson.fromJson(data, GetPublicacionModel.class);
        contPublicacion=getPublicacionModel.Count;
        if(Tipo.equals("1")) // adapter add
        {

                for(PublicacionModel item:getPublicacionModel.Data)
                {
                    if(codselected==1)  obsa.add(item);
                    else if(codselected==12) ver.add(item);
                    else inspa.add(item);
                }
            if(codselected==1 || codselected==12)  obsa.notifyDataSetChanged();
            else if(codselected==12) ver.notifyDataSetChanged();
            else inspa.notifyDataSetChanged();
        }
        else{ // re create adapter and reset pagination
            paginacion2=1;
            boolean passcount=true;
            GlobalVariables.listaGlobalFiltro.clear();
            GlobalVariables.listaGlobalFiltro=getPublicacionModel.Data;

            if(contPublicacion>0){
                if(codselected==1)   {
                    obsa = new FichaObservacionAdapter(this, GlobalVariables.listaGlobalFiltro);
                    list_estadistica.setAdapter(obsa);
                } else if(codselected==12){
                    ver = new FichaVerificacionAdapter(this, GlobalVariables.listaGlobalFiltro);
                    list_estadistica.setAdapter(ver);
                }
                else {
                    inspa = new FichaInspecionAdapter(this, GlobalVariables.listaGlobalFiltro);
                    list_estadistica.setAdapter(inspa);
                }
            }

            if(contPublicacion>0){
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                tx_mensajeb.setVisibility(View.GONE);
            }
            else {
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                tx_mensajeb.setVisibility(View.VISIBLE);
            }
            tipo_estadistica.setText(descripcion+" ("+contPublicacion+")");

        }
        constraintLayout.setVisibility(View.GONE);
        btn_buscar_e.setEnabled(true);

        flag_enter=true;

        if(loadingTop)
        {
            loadingTop=false;
            swipeRefreshLayout.setRefreshing(false);
            tx_texto.setVisibility(View.GONE);
            swipeRefreshLayout.setEnabled( false );
        }
    }

    @Override
    public void error(String mensaje, String Tipo) {
        if(flagpopup){
            popupWindow.dismiss();
            flagpopup=false;
        }
        constraintLayout.setVisibility(View.GONE);
        paginacion-=1;
        flag_enter=true;

        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 3) {
                if(resultCode == this.RESULT_OK){
                    try {
                        Gson gson = new Gson();
                        PlanMinModel plan= gson.fromJson(data.getStringExtra("planaccion"), PlanMinModel.class);
                        pma.replace(plan);
                    } catch (Exception ex) {
                        Toast.makeText(this, ex.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                if (resultCode == this.RESULT_CANCELED) {
                }
            }
    }


}
