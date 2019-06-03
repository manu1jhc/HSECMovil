package com.pango.hsec.hsec.Verificaciones;
import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.content.ContextCompat;
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
import android.widget.TimePicker;
import android.widget.Toast;

        import com.google.gson.Gson;
        import com.pango.hsec.hsec.Busquedas.B_personas;
        import com.pango.hsec.hsec.GlobalVariables;
        import com.pango.hsec.hsec.IActivity;
        import com.pango.hsec.hsec.R;
        import com.pango.hsec.hsec.controller.ActivityController;
        import com.pango.hsec.hsec.model.Maestro;
        import com.pango.hsec.hsec.model.ObservacionModel;
        import com.pango.hsec.hsec.model.UsuarioModel;
import com.pango.hsec.hsec.model.VerificacionModel;

import org.apache.commons.lang3.StringUtils;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;

        import static android.app.Activity.RESULT_OK;

public class FragmentVerificacion extends Fragment implements IActivity {

    private static View mView, dView;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    DialogFragment newFragment;
    Button botonEscogerFecha,btn_hora;
    ImageButton btnSelectObservador;
    Spinner spinnerArea, spinnerNivel, spinnerUbica,spinnerSububic,spinnerUbicEspec, spinnerTipoObs;
    String Ubicacionfinal="";
    EditText txtLugar;
    TextView txtObservado;
    TextView Codigo;
    String Ubicacion="";
    TextView textView6, textView7,textView9;

    //fecha
    DatePickerDialog.OnDateSetListener  datefin;
    Calendar myCalendar2;
    String fecha_fin="-";
    boolean escogioFecha;
    String fechaEscogida;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    int hora = c.get(Calendar.HOUR_OF_DAY);
    int minuto = c.get(Calendar.MINUTE);

    public ArrayAdapter adapterUbicEspc,adapterSubN;


    public static final FragmentVerificacion newInstance(String sampleText) {
        FragmentVerificacion f = new FragmentVerificacion();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_verificacion, container, false);
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
        textView6=mView.findViewById(R.id.textView6);
        textView6.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Área"));
        //sp2.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Responsable"));
        textView7=mView.findViewById(R.id.textView7);
        textView7.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Nivel de Riesgo:"));

        textView9=mView.findViewById(R.id.textView9);
        textView9.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Ubicación:"));

        ArrayAdapter adapterTipoObs = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item, GlobalVariables.Tipo_Ver);
        adapterTipoObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerTipoObs.setAdapter(adapterTipoObs);

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

        Codigo = (TextView) mView.findViewById(R.id.id_CodVerificacion);
        btnSelectObservador=(ImageButton) mView.findViewById(R.id.btn_observadopor);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        botonEscogerFecha=(Button) mView.findViewById(R.id.btn_fecha);

        btn_hora=(Button) mView.findViewById(R.id.btn_hora);
        btn_hora.setText("SELECCIONAR HORA");

        Date fecha=new Date();
        if(GlobalVariables.ObjectEditable){ // load data of server
            if(GlobalVariables.Verificacion.CodVerificacion==null)
            {
                String url= GlobalVariables.Url_base+"Verificacion/Get/"+codigo_obs;
                ActivityController obj = new ActivityController("get", url, FragmentVerificacion.this,getActivity());
                obj.execute("");
            }
            else setdata();
        }
        else // new Verificacion
        {
            if(GlobalVariables.Verificacion.CodVerificacion==null){
                myCalendar = Calendar.getInstance();
                fecha = myCalendar.getTime();
                Gson gson = new Gson();
                UsuarioModel UsuarioLogeado = gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);

                GlobalVariables.Verificacion= new VerificacionModel();
                GlobalVariables.Verificacion.CodVerificacion= codigo_obs;

                GlobalVariables.Verificacion.ObservadoPor=UsuarioLogeado.Nombres;
                GlobalVariables.Verificacion.CodVerificacionPor=UsuarioLogeado.CodPersona;
                GlobalVariables.Verificacion.Fecha=df.format(fecha);
                GlobalVariables.Verificacion.Lugar="";
                GlobalVariables.Verificacion.CodAreaHSEC=GlobalVariables.Area_obs.get(0).CodTipo;
                GlobalVariables.Verificacion.CodNivelRiesgo=GlobalVariables.NivelRiesgo_obs.get(0).CodTipo;
                GlobalVariables.Verificacion.CodTipo=GlobalVariables.Tipo_obs.get(0).CodTipo;
                GlobalVariables.Verificacion.CodUbicacion="";
                GlobalVariables.StrVerificacion=gson.toJson(GlobalVariables.Verificacion);
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
                    if(!pass[0]&&GlobalVariables.Verificacion.CodUbicacion!=null)
                    {
                        pass[0] =true;
                        String data[]= Ubicacion.split("\\.");
                        if(data.length>1)
                            spinnerSububic.setSelection(GlobalVariables.indexOf(GlobalVariables.SubUbicacion_obs,data[0]+"."+data[1]));
                        else pass[1] =true;
                    }
                    else {
                        GlobalVariables.Verificacion.CodUbicacion=Ubicacionfinal;
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
                    if(!pass[1]&&GlobalVariables.Verificacion.CodUbicacion!=null)
                    {
                        pass[1] =true;
                        if(Ubicacion.split("\\.").length==3)
                            spinnerUbicEspec.setSelection(GlobalVariables.indexOf(GlobalVariables.UbicacionEspecifica_obs, GlobalVariables.Obserbacion.CodUbicacion));
                    }
                    else{
                        GlobalVariables.Verificacion.CodUbicacion=Ubicacionfinal;
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
                    GlobalVariables.Verificacion.CodUbicacion=UbicaEspec.CodTipo;
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
                GlobalVariables.Verificacion.CodTipo=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_area) ).getSelectedItem();
                GlobalVariables.Verificacion.CodAreaHSEC=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_NivelR) ).getSelectedItem();
                GlobalVariables.Verificacion.CodNivelRiesgo=Tipo.CodTipo;
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
                GlobalVariables.Verificacion.Lugar = txtLugar.getText().toString();
            }
        });
