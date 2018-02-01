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

import java.util.Arrays;

import static com.pango.hsec.hsec.Inspecciones.ActInspeccionDet.jsonInspeccion;


public class FragmentInspeccion extends Fragment implements IActivity {
    String[] obsDetcab={"CodInspeccion","Gerencia","SuperInt","CodContrata","FechaP","Fecha","Hora","CodUbicacion","CodSubUbicacion","CodTipo"};
    String[] obsDetIzq={"Codigo","Gerencia","Superintendencia","Contrata","Fecha programada","Fecha de inspección","Hora","Ubicación","Sub Ubicacion","Tipo de Inspección"};
    InspAdapter inspAdapter;
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
        //codObs=getArguments().getString("bString");
        codObs="INSP0000008508";

        GlobalVariables.count=1;
        GlobalVariables.view_fragment=mView;
        GlobalVariables.isFragment=true;
        url= GlobalVariables.Url_base+"Inspecciones/Get/"+codObs;
        if(jsonInspeccion.isEmpty()) {
            final ActivityController obj = new ActivityController("get", url, FragmentInspeccion.this);
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
        String[] parts = new String[0];

        if(getInspeccionModel.CodUbicacion!=null) {
            parts = getInspeccionModel.CodUbicacion.split("\\.");
        }



        if(parts.length==1||parts.length==0){
            for(int i=0;i<obsDetcab.length;i++){
                if(obsDetcab[i].equals("CodSubUbicacion")){

                    for (int j = i; j < obsDetcab.length - 1; j++) {
                        obsDetcab[j] = obsDetcab[j+1];
                        obsDetIzq[j]=obsDetIzq[j+1];
                        //obsDetIzq[j+1]=obsDetIzq[j+2];
                    }
                    obsDetcab[obsDetcab.length - 1] = "";
                    //obsDetcab[obsDetcab.length - 2] = "";

                    obsDetcab = Arrays.copyOf(obsDetcab,obsDetcab.length-1);


                    obsDetIzq[obsDetIzq.length - 1] = "";
                    //obsDetIzq[obsDetIzq.length - 2] = "";
                    obsDetIzq = Arrays.copyOf(obsDetIzq,obsDetIzq.length-1);


                }

            }

        }


        inspAdapter = new InspAdapter(getContext(),getInspeccionModel,obsDetcab,obsDetIzq);

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
