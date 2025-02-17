package com.pango.hsec.hsec;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.model.UsuarioModel;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class obs_cabecera extends Fragment implements IActivity{

    private static View mView, dView;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    DialogFragment newFragment;
    Button botonEscogerFecha;
    ImageButton btnSelectObservador;
    Spinner spinnerArea, spinnerNivel, spinnerUbica,spinnerSububic,spinnerUbicEspec, spinnerTipoObs,spinnerSubtipo;
    CardView CarSubTipo;
    String Ubicacionfinal="";
    EditText txtLugar;
    TextView txtObservado;
    TextView Codigo;
    String Ubicacion="";
    TextView textView6, textView7,textView9,textView12;

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
        boolean[] pass = {false,false};
        Integer[] itemSel = {0,0,0};
        String codigo_obs = getArguments().getString("bString");
        txtObservado=(TextView) mView.findViewById(R.id.txt_observadopor);
        txtLugar=(EditText) mView.findViewById(R.id.txt_lugar);
        spinnerArea = (Spinner) mView.findViewById(R.id.spinner_area);
        spinnerNivel = (Spinner) mView.findViewById(R.id.spinner_NivelR);
        spinnerUbica = (Spinner) mView.findViewById(R.id.spinner_ubic);
        spinnerSububic = (Spinner) mView.findViewById(R.id.spinner_sububic);
        spinnerUbicEspec = (Spinner) mView.findViewById(R.id.spinner_ubicespc);
        spinnerTipoObs = (Spinner) mView.findViewById(R.id.spinner_tipobs);
        spinnerSubtipo = (Spinner) mView.findViewById(R.id.spinner_subtipo);
        CarSubTipo =(CardView) mView.findViewById(R.id.id_SubTipo);
        textView6=mView.findViewById(R.id.textView6);
        textView6.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Área"));
        //sp2.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Responsable"));
        textView7=mView.findViewById(R.id.textView7);
        textView7.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Nivel de Riesgo:"));
        textView9=mView.findViewById(R.id.textView9);
        textView9.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Ubicación:"));
        textView12=mView.findViewById(R.id.textView12);
        textView12.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Lugar:"));

        ArrayAdapter adapterTipoObs = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Tipo_obs2);
        adapterTipoObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerTipoObs.setAdapter(adapterTipoObs);

        ArrayAdapter adapterSubTipo = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.SubTipo_obs);
        adapterSubTipo.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerSubtipo.setAdapter(adapterSubTipo);

        ArrayAdapter adapterArea = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Area_obs);
        adapterArea.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerArea.setAdapter(adapterArea);

        ArrayAdapter adapterNivel = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.NivelRiesgo_obs);
        adapterNivel.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerNivel.setAdapter(adapterNivel);

        //Ubicaciones
        GlobalVariables.reloadUbicacion();
        ArrayAdapter adapterUbic = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Ubicacion_obs);
        adapterUbic.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerUbica.setAdapter(adapterUbic);

        adapterSubN = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.SubUbicacion_obs);
        adapterSubN.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerSububic.setAdapter(adapterSubN);

        adapterUbicEspc = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.UbicacionEspecifica_obs);
        adapterUbicEspc.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerUbicEspec.setAdapter(adapterUbicEspc);

        ///////////////////////////

        Codigo = (TextView) mView.findViewById(R.id.id_CodObservacion);
        btnSelectObservador=(ImageButton) mView.findViewById(R.id.btn_observadopor);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        botonEscogerFecha=(Button) mView.findViewById(R.id.btn_fecha);

        Date fecha=new Date();
        if(GlobalVariables.ObjectEditable){ // load data of server
            if(GlobalVariables.Obserbacion.CodObservacion==null)
            {
                String url= GlobalVariables.Url_base+"Observaciones/Get/"+codigo_obs;
                ActivityController obj = new ActivityController("get", url, obs_cabecera.this,getActivity());
                obj.execute("");
            }
            else setdata();
        }
        else // new Obserbacion
        {
            if(GlobalVariables.Obserbacion.CodObservacion==null){
                pass[0]=true;
                pass[1]=true;
                myCalendar = Calendar.getInstance();
                fecha = myCalendar.getTime();
                Gson gson = new Gson();
                UsuarioModel UsuarioLogeado = gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);

                GlobalVariables.Obserbacion= new ObservacionModel();
                GlobalVariables.Obserbacion.CodObservacion= codigo_obs;

                GlobalVariables.Obserbacion.ObservadoPor=UsuarioLogeado.Nombres;
                GlobalVariables.Obserbacion.CodObservadoPor=UsuarioLogeado.CodPersona;
                GlobalVariables.Obserbacion.Fecha=df.format(fecha);
                GlobalVariables.Obserbacion.Lugar="";
                GlobalVariables.Obserbacion.CodAreaHSEC=GlobalVariables.Area_obs.get(0).CodTipo;
                GlobalVariables.Obserbacion.CodNivelRiesgo=GlobalVariables.NivelRiesgo_obs.get(0).CodTipo;
                GlobalVariables.Obserbacion.CodTipo=GlobalVariables.Tipo_obs2.get(0).CodTipo;
                GlobalVariables.Obserbacion.CodUbicacion="";
                GlobalVariables.StrObservacion=gson.toJson(GlobalVariables.Obserbacion);
            }
            //else if(!GlobalVariables.Obserbacion.CodObservacion.contains("XYZ"))
            setdata();
        }

        //inicialice data

        ////////////////////////////

