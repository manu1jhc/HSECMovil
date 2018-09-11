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
import com.pango.hsec.hsec.Busquedas.B_noticias;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Ingresos.Inspecciones.AddInspeccion;
import com.pango.hsec.hsec.MainActivity;
import com.pango.hsec.hsec.Noticias.ActNoticiaDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.NoticiasAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.NoticiasModel;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNoticias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNoticias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNoticias extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    public static final int REQUEST_CODE = 1;
    View rootView;
    //Spinner sp_busqueda;
    String tipo_filtro="";
    Button btn_filtro;
    Button add_obs;
    String url="";
    //int contPublicacion;
    public ListView list_busqueda;
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
    public NoticiasAdapter ca;

    ConstraintLayout linear_total;
    Button btn_eliminarf;
    TextView tx_filtro;
    String Elemperpage="7";

    private OnFragmentInteractionListener mListener;

    public FragmentNoticias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNoticias.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNoticias newInstance(String param1, String param2) {
        FragmentNoticias fragment = new FragmentNoticias();
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

    //View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_noticias, container, false);
        //ca = new NoticiasAdapter(getActivity(), GlobalVariables.listaGlobalFiltro);
        //list_busqueda.setAdapter(ca);
        //ca.notifyDataSetChanged();
        // Inflate the layout for this fragment


        add_obs=rootView.findViewById(R.id.add_obs);
        tx_texto =(TextView) rootView.findViewById(R.id.tx_texto);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout);
        constraintLayout=(ConstraintLayout) rootView.findViewById(R.id.const_main);
        //swipeRefreshLayout.setVisibility(View.INVISIBLE);
        lupabuscar=(ImageView) rootView.findViewById(R.id.lupabuscar);
        GlobalVariables loaddata = new GlobalVariables();
        //loaddata.LoadData();
        list_busqueda=(ListView) rootView.findViewById(R.id.list_busqueda);
        //sp_busqueda=(Spinner) rootView.findViewById(R.id.sp_busqueda);
        tx_mensajeb=rootView.findViewById(R.id.tx_mensajeb);
        btn_filtro=(Button) rootView.findViewById(R.id.btn_filtro);
        linear_total=rootView.findViewById(R.id.linear_total);
        btn_eliminarf=rootView.findViewById(R.id.btn_eliminarf);
        tx_filtro=rootView.findViewById(R.id.tx_filtro);

        url = GlobalVariables.Url_base + "Noticia/FiltroNoticias";

        if(GlobalVariables.listaGlobalNoticias.size()==0) {

            Utils.noticiasModel = new NoticiasModel();
            tipo_busqueda = 2;
            Utils.noticiasModel.Elemperpage="5";
            Utils.noticiasModel.Pagenumber="1";


            String json = "";

            Gson gson = new Gson();
            json = gson.toJson(Utils.noticiasModel);

            //Utils.isActivity = true;
            //GlobalVariables.listaGlobalInspeccion = new ArrayList<>();

            final ActivityController obj = new ActivityController("post", url, FragmentNoticias.this, getActivity());
            obj.execute(json);

        }else{
            successpost("","-1");

        }


        btn_eliminarf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_total.setVisibility(View.GONE);
                MainActivity.flag_noticia =false;
                GlobalVariables.listaGlobalNoticias.clear();

                Utils.noticiasModel = new NoticiasModel();
                tipo_busqueda = 2;
                Utils.noticiasModel.Elemperpage="5";
                Utils.noticiasModel.Pagenumber="1";


                String json = "";

                Gson gson = new Gson();
                json = gson.toJson(Utils.noticiasModel);

                //Utils.isActivity = true;
                //GlobalVariables.listaGlobalInspeccion = new ArrayList<>();

                final ActivityController obj = new ActivityController("post", url, FragmentNoticias.this, getActivity());
                obj.execute(json);
            }
        });



        /////PREGUNTAR-----------------------------------------------------------------
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
                Intent intent = new Intent(getActivity(), B_noticias.class);
                startActivityForResult(intent , REQUEST_CODE);
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
                GlobalVariables.listaGlobalNoticias.clear(); //crear segun el formato
                //GlobalVariables.contpublic=2;
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;
                GlobalVariables.isFragment=false;

                String json = "";
                Utils.noticiasModel.Elemperpage = "5";
                Utils.noticiasModel.Pagenumber = String.valueOf(paginacion2);


                Gson gson = new Gson();
                json = gson.toJson(Utils.noticiasModel);


                //Utils.isActivity=true;
                GlobalVariables.istabs=false;
                final ActivityController obj = new ActivityController("post-0", url, FragmentNoticias.this,getActivity());
                obj.execute(json,"1");

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
                    if (GlobalVariables.listaGlobalNoticias.size() != MainActivity.countNoticia && flag_enter&&flagObsFiltro) {

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
                        Utils.noticiasModel.Elemperpage = Elemperpage;
                        Utils.noticiasModel.Pagenumber = String.valueOf(paginacion2);
                        Gson gson = new Gson();
                        json2 = gson.toJson(Utils.noticiasModel);
                        /*
                        if(tipo_busqueda==1) {
                            //Utils.observacionModel=new ObservacionModel();
                            Utils.observacionModel.CodUbicacion = Elemperpage;
                            Utils.observacionModel.Lugar = String.valueOf(paginacion2);
                            Gson gson = new Gson();
                            json2 = gson.toJson(Utils.observacionModel);
                        }else if(tipo_busqueda==2){
                            //Utils.inspeccionModel=new InspeccionModel();
                            Utils.inspeccionModel.Elemperpage = Elemperpage;
                            Utils.inspeccionModel.Pagenumber = String.valueOf(paginacion2);
                            Gson gson = new Gson();
                            json2 = gson.toJson(Utils.inspeccionModel);
                        }else if(tipo_busqueda==3){
                            //Utils.noticiasModel=new NoticiasModel();
                            Utils.noticiasModel.Elemperpage = Elemperpage;
                            Utils.noticiasModel.Pagenumber = String.valueOf(paginacion2);
                            Gson gson = new Gson();
                            json2 = gson.toJson(Utils.noticiasModel);
                        }
*/



                        GlobalVariables.istabs=false;// para que no entre al flag de tabs
                        final ActivityController obj = new ActivityController("post-2", url, FragmentNoticias.this,getActivity());
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

                String CodNoticia= GlobalVariables.listaGlobalNoticias.get(position).Codigo;
                Intent intent = new Intent(getActivity(), ActNoticiaDet.class);
                intent.putExtra("codObs",CodNoticia);
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

                Utils.noticiasModel=new NoticiasModel();
                NoticiasModel noticiasModel = new NoticiasModel();
                tipo_busqueda = 3;
                noticiasModel.Elemperpage = Elemperpage;
                noticiasModel.Pagenumber = "1";
                String json = "";

                Gson gson = new Gson();
                json = gson.toJson(noticiasModel);

                Utils.isActivity = true;
                url = GlobalVariables.Url_base + "Noticia/FiltroNoticias";
                GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                final ActivityController obj = new ActivityController("post", url, FragmentNoticias.this,getActivity());
                obj.execute(json);






                /*
                if(tipo_filtro.equals(busqueda_tipo[0])) {
                    Utils.observacionModel=new ObservacionModel();
                    ObservacionModel observacionModel=new ObservacionModel();
                    tipo_busqueda=1;
                    observacionModel.CodUbicacion = Elemperpage;
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
                    inspeccionModel.Elemperpage=Elemperpage;
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
                    noticiasModel.Elemperpage = Elemperpage;
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
        if(data.contains("-1")) Toast.makeText(getContext(), "Ocurrio un error al eliminar registro.",    Toast.LENGTH_SHORT).show();
        else ca.remove(Integer.parseInt(Tipo)-2);
    }

    @Override
    public void successpost(String data, String Tipo)  {



        //data add
        if(Tipo.equals("")){
            Gson gson = new Gson();
            GetPublicacionModel getPublicacionModel = gson.fromJson(data, GetPublicacionModel.class);
            GlobalVariables.listaGlobalNoticias=getPublicacionModel.Data;

            ca = new NoticiasAdapter(getActivity(), GlobalVariables.listaGlobalNoticias,this);
            list_busqueda.setAdapter(ca);
            MainActivity.countNoticia= getPublicacionModel.Count;
            if(getPublicacionModel.Data.size()==0){
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                tx_mensajeb.setVisibility(View.VISIBLE);
            }else{
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                tx_mensajeb.setVisibility(View.GONE);
            }
        }
        else if(Tipo.equals("-1")){ // load data preview load
            ca = new NoticiasAdapter(getActivity(), GlobalVariables.listaGlobalNoticias,this);
            list_busqueda.setAdapter(ca);
            if(GlobalVariables.stateInsp != null&&GlobalVariables.passInsp) {
                swipeRefreshLayout.setEnabled(false);
                list_busqueda.onRestoreInstanceState(GlobalVariables.stateInsp);
                GlobalVariables.passInsp=false;
            }
        }
        else if(Tipo.equals("0")){ //from refresh data
            Gson gson = new Gson();
            GetPublicacionModel getPublicacionModel = gson.fromJson(data, GetPublicacionModel.class);
            GlobalVariables.listaGlobalInspeccion=getPublicacionModel.Data;
            MainActivity.countNoticia= getPublicacionModel.Count;

            ca = new NoticiasAdapter(getActivity(), GlobalVariables.listaGlobalNoticias,this);
            list_busqueda.setAdapter(ca);

            swipeRefreshLayout.setRefreshing(false);
            tx_texto.setVisibility(View.GONE);
            swipeRefreshLayout.setEnabled( false );
        }
        else if(Tipo.equals("2")){ // addd more data
            Gson gson = new Gson();
            GetPublicacionModel getPublicacionModel = gson.fromJson(data, GetPublicacionModel.class);
            for(PublicacionModel item:getPublicacionModel.Data)
                ca.add(item);
            ca.notifyDataSetChanged();
            constraintLayout.setVisibility(View.GONE);
            flag_enter=true;
        }

        if(MainActivity.flag_noticia){
            linear_total.setVisibility(View.VISIBLE);
            tx_filtro.setText("("+ MainActivity.countNoticia+")"+" resultados");
        }else {linear_total.setVisibility(View.GONE);}



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
    public void Filtro_Noticias(){
        Intent intent = new Intent(getActivity(), B_noticias.class);//cambiar a B_noticias
        startActivityForResult(intent , REQUEST_CODE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            //Bundle datos = this.getIntent().getExtras();
            //tipo_busqueda=datos.getInt("Tipo_Busqueda");

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                MainActivity.flag_noticia=true;

                GlobalVariables.flagUpSc=true;
                MainActivity.flag_inspeccion=true;
                tipo_busqueda = data.getIntExtra("Tipo_Busqueda",0);

              /*  String nombre_obs = data.getStringExtra("nombreP");
                String codpersona_obs = data.getStringExtra("codpersona");
                id_persona.setText(nombre_obs);
                Utils.observacionModel.ObservadoPor=codpersona_obs;
                */

                Utils.noticiasModel.Elemperpage=Elemperpage;
                Utils.noticiasModel.Pagenumber="1";
                String json = "";

                Gson gson = new Gson();
                json = gson.toJson(Utils.noticiasModel);

                GlobalVariables.isFragment=false;
                Utils.isActivity = true;
                url = GlobalVariables.Url_base + "Noticia/FiltroNoticias";
                GlobalVariables.listaGlobalFiltro = new ArrayList<>();

                final ActivityController obj = new ActivityController("post", url, FragmentNoticias.this,getActivity());
                obj.execute(json);

              /*
                if(tipo_busqueda==1) {
                    Utils.observacionModel.CodUbicacion = Elemperpage;
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
                    Utils.inspeccionModel.Elemperpage=Elemperpage;
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
                    Utils.noticiasModel.Elemperpage=Elemperpage;
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
