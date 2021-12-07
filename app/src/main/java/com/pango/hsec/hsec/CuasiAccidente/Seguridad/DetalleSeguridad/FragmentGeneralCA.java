package com.pango.hsec.hsec.CuasiAccidente.Seguridad.DetalleSeguridad;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.DetalleMACuasi.FragmentMACuasiDetalle1;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.GeneralSegAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.SeguridadCAModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGeneralCA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGeneralCA extends Fragment implements IActivity {

    String[] obsDetcab={"CodSegIn","CodAreaHSEC","CodTipo","CodSubTipo","ObservadoPor","Gerencia","Superint","ClasReal","ClasPotencial","ActRelacionada","HHRelacionada","GrupRiesgo","Riesgo","Fecha","Hora","CodUbicacion","CodSubUbicacion","UbicacionEsp","Lugar"};
    String[] obsDetIzq={"Código Incidente","Área","Tipo","Subtipo","Persona que Reporta","Gerencia","Superintendencia","Clasificación Real","Clasificación Potencial","Activiad Relacionada","HHR Relacionada","Grupo de Riesgo","Riesgo","Fecha","Hora","Ubicación","Sub Ubicación","Sub Ubicación Específica","Lugar"};
    String jsonSec="";
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

    public FragmentGeneralCA() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentGeneralCA newInstance(String sampleText) {
        FragmentGeneralCA fragment = new FragmentGeneralCA();
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
        mView = inflater.inflate(R.layout.fragment_general_ca, container, false);
        codObs=getArguments().getString("bString");

        GlobalVariables.view_fragment=mView;
        url= GlobalVariables.Url_base+"Observaciones/Get/"+codObs;
        if(jsonSec.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentGeneralCA.this,getActivity());
            obj.execute("");
        }else {
            success(jsonSec,"");
        }
        return mView;
    }

    @Override
    public void success(String data, String Tipo) {
        jsonSec=data;
        Gson gson = new Gson();
        SeguridadCAModel getSeguridadModel = gson.fromJson(data, SeguridadCAModel.class);

        ArrayList<String> obsDetcabf=new ArrayList<>();//
        ArrayList<String> obsDetIzqf=new ArrayList<>();//

        for(int i=0;i<19;i++){
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

        GeneralSegAdapter generalSegAdapter= new GeneralSegAdapter(getContext(),getSeguridadModel,obsDetcabf,obsDetIzqf);

        ListView listaDetalles = (ListView) mView.findViewById(R.id.list_generalCA);
        listaDetalles.setAdapter(generalSegAdapter);
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}