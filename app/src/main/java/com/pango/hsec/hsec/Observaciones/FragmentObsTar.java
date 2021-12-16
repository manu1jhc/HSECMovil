package com.pango.hsec.hsec.Observaciones;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.AspectosAdapter;
import com.pango.hsec.hsec.adapter.EtapasAdapter;
import com.pango.hsec.hsec.adapter.ParticipanteAdapter;
import com.pango.hsec.hsec.adapter.PersonaAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetEquipoModel;
import com.pango.hsec.hsec.model.GetSubDetalleModel;
import com.pango.hsec.hsec.model.ObsDetalleModel;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.ArrayList;

import static com.pango.hsec.hsec.GlobalVariables.Actividad_obs;
import static com.pango.hsec.hsec.GlobalVariables.Error_obs;
import static com.pango.hsec.hsec.GlobalVariables.Estado_obs;
import static com.pango.hsec.hsec.GlobalVariables.HHA_obs;
import static com.pango.hsec.hsec.GlobalVariables.StopWork_obs;


public class FragmentObsTar extends Fragment implements IActivity {
    String jsonObsTar="";
    String jsonPersnas="";
    String jsonSubDetalle="";

    private View mView;
    String codObs;
    String url,url2,url3;
    ParticipanteAdapter participanteAdapter;
    String[] comentarios={"Se Cumple el PET","El trabajador requiere feedback","El procedimiento debe modificarse", "Reconocimientos/Oportunidades"};
    LinearLayout ll_comentario1,ll_comentario2,ll_comentario3,ll_comentario4;
    TextView tx_tarea_obs,tx_actividad,tx_cod_pet,tx_hha,tx_estado,tx_error,tx_StopWork,tipo_com1,tipo_com2,tipo_com3,tipo_com4,descripcion1,descripcion2,descripcion3,descripcion4;
    // TODO: Rename and change types and number of parameters
    public static FragmentObsTar newInstance(String sampleText) {
        FragmentObsTar f = new FragmentObsTar();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_obs_tar, container, false);
        tx_tarea_obs=mView.findViewById(R.id.tx_tarea_obs);
        tx_actividad=mView.findViewById(R.id.tx_actividad);
        tx_cod_pet=mView.findViewById(R.id.tx_cod_pet);
        tx_hha=mView.findViewById(R.id.tx_hha);
        tx_estado=mView.findViewById(R.id.tx_estado);
        tx_error=mView.findViewById(R.id.tx_error);
        tx_StopWork=(TextView) mView.findViewById(R.id.tx_StopWork);

        tipo_com1=mView.findViewById(R.id.tipo_com1);
        tipo_com2=mView.findViewById(R.id.tipo_com2);
        tipo_com3=mView.findViewById(R.id.tipo_com3);
        tipo_com4=mView.findViewById(R.id.tipo_com4);
        descripcion1=mView.findViewById(R.id.descripcion1);
        descripcion2=mView.findViewById(R.id.descripcion2);
        descripcion3=mView.findViewById(R.id.descripcion3);
        descripcion4=mView.findViewById(R.id.descripcion4);

        ll_comentario1=mView.findViewById(R.id.ll_comentario1);
        ll_comentario2=mView.findViewById(R.id.ll_comentario2);
        ll_comentario3=mView.findViewById(R.id.ll_comentario3);
        ll_comentario4=mView.findViewById(R.id.ll_comentario4);

        GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=true;

        codObs=getArguments().getString("bString");
        url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codObs;

