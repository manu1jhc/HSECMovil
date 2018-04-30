package com.pango.hsec.hsec.Ingresos.Inspecciones;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.FragmentParticipante;
import com.pango.hsec.hsec.PlanAccionEdit;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ListPersonEditAdapter;
import com.pango.hsec.hsec.adapter.ParticipanteAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetEquipoModel;
import com.pango.hsec.hsec.model.GetPersonaModel;
import com.pango.hsec.hsec.model.PersonaModel;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentAddParticipante extends Fragment implements IActivity {
    RecyclerView rec_listResponsables,rec_listAtendidos;
    ImageButton add_responsables,add_atendidos;
    //private ArrayList<PersonaModel> ListResponsables=new ArrayList<>();
    //private ArrayList<PersonaModel> ListAtendidos=new ArrayList<>();
    private ListPersonEditAdapter listPersonAdapter;

    private ListPersonEditAdapter listPersonAdapter2;
    public FragmentAddParticipante() {
        // Required empty public constructor
    }

    private View mView;
    String codInsp,url,url2;
    // TODO: Rename and change types and number of parameters
    public static FragmentAddParticipante newInstance(String sampleText) {
        FragmentAddParticipante fragment = new FragmentAddParticipante();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_participante, container, false);
        codInsp=getArguments().getString("bString");
        rec_listResponsables=mView.findViewById(R.id.rec_listParticipantes);
        rec_listAtendidos=mView.findViewById(R.id.rec_listAtendidos);

        add_responsables=mView.findViewById(R.id.add_responsables);
        add_atendidos=mView.findViewById(R.id.add_atendidos);

        if(GlobalVariables.ObjectEditable){ // load data of server
            //GlobalVariables.ListAtendidos

            if(codInsp!="") {
                url = GlobalVariables.Url_base + "Inspecciones/GetEquipoInspeccion/" + codInsp;
                final ActivityController obj = new ActivityController("get", url, FragmentAddParticipante.this, getActivity());
                obj.execute("1");

                url2 = GlobalVariables.Url_base + "Inspecciones/GetPersonasAtendidas/" + codInsp;
                final ActivityController obj2 = new ActivityController("get", url2, FragmentAddParticipante.this, getActivity());
                obj2.execute("2");
            }

           /* if(GlobalVariables.AddInspeccion.CodInspeccion==null || !GlobalVariables.AddInspeccion.CodInspeccion.equals(codInsp))
            {


            }
            else setdata();*/
        }
        else // new inspeccion
        {
            if(GlobalVariables.AddInspeccion.CodInspeccion==null||!GlobalVariables.AddInspeccion.CodInspeccion.contains("XYZ")){

                //myCalendar = Calendar.getInstance();
                //fecha = myCalendar.getTime();
                GlobalVariables.ListResponsables= new ArrayList<>();
                GlobalVariables.ListAtendidos= new ArrayList<>();
                //GlobalVariables.AddInspeccion.CodInspeccion="-1";//saber si es nuevo
/*
                GlobalVariables.Inspeccion.CodInspeccion= codInsp;
                GlobalVariables.Inspeccion.Fecha="";
                GlobalVariables.Inspeccion.Lugar="";
                GlobalVariables.Inspeccion.CodAreaHSEC=GlobalVariables.Area_obs.get(0).CodTipo;
                GlobalVariables.Inspeccion.CodNivelRiesgo=GlobalVariables.NivelRiesgo_obs.get(0).CodTipo;
                GlobalVariables.Inspeccion.CodTipo=GlobalVariables.Tipo_obs.get(0).CodTipo;
                GlobalVariables.Inspeccion.CodUbicacion="";
                GlobalVariables.Inspeccion.CodSubEstandar=GlobalVariables.Acto_obs.get(0).CodTipo;
                */
            }
            //  else if(!GlobalVariables.Obserbacion.CodObservacion.contains("XYZ"))

            //setdata();
        }


        add_responsables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), B_personas.class);
                startActivityForResult(intent , 1);
            }
        });

        add_atendidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), B_personas.class);
                startActivityForResult(intent , 2);
            }
        });

////////////////////////////////////////////////////////


        LinearLayoutManager horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rec_listResponsables.setLayoutManager(horizontalManager);
        listPersonAdapter = new ListPersonEditAdapter(getActivity(), GlobalVariables.ListResponsables);
        rec_listResponsables.setAdapter(listPersonAdapter);



        LinearLayoutManager horizontalManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rec_listAtendidos.setLayoutManager(horizontalManager2);
        listPersonAdapter2 = new ListPersonEditAdapter(getActivity(), GlobalVariables.ListAtendidos);
        rec_listAtendidos.setAdapter(listPersonAdapter2);


        return mView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) {
                String name=data.getStringExtra("nombreP");
                String codSolicitado=data.getStringExtra("codpersona");

                listPersonAdapter.add(new PersonaModel(data.getStringExtra("codpersona"),data.getStringExtra("nombreP"),data.getStringExtra("dni"),data.getStringExtra("cargo")));

            }
            if (requestCode == 2  && resultCode  == RESULT_OK) {
                listPersonAdapter2.add(new PersonaModel(data.getStringExtra("codpersona"),data.getStringExtra("nombreP"),data.getStringExtra("dni"),data.getStringExtra("cargo")));

            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void success(String data, String Tipo) {


        if (Tipo == "1") {

            Gson gson = new Gson();

            GetPersonaModel getPersonaModel = gson.fromJson(data, GetPersonaModel.class);
            int count=getPersonaModel.Count;
            GlobalVariables.ListResponsables=getPersonaModel.Data;

            LinearLayoutManager horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rec_listResponsables.setLayoutManager(horizontalManager);
            listPersonAdapter = new ListPersonEditAdapter(getActivity(), GlobalVariables.ListResponsables);
            rec_listResponsables.setAdapter(listPersonAdapter);

        }else if(Tipo == "2"){
            Gson gson = new Gson();
            GetPersonaModel getPersonaModel = gson.fromJson(data, GetPersonaModel.class);
            int count=getPersonaModel.Count;
            GlobalVariables.ListAtendidos=getPersonaModel.Data;

            LinearLayoutManager horizontalManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rec_listAtendidos.setLayoutManager(horizontalManager2);
            listPersonAdapter2 = new ListPersonEditAdapter(getActivity(), GlobalVariables.ListAtendidos);
            rec_listAtendidos.setAdapter(listPersonAdapter2);

        }
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
