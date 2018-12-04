package layout;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_observaciones;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.Capacitacion.AsistentesCurso;
import com.pango.hsec.hsec.Ficha.CambiarPassword;
import com.pango.hsec.hsec.Ficha.Capacitaciones;
import com.pango.hsec.hsec.Ficha.Estadisticas;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.MainActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.model.UsuarioModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import org.apache.commons.lang3.StringUtils;

import static android.app.Activity.RESULT_OK;
 //import static com.pango.hsec.hsec.MainActivity.jsonMuro;

public class FragmentFichaPersonal extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    TextView perfil_user,ficha_user,ficha_nombre,ficha_dni,ficha_correo,ficha_empresa,ficha_rol,ficha_area,ficha_tipo,tx_sexo;
    Button btn_estadistica,btn_capacita,btn_buscaruser;
    String url="";
    ImageView ficha_avatar;
    public static final int REQUEST_CODE = 1;
    private BottomNavigationView bottomNavigationView;
    private OnFragmentInteractionListener mListener;

    //scan NFC
    boolean activeNFC=false;
    ImageButton btn_addNFC;
    TextView txtPassword;

    Gson gson ;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_ficha_personal, container, false);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        String ActivarNFC =getArguments().getString("param1","0");
        perfil_user=rootView.findViewById(R.id.Perfil_user);
        ficha_user=rootView.findViewById(R.id.ficha_user);
        ficha_nombre=rootView.findViewById(R.id.ficha_nombre);
        ficha_dni=rootView.findViewById(R.id.ficha_dni);
        ficha_correo=rootView.findViewById(R.id.ficha_correo);
        ficha_empresa=rootView.findViewById(R.id.ficha_empresa);
        ficha_rol=rootView.findViewById(R.id.ficha_rol);
        ficha_area=rootView.findViewById(R.id.ficha_area);
        ficha_tipo=rootView.findViewById(R.id.ficha_tipo);

        btn_addNFC=rootView.findViewById(R.id.btn_addNFC);
        btn_capacita=rootView.findViewById(R.id.btn_capacita);
        btn_estadistica=rootView.findViewById(R.id.btn_estadistica);
        btn_buscaruser=rootView.findViewById(R.id.btn_buscaruser);
        ficha_avatar=rootView.findViewById(R.id.ficha_avatar);
        tx_sexo=rootView.findViewById(R.id.tx_sexo);
        txtPassword=rootView.findViewById(R.id.txtPassword);
        GlobalVariables.view_fragment=rootView;
        GlobalVariables.isFragment=true;
        Utils.isActivity=false;
        gson = new Gson();
        if(GlobalVariables.userLogin.Rol.equals("1")||GlobalVariables.userLogin.Rol.equals("4")){
            btn_buscaruser.setVisibility(View.VISIBLE);
        }
        if(ActivarNFC.equals("0")){
            if (!((MainActivity)getActivity()).existNFC) {
                btn_addNFC.setVisibility(View.GONE);
            }
            else ((MainActivity)getActivity()).nfcAdapter=null;
        }
        else  btn_addNFC.setVisibility(View.GONE);

        if(GlobalVariables.userLoaded.NroDocumento.equals(GlobalVariables.dniUser)) {
            setdata();
        }else {
            //GlobalVariables.dniUser
            //GlobalVariables.userLoaded.NroDocumento=GlobalVariables.dniUser;
            GlobalVariables.token_auth=GlobalVariables.token_auth+"-";
            url = GlobalVariables.Url_base + "FichaPersonal/Informaciongeneral?id="+GlobalVariables.dniUser;
            final ActivityController obj = new ActivityController("get", url, FragmentFichaPersonal.this,getActivity());
            obj.execute("");
        }

        btn_addNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCarnetf(v);
            }
        });

        btn_capacita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.isFragment=true;

                Intent intent = new Intent(getActivity(),Capacitaciones.class );
                intent.putExtra("CodPersona",GlobalVariables.userLoaded.CodPersona);
                startActivity(intent);
            }
        });

        btn_estadistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Estadisticas.class );
                intent.putExtra("CodPersona",GlobalVariables.userLoaded.CodPersona);
                startActivity(intent);
            }
        });

        btn_buscaruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), B_personas.class);
                intent.putExtra("title","Ficha/Usuario");
                startActivityForResult(intent , REQUEST_CODE);
            }
        });

        txtPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CambiarPassword.class);
                //intent.putExtra("title","Ficha/Cambiar Contraseña");
                startActivity(intent);
            }
        });
        return rootView;
    }

    public void setdata(){

        UsuarioModel UserLoged= gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
        if(GlobalVariables.userLoaded.CodPersona.equals(UserLoged.CodPersona)&&UserLoged.Tipo_Autenticacion.trim().equals("B"))
            txtPassword.setVisibility(View.VISIBLE);
        else txtPassword.setVisibility(View.GONE);
        if(GlobalVariables.userLoaded.PerfilCap)
            perfil_user.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorBtnBeige)+">Cumple</font>"));
        else
            perfil_user.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+">No Cumple</font>"));

        ficha_user.setText( GlobalVariables.userLoaded.Codigo_Usuario);
        ficha_nombre.setText( GlobalVariables.userLoaded.Nombres);
        ficha_dni.setText( GlobalVariables.userLoaded.NroDocumento);
        tx_sexo.setText(GlobalVariables.getDescripcion(GlobalVariables.Sexo,GlobalVariables.userLoaded.Sexo.trim()));
        ficha_correo.setText( GlobalVariables.userLoaded.Email);
        ficha_empresa.setText( GlobalVariables.userLoaded.Empresa);

        ficha_rol.setText( GlobalVariables.getDescripcion(GlobalVariables.Roles,GlobalVariables.userLoaded.Rol));

        ficha_area.setText( GlobalVariables.userLoaded.Area);

        if( GlobalVariables.userLoaded.Tipo_Autenticacion==null) {
            ficha_tipo.setText("");
        }else{
            ficha_tipo.setText(GlobalVariables.getDescripcion(GlobalVariables.TipoAutenticacion,  GlobalVariables.userLoaded.Tipo_Autenticacion.trim()));
        }

        String url_avatar=GlobalVariables.Url_base+"media/getAvatar/"+GlobalVariables.userLoaded.NroDocumento.replace("*","").replace(".","")+"/fotocarnet.jpg";
        // String url_avatar="https://app.antapaccay.com.pe/HSECWeb/WHSEC_Service/api/media/getAvatar/43054695/fotocarnet.jpg";
        Glide.with(getContext())
                .load(url_avatar) // add your image url
                .transform(new CircleTransform(getContext())) // applying the image transformer
                .into(ficha_avatar);
    }

    @Override
    public void success(String data, String Tipo) {
        UsuarioModel resp=gson.fromJson(data, UsuarioModel.class);
        if(resp != null){
            if(!StringUtils.isEmpty(resp.Estado)&& resp.Estado.equals("N")) Toast.makeText(getActivity(), "Ficha no encontrada!!", Toast.LENGTH_LONG).show();
            else{
                GlobalVariables.userLoaded=resp;
                setdata();
            }
        }

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

    public void scanCarnetf(View view){
        if(activeNFC) {
            btn_addNFC.setImageResource(R.drawable.ic_nfc); //desactivar scaneo nfc
            ((MainActivity)getActivity()).showNFC(activeNFC);
        }
        else {
            btn_addNFC.setImageResource(R.drawable.ic_activencf);
            ((MainActivity)getActivity()).showNFC(activeNFC);
        }
        activeNFC=!activeNFC;
    }

    public void loadScan(String Urls){
        final ActivityController obj = new ActivityController("get", Urls, FragmentFichaPersonal.this,getActivity());
        obj.execute("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {

                String nombre_obs = data.getStringExtra("nombreP");
                String codpersona_obs = data.getStringExtra("codpersona");
                String dniPersona=data.getStringExtra("dni");
                GlobalVariables.dniUser=dniPersona;
                url = GlobalVariables.Url_base + "FichaPersonal/Informaciongeneral?id="+GlobalVariables.dniUser;
                final ActivityController obj = new ActivityController("get", url, FragmentFichaPersonal.this,getActivity());
                obj.execute("");
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

}
