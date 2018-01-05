package com.pango.hsec.hsec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.R;

public class obs_detalle1 extends Fragment {

    private static View mView;
    Spinner spinneActividad, spinnerHHA, spinnerActo,spinnerCondicion,spinnerEstado, spinnerError;
    public static final com.pango.hsec.hsec.obs_detalle1 newInstance(String sampleText) {
        obs_detalle1 f = new obs_detalle1();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_detalle1, container,false);

        spinneActividad = (Spinner) mView.findViewById(R.id.sp_actividad);
        spinnerHHA = (Spinner) mView.findViewById(R.id.sp_hha);
        spinnerActo = (Spinner) mView.findViewById(R.id.sp_acto);
        spinnerCondicion = (Spinner) mView.findViewById(R.id.sp_condicion);
        spinnerEstado = (Spinner) mView.findViewById(R.id.sp_estado);
        spinnerError = (Spinner) mView.findViewById(R.id.sp_error);

        ArrayAdapter adapterActividadObs = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Actividad_obs);
        adapterActividadObs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinneActividad.setAdapter(adapterActividadObs);

        ArrayAdapter adapterHHA = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.HHA_obs);
        adapterHHA.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerHHA.setAdapter(adapterHHA);

        ArrayAdapter adapterActo = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Acto_obs);
        adapterActo.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerActo.setAdapter(adapterActo);

        ArrayAdapter adapterCondicion = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Condicion_obs);
        adapterCondicion.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCondicion.setAdapter(adapterCondicion);

        ArrayAdapter adapterEstado = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Estado_obs);
        adapterEstado.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapterEstado);

        ArrayAdapter adapterError = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Error_obs);
        adapterError.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerError.setAdapter(adapterError);


      /*  TextView txtSampleText = (TextView) mView.findViewById(R.id.txtViewSample);
        txtSampleText.setText(sampleText);
        Button button=(Button) mView.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Click en publicacion",Toast.LENGTH_SHORT).show();

            }
        });*/
        return mView;
    }
}