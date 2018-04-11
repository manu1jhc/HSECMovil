package com.pango.hsec.hsec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsDetalleModel;

public class obs_detalle1 extends Fragment implements IActivity{

    private static View mView;
    Spinner spinneActividad, spinnerHHA, spinnerActo,spinnerCondicion,spinnerEstado, spinnerError;
    EditText txtObservacion,txtAccion;
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
        String codigo_obs = getArguments().getString("bString");

        txtObservacion=(EditText) mView.findViewById(R.id.txt_observacion);
        txtAccion=(EditText) mView.findViewById(R.id.txt_accion);

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


        //detect chabgues values
        txtObservacion.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                GlobalVariables.ObserbacionDetalle.Observacion = txtObservacion.getText().toString();
            }
        });
        txtAccion.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                GlobalVariables.ObserbacionDetalle.Accion = txtAccion.getText().toString();
            }
        });

        spinneActividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_actividad) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodActiRel=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerHHA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_hha) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodHHA=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerActo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_acto) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodSubEstandar=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerCondicion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_condicion) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodSubEstandar=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_estado) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodEstado=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerError.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_error) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodError=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

       if(GlobalVariables.ObjectEditable){ // load data of server

           if(GlobalVariables.ObserbacionDetalle.CodObservacion==null || !GlobalVariables.ObserbacionDetalle.CodObservacion.equals(codigo_obs))
           {
               String url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codigo_obs;
               ActivityController obj = new ActivityController("get", url, obs_detalle1.this,getActivity());
               obj.execute("");
           }
           else setdata();
           GlobalVariables.ObserbacionDetalle.CodObservacion=codigo_obs;
        }
        else // new Obserbacion
        {
            if(GlobalVariables.ObserbacionDetalle.CodObservacion==null || !GlobalVariables.Obserbacion.CodObservacion.contains("XYZ")){

                GlobalVariables.ObserbacionDetalle.CodObservacion=codigo_obs;
                GlobalVariables.ObserbacionDetalle.Observacion="";
                GlobalVariables.ObserbacionDetalle.Accion="";
                GlobalVariables.ObserbacionDetalle.CodActiRel=GlobalVariables.Actividad_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodHHA=GlobalVariables.HHA_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodEstado=GlobalVariables.Estado_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodError=GlobalVariables.Error_obs.get(0).CodTipo;
            }
            setdata();
        }

        return mView;
    }

    public void setdata(){
        txtObservacion.setText(GlobalVariables.ObserbacionDetalle.Observacion);
        txtAccion.setText(GlobalVariables.ObserbacionDetalle.Accion);

        spinneActividad.setSelection(GlobalVariables.indexOf(GlobalVariables.Actividad_obs,GlobalVariables.ObserbacionDetalle.CodActiRel));
        spinnerHHA.setSelection(GlobalVariables.indexOf(GlobalVariables.HHA_obs,GlobalVariables.ObserbacionDetalle.CodHHA));
        if(GlobalVariables.Obserbacion.CodTipo=="TO01")
            spinnerActo.setSelection(GlobalVariables.indexOf(GlobalVariables.Acto_obs,GlobalVariables.ObserbacionDetalle.CodSubEstandar));
        else spinnerCondicion.setSelection(GlobalVariables.indexOf(GlobalVariables.Condicion_obs,GlobalVariables.ObserbacionDetalle.CodSubEstandar));
        spinnerEstado.setSelection(GlobalVariables.indexOf(GlobalVariables.Estado_obs,GlobalVariables.ObserbacionDetalle.CodEstado));
        spinnerError.setSelection(GlobalVariables.indexOf(GlobalVariables.Error_obs,GlobalVariables.ObserbacionDetalle.CodError));
    }

    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        String codigo_obs = GlobalVariables.ObserbacionDetalle.CodObservacion;
        GlobalVariables.ObserbacionDetalle = gson.fromJson(data, ObsDetalleModel.class);
        GlobalVariables.ObserbacionDetalle.CodObservacion=codigo_obs;
        setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}