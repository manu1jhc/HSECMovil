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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.DetalleMACuasi.MACuasiDetalle;
import com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.IngresosMA.ActIngresoMA;
import com.pango.hsec.hsec.CuasiAccidente.Seguridad.DetalleSeguridad.ActSeguridad;
import com.pango.hsec.hsec.CuasiAccidente.Seguridad.IngresosSeguridad.ActIngresoSeg;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.MainActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.MACuasiAccidenteAdapter;
import com.pango.hsec.hsec.adapter.SeguridadCAAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.MACuasiAccidenteModel;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.util.ArrayList;

import static com.pango.hsec.hsec.MainActivity.flag_maCuasi;
import static com.pango.hsec.hsec.MainActivity.flag_seguri;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSecuridadCA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSecuridadCA extends Fragment implements IActivity {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    String tipo_filtro="";
    Button btn_filtro;
    Button add_sec;
    public static final int REQUEST_CODE = 1;
    String url="";
    public ListView list_busqueda;
    static int paginacion2=1;
    boolean flagSECFiltro=true;
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
    public SeguridadCAAdapter ca;
    ConstraintLayout linear_total;
    Button btn_eliminarf;
    TextView tx_filtro;
    String Elemperpage="7";

    private FragmentSecuridadCA.OnFragmentInteractionListener mListener;

    public FragmentSecuridadCA() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentSecuridadCA newInstance(String param1, String param2) {
        FragmentSecuridadCA fragment = new FragmentSecuridadCA();
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
        rootView = inflater.inflate(R.layout.fragment_securidad_ca, container, false);
        // Inflate the layout for this fragment
        add_sec = rootView.findViewById(R.id.add_seg);

        tx_texto = (TextView) rootView.findViewById(R.id.tx_texto);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout);
        constraintLayout = (ConstraintLayout) rootView.findViewById(R.id.const_main);
        lupabuscar = (ImageView) rootView.findViewById(R.id.lupabuscar);
        GlobalVariables loaddata = new GlobalVariables();
        list_busqueda = (ListView) rootView.findViewById(R.id.list_busqueda);
        //sp_busqueda=(Spinner) rootView.findViewById(R.id.sp_busqueda);
        tx_mensajeb = rootView.findViewById(R.id.tx_mensajeb);
        btn_filtro = (Button) rootView.findViewById(R.id.btn_filtro);
        linear_total = rootView.findViewById(R.id.linear_total);
        btn_eliminarf = rootView.findViewById(R.id.btn_eliminarf);
        tx_filtro = rootView.findViewById(R.id.tx_filtro);

        url = GlobalVariables.Url_base + "Observaciones/FiltroObservaciones";
        if (GlobalVariables.listaGlobalSeguridadMA.size() == 0) {

            Utils.maCuasiAccidenteModel = new MACuasiAccidenteModel();
            MACuasiAccidenteModel maCuasiAccidenteModel = new MACuasiAccidenteModel();
            tipo_busqueda = 1;
            maCuasiAccidenteModel.CodUbicacion = Elemperpage;
            maCuasiAccidenteModel.Lugar = "1";
            String json = "";
            Gson gson = new Gson();
            json = gson.toJson(maCuasiAccidenteModel);

            Utils.isActivity = true;
            final ActivityController obj = new ActivityController("post-" + paginacion2, url, FragmentSecuridadCA.this, getActivity());
            obj.execute(json, "0");

        } else {
            successpost("", "-1");
        }

        btn_eliminarf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_total.setVisibility(View.GONE);
                flag_seguri =false;
                GlobalVariables.listaGlobalSeguridadMA.clear();

                Utils.maCuasiAccidenteModel = new MACuasiAccidenteModel();
                MACuasiAccidenteModel maCuasiAccidenteModel = new MACuasiAccidenteModel();
                tipo_busqueda = 1;
                maCuasiAccidenteModel.CodUbicacion = Elemperpage;
                maCuasiAccidenteModel.Lugar = "1";
                String json = "";

                Gson gson = new Gson();
                json = gson.toJson(maCuasiAccidenteModel);

                Utils.isActivity = true;
                //GlobalVariables.listaGlobalInspeccion = new ArrayList<>();

                final ActivityController obj = new ActivityController("post", url, FragmentSecuridadCA.this, getActivity());
                obj.execute(json);
            }
        });

        add_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.ObjectEditable=false;
                Intent addSecCA = new Intent(getActivity(), ActIngresoSeg.class);
                addSecCA.putExtra("codObs","INC000000XYZ");
                startActivity(addSecCA);
            }
        });

        btn_filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*               Intent intent = new Intent(getActivity(), B_inspecciones.class);
                startActivityForResult(intent , REQUEST_CODE);*/
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GlobalVariables.istabs=false;// para que no entre al flag de tabs
                //is_swipe=false;
                swipeRefreshLayout.setRefreshing(true);