        if(jsonObsTar.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentObsTar.this,getActivity());
            obj.execute("1");
        }else {
            success(jsonObsTar,"1");
        }

        url2=GlobalVariables.Url_base+"Observaciones/GetInvolucrados/"+codObs;

        if(jsonPersnas.isEmpty()) {
            final ActivityController obj2 = new ActivityController("get", url2, FragmentObsTar.this,getActivity());
            obj2.execute("2");
        }else{
            success(jsonPersnas,"2");
        }

        url3=GlobalVariables.Url_base+"Observaciones/GetsSubDetalle/"+codObs;

        if(jsonSubDetalle.isEmpty()) {
            final ActivityController obj2 = new ActivityController("get", url3, FragmentObsTar.this,getActivity());
            obj2.execute("3");
        }else{
            success(jsonSubDetalle,"3");
        }





        return mView;
    }


    @Override
    public void success(String data, String Tipo) {
        ArrayList<SubDetalleModel> DataPrea=new ArrayList<>();
        ArrayList<SubDetalleModel> DataPeto=new ArrayList<>();

        if (Tipo == "1") {
            jsonObsTar = data;
            Gson gson = new Gson();
            ObsDetalleModel observacionModel = gson.fromJson(data, ObsDetalleModel.class);
            if(observacionModel != null)
            {
                String[] parts = observacionModel.CodSubEstandar.split(";",-1);
                if(observacionModel.Observacion!=null)tx_tarea_obs.setText(observacionModel.Observacion);
                if(observacionModel.CodActiRel!=null)tx_actividad.setText(GlobalVariables.getDescripcion(Actividad_obs, observacionModel.CodActiRel));
                if(observacionModel.Accion!=null)tx_cod_pet.setText(observacionModel.Accion);
                if(observacionModel.CodHHA!=null)tx_hha.setText(GlobalVariables.getDescripcion(HHA_obs, observacionModel.CodHHA));
                if(observacionModel.CodEstado!=null)tx_estado.setText(GlobalVariables.getDescripcion(Estado_obs, observacionModel.CodEstado));
                if(observacionModel.CodError!=null)tx_error.setText(GlobalVariables.getDescripcion(Error_obs, observacionModel.CodError));
                if(observacionModel.StopWork!=null)tx_StopWork.setText(GlobalVariables.getDescripcion(StopWork_obs,observacionModel.StopWork));
                //observacionModel.CodSubEstandar;
                if (parts.length == 0) {
                    ll_comentario1.setVisibility(View.GONE);
                    ll_comentario2.setVisibility(View.GONE);
                    ll_comentario3.setVisibility(View.GONE);
                    ll_comentario4.setVisibility(View.GONE);

                } else {
                    tipo_com1.setText(comentarios[0]);
                    tipo_com2.setText(comentarios[1]);
                    tipo_com3.setText(comentarios[2]);
                    tipo_com4.setText(comentarios[3]);
                    descripcion1.setText(parts[0]);
                    descripcion2.setText(parts[1]);
                    descripcion3.setText(parts[2]);
                    descripcion4.setText(parts[3]);

                    for (int i = 0; i < 4; i++) {
                        if (parts[i].isEmpty()) {
                            switch (i) {
                                case 0:
                                    ll_comentario1.setVisibility(View.GONE);
                                    break;
                                case 1:
                                    ll_comentario2.setVisibility(View.GONE);
                                    break;
                                case 2:
                                    ll_comentario3.setVisibility(View.GONE);
                                    break;
                                case 3:
                                    ll_comentario4.setVisibility(View.GONE);
                                    break;
                            }

                        }
                    }
                }
            }
        }else if (Tipo == "2"){

            jsonPersnas=data;
            Gson gson = new Gson();
            GetEquipoModel getEquipoModel = gson.fromJson(data, GetEquipoModel.class);
            int count=getEquipoModel.Count;

            final RecyclerView recList = (RecyclerView) mView.findViewById(R.id.recycler_persona);
            recList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recList.setLayoutManager(llm);
            PersonaAdapter ca = new PersonaAdapter(getEquipoModel.Data);
            recList.setAdapter(ca);
            /*ListView list_personas;
            participanteAdapter = new ParticipanteAdapter(getContext(),getEquipoModel.Data,count,Tipo);
            list_personas= (ListView) mView.findViewById(R.id.list_equipo);
            list_personas.setAdapter(participanteAdapter); */
        }else if (Tipo == "3"){
            jsonSubDetalle=data;
            Gson gson = new Gson();
            GetSubDetalleModel getSubDetalleModel = gson.fromJson(data, GetSubDetalleModel.class);
            int count=getSubDetalleModel.Count;

            for(int i = 0; i < getSubDetalleModel.Data.size(); i++){
                if(getSubDetalleModel.Data.get(i).CodTipo.equals("PREA")){
                    DataPrea.add(new SubDetalleModel(getSubDetalleModel.Data.get(i).CodTipo,getSubDetalleModel.Data.get(i).CodSubtipo,getSubDetalleModel.Data.get(i).Descripcion));
                }else if(getSubDetalleModel.Data.get(i).CodTipo.equals("PETO")){
                    DataPeto.add(new SubDetalleModel(getSubDetalleModel.Data.get(i).CodTipo,getSubDetalleModel.Data.get(i).CodSubtipo,getSubDetalleModel.Data.get(i).Descripcion));
                }
            }


            final RecyclerView recAspectos = (RecyclerView) mView.findViewById(R.id.recycler_aspectos);
            recAspectos.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recAspectos.setLayoutManager(llm);

            AspectosAdapter ca = new AspectosAdapter(DataPrea);
            recAspectos.setAdapter(ca);


            final RecyclerView recEtapas = (RecyclerView) mView.findViewById(R.id.recycler_etapas);
            recEtapas.setHasFixedSize(true);

            LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recEtapas.setLayoutManager(llm2);

            EtapasAdapter ea = new EtapasAdapter(DataPeto);
            recEtapas.setAdapter(ea);

        }
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
