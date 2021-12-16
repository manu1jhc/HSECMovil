package com.pango.hsec.hsec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Ingresos.Inspecciones.ActObsInspEdit;
import com.pango.hsec.hsec.adapter.PlanEditAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPlanModel;
import com.pango.hsec.hsec.model.PlanModel;
import com.pango.hsec.hsec.model.UsuarioModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class obs_planaccion extends Fragment implements IActivity{

    private static View mView;
    private RecyclerView listPlan;
    public PlanEditAdapter listViewAdapter;
    private  Gson gson;
    private String Tipo;
    SimpleDateFormat df;
    Calendar myCalendar;
    public static final com.pango.hsec.hsec.obs_planaccion newInstance(String sampleText) {
        obs_planaccion f = new obs_planaccion();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_planaccion, container, false);
        gson = new Gson();
        ImageButton btnAddPlan=(ImageButton) mView.findViewById(R.id.btn_addplan);
       /* mImageSampleRecycler = (RecyclerView) mView.findViewById(R.id.images_sample);
        setupRecycler();*/
        listPlan = (RecyclerView) mView.findViewById(R.id.list_plan);

        String codigo_obs = getArguments().getString("bString");
        Tipo= codigo_obs.substring(0,3);
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        myCalendar = Calendar.getInstance();

        if(GlobalVariables.ObjectEditable && ActObsInspEdit.editar){ // load data of server
            if(GlobalVariables.ObserbacionPlan==null)
            {
                GlobalVariables.ObserbacionPlan=codigo_obs;
                String url=GlobalVariables.Url_base+"PlanAccion/GetPlanes/"+codigo_obs;
                final ActivityController obj = new ActivityController("get", url, obs_planaccion.this,getActivity());
                obj.execute("");
            }
            else setdata();
        }
       /* else if(GlobalVariables.editar_list){////editar galeria no almacenada en el servidor

                //GlobalVariables.Planes= new ArrayList<>();
                //GlobalVariables.Planes=GlobalVariables.obsInspAddModel.Planes;
                //GlobalVariables.editar_list=false;
                setdata();

            }*/
        else// new Obserbacion
        {
            if(GlobalVariables.ObserbacionPlan==null){
                GlobalVariables.ObserbacionPlan=codigo_obs;
                GlobalVariables.Planes= new ArrayList<>();
            }
            setdata();
        }


        btnAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PlanAccionEdit.class);
               PlanModel Plan= new PlanModel();
                Gson gson = new Gson();
                Date actual = myCalendar.getTime();
                Plan.FechaSolicitud=df.format(actual);
                if(Tipo.equals("OBS")){
                    Plan.CodTabla="TOBS";
                    Plan.CodReferencia="01";
                    Plan.SolicitadoPor=GlobalVariables.Obserbacion.ObservadoPor;
                    Plan.CodSolicitadoPor=GlobalVariables.Obserbacion.CodObservadoPor;
                    Plan.CodAreaHSEC=GlobalVariables.Obserbacion.CodAreaHSEC;
                    Plan.CodNivelRiesgo=GlobalVariables.Obserbacion.CodNivelRiesgo;
                    Plan.NroDocReferencia=GlobalVariables.ObserbacionPlan.equals("OBS000000XYZ")?null:GlobalVariables.ObserbacionPlan;
                }
                else if(Tipo.equals("VER")){
                    Plan.CodTabla="TVER";
                    Plan.CodReferencia="12";
                    Plan.SolicitadoPor=GlobalVariables.Verificacion.ObservadoPor;
                    Plan.CodSolicitadoPor=GlobalVariables.Verificacion.CodVerificacionPor;
                    Plan.CodAreaHSEC=GlobalVariables.Verificacion.CodAreaHSEC;
                    Plan.CodNivelRiesgo=GlobalVariables.Verificacion.CodNivelRiesgo;
                    Plan.NroDocReferencia=GlobalVariables.ObserbacionPlan.equals("VER000000XYZ")?null:GlobalVariables.ObserbacionPlan;
                }
                else{
                    UsuarioModel UsuarioLogeado = gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
                    Plan.CodTabla="TINS";
                    Plan.CodReferencia="02";
                    Plan.SolicitadoPor=UsuarioLogeado.Nombres;
                    Plan.CodSolicitadoPor=UsuarioLogeado.CodPersona;
                    Plan.CodNivelRiesgo=GlobalVariables.obsInspDetModel.CodNivelRiesgo;
                    Plan.NroAccionOrigen=GlobalVariables.obsInspDetModel.NroDetInspeccion;
                    if(!GlobalVariables.ObserbacionPlan.contains("INSP000000XYZ")&Integer.parseInt(GlobalVariables.obsInspDetModel.Correlativo)>0){
                        Plan.NroDocReferencia=GlobalVariables.obsInspDetModel.CodInspeccion;
                    }
                }
                Plan.CodAccion="0";
                i.putExtra("Plan", gson.toJson(Plan));
                startActivityForResult(i, 1);
            }
        });

        return mView;
    }
    public void setdata(){
        LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listPlan.setLayoutManager(horizontalManager);
        listViewAdapter = new PlanEditAdapter(getActivity(),this, GlobalVariables.Planes);
        listPlan.setAdapter(listViewAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //listViewAdapter.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == 1 && resultCode == Activity.RESULT_OK) { // new plan
                PlanModel plan= gson.fromJson(data.getStringExtra("planaccion"),PlanModel.class);
                listViewAdapter.add(plan);
            }
            if(requestCode == 2 && resultCode == Activity.RESULT_OK) { // edit plan
                PlanModel plan= gson.fromJson(data.getStringExtra("planaccion"),PlanModel.class);
                listViewAdapter.replace(plan);
            }
        }
        catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void success(String data, String Tipo) {
        GlobalVariables.Planes = gson.fromJson(data, GetPlanModel.class).Data;
        GlobalVariables.StrPlanes= (ArrayList<PlanModel>)GlobalVariables.Planes.clone();

        setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}