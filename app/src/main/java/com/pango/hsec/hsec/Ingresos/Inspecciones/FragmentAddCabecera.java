package com.pango.hsec.hsec.Ingresos.Inspecciones;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_contrata;
import com.pango.hsec.hsec.Busquedas.B_inspecciones;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.FragmentInspeccion;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.obs_cabecera;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class FragmentAddCabecera extends Fragment implements IActivity {

    ArrayList<Maestro> superintdata;
    TextView insp_contrata,edit_codigo;

    Button btnFechaInicio,btnFechaFin,btn_hora;

    Spinner spinnerUbicacion,spinnerSubUbicacion, spinnerGerencia,spinnerSuperInt,spinnerTipoInsp;

    public static final int REQUEST_CODE = 1;
    ImageButton btn_buscar_c;
    Calendar myCalendar,myCalendar2;
    DatePickerDialog.OnDateSetListener date, datefin;
    DialogFragment newFragment;
    String fecha_inicio="-";
    String fecha_fin="-";
    boolean escogioFecha;
    String fechaEscogida;
    String Ubicacion="";

    String Ubicacionfinal="", Gerenciafinal="";

    ///////////////////////////////
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    int hora = c.get(Calendar.HOUR_OF_DAY);
    int minuto = c.get(Calendar.MINUTE);


    public FragmentAddCabecera() {
        // Required empty public constructor
    }

    private View mView;
    String codInsp;
    // TODO: Rename and change types and number of parameters
    public static FragmentAddCabecera newInstance(String sampleText) {
        FragmentAddCabecera fragment = new FragmentAddCabecera();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_cabecera, container, false);
        codInsp=getArguments().getString("bString");

        boolean[] pass = {false,false},passGer={false};
        Integer[] itemSel = {0,0},itemSelGer={0};

        spinnerGerencia=(Spinner) mView.findViewById(R.id.spinner_gerencia);
        spinnerSuperInt=(Spinner) mView.findViewById(R.id.spinner_superint);
        spinnerUbicacion = (Spinner) mView.findViewById(R.id.spinner_ubicacion);
        spinnerSubUbicacion=(Spinner) mView.findViewById(R.id.spinner_sububicacion);
        spinnerTipoInsp=(Spinner) mView.findViewById(R.id.spinner_tipoinsp);

        btn_buscar_c=(ImageButton) mView.findViewById(R.id.btn_buscar_c);
        insp_contrata=(TextView) mView.findViewById(R.id.insp_contrata);

        btnFechaInicio=(Button) mView.findViewById(R.id.btn_fecha_prog);
        btnFechaFin=(Button) mView.findViewById(R.id.btn_fechainsp);
        btn_hora=(Button) mView.findViewById(R.id.btn_hora);
        edit_codigo=(TextView) mView.findViewById(R.id.edit_codigo);
        String SelecFecha="SELECCIONAR FECHA";
        //insp_contrata.setText(AddInspeccion.DesContrata);
        btnFechaInicio.setText(SelecFecha);
        btnFechaFin.setText(SelecFecha);
        btn_hora.setText("SELECCIONAR HORA");

        ArrayAdapter adapterGerencia = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.Gerencia);
        adapterGerencia.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

        superintdata=new ArrayList<>();
        superintdata.add(new Maestro("","-  Seleccione  -"));
        ArrayAdapter adapterSuperInt = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerSuperInt.setAdapter(adapterSuperInt);

        //ubicaciones
        GlobalVariables.reloadUbicacion();
        ArrayAdapter adapterUbic = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.Ubicacion_obs);
        adapterUbic.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerUbicacion.setAdapter(adapterUbic);

        ArrayAdapter adapterSubUbic = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.SubUbicacion_obs);
        adapterSubUbic.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerSubUbicacion.setAdapter(adapterSubUbic);


        ArrayAdapter adapterTipoInsp = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.Tipo_insp);
        adapterTipoInsp.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerTipoInsp.setAdapter(adapterTipoInsp);


        if(GlobalVariables.ObjectEditable){ // load data of server
            if(GlobalVariables.AddInspeccion.CodInspeccion==null) // || !GlobalVariables.AddInspeccion.CodInspeccion.equals(codInsp)
            {
                String url= GlobalVariables.Url_base+"Inspecciones/Get/"+codInsp;
                ActivityController obj = new ActivityController("get", url, FragmentAddCabecera.this,getActivity());
                obj.execute("");
            }
            else setdata();
        }
        else // new inspeccion
        {
            if(GlobalVariables.AddInspeccion.CodInspeccion==null){
                pass[0]=true;
                pass[1]=true;
                passGer[0]=true;
                GlobalVariables.AddInspeccion= new InspeccionModel();
                GlobalVariables.AddInspeccion.CodInspeccion=codInsp;//saber si es nuevo
            }
            //else if(!GlobalVariables.AddInspeccion.CodInspeccion.contains("XYZ"))
             else   setdata();
        }

        spinnerGerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(itemSelGer[0]!=position) {
                    itemSelGer[0]=position;
                    Maestro ubica = (Maestro) ((Spinner) mView.findViewById(R.id.spinner_gerencia)).getSelectedItem();
                    Gerenciafinal = ubica.CodTipo;
                    superintdata.clear();
                    for (Maestro item : GlobalVariables.loadSuperInt(Gerenciafinal)) {
                        superintdata.add(item);
                    }
                    adapterSuperInt.notifyDataSetChanged();
                    if(!passGer[0])
                    {
                        passGer[0] =true;
                        if(!StringUtils.isEmpty(GlobalVariables.AddInspeccion.SuperInt))
                            spinnerSuperInt.setSelection(GlobalVariables.indexOf(superintdata,Gerenciafinal+"."+GlobalVariables.AddInspeccion.SuperInt));
                    }
                    else {
                        GlobalVariables.AddInspeccion.Gerencia=Gerenciafinal;
                        spinnerSuperInt.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //gerencia="";
            }
        });

        spinnerSuperInt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_superint) ).getSelectedItem();
                if(!StringUtils.isEmpty(Tipo.CodTipo))
                GlobalVariables.AddInspeccion.SuperInt=Tipo.CodTipo.split("\\.")[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // superint="";
            }
        });

        spinnerUbicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(itemSel[0]!=position) {
                    itemSel[0]=position;
                    Maestro ubica = (Maestro) ((Spinner) mView.findViewById(R.id.spinner_ubicacion)).getSelectedItem();
                    Ubicacionfinal = ubica.CodTipo;
                    GlobalVariables.SubUbicacion_obs.clear();
                    for (Maestro item : GlobalVariables.loadUbicacion(Ubicacionfinal, 2) ) {
                        GlobalVariables.SubUbicacion_obs.add(item);
                    }
                    adapterSubUbic.notifyDataSetChanged();
                    if(!pass[0])
                    {
                        pass[0] =true;
                        if(!StringUtils.isEmpty(GlobalVariables.AddInspeccion.CodSubUbicacion))spinnerSubUbicacion.setSelection(GlobalVariables.indexOf(GlobalVariables.SubUbicacion_obs,GlobalVariables.AddInspeccion.CodSubUbicacion));
                    }
                    else {
                        GlobalVariables.AddInspeccion.CodUbicacion=Ubicacionfinal;
                        spinnerSubUbicacion.setSelection(0);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //GlobalVariables.AddInspeccion.CodUbicacion = "";
                Ubicacionfinal = "";
            }
        });

        spinnerSubUbicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(itemSel[1]!=position) {
                    itemSel[1]=position;
                    Maestro Sububica = (Maestro) ((Spinner) mView.findViewById(R.id.spinner_sububicacion)).getSelectedItem();
                    Ubicacionfinal = Sububica.CodTipo;
                    GlobalVariables.UbicacionEspecifica_obs.clear();
                    for (Maestro item : GlobalVariables.loadUbicacion(Ubicacionfinal, 3)) {
                        GlobalVariables.UbicacionEspecifica_obs.add(item);
                    }
                        GlobalVariables.AddInspeccion.CodSubUbicacion=Ubicacionfinal;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Ubicacionfinal = Ubicacionfinal.split("\\.")[0];
                GlobalVariables.AddInspeccion.CodSubUbicacion="";
            }
        });

        spinnerTipoInsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_tipoinsp) ).getSelectedItem();
                GlobalVariables.AddInspeccion.CodTipo=Tipo.CodTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //gerencia="";
            }
        });

        /////////////Fechas//////////////////////////////////////////////////////
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date actual = myCalendar.getTime();

                SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
                SimpleDateFormat fecha_envio = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                //Utils.inspeccionModel.FechaP= String.valueOf(fecha_envio.format(actual));
                GlobalVariables.AddInspeccion.FechaP=String.valueOf(fecha_envio.format(actual));

                fecha_inicio=dt.format(actual);
                btnFechaInicio.setText(dt.format(actual));

                // btnFechaFin.setText(dt.format(actual));
                // fecha_inicio=dt.format(actual);
                escogioFecha = true;
                dt = new SimpleDateFormat("yyyyMMdd");
                fechaEscogida = dt.format(actual);

            }

        };
        myCalendar2 = Calendar.getInstance();
        datefin= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                //ultima_fecha=false;
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date actual = myCalendar2.getTime();
                //Utils.observacionModel.Fecha_fin= String.valueOf(actual);

                SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
                //btnFechaInicio.setText(dt.format(actual));

                SimpleDateFormat fecha_envio = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String FechaEnvio="";
                FechaEnvio= fecha_envio.format(actual);
                if(GlobalVariables.AddInspeccion.Fecha==null)
                    btn_hora.setText("00:00:00");
                else{
                    String [] hora=GlobalVariables.AddInspeccion.Fecha.split("T");
                    FechaEnvio=FechaEnvio.split("T")[0]+"T"+hora[1];
                }
                //Utils.inspeccionModel.Fecha= String.valueOf(fecha_envio.format(actual));
                GlobalVariables.AddInspeccion.Fecha=FechaEnvio;
                fecha_fin=dt.format(actual);
                btnFechaFin.setText(dt.format(actual));
                //fecha_fin=dt.format(actual);
                escogioFecha = true;
                dt = new SimpleDateFormat("yyyyMMdd");
                fechaEscogida = dt.format(actual);
            }
        };

        btn_buscar_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), B_contrata.class);
                startActivityForResult(intent , REQUEST_CODE);

            }
        });

        ////////////////////botones para escoger fecha///////////////////////////////////////

        btnFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FragmentAddCabecera.this.getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.set(Calendar.DAY_OF_MONTH,1);
                tempCalendar.set(Calendar.HOUR, 0);
                tempCalendar.set(Calendar.MINUTE, 0);
                tempCalendar.set(Calendar.SECOND, 0);
                tempCalendar.set(Calendar.MILLISECOND, 0);
