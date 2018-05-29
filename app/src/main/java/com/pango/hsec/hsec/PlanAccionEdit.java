package com.pango.hsec.hsec;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.Busquedas.B_personasM;
import com.pango.hsec.hsec.adapter.ListPersonEditAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.model.PlanModel;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PlanAccionEdit extends AppCompatActivity implements IActivity{
    public PlanModel Plan;
    public String StrPlan,DniAvatar;
    public TextView SolicitadoPor,FechaSolic,CodReferencia,txtReferencia;
    Button btnFechaInicio,btnFechaFin;
    ImageButton btnSelectSolicitante, btnaddresponsables;
    Spinner spActRelacionada, spNivelRiesgo, spinnerArea,spTipoAccion;
    DatePickerDialog.OnDateSetListener dateInicial,dateFinal;
    Calendar myCalendar;
    SimpleDateFormat df,dt;
    DateFormat formatoRender;
    EditText txtTarea;
    String title="";
    Gson gson;
    TextView subresponsables,textView23,textView,subsolicitado,subactrelacionada,subnivelriesgo,subareahsec,subtipoaccion,textView24;
    ImageView icon_tipo;
    boolean edit=false;
    private RecyclerView listView;
    private ListPersonEditAdapter listPersonAdapter;
    private ArrayList<PersonaModel> ListResponsables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle datos = this.getIntent().getExtras();
        edit=datos.getBoolean("editplan");
        gson = new Gson();
        StrPlan=datos.getString("Plan");
        Plan = gson.fromJson(StrPlan, PlanModel.class);
        setContentView(R.layout.activity_plan_accion_edit);
        textView=findViewById(R.id.textView);
        icon_tipo=findViewById(R.id.icon_tipo);

        if(edit){
            if(GlobalVariables.ObjectEditable&&Plan.CodTabla.equals("TOBS")){
                title="Editar Obs/Plan de acción";
                icon_tipo.setImageResource(R.drawable.ic_iobservacion);
            }else if(GlobalVariables.ObjectEditable&&Plan.CodTabla.equals("TINS")){
                title="Editar Insp/Obs/Plan de acción";
                icon_tipo.setImageResource(R.drawable.ic_iinspeccion);

            }else {
                title="Editar plan de acción";
                icon_tipo.setImageResource(R.drawable.ic_pendiente);
            }

        }else{
            if(Plan.CodTabla.equals("TOBS")){
                title="Nuevo Obs/Plan de acción";
                icon_tipo.setImageResource(R.drawable.ic_iobservacion);

            }else if(Plan.CodTabla.equals("TINS")){
                title="Nuevo Insp/Obs/Plan de acción";
                icon_tipo.setImageResource(R.drawable.ic_iinspeccion);

            }/*else {
                title="Nuevo plan de acción";
                icon_tipo.setImageResource(R.drawable.ic_pendiente);
            }*/
        }
        textView.setText(title);
        myCalendar = Calendar.getInstance();
        if(!(Plan.CodAccion.contains("-1")||Plan.CodAccion.equals("0"))) {
            CardView cv_edit = (CardView) findViewById(R.id.plan_edit);
            cv_edit.setVisibility(View.VISIBLE);

            TextView CodAccion = (TextView)findViewById(R.id.txt_codaccion);
            TextView CodEstado = (TextView)findViewById(R.id.txt_estado);

            CodReferencia = (TextView)findViewById(R.id.txt_codreferencia);
            txtReferencia = (TextView)findViewById(R.id.txt_referencia);
            txtReferencia.setText(GlobalVariables.getDescripcion(GlobalVariables.Referencia_Plan,Plan.CodReferencia));
            CodReferencia.setText(Plan.NroDocReferencia);
            CodAccion.setText(Plan.CodAccion);
            CodEstado.setText(GlobalVariables.getDescripcion(GlobalVariables.Estado_Plan,Plan.CodEstadoAccion));
        }
        subresponsables=findViewById(R.id.subresponsables);
        String mensaje="<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Responsables";
        subresponsables.setText(Html.fromHtml(mensaje));
        textView23=findViewById(R.id.textView23);
        textView23.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Fecha Comprometida"));
        subsolicitado=findViewById(R.id.subsolicitado);
        subsolicitado.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Solicitado Por:"));
        subactrelacionada=findViewById(R.id.subactrelacionada);
        subactrelacionada.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Actividad Relacionada:"));
        subnivelriesgo=findViewById(R.id.subnivelriesgo);
        subnivelriesgo.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Nivel Riesgo:"));
        subareahsec=findViewById(R.id.subareahsec);
        subareahsec.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Área HSEC:"));
        subtipoaccion=findViewById(R.id.subtipoaccion);
        subtipoaccion.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Tipo Accion:"));
        textView24=findViewById(R.id.textView24);
        textView24.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Tarea:"));

        FechaSolic = (TextView)findViewById(R.id.txt_fecha);
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dt = new SimpleDateFormat("dd 'de' MMMM");
        SolicitadoPor = (TextView)findViewById(R.id.txt_solicitado);


//Spinners
        spActRelacionada = (Spinner) findViewById(R.id.sp_actrelacionada);
        spNivelRiesgo = (Spinner) findViewById(R.id.sp_nivelriesgo);
        spinnerArea = (Spinner) findViewById(R.id.sp_areahsec);
        spTipoAccion = (Spinner) findViewById(R.id.sp_tipoaccion);

        ArrayAdapter adapterTipoObs = new ArrayAdapter(this,R.layout.custom_spinner_item,GlobalVariables.Actividad_obs);
        adapterTipoObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spActRelacionada.setAdapter(adapterTipoObs);

        ArrayAdapter adapterNivelR = new ArrayAdapter(this,R.layout.custom_spinner_item,GlobalVariables.NivelRiesgo_obs);
        adapterTipoObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spNivelRiesgo.setAdapter(adapterNivelR);

        ArrayAdapter adapterArea = new ArrayAdapter(this,R.layout.custom_spinner_item,GlobalVariables.Area_obs);
        adapterTipoObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerArea.setAdapter(adapterArea);

        ArrayAdapter adapterTipoacc = new ArrayAdapter(this,R.layout.custom_spinner_item,GlobalVariables.Tipo_Plan);
        adapterTipoObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spTipoAccion.setAdapter(adapterTipoacc);

//buttons
        btnFechaInicio=(Button) findViewById(R.id.btn_fecha);
        btnFechaFin=(Button) findViewById(R.id.btn_fechafin);
        btnSelectSolicitante=(ImageButton) findViewById(R.id.btn_solcitadopor);
        btnaddresponsables=(ImageButton) findViewById(R.id.add_responsables);
        btnaddresponsables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanAccionEdit.this, B_personasM.class);
                //itle
                intent.putExtra("titulo",title+"/Responsables");
                startActivityForResult(intent , 2);
            }
        });
        btnSelectSolicitante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanAccionEdit.this, B_personas.class);
                intent.putExtra("title",title+"/Solicitante");
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

        formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        if(!Plan.CodAccion.equals("0")){ //Edit plan

            if(edit){
                String url= GlobalVariables.Url_base+"PlanAccion/Get/"+Plan.CodAccion;
                final ActivityController obj = new ActivityController("get", url, PlanAccionEdit.this,PlanAccionEdit.this);
                obj.execute("");
            }
            setData();

        }
        else{

            Date actual = myCalendar.getTime();

            //btnFechaInicio.setText(dt.format(actual));
            //FechaSolic.setText(formatoRender.format(actual));
            Plan.FechaSolicitud=df.format(actual);

            /*LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(horizontalManager);
            listPersonAdapter = new ListPersonEditAdapter(this, ListResponsables);
            listView.setAdapter(listPersonAdapter);*/

            setData();
        }
    }
    public void setData(){

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
        if(!StringUtils.isEmpty(Plan.CodResponsables)){
            String  Responsables[]=Plan.CodResponsables.split(";"),ResponsablesData[]=Plan.Responsables.split(";");
            for(int i=0;i<Responsables.length;i++){
                String datosper[]= ResponsablesData[i].split(":");
                ListResponsables.add(new PersonaModel(Responsables[i],datosper[0],"",datosper[1]));
            }
        }
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(horizontalManager);
        listPersonAdapter = new ListPersonEditAdapter(this, ListResponsables);
        listView.setAdapter(listPersonAdapter);
    }


    public boolean ValifarFormulario(View view){
        String ErrorForm="";
        if(StringUtils.isEmpty(Plan.CodSolicitadoPor)) {ErrorForm+="*Solicitante";}
        else if(StringUtils.isEmpty(Plan.CodActiRelacionada)) {ErrorForm+="*Actividad Relacionada";}
        else if(StringUtils.isEmpty(Plan.CodNivelRiesgo)) {ErrorForm+="*Nivel de riesgo";}
        else if(StringUtils.isEmpty(Plan.CodAreaHSEC)) {ErrorForm+="*Area HSEC";}
        else if(StringUtils.isEmpty(Plan.CodTipoAccion)) {ErrorForm+="*Tipo de accion";}
        else if(StringUtils.isEmpty(Plan.FecComprometidaInicial)) {ErrorForm+=" *Fecha Inicial";}
        else if(StringUtils.isEmpty(Plan.FecComprometidaFinal)) {ErrorForm+=" *Fecha Final";}
        else if(StringUtils.isEmpty(Plan.DesPlanAccion)) {ErrorForm+=" *Tarea";}
        else if(StringUtils.isEmpty(Plan.CodResponsables)) {ErrorForm+=" *Responsables";}

        if(ErrorForm.isEmpty()) return true;
        else{

/*
            String Mensaje="Complete los siguientes campos obligatorios:\n"+ErrorForm;
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Datos incorrectos");
            alertDialog.setIcon(R.drawable.warninicon);
            alertDialog.setMessage(Mensaje);

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
*/


            Snackbar.make(view, "El campo "+ErrorForm+" no puede estar vacío", Snackbar.LENGTH_LONG).show();


            //showSnackBar("El campo "+ErrorForm+" no puede estar vacío",view);



            return false;
        }
    }

