package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.Busqueda;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.PublicacionAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.UsuarioModel;
import com.pango.hsec.hsec.observacion_edit;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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
    ListView List_muro;
    View rootView;

    //boolean flagFiltro=true;
    //ArrayList<PublicacionModel> listaPublicaciones = new  ArrayList<PublicacionModel>();
    boolean upFlag;
    boolean downFlag;
    boolean listenerFlag;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean flag_enter=true;
    ConstraintLayout constraintLayout;
    boolean loadingTop=false;
    String url;
    TextView tx_texto;
    CardView cardPublicacion;
    ImageButton buscar;
    ImageButton btn_usuario;
    ImageButton btn_galeria;
    int contPublicacion;
    boolean is_swipe=true;
    ImageView imageView;
    int paginacion=1;
    String datos;
    //TextView tx_comentario;
    boolean flagpopup=false;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_muro, container, false);
        GlobalVariables.view_fragment=rootView;
        GlobalVariables.isFragment=true;
        List_muro=rootView.findViewById(R.id.List_muro);
        cardPublicacion=rootView.findViewById(R.id.cardPublicacion);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout);
        constraintLayout=(ConstraintLayout) getActivity().findViewById(R.id.const_main);
        tx_texto =(TextView)rootView.findViewById(R.id.tx_texto);
        buscar=(ImageButton) rootView.findViewById(R.id.btn_buscar);
        btn_usuario=(ImageButton) rootView.findViewById(R.id.btn_usuario);
        btn_galeria=(ImageButton) rootView.findViewById(R.id.btn_galeria);
        imageView=rootView.findViewById(R.id.imageView3);
        //tx_comentario=(TextView) rootView.findViewById(R.id.tx_comentario);

        url=GlobalVariables.Url_base+"Observaciones/GetOBservaciones/-/"+paginacion+"/"+"7";

        GlobalVariables.count=5;
        GlobalVariables.LoadData();


        if(GlobalVariables.listaGlobal.size()==0){
            final ActivityController obj = new ActivityController("get", url, FragmentMuro.this);
            obj.execute("");
        }else{
            PublicacionAdapter ca = new PublicacionAdapter(getContext(),GlobalVariables.listaGlobal);
            List_muro.setAdapter(ca);
        }


        Gson gson = new Gson();
        UsuarioModel getUsuarioModel = gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);

        String url_avatar=GlobalVariables.Url_base+getUsuarioModel.Avatar;

       // String url_avatar="https://app.antapaccay.com.pe/HSECWeb/WHSEC_Service/api/media/getAvatar/43054695/fotocarnet.jpg";
        Glide.with(getContext())
                .load(url_avatar) // add your image url
                .override(50, 50)
                .transform(new CircleTransform(getContext())) // applying the image transformer
                .into(imageView);





