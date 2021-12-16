package com.pango.hsec.hsec.Ingresos.Inspecciones;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsInspDetModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentObsInsAdd extends Fragment implements IActivity {
    //ArrayList<Maestro> ubicacionEsp_data;
    ArrayList<Maestro> aspecto_data;
    ArrayList<Maestro> actividad_data;
    ArrayList<Maestro> nivel_data;

    Gson gson;
    TextView edit_ninsp,edit_codigo;
    EditText edit_lugar,edit_observacion;
    Spinner spinner_ubicacionEsp,spinner_aspecto,spinner_actividad,spinner_riesgo;
    String correlativo;
    TextView tx_aspecto, tx_actividad,tx_riesgo,tx_obs;
    public FragmentObsInsAdd() {
        // Required empty public constructor
    }
    private View mView;
    //String codObs;
    public static FragmentObsInsAdd newInstance(String Correlativo,String Grupo) {
        FragmentObsInsAdd fragment = new FragmentObsInsAdd();
        Bundle b = new Bundle();
        b.putString("correlativo", Correlativo);
        b.putString("grupo", Grupo);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_obs_ins_add, container, false);
        correlativo=getArguments().getString("correlativo");
        String grupo =getArguments().getString("grupo");
        gson = new Gson();

        edit_codigo=mView.findViewById(R.id.edit_codigo);

        edit_ninsp=mView.findViewById(R.id.edit_ninsp);

        edit_lugar=mView.findViewById(R.id.edit_lugar);
        edit_observacion=mView.findViewById(R.id.edit_observacion);
        spinner_ubicacionEsp=mView.findViewById(R.id.spinner_ubicacion);
        spinner_aspecto=mView.findViewById(R.id.spinner_aspecto);
        spinner_actividad=mView.findViewById(R.id.spinner_actividad);
        spinner_riesgo=mView.findViewById(R.id.spinner_riesgo);
        tx_aspecto=mView.findViewById(R.id.tx_aspecto);
        tx_aspecto.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Aspecto observado"));
        tx_actividad=mView.findViewById(R.id.tx_actividad);
        tx_actividad.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Actividad relacionada"));
        tx_riesgo=mView.findViewById(R.id.tx_riesgo);
        tx_riesgo.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Nivel de riesgo"));
        tx_obs=mView.findViewById(R.id.tx_obs);
        tx_obs.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Observación"));

/*
        textView9=mView.findViewById(R.id.textView9);
        textView9.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Ubicación:"));
*/



        edit_lugar.setText(GlobalVariables.obsInspDetModel.Lugar);
        edit_observacion.setText(GlobalVariables.obsInspDetModel.Observacion);

        ArrayAdapter adapterUbicEspc = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.UbicacionEspecifica_obs);
        adapterUbicEspc.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_ubicacionEsp.setAdapter(adapterUbicEspc);

        ArrayAdapter adapterAspecto = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Aspecto_Obs);
        adapterAspecto.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_aspecto.setAdapter(adapterAspecto);


        ArrayAdapter adapterNivel = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Actividad_obs);
        adapterNivel.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_actividad.setAdapter(adapterNivel);


        ArrayAdapter adapterActividad = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.NivelRiesgo_obs);
        adapterActividad.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_riesgo.setAdapter(adapterActividad);

        spinner_ubicacionEsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_ubicacion) ).getSelectedItem();
                GlobalVariables.obsInspDetModel.CodUbicacion=Tipo.CodTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_aspecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_aspecto) ).getSelectedItem();
                GlobalVariables.obsInspDetModel.CodAspectoObs=Tipo.CodTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                GlobalVariables.obsInspDetModel.CodAspectoObs=null;
            }
        });
        spinner_actividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_actividad) ).getSelectedItem();
                GlobalVariables.obsInspDetModel.CodActividadRel=Tipo.CodTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_riesgo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_riesgo) ).getSelectedItem();
                GlobalVariables.obsInspDetModel.CodNivelRiesgo=Tipo.CodTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edit_lugar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                GlobalVariables.obsInspDetModel.Lugar=s.toString().trim();
            }
        });

        edit_observacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GlobalVariables.obsInspDetModel.Observacion=s.toString().trim();

            }
        });

        if(ActObsInspEdit.editar){
            if(GlobalVariables.obsInspDetModel.Correlativo==null)  // load server
            {
                String url= GlobalVariables.Url_base+"Inspecciones/GetDetalleInspeccionID/"+correlativo;
                final ActivityController obj = new ActivityController("get", url, FragmentObsInsAdd.this,getActivity());
                obj.execute("");
            }
            else setdata();
        }
        else {
            if(GlobalVariables.obsInspDetModel.Correlativo==null){

                GlobalVariables.obsInspDetModel= new ObsInspDetModel();

                GlobalVariables.obsInspDetModel.Correlativo= "-1";
                GlobalVariables.obsInspDetModel.CodInspeccion=GlobalVariables.InspeccionObserbacion;
                GlobalVariables.obsInspDetModel.NroDetInspeccion=grupo;
                GlobalVariables.obsInspDetModel.Lugar="";
                GlobalVariables.obsInspDetModel.Observacion="";
                GlobalVariables.obsInspDetModel.CodUbicacion="";
                GlobalVariables.StrObservacion=gson.toJson(GlobalVariables.obsInspDetModel);
            }
          //  else //if(!GlobalVariables.Obserbacion.CodObservacion.contains("XYZ"))
                setdata();
        }
        return mView;
    }


    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        ObsInspDetModel getObsInspDetModel = gson.fromJson(data, ObsInspDetModel.class);
        //GlobalVariables.obsInspDetModel= new ObsInspDetModel();
        GlobalVariables.obsInspDetModel=getObsInspDetModel;
        GlobalVariables.obsInspDetModel.Correlativo=correlativo;
        GlobalVariables.StrObservacion=gson.toJson(GlobalVariables.obsInspDetModel);
        setdata();
    }

    public void updateCodigo(String Codigotxt){
        edit_ninsp.setText(Codigotxt);
    }

    public void setdata(){

        if(GlobalVariables.obsInspDetModel.CodInspeccion!=null)edit_codigo.setText(GlobalVariables.obsInspDetModel.CodInspeccion);
        if(GlobalVariables.obsInspDetModel.NroDetInspeccion!=null)edit_ninsp.setText(GlobalVariables.obsInspDetModel.NroDetInspeccion);
        if(GlobalVariables.obsInspDetModel.Lugar!=null)edit_lugar.setText(GlobalVariables.obsInspDetModel.Lugar);
        if(GlobalVariables.obsInspDetModel.CodUbicacion!=null)spinner_ubicacionEsp.setSelection(GlobalVariables.indexOf(GlobalVariables.UbicacionEspecifica_obs,GlobalVariables.obsInspDetModel.CodUbicacion));
        if(GlobalVariables.obsInspDetModel.CodAspectoObs!=null)spinner_aspecto.setSelection(GlobalVariables.indexOf(GlobalVariables.Aspecto_Obs,GlobalVariables.obsInspDetModel.CodAspectoObs));
        if(GlobalVariables.obsInspDetModel.CodActividadRel!=null)spinner_actividad.setSelection(GlobalVariables.indexOf(GlobalVariables.Actividad_obs,GlobalVariables.obsInspDetModel.CodActividadRel));
        if(GlobalVariables.obsInspDetModel.CodNivelRiesgo!=null)spinner_riesgo.setSelection(GlobalVariables.indexOf(GlobalVariables.NivelRiesgo_obs,GlobalVariables.obsInspDetModel.CodNivelRiesgo));
        if(GlobalVariables.obsInspDetModel.Observacion!=null)edit_observacion.setText(GlobalVariables.obsInspDetModel.Observacion);
    }


    @Override
    public void successpost(String data, String Tipo) {
    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
