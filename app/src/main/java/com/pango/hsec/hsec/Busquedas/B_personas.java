package com.pango.hsec.hsec.Busquedas;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Observaciones.FragmentComent;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.BuscarPersonaAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.GetPersonaModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.PlanModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class B_personas extends AppCompatActivity implements IActivity {
    ArrayList<Maestro> gerenciadata;
    ArrayList<Maestro> superintdata;

    Spinner spinnerGerencia,spinnerSuperInt;
    TextView tx_b_persona;
    ConstraintLayout const_persona;
    Button btn_busqueda;
    String gerencia,superint,filtro;
    EditText id_apellidos,id_nombre,id_dni;
    String url="";
    ArrayAdapter adapterGerencia,adapterSuperInt;
    int contPublicacion;
    ListView List_personas;
    GetPersonaModel getPersonaModel;
    boolean upFlag;
    boolean downFlag;
    boolean listenerFlag;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean flag_enter=true;
    ConstraintLayout constraintLayout;
    boolean flagPersonaFiltro=true;
    //ConstraintLayout constraintLayout;
    boolean loadingTop=false;
    TextView tx_texto;
    int paginacion=1;

    //int first_spinner = 0, first_spinner_counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_personas);
        tx_b_persona=(TextView) findViewById(R.id.tx_b_persona);
        const_persona=(ConstraintLayout) findViewById(R.id.const_persona);
        btn_busqueda=(Button) findViewById(R.id.btn_busqueda);
        id_apellidos=(EditText) findViewById(R.id.id_apellidos);
        id_nombre=(EditText) findViewById(R.id.id_nombre);
        id_dni=(EditText) findViewById(R.id.id_dni);
        List_personas=findViewById(R.id.listView);
        tx_texto =(TextView) findViewById(R.id.tx_texto);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        constraintLayout=(ConstraintLayout) findViewById(R.id.const_main);

        swipeRefreshLayout.setVisibility(View.INVISIBLE);


        spinnerGerencia=(Spinner) findViewById(R.id.spinner_gerencia);
        spinnerSuperInt=(Spinner) findViewById(R.id.spinner_superint);
        //spinnerGerencia.setOnItemSelectedListener(this);
        gerenciadata= new ArrayList<>();
        gerenciadata.add(new Maestro("","-  Seleccione  -"));
        gerenciadata.addAll(GlobalVariables.Gerencia);

        superintdata=new ArrayList<>();
        superintdata.add(new Maestro("","-  Seleccione  -"));
//        superintdata.addAll(GlobalVariables.SuperIntendencia);


        adapterGerencia = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, gerenciadata);
        adapterGerencia.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

        adapterSuperInt = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSuperInt.setAdapter(adapterSuperInt);


        spinnerGerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /*Maestro Geren = (Maestro) ( (Spinner) view.findViewById(R.id.spinner_gerencia) ).getSelectedItem();
                gerencia=Geren.CodTipo;*/

                superint="";
                gerencia = gerenciadata.get(position).CodTipo;
                superintdata.clear();
                for (Maestro item: GlobalVariables.loadSuperInt(gerencia)
                        ) {
                    superintdata.add(item);
                }
                adapterSuperInt.notifyDataSetChanged();
                spinnerSuperInt.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gerencia="";
            }
        });


        spinnerSuperInt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(position!=0) {
                    superint = superintdata.get(position).CodTipo.split("\\.")[1];
                }else{
                    superint="";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                superint="";
            }
        });

        tx_b_persona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(const_persona.getVisibility()==View.GONE){
                    const_persona.setVisibility(View.VISIBLE);
                }else{
                    const_persona.setVisibility(View.GONE);
                }
            }
        });
        //busqueda
        String url2;
        btn_busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                GlobalVariables.lista_Personas=new ArrayList<>();
                filtro= Utils.ChangeUrl(String.valueOf(String.valueOf(id_nombre.getText())+"@"+id_apellidos.getText())+"@"+String.valueOf(id_dni.getText())
                +"@"+gerencia+"@"+superint);

                url= GlobalVariables.Url_base+"Usuario/FiltroPersona/"+filtro+"/1/7";
                //url="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/Usuario/FiltroPersona/@@@@/1/5";
                Utils.isActivity=true;
                final ActivityController obj = new ActivityController("get", url, B_personas.this);
                obj.execute("");


            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {
                paginacion=1;
                flagPersonaFiltro=true;
                //is_swipe=false;
                swipeRefreshLayout.setRefreshing(true);
                tx_texto.setVisibility(View.VISIBLE);
                //   upFlag=false;
                //  downFlag=false;
                // (new Handler()).postDelayed(new Runnable() {
                //    @Override
                //    public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadingTop=true;
                tx_texto.setVisibility(View.VISIBLE);
                GlobalVariables.lista_Personas.clear();
                GlobalVariables.contpublic=2;
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;
                GlobalVariables.isFragment=true;


                url= GlobalVariables.Url_base+"Usuario/FiltroPersona/"+filtro+"/"+paginacion+"/"+"7";
                //url=GlobalVariables.Url_base+"Observaciones/GetOBservaciones/-/"+1+"/"+GlobalVariables.num_items;
                GlobalVariables.count=5;
                final ActivityController obj = new ActivityController("get", url, B_personas.this);
                obj.execute("");
                // Toast.makeText(rootView.getContext(),"swipe",Toast.LENGTH_SHORT).show();

                //  } },0);





            } });





        List_personas.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    if (GlobalVariables.lista_Personas.size() != contPublicacion && flag_enter&&flagPersonaFiltro) {

                        //progressBarMain.setVisibility(View.VISIBLE);
                        flag_enter = false;
                        constraintLayout.setVisibility(View.VISIBLE);
                        GlobalVariables.isFragment=true;
                        paginacion+=1;
                        //Utils.isActivity=true;
                        //url= GlobalVariables.Url_base+"Usuario/FiltroPersona/"+filtro+"/1/7";

                        url =GlobalVariables.Url_base+"Usuario/FiltroPersona/"+filtro+"/"+paginacion + "/" + "7";

                        GlobalVariables.count=5;
                        final ActivityController obj = new ActivityController("get", url, B_personas.this);
                        obj.execute("");

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


    }
    public void close(View view){
        finish();
    }

    @Override
    public void success(String data,String Tipo) {
        closeSoftKeyBoard();
        Gson gson = new Gson();
        getPersonaModel = gson.fromJson(data, GetPersonaModel.class);
        contPublicacion=getPersonaModel.Count;

        if(GlobalVariables.lista_Personas.size()==0) {
            GlobalVariables.lista_Personas = getPersonaModel.Data;
            //GlobalVariables.listaGlobal=listaPublicaciones;
        }else{
            //listaPublicaciones.addAll(getPublicacionModel.Data);
            GlobalVariables.lista_Personas.addAll(getPersonaModel.Data);
        }
        BuscarPersonaAdapter ca = new BuscarPersonaAdapter(this,GlobalVariables.lista_Personas);
        List_personas.setAdapter(ca);



        ca.notifyDataSetChanged();
        if(GlobalVariables.flagUpSc==true){
            List_personas.setSelection(0);
            GlobalVariables.flagUpSc=false;
        }else
            //reemplazar el 100
            if(GlobalVariables.lista_Personas.size()>7&&GlobalVariables.lista_Personas.size()<contPublicacion) {
                //recListImag.smoothScrollToPosition(GlobalVariables.imagen2.size()-3);
                List_personas.setSelection(GlobalVariables.lista_Personas.size()-8);
                flagPersonaFiltro=true;

            }else if(GlobalVariables.lista_Personas.size()==contPublicacion){
                List_personas.setSelection(GlobalVariables.lista_Personas.size()-1);
                flagPersonaFiltro=false;

            }

        constraintLayout.setVisibility(View.GONE);


        flag_enter=true;
        GlobalVariables.contpublic += 1;
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









        List_personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String nombre=GlobalVariables.lista_Personas.get(position).Nombres;
                String CodPersona=GlobalVariables.lista_Personas.get(position).CodPersona;

                /*Intent intent = new Intent(B_personas.this, B_observaciones.class);
                intent.putExtra("nombreP",nombre);
                intent.putExtra("codpersona",CodPersona);

                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                startActivity(intent);*/

                Intent intent = getIntent();
                intent.putExtra("nombreP",nombre);
                intent.putExtra("codpersona",CodPersona);
                intent.putExtra("tipo","persona");

                setResult(RESULT_OK, intent);
                finish();
            }
        });





    }

    @Override
    public void successpost(String data,String Tipo) {

    }

    @Override
    public void error(String mensaje,String Tipo) {

    }

    public void closeSoftKeyBoard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