//                tx_texto.setVisibility(View.VISIBLE);
                flagSECFiltro=true;
                paginacion2=1;
                //   upFlag=false;
                //  downFlag=false;
                // (new Handler()).postDelayed(new Runnable() {
                //    @Override
                //    public void run() {
                //swipeRefreshLayout.setRefreshing(true);
                loadingTop=true;
                tx_texto.setVisibility(View.VISIBLE);
                GlobalVariables.listaGlobalSeguridadMA.clear(); //crear segun el formato
                //GlobalVariables.contpublic=2;
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;
                GlobalVariables.isFragment=false;

                String json = "";
                Utils.maCuasiAccidenteModel.CodUbicacion = Elemperpage;
                Utils.maCuasiAccidenteModel.Lugar = String.valueOf(paginacion2);
                Gson gson = new Gson();
                json = gson.toJson(Utils.maCuasiAccidenteModel);

                //Utils.isActivity=true;
                GlobalVariables.istabs=false;
                final ActivityController obj = new ActivityController("post-0", url, FragmentSecuridadCA.this,getActivity());
                obj.execute(json,"0");

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
                    if (GlobalVariables.listaGlobalSeguridadMA.size() != MainActivity.countSegu && flag_enter&&flagSECFiltro) {

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
                        //Utils.inspeccionModel=new InspeccionModel();
                        Utils.maCuasiAccidenteModel.CodUbicacion = Elemperpage;
                        Utils.maCuasiAccidenteModel.Lugar = String.valueOf(paginacion2);
                        Gson gson = new Gson();
                        json2 = gson.toJson(Utils.maCuasiAccidenteModel);
                        /*
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
*/



                        GlobalVariables.istabs=false;// para que no entre al flag de tabs
                        final ActivityController obj = new ActivityController("post-2", url, FragmentSecuridadCA.this,getActivity());
                        obj.execute(json2,"2");

                       /* layoutInflater =(LayoutInflater) rootView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        popupView = layoutInflater.inflate(R.layout.popup_blanco, null);

                        popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT ,580,false);
                        popupWindow.showAtLocation(list_busqueda, Gravity.CENTER, 0, 0);
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

        list_busqueda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"Click en "+position,Toast.LENGTH_SHORT).show();
                String CodSeguCA= GlobalVariables.listaGlobalSeguridadMA.get(position).Codigo;
                Intent intent = new Intent(getActivity(), ActSeguridad.class);
                intent.putExtra("codObs",CodSeguCA);
                intent.putExtra("posTab",0);
                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                startActivity(intent);

            }
        });

        lupabuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.flagUpSc=true;
                GlobalVariables.isFragment=false;

                Utils.maCuasiAccidenteModel=new MACuasiAccidenteModel();
                MACuasiAccidenteModel maCuasiAccidenteModel=new MACuasiAccidenteModel();
                tipo_busqueda=2;
                maCuasiAccidenteModel.CodUbicacion=Elemperpage;
                maCuasiAccidenteModel.Lugar="1";
                String json = "";

                Gson gson = new Gson();
                json = gson.toJson(maCuasiAccidenteModel);

                Utils.isActivity = true;
                url = GlobalVariables.Url_base + "Observaciones/FiltroObservaciones";
                GlobalVariables.listaGlobalSeguridadMA = new ArrayList<>();

                final ActivityController obj = new ActivityController("post", url, FragmentSecuridadCA.this,getActivity());
                obj.execute(json);

                /*
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

                    final ActivityController obj = new ActivityController("post", url, FragmentObservaciones.this,getActivity());
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

                    final ActivityController obj = new ActivityController("post", url, FragmentObservaciones.this,getActivity());
                    obj.execute(json);


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

                    final ActivityController obj = new ActivityController("post", url, FragmentObservaciones.this,getActivity());
                    obj.execute(json);

                }
*/


            }
        });
        return rootView;

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void DeleteObject(String Url, int index){
        String url= GlobalVariables.Url_base+Url;
        ActivityController obj = new ActivityController("get", url, FragmentSecuridadCA.this,getActivity());
        obj.execute(""+index);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentSecuridadCA.OnFragmentInteractionListener) {
            mListener = (FragmentSecuridadCA.OnFragmentInteractionListener) context;
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

    @Override
    public void success(String data, String Tipo) {
        if(data.contains("-1")) Toast.makeText(getContext(), "Ocurrio un error al eliminar registro.",    Toast.LENGTH_SHORT).show();
        else ca.remove(Integer.parseInt(Tipo)-2);

    }

    @Override
    public void successpost(String data1, String Tipo) {
        //data add
        if(Tipo.equals("")){

            Gson gson = new Gson();
            GetPublicacionModel getPublicacionModel = gson.fromJson(data1, GetPublicacionModel.class);
            GlobalVariables.listaGlobalSeguridadMA=getPublicacionModel.Data;
            MainActivity.countSegu=getPublicacionModel.Count;
            ca = new SeguridadCAAdapter(getActivity(), GlobalVariables.listaGlobalSeguridadMA,this);
            list_busqueda.setAdapter(ca);
            if(getPublicacionModel.Data.size()==0){
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                tx_mensajeb.setVisibility(View.VISIBLE);
            }else{
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                tx_mensajeb.setVisibility(View.GONE);
            }

        }

        else if(Tipo.equals("-1")){ // load data preview load
            ca = new SeguridadCAAdapter(getActivity(), GlobalVariables.listaGlobalSeguridadMA,this);
            list_busqueda.setAdapter(ca);
            if(GlobalVariables.stateSEC != null&&GlobalVariables.passSEC) {
                swipeRefreshLayout.setEnabled(false);
                list_busqueda.onRestoreInstanceState(GlobalVariables.stateSEC);
                GlobalVariables.passSEC=false;
            }
        }
        else if(Tipo.equals("0")){ //from refresh data (add 1)
            Gson gson = new Gson();
            GetPublicacionModel getPublicacionModel = gson.fromJson(data1, GetPublicacionModel.class);
            GlobalVariables.listaGlobalSeguridadMA=getPublicacionModel.Data;
            MainActivity.countSegu=getPublicacionModel.Count;
            ca = new SeguridadCAAdapter(getContext(),GlobalVariables.listaGlobalSeguridadMA,this);
            list_busqueda.setAdapter(ca);

            swipeRefreshLayout.setRefreshing(false);
            tx_texto.setVisibility(View.GONE);
            swipeRefreshLayout.setEnabled( false );
        }
        else if(Tipo.equals("2")){ // addd more data
            Gson gson = new Gson();
            GetPublicacionModel getPublicacionModel = gson.fromJson(data1, GetPublicacionModel.class);
            for(PublicacionModel item:getPublicacionModel.Data)
                ca.add(item);
            ca.notifyDataSetChanged();
            constraintLayout.setVisibility(View.GONE);
            flag_enter=true;
        }



        if(flag_seguri){
            linear_total.setVisibility(View.VISIBLE);
            tx_filtro.setText("("+ MainActivity.countSegu+")"+" resultados");
        }else {linear_total.setVisibility(View.GONE);}

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}