/*
    public void showSnackBar(String txt,View view2){
        Snackbar snackbar = null;

        final Snackbar finalSnackbar = snackbar;
        snackbar=Snackbar.make(view2,txt,Snackbar.LENGTH_LONG);

        View view=snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
    }
*/

    public void SalvarPlan(View view){

        Utils.closeSoftKeyBoard(this);
        Plan.CodActiRelacionada= ((Maestro) spActRelacionada.getSelectedItem()).CodTipo;
        Plan.CodNivelRiesgo= ((Maestro)spNivelRiesgo.getSelectedItem()).CodTipo;
        Plan.CodAreaHSEC= ((Maestro)spinnerArea.getSelectedItem()).CodTipo;
        Plan.CodTipoAccion= ((Maestro)spTipoAccion.getSelectedItem()).CodTipo;
        Plan.DesPlanAccion= txtTarea.getText().toString();
        if(Plan.CodAccion.equals("0")){
            int pos=1;
            if(GlobalVariables.Planes.size()>0)
                pos =Math.abs(Integer.parseInt(GlobalVariables.Planes.get(GlobalVariables.Planes.size()-1).CodAccion))+1;
            Plan.CodAccion="-"+pos;
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
       String StrNewPlan=gson.toJson(Plan);
       if(StrPlan.equals(StrNewPlan)){
           Toast.makeText(this,"No se detectaron cambios",Toast.LENGTH_SHORT).show();
           finish();
       }
       else{
           if(ValifarFormulario(view)){
               if(edit||Plan.NroDocReferencia!=null){
                   String url= GlobalVariables.Url_base+"PlanAccion/Post";
                   final ActivityController obj = new ActivityController("post", url, PlanAccionEdit.this,PlanAccionEdit.this);
                   obj.execute(gson.toJson(Plan));
               }
               else {
                   Intent intent = getIntent();
                   intent.putExtra("planaccion",gson.toJson(Plan));

                   setResult(RESULT_OK, intent);
                   finish();
               }
           }
       }
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
    public void FinishSave(boolean pass){

            String Mensaje="Se guardo los datos correctamente";
            String Titulo="Desea Finalizar?";
            int icon=R.drawable.confirmicon;
            if(!pass)
            {
                icon=R.drawable.erroricon;
                Mensaje="Ocurrio un error al intentar guardar los datos.";
                Titulo="Ocurrio un error";
            }
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle(Titulo);
            alertDialog.setIcon(icon);
            alertDialog.setMessage(Mensaje);

            if(pass)
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(pass){
                        if(edit)Plan.CodSolicitadoPor=DniAvatar;
                        Intent intent = getIntent();
                        intent.putExtra("planaccion",gson.toJson(Plan));

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });
            alertDialog.show();


    }
    public void close(View view){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) { // seleccion de solicitado por
                String name=data.getStringExtra("nombreP");
                DniAvatar=data.getStringExtra("dni");
                SolicitadoPor.setText(name);
                Plan.SolicitadoPor=name;
                Plan.CodSolicitadoPor=data.getStringExtra("codpersona");
            }
            if (requestCode == 2  && resultCode  == RESULT_OK) { //agregar responsables
                for(PersonaModel item: GlobalVariables.lista_Personas)
                    if(item.Check) listPersonAdapter.add(item);
                listPersonAdapter.notifyDataSetChanged();
                //listPersonAdapter.add(new PersonaModel(data.getStringExtra("codpersona"),data.getStringExtra("nombreP"),data.getStringExtra("dni"),data.getStringExtra("cargo")));
            }
        } catch (Exception ex) {
            Toast.makeText(PlanAccionEdit.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        DniAvatar=Plan.CodSolicitadoPor;
        Plan = gson.fromJson(data, PlanModel.class);
        Plan.Editable="1";
        txtReferencia.setText(GlobalVariables.getDescripcion(GlobalVariables.Referencia_Plan,Plan.CodReferencia));
        CodReferencia.setText(Plan.NroDocReferencia);
        StrPlan=gson.toJson(Plan);
        setData();
    }

    @Override
    public void successpost(String data, String Tipo) {
        if(data.contains("-1")) FinishSave(false);
            //Toast.makeText(this,"Ocurrio un error interno al intentar guardar cambios",Toast.LENGTH_SHORT).show();
        else{
            Plan.CodAccion = data.substring(1, data.length() - 1);
            FinishSave(true);
        }
    }

    @Override
    public void error(String mensaje, String Tipo) {
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }

    public void sel_fec_inicial(View view) {
        EscogerFechainicial(this.findViewById(android.R.id.content));
    }

    public void sel_fec_final(View view) {
        EscogerFechafinal(this.findViewById(android.R.id.content));
    }
}
