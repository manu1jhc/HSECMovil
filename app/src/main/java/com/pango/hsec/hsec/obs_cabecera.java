package com.pango.hsec.hsec;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObservacionModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class obs_cabecera extends Fragment implements IActivity{

    private static View mView, dView;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    DialogFragment newFragment;
    Button botonEscogerFecha;
    Spinner spinnerArea, spinnerNivel, spinnerUbica,spinnerSububic,spinnerUbicEspec, spinnerTipoObs;
    String Ubicacionfinal="",TipoObs;
    EditText txtLugar;
    String Ubicacion="";
    final boolean[] pass1 = {false};
    public ArrayAdapter adapterUbicEspc,adapterSubN;


    public static final obs_cabecera newInstance(String sampleText) {
        obs_cabecera f = new obs_cabecera();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_cabecera, container, false);
        final boolean[] pass2 = {false};
        final Date opentab= new Date();
        String codigo_obs = getArguments().getString("bString");
        txtLugar=(EditText) mView.findViewById(R.id.txt_lugar);
        spinnerArea = (Spinner) mView.findViewById(R.id.spinner_area);
        spinnerNivel = (Spinner) mView.findViewById(R.id.spinner_NivelR);
        spinnerUbica = (Spinner) mView.findViewById(R.id.spinner_ubic);
        spinnerSububic = (Spinner) mView.findViewById(R.id.spinner_sububic);
        spinnerUbicEspec = (Spinner) mView.findViewById(R.id.spinner_ubicespc);
        spinnerTipoObs = (Spinner) mView.findViewById(R.id.spinner_tipobs);

        ArrayAdapter adapterTipoObs = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Tipo_obs);
        adapterTipoObs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTipoObs.setAdapter(adapterTipoObs);

        ArrayAdapter adapterArea = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Area_obs);
        adapterArea.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerArea.setAdapter(adapterArea);

        ArrayAdapter adapterNivel = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.NivelRiesgo_obs);
        adapterNivel.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerNivel.setAdapter(adapterNivel);

        //Ubicaciones
        ArrayAdapter adapterUbic = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Ubicacion_obs);
        adapterUbic.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerUbica.setAdapter(adapterUbic);

        adapterSubN = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.SubUbicacion_obs);
        adapterSubN.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSububic.setAdapter(adapterSubN);

        adapterUbicEspc = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.UbicacionEspecifica_obs);
        adapterUbicEspc.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerUbicEspec.setAdapter(adapterUbicEspc);

        ///////////////////////////

        TextView Codigo = (TextView) mView.findViewById(R.id.id_CodObservacion);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        botonEscogerFecha=(Button) mView.findViewById(R.id.btn_fecha);

        Date fecha=new Date();
        if(GlobalVariables.ObjectEditable){ // load data of server
            if(GlobalVariables.Obserbacion.CodObservacion==null || !GlobalVariables.Obserbacion.CodObservacion.equals(codigo_obs))
            {
                String url= GlobalVariables.Url_base+"Observaciones/Get/"+codigo_obs;
                ActivityController obj = new ActivityController("get", url, obs_cabecera.this,getActivity());
                obj.execute("");
            }
            else setdata();
        }
        else // new Obserbacion
        {
            if(GlobalVariables.Obserbacion.CodObservacion==null||!GlobalVariables.Obserbacion.CodObservacion.contains("XYZ")){

                myCalendar = Calendar.getInstance();
                fecha = myCalendar.getTime();
                GlobalVariables.Obserbacion= new ObservacionModel();
                GlobalVariables.Obserbacion.CodObservacion= codigo_obs;
                GlobalVariables.Obserbacion.Fecha=df.format(fecha);
                GlobalVariables.Obserbacion.Lugar="";
                GlobalVariables.Obserbacion.CodAreaHSEC=GlobalVariables.Area_obs.get(0).CodTipo;
                GlobalVariables.Obserbacion.CodNivelRiesgo=GlobalVariables.NivelRiesgo_obs.get(0).CodTipo;
                GlobalVariables.Obserbacion.CodTipo=GlobalVariables.Tipo_obs.get(0).CodTipo;
                GlobalVariables.Obserbacion.CodUbicacion="";
                GlobalVariables.ObserbacionDetalle.CodSubEstandar=GlobalVariables.Acto_obs.get(0).CodTipo;
            }
          //  else if(!GlobalVariables.Obserbacion.CodObservacion.contains("XYZ"))

           setdata();
        }

        //inicialice data
        Codigo.setText(codigo_obs);

        ////////////////////////////

