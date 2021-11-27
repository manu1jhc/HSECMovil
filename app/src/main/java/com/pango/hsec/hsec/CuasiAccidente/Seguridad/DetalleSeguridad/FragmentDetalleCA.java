package com.pango.hsec.hsec.CuasiAccidente.Seguridad.DetalleSeguridad;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.DetalleMACuasi.FragmentMACuasiDetalle2;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.Detalle2Adapter;
import com.pango.hsec.hsec.adapter.DetalleSegAdapter;
import com.pango.hsec.hsec.adapter.GeneralSegAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.MACuasiAccidenteModel;
import com.pango.hsec.hsec.model.SeguridadCAModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDetalleCA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDetalleCA extends Fragment implements IActivity {
    String[] obsDetcab={"TituIncidente","TituDetallado","Turno","Contrata","Conclusiones","Aprendizaje","ResumenIM"};
    String[] obsDetIzq={"Título del Incidente","Título Detallado","Turno","Contrata","Conclusiones","Aprendizaje", "Resumen de Informe Médico"};
    Detalle2Adapter detalle2Adapter;
    String jsonSegDet="";
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

    public FragmentDetalleCA() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentDetalleCA newInstance(String sampleText) {
        FragmentDetalleCA fragment = new FragmentDetalleCA();
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
        mView = inflater.inflate(R.layout.fragment_detalle_ca, container, false);
        codObs=getArguments().getString("bString");

        GlobalVariables.view_fragment=mView;
        url= GlobalVariables.Url_base+"Observaciones/Get/"+codObs;
        if(jsonSegDet.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentDetalleCA.this,getActivity());
            obj.execute("");
        }else {
            success(jsonSegDet,"");
        }

        return mView;
    }

    @Override
    public void success(String data, String Tipo){
        jsonSegDet=data;
        Gson gson = new Gson();
        SeguridadCAModel getSeguridadModel = gson.fromJson(data, SeguridadCAModel.class);

        ArrayList<String> obsDetcabf=new ArrayList<>();//
        ArrayList<String> obsDetIzqf=new ArrayList<>();//

        for(int i=0;i<7;i++){
            Boolean pass=true;
            /*
            switch (i){
                case 2:
                    if(StringUtils.isEmpty(getSeguridadModel.SuperInt))pass=false;
                    break;
            }*/
            if(pass){
                obsDetcabf.add(obsDetcab[i]);
                obsDetIzqf.add(obsDetIzq[i]);
            }
        }

        DetalleSegAdapter generalSegAdapter= new DetalleSegAdapter(getContext(),getSeguridadModel,obsDetcabf,obsDetIzqf);

        ListView listaDetalles = (ListView) mView.findViewById(R.id.list_detSegCA);
        listaDetalles.setAdapter(generalSegAdapter);
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}