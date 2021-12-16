package com.pango.hsec.hsec.Observaciones;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ControlCriticoAdapter;
import com.pango.hsec.hsec.adapter.TablaAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.EquipoModel;
import com.pango.hsec.hsec.model.GetControlCriticoModel;
import com.pango.hsec.hsec.model.GetSubDetalleModel;
import com.pango.hsec.hsec.model.ObsDetalleModel;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.ArrayList;

import static com.pango.hsec.hsec.GlobalVariables.Contrata;
import static com.pango.hsec.hsec.GlobalVariables.StopWork_obs;

public class FragmentObsIS extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    String jsonObsIS="";
    String jsonPersonas="";
    String  jsonSubDetalle="";
    String  jsonControlCritico="";


    private View mView;
    String codObs,url="",url2="", url3="", url4= "";
    String codLider="";

    TextView tx_tarea_obs,tx_empresa,tx_equipo,tx_interaccion,det_comportamiento,tx_acciones,nombre_lider,tx_area,tx_StopWork, tx_gerencia, tx_subproceso, tx_peli_fatal;
    CardView cv_1, cv_2, cv_3, cv_4, cv_5, cv_6, cv_7, cv_8, cv_is1, cv_is2, cv_is3, cv_is4, cv_is5, cv_is6, cv_is7, cv_cc_covid1, cv_cc_covid2, cv_cc_covid3;
    RecyclerView recList, rec_metodologia, rec_alto_riesgo, rec_clasificacion, rec_comportamiento, rec_preinteraccion, rec_cierre, rec_cartillas;
    //LinearLayout cv_is3, ll_cc_covid, ll_is_det;

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
        cv_1 = (CardView) mView.findViewById(R.id.cv_1);
        cv_2 = (CardView) mView.findViewById(R.id.cv_2);
        cv_3 = (CardView) mView.findViewById(R.id.cv_3);
        cv_4 = (CardView) mView.findViewById(R.id.cv_4);
        cv_5 = (CardView) mView.findViewById(R.id.cv_5);
        cv_6 = (CardView) mView.findViewById(R.id.cv_6);
        cv_7 = (CardView) mView.findViewById(R.id.cv_7);
        cv_8 = (CardView) mView.findViewById(R.id.cv_8);

        cv_is1 = (CardView) mView.findViewById(R.id.cv_is1);
        cv_is2 = (CardView) mView.findViewById(R.id.cv_is2);
        cv_is3 = (CardView) mView.findViewById(R.id.cv_is3);
        cv_is4 = (CardView) mView.findViewById(R.id.cv_is4);
        cv_is5 = (CardView) mView.findViewById(R.id.cv_is5);
        cv_is6 = (CardView) mView.findViewById(R.id.cv_is6);
        cv_is7 = (CardView) mView.findViewById(R.id.cv_is7);



        recList =  mView.findViewById(R.id.rec_atendidos);
        rec_metodologia = (RecyclerView) mView.findViewById(R.id.rec_metodologia);
        rec_alto_riesgo = (RecyclerView) mView.findViewById(R.id.rec_alto_riesgo);
        rec_clasificacion = (RecyclerView) mView.findViewById(R.id.rec_clasificacion);
        rec_comportamiento = (RecyclerView) mView.findViewById(R.id.rec_comportamiento);


        tx_tarea_obs= mView.findViewById(R.id.tx_tarea_obs);
        tx_empresa=mView.findViewById(R.id.tx_empresa);
        tx_equipo=mView.findViewById(R.id.tx_equipo);
        tx_interaccion=mView.findViewById(R.id.tx_interaccion);
        det_comportamiento=mView.findViewById(R.id.det_comportamiento);
        tx_acciones=mView.findViewById(R.id.tx_acciones);
        tx_StopWork=(TextView) mView.findViewById(R.id.tx_StopWork);
        nombre_lider=mView.findViewById(R.id.nombre_lider);
        tx_area=mView.findViewById(R.id.tx_area);

        tx_gerencia = mView.findViewById(R.id.tx_gerencia);
        tx_subproceso = mView.findViewById(R.id.tx_subproceso);
        tx_peli_fatal = mView.findViewById(R.id.tx_peli_fatal);

        // controles criticos y covid

        cv_cc_covid1 = mView.findViewById(R.id.cv_cc_covid1);
        cv_cc_covid2 = mView.findViewById(R.id.cv_cc_covid2);
        cv_cc_covid3 = mView.findViewById(R.id.cv_cc_covid3);

        rec_preinteraccion = mView.findViewById(R.id.rec_preinteraccion);
        rec_cierre = mView.findViewById(R.id.rec_cierre);
        rec_cartillas = mView.findViewById(R.id.rec_cartillas);

        url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codObs;
        if(jsonObsIS.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentObsIS.this,getActivity());
            obj.execute("1");
        }else {
            success(jsonObsIS,"1");
        }

        url3= GlobalVariables.Url_base+"Observaciones/GetsSubDetalle/"+codObs;
        if(jsonSubDetalle.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url3, FragmentObsIS.this,getActivity());
            obj.execute("3");
        }else {
            success(jsonSubDetalle,"3");
        }






