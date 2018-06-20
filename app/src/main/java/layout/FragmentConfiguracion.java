package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetMaestroModel;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.Maestro;

import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentConfiguracion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentConfiguracion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConfiguracion extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Button btn_recarga;
    String url="";
    View mView;
    public FragmentConfiguracion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConfiguracion.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConfiguracion newInstance(String param1, String param2) {
        FragmentConfiguracion fragment = new FragmentConfiguracion();
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
        mView = inflater.inflate(R.layout.fragment_configuracion, container, false);
        url=GlobalVariables.Url_base+"Maestro/GetTipoMaestro/ALL";

        btn_recarga=mView.findViewById(R.id.btn_recarga);
        btn_recarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ActivityController obj = new ActivityController("get", url, FragmentConfiguracion.this, getActivity());
                obj.execute("");


            }
        });

        return mView;
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
    public void success(String data, String Tipo) throws CloneNotSupportedException {

        cleanData();
        Setdata(data);

    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    public void cleanData(){
        GlobalVariables.Ubicaciones_obs.clear();
        GlobalVariables.Gerencia.clear();
        GlobalVariables.SuperIntendencia.clear();
        GlobalVariables.Contrata.clear();
        GlobalVariables.HHA_obs.clear();
        GlobalVariables.Actividad_obs.clear();
        GlobalVariables.Tipo_obs2.clear();
        GlobalVariables.Estado_obs.clear();
        GlobalVariables.Error_obs.clear();
        GlobalVariables.Aspecto_Obs.clear();
        GlobalVariables.Tipo_insp.clear();
        GlobalVariables.Area_obs.clear();
        GlobalVariables.Tipo_Plan.clear();
        GlobalVariables.Roles.clear();

        GlobalVariables.C_Empresa.clear();
        GlobalVariables.C_Lugar.clear();
        GlobalVariables.C_Tema.clear();
        GlobalVariables.C_Tipo.clear();
        GlobalVariables.C_Sala.clear();
    }


    public void Setdata(String data){
        Gson gson = new Gson();
        GetMaestroModel getMaestroModel = gson.fromJson(data, GetMaestroModel.class);
        int count=getMaestroModel.Count;
        if(count!=0) {

            SharedPreferences VarMaestros = (getActivity()).getSharedPreferences("HSEC_Maestros", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = VarMaestros.edit();
            editor.putString("MaestroAll", data);
            editor.commit();

            for (Maestro item : getMaestroModel.Data) {
                switch (item.Codigo) {
                    case "UBIC":
                        GlobalVariables.Ubicaciones_obs.add(item);
                        break;
                    case "GERE":
                        GlobalVariables.Gerencia.add(item);
                        break;
                    case "SUPE":
                        GlobalVariables.SuperIntendencia.add(item);
                        break;
                    case "PROV":
                        GlobalVariables.Contrata.add(item);
                        break;
                    //observacion
                    case "HHAR":
                        GlobalVariables.HHA_obs.add(item);
                        break;
                    case "ACTR":
                        GlobalVariables.Actividad_obs.add(item);
                        break;
                    case "TPOB":
                        GlobalVariables.Tipo_obs2.add(item);
                        break;
                    case "ESOB":
                        GlobalVariables.Estado_obs.add(item);
                        break;
                    case "EROB":
                        GlobalVariables.Error_obs.add(item);
                        break;
                    //inspecciones
                    case "ASPO":
                        GlobalVariables.Aspecto_Obs.add(item);
                        break;
                    case "TPIN":
                        GlobalVariables.Tipo_insp.add(item);
                        break;
                    //Plan de Accion
                    case "AREA":
                        GlobalVariables.Area_obs.add(item);
                        break;
                    case "TPAC":
                        GlobalVariables.Tipo_Plan.add(item);
                        break;
                    case "TROL":
                        GlobalVariables.Roles.add(item);
                        break;


                    case "CEMP":
                        GlobalVariables.C_Empresa.add(item);
                        break;
                    case "CLUG":
                        GlobalVariables.C_Lugar.add(item);
                        break;
                    case "CTEM":
                        GlobalVariables.C_Tema.add(item);
                        break;
                    case "CTIP":
                        GlobalVariables.C_Tipo.add(item);
                        break;
                    case "CSAL":
                        GlobalVariables.C_Tipo.add(item);
                        break;
                /*default:
                    break;*/
                }
            }

            GlobalVariables.Ubicacion_obs = GlobalVariables.loadUbicacion("", 1);
        }else {
            Toast.makeText(getActivity(),"Ocurrio un error al cargar datos del sistemas",Toast.LENGTH_SHORT).show();
        }
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
