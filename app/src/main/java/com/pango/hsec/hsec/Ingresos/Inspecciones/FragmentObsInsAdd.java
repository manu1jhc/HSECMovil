package com.pango.hsec.hsec.Ingresos.Inspecciones;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.FragmentObsdet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsInspAddModel;
import com.pango.hsec.hsec.model.ObsInspDetModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentObsInsAdd extends Fragment implements IActivity {
    //ArrayList<Maestro> ubicacionEsp_data;
    ArrayList<Maestro> aspecto_data;
    ArrayList<Maestro> actividad_data;
    ArrayList<Maestro> nivel_data;


    TextView edit_ninsp,edit_codigo;
    EditText edit_lugar,edit_observacion;
    Spinner spinner_ubicacionEsp,spinner_aspecto,spinner_actividad,spinner_riesgo;
    String url, correlativo;
    public FragmentObsInsAdd() {
        // Required empty public constructor
    }
    private View mView;
    //String codObs;
    public static FragmentObsInsAdd newInstance(String sampleText) {
        FragmentObsInsAdd fragment = new FragmentObsInsAdd();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_obs_ins_add, container, false);
        correlativo=getArguments().getString("bString");
        edit_codigo=mView.findViewById(R.id.edit_codigo);

        edit_ninsp=mView.findViewById(R.id.edit_ninsp);

        edit_lugar=mView.findViewById(R.id.edit_lugar);
        edit_observacion=mView.findViewById(R.id.edit_observacion);
        spinner_ubicacionEsp=mView.findViewById(R.id.spinner_ubicacion);
        spinner_aspecto=mView.findViewById(R.id.spinner_aspecto);
        spinner_actividad=mView.findViewById(R.id.spinner_actividad);
        spinner_riesgo=mView.findViewById(R.id.spinner_riesgo);


        edit_lugar.setText(GlobalVariables.obsInspDetModel.Lugar);
        edit_observacion.setText(GlobalVariables.obsInspDetModel.Observacion);


        GlobalVariables.obsInspDetModel.CodInspeccion="-1";


        if(ActObsInspEdit.editar){
            url= GlobalVariables.Url_base+"Inspecciones/GetDetalleInspeccionID/"+correlativo;
            final ActivityController obj = new ActivityController("get", url, FragmentObsInsAdd.this,getActivity());
            obj.execute("");
        }



        aspecto_data= new ArrayList<>();
        aspecto_data.add(new Maestro(null,"-  Seleccione  -"));
        aspecto_data.addAll(GlobalVariables.Aspecto_Obs);
///         UbicacionEspecifica_obs

        actividad_data= new ArrayList<>();
        actividad_data.add(new Maestro(null,"-  Seleccione  -"));
        actividad_data.addAll(GlobalVariables.Actividad_obs);

        nivel_data= new ArrayList<>();
        nivel_data.add(new Maestro(null,"-  Seleccione  -"));
        nivel_data.addAll(GlobalVariables.NivelRiesgo_obs);


        ArrayAdapter adapterUbicEspc = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.UbicacionEspecifica_obs);
        adapterUbicEspc.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_ubicacionEsp.setAdapter(adapterUbicEspc);

        ArrayAdapter adapterAspecto = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,aspecto_data);
        adapterAspecto.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_aspecto.setAdapter(adapterAspecto);


        ArrayAdapter adapterNivel = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,actividad_data);
        adapterNivel.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_actividad.setAdapter(adapterNivel);


        ArrayAdapter adapterActividad = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,nivel_data);
        adapterActividad.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_riesgo.setAdapter(adapterActividad);

        if(ActObsInspEdit.editar){
            spinner_ubicacionEsp.setSelection(GlobalVariables.indexOf(GlobalVariables.UbicacionEspecifica_obs,GlobalVariables.obsInspDetModel.CodUbicacion));

            //String minutoFormateado = (minutoFinal < 10)? String.valueOf(CERO + minutoFinal):String.valueOf(minutoFinal);


            if(GlobalVariables.obsInspDetModel.CodAspectoObs==null){
                spinner_aspecto.setSelection(0);
            }else{
                spinner_aspecto.setSelection(1+GlobalVariables.indexOf(GlobalVariables.Aspecto_Obs,GlobalVariables.obsInspDetModel.CodAspectoObs));

            }



            //spinner_aspecto.setSelection((GlobalVariables.indexOf(GlobalVariables.Aspecto_Obs,GlobalVariables.obsInspDetModel.CodAspectoObs)==0)?0
            //        :GlobalVariables.indexOf(GlobalVariables.Aspecto_Obs,GlobalVariables.obsInspDetModel.CodAspectoObs)+1);
            spinner_actividad.setSelection(1+GlobalVariables.indexOf(GlobalVariables.Actividad_obs,GlobalVariables.obsInspDetModel.CodActividadRel));
            spinner_riesgo.setSelection(1+GlobalVariables.indexOf(GlobalVariables.NivelRiesgo_obs,GlobalVariables.obsInspDetModel.CodNivelRiesgo));


        }/*else {

        }*/
        spinner_ubicacionEsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Utils.inspeccionModel.CodUbicacion=ubicaciondata.get(position).CodTipo;
                String data;
                if(position!=0) {
                    GlobalVariables.obsInspDetModel.CodUbicacion =GlobalVariables.UbicacionEspecifica_obs.get(position).CodTipo;

                    //area = area_data.get(position).CodTipo;
                    //area_pos= String.valueOf(position);
                }else {
                    GlobalVariables.obsInspDetModel.CodUbicacion =null;
                    //data=null;
                    //area="";
                    //area_pos=String.valueOf(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinner_aspecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(position!=0) {
                    GlobalVariables.obsInspDetModel.CodAspectoObs=aspecto_data.get(position).CodTipo;

                    //area = area_data.get(position).CodTipo;
                    /////////area_pos= String.valueOf(position);
                }else {
                    GlobalVariables.obsInspDetModel.CodAspectoObs=null;
                    //area="";
                    //area_pos=String.valueOf(position);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                GlobalVariables.obsInspDetModel.CodAspectoObs=null;
            }
        });


        spinner_actividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    GlobalVariables.obsInspDetModel.CodActividadRel=actividad_data.get(position).CodTipo;

                    //area = area_data.get(position).CodTipo;
                    /////////area_pos= String.valueOf(position);
                }else {
                    GlobalVariables.obsInspDetModel.CodActividadRel=null;
                    //area="";
                    //area_pos=String.valueOf(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_riesgo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    GlobalVariables.obsInspDetModel.CodNivelRiesgo=nivel_data.get(position).CodTipo;

                    //area = area_data.get(position).CodTipo;
                    /////////area_pos= String.valueOf(position);
                }else {
                    GlobalVariables.obsInspDetModel.CodNivelRiesgo=null;
                    //area="";
                    //area_pos=String.valueOf(position);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //////////////////////////////////////////////////GlobalVariables.AddInspeccion.CodSubUbicacion

        //GlobalVariables.ListaObsInsp
        //GlobalVariables.countObsInsp
        edit_ninsp.setText(GlobalVariables.countObsInsp+"");
        GlobalVariables.obsInspDetModel.NroDetInspeccion=String.valueOf(GlobalVariables.countObsInsp);

        ///if()




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

        return mView;
    }


    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        ObsInspDetModel getObsInspDetModel = gson.fromJson(data, ObsInspDetModel.class);
        ///GlobalVariables.obsInspDetModel= new ObsInspDetModel();
        GlobalVariables.obsInspDetModel=getObsInspDetModel;
        setdata();
    }



    public void setdata(){


        edit_codigo.setText(GlobalVariables.obsInspDetModel.CodInspeccion);
        edit_ninsp.setText(GlobalVariables.obsInspDetModel.NroDetInspeccion);
        edit_lugar.setText(GlobalVariables.obsInspDetModel.Lugar);
        spinner_ubicacionEsp.setSelection(GlobalVariables.indexOf(GlobalVariables.UbicacionEspecifica_obs,GlobalVariables.obsInspDetModel.CodUbicacion));

        spinner_aspecto.setSelection(GlobalVariables.indexOf(GlobalVariables.Aspecto_Obs,GlobalVariables.obsInspDetModel.CodAspectoObs)+1);

        spinner_actividad.setSelection(GlobalVariables.indexOf(GlobalVariables.Actividad_obs,GlobalVariables.obsInspDetModel.CodActividadRel)+1);
        spinner_riesgo.setSelection(GlobalVariables.indexOf(GlobalVariables.NivelRiesgo_obs,GlobalVariables.obsInspDetModel.CodNivelRiesgo)+1);

        edit_observacion.setText(GlobalVariables.obsInspDetModel.Observacion);

    }




    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
