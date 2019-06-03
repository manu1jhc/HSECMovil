package com.pango.hsec.hsec.Verificaciones;

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
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ObsAdapter;
import com.pango.hsec.hsec.adapter.VerAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.VerificacionModel;

import java.util.Arrays;


public class FragmentCabVerificacion extends Fragment implements IActivity {
    VerAdapter verAdapter;

    String[] obsDetcab={"CodVerificacion","CodAreaHSEC","CodNivelRiesgo","ObservadoPor","Fecha","Hora","Gerencia","Superint","CodUbicacion","CodSubUbicacion","UbicacionEsp","Lugar","CodTipo"};
    String[] obsDetIzq={"Código","Área","Nivel de riesgo","Verificado Por","Fecha","Hora","Gerencia","Superintendencia","Ubicación","Sub Ubicación","Ubicación Específica","Lugar","Tipo"};


    private View mView;
    String codVer;
    String url;
    String jsonVerificaccion="";

    public static FragmentCabVerificacion newInstance(String sampleText) {

        FragmentCabVerificacion f = new FragmentCabVerificacion();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cab_verificacion, container, false);
        codVer=getArguments().getString("bString");
        GlobalVariables.view_fragment=mView;
        url= GlobalVariables.Url_base+"Verificacion/Get/"+ codVer;
        if(jsonVerificaccion.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentCabVerificacion.this,getActivity());
            obj.execute("");
        }else {
            success(jsonVerificaccion,"");
        }
        return mView;
    }


    @Override
    public void success(String data, String Tipo) {

        jsonVerificaccion =data;
        Gson gson = new Gson();
        VerificacionModel getUsuarioModel = gson.fromJson(data, VerificacionModel.class);
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

        verAdapter = new VerAdapter(getContext(),getUsuarioModel,obsDetcab,obsDetIzq);

        ListView listaDetalles = (ListView) mView.findViewById(R.id.list_det);
        listaDetalles.setAdapter(verAdapter);


    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