/*
        url2= GlobalVariables.Url_base+"Observaciones/GetInvolucrados/"+codObs;
        if(jsonPersonas.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url2, FragmentObsIS.this,getActivity());
            obj.execute("2");
        }else {
            success(jsonPersonas,"2");
        }
        url3= GlobalVariables.Url_base+"Observaciones/GetsSubDetalle/"+codObs;
        if(jsonSubDetalle.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url3, FragmentObsIS.this,getActivity());
            obj.execute("3");
        }else {
            success(jsonSubDetalle,"3");
        }
*/



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

        ArrayList<SubDetalleModel> DataOBSP = new ArrayList<>();
        ArrayList<SubDetalleModel> DataOBVE = new ArrayList<>();
        ObsDetalleModel observacionModel;


        if (Tipo == "1") {
            jsonObsIS = data;
            Gson gson = new Gson();
            observacionModel = gson.fromJson(data, ObsDetalleModel.class);
            if(observacionModel!=null)
            {
                if (observacionModel.CodHHA.equals("2")){

                    cv_1.setVisibility(View.VISIBLE);
                    cv_2.setVisibility(View.VISIBLE);
                    cv_3.setVisibility(View.GONE);
                    cv_4.setVisibility(View.GONE);
                    cv_5.setVisibility(View.GONE);
                    cv_6.setVisibility(View.VISIBLE);
                    cv_7.setVisibility(View.VISIBLE);
                    cv_8.setVisibility(View.VISIBLE);

                    cv_is1.setVisibility(View.GONE);
                    cv_is2.setVisibility(View.GONE);
                    cv_is3.setVisibility(View.GONE);
                    cv_is4.setVisibility(View.GONE);
                    cv_is5.setVisibility(View.GONE);
                    cv_is6.setVisibility(View.GONE);
                    cv_is7.setVisibility(View.GONE);
                    recList.setVisibility(View.GONE);
                    rec_metodologia.setVisibility(View.GONE);
                    rec_alto_riesgo.setVisibility(View.GONE);
                    rec_clasificacion.setVisibility(View.GONE);
                    rec_comportamiento.setVisibility(View.GONE);

                    cv_cc_covid1.setVisibility(View.VISIBLE);
                    cv_cc_covid2.setVisibility(View.VISIBLE);
                    cv_cc_covid3.setVisibility(View.VISIBLE);
                    rec_preinteraccion.setVisibility(View.VISIBLE);
                    rec_cierre.setVisibility(View.VISIBLE);
                    rec_cartillas.setVisibility(View.VISIBLE);



                    if (observacionModel.Observacion != null)
                        tx_tarea_obs.setText(observacionModel.Observacion);
                    if (observacionModel.CodActiRel != null)
                        tx_empresa.setText(GlobalVariables.getDescripcion(Contrata, observacionModel.CodError));

                    if (observacionModel.Accion != null)
                        tx_gerencia.setText(GlobalVariables.getDescripcion(GlobalVariables.Gerencia,observacionModel.Accion).trim().replace("=","")); //
                    if (observacionModel.CodEstado != null)
                        tx_subproceso.setText(observacionModel.CodEstado);
                    if (observacionModel.CodError != null)
                        tx_peli_fatal.setText(observacionModel.CodActiRel);


                    url4= GlobalVariables.Url_base+"Observaciones/GetRespControlCritico?id="+codObs+"&Cartilla";

                    if(jsonControlCritico.isEmpty()) {
                        GlobalVariables.istabs=true;
                        final ActivityController obj = new ActivityController("get", url4, FragmentObsIS.this,getActivity());
                        obj.execute("4");
                    }else {
                        success(jsonControlCritico,"4");
                    }



                } else if (observacionModel.CodHHA.equals("3")){

                    cv_1.setVisibility(View.GONE);
                    cv_2.setVisibility(View.VISIBLE);
                    cv_3.setVisibility(View.GONE);
                    cv_4.setVisibility(View.GONE);
                    cv_5.setVisibility(View.GONE);
                    cv_6.setVisibility(View.VISIBLE);
                    cv_7.setVisibility(View.VISIBLE);
                    cv_8.setVisibility(View.VISIBLE);

                    cv_is1.setVisibility(View.GONE);
                    cv_is2.setVisibility(View.GONE);
                    cv_is3.setVisibility(View.GONE);
                    cv_is4.setVisibility(View.GONE);
                    cv_is5.setVisibility(View.GONE);
                    cv_is6.setVisibility(View.GONE);
                    cv_is7.setVisibility(View.GONE);
                    recList.setVisibility(View.GONE);
                    rec_metodologia.setVisibility(View.GONE);
                    rec_alto_riesgo.setVisibility(View.GONE);
                    rec_clasificacion.setVisibility(View.GONE);
                    rec_comportamiento.setVisibility(View.GONE);

                    cv_cc_covid1.setVisibility(View.VISIBLE);
                    cv_cc_covid2.setVisibility(View.VISIBLE);
                    cv_cc_covid3.setVisibility(View.VISIBLE);
                    rec_preinteraccion.setVisibility(View.VISIBLE);
                    rec_cierre.setVisibility(View.VISIBLE);
                    rec_cartillas.setVisibility(View.VISIBLE);

                    if (observacionModel.CodActiRel != null)
                        tx_empresa.setText(GlobalVariables.getDescripcion(Contrata, observacionModel.CodError));
                    if (observacionModel.Accion != null)
                        tx_gerencia.setText(GlobalVariables.getDescripcion(GlobalVariables.Gerencia,observacionModel.Accion).trim().replace("=","")); //
                    if (observacionModel.CodEstado != null)
                        tx_subproceso.setText(observacionModel.CodEstado);
                    if (observacionModel.CodError != null)
                        tx_peli_fatal.setText(GlobalVariables.getDescripcion(GlobalVariables.Peligro_fatal  , observacionModel.CodActiRel));

                    url4= GlobalVariables.Url_base+"Observaciones/GetRespControlCritico?id="+codObs+"&Cartilla";
                    if(jsonControlCritico.isEmpty()) {
                        GlobalVariables.istabs=true;
                        final ActivityController obj = new ActivityController("get", url4, FragmentObsIS.this,getActivity());
                        obj.execute("4");
                    }else {
                        success(jsonControlCritico,"4");
                    }

                }else {
                    cv_1.setVisibility(View.VISIBLE);
                    cv_2.setVisibility(View.VISIBLE);
                    cv_3.setVisibility(View.VISIBLE);
                    cv_4.setVisibility(View.VISIBLE);
                    cv_5.setVisibility(View.VISIBLE);
                    cv_6.setVisibility(View.GONE);
                    cv_7.setVisibility(View.GONE);
                    cv_8.setVisibility(View.GONE);

                    cv_cc_covid1.setVisibility(View.GONE);
                    cv_cc_covid2.setVisibility(View.GONE);
                    cv_cc_covid3.setVisibility(View.GONE);
                    rec_preinteraccion.setVisibility(View.GONE);
                    rec_cierre.setVisibility(View.GONE);
                    rec_cartillas.setVisibility(View.GONE);

                    if (observacionModel.Observacion != null)
                        tx_tarea_obs.setText(observacionModel.Observacion);
                    if (observacionModel.CodError != null)
                        tx_empresa.setText(GlobalVariables.getDescripcion(Contrata, observacionModel.CodError));
                    if (observacionModel.CodHHA != null) tx_equipo.setText(observacionModel.CodHHA);
                    if (observacionModel.CodSubEstandar != null)
                        tx_interaccion.setText(observacionModel.CodSubEstandar);
                    if (observacionModel.CodActiRel != null)
                        det_comportamiento.setText(observacionModel.CodActiRel);
                    if (observacionModel.Accion != null)
                        tx_acciones.setText(observacionModel.Accion);
                    if (observacionModel.StopWork != null)
                        tx_StopWork.setText(GlobalVariables.getDescripcion(StopWork_obs, observacionModel.StopWork));

                    url2= GlobalVariables.Url_base+"Observaciones/GetInvolucrados/"+codObs;
                    if(jsonPersonas.isEmpty()) {
                        GlobalVariables.istabs=true;
                        final ActivityController obj = new ActivityController("get", url2, FragmentObsIS.this,getActivity());
                        obj.execute("2");
                    }else {
                        success(jsonPersonas,"2");
                    }
                }
            }
        }
        else if (Tipo == "2"){
            jsonPersonas=data;
            Gson gson = new Gson();
            GetControlCriticoModel getControlCriticoModel = gson.fromJson(data, GetControlCriticoModel.class);
           /* int count=getEquipoModel.Count;

            for(int i=0;i<getEquipoModel.Count;i++){
                if(getEquipoModel.Data.get(i).CodPersona.equals(codLider)){
                    lider=getEquipoModel.Data.get(i);
                }else{
                    listAtendidos.add(getEquipoModel.Data.get(i));
                }

            }
            nombre_lider.setText(lider.Nombres);
            tx_area.setText(lider.Cargo);*/

            rec_cartillas.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rec_cartillas.setLayoutManager(llm);
            ControlCriticoAdapter ca = new ControlCriticoAdapter(getControlCriticoModel.Data);
            rec_cartillas.setAdapter(ca);


        }
        else if(Tipo == "3"){
            jsonSubDetalle=data;
            Gson gson = new Gson();
            GetSubDetalleModel getSubDetalleModel = gson.fromJson(data, GetSubDetalleModel.class);


            for (SubDetalleModel item:getSubDetalleModel.Data) {
                switch (item.CodTipo){
                    case "OBSR": DataOBSR.add(item); break;
                    case "OBSC": DataOBSC.add(item); break;
                    case "OBCC": DataOBCC.add(item); break;
                    case "HHA": DataHHA.add(item); break;

                    case "OBSP": DataOBSP.add(item); break;
                    case "OBVE": DataOBVE.add(item); break;
                }
            }


            if (DataOBSP.size() != 0 || DataOBVE.size() != 0){

                rec_preinteraccion.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rec_preinteraccion.setLayoutManager(llm);
                TablaAdapter ca = new TablaAdapter(DataOBSP);
                rec_preinteraccion.setAdapter(ca);

                //rec_cierre
                rec_cierre.setHasFixedSize(true);
                LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rec_cierre.setLayoutManager(llm2);
                TablaAdapter ca2= new TablaAdapter(DataOBVE);
                rec_cierre.setAdapter(ca2);


            } else {

                rec_metodologia.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rec_metodologia.setLayoutManager(llm);
                TablaAdapter ca = new TablaAdapter(DataOBSR);
                rec_metodologia.setAdapter(ca);

                //rec_metodologia

                rec_alto_riesgo.setHasFixedSize(true);
                LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
                llm2.setOrientation(LinearLayoutManager.VERTICAL);
                rec_alto_riesgo.setLayoutManager(llm2);
                TablaAdapter ca2 = new TablaAdapter(DataHHA);
                rec_alto_riesgo.setAdapter(ca2);

                //rec_clasificacion
                rec_clasificacion.setHasFixedSize(true);
                LinearLayoutManager llm3 = new LinearLayoutManager(getActivity());
                llm3.setOrientation(LinearLayoutManager.VERTICAL);
                rec_clasificacion.setLayoutManager(llm3);

                TablaAdapter ca3 = new TablaAdapter(DataOBSC);
                rec_clasificacion.setAdapter(ca3);

                //rec_comportamiento
                rec_comportamiento.setHasFixedSize(true);
                LinearLayoutManager llm4 = new LinearLayoutManager(getActivity());
                llm4.setOrientation(LinearLayoutManager.VERTICAL);
                rec_comportamiento.setLayoutManager(llm4);

                TablaAdapter ca4 = new TablaAdapter(DataOBCC);
                rec_comportamiento.setAdapter(ca4);
            }
        }
        else if(Tipo == "4") {

            jsonControlCritico=data;
            Gson gson = new Gson();
            GetControlCriticoModel getControlCriticoModel = gson.fromJson(data, GetControlCriticoModel.class);

            rec_cartillas.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rec_cartillas.setLayoutManager(llm);
            ControlCriticoAdapter ca = new ControlCriticoAdapter(getControlCriticoModel.Data);
            rec_cartillas.setAdapter(ca);
        }
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
