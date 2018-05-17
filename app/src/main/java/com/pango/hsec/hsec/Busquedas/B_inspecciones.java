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
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.Maestro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class B_inspecciones extends AppCompatActivity {
    ArrayList<Maestro> gerenciadata;
    ArrayList<Maestro> superintdata;
    ArrayList<Maestro> ubicaciondata;
    ArrayList<Maestro> sububicacion_data;

    InspeccionModel inspeccionModel=new InspeccionModel();


    Calendar myCalendar,myCalendar2;
    DatePickerDialog.OnDateSetListener date, datefin;
    DialogFragment newFragment;

    Button btnFechaInicio,btnFechaFin;
    ImageButton btnbuscar;
    Spinner spinnerUbicacion,spinnerSubUbicacion, spinnerGerencia,spinnerSuperInt;
    String Ubicacionfinal="",TipoObs;
    boolean escogioFecha;
    String fechaEscogida;
    boolean ultima_fecha=true;
    ImageButton btn_buscar_p;
    ImageButton btn_buscar_c;

    String datos_user;
    String codUser;
    ArrayAdapter adapterSuperInt;

    TextView id_persona,insp_contrata;
    EditText codInsp;
    String fecha_inicio="-";
    String fecha_fin="-";
    public static final int REQUEST_CODE = 1;

    String gerencia_pos="0";
    String superint_pos="0";
    String ubic_pos="0";
    String sububic_pos="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_inspecciones);

        spinnerUbicacion = (Spinner) findViewById(R.id.spinner_ubicacion);
        spinnerSubUbicacion=(Spinner) findViewById(R.id.spinner_sububic);
        spinnerGerencia=(Spinner) findViewById(R.id.spinner_gerencia);
        spinnerSuperInt=(Spinner) findViewById(R.id.spinner_superint);

        codInsp=(EditText) findViewById(R.id.id_CodInspeccion);
        btnFechaInicio=(Button) findViewById(R.id.btn_fecha_desde);
        btnFechaFin=(Button) findViewById(R.id.btn_fecha_fin);

        btnbuscar=(ImageButton) findViewById(R.id.btn_buscar_insp);

        btn_buscar_p=(ImageButton) findViewById(R.id.btn_buscar_p);
        id_persona=(TextView) findViewById(R.id.id_persona);
        btn_buscar_c=(ImageButton) findViewById(R.id.btn_buscar_c);
        insp_contrata=(TextView) findViewById(R.id.insp_contrata);

        ubicaciondata= new ArrayList<>();
        //ubicaciondata.add(new Maestro(null,"-  Seleccione  -"));
        ubicaciondata.addAll(GlobalVariables.Ubicacion_obs);

        sububicacion_data= new ArrayList<>();
        //sububicacion_data.add(new Maestro(null,"-  Seleccione  -"));
        sububicacion_data.addAll(GlobalVariables.SubUbicacion_obs);

        gerenciadata= new ArrayList<>();
        //gerenciadata.add(new Maestro(null,"-  Seleccione  -"));
        gerenciadata.addAll(GlobalVariables.Gerencia);

        superintdata=new ArrayList<>();
        superintdata.add(new Maestro(null,"-  Seleccione  -"));
        //superintdata.addAll(GlobalVariables.SuperIntendencia);

        ArrayAdapter adapterUbic = new ArrayAdapter(this.getBaseContext(),R.layout.custom_spinner_item, ubicaciondata);
        adapterUbic.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerUbicacion.setAdapter(adapterUbic);

        final ArrayAdapter adapterSubUbic = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item, sububicacion_data);
        adapterSubUbic.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerSubUbicacion.setAdapter(adapterSubUbic);

        //aqui va spinner gerencia y superintendencia
        ArrayAdapter adapterGerencia = new ArrayAdapter(this.getBaseContext(),R.layout.custom_spinner_item, gerenciadata);
        adapterGerencia.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

        adapterSuperInt = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerSuperInt.setAdapter(adapterSuperInt);

        spinnerUbicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               /* Maestro ubica = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_ubic) ).getSelectedItem();
                Ubicacionfinal=ubica.CodTipo;*/
                String ubicacion=ubicaciondata.get(position).CodTipo;
                Utils.inspeccionModel.CodUbicacion=ubicaciondata.get(position).CodTipo;

                sububicacion_data.clear();

                for (Maestro item: GlobalVariables.loadUbicacion(ubicacion,2)
                        ) {
                    sububicacion_data.add(item);
                }
                adapterSubUbic.notifyDataSetChanged();
                spinnerSubUbicacion.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Ubicacionfinal = "";
            }
        });

        spinnerSubUbicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String sububic;
                if(position!=0) {
                    sububic = superintdata.get(position).CodTipo.split("\\.")[1];
                    Utils.inspeccionModel.CodSubUbicacion=sububicacion_data.get(position).CodTipo.split("\\.")[1];
                    sububic_pos=String.valueOf(position);
                }else{
                    sububic="";
                    sububic_pos="0";
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Ubicacionfinal = Ubicacionfinal.split("\\.")[0];
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
                    Utils.inspeccionModel.Gerencia = gerenciadata.get(position).CodTipo;
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
                    Utils.inspeccionModel.Gerencia=null;
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
                    Utils.inspeccionModel.SuperInt=superintdata.get(position).CodTipo.split("\\.")[1];
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
                Utils.inspeccionModel.FechaP= String.valueOf(fecha_envio.format(actual));

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

                Utils.inspeccionModel.Fecha= String.valueOf(fecha_envio.format(actual));

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
             /*   Utils.observacionModel.CodObservacion=String.valueOf(codObs.getText());

                Utils.tempObs.add(String.valueOf(codObs.getText()));
                //Utils.tempObs.add(String.valueOf(spinnerArea.get));
                Utils.tempObs.add(area_pos);
                Utils.tempObs.add(tipo_pos);
                Utils.tempObs.add(nivel_pos);
                Utils.tempObs.add(fecha_inicio);
                Utils.tempObs.add(fecha_fin);
                Utils.tempObs.add(gerencia_pos);
                Utils.tempObs.add(superint_pos);
*/
                // Intent intent = new Intent(B_observaciones.this, B_personas.class);
                //startActivity(intent);

                Intent intent = new Intent(B_inspecciones.this, B_personas.class);
                startActivityForResult(intent , REQUEST_CODE);
            }
        });


        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.inspeccionModel.CodInspeccion=String.valueOf(codInsp.getText());
                inspeccionModel=Utils.inspeccionModel;
                Intent intent = getIntent();
                intent.putExtra("Tipo_Busqueda",2);
                //intent.putExtra("codpersona",CodPersona);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



        btn_buscar_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(B_inspecciones.this, B_contrata.class);
                startActivityForResult(intent , REQUEST_CODE);

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                String tipo_dato=data.getStringExtra("tipo");

                if(tipo_dato.equals("persona")) {
                    String nombre_obs = data.getStringExtra("nombreP");
                    String codpersona_obs = data.getStringExtra("codpersona");
                    id_persona.setText(nombre_obs);
                    Utils.inspeccionModel.CodTipo = codpersona_obs;
                }else{
                    String cod_contrata = data.getStringExtra("codContrata");
                    String des_contrata = data.getStringExtra("desContrata");
                    insp_contrata.setText(des_contrata);
                    Utils.inspeccionModel.CodContrata = cod_contrata;
                }



            }
        } catch (Exception ex) {
            Toast.makeText(B_inspecciones.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

}
