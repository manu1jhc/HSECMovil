
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
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.PopupWindow;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.gson.Gson;
        import com.pango.hsec.hsec.Busquedas.B_facilito;
        import com.pango.hsec.hsec.Busquedas.B_inspecciones;
        import com.pango.hsec.hsec.Facilito.obsFacilitoDet;
        import com.pango.hsec.hsec.GlobalVariables;
        import com.pango.hsec.hsec.IActivity;
        import com.pango.hsec.hsec.Ingresos.Inspecciones.AddInspeccion;
        import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
        import com.pango.hsec.hsec.MainActivity;
        import com.pango.hsec.hsec.R;
        import com.pango.hsec.hsec.Utils;
        import com.pango.hsec.hsec.adapter.InspeccionAdapter;
        import com.pango.hsec.hsec.adapter.ObsFacilitoAdapter;
        import com.pango.hsec.hsec.controller.ActivityController;
        import com.pango.hsec.hsec.model.GetObsFacilitoModel;
        import com.pango.hsec.hsec.model.GetPublicacionModel;
        import com.pango.hsec.hsec.model.InspeccionModel;
        import com.pango.hsec.hsec.model.ObsFacilitoMinModel;
        import com.pango.hsec.hsec.model.ObsFacilitoModel;
        import com.pango.hsec.hsec.model.PublicacionModel;

        import java.util.ArrayList;

        import static android.app.Activity.RESULT_OK;
        import static com.pango.hsec.hsec.MainActivity.flag_Facilito;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInspecciones.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInspecciones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentObsFacilito extends Fragment implements IActivity {
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
    public ObsFacilitoAdapter ca;
    TextView tx_filtro;
    //boolean flag_filtro=false;
    ConstraintLayout linear_total;
    Button btn_eliminarf;
    private OnFragmentInteractionListener mListener;

    public FragmentObsFacilito() {
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
        rootView=inflater.inflate(R.layout.fragment_obs_facilito, container, false);
        tx_filtro=rootView.findViewById(R.id.tx_filtro);
        linear_total=rootView.findViewById(R.id.linear_total);
        tx_texto =(TextView) rootView.findViewById(R.id.tx_texto);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout);
        constraintLayout=(ConstraintLayout) rootView.findViewById(R.id.const_main);
        //swipeRefreshLayout.setVisibility(View.INVISIBLE);
        GlobalVariables loaddata = new GlobalVariables();
        //loaddata.LoadData();
        list_busqueda=(ListView) rootView.findViewById(R.id.list_busqueda);
        //sp_busqueda=(Spinner) rootView.findViewById(R.id.sp_busqueda);
        tx_mensajeb=rootView.findViewById(R.id.tx_mensajeb);
        btn_eliminarf=rootView.findViewById(R.id.btn_eliminarf);




        url = GlobalVariables.Url_base + "ObsFacilito/Filtro";


        if(GlobalVariables.listaGlobalFacilito.size()==0) {

            GlobalVariables.FacilitoList =new ObsFacilitoModel();
            GlobalVariables.FacilitoList.Accion="5";
            GlobalVariables.FacilitoList.Observacion="1";

            Gson gson = new Gson();
            String json = gson.toJson(GlobalVariables.FacilitoList);

            final ActivityController obj = new ActivityController("post", url, FragmentObsFacilito.this, getActivity());
            obj.execute(json,"-1");




        }else{
            success("","-1");
        }


        btn_eliminarf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_total.setVisibility(View.GONE);
                flag_Facilito=false;
                GlobalVariables.listaGlobalFacilito.clear();

                GlobalVariables.FacilitoList =new ObsFacilitoModel();
                GlobalVariables.FacilitoList.Accion="5";
                GlobalVariables.FacilitoList.Observacion="1";

                Gson gson = new Gson();
                String json = gson.toJson(GlobalVariables.FacilitoList);

                final ActivityController obj = new ActivityController("post", url, FragmentObsFacilito.this, getActivity());
                obj.execute(json,"-1");
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

                loadingTop=true;
                tx_texto.setVisibility(View.VISIBLE);
                GlobalVariables.listaGlobalFacilito.clear(); //crear segun el formato
                //GlobalVariables.contpublic=2;
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;

                //Utils.isActivity=true;

                url = GlobalVariables.Url_base + "ObsFacilito/Filtro";

                GlobalVariables.FacilitoList.Accion="5";
                GlobalVariables.FacilitoList.Observacion="1";

                //ObsFacilitoModel obsFacilitoModel = new ObsFacilitoModel();
                //tipo_busqueda = 2;

                String json = "";

                Gson gson = new Gson();
                json = gson.toJson(GlobalVariables.FacilitoList);

                final ActivityController obj = new ActivityController("post-2", url, FragmentObsFacilito.this, getActivity());
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
                    if (GlobalVariables.listaGlobalFacilito.size() != MainActivity.countFacilito && flag_enter) {

                        //progressBarMain.setVisibility(View.VISIBLE);
                        flag_enter = false;
                        constraintLayout.setVisibility(View.VISIBLE);
                        //lupabuscar.setEnabled(false);
                        GlobalVariables.isFragment=false;
                        paginacion2+=1;

                        url = GlobalVariables.Url_base + "ObsFacilito/Filtro";


                        GlobalVariables.FacilitoList.Accion="5";
                        GlobalVariables.FacilitoList.Observacion=String.valueOf(paginacion2);


                        //url = GlobalVariables.Url_base + "ObsFacilito/Filtro";

                        //ObsFacilitoModel obsFacilitoModel = new ObsFacilitoModel();
                        //tipo_busqueda = 2;

                        //obsFacilitoModel.Accion = "5";
                        //obsFacilitoModel.Observacion = String.valueOf(paginacion2);
                        String json = "";

                        Gson gson = new Gson();
                        json = gson.toJson(GlobalVariables.FacilitoList);


                        final ActivityController obj = new ActivityController("post-2", url, FragmentObsFacilito.this, getActivity());
                        obj.execute(json,"1");




                        //final ActivityController obj = new ActivityController("get-2", url, FragmentObsFacilito.this,getActivity());
                        //obj.execute("1");
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
                String Codigo= GlobalVariables.listaGlobalFacilito.get(position).CodObsFacilito;
                String Editable=GlobalVariables.listaGlobalFacilito.get(position).Editable;

                Intent intent = new Intent(getActivity(), obsFacilitoDet.class);
                intent.putExtra("codObs",Codigo);
                intent.putExtra("verBoton",Editable);
                startActivity(intent);
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

    public void DeleteObject(String Url, int index){
        String url= GlobalVariables.Url_base+Url;
        ActivityController obj = new ActivityController("get", url, FragmentObsFacilito.this,getActivity());
        obj.execute(""+index);
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
        //data add
        if (Tipo.equals("-1")) { // load data preview load
            ca = new ObsFacilitoAdapter(getActivity(), GlobalVariables.listaGlobalFacilito, this);
            list_busqueda.setAdapter(ca);
            if (GlobalVariables.stateFac != null && GlobalVariables.passFac) {
                swipeRefreshLayout.setEnabled(false);
                list_busqueda.onRestoreInstanceState(GlobalVariables.stateFac);
                GlobalVariables.passFac = false;
        }
        }
        else {
            if(data.contains("-1")) Toast.makeText(getContext(), "Ocurrio un error al eliminar registro.",    Toast.LENGTH_SHORT).show();
            else ca.remove(Integer.parseInt(Tipo)-2);
        }


        if(flag_Facilito){
            linear_total.setVisibility(View.VISIBLE);
            tx_filtro.setText("("+MainActivity.countFacilito+")"+" resultados");
        }else {linear_total.setVisibility(View.GONE);}

    }

    @Override
    public void successpost(String data, String Tipo) {


        if (Tipo.equals("-1")) { // load first data
            Gson gson = new Gson();
            GetObsFacilitoModel getPublicacionModel = gson.fromJson(data, GetObsFacilitoModel.class);
            GlobalVariables.listaGlobalFacilito = getPublicacionModel.Data;
            ca = new ObsFacilitoAdapter(getActivity(), GlobalVariables.listaGlobalFacilito, this);
            list_busqueda.setAdapter(ca);
            MainActivity.countFacilito= getPublicacionModel.Count;

            if (MainActivity.countFacilito == 0) {
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                tx_mensajeb.setVisibility(View.VISIBLE);
            } else {
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                tx_mensajeb.setVisibility(View.GONE);
            }
        } else if (Tipo.equals("0")) { //from refresh data
            Gson gson = new Gson();
            GetObsFacilitoModel getPublicacionModel = gson.fromJson(data, GetObsFacilitoModel.class);
            GlobalVariables.listaGlobalFacilito = getPublicacionModel.Data;
            MainActivity.countFacilito= getPublicacionModel.Count;

            ca = new ObsFacilitoAdapter(getContext(), GlobalVariables.listaGlobalFacilito, this);
            list_busqueda.setAdapter(ca);

            swipeRefreshLayout.setRefreshing(false);
            tx_texto.setVisibility(View.GONE);
            swipeRefreshLayout.setEnabled(false);
        } else if (Tipo.equals("1")) { // addd more data
            Gson gson = new Gson();
            GetObsFacilitoModel getPublicacionModel = gson.fromJson(data, GetObsFacilitoModel.class);

            for (ObsFacilitoMinModel item : getPublicacionModel.Data)
                ca.add(item);
            ca.notifyDataSetChanged();
            constraintLayout.setVisibility(View.GONE);
            flag_enter = true;
        }


        if(flag_Facilito){
            linear_total.setVisibility(View.VISIBLE);
            tx_filtro.setText("("+MainActivity.countFacilito+")"+" resultados");
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

    public void Filtro_Facilito(){
        Intent intent = new Intent(getActivity(), B_facilito.class);
        startActivityForResult(intent , REQUEST_CODE);
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

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {



                flag_Facilito=true;
                GlobalVariables.FacilitoList.Accion="5";
                GlobalVariables.FacilitoList.Observacion="1";
                String json = "";

                Gson gson = new Gson();
                json = gson.toJson(GlobalVariables.FacilitoList);

                url = GlobalVariables.Url_base + "ObsFacilito/Filtro";
                GlobalVariables.listaGlobalFacilito = new ArrayList<>();

                final ActivityController obj = new ActivityController("post", url, FragmentObsFacilito.this,getActivity());
                obj.execute(json,"-1");


                /*
                String tipo_dato=data.getStringExtra("tipo");

                if(tipo_persona.equals("responsable")) {
                    String nombre_obs = data.getStringExtra("nombreP");
                    String codpersona_obs = data.getStringExtra("codpersona");
                    id_persona_res.setText(nombre_obs);
                    //Utils.inspeccionModel.CodTipo = codpersona_obs;
                }else{

                    String cod_contrata = data.getStringExtra("codContrata");
                    String des_contrata = data.getStringExtra("desContrata");
                    id_creador.setText(des_contrata);
                    //Utils.inspeccionModel.CodContrata = cod_contrata;
                }


*/
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }
}
