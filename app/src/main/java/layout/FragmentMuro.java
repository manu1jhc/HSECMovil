package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Facilito.obsFacilitoDet;
import com.pango.hsec.hsec.Facilito.report_obs;
import com.pango.hsec.hsec.Ingresos.Inspecciones.AddInspeccion;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Noticias.ActNoticiaDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.MuroAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.PublicacionModel;
import com.pango.hsec.hsec.model.UsuarioModel;
import com.pango.hsec.hsec.observacion_edit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMuro.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMuro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMuro extends Fragment implements IActivity{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentMuro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMuro.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMuro newInstance(String param1, String param2) {
        FragmentMuro fragment = new FragmentMuro();
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
///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ListView List_muro;
    View rootView;
    public MuroAdapter ca;
    //boolean flagFiltro=true;
    //ArrayList<PublicacionModel> listaPublicaciones = new  ArrayList<PublicacionModel>();
    boolean upFlag;
    boolean downFlag;
    boolean listenerFlag;
    int paginacion=1;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean flag_enter=true;
    ConstraintLayout constraintLayout;
    String url;
    TextView tx_texto;
    //ImageButton btn_galeria;
    //ImageView imageView;
    int contPublicacion;
    UsuarioModel getUsuarioModel;
    boolean is_swipe=true;
    //int paginacion=1;
   // private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    Button btn_facilito,btn_obs,btn_insp;

    //TextView tx_comentario;
    boolean flagpopup=false;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    Parcelable status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,   Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_muro, container, false);
        GlobalVariables.view_fragment=rootView;
        GlobalVariables.isFragment=true;
        List_muro=rootView.findViewById(R.id.List_muro);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout);
        constraintLayout=(ConstraintLayout) rootView.findViewById(R.id.const_main2);
        tx_texto =(TextView)rootView.findViewById(R.id.tx_texto);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation);

        btn_facilito=rootView.findViewById(R.id.btn_asistencia);
        btn_obs=rootView.findViewById(R.id.btn_obs);
        btn_insp=rootView.findViewById(R.id.btn_insp);
        //buscar=(ImageButton) rootView.findViewById(R.id.btn_buscar);
        //btn_usuario=(ImageButton) rootView.findViewById(R.id.btn_usuario);
        //btn_galeria=(ImageButton) rootView.findViewById(R.id.btn_galeria);
        //imageView= rootView.findViewById(R.id.btn_usuario);
        //tx_comentario=(TextView) rootView.findViewById(R.id.tx_comentario);
       // navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        //GlobalVariables.count=5;

        success("","0");

        Gson gson = new Gson();
        getUsuarioModel = gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
        /*if(getUsuarioModel==null){
            getUsuarioModel= new UsuarioModel();
            getUsuarioModel.NroDocumento="1234";
        }*/

        //String url_avatar=GlobalVariables.Url_base+"media/getAvatar/"+getUsuarioModel.NroDocumento+"/fotocarnet.jpg";

       // String url_avatar="https://app.antapaccay.com.pe/HSECWeb/WHSEC_Service/api/media/getAvatar/43054695/fotocarnet.jpg";
        /*
        Glide.with(getContext())
                .load(url_avatar) // add your image url
                .override(50, 50)
                .transform(new CircleTransform(getContext())) // applying the image transformer
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.isUserlogin=true;

                // Crea el nuevo fragmento y la transacción.
                Fragment nuevoFragmento = new FragmentFichaPersonal();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content, nuevoFragmento);
                //transaction.replace(R.id.content, nuevoFragmento);
                transaction.hide(GlobalVariables.fragmentStack.lastElement());

                //transaction.addToBackStack(null);
                // Commit a la transacción
                transaction.commit();
                GlobalVariables.apilarFrag(nuevoFragmento);
                //navigationView.getMenu().findItem(R.id.nav_noticias).setChecked(true);
                //bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.navigation_ficha).setChecked(true);
            }
        });
*/
//opciones de menu y publicacion
/*
        btn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GlobalVariables.ObjectEditable=false;
                Intent intent = new Intent(getContext(),observacion_edit.class);
                intent.putExtra("codObs", "OBS000000XYZ");
                intent.putExtra("tipoObs","TO01");
                intent.putExtra("posTab", 2);
                startActivity(intent);
            }
        });
*/
        btn_facilito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start NewActivity.class
                GlobalVariables.flagFacilito=false;
                Intent myIntent = new Intent(getActivity(), report_obs.class);
                startActivity(myIntent);

            }
        });
        btn_obs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.ObjectEditable=false;
                Intent obserbacion_edit = new Intent(getActivity(),observacion_edit.class);
                obserbacion_edit.putExtra("codObs", "OBS000000XYZ");
                obserbacion_edit.putExtra("tipoObs","TO01");
                obserbacion_edit.putExtra("posTab", 0);
                startActivity(obserbacion_edit);
            }
        });
        btn_insp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.ObjectEditable=false;
                Intent addInspeccion = new Intent(getActivity(),AddInspeccion.class);
                addInspeccion.putExtra("codObs","INSP000000XYZ");
                startActivity(addInspeccion);
            }
        });

        List_muro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"Click en "+position,Toast.LENGTH_SHORT).show();
                String Codigo=GlobalVariables.listaGlobal.get(position).Codigo;
                String Editable=GlobalVariables.listaGlobal.get(position).Editable;
                Intent intent;
                switch (Codigo.substring(0,3)){
                    case "OBS":
                        String tipoObs=GlobalVariables.listaGlobal.get(position).Tipo;
                        intent = new Intent(getActivity(), ActMuroDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("posTab",0);
                        intent.putExtra("tipoObs",tipoObs);
                        startActivity(intent);
                        break;
                    case "INS":
                        intent = new Intent(getActivity(), ActInspeccionDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("posTab",0);
                        //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                        startActivity(intent);
                        break;
                    case "NOT":
                        intent = new Intent(getActivity(), ActNoticiaDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("posTab",0);
                        //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                        startActivity(intent);

                        break;
                    case "OBF":
                        intent = new Intent(getActivity(), obsFacilitoDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("verBoton",Editable);
                        startActivity(intent);

                        break;
                }
                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GlobalVariables.istabs=false;// para que no entre al flag de tabs

                //is_swipe=false;
                swipeRefreshLayout.setRefreshing(true);
                tx_texto.setVisibility(View.VISIBLE);
                upFlag=false;
                downFlag=false;

                // (new Handler()).postDelayed(new Runnable() {
                //    @Override
                //    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        tx_texto.setVisibility(View.VISIBLE);
                        //GlobalVariables.u.clear();

                        GlobalVariables.listaGlobal.clear();
                        //GlobalVariables.contpublic=2;
                        GlobalVariables.flagUpSc=true;
                        GlobalVariables.flag_up_toast=true;
                        GlobalVariables.isFragment=true;
                        paginacion=1;
                        url=GlobalVariables.Url_base+"Muro/GetMuro/"+paginacion+"/"+"7";
                        Utils.isActivity=false;
                        //success(datos,"");

                        //GlobalVariables.count=5;//para que no entre al flag
                        final ActivityController obj = new ActivityController("get-0", url, FragmentMuro.this,getActivity());
                        obj.execute("1");
                       // Toast.makeText(rootView.getContext(),"swipe",Toast.LENGTH_SHORT).show();

                  //  } },0);

            } });

        List_muro.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
               //     if(is_swipe) {

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
                        if (GlobalVariables.listaGlobal.size() != contPublicacion && flag_enter) {
                            GlobalVariables.istabs=false;// para que no entre al flag de tabs

                            //progressBarMain.setVisibility(View.VISIBLE);
                            flag_enter = false;
                            constraintLayout.setVisibility(View.VISIBLE);
                            Utils.isActivity=false;

                            paginacion+=1;
                            url = GlobalVariables.Url_base + "Muro/GetMuro/" + paginacion + "/" + "7";
                            //GlobalVariables.count=5;

                            final ActivityController obj = new ActivityController("get-"+paginacion, url, FragmentMuro.this,getActivity());
                            obj.execute("2");
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
                   // GlobalVariables.stateMuro=List_muro.onSaveInstanceState();
                    // Log.d("x:",""+view.getScrollX());
                    if (listenerFlag && !view.canScrollVertically(1)) {
                        downFlag = true;
                        upFlag = false;

                    }
                    if (listenerFlag && !view.canScrollVertically(-1)) {
                        upFlag = true;
                        downFlag = false;

                    }  }
            });
        listenerFlag = false;

        return rootView;
    }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        ActivityController obj = new ActivityController("get", url, FragmentMuro.this,getActivity());
        obj.execute(""+index);
    }

    @Override
    public void success(String data1,String Tipo) {

    //data add
    if(Tipo.equals("0")){ // from login
        ca = new MuroAdapter(getContext(),GlobalVariables.listaGlobal,this);
        List_muro.setAdapter(ca);
        if(GlobalVariables.stateMuro != null&&GlobalVariables.passHome) {
            swipeRefreshLayout.setEnabled(false);
            List_muro.onRestoreInstanceState(GlobalVariables.stateMuro);
            GlobalVariables.passHome=false;
        }
    }
    else if(Tipo.equals("1")){ //from refresh data
        Gson gson = new Gson();
        GetPublicacionModel getPublicacionModel = gson.fromJson(data1, GetPublicacionModel.class);
        GlobalVariables.listaGlobal=getPublicacionModel.Data;
        ca = new MuroAdapter(getContext(),GlobalVariables.listaGlobal,this);
        List_muro.setAdapter(ca);

        swipeRefreshLayout.setRefreshing(false);
        tx_texto.setVisibility(View.GONE);
        swipeRefreshLayout.setEnabled( false );
    }
    else if(Tipo.equals("2")){ // addd more data
        Gson gson = new Gson();
        constraintLayout.setVisibility(View.GONE);
        flag_enter=true;
        GetPublicacionModel getPublicacionModel = gson.fromJson(data1, GetPublicacionModel.class);
        contPublicacion=getPublicacionModel.Count;
        for(PublicacionModel item:getPublicacionModel.Data)
            ca.add(item);
        ca.notifyDataSetChanged();

    }
    // data remove
    else
    {   if(data1.contains("-1")) Toast.makeText(getContext(), "Ocurrio un error al eliminar registro.",    Toast.LENGTH_SHORT).show();
        else ca.remove(Integer.parseInt(Tipo)-3);
    }

    }

    @Override
    public void successpost(String data,String Tipo) {

    }

    @Override
    public void error(String mensaje,String Tipo) {
        if(flagpopup){
            popupWindow.dismiss();
            flagpopup=false;
        }
        swipeRefreshLayout.setRefreshing(false);
        tx_texto.setVisibility(View.GONE);

        constraintLayout.setVisibility(View.GONE);
        paginacion-=1;
        flag_enter=true;

        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();


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
}
