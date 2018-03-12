package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_observaciones;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.Ficha.Capacitaciones;
import com.pango.hsec.hsec.Ficha.Estadisticas;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.MainActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.UsuarioModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import static android.app.Activity.RESULT_OK;
import static com.pango.hsec.hsec.GlobalVariables.paginacion;
import static com.pango.hsec.hsec.MainActivity.jsonMuro;

public class FragmentFichaPersonal extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    TextView ficha_user,ficha_nombre,ficha_dni,ficha_correo,ficha_empresa,ficha_rol,ficha_area,ficha_tipo,tx_sexo;
    Button btn_estadistica,btn_capacita,btn_buscaruser;
    String url="";
    ImageView ficha_avatar;
    CardView cardView3;
    UsuarioModel getUsuarioModel;
    public static final int REQUEST_CODE = 1;
    private BottomNavigationView bottomNavigationView;
    private OnFragmentInteractionListener mListener;

    public FragmentFichaPersonal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFichaPersonal.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFichaPersonal newInstance(String param1, String param2) {
        FragmentFichaPersonal fragment = new FragmentFichaPersonal();
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
        rootView=inflater.inflate(R.layout.fragment_ficha_personal, container, false);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation);

        ficha_user=rootView.findViewById(R.id.ficha_user);
        ficha_nombre=rootView.findViewById(R.id.ficha_nombre);
        ficha_dni=rootView.findViewById(R.id.ficha_dni);
        ficha_correo=rootView.findViewById(R.id.ficha_correo);
        ficha_empresa=rootView.findViewById(R.id.ficha_empresa);
        ficha_rol=rootView.findViewById(R.id.ficha_rol);
        ficha_area=rootView.findViewById(R.id.ficha_area);
        ficha_tipo=rootView.findViewById(R.id.ficha_tipo);

        btn_capacita=rootView.findViewById(R.id.btn_capacita);
        btn_estadistica=rootView.findViewById(R.id.btn_estadistica);
        btn_buscaruser=rootView.findViewById(R.id.btn_buscaruser);
        ficha_avatar=rootView.findViewById(R.id.ficha_avatar);
        tx_sexo=rootView.findViewById(R.id.tx_sexo);
        cardView3=rootView.findViewById(R.id.cardView3);


        if(GlobalVariables.barTitulo){
            cardView3.setVisibility(View.VISIBLE);
        }else{
            cardView3.setVisibility(View.GONE);

        }



        GlobalVariables.view_fragment=rootView;
        GlobalVariables.isFragment=true;
        Utils.isActivity=false;


        if(GlobalVariables.isUserlogin) {
            //cardView3.setVisibility(View.VISIBLE);
            GlobalVariables.isUserlogin=false;
            success(GlobalVariables.json_user,"");
        }else {
            //GlobalVariables.dniUser


            url = GlobalVariables.Url_base + "FichaPersonal/Informaciongeneral/"+GlobalVariables.dniUser;

            final ActivityController obj = new ActivityController("get", url, FragmentFichaPersonal.this);
            obj.execute("");
        }

        btn_capacita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.isFragment=true;

                Intent intent = new Intent(getActivity(),Capacitaciones.class );
                intent.putExtra("CodPersona",getUsuarioModel.CodPersona);
                startActivity(intent);

            }
        });

        btn_estadistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Estadisticas.class );
                intent.putExtra("CodPersona",getUsuarioModel.CodPersona);
                startActivity(intent);
            }
        });




/*
        btn_buscar_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent intent = new Intent(B_observaciones.this, B_personas.class);
                //startActivity(intent);

                Intent intent = new Intent(B_observaciones.this, B_personas.class);
                startActivityForResult(intent , REQUEST_CODE);
            }
        });
        */


        btn_buscaruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), B_personas.class);
                startActivityForResult(intent , REQUEST_CODE);
            }
        });


        return rootView;
    }

    @Override
    public void success(String data, String Tipo) {


        Gson gson = new Gson();
        getUsuarioModel = gson.fromJson(data, UsuarioModel.class);

        GlobalVariables.nombre=getUsuarioModel.Nombres;

        ficha_user.setText(getUsuarioModel.Codigo_Usuario);
        ficha_nombre.setText(getUsuarioModel.Nombres);
        ficha_dni.setText(getUsuarioModel.NroDocumento);
        tx_sexo.setText(GlobalVariables.getDescripcion(GlobalVariables.Sexo,getUsuarioModel.Sexo.trim()));
        ficha_correo.setText(getUsuarioModel.Email);
        ficha_empresa.setText(getUsuarioModel.Empresa);
        ficha_rol.setText(getUsuarioModel.Rol);
        ficha_area.setText(getUsuarioModel.Area);



        if(getUsuarioModel.Tipo_Autenticacion==null) {
            ficha_tipo.setText("");

        }else{
            ficha_tipo.setText(GlobalVariables.getDescripcion(GlobalVariables.TipoAutenticacion, getUsuarioModel.Tipo_Autenticacion.trim()));
        }


        String url_avatar=GlobalVariables.Url_base+"media/getAvatar/"+getUsuarioModel.NroDocumento+"/fotocarnet.jpg";

        // String url_avatar="https://app.antapaccay.com.pe/HSECWeb/WHSEC_Service/api/media/getAvatar/43054695/fotocarnet.jpg";
        Glide.with(getContext())
                .load(url_avatar) // add your image url
                .into(ficha_avatar);

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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);



            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {

                String nombre_obs = data.getStringExtra("nombreP");
                //dni de la persona
                String codpersona_obs = data.getStringExtra("codpersona");
                String dniPersona=data.getStringExtra("dni");
                //id_persona.setText(nombre_obs);
                //Utils.observacionModel.ObservadoPor=codpersona_obs;

                GlobalVariables.dniUser=dniPersona;

                Fragment nuevoFragmento = new FragmentFichaPersonal();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content, nuevoFragmento);
                //transaction.replace(R.id.content, nuevoFragmento);
                transaction.hide(GlobalVariables.fragmentStack.lastElement());

                //transaction.addToBackStack(null);
                // Commit a la transacción
                transaction.commit();
                GlobalVariables.apilarFrag(nuevoFragmento);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().findItem(R.id.navigation_ficha).setChecked(true);

/*
                url = GlobalVariables.Url_base + "FichaPersonal/Informaciongeneral/"+dniPersona;

                final ActivityController obj = new ActivityController("get", url, FragmentFichaPersonal.this);
                obj.execute("");
*/
            }



        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


}
