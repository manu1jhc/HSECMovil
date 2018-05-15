package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.pango.hsec.hsec.Ficha.BusqEstadistica;
import com.pango.hsec.hsec.Ficha.PlanAccionDet;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.PlanMinAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPlanMinModel;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.model.PlanMinModel;
import com.pango.hsec.hsec.model.UsuarioModel;

import java.util.ArrayList;
import java.util.Calendar;

import static com.pango.hsec.hsec.Utils.inspeccionModel;


/**
 * A simple {@link FragmentPlanPendiente} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPlanPendiente.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPlanPendiente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlanPendiente extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    UsuarioModel getUsuarioModel;
    View rootView;
    Spinner sp_anio, sp_mes;
    int anioActual,mesActual,diaActual;
    String anio,mes,codPersona,descripcion;
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
    public PlanMinAdapter pma;


    public FragmentPlanPendiente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPlanPendiente newInstance(String param1, String param2) {
        FragmentPlanPendiente fragment = new FragmentPlanPendiente();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_planpendiente, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout);
        constraintLayout=(ConstraintLayout) rootView.findViewById(R.id.const_main);
        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        tx_mensajeb=rootView.findViewById(R.id.tx_mensajeb);
        btn_buscar_e=rootView.findViewById(R.id.btn_buscar_e);
        tx_texto =(TextView) rootView.findViewById(R.id.tx_texto);

        list_estadistica=rootView.findViewById(R.id.list_estadistica);
        tipo_estadistica=rootView.findViewById(R.id.tipo_estadistica);

        sp_anio=rootView.findViewById(R.id.spinner_anio);
        sp_mes=rootView.findViewById(R.id.spinner_mes);

        Gson gson = new Gson();
        getUsuarioModel = gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);


        Calendar fechaHoy = Calendar.getInstance();
        anioActual=fechaHoy.get(Calendar.YEAR);
        anio= String.valueOf(anioActual);

        mesActual=fechaHoy.get(Calendar.MONTH)+ 1;
        mes= (mesActual < 10 ? "0" : "")+mesActual;

        codPersona=getUsuarioModel.CodPersona;

        if(Integer.parseInt(anio)<2018) {
            anio=2018+"";
        }
        //int inc=0;
        GlobalVariables.busqueda_anio = new String[Integer.parseInt(anio) - 2014 + 1];

        for (int i = 0; i <= Integer.parseInt(anio) - 2014; i++) {
            GlobalVariables.busqueda_anio[i] = String.valueOf(Integer.parseInt(anio) - i);
            //inc+=1;
        }

        ArrayAdapter adapterAnio = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.busqueda_anio);
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

        ArrayAdapter adapterMes = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.busqueda_mes);
        adapterMes.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_mes.setAdapter(adapterMes);
        sp_mes.setSelection(mesActual);
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                tx_texto.setVisibility(View.VISIBLE);
                loadingTop=true;
                paginacion=2;
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;
                getdata();

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
                    // GlobalVariables.FDown=true;
                    //Toast.makeText(rootView.getContext(), "ACEPTO DOWNFLAG", Toast.LENGTH_SHORT).show();
                    /// cambiar el 100 por el total de publicaciones
                    if (GlobalVariables.listaPlanMin.size() != contPublicacion && flag_enter) {

                        flag_enter = false;
                        constraintLayout.setVisibility(View.VISIBLE);

                        paginacion2+=1;

                            if(mes.equals("00")) {
                                url = GlobalVariables.Url_base + "PlanAccion/GetPlanes?CodPersonaF="+codPersona+"&&Fecha=p" + anio + "%7C"+ "&Pagenumber=" + paginacion2 + "&Elemperpage=" + "7";
                            }else{
                                url = GlobalVariables.Url_base + "PlanAccion/GetPlanes?CodPersonaF="+codPersona+"&&Fecha=p" + anio + "%7C" + mes + "&Pagenumber=" + paginacion2 + "&Elemperpage=" + "7";
                            }
                            GlobalVariables.isFragment=true;
                            final ActivityController obj = new ActivityController("get-"+paginacion2, url, FragmentPlanPendiente.this,getActivity());
                            obj.execute("1");
                       // }
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

                    boolean  pass=Integer.parseInt(GlobalVariables.listaPlanMin.get(position).Editable)>1?true:false;

                    String CodAccion= GlobalVariables.listaPlanMin.get(position).CodAccion;
                    Intent intent = new Intent(getActivity(), PlanAccionDet.class);
                    intent.putExtra("codAccion",CodAccion);
                    intent.putExtra("jsonPlan","");
                    intent.putExtra("verBoton",pass);
                    startActivity(intent);
                //}
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
                getdata();
            }});
        getdata();
        return rootView;

    }

    public void getdata(){
        GlobalVariables.listaPlanMin.clear();
        paginacion2=1;
        if(mes.equals("00")) {
            url = GlobalVariables.Url_base + "PlanAccion/GetPlanes?CodPersonaF="+codPersona+"&Fecha=p" + anio + "%7C"+ "&Pagenumber=" + "1" + "&Elemperpage=" + "7";
        }else{
            url = GlobalVariables.Url_base + "PlanAccion/GetPlanes?CodPersonaF="+codPersona+"&Fecha=p" + anio + "%7C" + mes + "&Pagenumber=" + "1" + "&Elemperpage=" + "7";
        }

        GlobalVariables.listaPlanMin=new ArrayList<>();
        final ActivityController obj = new ActivityController("get-"+paginacion, url, FragmentPlanPendiente.this,getActivity());
        obj.execute("");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void DeleteObject(String Url, int index){
        String url= GlobalVariables.Url_base+Url;
        ActivityController obj = new ActivityController("get", url, FragmentPlanPendiente.this,getActivity());
        obj.execute(""+index);
    }

    @Override
    public void success(String data, String Tipo) {

        Gson gson = new Gson();
        if(Tipo.equals("")){  // new adapter
            GetPlanMinModel getPlanMinModel = gson.fromJson(data, GetPlanMinModel.class);
            contPublicacion=getPlanMinModel.Count;
            tipo_estadistica.setText("Planes de acción pendientes"+" ("+contPublicacion+")");
            GlobalVariables.listaPlanMin=getPlanMinModel.Data;
            pma = new PlanMinAdapter(getActivity(), GlobalVariables.listaPlanMin,this);
            list_estadistica.setAdapter(pma);

            btn_buscar_e.setEnabled(true);

            flag_enter=true;
            if(loadingTop)
            {
                loadingTop=false;
                swipeRefreshLayout.setRefreshing(false);
                tx_texto.setVisibility(View.GONE);
                swipeRefreshLayout.setEnabled( false );
            }
            if(contPublicacion>0){
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                tx_mensajeb.setVisibility(View.GONE);
            }
            else {
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                tx_mensajeb.setVisibility(View.VISIBLE);
            }

        }
        else if(Tipo.equals("1")){  // add adapter
            constraintLayout.setVisibility(View.GONE);
            GetPlanMinModel getPlanMinModel = gson.fromJson(data, GetPlanMinModel.class);
            for(PlanMinModel item:getPlanMinModel.Data)
                pma.add(item);
            pma.notifyDataSetChanged();
        }
        else{
            if(data.contains("-1")) Toast.makeText(getActivity(), "Ocurrio un error al eliminar registro.",    Toast.LENGTH_SHORT).show();
            else pma.remove(Integer.parseInt(Tipo)-2);
        }



       /* if(GlobalVariables.listaPlanMin.size()==0) {
            GlobalVariables.listaPlanMin = getPlanMinModel.Data;
            if(getPlanMinModel.Data.size()==0){

            }else{
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                tx_mensajeb.setVisibility(View.GONE);
            }
            //swipeRefreshLayout.setVisibility(View.VISIBLE);

            //GlobalVariables.listaGlobal=listaPublicaciones;
        }else{
            //listaPublicaciones.addAll(getPublicacionModel.Data);
            GlobalVariables.listaPlanMin.addAll(getPlanMinModel.Data);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }

        if(GlobalVariables.flagUpSc==true){
            list_estadistica.setSelection(0);
            GlobalVariables.flagUpSc=false;
        }else if(GlobalVariables.listaPlanMin.size()==7){
            list_estadistica.setSelection(0);
        }else if(GlobalVariables.listaPlanMin.size()>7&&GlobalVariables.listaPlanMin.size()<contPublicacion) {
                //recListImag.smoothScrollToPosition(GlobalVariables.imagen2.size()-3);
                if(GlobalVariables.listaPlanMin.size()%7==0) {
                    list_estadistica.setSelection(GlobalVariables.listaPlanMin.size() - 8);
                }else{
                    list_estadistica.setSelection(GlobalVariables.listaPlanMin.size()-1 );
                    //- GlobalVariables.listaGlobalFiltro.size()%5+1
                }

                flagObsFiltro=true;

            }else if(GlobalVariables.listaPlanMin.size()==contPublicacion){
            list_estadistica.setSelection((GlobalVariables.listaPlanMin.size()/7)*7-1);
                flagObsFiltro=false;

            }*/
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

}
