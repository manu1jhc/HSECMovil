package com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.IngresosMA;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.Html;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.Maestro;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddCabeceraMA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddCabeceraMA extends Fragment implements IActivity {
    String codIncMA;
    ArrayList<Maestro> superintdata;
    public ArrayAdapter adapterUbicEspc,adapterSubN;
    String Ubicacionfinal="", Gerenciafinal="",  Ubicacion="", GrupoRiesgoFinal;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;
    ArrayList<Maestro> riesgoData;

    String fecha="-";
    boolean escogioFecha;
    String fechaEscogida;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    int hora = c.get(Calendar.HOUR_OF_DAY);
    int minuto = c.get(Calendar.MINUTE);


    private View mView;
    TextView tx_reportado;
    Spinner spinner_area, spinner_tipo, spinner_gerencia, spinner_superint, spinner_clasr,spinner_clasp, spinner_act_rel, spinner_grupo_riesgo, spinner_riesgo, spinner_ubicacion, spinner_sububicacion, spinner_ubic_esp;
    Button btn_fecha, btn_hora;
    EditText edit_lugar;
    ImageButton btn_buscar_r;
    TextView tx_codigo, tx_tipo,tx_rep,tx_gerencia,tx_clasr,tx_act_rel,tx_grupo_riesgo, tx_riesgo,tx_fecha, tx_hora,tx_ubicacion;


    public FragmentAddCabeceraMA() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentAddCabeceraMA.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddCabeceraMA newInstance(String sampleText) {
        FragmentAddCabeceraMA fragment = new FragmentAddCabeceraMA();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_cabecera_ma, container, false);
        codIncMA=getArguments().getString("bString");
        Gson gson = new Gson();
        boolean[] pass = {false,false},passGer={false};
        Integer[] itemSel = {0,0},itemSelGer={0};
        boolean[] pass_GR_R = {false,false},passGR={false};
        Integer[] itemSel_GR_R = {0,0}, itemSelGR={0};

        tx_tipo=mView.findViewById(R.id.tx_tipo);
        tx_tipo.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Tipo:"));
        tx_rep=mView.findViewById(R.id.tx_rep);
        tx_rep.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Reportado Por:"));
        tx_gerencia=mView.findViewById(R.id.tx_gerencia);
        tx_gerencia.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Gerencia:"));
        tx_clasr=mView.findViewById(R.id.tx_clasr);
        tx_clasr.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Clasificación Real:"));
        tx_act_rel=mView.findViewById(R.id.tx_act_rel);
        tx_act_rel.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Actividad Relacionada:"));
        tx_grupo_riesgo=mView.findViewById(R.id.tx_grupo_riesgo);
        tx_grupo_riesgo.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Grupo de Riesgo:"));
        tx_riesgo=mView.findViewById(R.id.tx_riesgo);
        tx_riesgo.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Riesgo:"));
        tx_fecha=mView.findViewById(R.id.tx_fecha);
        tx_fecha.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Fecha:"));
        tx_hora=mView.findViewById(R.id.tx_hora);
        tx_hora.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Hora:"));
        tx_ubicacion=mView.findViewById(R.id.tx_ubicacion);
        tx_ubicacion.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Ubicación:"));

        spinner_area=(Spinner) mView.findViewById(R.id.spinner_area);
        spinner_tipo=(Spinner) mView.findViewById(R.id.spinner_tipo);
        spinner_gerencia=(Spinner) mView.findViewById(R.id.spinner_gerencia);
        spinner_superint=(Spinner) mView.findViewById(R.id.spinner_superint);
        spinner_clasr=(Spinner) mView.findViewById(R.id.spinner_clasr);
        spinner_clasp=(Spinner) mView.findViewById(R.id.spinner_clasp);
        spinner_act_rel = (Spinner) mView.findViewById(R.id.spinner_act_rel);
        spinner_grupo_riesgo=(Spinner) mView.findViewById(R.id.spinner_grupo_riesgo);
        spinner_riesgo=(Spinner) mView.findViewById(R.id.spinner_riesgo);
        spinner_ubicacion=(Spinner) mView.findViewById(R.id.spinner_ubicacion);
        spinner_sububicacion=(Spinner) mView.findViewById(R.id.spinner_sububicacion);
        spinner_ubic_esp=(Spinner) mView.findViewById(R.id.spinner_ubic_esp);

        btn_buscar_r=(ImageButton) mView.findViewById(R.id.btn_buscar_r);

        tx_reportado = mView.findViewById(R.id.tx_reportado);
        btn_fecha = mView.findViewById(R.id.btn_fecha);
        btn_hora = mView.findViewById(R.id.btn_hora);
        tx_codigo = mView.findViewById(R.id.tx_codigo);
        edit_lugar = mView.findViewById(R.id.edit_lugar);

        btn_fecha.setText("SELECCIONAR FECHA");
        btn_hora.setText("SELECCIONAR HORA");
        spinner_area.setEnabled(false);
        spinner_tipo.setEnabled(false);
        ArrayAdapter adapterGerencia = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.Gerencia);
        adapterGerencia.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_gerencia.setAdapter(adapterGerencia);

        superintdata=new ArrayList<>();
        superintdata.add(new Maestro("","-  Seleccione  -"));
        ArrayAdapter adapterSuperInt = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_superint.setAdapter(adapterSuperInt);

        //Ubicaciones
        GlobalVariables.reloadUbicacion();
        ArrayAdapter adapterUbic = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Ubicacion_obs);
        adapterUbic.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_ubicacion.setAdapter(adapterUbic);

        adapterSubN = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.SubUbicacion_obs);
        adapterSubN.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_sububicacion.setAdapter(adapterSubN);

        adapterUbicEspc = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.UbicacionEspecifica_obs);
        adapterUbicEspc.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_ubic_esp.setAdapter(adapterUbicEspc);

        ArrayAdapter adapterArea = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.Area_obs);
        adapterArea.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_area.setAdapter(adapterArea);
        spinner_area.setSelection(3);
        ArrayAdapter adapterTipo = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.Tipo_Inc);
        adapterTipo.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterTipo);
        spinner_tipo.setSelection(1);
        //falta subtipo

        ArrayAdapter adapterClasReal = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.ClasReal);
        adapterClasReal.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_clasr.setAdapter(adapterClasReal);

        ArrayAdapter adapterClasPotencial = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.ClasPotencial);
        adapterClasPotencial.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_clasp.setAdapter(adapterClasPotencial);

        ArrayAdapter adapterGRiesgo = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.GrupRiesgo);
        adapterGRiesgo.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_grupo_riesgo.setAdapter(adapterGRiesgo);

        riesgoData=new ArrayList<>();
        riesgoData.add(new Maestro("","-  Seleccione  -"));
        ArrayAdapter adapterRiesgo = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, riesgoData);
        adapterRiesgo.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_riesgo.setAdapter(adapterRiesgo);


        ArrayAdapter adapterActRel = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.Actividad_obs);
        adapterActRel.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_act_rel.setAdapter(adapterActRel);

