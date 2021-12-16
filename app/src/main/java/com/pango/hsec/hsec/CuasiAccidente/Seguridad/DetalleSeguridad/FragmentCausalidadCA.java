package com.pango.hsec.hsec.CuasiAccidente.Seguridad.DetalleSeguridad;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.CausalidadAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetCausalidadModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCausalidadCA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCausalidadCA extends Fragment implements IActivity {

    String jsonCausaidad="";
    private View mView;
    String codObs,url="";
    RecyclerView recCaus;

    // TODO: Rename and change types and number of parameters
    public static FragmentCausalidadCA newInstance(String sampleText) {
        FragmentCausalidadCA f = new FragmentCausalidadCA();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_causalidad_ca, container, false);
        codObs=getArguments().getString("bString");
        recCaus = mView.findViewById(R.id.rec_causalidad);

        GlobalVariables.view_fragment=mView;
        url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codObs;
        if(jsonCausaidad.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentCausalidadCA.this,getActivity());
            obj.execute("");
        }else {
            success(jsonCausaidad,"");
        }
        return mView;

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
    public void success(String data, String Tipo)  {
        jsonCausaidad=data;
        Gson gson = new Gson();
        GetCausalidadModel getCausalidadModel = gson.fromJson(data, GetCausalidadModel.class);

        recCaus.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recCaus.setLayoutManager(llm);

        CausalidadAdapter causalidadSegAdapter= new CausalidadAdapter(getCausalidadModel.Data);
        // ListView listaDetalles = (ListView) mView.findViewById(R.id.list_detSegCA);
        recCaus.setAdapter(causalidadSegAdapter);

    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentCausalidadCA.OnFragmentInteractionListener mListener;
    public FragmentCausalidadCA() {
        // Required empty public constructor
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCausalidadCA.OnFragmentInteractionListener) {
            mListener = (FragmentCausalidadCA.OnFragmentInteractionListener) context;
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
*/


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
}