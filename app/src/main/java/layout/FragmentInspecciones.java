package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_inspecciones;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Ingresos.Inspecciones.AddInspeccion;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.InspeccionAdapter;
import com.pango.hsec.hsec.adapter.PublicacionAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.observacion_edit;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.pango.hsec.hsec.GlobalVariables.paginacion;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInspecciones.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInspecciones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInspecciones extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    View rootView;
    //Spinner sp_busqueda;
    String tipo_filtro="";
    Button btn_filtro;
    Button add_obs;
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





    private OnFragmentInteractionListener mListener;

    public FragmentInspecciones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentInspecciones.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentInspecciones newInstance(String param1, String param2) {
        FragmentInspecciones fragment = new FragmentInspecciones();
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
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_inspecciones, container, false);
        add_obs=rootView.findViewById(R.id.add_obs);
        tx_texto =(TextView) rootView.findViewById(R.id.tx_texto);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout);
        constraintLayout=(ConstraintLayout) rootView.findViewById(R.id.const_main);
        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        lupabuscar=(ImageView) rootView.findViewById(R.id.lupabuscar);
        GlobalVariables loaddata = new GlobalVariables();
        //loaddata.LoadData();
        list_busqueda=(ListView) rootView.findViewById(R.id.list_busqueda);
        //sp_busqueda=(Spinner) rootView.findViewById(R.id.sp_busqueda);
        tx_mensajeb=rootView.findViewById(R.id.tx_mensajeb);
        btn_filtro=(Button) rootView.findViewById(R.id.btn_filtro);



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

        final ActivityController obj = new ActivityController("post", url, FragmentInspecciones.this,getActivity());
        obj.execute(json);






        add_obs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.ObjectEditable=false;
                Intent addInspeccion = new Intent(getActivity(),AddInspeccion.class);
                addInspeccion.putExtra("codObs","INSP000000XYZ");
                startActivity(addInspeccion);
            }
        });





        btn_filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), B_inspecciones.class);
                startActivityForResult(intent , REQUEST_CODE);

                /*
                if(tipo_filtro.equals(busqueda_tipo[0])) {

                    Utils.observacionModel=new ObservacionModel();

                    Intent intent = new Intent(getActivity(), B_observaciones.class);
                    startActivityForResult(intent , REQUEST_CODE);


                }else if(tipo_filtro.equals(busqueda_tipo[1])){


                    Intent intent = new Intent(getActivity(), B_inspecciones.class);
                    startActivityForResult(intent , REQUEST_CODE);


                }else if(tipo_filtro.equals(busqueda_tipo[2])){
                    Intent intent = new Intent(getActivity(), B_noticias.class);
                    startActivityForResult(intent , REQUEST_CODE);
                }
*/




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

                String json = "";

                //Utils.inspeccionModel=new InspeccionModel();
                Utils.inspeccionModel.Elemperpage = "5";
                Utils.inspeccionModel.Pagenumber = String.valueOf(paginacion2);
                Gson gson = new Gson();
                json = gson.toJson(Utils.inspeccionModel);

                /*
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
*/

                //Utils.isActivity=true;
                GlobalVariables.istabs=false;
                final ActivityController obj = new ActivityController("post", url, FragmentInspecciones.this,getActivity());
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
                        //Utils.inspeccionModel=new InspeccionModel();
                        Utils.inspeccionModel.Elemperpage = "5";
                        Utils.inspeccionModel.Pagenumber = String.valueOf(paginacion2);
                        Gson gson = new Gson();
                        json2 = gson.toJson(Utils.inspeccionModel);
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
                        final ActivityController obj = new ActivityController("post", url, FragmentInspecciones.this,getActivity());
                        obj.execute(json2);

                        layoutInflater =(LayoutInflater) rootView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
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
                String CodInspeccion= GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                Intent intent = new Intent(getActivity(), ActInspeccionDet.class);
                intent.putExtra("codObs",CodInspeccion);
                intent.putExtra("posTab",0);
                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                startActivity(intent);