//        if(GlobalVariables.ObjectEditable){ // load data of server
//            if(GlobalVariables.AddInspeccion.CodInspeccion==null) // || !GlobalVariables.AddInspeccion.CodInspeccion.equals(codInsp)
//            {
//                String url= GlobalVariables.Url_base+"Inspecciones/Get/"+codInsp;
//                ActivityController obj = new ActivityController("get", url, FragmentAddCabecera.this,getActivity());
//                obj.execute("");
//            }
//            else setdata();
//        }
//        else // new inspeccion
//        {
//            if(GlobalVariables.AddInspeccion.CodInspeccion==null){
//                pass[0]=true;
//                pass[1]=true;
//                passGer[0]=true;
//                GlobalVariables.AddInspeccion= new InspeccionModel();
//                GlobalVariables.AddInspeccion.CodInspeccion=codInsp;//saber si es nuevo
//                GlobalVariables.StrInspeccion=gson.toJson(GlobalVariables.AddInspeccion);
//            }
//            //else if(!GlobalVariables.AddInspeccion.CodInspeccion.contains("XYZ"))
//            else   setdata();
//        }

        spinner_gerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    if(!passGer[0]&&GlobalVariables.AddIncidenteSeg.Gerencia!=null)
                    {
                        passGer[0] =true;
                        if(!StringUtils.isEmpty(GlobalVariables.AddIncidenteSeg.SuperInt))
                            spinner_superint.setSelection(GlobalVariables.indexOf(superintdata,Gerenciafinal+"."+GlobalVariables.AddIncidenteSeg.SuperInt));
                    }
                    else {
                        GlobalVariables.AddIncidenteSeg.Gerencia=Gerenciafinal;
                        spinner_superint.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //gerencia="";
            }
        });

        spinner_superint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_superint) ).getSelectedItem();
                if(!StringUtils.isEmpty(Tipo.CodTipo))
                    GlobalVariables.AddIncidenteSeg.SuperInt=Tipo.CodTipo.split("\\.")[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // superint="";
            }
        });

        spinner_ubicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(itemSel[0]!=position) {
                    itemSel[0]=position;
                    Maestro ubica = (Maestro) ((Spinner) mView.findViewById(R.id.spinner_ubicacion)).getSelectedItem();
                    Ubicacionfinal = ubica.CodTipo;
                    GlobalVariables.SubUbicacion_obs.clear();
                    for (Maestro item : GlobalVariables.loadUbicacion(Ubicacionfinal, 2)) {
                        GlobalVariables.SubUbicacion_obs.add(item);
                    }
                    adapterSubN.notifyDataSetChanged();
                    if(!pass[0]&&GlobalVariables.AddIncidenteSeg.CodUbicacion!=null)
                    {
                        pass[0] =true;
                        String data[]= Ubicacion.split("\\.");
                        if(data.length>1)
                            spinner_sububicacion.setSelection(GlobalVariables.indexOf(GlobalVariables.SubUbicacion_obs,data[0]+"."+data[1]));
                        else pass[1] =true;
                    }
                    else {
                        GlobalVariables.AddIncidenteSeg.CodUbicacion=Ubicacionfinal;
                        spinner_sububicacion.setSelection(0);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Ubicacionfinal = "";
            }
        });
        spinner_sububicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    adapterUbicEspc.notifyDataSetChanged();
                    if(!pass[1]&&GlobalVariables.AddIncidenteSeg.CodUbicacion!=null)
                    {
                        pass[1] =true;
                        if(Ubicacion.split("\\.").length==3)
                            spinner_ubic_esp.setSelection(GlobalVariables.indexOf(GlobalVariables.UbicacionEspecifica_obs, GlobalVariables.Obserbacion.CodUbicacion));
                    }
                    else{
                        GlobalVariables.AddIncidenteSeg.CodUbicacion=Ubicacionfinal;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Ubicacionfinal = Ubicacionfinal.split("\\.")[0];
            }
        });
        spinner_ubic_esp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position>0)
                {
                    Maestro UbicaEspec = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_ubic_esp) ).getSelectedItem();
                    Ubicacionfinal=UbicaEspec.CodTipo;
                    GlobalVariables.AddIncidenteSeg.CodUbicacion=UbicaEspec.CodTipo;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                String Ubic[] =  Ubicacionfinal.split("\\.");
                Ubicacionfinal = Ubic[0]+"."+ Ubic[1];
            }
        });

        spinner_grupo_riesgo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(itemSelGR[0]!=position) {
                    itemSelGR[0]=position;
                    Maestro gRiesgo = (Maestro) ((Spinner) mView.findViewById(R.id.spinner_grupo_riesgo)).getSelectedItem();
                    GrupoRiesgoFinal = gRiesgo.CodTipo;
                    riesgoData.clear();
                    for (Maestro item : GlobalVariables.loadRiesgo(GrupoRiesgoFinal)) {
                        riesgoData.add(item);
                    }
                    adapterRiesgo.notifyDataSetChanged();
                    if(!passGR[0]&&GlobalVariables.AddIncidenteSeg.GrupoRiesgo!=null)
                    {
                        passGR[0] =true;
                        if(!StringUtils.isEmpty(GlobalVariables.AddIncidenteSeg.Riesgo))
                            spinner_riesgo.setSelection(GlobalVariables.indexOf(riesgoData,GrupoRiesgoFinal+"."+GlobalVariables.AddIncidenteSeg.Riesgo));
                    }
                    else {
                        GlobalVariables.AddIncidenteSeg.GrupoRiesgo=GrupoRiesgoFinal;
                        spinner_riesgo.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //gerencia="";
            }
        });

        spinner_riesgo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_riesgo) ).getSelectedItem();
                if(!StringUtils.isEmpty(Tipo.CodTipo))
                    GlobalVariables.AddIncidenteSeg.Riesgo=Tipo.CodTipo.split("\\.")[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // superint="";
            }
        });


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
                String FechaEnvio="";
                FechaEnvio= fecha_envio.format(actual);
                if(GlobalVariables.AddIncidenteSeg.Fecha==null)
                    btn_hora.setText("00:00:00");
                else{
                    String [] hora=GlobalVariables.AddIncidenteSeg.Fecha.split("T");
                    FechaEnvio=FechaEnvio.split("T")[0]+"T"+hora[1];
                }
                //Utils.inspeccionModel.Fecha= String.valueOf(fecha_envio.format(actual));
                GlobalVariables.AddIncidenteSeg.Fecha=FechaEnvio;
                fecha=dt.format(actual);
                btn_fecha.setText(dt.format(actual));
                //fecha_fin=dt.format(actual);
                escogioFecha = true;
                dt = new SimpleDateFormat("yyyyMMdd");
                fechaEscogida = dt.format(actual);

            }

        };

        btn_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FragmentAddCabeceraMA.this.getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
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

        btn_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalVariables.AddIncidenteSeg.Fecha!=null){
                    TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            //Formateo el hora obtenido: antepone el 0 si son menores de 10

                            //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                            //String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                            //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                            String AM_PM;
                            int horaFinal;
                            int minutoFinal;
                            //GlobalVariables.AddIncidenteSeg.Fecha=GlobalVariables.AddInspeccion.Fecha+"'"+"T"+"'"+String.valueOf(hourOfDay)+":"+minute;

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
                            String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                            String [] fecha=GlobalVariables.AddIncidenteSeg.Fecha.split("T");
                            GlobalVariables.AddIncidenteSeg.Fecha=fecha[0]+"T"+horaFormateada+":"+minutoFormateado+":00";

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

        btn_buscar_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title="Nueva Incidente/Reportado por";
                if(GlobalVariables.ObjectEditable)
                    title="Editar Incidente/Reportado por";

                Intent intent = new Intent(getContext(), B_personas.class);
                intent.putExtra("title",title);
                startActivityForResult(intent , 1);
            }
        });

        return mView;


    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) { // seleccion de Observado por
                String name=data.getStringExtra("nombreP");
                tx_reportado.setText(name);
//                GlobalVariables.AddIncidenteSeg.ObservadoPor=name;
//                GlobalVariables.AddIncidenteSeg.CodObservadoPor=data.getStringExtra("codpersona");
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void success(String data, String Tipo) {

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}