// DETECT changue values
        spinnerUbica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro ubica = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_ubic) ).getSelectedItem();
                Ubicacionfinal=ubica.CodTipo;
                GlobalVariables.SubUbicacion_obs.clear();
                for (Maestro item: GlobalVariables.loadUbicacion(Ubicacionfinal,2)
                     ) {
                    GlobalVariables.SubUbicacion_obs.add(item);
                }
                adapterSubN.notifyDataSetChanged();
                if(!pass1[0]){
                    Date openSpinner=new Date();
                    long diff=openSpinner.getTime()-opentab.getTime();
                    if(diff>2000)
                    {
                        GlobalVariables.Obserbacion.CodUbicacion=Ubicacionfinal;
                        spinnerSububic.setSelection(0);
                    }
                }
                else {
                    pass1[0] =false;
                    String data[]= Ubicacion.split("\\.");
                    if(data.length>1)
                    {
                        pass2[0] =true;
                        spinnerSububic.setSelection(GlobalVariables.indexOf(GlobalVariables.SubUbicacion_obs,data[0]+"."+data[1]));
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Ubicacionfinal = "";
            }
        });
        spinnerSububic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Maestro Sububica = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_sububic) ).getSelectedItem();
                Ubicacionfinal=Sububica.CodTipo;
                GlobalVariables.UbicacionEspecifica_obs.clear();
                for (Maestro item: GlobalVariables.loadUbicacion(Ubicacionfinal,3)) {
                    GlobalVariables.UbicacionEspecifica_obs.add(item);
                }
                adapterUbicEspc.notifyDataSetChanged();
                if(!pass2[0]){
                    Date openSpinner=new Date();
                    long diff=openSpinner.getTime()-opentab.getTime();
                    if(diff>2000){
                        GlobalVariables.Obserbacion.CodUbicacion=Ubicacionfinal;
                        spinnerUbicEspec.setSelection(0);
                    }
                }
                else {
                    pass2[0] =false;
                    if(Ubicacion.split("\\.").length==3)
                    spinnerUbicEspec.setSelection(GlobalVariables.indexOf(GlobalVariables.UbicacionEspecifica_obs,Ubicacion));
                    else spinnerUbicEspec.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Ubicacionfinal = Ubicacionfinal.split("\\.")[0];
            }
        });
        spinnerUbicEspec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro UbicaEspec = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_ubicespc) ).getSelectedItem();
                Ubicacionfinal=UbicaEspec.CodTipo;
                GlobalVariables.Obserbacion.CodUbicacion=UbicaEspec.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                String Ubic[] =  Ubicacionfinal.split("\\.");
                Ubicacionfinal = Ubic[0]+"."+ Ubic[1];
            }
        });
        spinnerTipoObs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_tipobs) ).getSelectedItem();
                TabHost mTabHost = (TabHost) getActivity().findViewById(android.R.id.tabhost);
                TabWidget tabWidget =(TabWidget)getActivity().findViewById(android.R.id.tabs);
                GlobalVariables.Obserbacion.CodTipo=Tipo.CodTipo;
                GlobalVariables.ObserbacionDetalle.CodSubEstandar=Tipo.CodTipo=="TO01"?GlobalVariables.Acto_obs.get(0).CodTipo:GlobalVariables.Condicion_obs.get(0).CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_area) ).getSelectedItem();
                GlobalVariables.Obserbacion.CodAreaHSEC=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_NivelR) ).getSelectedItem();
                GlobalVariables.Obserbacion.CodNivelRiesgo=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        txtLugar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                GlobalVariables.Obserbacion.Lugar = txtLugar.getText().toString();
            }
        });
        botonEscogerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newFragment = new SelectDateFragment();
                newFragment.show(getActivity().getFragmentManager(), "DatePicker");
            }
        });

        return mView;
    }

    public  void newObservacion(){

    }
  public void setdata(){
      Date fecha=new Date();
      SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

      try {
          fecha = df.parse(GlobalVariables.Obserbacion.Fecha);
      } catch (ParseException e) {
          e.printStackTrace();
      }

      txtLugar.setText(GlobalVariables.Obserbacion.Lugar);
      botonEscogerFecha.setText(dt.format(fecha));

      spinnerArea.setSelection(GlobalVariables.indexOf(GlobalVariables.Area_obs,GlobalVariables.Obserbacion.CodAreaHSEC));
      spinnerNivel.setSelection(GlobalVariables.indexOf(GlobalVariables.NivelRiesgo_obs,GlobalVariables.Obserbacion.CodNivelRiesgo));
      if(GlobalVariables.Obserbacion.CodUbicacion!=null&&!GlobalVariables.Obserbacion.CodUbicacion.isEmpty()){
          String data[]=GlobalVariables.Obserbacion.CodUbicacion.split("\\.");
          spinnerUbica.setSelection(GlobalVariables.indexOf(GlobalVariables.Ubicacion_obs,data[0]));
          Ubicacion=GlobalVariables.Obserbacion.CodUbicacion;
          pass1[0] =true;
      }
      spinnerTipoObs.setSelection(GlobalVariables.indexOf(GlobalVariables.Tipo_obs,GlobalVariables.Obserbacion.CodTipo));

  }
    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        GlobalVariables.Obserbacion = gson.fromJson(data, ObservacionModel.class);
        setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}


