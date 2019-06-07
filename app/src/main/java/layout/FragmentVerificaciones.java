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
import com.pango.hsec.hsec.Busquedas.B_Verificaciones;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.MainActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.Verificaciones.ActVerificacionDet;
import com.pango.hsec.hsec.adapter.PublicacionAdapter;
import com.pango.hsec.hsec.adapter.VerificacionAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.model.PublicacionModel;
import com.pango.hsec.hsec.model.VerificacionModel;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.pango.hsec.hsec.MainActivity.flag_verificacion;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentVerificaciones.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentVerificaciones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentVerificaciones extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public static final int REQUEST_CODE = 1;

    View rootView;
    Button add_ver, btn_filtro, btn_eliminarf;
    TextView tx_texto, tx_mensajeb, tx_filtro;
    SwipeRefreshLayout swipeRefreshLayout;
    ConstraintLayout constraintLayout, linear_total;
    ImageView lupabuscar;
    public ListView list_busqueda;
    String url;
    int tipo_busqueda;
    String Elemperpage="7";
    static int paginacion2=1;
    boolean flagVerFiltro=true;
    boolean loadingTop=false;
    boolean listenerFlag;
    boolean upFlag;
    boolean downFlag;
    boolean flag_enter=true;
    boolean flagpopup=false;
    PopupWindow popupWindow;

    public VerificacionAdapter ca;


    public FragmentVerificaciones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentVerificaciones.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentVerificaciones newInstance(String param1, String param2) {
        FragmentVerificaciones fragment = new FragmentVerificaciones();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_verificaciones, container, false);
        add_ver=rootView.findViewById(R.id.add_ver);

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

        url = GlobalVariables.Url_base + "Verificacion/Filtro";

        if(GlobalVariables.listaGlobalVerificaciones.size()==0) {

            Utils.verificacionModel = new VerificacionModel();
            VerificacionModel verificacionModel = new VerificacionModel();
            tipo_busqueda = 1;
            verificacionModel.CodUbicacion = Elemperpage;
            verificacionModel.Lugar = "1";


            String json = "";
            Gson gson = new Gson();
            json = gson.toJson(verificacionModel);

            Utils.isActivity = true;
            final ActivityController obj = new ActivityController("post-" + paginacion2, url, FragmentVerificaciones.this, getActivity());
            obj.execute(json,"0");

        }else{
            successpost("","-1");
        }

        btn_eliminarf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_total.setVisibility(View.GONE);
                MainActivity.flag_verificacion=false;
                GlobalVariables.listaGlobalVerificaciones.clear();

                Utils.verificacionModel = new VerificacionModel();
                VerificacionModel verificacionModel = new VerificacionModel();
                tipo_busqueda = 5;
                paginacion2=1;
                verificacionModel.CodUbicacion = Elemperpage;
                verificacionModel.Lugar = "1";
                String json = "";
                Gson gson = new Gson();
                json = gson.toJson(verificacionModel);

                Utils.isActivity = true;
                final ActivityController obj = new ActivityController("post" + paginacion2, url, FragmentVerificaciones.this, getActivity());
                obj.execute(json);
            }
        });

        add_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GlobalVariables.ObjectEditable=false;
