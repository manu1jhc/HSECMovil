package com.pango.hsec.hsec;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.adapter.ListPersonEditAdapter;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.model.PlanModel;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PlanAccionEdit extends AppCompatActivity {
    public PlanModel Plan;
    public TextView SolicitadoPor;
    Button btnFechaInicio,btnFechaFin;
    ImageButton btnSelectSolicitante, btnaddresponsables;
    Spinner spActRelacionada, spNivelRiesgo, spinnerArea,spTipoAccion;
    DatePickerDialog.OnDateSetListener dateInicial,dateFinal;
    Calendar myCalendar;
    SimpleDateFormat df;
    EditText txtTarea;
    Gson gson;
    private RecyclerView listView;
    private ListPersonEditAdapter listPersonAdapter;
    private ArrayList<PersonaModel> ListResponsables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle datos = this.getIntent().getExtras();
        gson = new Gson();
        Plan = gson.fromJson(datos.getString("Plan"), PlanModel.class);
        setContentView(R.layout.activity_plan_accion_edit);
        if(!(Plan.CodAccion.contains("-1")||Plan.CodAccion.equals("0"))) {
            CardView cv_edit = (CardView) findViewById(R.id.plan_edit);
            cv_edit.setVisibility(View.VISIBLE);
            TextView CodReferencia = (TextView)findViewById(R.id.txt_codreferencia);
            TextView CodAccion = (TextView)findViewById(R.id.txt_codaccion);
            TextView CodEstado = (TextView)findViewById(R.id.txt_estado);
            TextView txtReferencia = (TextView)findViewById(R.id.txt_referencia);
            txtReferencia.setText(GlobalVariables.getDescripcion(GlobalVariables.Referencia_Plan,Plan.CodReferencia));
            CodReferencia.setText(Plan.NroDocReferencia);
            CodAccion.setText(Plan.CodAccion);
            CodEstado.setText(GlobalVariables.getDescripcion(GlobalVariables.Estado_Plan,Plan.CodEstadoAccion));
        }

        TextView FechaSolic = (TextView)findViewById(R.id.txt_fecha);
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
        SolicitadoPor = (TextView)findViewById(R.id.txt_solicitado);


//Spinners
        spActRelacionada = (Spinner) findViewById(R.id.sp_actrelacionada);
        spNivelRiesgo = (Spinner) findViewById(R.id.sp_nivelriesgo);
        spinnerArea = (Spinner) findViewById(R.id.sp_areahsec);
        spTipoAccion = (Spinner) findViewById(R.id.sp_tipoaccion);

        ArrayAdapter adapterTipoObs = new ArrayAdapter(this,android.R.layout.simple_spinner_item,GlobalVariables.Actividad_obs);
        adapterTipoObs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spActRelacionada.setAdapter(adapterTipoObs);

        ArrayAdapter adapterNivelR = new ArrayAdapter(this,android.R.layout.simple_spinner_item,GlobalVariables.NivelRiesgo_obs);
        adapterTipoObs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spNivelRiesgo.setAdapter(adapterNivelR);

        ArrayAdapter adapterArea = new ArrayAdapter(this,android.R.layout.simple_spinner_item,GlobalVariables.Area_obs);
        adapterTipoObs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerArea.setAdapter(adapterArea);

        ArrayAdapter adapterTipoacc = new ArrayAdapter(this,android.R.layout.simple_spinner_item,GlobalVariables.Tipo_Plan);
        adapterTipoObs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spTipoAccion.setAdapter(adapterTipoacc);

//buttons
        btnFechaInicio=(Button) findViewById(R.id.btn_fecha);
        btnFechaFin=(Button) findViewById(R.id.btn_fechafin);
        btnSelectSolicitante=(ImageButton) findViewById(R.id.btn_solcitadopor);
        btnaddresponsables=(ImageButton) findViewById(R.id.add_responsables);
        btnaddresponsables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanAccionEdit.this, B_personas.class);
                startActivityForResult(intent , 2);
            }
        });
        btnSelectSolicitante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanAccionEdit.this, B_personas.class);
                startActivityForResult(intent , 1);
            }
        });

        dateInicial = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date actual = myCalendar.getTime();
                SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
                Plan.FecComprometidaInicial=df.format(actual);
                btnFechaInicio.setText(dt.format(actual));

                /*escogioFecha = true;
                dt = new SimpleDateFormat("yyyyMMdd");
                fechaEscogida = dt.format(actual);
                if (escogioOrigen && escogioDestino && escogioFecha)
                    botonBuscarTickets.setEnabled(true);*/
            }
        };
        dateFinal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date actual = myCalendar.getTime();

                Plan.FecComprometidaFinal=df.format(actual);
                btnFechaFin.setText(dt.format(actual));
            }
        };
        txtTarea   = (EditText)findViewById(R.id.txt_tarea);
        // listviewAdapter
        ListResponsables= new ArrayList<>();
        listView = (RecyclerView) findViewById(R.id.listResponsables);

        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        if(!Plan.CodAccion.equals("0")){ //Edit plan

            try {
                Date fecha = df.parse(Plan.FechaSolicitud);
                FechaSolic.setText(formatoRender.format(fecha));
                if(Plan.FecComprometidaInicial!=null)
                {
                    fecha = df.parse(Plan.FecComprometidaInicial);
                    btnFechaInicio.setText(dt.format(fecha));
                }
                if(Plan.FecComprometidaFinal!=null){
                    fecha = df.parse(Plan.FecComprometidaFinal);
                    btnFechaFin.setText(dt.format(fecha));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spActRelacionada.setSelection(GlobalVariables.indexOf(GlobalVariables.Actividad_obs,Plan.CodActiRelacionada));
            spNivelRiesgo.setSelection(GlobalVariables.indexOf(GlobalVariables.NivelRiesgo_obs,Plan.CodNivelRiesgo));
            spinnerArea.setSelection(GlobalVariables.indexOf(GlobalVariables.Area_obs,Plan.CodAreaHSEC));
            spTipoAccion.setSelection(GlobalVariables.indexOf(GlobalVariables.Tipo_Plan,Plan.CodTipoAccion));
            if(Plan.SolicitadoPor!=null)SolicitadoPor.setText(Plan.SolicitadoPor);
            if(Plan.DesPlanAccion!=null)txtTarea.setText(Plan.DesPlanAccion);
            if(!Plan.CodResponsables.isEmpty()){
                String  Responsables[]=Plan.CodResponsables.split(";"),ResponsablesData[]=Plan.Responsables.split(";");
                for(int i=0;i<Responsables.length;i++){
                    String datosper[]= ResponsablesData[i].split(":");
                    ListResponsables.add(new PersonaModel(Responsables[i],datosper[0],"",datosper[1]));
                }

                LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                listView.setLayoutManager(horizontalManager);
                listPersonAdapter = new ListPersonEditAdapter(this, ListResponsables);
                listView.setAdapter(listPersonAdapter);
            }

        }
        else{
            myCalendar = Calendar.getInstance();
            Date actual = myCalendar.getTime();

            //btnFechaInicio.setText(dt.format(actual));
            FechaSolic.setText(formatoRender.format(actual));
            Plan.FechaSolicitud=df.format(actual);

        }

    }
    public void SalvarPlan(View view){

        Plan.CodActiRelacionada= ((Maestro) spActRelacionada.getSelectedItem()).CodTipo;
        Plan.CodNivelRiesgo= ((Maestro)spNivelRiesgo.getSelectedItem()).CodTipo;
        Plan.CodAreaHSEC= ((Maestro)spinnerArea.getSelectedItem()).CodTipo;
        Plan.CodTipoAccion= ((Maestro)spTipoAccion.getSelectedItem()).CodTipo;
        Plan.DesPlanAccion= txtTarea.getText().toString();
        if(Plan.CodAccion.equals("0")){
            Plan.CodAccion="-1";
            Plan.CodEstadoAccion="01";
        }
        int cont=0;
        String ResponsableCod="", ResponsableData="";
        for (PersonaModel p: ListResponsables) {
            if(cont>0){
                ResponsableCod+=";";
                ResponsableData+=";";
            }
            ResponsableCod+=p.CodPersona;
            ResponsableData+=p.Nombres+":"+p.Cargo;
            cont++;
        }
        Plan.Responsables=ResponsableData;
        Plan.CodResponsables=ResponsableCod;
       /* Intent data = new Intent();
        data.setData(Uri.parse(gson.toJson(Plan)));
        setResult(RESULT_OK, data);
        finish();*/

        Intent intent = getIntent();
        intent.putExtra("planaccion",gson.toJson(Plan));

        setResult(RESULT_OK, intent);
        finish();
    }
    public void EscogerFechainicial(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateInicial, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.HOUR, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);

        datePickerDialog.getDatePicker().setMinDate(tempCalendar.getTimeInMillis());
        tempCalendar.set(Calendar.MONTH, (new Date()).getMonth() + 1);
        datePickerDialog.getDatePicker().setMaxDate(tempCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void EscogerFechafinal(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateFinal, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.HOUR, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);

        datePickerDialog.getDatePicker().setMinDate(tempCalendar.getTimeInMillis());
        tempCalendar.set(Calendar.MONTH, (new Date()).getMonth() + 1);
        datePickerDialog.getDatePicker().setMaxDate(tempCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void close(View view){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) {
                String name=data.getStringExtra("nombreP");
                SolicitadoPor.setText(name);
                Plan.SolicitadoPor=name;
                Plan.CodSolicitadoPor=data.getStringExtra("codpersona");
            }
            if (requestCode == 2  && resultCode  == RESULT_OK) {

                ListResponsables.add(new PersonaModel(data.getStringExtra("codpersona"),data.getStringExtra("nombreP"),data.getStringExtra("dni"),data.getStringExtra("cargo")));
                LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                listView.setLayoutManager(horizontalManager);
                listPersonAdapter = new ListPersonEditAdapter(this, ListResponsables);
                listView.setAdapter(listPersonAdapter);
            }
        } catch (Exception ex) {
            Toast.makeText(PlanAccionEdit.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }
}