/*
                datePickerDialog.getDatePicker().setMinDate(tempCalendar.getTimeInMillis());
                //tempCalendar.set(Calendar.MONTH, (new Date()).getMonth() + 1);
                datePickerDialog.getDatePicker().setMaxDate(tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
*/

                datePickerDialog.getDatePicker().setMinDate(tempCalendar.getTimeInMillis());
                tempCalendar.set(Calendar.DAY_OF_MONTH,tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                tempCalendar.set(Calendar.MONTH, (new Date()).getMonth());
                datePickerDialog.getDatePicker().setMaxDate(tempCalendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });

        btnFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), datefin, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH));
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.set(Calendar.DAY_OF_MONTH,1);
                tempCalendar.set(Calendar.HOUR, 0);
                tempCalendar.set(Calendar.MINUTE, 0);
                tempCalendar.set(Calendar.SECOND, 0);
                tempCalendar.set(Calendar.MILLISECOND, 0);
/*
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                //tempCalendar.set(Calendar.MONTH, (new Date()).getMonth() + 1);
                datePickerDialog.getDatePicker().setMaxDate(tempCalendar.getTimeInMillis());
                datePickerDialog.show();
*/

                datePickerDialog.getDatePicker().setMinDate(tempCalendar.getTimeInMillis());
                tempCalendar.set(Calendar.DAY_OF_MONTH,tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                tempCalendar.set(Calendar.MONTH, (new Date()).getMonth());
                datePickerDialog.getDatePicker().setMaxDate(tempCalendar.getTimeInMillis());
                datePickerDialog.show();


            }
        });

        btn_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalVariables.AddInspeccion.Fecha!=null){
                    TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            //Formateo el hora obtenido: antepone el 0 si son menores de 10
                            String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                            //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                            //String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                            //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                            String AM_PM;
                            int horaFinal;
                            int minutoFinal;
                            //GlobalVariables.AddInspeccion.Fecha=GlobalVariables.AddInspeccion.Fecha+"'"+"T"+"'"+String.valueOf(hourOfDay)+":"+minute;

                            if(hourOfDay < 12) {
                                AM_PM = "a.m.";
                                horaFinal=hourOfDay;
                                minutoFinal=minute;
                            } else {
                                AM_PM = "p.m.";
                                horaFinal=hourOfDay-12;
                                minutoFinal=minute;
                            }
                            //Muestro la hora con el formato deseado
                            String minutoFormateado = (minutoFinal < 10)? String.valueOf(CERO + minutoFinal):String.valueOf(minutoFinal);

                            String [] fecha=GlobalVariables.AddInspeccion.Fecha.split("T");
                            GlobalVariables.AddInspeccion.Fecha=fecha[0]+"T"+hourOfDay+":"+minutoFormateado+":00";

                            //AddInspeccion.hora=horaFinal + DOS_PUNTOS + minutoFormateado + " " + AM_PM;
                            btn_hora.setText(horaFinal + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                            hora=hourOfDay;
                            minuto=minute;

                        }
                        //Estos valores deben ir en ese orden
                        //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                        //Pero el sistema devuelve la hora en formato 24 horas
                    }, hora, minuto, false);
                    recogerHora.show();
                }
            }
        });

        return mView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                String tipo_dato=data.getStringExtra("tipo");

                String cod_contrata = data.getStringExtra("codContrata");
                String des_contrata = data.getStringExtra("desContrata");
                insp_contrata.setText(des_contrata);

                GlobalVariables.AddInspeccion.CodContrata=cod_contrata;
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void success(String data, String Tipo) {

        Gson gson = new Gson();
        GlobalVariables.AddInspeccion = gson.fromJson(data, InspeccionModel.class);
        GlobalVariables.StrInspeccion= gson.toJson(GlobalVariables.AddInspeccion);
        setdata();

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    public void updateCodigo(String Codigotxt){
        edit_codigo.setText(Codigotxt);
    }

    public void setdata(){

        Date fecha=new Date();
        Date fecha2=new Date();

        SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dh = new SimpleDateFormat("h:mm a");


            if(!StringUtils.isEmpty(GlobalVariables.AddInspeccion.FechaP)){
                try {
                fecha = df.parse(GlobalVariables.AddInspeccion.FechaP);
                btnFechaInicio.setText(dt.format(fecha));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(!StringUtils.isEmpty(GlobalVariables.AddInspeccion.Fecha)) {
                try {
                fecha2 = df.parse(GlobalVariables.AddInspeccion.Fecha);
                btnFechaFin.setText(dt.format(fecha2));
                btn_hora.setText(dh.format(fecha2));
                } catch (ParseException e) {
                e.printStackTrace();
                }
            }


        edit_codigo.setText(GlobalVariables.AddInspeccion.CodInspeccion);
        if(!StringUtils.isEmpty(GlobalVariables.AddInspeccion.CodContrata)) insp_contrata.setText(GlobalVariables.getDescripcion(GlobalVariables.Contrata,GlobalVariables.AddInspeccion.CodContrata));

        if(!StringUtils.isEmpty(GlobalVariables.AddInspeccion.CodUbicacion))spinnerUbicacion.setSelection(GlobalVariables.indexOf(GlobalVariables.Ubicacion_obs, GlobalVariables.AddInspeccion.CodUbicacion));

        if(!StringUtils.isEmpty(GlobalVariables.AddInspeccion.CodTipo)) spinnerTipoInsp.setSelection(GlobalVariables.indexOf(GlobalVariables.Tipo_insp,GlobalVariables.AddInspeccion.CodTipo));
        if(!StringUtils.isEmpty(GlobalVariables.AddInspeccion.Gerencia))spinnerGerencia.setSelection(GlobalVariables.indexOf(GlobalVariables.Gerencia,GlobalVariables.AddInspeccion.Gerencia));

    }
}






