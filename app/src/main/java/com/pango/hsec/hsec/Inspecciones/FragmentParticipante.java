package com.pango.hsec.hsec.Inspecciones;

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
import com.pango.hsec.hsec.adapter.ParticipanteAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetEquipoModel;


public class FragmentParticipante extends Fragment implements IActivity {
    private View mView;
    String codObs;
    String url,url2;
    ParticipanteAdapter participanteAdapter;
    String jsonEquipo="";
    String jsonParticipante="";

    public FragmentParticipante() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentParticipante newInstance(String sampleText) {
        FragmentParticipante fragment = new FragmentParticipante();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_participante, container, false);
        codObs=getArguments().getString("bString");
        //GlobalVariables.count=1;
        GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=true;

        //codObs="INSP0000008302";
        url= GlobalVariables.Url_base+"Inspecciones/GetEquipoInspeccion/"+codObs;

        if(jsonEquipo.isEmpty()) {
            GlobalVariables.istabs=true;

            final ActivityController obj = new ActivityController("get", url, FragmentParticipante.this,getActivity());
            obj.execute("1");
        }else{
            success(jsonEquipo,"1");
        }

        url2=GlobalVariables.Url_base+"Inspecciones/GetPersonasAtendidas/"+codObs;

        if(jsonParticipante.isEmpty()) {
            final ActivityController obj2 = new ActivityController("get", url2, FragmentParticipante.this,getActivity());
            obj2.execute("2");
        }else{
            success(jsonParticipante,"2");
        }



        return mView;
    }


    @Override
    public void success(String data, String Tipo) {

        if (Tipo == "1") {
            jsonEquipo=data;

            Gson gson = new Gson();
            GetEquipoModel getEquipoModel = gson.fromJson(data, GetEquipoModel.class);
            int count=getEquipoModel.Count;


            participanteAdapter = new ParticipanteAdapter(getContext(),getEquipoModel.Data,count,Tipo);
            ListView list_equipo;
            list_equipo= (ListView) mView.findViewById(R.id.list_equipo);
            list_equipo.setAdapter(participanteAdapter);


        }else if(Tipo == "2"){
            jsonParticipante=data;
            Gson gson = new Gson();
            GetEquipoModel getEquipoModel = gson.fromJson(data, GetEquipoModel.class);
            int count=getEquipoModel.Count;
            ListView list_atendidos;
            participanteAdapter = new ParticipanteAdapter(getContext(),getEquipoModel.Data,count,Tipo);

            list_atendidos= (ListView) mView.findViewById(R.id.list_atendidos);
            list_atendidos.setAdapter(participanteAdapter);

        }




    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