// Fecha y Hora
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
                if(GlobalVariables.Verificacion.Fecha==null)
                    btn_hora.setText("00:00:00");
                else{
                    String [] hora=GlobalVariables.Verificacion.Fecha.split("T");
                    FechaEnvio=FechaEnvio.split("T")[0]+"T"+hora[1];
                }
                //Utils.inspeccionModel.Fecha= String.valueOf(fecha_envio.format(actual));
                GlobalVariables.Verificacion.Fecha=FechaEnvio;
                fecha_fin=dt.format(actual);
                botonEscogerFecha.setText(dt.format(actual));
                //fecha_fin=dt.format(actual);
                escogioFecha = true;
                dt = new SimpleDateFormat("yyyyMMdd");
                fechaEscogida = dt.format(actual);
            }
        };
        botonEscogerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), datefin, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH));
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.set(Calendar.DAY_OF_MONTH,1);
                tempCalendar.set(Calendar.HOUR, 0);
                tempCalendar.set(Calendar.MINUTE, 0);
                tempCalendar.set(Calendar.SECOND, 0);
                tempCalendar.set(Calendar.MILLISECOND, 0);

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
                if(GlobalVariables.Verificacion.Fecha!=null){
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
                            String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                            String [] fecha=GlobalVariables.Verificacion.Fecha.split("T");
                            GlobalVariables.Verificacion.Fecha=fecha[0]+"T"+horaFormateada+":"+minutoFormateado+":00";

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
        SimpleDateFormat dh = new SimpleDateFormat("h:mm a");

        if(!StringUtils.isEmpty(GlobalVariables.Verificacion.Fecha)){
            try {
                fecha = df.parse(GlobalVariables.Verificacion.Fecha);
                botonEscogerFecha.setText(dt.format(fecha));
                btn_hora.setText(dh.format(fecha));
            } catch (ParseException e) {
                e.printStackTrace();
                botonEscogerFecha.setText("Seleccionar fecha");
            }
        }
        if(GlobalVariables.Verificacion.CodVerificacion!=null)Codigo.setText(GlobalVariables.Verificacion.CodVerificacion);
        if(GlobalVariables.Verificacion.ObservadoPor!=null)txtObservado.setText(GlobalVariables.Verificacion.ObservadoPor);
        if(GlobalVariables.Verificacion.Lugar!=null)txtLugar.setText(GlobalVariables.Verificacion.Lugar);

        if(!StringUtils.isEmpty(GlobalVariables.Verificacion.CodTipo))spinnerTipoObs.setSelection(GlobalVariables.indexOf(GlobalVariables.Tipo_obs,GlobalVariables.Verificacion.CodTipo));
        if(!StringUtils.isEmpty(GlobalVariables.Verificacion.CodAreaHSEC))spinnerArea.setSelection(GlobalVariables.indexOf(GlobalVariables.Area_obs,GlobalVariables.Verificacion.CodAreaHSEC));
        if(!StringUtils.isEmpty(GlobalVariables.Verificacion.CodNivelRiesgo))spinnerNivel.setSelection(GlobalVariables.indexOf(GlobalVariables.NivelRiesgo_obs,GlobalVariables.Verificacion.CodNivelRiesgo));

        if(!StringUtils.isEmpty(GlobalVariables.Verificacion.CodUbicacion)){
            Ubicacion=GlobalVariables.Verificacion.CodUbicacion;
            String data[] = GlobalVariables.Verificacion.CodUbicacion.split("\\.");
            spinnerUbica.setSelection(GlobalVariables.indexOf(GlobalVariables.Ubicacion_obs, data[0]));

        }
    }
    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        GlobalVariables.Verificacion = gson.fromJson(data, VerificacionModel.class);
        GlobalVariables.StrVerificacion = gson.toJson(GlobalVariables.Verificacion);
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
                GlobalVariables.Verificacion.ObservadoPor=name;
                GlobalVariables.Verificacion.CodVerificacionPor=data.getStringExtra("codpersona");
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}



