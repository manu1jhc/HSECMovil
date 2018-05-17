package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.pango.hsec.hsec.Busquedas.B_facilito;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentContactenos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentContactenos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentContactenos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentContactenos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentContactenos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentContactenos newInstance(String param1, String param2) {
        FragmentContactenos fragment = new FragmentContactenos();
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
View mView;
    Button btn_facilito;
    public static final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_contactenos, container, false);
        btn_facilito=mView.findViewById(R.id.btn_facilito);

        btn_facilito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), B_facilito.class);
                startActivityForResult(intent , REQUEST_CODE);

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
