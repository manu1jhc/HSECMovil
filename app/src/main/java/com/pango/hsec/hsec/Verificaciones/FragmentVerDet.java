package com.pango.hsec.hsec.Verificaciones;

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
import com.pango.hsec.hsec.model.VerificacionModel;

import static com.pango.hsec.hsec.GlobalVariables.StopWork_obs;

public class FragmentVerDet extends Fragment implements IActivity {
    View mView;
    TextView tx_observacion,tx_accion,tx_stopwork;
    String url;
    String codVer;

    public static FragmentVerDet newInstance(String sampleText) {
        FragmentVerDet f = new FragmentVerDet();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_ver_det, container, false);
        tx_observacion = mView.findViewById(R.id.tx_observacion);
        tx_accion = mView.findViewById(R.id.tx_accion);
        tx_stopwork = mView.findViewById(R.id.tx_stopwork);


        codVer=getArguments().getString("bString");
        url= GlobalVariables.Url_base+"Verificacion/Get/"+ codVer;

        if(GlobalVariables.jsonVerificaccion.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentVerDet.this,getActivity());
            obj.execute("");
        }else {
            success(GlobalVariables.jsonVerificaccion,"");
        }

        return mView;

    }


    @Override
    public void success(String data, String Tipo) {
        GlobalVariables.jsonVerificaccion =data;
        Gson gson = new Gson();
        VerificacionModel getUsuarioModel = gson.fromJson(data, VerificacionModel.class);

        tx_observacion.setText(getUsuarioModel.Observacion);

        tx_accion.setText(getUsuarioModel.Accion);


        tx_stopwork.setText(GlobalVariables.getDescripcion(StopWork_obs,getUsuarioModel.StopWork));

    }

    @Override
    public void successpost(String data, String Tipo){

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
