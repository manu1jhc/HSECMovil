package com.pango.hsec.hsec.Ingresos.Inspecciones;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.Busquedas.B_personasM;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.FragmentParticipante;
import com.pango.hsec.hsec.PlanAccionEdit;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ListEquipoAdapter;
import com.pango.hsec.hsec.adapter.ListPersonEditAdapter;
import com.pango.hsec.hsec.adapter.ParticipanteAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.EquipoModel;
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
    public ListEquipoAdapter listPersonAdapter,listPersonAdapter2;
    public FragmentAddParticipante() {
        // Required empty public constructor
    }

    private View mView;
    String codInsp,url,url2;
    TextView tx_realizan_insp;
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
        tx_realizan_insp=mView.findViewById(R.id.tx_realizan_insp);
        tx_realizan_insp.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Personas que realizan la inspección:"));



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
            else setdata();
        }
        else // new inspeccion
        {
            if(GlobalVariables.AddInspeccion.CodInspeccion==null){ //||!GlobalVariables.AddInspeccion.CodInspeccion.contains("XYZ")
                GlobalVariables.ListResponsables= new ArrayList<>();
                GlobalVariables.ListAtendidos= new ArrayList<>();
            }
            setdata();
        }


        add_responsables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), B_personasM.class);
                startActivityForResult(intent , 1);
            }
        });

        add_atendidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), B_personasM.class);
                startActivityForResult(intent , 2);
            }
        });
////////////////////////////////////////////////////////

        return mView;
    }

    public void setdata(){
        LinearLayoutManager horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rec_listResponsables.setLayoutManager(horizontalManager);
        listPersonAdapter = new ListEquipoAdapter(getActivity(), GlobalVariables.ListResponsables,true);
        rec_listResponsables.setAdapter(listPersonAdapter);

        LinearLayoutManager horizontalManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rec_listAtendidos.setLayoutManager(horizontalManager2);
        listPersonAdapter2 = new ListEquipoAdapter(getActivity(), GlobalVariables.ListAtendidos,false);
        rec_listAtendidos.setAdapter(listPersonAdapter2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) {
                String name=data.getStringExtra("nombreP");
                String codSolicitado=data.getStringExtra("codpersona");
                for(PersonaModel item: GlobalVariables.lista_Personas)
                    if(item.Check) listPersonAdapter.add(new EquipoModel(item));
                listPersonAdapter.notifyDataSetChanged();
               // listPersonAdapter.add(new EquipoModel(data.getStringExtra("codpersona"),data.getStringExtra("nombreP"),data.getStringExtra("dni"),data.getStringExtra("cargo")));

            }
            if (requestCode == 2  && resultCode  == RESULT_OK) {
                //listPersonAdapter2.add(new EquipoModel(data.getStringExtra("codpersona"),data.getStringExtra("nombreP"),data.getStringExtra("dni"),data.getStringExtra("cargo")));
                for(PersonaModel item: GlobalVariables.lista_Personas)
                    if(item.Check) listPersonAdapter2.add(new EquipoModel(item));
                listPersonAdapter2.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    ArrayList<Integer> actives= new ArrayList<>();
    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {


        if (Tipo == "1") {

            Gson gson = new Gson();
            GlobalVariables.ListResponsables=gson.fromJson(data, GetEquipoModel.class).Data;
            for(EquipoModel item:GlobalVariables.ListResponsables)
                GlobalVariables.StrResponsables.add((EquipoModel)item.clone());
                //GlobalVariables.StrResponsables=(ArrayList<EquipoModel>)GlobalVariables.ListResponsables.clone();
            actives.add(1);

        }else if(Tipo == "2"){
            Gson gson = new Gson();
            GlobalVariables.ListAtendidos=gson.fromJson(data, GetEquipoModel.class).Data;
            for(EquipoModel item:GlobalVariables.ListAtendidos)
                GlobalVariables.StrAtendidos.add((EquipoModel)item.clone());
            //GlobalVariables.StrAtendidos=(ArrayList<EquipoModel>)GlobalVariables.ListAtendidos.clone();
            actives.add(1);
        }
        if(actives.size()==2)setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
