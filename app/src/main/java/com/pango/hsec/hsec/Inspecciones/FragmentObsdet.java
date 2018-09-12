package com.pango.hsec.hsec.Inspecciones;


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
import com.pango.hsec.hsec.adapter.ObsDetAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.ObsInspDetModel;
import com.pango.hsec.hsec.model.ObsInspModel;
/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentObsdet extends Fragment implements IActivity {
    ObsDetAdapter obsDetAdapter;
    private View mView;
    String correlativo;
    String url;
    String jsonObsdet="";
    ListView listaDet;
    String[] obsDetcab={"CodInspeccion","NroDetInspeccion","Lugar","CodUbicacion","CodAspectoObs","CodActividadRel","CodNivelRiesgo","Observacion"};
    String[] obsDetIzq={"Código","Nro. Inspección","Lugar","Ubicación","Aspecto observado","Actividad relacionada","Nivel de riesgo","Observación"};



    public FragmentObsdet() {
        // Required empty public constructor
    }


    public static FragmentObsdet newInstance(String sampleText) {
        FragmentObsdet fragment = new FragmentObsdet();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_obsdet, container, false);
         listaDet = (ListView) mView.findViewById(R.id.list_detobsinsp);
        correlativo=getArguments().getString("bString");
        //correlativo="21557";

        GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=true;
        url= GlobalVariables.Url_base+"Inspecciones/GetDetalleInspeccionID/"+correlativo;
        if(jsonObsdet.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentObsdet.this,getActivity());
            obj.execute("");
        }else {
            success(jsonObsdet,"");
        }






        return mView;
    }

    @Override
    public void success(String data, String Tipo) {

        jsonObsdet=data;
        Gson gson = new Gson();
        ObsInspDetModel getObsInspDetModel = gson.fromJson(data, ObsInspDetModel.class);


        obsDetAdapter = new ObsDetAdapter(getContext(),getObsInspDetModel,obsDetcab,obsDetIzq);



        listaDet.setAdapter(obsDetAdapter);


    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
