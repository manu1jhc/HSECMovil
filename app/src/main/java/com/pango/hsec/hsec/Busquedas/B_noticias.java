package com.pango.hsec.hsec.Busquedas;

import android.app.DatePickerDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class B_noticias extends AppCompatActivity {
    Calendar myCalendar,myCalendar2;
    public static final int REQUEST_CODE = 1;

    DatePickerDialog.OnDateSetListener date, datefin;
    Button btnFechaInicio,btnFechaFin;
    ImageButton btnbuscar;
    boolean escogioFecha;
    String fechaEscogida;
    EditText codNoticia,titulo;
    ImageButton btn_buscar_p;
    TextView id_persona;
    String fecha_inicio="-";
    String fecha_fin="-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_noticias);

        codNoticia=(EditText) findViewById(R.id.id_CodNoticia);
        titulo=(EditText) findViewById(R.id.id_titulo);
        btnFechaInicio=(Button) findViewById(R.id.btn_fecha_desde);
        btnFechaFin=(Button) findViewById(R.id.btn_fecha_fin);
        btnbuscar=(ImageButton) findViewById(R.id.btn_buscar);
        id_persona=(TextView) findViewById(R.id.id_persona);
        btn_buscar_p=(ImageButton) findViewById(R.id.btn_buscar_p);







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
                Utils.noticiasModel.Fecha= String.valueOf(fecha_envio.format(actual));

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

                Utils.noticiasModel.Fecha2= String.valueOf(fecha_envio.format(actual));

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
                Intent intent = new Intent(B_noticias.this, B_personas.class);
                intent.putExtra("title","Noticias/Filtro/Autor");
                startActivityForResult(intent , REQUEST_CODE);
            }
        });


        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.noticiasModel.CodNoticia= String.valueOf(codNoticia.getText()).trim();
                Utils.noticiasModel.Titulo= String.valueOf(titulo.getText()).trim();

                //inspeccionModel=Utils.noticiasModel;
                Intent intent = getIntent();
                intent.putExtra("Tipo_Busqueda",3);
                //intent.putExtra("codpersona",CodPersona);
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



                    String nombre = data.getStringExtra("nombreP");
                    String codpersona = data.getStringExtra("codpersona");
                    id_persona.setText(nombre);
                    Utils.noticiasModel.Autor = codpersona;







            }
        } catch (Exception ex) {
            Toast.makeText(B_noticias.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

}
