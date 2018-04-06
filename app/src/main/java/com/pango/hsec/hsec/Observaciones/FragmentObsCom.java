package com.pango.hsec.hsec.Observaciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.pango.hsec.hsec.model.ObservacionModel;

import static com.pango.hsec.hsec.GlobalVariables.Actividad_obs;
import static com.pango.hsec.hsec.GlobalVariables.Acto_obs;
import static com.pango.hsec.hsec.GlobalVariables.Error_obs;
import static com.pango.hsec.hsec.GlobalVariables.Estado_obs;
import static com.pango.hsec.hsec.GlobalVariables.HHA_obs;

public class FragmentObsCom extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match


    String jsonObsCom="";
    private View mView;
    String codObs;
    String url;
    TextView tx_obs_det,tx_accion,tx_act_rel,tx_hha,tx_sub_estandar,tx_estado,tx_error;
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

        GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=true;

        codObs=getArguments().getString("bString");
        url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codObs;

        if(jsonObsCom.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentObsCom.this);
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

        tx_obs_det.setText(observacionModel.Observacion);
        tx_accion.setText(observacionModel.Accion);
        tx_act_rel.setText(GlobalVariables.getDescripcion(Actividad_obs,observacionModel.CodActiRel));
        tx_hha.setText(GlobalVariables.getDescripcion(HHA_obs,observacionModel.CodHHA));
        tx_sub_estandar.setText(GlobalVariables.getDescripcion(Acto_obs,observacionModel.CodSubEstandar));
        tx_estado.setText(GlobalVariables.getDescripcion(Estado_obs,observacionModel.CodEstado));
        tx_error.setText(GlobalVariables.getDescripcion(Error_obs,observacionModel.CodError));



    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
