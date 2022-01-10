package com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.DetalleMACuasi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.Detalle2Adapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.IncidentesMAModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMACuasiDetalle2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMACuasiDetalle2 extends Fragment implements IActivity {
    String[] obsDetcab={"CodTituloInci","DescripcionIncidente","CodTurno","CodContrata","DesSuceso","AccInmediatas"};
    String[] obsDetIzq={"Título del Incidente","Título Detallado","Turno","Contrata","Descripción del Suceso","Acciones Inmediatas"};
    Detalle2Adapter detalle2Adapter;
    String jsonCuasi2="";
    String url;

    String codObs;
    private View mView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMACuasiDetalle2() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentMACuasiDetalle2 newInstance(String sampleText) {
        FragmentMACuasiDetalle2 fragment = new FragmentMACuasiDetalle2();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
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
        mView = inflater.inflate(R.layout.fragment_ma_cuasi_detalle2, container, false);
        codObs=getArguments().getString("bString");

        GlobalVariables.view_fragment=mView;
        url= GlobalVariables.Url_base+"Incidentes/GetDetalleIncidenteID/"+codObs;
        if(jsonCuasi2.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentMACuasiDetalle2.this,getActivity());
            obj.execute("");
        }else {
            success(jsonCuasi2,"");
        }

        return mView;
    }

    @Override
    public void success(String data, String Tipo) {
        jsonCuasi2=data;
        Gson gson = new Gson();
        IncidentesMAModel getMACuasiModel2 = gson.fromJson(data, IncidentesMAModel.class);

        ArrayList<String> obsDetcabf=new ArrayList<>();//
        ArrayList<String> obsDetIzqf=new ArrayList<>();//

        for(int i=0;i<6;i++){
            obsDetcabf.add(obsDetcab[i]);
            obsDetIzqf.add(obsDetIzq[i]);
        }

        Detalle2Adapter detalle2Adapter= new Detalle2Adapter(getContext(),getMACuasiModel2,obsDetcabf,obsDetIzqf);
        ListView listaDetalles = (ListView) mView.findViewById(R.id.list_detMACuasiDet);
        listaDetalles.setAdapter(detalle2Adapter);
    }

    @Override
    public void successpost(String data, String Tipo){

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}