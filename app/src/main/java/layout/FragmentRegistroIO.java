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

import com.pango.hsec.hsec.Facilito.report_obs;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.observacion_edit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRegistroIO.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRegistroIO#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRegistroIO extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btn_obs,btn_newobs;
    private static View mView;

    private OnFragmentInteractionListener mListener;

    public FragmentRegistroIO() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRegistroIO.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRegistroIO newInstance(String param1, String param2) {
        FragmentRegistroIO fragment = new FragmentRegistroIO();
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

        mView= inflater.inflate(R.layout.fragment_registro_io, container, false);
        btn_obs=(Button) mView.findViewById(R.id.btn_obs);
        btn_newobs=(Button) mView.findViewById(R.id.btn_observacion);
        btn_obs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(getActivity(), report_obs.class);
                startActivity(myIntent);
            }
        });
        btn_newobs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                GlobalVariables.ObjectEditable=false;
                Intent obserbacion_edit = new Intent(getContext(),observacion_edit.class);
                obserbacion_edit.putExtra("codObs", "OBS000000XYZ");
                obserbacion_edit.putExtra("posTab", 0);
                startActivity(obserbacion_edit);
            }
        });
        return  mView;
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
}
