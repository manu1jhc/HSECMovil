package com.pango.hsec.hsec.Observaciones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.ObsDetalleModel;

import static com.pango.hsec.hsec.GlobalVariables.Actividad_obs;
import static com.pango.hsec.hsec.GlobalVariables.Acto_obs;
import static com.pango.hsec.hsec.GlobalVariables.Condicion_obs;
import static com.pango.hsec.hsec.GlobalVariables.Covid;
import static com.pango.hsec.hsec.GlobalVariables.StopWork_obs;
import static com.pango.hsec.hsec.GlobalVariables.Correccion_obs;
import static com.pango.hsec.hsec.GlobalVariables.HHA_obs;

public class FragmentObsCond extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    String jsonObsCond="";
    private View mView;
    String codObs;
    String url;
    TextView tx_obs_det,tx_accion,tx_act_rel,tx_hha,tx_sub_estandar,tx_StopWork,tx_Correccion, tx_sub_tipo;
    CardView cv_51;
    public static FragmentObsCond newInstance(String sampleText) {
        FragmentObsCond f = new FragmentObsCond();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_cond, container, false);
        // Inflate the layout for this fragment
        tx_obs_det=(TextView) mView.findViewById(R.id.tx_obs_det);
        tx_accion=(TextView) mView.findViewById(R.id.tx_accion);
        tx_act_rel=(TextView) mView.findViewById(R.id.tx_act_rel);
        tx_hha=(TextView) mView.findViewById(R.id.tx_hha);
        tx_sub_estandar=(TextView) mView.findViewById(R.id.tx_sub_estandar);
        tx_StopWork=(TextView) mView.findViewById(R.id.tx_StopWork);
        tx_Correccion=(TextView) mView.findViewById(R.id.tx_Correccion);

        cv_51 = (CardView) mView.findViewById(R.id.cv_51);
        tx_sub_tipo=(TextView) mView.findViewById(R.id.tx_sub_tipo);

        GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=true;

        codObs=getArguments().getString("bString");
        url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codObs;

        if(jsonObsCond.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentObsCond.this,getActivity());
            obj.execute("");
        }else {
            success(jsonObsCond,"");
        }

        return mView;
    }


    @Override
    public void success(String data, String Tipo) {

        jsonObsCond =data;
        Gson gson = new Gson();
        ObsDetalleModel observacionModel = gson.fromJson(data, ObsDetalleModel.class);
        if(observacionModel!=null)
        {
            if(observacionModel.Observacion!=null) tx_obs_det.setText(observacionModel.Observacion);
            if(observacionModel.Accion!=null) tx_accion.setText(observacionModel.Accion);
            if(observacionModel.CodActiRel!=null) tx_act_rel.setText(GlobalVariables.getDescripcion(Actividad_obs,observacionModel.CodActiRel));
            if(observacionModel.CodHHA!=null) tx_hha.setText(GlobalVariables.getDescripcion(HHA_obs,observacionModel.CodHHA));
            if(observacionModel.StopWork!=null)tx_StopWork.setText(GlobalVariables.getDescripcion(StopWork_obs,observacionModel.StopWork));
            if(observacionModel.CodCorreccion!=null)tx_Correccion.setText(GlobalVariables.getDescripcion(Correccion_obs,observacionModel.CodCorreccion));

            if(observacionModel.CodSubEstandar!=null) tx_sub_estandar.setText(GlobalVariables.getDescripcion(Condicion_obs,observacionModel.CodSubEstandar));

            String descripcion = GlobalVariables.getDescripcion(Condicion_obs,observacionModel.CodSubEstandar);
            if(descripcion.contains("COVID-19")){

                cv_51.setVisibility(View.VISIBLE);
                tx_sub_tipo.setText(GlobalVariables.getDescripcion(Covid, observacionModel.ComOpt1));

            }else {
                cv_51.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
