package com.pango.hsec.hsec.Observaciones;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
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
import static com.pango.hsec.hsec.GlobalVariables.Correccion_obs;
import static com.pango.hsec.hsec.GlobalVariables.Covid;
import static com.pango.hsec.hsec.GlobalVariables.EPP;
import static com.pango.hsec.hsec.GlobalVariables.Error_obs;
import static com.pango.hsec.hsec.GlobalVariables.Estado_obs;
import static com.pango.hsec.hsec.GlobalVariables.HHA_obs;
import static com.pango.hsec.hsec.GlobalVariables.StopWork_obs;

public class FragmentObsCom extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match


    String jsonObsCom="";
    private View mView;
    String codObs;
    String url;
    TextView tx_obs_det,tx_accion,tx_act_rel,tx_hha,tx_sub_estandar,tx_estado,tx_error,tx_StopWork,tx_Correccion, tx_sub_tipo,tx_tipo_prot,tx_epp;
    CardView cv_51, cv_52, cv_53;
    // TODO: Rename and change types and number of parameters
    public static FragmentObsCom newInstance(String sampleText) {
        FragmentObsCom f = new FragmentObsCom();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);
        return f;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_obs_com, container, false);
        tx_obs_det=(TextView) mView.findViewById(R.id.tx_obs_det);
        tx_accion=(TextView) mView.findViewById(R.id.tx_accion);
        tx_act_rel=(TextView) mView.findViewById(R.id.tx_act_rel);
        tx_hha=(TextView) mView.findViewById(R.id.tx_hha);
        tx_sub_estandar=(TextView) mView.findViewById(R.id.tx_sub_estandar);
        tx_estado=(TextView) mView.findViewById(R.id.tx_estado);
        tx_error=(TextView) mView.findViewById(R.id.tx_error);
        tx_StopWork=(TextView) mView.findViewById(R.id.tx_StopWork);
        tx_Correccion=(TextView) mView.findViewById(R.id.tx_Correccion);

        tx_sub_tipo=(TextView) mView.findViewById(R.id.tx_sub_tipo);
        tx_tipo_prot=(TextView) mView.findViewById(R.id.tx_tipo_prot);
        tx_epp=(TextView) mView.findViewById(R.id.tx_epp);

        cv_51=(CardView) mView.findViewById(R.id.cv_51);
        cv_52=(CardView) mView.findViewById(R.id.cv_52);
        cv_53=(CardView) mView.findViewById(R.id.cv_53);


        GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=true;

        codObs=getArguments().getString("bString");
        url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codObs;

        if(jsonObsCom.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentObsCom.this,getActivity());
            obj.execute("");
        }else {
            success(jsonObsCom,"");
        }


        return mView;
    }


    @Override
    public void success(String data, String Tipo) {

        jsonObsCom =data;
        Gson gson = new Gson();
        ObsDetalleModel observacionModel = gson.fromJson(data, ObsDetalleModel.class);
        if(observacionModel!=null)
        {
            if(observacionModel.Observacion!=null)tx_obs_det.setText(observacionModel.Observacion);
            if(observacionModel.Accion!=null)tx_accion.setText(observacionModel.Accion);
            if(observacionModel.CodActiRel!=null)tx_act_rel.setText(GlobalVariables.getDescripcion(Actividad_obs,observacionModel.CodActiRel));
            if(observacionModel.CodHHA!=null)tx_hha.setText(GlobalVariables.getDescripcion(HHA_obs,observacionModel.CodHHA));
            if(observacionModel.CodEstado!=null)tx_estado.setText(GlobalVariables.getDescripcion(Estado_obs,observacionModel.CodEstado));
            if(observacionModel.CodError!=null)tx_error.setText(GlobalVariables.getDescripcion(Error_obs,observacionModel.CodError));
            if(observacionModel.StopWork!=null)tx_StopWork.setText(GlobalVariables.getDescripcion(StopWork_obs,observacionModel.StopWork));
            if(observacionModel.CodCorreccion!=null)tx_Correccion.setText(GlobalVariables.getDescripcion(Correccion_obs,observacionModel.CodCorreccion));
            if(observacionModel.CodSubEstandar!=null)tx_sub_estandar.setText(GlobalVariables.getDescripcion(Acto_obs,observacionModel.CodSubEstandar));

            String descripcion = GlobalVariables.getDescripcion(Acto_obs,observacionModel.CodSubEstandar);

            if(descripcion.contains("COVID-19")){

                cv_51.setVisibility(View.VISIBLE);
                cv_52.setVisibility(View.GONE);
                cv_53.setVisibility(View.GONE);

                tx_sub_tipo.setText(GlobalVariables.getDescripcion(Covid, observacionModel.ComOpt1));


            } else if (descripcion.contains("EPP")){
                cv_51.setVisibility(View.GONE);
                cv_52.setVisibility(View.VISIBLE);
                cv_53.setVisibility(View.VISIBLE);

                if(observacionModel.ComOpt1 != null && !observacionModel.ComOpt1.equals("") && observacionModel.ComOpt1.contains(".")){
                    tx_tipo_prot.setText(GlobalVariables.getDescripcion(EPP, observacionModel.ComOpt1.split("\\.")[0]));
                    tx_epp.setText(GlobalVariables.getDescripcion(EPP, observacionModel.ComOpt1));

                }else if(observacionModel.ComOpt1 != null &&!observacionModel.ComOpt1.equals("") && !observacionModel.ComOpt1.contains(".")){
                    tx_tipo_prot.setText(GlobalVariables.getDescripcion(EPP, observacionModel.ComOpt1));
                }


            } else {
                cv_51.setVisibility(View.GONE);
                cv_52.setVisibility(View.GONE);
                cv_53.setVisibility(View.GONE);
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