//opciones de menu y publicacion
        cardPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obserbacion_edit = new Intent(getContext(),observacion_edit.class);
                startActivity(obserbacion_edit);

            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Click en buscar post",Toast.LENGTH_SHORT).show();
                Intent busquedas = new Intent(getContext(),Busqueda.class);
                startActivity(busquedas);





            }
        });

        btn_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(),"Click en perfil de usuario",Toast.LENGTH_SHORT).show();

            }
        });
        btn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Click en boton camara",Toast.LENGTH_SHORT).show();
             /*   Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
                */
            }
        });


        List_muro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"Click en "+position,Toast.LENGTH_SHORT).show();
                String CodObservacion=GlobalVariables.listaGlobal.get(position).Codigo;

                Intent intent = new Intent(getActivity(), ActMuroDet.class);
                intent.putExtra("codObs",CodObservacion);
                intent.putExtra("posTab",0);

                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                startActivity(intent);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {
                GlobalVariables.istabs=false;

                //is_swipe=false;
                swipeRefreshLayout.setRefreshing(true);
                tx_texto.setVisibility(View.VISIBLE);
                upFlag=false;
                downFlag=false;

                // (new Handler()).postDelayed(new Runnable() {
                //    @Override
                //    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        loadingTop=true;
                        tx_texto.setVisibility(View.VISIBLE);
                        //GlobalVariables.u.clear();

                        GlobalVariables.listaGlobal.clear();
                        //GlobalVariables.contpublic=2;
                        GlobalVariables.flagUpSc=true;
                        GlobalVariables.flag_up_toast=true;
                        GlobalVariables.isFragment=true;
                        paginacion=1;
                        url=GlobalVariables.Url_base+"Observaciones/GetOBservaciones/-/"+paginacion+"/"+"7";
                        //success(datos,"");



                        GlobalVariables.count=5;//para que no entre al flag
                        final ActivityController obj = new ActivityController("get", url, FragmentMuro.this);
                        obj.execute("");
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
                            GlobalVariables.istabs=false;

                            //progressBarMain.setVisibility(View.VISIBLE);
                            flag_enter = false;
                            constraintLayout.setVisibility(View.VISIBLE);
                            GlobalVariables.isFragment=true;
                            paginacion+=1;
                            url = GlobalVariables.Url_base + "Observaciones/GetOBservaciones/-/" + paginacion + "/" + "7";
                            GlobalVariables.count=5;
                            final ActivityController obj = new ActivityController("get", url, FragmentMuro.this);
                            obj.execute("");

                            layoutInflater =(LayoutInflater) rootView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            popupView = layoutInflater.inflate(R.layout.popup_blanco, null);

                            popupWindow = new PopupWindow(popupView,RadioGroup.LayoutParams.MATCH_PARENT ,580,false);
                            popupWindow.showAtLocation(List_muro, Gravity.CENTER, 0, 0);
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
                    }  }
            });
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




    @Override
    public void success(String data1,String Tipo) {

        //datos=data1;

        if(flagpopup){
        popupWindow.dismiss();
            flagpopup=false;
        }

        Gson gson = new Gson();
        //List<PublicacionModel> getPublicacionModel= Arrays.asList(gson.fromJson(data1, PublicacionModel.class));
        //List<Post> posts = Arrays.asList(gson.fromJson(response, Post[].class));
        //UsuarioModel getUsuarioModel = gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);


        GetPublicacionModel getPublicacionModel = gson.fromJson(data1, GetPublicacionModel.class);
        contPublicacion=getPublicacionModel.Count;

        if(GlobalVariables.listaGlobal.size()==0) {
            GlobalVariables.listaGlobal = getPublicacionModel.Data;
            //GlobalVariables.listaGlobal=listaPublicaciones;
        }else{
            //listaPublicaciones.addAll(getPublicacionModel.Data);
            GlobalVariables.listaGlobal.addAll(getPublicacionModel.Data);
        }

       // String a=data1;

        PublicacionAdapter ca = new PublicacionAdapter(getContext(),GlobalVariables.listaGlobal);
        List_muro.setAdapter(ca);


        //ca.notifyDataSetChanged();
        if(GlobalVariables.flagUpSc==true){
            List_muro.setSelection(0);
            GlobalVariables.flagUpSc=false;
        }else
            //reemplazar el 100
        if(GlobalVariables.listaGlobal.size()>7&&GlobalVariables.listaGlobal.size()<contPublicacion) {
            //recListImag.smoothScrollToPosition(GlobalVariables.imagen2.size()-3);
            List_muro.setSelection(GlobalVariables.listaGlobal.size()-8);

        }else if(GlobalVariables.listaGlobal.size()==contPublicacion){
            List_muro.setSelection(GlobalVariables.listaGlobal.size());
        }

        constraintLayout.setVisibility(View.GONE);


        flag_enter=true;
        //GlobalVariables.contpublic += 1;
        // progressDialog.dismiss();
       // progressBar.setVisibility(View.GONE);

        if(loadingTop)
        {
            loadingTop=false;
            swipeRefreshLayout.setRefreshing(false);
            tx_texto.setVisibility(View.GONE);
            //popupWindow.dismiss();
            swipeRefreshLayout.setEnabled( false );
        }

       // GlobalVariables.FDown=false;

    }

    @Override
    public void successpost(String data,String Tipo) {

    }

    @Override
    public void error(String mensaje,String Tipo) {
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
