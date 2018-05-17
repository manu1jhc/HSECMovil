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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.Maestro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class B_facilito extends AppCompatActivity {
    //ArrayList<Maestro> gerenciadata;
    ArrayList<Maestro> superintdata;
    ArrayList<Maestro> tipodata;

    //fecha
    Calendar myCalendar,myCalendar2;
    DatePickerDialog.OnDateSetListener date, datefin;
    DialogFragment newFragment;
    String fecha_inicio="-";
    String fecha_fin="-";
    boolean escogioFecha;
    String fechaEscogida;
    Button btnFechaInicio,btnFechaFin,btnbuscar;
    ImageView img_guardar;
    //
    Spinner spinnerGerencia,spinnerSuperInt,spinner_tipo,spinner_estado;
    String gerencia_pos, superint_pos;
    EditText et_CodFacilito;
    ImageButton btn_buscar_p;
    ImageButton btn_buscar_c;
    TextView id_persona_res,id_creador;
    String tipo_persona="";
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_facilito);

        spinnerGerencia=(Spinner) findViewById(R.id.spinner_gerencia);
        spinnerSuperInt=(Spinner) findViewById(R.id.spinner_superint);
        spinner_tipo=findViewById(R.id.spinner_tipo);
        spinner_estado=findViewById(R.id.spinner_estado);

        btnFechaInicio=(Button) findViewById(R.id.btn_fecha_desde);
        btnFechaFin=(Button) findViewById(R.id.btn_fecha_fin);
        et_CodFacilito=findViewById(R.id.id_CodFacilito);

        btn_buscar_p=(ImageButton) findViewById(R.id.btn_buscar_p);
        id_persona_res=(TextView) findViewById(R.id.id_persona_res);
        btn_buscar_c=(ImageButton) findViewById(R.id.btn_buscar_c);
        id_creador=(TextView) findViewById(R.id.id_creador);
        img_guardar=findViewById(R.id.img_guardar);
        tipodata= new ArrayList<>();
        //ubicaciondata.add(new Maestro(null,"-  Seleccione  -"));
        tipodata.add(new Maestro("c","Condición"));
        tipodata.add(new Maestro("a","Acción"));

        superintdata=new ArrayList<>();
        superintdata.add(new Maestro(null,"-  Seleccione  -"));

        //aqui va spinner gerencia y superintendencia
        ArrayAdapter adapterGerencia = new ArrayAdapter(this.getBaseContext(),R.layout.custom_spinner_item, GlobalVariables.Gerencia);
        adapterGerencia.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

        ArrayAdapter adapterSuperInt = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerSuperInt.setAdapter(adapterSuperInt);


        ArrayAdapter adapterTipo = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item, tipodata);
        adapterTipo.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterTipo);

        ArrayAdapter adapterEstado = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item, GlobalVariables.ObsFacilito_estado);
        adapterEstado.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterEstado);


        spinner_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Maestro Tipo = (Maestro) ( (Spinner) findViewById(R.id.spinner_tipo) ).getSelectedItem();
                //GlobalVariables.Obserbacion.CodAreaHSEC=Tipo.CodTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Maestro estado = (Maestro) ( (Spinner) findViewById(R.id.spinner_estado) ).getSelectedItem();
                //GlobalVariables.Obserbacion.CodAreaHSEC=Tipo.CodTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        img_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.inspeccionModel.CodInspeccion=String.valueOf(codInsp.getText());
                //inspeccionModel=Utils.inspeccionModel;
                Intent intent = getIntent();
                intent.putExtra("Tipo_Busqueda",2);
                //intent.putExtra("codpersona",CodPersona);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



        spinnerGerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
               /* Maestro Geren = (Maestro) ( (Spinner) view.findViewById(R.id.spinner_gerencia) ).getSelectedItem();
                gerencia=Geren.CodTipo;*/
                    //superint=null;
                    String gerencia = GlobalVariables.Gerencia.get(position).CodTipo;
                    //Utils.inspeccionModel.Gerencia = gerenciadata.get(position).CodTipo;
                    gerencia_pos = String.valueOf(position);
                    superintdata.clear();
                    for (Maestro item : GlobalVariables.loadSuperInt(gerencia)
                            ) {
                        superintdata.add(item);
                    }
                    adapterSuperInt.notifyDataSetChanged();
                    spinnerSuperInt.setSelection(0);

                }else{
                    gerencia_pos = String.valueOf(position);
                    //Utils.inspeccionModel.Gerencia=null;
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

                String superint;
                if(position!=0) {
                    superint = superintdata.get(position).CodTipo.split("\\.")[1];
                    //Utils.inspeccionModel.SuperInt=superintdata.get(position).CodTipo.split("\\.")[1];
                    superint_pos=String.valueOf(position);
                }else{
                    superint="";
                    superint_pos="0";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // superint="";
            }
        });



        btn_buscar_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_persona="responsable";
                Intent intent = new Intent(B_facilito.this, B_personas.class);
                startActivityForResult(intent , REQUEST_CODE);
            }
        });


        btn_buscar_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_persona="creador";
                Intent intent = new Intent(B_facilito.this, B_personas.class);
                startActivityForResult(intent , REQUEST_CODE);
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
                /////////////Utils.inspeccionModel.FechaP= String.valueOf(fecha_envio.format(actual));

                fecha_inicio=dt.format(actual);
                btnFechaInicio.setText(dt.format(actual));
                // btnFechaFin.setText(dt.format(actual));
                // fecha_inicio=dt.format(actual);
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
                //Utils.observacionModel.Fecha_fin= String.valueOf(actual);

                SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
                //btnFechaInicio.setText(dt.format(actual));

                SimpleDateFormat fecha_envio = new SimpleDateFormat("yyyy-MM-dd");

                ///////////////////////Utils.inspeccionModel.Fecha= String.valueOf(fecha_envio.format(actual));

                fecha_fin=dt.format(actual);
                btnFechaFin.setText(dt.format(actual));
                //fecha_fin=dt.format(actual);
                escogioFecha = true;
                dt = new SimpleDateFormat("yyyyMMdd");
                fechaEscogida = dt.format(actual);
              /*  if (escogioOrigen && escogioDestino && escogioFecha)
                    botonBuscarTickets.setEnabled(true);
                    */
            }

        };

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

        //datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        //tempCalendar.set(Calendar.MONTH, (new Date()).getMonth() + 1);
        datePickerDialog.getDatePicker().setMaxDate(tempCalendar.getTimeInMillis());
        datePickerDialog.show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                String tipo_dato=data.getStringExtra("tipo");

                if(tipo_persona.equals("responsable")) {
                    String nombre_obs = data.getStringExtra("nombreP");
                    String codpersona_obs = data.getStringExtra("codpersona");
                    id_persona_res.setText(nombre_obs);
                    //Utils.inspeccionModel.CodTipo = codpersona_obs;
                }else{

                    String nombre_obs = data.getStringExtra("nombreP");
                    String codpersona_obs = data.getStringExtra("codpersona");
                    id_creador.setText(nombre_obs);
                    //Utils.inspeccionModel.CodContrata = cod_contrata;
                }



            }
        } catch (Exception ex) {
            Toast.makeText(B_facilito.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }



}
