package com.pango.hsec.hsec.Facilito;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ObsAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.ObservacionModel;

import java.util.Arrays;

import layout.FragmentMuro;

public class ObsFacilito extends Fragment implements IActivity {
    ObsAdapter obsAdapter;
    String[] obsDetcab={"CodObsFacilito","Persona","Tipo","UbicacionExacta","Observacion","Accion","FecCreacion","Gerencia","Superint"};
    String[] obsDetIzq={"Codigo","Observado por","Tipo de Observación","Ubicacion Exacta","Observacion","Accion a tomar","Fecha Creada","Gerencia","Superintendencia"};
    String jsonObservacion="";
    private View mView;
    String codObs;


    public static ObsFacilito newInstance(String sampleText) {
        ObsFacilito f = new ObsFacilito();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }

    ////////////////////////////////////////////////////////
    String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs, container, false);
        //GlobalVariables.count=1;
        GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=true;

        codObs=getArguments().getString("bString");
        url= GlobalVariables.Url_base+"Observaciones/Get/"+codObs;

        if(jsonObservacion.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, ObsFacilito.this,getActivity());
            obj.execute("");
        }else {
            success(jsonObservacion,"");
        }
        return mView;
    }


    @Override
    public void success(String data,String Tipo) {
        jsonObservacion =data;
        Gson gson = new Gson();
        ObservacionModel getUsuarioModel = gson.fromJson(data, ObservacionModel.class);
        String[] parts = new String[0];




        if(getUsuarioModel.CodUbicacion!=null) {
            parts = getUsuarioModel.CodUbicacion.split("\\.");

        }

        if(parts.length==1||parts.length==0){
            for(int i=0;i<obsDetcab.length;i++){
                if(obsDetcab[i].equals("CodSubUbicacion")){

                    for (int j = i; j < obsDetcab.length - 2; j++) {
                        obsDetcab[j] = obsDetcab[j+2];
                        obsDetIzq[j]=obsDetIzq[j+2];
                        //obsDetIzq[j+1]=obsDetIzq[j+2];
                    }
                    obsDetcab[obsDetcab.length - 1] = "";
                    obsDetcab[obsDetcab.length - 2] = "";

                    obsDetcab = Arrays.copyOf(obsDetcab,obsDetcab.length-2);


                    obsDetIzq[obsDetIzq.length - 1] = "";
                    obsDetIzq[obsDetIzq.length - 2] = "";
                    obsDetIzq = Arrays.copyOf(obsDetIzq,obsDetIzq.length-2);


                }

            }

        }else if(parts.length==2){

            for(int i=0;i<obsDetcab.length;i++){
                if(obsDetcab[i].equals("UbicacionEsp")){

                    for (int j = i; j < obsDetcab.length - 1; j++) {
                        obsDetcab[j] = obsDetcab[j+1];
                        obsDetIzq[j]=obsDetIzq[j+1];

                    }
                    obsDetcab[obsDetcab.length - 1] = "";
                    obsDetcab = Arrays.copyOf(obsDetcab,obsDetcab.length-1);

                    obsDetIzq[obsDetIzq.length - 1] = "";
                    obsDetIzq = Arrays.copyOf(obsDetIzq,obsDetIzq.length-1);

                }
            }
        }

        obsAdapter = new ObsAdapter(getContext(),getUsuarioModel,obsDetcab,obsDetIzq);

        ListView listaDetalles = (ListView) mView.findViewById(R.id.list_det);
        listaDetalles.setAdapter(obsAdapter);

    }

    @Override
    public void successpost(String data,String Tipo) {

    }

    @Override
    public void error(String mensaje,String Tipo) {


    }
}