//                Intent obserbacion_edit = new Intent(getActivity(),observacion_edit.class);
//                obserbacion_edit.putExtra("codObs", "OBS000000XYZ");
//                obserbacion_edit.putExtra("tipoObs","TO01");
//                obserbacion_edit.putExtra("posTab", 0);
//                startActivity(obserbacion_edit);

            }
        });

        btn_filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.verificacionModel=new VerificacionModel();
                Intent intent = new Intent(getActivity(), B_Verificaciones.class);
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
                flagVerFiltro=true;
                paginacion2=1;
                loadingTop=true;
                tx_texto.setVisibility(View.VISIBLE);
                GlobalVariables.listaGlobalVerificaciones.clear(); //crear segun el formato
                //GlobalVariables.contpublic=2;
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;
                GlobalVariables.isFragment=false;
                String json = "";
                Utils.verificacionModel.CodUbicacion = Elemperpage;
                Utils.verificacionModel.Lugar = String.valueOf(paginacion2);
                Gson gson = new Gson();
                json = gson.toJson(Utils.verificacionModel);


                GlobalVariables.istabs=false;
                final ActivityController obj = new ActivityController("post-0", url, FragmentVerificaciones.this,getActivity());
                obj.execute(json,"0");

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
                    if (GlobalVariables.listaGlobalVerificaciones.size() != MainActivity.countVerificacion && flag_enter&&flagVerFiltro) {

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
                        Utils.verificacionModel.CodUbicacion = Elemperpage;
                        Utils.verificacionModel.Lugar = String.valueOf(paginacion2);
                        Gson gson = new Gson();
                        json2 = gson.toJson(Utils.verificacionModel);

                        GlobalVariables.istabs=false;// para que no entre al flag de tabs
                        final ActivityController obj = new ActivityController("post-2", url, FragmentVerificaciones.this,getActivity());
                        obj.execute(json2,"2");

                       /* layoutInflater =(LayoutInflater) rootView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        popupView = layoutInflater.inflate(R.layout.popup_blanco, null);

                        popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT ,580,false);
                        popupWindow.showAtLocation(list_busqueda, Gravity.CENTER, 0, 0);
                        flagpopup=true;*/

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
                String CodVerificacion=GlobalVariables.listaGlobalVerificaciones.get(position).Codigo;
                String tipoVer=GlobalVariables.listaGlobalVerificaciones.get(position).Tipo;

                Intent intent = new Intent(getActivity(), ActVerificacionDet.class);
                intent.putExtra("codVer",CodVerificacion);
                intent.putExtra("posTab",0);
                intent.putExtra("tipoVer",tipoVer);

                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                startActivity(intent);
            }
        });

        lupabuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.flagUpSc=true;
                GlobalVariables.isFragment=false;

                Utils.verificacionModel=new VerificacionModel();
                VerificacionModel verificacionModel = new VerificacionModel();
                tipo_busqueda=5;
                verificacionModel.CodUbicacion = "5";
                verificacionModel.Lugar = "1";
                String json = "";
                Gson gson = new Gson();
                json = gson.toJson(verificacionModel);

                Utils.isActivity = true;
                url = GlobalVariables.Url_base + "Verificacion/Filtro";
                GlobalVariables.listaGlobalVerificaciones = new ArrayList<>();

                final ActivityController obj = new ActivityController("post", url, FragmentVerificaciones.this,getActivity());
                obj.execute(json);

            }
        });




        return rootView;
    }


    public void Filtro_Verificaciones(){
        Utils.verificacionModel=new VerificacionModel();
        Intent intent = new Intent(this.getActivity(), B_Verificaciones.class);
        startActivityForResult(intent , REQUEST_CODE);
    }



    public void DeleteObject(String Url, int index){
        String url= GlobalVariables.Url_base+Url;
        ActivityController obj = new ActivityController("get", url, FragmentVerificaciones.this,getActivity());
        obj.execute(""+index);
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
            GlobalVariables.listaGlobalVerificaciones =getPublicacionModel.Data;
            MainActivity.countVerificacion=getPublicacionModel.Count;
            ca = new VerificacionAdapter(getActivity(), GlobalVariables.listaGlobalVerificaciones,this);
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
            ca = new  VerificacionAdapter(getActivity(), GlobalVariables.listaGlobalVerificaciones,this);
            list_busqueda.setAdapter(ca);
            if(GlobalVariables.stateVer != null&&GlobalVariables.passVer) {
                swipeRefreshLayout.setEnabled(false);
                list_busqueda.onRestoreInstanceState(GlobalVariables.stateVer);
                GlobalVariables.passVer=false;
            }
        }
        else if(Tipo.equals("0")){ //from refresh data (add 1)
            Gson gson = new Gson();
            GetPublicacionModel getPublicacionModel = gson.fromJson(data1, GetPublicacionModel.class);
            GlobalVariables.listaGlobalVerificaciones=getPublicacionModel.Data;
            MainActivity.countVerificacion=getPublicacionModel.Count;
            ca = new VerificacionAdapter(getContext(),GlobalVariables.listaGlobalVerificaciones,this);
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



        if(flag_verificacion){
            linear_total.setVisibility(View.VISIBLE);
            tx_filtro.setText("("+ MainActivity.countVerificacion+")"+" resultados");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                flag_verificacion=true;

                GlobalVariables.flagUpSc=true;
                tipo_busqueda = data.getIntExtra("Tipo_Busqueda",0);

                Utils.verificacionModel.CodUbicacion = "5";
                Utils.verificacionModel.Lugar = "1";
                String json = "";
                Gson gson = new Gson();
                json = gson.toJson(Utils.verificacionModel);
                GlobalVariables.isFragment=false;
                Utils.isActivity = true;
                url = GlobalVariables.Url_base + "Verificacion/Filtro";
                GlobalVariables.listaGlobalVerificaciones = new ArrayList<>();

                final ActivityController obj = new ActivityController("post", url, FragmentVerificaciones.this,getActivity());
                obj.execute(json);
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }









}
