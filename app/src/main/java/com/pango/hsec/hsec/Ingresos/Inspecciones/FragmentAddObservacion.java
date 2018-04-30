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
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.FragmentObsInsp;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ListPersonEditAdapter;
import com.pango.hsec.hsec.adapter.ObsInspAddAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetObsInspModel;
import com.pango.hsec.hsec.model.ObsInspAddModel;
import com.pango.hsec.hsec.model.ObsInspDetModel;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.model.PlanModel;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.pango.hsec.hsec.GlobalVariables.obsInspDetModel;

public class FragmentAddObservacion extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ObsInspAddAdapter listObsInspAdapter;
    //public ArrayList<ObsInspDetModel> ListLocalObs=new ArrayList<>();

    RecyclerView rec_listObservacion;
    ImageButton add_Observacion;
    public FragmentAddObservacion() {
        // Required empty public constructor
    }

    private View mView;
    String codObs,url,url2;
    // TODO: Rename and change types and number of parameters
    public static FragmentAddObservacion newInstance(String sampleText) {
        FragmentAddObservacion fragment = new FragmentAddObservacion();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_observacion, container, false);
        codObs=getArguments().getString("bString");
        rec_listObservacion=mView.findViewById(R.id.rec_listObservacion);
        add_Observacion=mView.findViewById(R.id.add_Observacion);

        if(GlobalVariables.ObjectEditable){ // load data of server
            //GlobalVariables.ListAtendidos
            url= GlobalVariables.Url_base+"Inspecciones/GetDetalleInspeccion/"+codObs;

            if(codObs!="") {
                final ActivityController obj = new ActivityController("get", url, FragmentAddObservacion.this,getActivity());
                obj.execute("");

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




        add_Observacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int tam=GlobalVariables.ListobsInspAddModel.size();
                if(tam==0){
                    GlobalVariables.countObsInsp=1;
                }   else {
                    GlobalVariables.countObsInsp = Integer.parseInt(GlobalVariables.ListobsInspAddModel.get(tam-1).obsInspDetModel.NroDetInspeccion) + 1;
                }
                obsInspDetModel=new ObsInspDetModel();
                Intent intent=new Intent(getContext(),ActObsInspEdit.class);
                intent.putExtra("codObs",codObs);
                intent.putExtra("Editar",false);

                startActivityForResult(intent , 1);


            }
        });



        LinearLayoutManager horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rec_listObservacion.setLayoutManager(horizontalManager);
        listObsInspAdapter = new ObsInspAddAdapter(getActivity(), GlobalVariables.ListobsInspAddModel);
        rec_listObservacion.setAdapter(listObsInspAdapter);

        return mView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) {
                //int obspos=data.getIntExtra("observacion",0);
                //ObsInspDetModel obsInspDetModel=new ObsInspDetModel();
                //obsInspDetModel=(ObsInspDetModel)data.getSerializableExtra("observacion");
                String json_obs=data.getStringExtra("JsonObsInsp");
                ObsInspAddModel obsInspAddModel=new ObsInspAddModel();

                Gson gson = new Gson();
                obsInspAddModel = gson.fromJson(json_obs, ObsInspAddModel.class);

                listObsInspAdapter.add(obsInspAddModel);



/*

               listObsInspAdapter.add(new ObsInspDetModel("",data.getStringExtra("codigo"),data.getStringExtra("Ninspeccion"),data.getStringExtra("lugar"),
                       data.getStringExtra("ub_especifica"),data.getStringExtra("aspecto"),data.getStringExtra("actividad"),data.getStringExtra("nivel"),
                       data.getStringExtra("observacion"), ""));
*/
                //ListLocalObs=GlobalVariables.ListaObsInsp;



            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void success(String data, String Tipo) {

            Gson gson = new Gson();
            GetObsInspModel getObsInspModel=new GetObsInspModel();

             getObsInspModel = gson.fromJson(data, GetObsInspModel.class);
            int count = getObsInspModel.Count;

            for(int i=0;i<count;i++){
                //GlobalVariables.ListobsInspAddModel.get(i).obsInspDetModel.Observacion="xsqfewfwe"; //getObsInspModel.Data.get(i).Observacion;
                ObsInspAddModel obsInspAddModel=new ObsInspAddModel();

                obsInspDetModel.Observacion=getObsInspModel.Data.get(i).Observacion;
                obsInspDetModel.Correlativo=getObsInspModel.Data.get(i).Correlativo;
                obsInspDetModel.CodInspeccion=getObsInspModel.Data.get(i).CodInspeccion;

                obsInspAddModel.obsInspDetModel=obsInspDetModel;

                listObsInspAdapter.add(obsInspAddModel);

            }




    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
