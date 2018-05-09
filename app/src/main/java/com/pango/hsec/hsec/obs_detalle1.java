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

import org.apache.commons.lang3.StringUtils;

public class obs_detalle1 extends Fragment implements IActivity{

    private static View mView;
    Spinner spinneActividad, spinnerHHA, spinnerActo,spinnerCondicion,spinnerEstado, spinnerError;
    EditText txtObservacion,txtAccion;
    public static final com.pango.hsec.hsec.obs_detalle1 newInstance(String sampleText,String CodTipo) {
        obs_detalle1 f = new obs_detalle1();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        b.putString("bTipo", CodTipo);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_detalle1, container,false);
        String codigo_obs = getArguments().getString("bString");
        String Tipo = getArguments().getString("bTipo");
        txtObservacion=(EditText) mView.findViewById(R.id.txt_observacion);
        txtAccion=(EditText) mView.findViewById(R.id.txt_accion);

        spinneActividad = (Spinner) mView.findViewById(R.id.sp_actividad);
        spinnerHHA = (Spinner) mView.findViewById(R.id.sp_hha);
        spinnerActo = (Spinner) mView.findViewById(R.id.sp_acto);
        spinnerCondicion = (Spinner) mView.findViewById(R.id.sp_condicion);
        spinnerEstado = (Spinner) mView.findViewById(R.id.sp_estado);
        spinnerError = (Spinner) mView.findViewById(R.id.sp_error);

        ArrayAdapter adapterActividadObs = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Actividad_obs);
        adapterActividadObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinneActividad.setAdapter(adapterActividadObs);

        ArrayAdapter adapterHHA = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.HHA_obs);
        adapterHHA.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerHHA.setAdapter(adapterHHA);

        ArrayAdapter adapterActo = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Acto_obs);
        adapterActo.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerActo.setAdapter(adapterActo);

        ArrayAdapter adapterCondicion = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Condicion_obs);
        adapterCondicion.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerCondicion.setAdapter(adapterCondicion);

        ArrayAdapter adapterEstado = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Estado_obs);
        adapterEstado.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapterEstado);

        ArrayAdapter adapterError = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Error_obs);
        adapterError.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
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
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO01")){
                    Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_acto) ).getSelectedItem();
                    GlobalVariables.ObserbacionDetalle.CodSubEstandar=Tipo.CodTipo;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerCondicion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO02")) {
                    Maestro Tipo = (Maestro) ((Spinner) mView.findViewById(R.id.sp_condicion)).getSelectedItem();
                    GlobalVariables.ObserbacionDetalle.CodSubEstandar = Tipo.CodTipo;
                }
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
        if(GlobalVariables.Obserbacion==null)changueTipo(Tipo);

       if(GlobalVariables.ObjectEditable){ // load data of server
           if(GlobalVariables.ObserbacionDetalle.CodObservacion==null)
           {
               String url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codigo_obs;
               ActivityController obj = new ActivityController("get", url, obs_detalle1.this,getActivity());
               obj.execute("");
           }
           else setdata();
        }
        else // new Obserbacion
        {
            if(GlobalVariables.ObserbacionDetalle.CodObservacion==null){
                Gson gson = new Gson();
                GlobalVariables.ObserbacionDetalle.CodObservacion=codigo_obs;
                GlobalVariables.ObserbacionDetalle.Observacion="";
                GlobalVariables.ObserbacionDetalle.Accion="";
                GlobalVariables.ObserbacionDetalle.CodActiRel=GlobalVariables.Actividad_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodHHA=GlobalVariables.HHA_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodEstado=GlobalVariables.Estado_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodError=GlobalVariables.Error_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodTipo=Tipo;

                GlobalVariables.StrObsDetalle=gson.toJson(GlobalVariables.ObserbacionDetalle);
            }
            setdata();
        }

        return mView;
    }

    public void changueTipo(String Tipo){

        if(!StringUtils.isEmpty(Tipo)&& Tipo.equals("TO01")){
            mView.findViewById(R.id.id_Condicion).setVisibility(View.GONE);
            mView.findViewById(R.id.id_Acto).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.id_Estado).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.id_Error).setVisibility(View.VISIBLE);
        }
        else{
            mView.findViewById(R.id.id_Condicion).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.id_Acto).setVisibility(View.GONE);
            mView.findViewById(R.id.id_Estado).setVisibility(View.GONE);
            mView.findViewById(R.id.id_Error).setVisibility(View.GONE);
        }
    }
    public void setdata(){

        if(GlobalVariables.ObserbacionDetalle.Observacion!=null)txtObservacion.setText(GlobalVariables.ObserbacionDetalle.Observacion);
        if(GlobalVariables.ObserbacionDetalle.Accion!=null)txtAccion.setText(GlobalVariables.ObserbacionDetalle.Accion);

        if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodActiRel))spinneActividad.setSelection(GlobalVariables.indexOf(GlobalVariables.Actividad_obs,GlobalVariables.ObserbacionDetalle.CodActiRel));
        if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodHHA))spinnerHHA.setSelection(GlobalVariables.indexOf(GlobalVariables.HHA_obs,GlobalVariables.ObserbacionDetalle.CodHHA));

        if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar))spinnerActo.setSelection(GlobalVariables.indexOf(GlobalVariables.Acto_obs,GlobalVariables.ObserbacionDetalle.CodSubEstandar));
        if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar))spinnerCondicion.setSelection(GlobalVariables.indexOf(GlobalVariables.Condicion_obs,GlobalVariables.ObserbacionDetalle.CodSubEstandar));

        if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodEstado))spinnerEstado.setSelection(GlobalVariables.indexOf(GlobalVariables.Estado_obs,GlobalVariables.ObserbacionDetalle.CodEstado));
        if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodError))spinnerError.setSelection(GlobalVariables.indexOf(GlobalVariables.Error_obs,GlobalVariables.ObserbacionDetalle.CodError));
    }

    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        String codigo_obs = GlobalVariables.Obserbacion.CodObservacion;
        ObsDetalleModel temp= gson.fromJson(data, ObsDetalleModel.class);
        GlobalVariables.ObserbacionDetalle=temp;
        if(GlobalVariables.ObserbacionDetalle==null)
            GlobalVariables.ObserbacionDetalle= new ObsDetalleModel();
        GlobalVariables.ObserbacionDetalle.CodObservacion=codigo_obs;
            GlobalVariables.StrObsDetalle = gson.toJson(GlobalVariables.ObserbacionDetalle);
        setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}