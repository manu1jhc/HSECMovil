package layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Observaciones.FragmentComent;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.controller.ActivityController;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAvanzado.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAvanzado#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAvanzado extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    View rootView;

    private TextInputEditText tiet_asunto;
    private TextInputLayout til_asunto;
    Button btn_enviar;
    EditText et_mensaje;
    boolean flag_asunto=false;
    boolean flag_mensaje=false;
    String url="";


    public FragmentAvanzado() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAvanzado.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAvanzado newInstance(String param1, String param2) {
        FragmentAvanzado fragment = new FragmentAvanzado();
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
        rootView=inflater.inflate(R.layout.fragment_avanzado, container, false);

        tiet_asunto = (TextInputEditText) rootView.findViewById(R.id.tiet_asunto);
        til_asunto = (TextInputLayout) rootView.findViewById(R.id.til_asunto);
        btn_enviar=(Button)  rootView.findViewById(R.id.btn_enviar);
        et_mensaje=(EditText) rootView.findViewById(R.id.et_mensaje);
        btn_enviar.setEnabled(false);

        GlobalVariables.view_fragment=rootView;
        GlobalVariables.isFragment=true;

        tiet_asunto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //btn_enviar.setEnabled(false);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // btn_enviar.setEnabled(true);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String a=s.toString().trim();
                if(a.equals("")) {
                    btn_enviar.setEnabled(false);
                    flag_asunto=false;
                }else if(flag_mensaje){
                    btn_enviar.setEnabled(true);
                    flag_asunto=true;
                }else{
                    flag_asunto=true;
                }
            }
        });

        et_mensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String a=s.toString().trim();
                if(a.equals("")) {
                    btn_enviar.setEnabled(false);
                    flag_mensaje=false;
                }else if(flag_asunto){
                    btn_enviar.setEnabled(true);
                    flag_mensaje=true;
                }else {flag_mensaje=true;}
            }
        });



        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String asunto=tiet_asunto.getText().toString();
                String mensaje=et_mensaje.getText().toString();


                String json = "";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("Url",asunto);
                    jsonObject.accumulate("Descripcion",mensaje);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                json += jsonObject.toString();
                //http://servidorpango/whsec_Servicedmz/api/Inspecciones/Get/INSP0000008309
                url= GlobalVariables.Url_base+"Usuario/SendFeedback";
                GlobalVariables.isFragment=true;
                final ActivityController obj = new ActivityController("post", url, FragmentAvanzado.this,getActivity());
                obj.execute(json);


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

        int resul=Integer.parseInt(data.substring(1,data.length()-1));

        if ( GlobalVariables.con_status_post!=200||resul==-1||resul==0) {

            //Toast.makeText(contactar,Resultado,Toast.LENGTH_SHORT).show();

            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Error");
            alertDialog.setIcon(R.drawable.erroricon);
            alertDialog.setMessage("Ocurrio un problema durante el envio, inténtelo de nuevo");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();

        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Mensaje enviado");
            alertDialog.setIcon(R.drawable.confirmicon);
            alertDialog.setMessage("Su mensaje ha sido enviado. Cod "+data);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    tiet_asunto.setText("");
                    et_mensaje.setText("");
                    btn_enviar.setEnabled(false);

                    //contactar.finish();
                }
            });
            alertDialog.show();
        }



    }

    @Override
    public void error(String mensaje, String Tipo) {

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
