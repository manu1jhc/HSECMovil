package com.pango.hsec.hsec.Busquedas;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
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
import com.pango.hsec.hsec.model.VerificacionModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class B_Verificaciones extends AppCompatActivity {
    ArrayList<Maestro> gerenciadata;
    ArrayList<Maestro> superintdata;
    ArrayList<Maestro> area_data;
    ArrayList<Maestro> tipo_data;
    ArrayList<Maestro> nivel_data;
    String area, area_pos="0";
    String tipo, tipo_pos="0";
    String nivel, nivel_pos="0";
    String gerencia_pos="0";
    String superint_pos="0";
    VerificacionModel  verificacionModel=new VerificacionModel();
    Calendar myCalendar,myCalendar2;
    DatePickerDialog.OnDateSetListener date, datefin;
    DialogFragment newFragment;
    Button btnFechaInicio,btnFechaFin;
    ImageButton btnbuscar;
    Spinner spinnerArea,spinnerTipoObs, spinnerNivel, spinnerGerencia,spinnerSuperInt;
    String Ubicacionfinal="",TipoObs;
    EditText codObs;
    boolean escogioFecha;
    String fechaEscogida;
    boolean ultima_fecha=true;
    ImageView btn_buscar_p;
    String datos_user;
    String codUser;
    ArrayAdapter adapterSuperInt;
    TextView id_persona;
    CardView id_obspor;
    String fecha_inicio="-";
    String fecha_fin="-";
    public static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_verificaciones);
        Utils.verificacionModel = new VerificacionModel();

        spinnerArea = (Spinner) findViewById(R.id.spinner_area);
        spinnerTipoObs=(Spinner) findViewById(R.id.spinner_tipobs);
        spinnerNivel = (Spinner) findViewById(R.id.spinner_NivelR);

        spinnerGerencia=(Spinner) findViewById(R.id.spinner_gerencia);
        spinnerSuperInt=(Spinner) findViewById(R.id.spinner_superint);

        codObs=(EditText) findViewById(R.id.id_CodObservacion);
        btnFechaInicio=(Button) findViewById(R.id.btn_fecha_desde);
        btnFechaFin=(Button) findViewById(R.id.btn_fecha_fin);
        btnbuscar=(ImageButton) findViewById(R.id.btn_buscar_obs);
        btn_buscar_p=(ImageView) findViewById(R.id.btn_buscar_p);
        id_persona=(TextView) findViewById(R.id.id_persona);
        id_obspor=findViewById(R.id.id_obspor);
        if(GlobalVariables.userLogin.Rol.equals("1")||GlobalVariables.userLogin.Rol.equals("4")){
            id_obspor.setVisibility(View.VISIBLE);
        }else{
            Utils.verificacionModel.ObservadoPor= GlobalVariables.userLoaded.CodPersona;

        }
        area_data= new ArrayList<>();
        //area_data.add(new Maestro(null,"-  Seleccione  -"));
        area_data.addAll(GlobalVariables.Area_obs);

        tipo_data= new ArrayList<>();
        //tipo_data.add(new Maestro(null,"-  Seleccione  -"));
        tipo_data.addAll(GlobalVariables.Tipo_Ver);

        nivel_data= new ArrayList<>();
        //nivel_data.add(new Maestro(null,"-  Seleccione  -"));
        nivel_data.addAll(GlobalVariables.NivelRiesgo_obs);

        gerenciadata= new ArrayList<>();
        //gerenciadata.add(new Maestro(null,"-  Seleccione  -"));
        gerenciadata.addAll(GlobalVariables.Gerencia);

        superintdata=new ArrayList<>();
        superintdata.add(new Maestro(null,"-  Seleccione  -"));
        //superintdata.addAll(GlobalVariables.SuperIntendencia);

        ArrayAdapter adapterArea = new ArrayAdapter(this.getBaseContext(),R.layout.custom_spinner_item, area_data);
        adapterArea.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerArea.setAdapter(adapterArea);

        ArrayAdapter adapterTipoObs = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item, tipo_data);
        adapterTipoObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerTipoObs.setAdapter(adapterTipoObs);

        ArrayAdapter adapterNivel = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item, nivel_data);
        adapterNivel.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerNivel.setAdapter(adapterNivel);

        //aqui va spinner gerencia y superintendencia
        ArrayAdapter adapterGerencia = new ArrayAdapter(this.getBaseContext(),R.layout.custom_spinner_item, gerenciadata);
        adapterGerencia.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

        adapterSuperInt = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerSuperInt.setAdapter(adapterSuperInt);

        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    Utils.verificacionModel.CodAreaHSEC=area_data.get(position).CodTipo;

                    //area = area_data.get(position).CodTipo;
                    area_pos= String.valueOf(position);
                }else {
                    Utils.verificacionModel.CodAreaHSEC=null;
                    area="";
                    area_pos=String.valueOf(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
               /* Maestro Geren = (Maestro) ( (Spinner) view.findViewById(R.id.spinner_gerencia) ).getSelectedItem();
                gerencia=Geren.CodTipo;*/
                    //superint=null;
                    String gerencia = gerenciadata.get(position).CodTipo;
                    Utils.verificacionModel.Gerencia = gerenciadata.get(position).CodTipo;
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
                    Utils.verificacionModel.Gerencia=null;
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
                    Utils.verificacionModel.Superint=superintdata.get(position).CodTipo.split("\\.")[1];
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

        spinnerTipoObs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {

                    Utils.verificacionModel.CodTipo = tipo_data.get(position).CodTipo;
                    tipo_pos=String.valueOf(position);
                }else{
                    tipo="";
                    Utils.verificacionModel.CodTipo=null;
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
                    Utils.verificacionModel.CodNivelRiesgo=nivel_data.get(position).CodTipo;
                    //nivel = nivel_data.get(position).Descripcion;
                    nivel_pos=String.valueOf(position);
                }else{
                    Utils.verificacionModel.CodNivelRiesgo=null;
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
                SimpleDateFormat fecha_envio = new SimpleDateFormat("yyyy-MM-dd");
                Utils.observacionModel.FechaInicio= String.valueOf(fecha_envio.format(actual));

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

                Utils.observacionModel.FechaFin= String.valueOf(fecha_envio.format(actual));

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

        btn_buscar_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(B_Verificaciones.this, B_personas.class);
                intent.putExtra("title","Verificaciones/Filtro/Observador");
                startActivityForResult(intent , REQUEST_CODE);
            }
        });

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.observacionModel.CodObservacion=String.valueOf(codObs.getText());
                verificacionModel =Utils.verificacionModel;
                Intent intent = getIntent();
                intent.putExtra("Tipo_Busqueda",1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                String nombre_obs = data.getStringExtra("nombreP");
                String codpersona_obs = data.getStringExtra("codpersona");
                id_persona.setText(nombre_obs);
                Utils.verificacionModel.ObservadoPor=codpersona_obs;
            }
        } catch (Exception ex) {
            Toast.makeText(B_Verificaciones.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


}