/*
                if(tipo_busqueda==1) {
                    String CodObservacion=GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                    String tipoObs=GlobalVariables.listaGlobalFiltro.get(position).Tipo;

                    Intent intent = new Intent(getActivity(), ActMuroDet.class);
                    intent.putExtra("codObs",CodObservacion);
                    intent.putExtra("posTab",0);
                    intent.putExtra("tipoObs",tipoObs);

                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                    startActivity(intent);
                }else if(tipo_busqueda==2){
                    String CodInspeccion= GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                    Intent intent = new Intent(getActivity(), ActInspeccionDet.class);
                    intent.putExtra("codObs",CodInspeccion);
                    intent.putExtra("posTab",0);
                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                    startActivity(intent);

                }else if(tipo_busqueda==3){
                    String CodNoticia= GlobalVariables.listaGlobalFiltro.get(position).Codigo;
                    Intent intent = new Intent(getActivity(), ActNoticiaDet.class);
                    intent.putExtra("codObs",CodNoticia);
                    intent.putExtra("posTab",0);
                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                    startActivity(intent);

                }
*/

            }
        });

        lupabuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.flagUpSc=true;
                GlobalVariables.isFragment=false;

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

                final ActivityController obj = new ActivityController("post", url, FragmentInspecciones.this,getActivity());
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

    @Override
    public void success(String data, String Tipo) {

    }

    @Override
    public void successpost(String data, String Tipo) {

        //jsonObs=data1;
        if(flagpopup){
            popupWindow.dismiss();
            flagpopup=false;
        }
/*
        if(!data1.equals(jsonObs)){
            jsonObs=data1;
        }else{
            //GlobalVariables.listaGlobalFiltro=new ArrayList<PublicacionModel>();
        }
        */
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

        InspeccionAdapter ca = new InspeccionAdapter(getActivity(), GlobalVariables.listaGlobalFiltro);
        list_busqueda.setAdapter(ca);
        ca.notifyDataSetChanged();
        /*
        if(tipo_busqueda==1) {

            PublicacionAdapter ca = new PublicacionAdapter(getActivity(), GlobalVariables.listaGlobalFiltro);
            list_busqueda.setAdapter(ca);
            ca.notifyDataSetChanged();

        }else if(tipo_busqueda==2){

            InspeccionAdapter ca = new InspeccionAdapter(getActivity(), GlobalVariables.listaGlobalFiltro);
            list_busqueda.setAdapter(ca);
            ca.notifyDataSetChanged();
        }else if(tipo_busqueda==3){

            NoticiasAdapter ca = new NoticiasAdapter(getActivity(), GlobalVariables.listaGlobalFiltro);
            list_busqueda.setAdapter(ca);
            ca.notifyDataSetChanged();
        }

*/

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
        paginacion-=1;
        flag_enter=true;

        Toast.makeText(getActivity(),mensaje,Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                Utils.inspeccionModel.Elemperpage="5";
                Utils.inspeccionModel.Pagenumber="1";
                String json = "";

                Gson gson = new Gson();
                json = gson.toJson(Utils.inspeccionModel);
                GlobalVariables.isFragment=false;
                Utils.isActivity = true;
                url = GlobalVariables.Url_base + "Inspecciones/Filtroinspecciones";
                GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                final ActivityController obj = new ActivityController("post", url, FragmentInspecciones.this,getActivity());
                obj.execute(json);

              /*
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

                    final ActivityController obj = new ActivityController("post", url, FragmentObservaciones.this,getActivity());
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

                    final ActivityController obj = new ActivityController("post", url, FragmentObservaciones.this,getActivity());
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

                    final ActivityController obj = new ActivityController("post", url, FragmentObservaciones.this,getActivity());
                    obj.execute(json);

                }
*/
            }



        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


}
