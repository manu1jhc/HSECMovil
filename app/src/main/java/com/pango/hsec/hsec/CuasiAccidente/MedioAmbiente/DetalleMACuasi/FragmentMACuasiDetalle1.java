package com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.DetalleMACuasi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.Detalle1Adapter;
import com.pango.hsec.hsec.adapter.MACuasiAccidenteAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.MACuasiAccidenteModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMACuasiDetalle1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMACuasiDetalle1 extends Fragment implements IActivity {
    String[] obsDetcab={"CodCuasiAcci","CodAreaHSEC","CodTipo","ObservadoPor","Gerencia","Superint","ClasReal","ClasPotencial","ActRelacionada","GrupRiesgo","Fecha","Hora","CodUbicacion","CodSubUbicacion","UbicacionEsp","Lugar"};
    String[] obsDetIzq={"Código","Área","Tipo","Persona que Reporta","Gerencia","Superintendencia","Clasificación Real","Clasificación Potencial","Actividad Relacionada","Grupo de Riesgo","Fecha","Hora","Ubicación","Sub Ubicación","Sub Ubicación Específica","Lugar"};
    Detalle1Adapter detalle1Adapter;
    String jsonCuasi="";
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

    public FragmentMACuasiDetalle1() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentMACuasiDetalle1 newInstance(String sampleText) {
        FragmentMACuasiDetalle1 fragment = new FragmentMACuasiDetalle1();
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
        mView = inflater.inflate(R.layout.fragment_ma_cuasi_detalle1, container, false);
        codObs=getArguments().getString("bString");

        GlobalVariables.view_fragment=mView;
        url= GlobalVariables.Url_base+"Observaciones/Get/"+codObs;
        if(jsonCuasi.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentMACuasiDetalle1.this,getActivity());
            obj.execute("");
        }else {
            success(jsonCuasi,"");
        }

        return mView;
    }

    @Override
    public void success(String data, String Tipo) {
        jsonCuasi=data;
        Gson gson = new Gson();
        MACuasiAccidenteModel getMACuasiModel = gson.fromJson(data, MACuasiAccidenteModel.class);

        ArrayList<String> obsDetcabf=new ArrayList<>();//
        ArrayList<String> obsDetIzqf=new ArrayList<>();//

        for(int i=0;i<16;i++){
            Boolean pass=true;
           /* switch (i){
                case 2:
                    if(StringUtils.isEmpty(getMACuasiModel.SuperInt))pass=false;
                    break;
            }*/
            if(pass){
                obsDetcabf.add(obsDetcab[i]);
                obsDetIzqf.add(obsDetIzq[i]);
            }
        }

        Detalle1Adapter detalle1Adapter= new Detalle1Adapter(getContext(),getMACuasiModel,obsDetcabf,obsDetIzqf);

        ListView listaDetalles = (ListView) mView.findViewById(R.id.list_detMACuasi);
        listaDetalles.setAdapter(detalle1Adapter);
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}