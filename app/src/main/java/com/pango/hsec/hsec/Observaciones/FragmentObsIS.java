package com.pango.hsec.hsec.Observaciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.PersonaAdapter;
import com.pango.hsec.hsec.adapter.TablaAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.EquipoModel;
import com.pango.hsec.hsec.model.GetEquipoModel;
import com.pango.hsec.hsec.model.GetSubDetalleModel;
import com.pango.hsec.hsec.model.ObsDetalleModel;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.ArrayList;

import static com.pango.hsec.hsec.GlobalVariables.Contrata;
import static com.pango.hsec.hsec.GlobalVariables.HHA_obs;

public class FragmentObsIS extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    String jsonObsIS="";
    String jsonPersonas="";
    String  jsonSubDetalle="";

    private View mView;
    String codObs,url="",url2="", url3="";
    String codLider="";

    TextView tx_tarea_obs,tx_empresa,tx_equipo,tx_interaccion,det_comportamiento,tx_acciones,tx_otras_act,tx_otrocom,nombre_lider,tx_area;
    CardView card_otrasAct,card_comcon;
    public static FragmentObsIS newInstance(String sampleText) {
        FragmentObsIS f = new FragmentObsIS();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_obs_is, container, false);
        codObs=getArguments().getString("bString");
        GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=true;
        tx_tarea_obs= mView.findViewById(R.id.tx_tarea_obs);
        tx_empresa=mView.findViewById(R.id.tx_empresa);
        tx_equipo=mView.findViewById(R.id.tx_equipo);
        tx_interaccion=mView.findViewById(R.id.tx_interaccion);
        tx_otrocom=mView.findViewById(R.id.tx_otrocom);    // otro comportamiento para revisar
        det_comportamiento=mView.findViewById(R.id.det_comportamiento);
        tx_acciones=mView.findViewById(R.id.tx_acciones);
        tx_otras_act=mView.findViewById(R.id.tx_otras_act);

        nombre_lider=mView.findViewById(R.id.nombre_lider);
        tx_area=mView.findViewById(R.id.tx_area);
        card_otrasAct=mView.findViewById(R.id.card_otrasAct);
        card_comcon=mView.findViewById(R.id.card_comcon);

        url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codObs;

        if(jsonObsIS.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentObsIS.this,getActivity());
            obj.execute("1");
        }else {
            success(jsonObsIS,"1");
        }

        url2= GlobalVariables.Url_base+"Observaciones/GetInvolucrados/"+codObs;

        if(jsonPersonas.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url2, FragmentObsIS.this,getActivity());
            obj.execute("2");
        }else {
            success(jsonPersonas,"2");
        }
        url3= GlobalVariables.Url_base+"Observaciones/GetSubDetalle/"+codObs;

        if(jsonSubDetalle.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url3, FragmentObsIS.this,getActivity());
            obj.execute("3");
        }else {
            success(jsonSubDetalle,"3");
        }

        return mView;
    }


    @Override
    public void success(String data, String Tipo) {
        ArrayList<EquipoModel> listAtendidos=new ArrayList<>();
        EquipoModel lider=new EquipoModel();

        ArrayList<SubDetalleModel> DataOBSR=new ArrayList<>();
        ArrayList<SubDetalleModel> DataOBSC=new ArrayList<>();
        ArrayList<SubDetalleModel> DataOBCC =new ArrayList<>();
        ArrayList<SubDetalleModel> DataHHA=new ArrayList<>();
        card_comcon.setVisibility(View.GONE);
        card_otrasAct.setVisibility(View.GONE);


        if (Tipo == "1") {
            jsonObsIS = data;
            Gson gson = new Gson();
            ObsDetalleModel observacionModel = gson.fromJson(data, ObsDetalleModel.class);
            codLider=observacionModel.CodEstado;
            tx_tarea_obs.setText(observacionModel.Observacion);
            tx_empresa.setText(GlobalVariables.getDescripcion(Contrata, observacionModel.CodError));
            tx_equipo.setText(observacionModel.CodHHA);
            tx_interaccion.setText(observacionModel.CodSubEstandar);
            det_comportamiento.setText(observacionModel.CodActiRel);
            tx_acciones.setText(observacionModel.Accion);
            tx_otras_act.setText(observacionModel.CodObservacion);
            tx_otrocom.setText(observacionModel.CodTipo);

            //tx_otras_act.setVisibility(View.GONE);


        }else if (Tipo == "2"){
            jsonPersonas=data;
            Gson gson = new Gson();
            GetEquipoModel getEquipoModel = gson.fromJson(data, GetEquipoModel.class);
            int count=getEquipoModel.Count;

            for(int i=0;i<getEquipoModel.Count;i++){
                if(getEquipoModel.Data.get(i).CodPersona.equals(codLider)){
                    lider=getEquipoModel.Data.get(i);
                }else{
                    listAtendidos.add(getEquipoModel.Data.get(i));
                }

            }
            nombre_lider.setText(lider.Nombres);
            tx_area.setText(lider.Cargo);

            final RecyclerView recList = (RecyclerView) mView.findViewById(R.id.rec_atendidos);
            recList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recList.setLayoutManager(llm);
            PersonaAdapter ca = new PersonaAdapter(listAtendidos);
            recList.setAdapter(ca);


        }else if(Tipo == "3"){
            jsonSubDetalle=data;
            Gson gson = new Gson();
            GetSubDetalleModel getSubDetalleModel = gson.fromJson(data, GetSubDetalleModel.class);
            int count=getSubDetalleModel.Count;
            //otros 19

            for(int i = 0; i < getSubDetalleModel.Data.size(); i++){
                if(getSubDetalleModel.Data.get(i).CodTipo.equals("OBSR")){
                    DataOBSR.add(getSubDetalleModel.Data.get(i));
                }else if(getSubDetalleModel.Data.get(i).CodTipo.equals("OBSC")){
                    DataOBSC.add(getSubDetalleModel.Data.get(i));
                }else if(getSubDetalleModel.Data.get(i).CodTipo.equals("OBCC")){
                    DataOBCC.add(getSubDetalleModel.Data.get(i));

                    if(!getSubDetalleModel.Data.get(i).Descripcion.equals("COMCON11")){
                        card_comcon.setVisibility(View.GONE);
                    }else{
                        card_comcon.setVisibility(View.VISIBLE);
                    }

                }else if(getSubDetalleModel.Data.get(i).CodTipo.equals("HHA")){
                    DataHHA.add(getSubDetalleModel.Data.get(i));
                    if(!getSubDetalleModel.Data.get(i).Descripcion.equals("19")){
                        card_otrasAct.setVisibility(View.GONE);
                    }else{
                        card_otrasAct.setVisibility(View.VISIBLE);
                    }


                }
            }




            final RecyclerView rec_metodologia = (RecyclerView) mView.findViewById(R.id.rec_metodologia);
            rec_metodologia.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rec_metodologia.setLayoutManager(llm);

            TablaAdapter ca = new TablaAdapter(DataOBSR);
            rec_metodologia.setAdapter(ca);

            //rec_metodologia

            final RecyclerView rec_alto_riesgo = (RecyclerView) mView.findViewById(R.id.rec_alto_riesgo);
            rec_alto_riesgo.setHasFixedSize(true);
            LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
            llm2.setOrientation(LinearLayoutManager.VERTICAL);
            rec_alto_riesgo.setLayoutManager(llm2);

            TablaAdapter ca2 = new TablaAdapter(DataHHA);
            rec_alto_riesgo.setAdapter(ca2);

            //rec_clasificacion
            final RecyclerView rec_clasificacion = (RecyclerView) mView.findViewById(R.id.rec_clasificacion);
            rec_clasificacion.setHasFixedSize(true);
            LinearLayoutManager llm3 = new LinearLayoutManager(getActivity());
            llm3.setOrientation(LinearLayoutManager.VERTICAL);
            rec_clasificacion.setLayoutManager(llm3);

            TablaAdapter ca3 = new TablaAdapter(DataOBSC);
            rec_clasificacion.setAdapter(ca3);

            //rec_comportamiento
            final RecyclerView rec_comportamiento = (RecyclerView) mView.findViewById(R.id.rec_comportamiento);
            rec_comportamiento.setHasFixedSize(true);
            LinearLayoutManager llm4 = new LinearLayoutManager(getActivity());
            llm4.setOrientation(LinearLayoutManager.VERTICAL);
            rec_comportamiento.setLayoutManager(llm4);

            TablaAdapter ca4 = new TablaAdapter(DataOBCC);
            rec_comportamiento.setAdapter(ca4);

        }





    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
