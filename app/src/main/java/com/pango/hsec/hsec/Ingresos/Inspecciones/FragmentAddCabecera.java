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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class FragmentAddCabecera extends Fragment implements IActivity {
    //InspeccionModel AddInspeccion;

    ArrayList<Maestro> gerenciadata;
    ArrayList<Maestro> superintdata;
    //ArrayList<Maestro> ubicaciondata;
    //ArrayList<Maestro> sububicacion_data;
    ArrayList<Maestro> Tipo_insp_data;
    TextView insp_contrata,edit_codigo;

    Button btnFechaInicio,btnFechaFin,btn_hora;
    String gerencia_pos="0";
    String superint_pos="0";
    String ubic_pos="0";
    String sububic_pos="0";
    String tipoInsp_pos="0";

    Spinner spinnerUbicacion,spinnerSubUbicacion, spinnerGerencia,spinnerSuperInt,spinnerTipoInsp;
    ArrayAdapter adapterSuperInt;
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

    String Ubicacionfinal="",TipoObs;
    String Gerenciafinal="";
    final boolean[] pass1 = {false};
    final boolean[] pass2 = {false};

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
        final Date opentab= new Date();

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



        //AddInspeccion.DesContrata=des_contrata;
        insp_contrata.setText(AddInspeccion.DesContrata);
        btnFechaInicio.setText(AddInspeccion.fecha_1);
        btnFechaFin.setText(AddInspeccion.fecha_2);
        btn_hora.setText(AddInspeccion.hora);


        gerenciadata= new ArrayList<>();
        gerenciadata.add(new Maestro(null,"-  Seleccione  -"));
        gerenciadata.addAll(GlobalVariables.Gerencia);
        //GlobalVariables.Gerencia=new ArrayList<>();
        //GlobalVariables.Gerencia=gerenciadata;

        superintdata=new ArrayList<>();
        //superintdata.addAll(GlobalVariables.SuperIntendencia);
        superintdata.add(new Maestro(null,"-  Seleccione  -"));

        //ubicaciondata= new ArrayList<>();
        //ubicaciondata.add(new Maestro(null,"-  Seleccione  -"));
        //ubicaciondata.addAll(GlobalVariables.Ubicacion_obs);

        //sububicacion_data= new ArrayList<>();
        //sububicacion_data.add(new Maestro(null,"-  Seleccione  -"));
        //sububicacion_data.addAll(GlobalVariables.SubUbicacion_obs);


        Tipo_insp_data= new ArrayList<>();
        Tipo_insp_data.add(new Maestro(null,"-  Seleccione  -"));
        Tipo_insp_data.addAll(GlobalVariables.Tipo_insp);

        ArrayAdapter adapterGerencia = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, gerenciadata);
        adapterGerencia.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

        adapterSuperInt = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSuperInt.setAdapter(adapterSuperInt);

        ArrayAdapter adapterUbic = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, GlobalVariables.Ubicacion_obs);
        adapterUbic.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerUbicacion.setAdapter(adapterUbic);

        final ArrayAdapter adapterSubUbic = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, GlobalVariables.SubUbicacion_obs);
        adapterSubUbic.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSubUbicacion.setAdapter(adapterSubUbic);


        ArrayAdapter adapterTipoInsp = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Tipo_insp_data);
        adapterTipoInsp.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTipoInsp.setAdapter(adapterTipoInsp);


        if(GlobalVariables.ObjectEditable){ // load data of server
            if(GlobalVariables.AddInspeccion.CodInspeccion==null || !GlobalVariables.AddInspeccion.CodInspeccion.equals(codInsp))
            {
                String url= GlobalVariables.Url_base+"Inspecciones/Get/"+codInsp;
                ActivityController obj = new ActivityController("get", url, FragmentAddCabecera.this,getActivity());
                obj.execute("");
            }
            else setdata();
        }
        else // new inspeccion
        {
            if(GlobalVariables.AddInspeccion.CodInspeccion==null||!GlobalVariables.AddInspeccion.CodInspeccion.contains("XYZ")){

                //myCalendar = Calendar.getInstance();
                //fecha = myCalendar.getTime();
                GlobalVariables.AddInspeccion= new InspeccionModel();
                GlobalVariables.AddInspeccion.CodInspeccion="-1";//saber si es nuevo


/*
                GlobalVariables.Inspeccion.CodInspeccion= codInsp;
                GlobalVariables.Inspeccion.Fecha="";
                GlobalVariables.Inspeccion.Lugar="";
                GlobalVariables.Inspeccion.CodAreaHSEC=GlobalVariables.Area_obs.get(0).CodTipo;
                GlobalVariables.Inspeccion.CodNivelRiesgo=GlobalVariables.NivelRiesgo_obs.get(0).CodTipo;
                GlobalVariables.Inspeccion.CodTipo=GlobalVariables.Tipo_obs.get(0).CodTipo;
                GlobalVariables.Inspeccion.CodUbicacion="";
                GlobalVariables.Inspeccion.CodSubEstandar=GlobalVariables.Acto_obs.get(0).CodTipo;
                */
            }
            //  else if(!GlobalVariables.Obserbacion.CodObservacion.contains("XYZ"))

            //setdata();
        }



        spinnerGerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /*
                if(position!=0) {
                    GlobalVariables.AddInspeccion.CodTipo=Tipo_insp_data.get(position).CodTipo;
                    tipoInsp_pos = String.valueOf(position);
                }else{
                    tipoInsp_pos = String.valueOf(position);
                    //Utils.inspeccionModel.Gerencia=null;
                    GlobalVariables.AddInspeccion.CodTipo="";
                }
                */

                Maestro gerencia = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_gerencia) ).getSelectedItem();
                Gerenciafinal=gerencia.CodTipo;

                if(position!=0) {
               /* Maestro Geren = (Maestro) ( (Spinner) view.findViewById(R.id.spinner_gerencia) ).getSelectedItem();
                gerencia=Geren.CodTipo;*/
                    //superint=null;
                    //Gerenciafinal = gerenciadata.get(position).CodTipo;
                    GlobalVariables.AddInspeccion.Gerencia= Gerenciafinal;
                    gerencia_pos = String.valueOf(position);
                    spinnerGerencia.setSelection(position);
                    superintdata.clear();
                    for (Maestro item : GlobalVariables.loadSuperInt(Gerenciafinal)
                            ) {
                        superintdata.add(item);
                    }
                    adapterSuperInt.notifyDataSetChanged();
                    //spinnerSuperInt.setSelection(Integer.parseInt(superint_pos));

                }else{
                    spinnerGerencia.setSelection(0);
                    spinnerSuperInt.setSelection(0);
                    gerencia_pos = String.valueOf(position);
                    GlobalVariables.AddInspeccion.Gerencia=null;
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

                //Maestro SuperInt = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_superint) ).getSelectedItem();
                //Gerenciafinal=SuperInt.CodTipo;
                /*GlobalVariables.UbicacionEspecifica_obs.clear();
                for (Maestro item: GlobalVariables.loadUbicacion(Ubicacionfinal,3)) {
                    GlobalVariables.UbicacionEspecifica_obs.add(item);
                }*/
                //adapterUbicEspc.notifyDataSetChanged();

                GlobalVariables.AddInspeccion.SuperInt=superintdata.get(position).CodTipo;


                String superint;
                if(position!=0) {
                    superint = superintdata.get(position).CodTipo.split("\\.")[1];
                    GlobalVariables.AddInspeccion.SuperInt=superintdata.get(position).CodTipo.split("\\.")[1];
                    superint_pos=String.valueOf(position);
                    spinnerSuperInt.setSelection(position);

                }else{
                    spinnerSuperInt.setSelection(0);
                    superint="";
                    superint_pos="0";
                }





            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // superint="";
            }
        });


        spinnerUbicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro ubica = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_ubicacion) ).getSelectedItem();
                Ubicacionfinal=ubica.CodTipo;
                //String ubicacion=ubicaciondata.get(position).CodTipo;
                //Utils.inspeccionModel.CodUbicacion=ubicaciondata.get(position).CodTipo;
                GlobalVariables.SubUbicacion_obs.clear();
                //GlobalVariables.AddInspeccion.CodUbicacion=ubicaciondata.get(position).CodTipo;

                for (Maestro item: GlobalVariables.loadUbicacion(Ubicacionfinal,2)
                        ) {
                    GlobalVariables.SubUbicacion_obs.add(item);
                }
                adapterSubUbic.notifyDataSetChanged();
                //spinnerSubUbicacion.setSelection(GlobalVariables.indexOf(sububicacion_data,sububic_pos));

                if(!pass1[0]){
                    Date openSpinner=new Date();
                    long diff=openSpinner.getTime()-opentab.getTime();
                    if(diff>2000)
                    {
                        //GlobalVariables.Obserbacion.CodUbicacion=Ubicacionfinal;
                        GlobalVariables.AddInspeccion.CodUbicacion=Ubicacionfinal;
                        spinnerSubUbicacion.setSelection(0);
                    }
                }
                else {
                    pass1[0] =false;
                    String data[]= Ubicacion.split("\\.");
                    if(data.length>1)
                    {
                        //pass2[0] =true;
                        spinnerSubUbicacion.setSelection(GlobalVariables.indexOf(GlobalVariables.SubUbicacion_obs,data[0]+"."+data[1]));
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
                //String sububic;
                //spinnerSubUbicacion=(Spinner) mView.findViewById(R.id.spinner_sububicacion);
                //Maestro Sububica = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_sububicacion) ).getSelectedItem();

                Maestro Sububica = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_sububicacion) ).getSelectedItem();
                Ubicacionfinal=Sububica.CodTipo;
                GlobalVariables.UbicacionEspecifica_obs.clear();
                for (Maestro item: GlobalVariables.loadUbicacion(Ubicacionfinal,3)) {
                    GlobalVariables.UbicacionEspecifica_obs.add(item);
                }
                //adapterUbicEspc.notifyDataSetChanged();
                if(!pass2[0]){
                    Date openSpinner=new Date();
                    long diff=openSpinner.getTime()-opentab.getTime();
                    if(diff>2000){
                        GlobalVariables.AddInspeccion.CodUbicacion=Ubicacionfinal;
                    }
                }
                else {
                    pass2[0] =false;
                   /* if(Ubicacion.split("\\.").length==3)
                        spinnerUbicEspec.setSelection(GlobalVariables.indexOf(GlobalVariables.UbicacionEspecifica_obs,Ubicacion));
                    else spinnerUbicEspec.setSelection(0);*/
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
                if(position!=0) {
               /* Maestro Geren = (Maestro) ( (Spinner) view.findViewById(R.id.spinner_gerencia) ).getSelectedItem();
                gerencia=Geren.CodTipo;*/
                    //superint=null;
                    //Utils.inspeccionModel.Gerencia = gerenciadata.get(position).CodTipo;
                    GlobalVariables.AddInspeccion.CodTipo=Tipo_insp_data.get(position).CodTipo;
                    tipoInsp_pos = String.valueOf(position);


                }else{
                    tipoInsp_pos = String.valueOf(position);
                    //Utils.inspeccionModel.Gerencia=null;
                    GlobalVariables.AddInspeccion.CodTipo="";
                }

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
                SimpleDateFormat fecha_envio = new SimpleDateFormat("yyyy-MM-dd");
                //Utils.inspeccionModel.FechaP= String.valueOf(fecha_envio.format(actual));
                GlobalVariables.AddInspeccion.FechaP=String.valueOf(fecha_envio.format(actual));

                fecha_inicio=dt.format(actual);
                //btnFechaInicio.setText(dt.format(actual));
                AddInspeccion.fecha_1=dt.format(actual);
                btnFechaInicio.setText(AddInspeccion.fecha_1);

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

                SimpleDateFormat fecha_envio = new SimpleDateFormat("yyyy-MM-dd");

                //Utils.inspeccionModel.Fecha= String.valueOf(fecha_envio.format(actual));
                GlobalVariables.AddInspeccion.Fecha=String.valueOf(fecha_envio.format(actual));
                fecha_fin=dt.format(actual);
                //btnFechaFin.setText(dt.format(actual));
                AddInspeccion.fecha_2=dt.format(actual);

                btnFechaFin.setText(AddInspeccion.fecha_2);

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
                        AddInspeccion.hora=hourOfDay+":"+minutoFormateado+":00";

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
                AddInspeccion.DesContrata=des_contrata;
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
        setdata();

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }



    public void setdata(){

        Date fecha=new Date();
        Date fecha2=new Date();

        SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dh = new SimpleDateFormat("h:mm a");


        try {
            fecha = df.parse(GlobalVariables.AddInspeccion.FechaP);
            fecha2=df.parse(GlobalVariables.AddInspeccion.Fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnFechaInicio.setText(dt.format(fecha));
        btnFechaFin.setText(dt.format(fecha2));
        btn_hora.setText(dh.format(fecha2));

        edit_codigo.setText(GlobalVariables.AddInspeccion.CodInspeccion);
        spinnerGerencia.setSelection(GlobalVariables.indexOf(GlobalVariables.Gerencia,GlobalVariables.AddInspeccion.Gerencia)+1);
        int a=GlobalVariables.indexOf(GlobalVariables.Gerencia,GlobalVariables.AddInspeccion.Gerencia);
        superintdata.clear();
        for (Maestro item : GlobalVariables.loadSuperInt(GlobalVariables.AddInspeccion.Gerencia)
                ) {
            superintdata.add(item);
        }
        int b=GlobalVariables.indexOf(superintdata,GlobalVariables.AddInspeccion.Gerencia+"."+GlobalVariables.AddInspeccion.SuperInt);
        spinnerSuperInt.setSelection(GlobalVariables.indexOf(superintdata,GlobalVariables.AddInspeccion.Gerencia+"."+GlobalVariables.AddInspeccion.SuperInt));


        insp_contrata.setText(GlobalVariables.AddInspeccion.CodContrata);
        //insp_contrata.setText("xxxxxxxxxxxxx");
        if(GlobalVariables.AddInspeccion.CodUbicacion!=null&&!GlobalVariables.AddInspeccion.CodUbicacion.isEmpty()){
            String data[]=GlobalVariables.AddInspeccion.CodUbicacion.split("\\.");
            spinnerUbicacion.setSelection(GlobalVariables.indexOf(GlobalVariables.Ubicacion_obs,data[0]));
            Ubicacion=GlobalVariables.AddInspeccion.CodUbicacion;
            pass1[0] =true;
        }

        spinnerTipoInsp.setSelection(GlobalVariables.indexOf(GlobalVariables.Tipo_insp,GlobalVariables.AddInspeccion.CodTipo)+1);

        //spinnerSubUbicacion.setSelection(GlobalVariables.indexOf(GlobalVariables.Ubicacion_obs,GlobalVariables.Inspeccion.CodUbicacion));



        //spinnerSubUbicacion.setSelection(GlobalVariables.indexOf(GlobalVariables.SubUbicacion_obs,GlobalVariables.Inspeccion.CodUbicacion));

        //GlobalVariables.AddInspeccion.CodTipo=Tipo_insp_data.get(position).CodTipo;
        //tipoInsp_pos = String.valueOf(position);



        //spinnerUbicacion = (Spinner) mView.findViewById(R.id.spinner_ubicacion);
        //spinnerSubUbicacion=(Spinner) mView.findViewById(R.id.spinner_sububicacion);
        //spinnerTipoInsp=(Spinner) mView.findViewById(R.id.spinner_tipoinsp);



/*
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
      */

    }



}






