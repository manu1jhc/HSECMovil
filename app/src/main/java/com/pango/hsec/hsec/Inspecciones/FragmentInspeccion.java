package com.pango.hsec.hsec.Inspecciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Observaciones.FragmentObs;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.InspAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.InspeccionModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;



public class FragmentInspeccion extends Fragment implements IActivity {
    String[] obsDetcab={"CodInspeccion","Gerencia","SuperInt","CodContrata","FechaP","Fecha","Hora","CodUbicacion","CodSubUbicacion","CodTipo"};
    String[] obsDetIzq={"Codigo","Gerencia","Superintendencia","Contrata","Fecha programada","Fecha de inspección","Hora","Ubicación","Sub Ubicacion","Tipo de Inspección"};
    InspAdapter inspAdapter;
    String jsonInspeccion="";
    public FragmentInspeccion() {
        // Required empty public constructor
    }

    private View mView;
    String codObs;
    // TODO: Rename and change types and number of parameters
    public static FragmentInspeccion newInstance(String sampleText) {
        FragmentInspeccion fragment = new FragmentInspeccion();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }

    String url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_inspeccion, container, false);
        codObs=getArguments().getString("bString");
        //codObs="INSP0000008508";

        //GlobalVariables.count=1;
        GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=true;
        url= GlobalVariables.Url_base+"Inspecciones/Get/"+codObs;
        if(jsonInspeccion.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentInspeccion.this,getActivity());
            obj.execute("");
        }else {
            success(jsonInspeccion,"");
        }

        return mView;
    }


    @Override
    public void success(String data, String Tipo) {
        jsonInspeccion=data;
        Gson gson = new Gson();
        InspeccionModel getInspeccionModel = gson.fromJson(data, InspeccionModel.class);

        ArrayList<String> obsDetcabf=new ArrayList<>();//  {"CodInspeccion","Gerencia"}; obsDetcab={"CodInspeccion","Gerencia","SuperInt","CodContrata","FechaP","Fecha","Hora","CodUbicacion","CodSubUbicacion","CodTipo"};
        ArrayList<String> obsDetIzqf=new ArrayList<>();// obsDetIzq={"Codigo","Gerencia","Superintendencia","Contrata","Fecha programada","Fecha de inspección","Hora","Ubicación","Sub Ubicacion","Tipo de Inspección"};

        for(int i=0;i<10;i++){
            Boolean pass=true;
            switch (i){
                case 2:
                    if(StringUtils.isEmpty(getInspeccionModel.SuperInt))pass=false;
                    break;
                case 3:
                    if(StringUtils.isEmpty(getInspeccionModel.CodContrata))pass=false;
                    break;
                case 8:
                    if(StringUtils.isEmpty(getInspeccionModel.CodSubUbicacion))pass=false;
                    break;
            }
            if(pass){
                obsDetcabf.add(obsDetcab[i]);
                obsDetIzqf.add(obsDetIzq[i]);
            }
        }

        inspAdapter = new InspAdapter(getContext(),getInspeccionModel,obsDetcabf,obsDetIzqf);

        ListView listaDetalles = (ListView) mView.findViewById(R.id.list_detinsp);
        listaDetalles.setAdapter(inspAdapter);




    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
