package com.pango.hsec.hsec.Busquedas;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.SelectDateFragment;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.Maestro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class B_observaciones extends AppCompatActivity {
    ArrayList<Maestro> gerenciadata;
    ArrayList<Maestro> superintdata;
    ArrayList<Maestro> area_data;
    ArrayList<Maestro> tipo_data;
    ArrayList<Maestro> nivel_data;
    String area, area_pos;
    String tipo, tipo_pos;
    String nivel, nivel_pos;

    Calendar myCalendar,myCalendar2;
    DatePickerDialog.OnDateSetListener date, datefin;
    DialogFragment newFragment;
    Button btnFechaInicio,btnFechaFin,btnbuscar;
    Spinner spinnerArea,spinnerTipoObs, spinnerNivel, spinnerGerencia,spinnerSuperInt;
    String Ubicacionfinal="",TipoObs;
    EditText codObs;
    boolean escogioFecha;
    String fechaEscogida;
    boolean ultima_fecha=true;
    ImageButton btn_buscar_p;
    String datos_user;
    String codUser;

    TextView id_persona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_observaciones);

        spinnerArea = (Spinner) findViewById(R.id.spinner_area);
        spinnerTipoObs=(Spinner) findViewById(R.id.spinner_tipobs);
        spinnerNivel = (Spinner) findViewById(R.id.spinner_NivelR);

        spinnerGerencia=(Spinner) findViewById(R.id.spinner_gerencia);
        spinnerSuperInt=(Spinner) findViewById(R.id.spinner_superint);

        codObs=(EditText) findViewById(R.id.id_CodObservacion);
        btnFechaInicio=(Button) findViewById(R.id.btn_fecha_desde);
        btnFechaFin=(Button) findViewById(R.id.btn_fecha_fin);
        btnbuscar=(Button) findViewById(R.id.btn_buscar_obs);
        btn_buscar_p=(ImageButton) findViewById(R.id.btn_buscar_p);
        id_persona=(TextView) findViewById(R.id.id_persona);

        area_data= new ArrayList<>();
        area_data.add(new Maestro("-","-"));
        area_data.addAll(GlobalVariables.Area_obs);

        tipo_data= new ArrayList<>();
        tipo_data.add(new Maestro("-","-"));
        tipo_data.addAll(GlobalVariables.Tipo_obs);

        nivel_data= new ArrayList<>();
        nivel_data.add(new Maestro("-","-"));
        nivel_data.addAll(GlobalVariables.NivelRiesgo_obs);

        gerenciadata= new ArrayList<>();
        gerenciadata.add(new Maestro("-","-"));
        gerenciadata.addAll(GlobalVariables.Gerencia);

        superintdata=new ArrayList<>();
        superintdata.add(new Maestro("-","-"));
        superintdata.addAll(GlobalVariables.SuperIntendencia);



        ArrayAdapter adapterArea = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, area_data);
        adapterArea.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerArea.setAdapter(adapterArea);

        ArrayAdapter adapterTipoObs = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, tipo_data);
        adapterTipoObs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTipoObs.setAdapter(adapterTipoObs);

        ArrayAdapter adapterNivel = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, nivel_data);
        adapterNivel.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerNivel.setAdapter(adapterNivel);

        //aqui va spinner gerencia y superintendencia
        ArrayAdapter adapterGerencia = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, gerenciadata);
        adapterGerencia.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

        ArrayAdapter adapterSuperInt = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSuperInt.setAdapter(adapterSuperInt);

        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    area = area_data.get(position).CodTipo;
                    area_pos= String.valueOf(position);
                }else {
                    area="";
                    area_pos=String.valueOf(position);
                }

                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTipoObs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    tipo = tipo_data.get(position).Descripcion;
                    tipo_pos=String.valueOf(position);
                }else{
                    tipo="";
                    tipo_pos=String.valueOf(position);
                }

                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    nivel = nivel_data.get(position).Descripcion;
                    nivel_pos=String.valueOf(position);
                }else{
                    tipo="";
                    nivel_pos=String.valueOf(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                btnFechaInicio.setText(dt.format(actual));
               // btnFechaFin.setText(dt.format(actual));

                escogioFecha = true;
                dt = new SimpleDateFormat("yyyyMMdd");
                fechaEscogida = dt.format(actual);
              /*  if (escogioOrigen && escogioDestino && escogioFecha)
                    botonBuscarTickets.setEnabled(true);
                    */
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
                SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
                //btnFechaInicio.setText(dt.format(actual));
                 btnFechaFin.setText(dt.format(actual));

                escogioFecha = true;
                dt = new SimpleDateFormat("yyyyMMdd");
                fechaEscogida = dt.format(actual);
              /*  if (escogioOrigen && escogioDestino && escogioFecha)
                    botonBuscarTickets.setEnabled(true);
                    */
            }

        };


        btn_buscar_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.tempObs.add(String.valueOf(codObs.getText()));
                //Utils.tempObs.add(String.valueOf(spinnerArea.get));
                Utils.tempObs.add(area);
                Utils.tempObs.add(tipo);
                Utils.tempObs.add(nivel);

                Intent intent = new Intent(B_observaciones.this, B_personas.class);
                startActivity(intent);
            }
        });


        Bundle datos = this.getIntent().getExtras();
        datos_user=datos.getString("nombreP");

        if(datos_user.equals("")){

        }else{
            codUser=datos.getString("codpersona");
            id_persona.setText(datos_user);

            spinnerArea.setSelection(0);


        }


    }

    public void close(View view){
        finish();
    }


    public void escogerFecha(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.HOUR, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);

        //datePickerDialog.getDatePicker().setMinDate(tempCalendar.getTimeInMillis());
        //tempCalendar.set(Calendar.MONTH, (new Date()).getMonth() + 1);
        datePickerDialog.getDatePicker().setMaxDate(myCalendar2.getTimeInMillis());
        datePickerDialog.show();
    }

    public void escogerFecha2(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datefin, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH));
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.HOUR, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);

        datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        //tempCalendar.set(Calendar.MONTH, (new Date()).getMonth() + 1);
        datePickerDialog.getDatePicker().setMaxDate(tempCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

}