// DETECT changue values
        spinnerUbica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(itemSel[0]!=position) {
                    itemSel[0]=position;
                    Maestro ubica = (Maestro) ((Spinner) mView.findViewById(R.id.spinner_ubic)).getSelectedItem();
                    Ubicacionfinal = ubica.CodTipo;
                    GlobalVariables.SubUbicacion_obs.clear();
                    for (Maestro item : GlobalVariables.loadUbicacion(Ubicacionfinal, 2)) {
                        GlobalVariables.SubUbicacion_obs.add(item);
                    }
                    adapterSubN.notifyDataSetChanged();
                    if(!pass[0]&&GlobalVariables.Obserbacion.CodUbicacion!=null)
                    {
                        pass[0] =true;
                        String data[]= Ubicacion.split("\\.");
                        if(data.length>1)
                            spinnerSububic.setSelection(GlobalVariables.indexOf(GlobalVariables.SubUbicacion_obs,data[0]+"."+data[1]));
                        else pass[1] =true;
                    }
                    else {
                        GlobalVariables.Obserbacion.CodUbicacion=Ubicacionfinal;
                        spinnerSububic.setSelection(0);
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

                if(itemSel[1]!=position) {
                    itemSel[1]=position;
                    Maestro Sububica = (Maestro) ((Spinner) mView.findViewById(R.id.spinner_sububic)).getSelectedItem();
                    Ubicacionfinal = Sububica.CodTipo;
                    GlobalVariables.UbicacionEspecifica_obs.clear();
                    for (Maestro item : GlobalVariables.loadUbicacion(Ubicacionfinal, 3)) {
                        GlobalVariables.UbicacionEspecifica_obs.add(item);
                    }
                    adapterUbicEspc.notifyDataSetChanged();
                    if(!pass[1]&&GlobalVariables.Obserbacion.CodUbicacion!=null)
                    {
                        pass[1] =true;
                        if(Ubicacion.split("\\.").length==3)
                            spinnerUbicEspec.setSelection(GlobalVariables.indexOf(GlobalVariables.UbicacionEspecifica_obs, GlobalVariables.Obserbacion.CodUbicacion));
                    }
                    else{
                        GlobalVariables.Obserbacion.CodUbicacion=Ubicacionfinal;
                   }
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
               if(position>0)
               {
                   Maestro UbicaEspec = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_ubicespc) ).getSelectedItem();
                   Ubicacionfinal=UbicaEspec.CodTipo;
                   GlobalVariables.Obserbacion.CodUbicacion=UbicaEspec.CodTipo;
               }
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
                GlobalVariables.Obserbacion.CodTipo=Tipo.CodTipo;
                GlobalVariables.ObserbacionDetalle.CodTipo=Tipo.CodTipo;
                if(Tipo.CodTipo.equals("TO04")) {
                    if(!StringUtils.isEmpty(GlobalVariables.Obserbacion.CodSubTipo))spinnerSubtipo.setSelection(GlobalVariables.indexOf(GlobalVariables.SubTipo_obs,GlobalVariables.Obserbacion.CodSubTipo));
                    else spinnerSubtipo.setSelection(0);
                    CarSubTipo.setVisibility(View.VISIBLE);
                }
                else {
                    CarSubTipo.setVisibility(View.GONE);
                }
                TabHost tabHost = (TabHost)getActivity().findViewById(android.R.id.tabhost);
                TabWidget widget = tabHost.getTabWidget();
                for(int i = 0; i < widget.getChildCount(); i++) {
                    if(i==2){
                        View v = widget.getChildAt(i);
                        if(Tipo.CodTipo.equals("TO01")||Tipo.CodTipo.equals("TO02")) v.setVisibility(View.GONE);
                        else  v.setVisibility(View.VISIBLE);
                    }
                }
                // reinicialize data Object  ObservaciobDetalle
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerSubtipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_subtipo) ).getSelectedItem();
                GlobalVariables.Obserbacion.CodSubTipo=Tipo.CodTipo;
                //GlobalVariables.ObserbacionDetalle.CodHHA=Tipo.CodTipo;
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

        btnSelectObservador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title="Nueva Observación/Observador";
                if(GlobalVariables.ObjectEditable)
                    title="Editar Observación/Observador";

                Intent intent = new Intent(getContext(), B_personas.class);
                intent.putExtra("title",title);
                startActivityForResult(intent , 1);
            }
        });

        return mView;
    }

    public void updateCodigo(String Codigotxt){
        Codigo.setText(Codigotxt);
    }
  public void setdata(){
      Date fecha=new Date();
      SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

      if(!StringUtils.isEmpty(GlobalVariables.Obserbacion.Fecha)){
          try {
              fecha = df.parse(GlobalVariables.Obserbacion.Fecha);
              botonEscogerFecha.setText(dt.format(fecha));
          } catch (ParseException e) {
              e.printStackTrace();
              botonEscogerFecha.setText("Seleccionar fecha");
          }
      }
      if(GlobalVariables.Obserbacion.CodObservacion!=null)Codigo.setText(GlobalVariables.Obserbacion.CodObservacion);
      if(GlobalVariables.Obserbacion.ObservadoPor!=null)txtObservado.setText(GlobalVariables.Obserbacion.ObservadoPor);
      if(GlobalVariables.Obserbacion.Lugar!=null)txtLugar.setText(GlobalVariables.Obserbacion.Lugar);

      if(!StringUtils.isEmpty(GlobalVariables.Obserbacion.CodTipo))spinnerTipoObs.setSelection(GlobalVariables.indexOf(GlobalVariables.Tipo_obs2,GlobalVariables.Obserbacion.CodTipo));
      if(!StringUtils.isEmpty(GlobalVariables.Obserbacion.CodAreaHSEC))spinnerArea.setSelection(GlobalVariables.indexOf(GlobalVariables.Area_obs,GlobalVariables.Obserbacion.CodAreaHSEC));
      if(!StringUtils.isEmpty(GlobalVariables.Obserbacion.CodNivelRiesgo))spinnerNivel.setSelection(GlobalVariables.indexOf(GlobalVariables.NivelRiesgo_obs,GlobalVariables.Obserbacion.CodNivelRiesgo));

      if(!StringUtils.isEmpty(GlobalVariables.Obserbacion.CodUbicacion)){
          Ubicacion=GlobalVariables.Obserbacion.CodUbicacion;
          String data[] = GlobalVariables.Obserbacion.CodUbicacion.split("\\.");
          spinnerUbica.setSelection(GlobalVariables.indexOf(GlobalVariables.Ubicacion_obs, data[0]));

      }
  }
    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        GlobalVariables.Obserbacion = gson.fromJson(data, ObservacionModel.class);
        GlobalVariables.StrObservacion = gson.toJson(GlobalVariables.Obserbacion);
        setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) { // seleccion de Observado por
                String name=data.getStringExtra("nombreP");
                txtObservado.setText(name);
                GlobalVariables.Obserbacion.ObservadoPor=name;
                GlobalVariables.Obserbacion.CodObservadoPor=data.getStringExtra("codpersona");
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}


