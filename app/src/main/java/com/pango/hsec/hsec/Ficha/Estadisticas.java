package com.pango.hsec.hsec.Ficha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.EstadisticaAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetEstadisticaModel;

import java.util.Calendar;
import java.util.Date;

public class Estadisticas extends AppCompatActivity implements IActivity {
    GetEstadisticaModel getEstadisticaModel;
    String codPersona="";
    String url="";
    ListView list_estadistica;
    Spinner sp_anio, sp_mes;
    //String[] busqueda_anio;
    String anio_sel="";
    String mes_sel="";
    String mes_pos="";
    ImageButton btn_buscar_e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        btn_buscar_e=findViewById(R.id.btn_buscar_e);
        list_estadistica=findViewById(R.id.list_estadistica);
        sp_anio=findViewById(R.id.spinner_anio);
        sp_mes=findViewById(R.id.spinner_mes);

        Bundle datos = getIntent().getExtras();
        codPersona=datos.getString("CodPersona");
        //Date fechaActual = new Date();
        Calendar fecha = Calendar.getInstance();
        int anio = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;

        if(anio<2018) {
            anio=2018;
        }
            //int inc=0;
            GlobalVariables.busqueda_anio = new String[anio - 2014 + 1];

            for (int i = 0; i <= anio - 2014; i++) {
                GlobalVariables.busqueda_anio[i] = String.valueOf(anio - i);
                //inc+=1;
            }


        ArrayAdapter adapterAnio = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, GlobalVariables.busqueda_anio);
        adapterAnio.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_anio.setAdapter(adapterAnio);

        sp_anio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                anio_sel= (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //tipo_filtro=GlobalVariables.busqueda_tipo[0];
            }
        });

        ArrayAdapter adapterMes = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, GlobalVariables.busqueda_mes);
        adapterMes.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_mes.setAdapter(adapterMes);
        sp_mes.setSelection(mes);


        sp_mes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //mes_sel
                if(position!=0) {
                    mes_pos = String.valueOf(position);

                }else{
                    //revisar la posicion

                    mes_pos="";
                    //mes_pos= (String) parent.getItemAtPosition(position);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Utils.isActivity=true;
        url= GlobalVariables.Url_base+"FichaPersonal/Estadisticasgenerales?CodPersona="+codPersona+"&anho="+anio+"&mes="+mes;
        final ActivityController obj = new ActivityController("get", url, Estadisticas.this);
        obj.execute("");



        btn_buscar_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.isActivity=true;
                url= GlobalVariables.Url_base+"FichaPersonal/Estadisticasgenerales?CodPersona="+codPersona+"&anho="+anio_sel+"&mes="+mes_pos;
                final ActivityController obj = new ActivityController("get", url, Estadisticas.this);
                obj.execute("");
            }
        });


        list_estadistica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mes_position;


                if(getEstadisticaModel.Data.get(position).Codigo.equals("00")) {//planes de accion




                }else if(getEstadisticaModel.Data.get(position).Codigo.equals("01")) {//observaciones

                    if(mes_pos.isEmpty()){
                        mes_position="0";
                    }else{
                        mes_position=mes_pos;
                    }

                    Intent intent = new Intent(Estadisticas.this, BusqEstadistica.class);

                    intent.putExtra("anio", anio_sel);
                    intent.putExtra("mes", mes_position);
                    intent.putExtra("codiPersona", codPersona);
                    intent.putExtra("descripcion", getEstadisticaModel.Data.get(position).Descripcion);

                    startActivity(intent);


                }else if(getEstadisticaModel.Data.get(position).Codigo.equals("02")){ //inspecciones




                    if(mes_pos.isEmpty()){
                        mes_position="0";
                    }else{
                        mes_position=mes_pos;
                    }

                    Intent intent = new Intent(Estadisticas.this, BusqEstadistica.class);

                    intent.putExtra("anio", anio_sel);
                    intent.putExtra("mes", mes_position);
                    intent.putExtra("codiPersona", codPersona);
                    intent.putExtra("descripcion", getEstadisticaModel.Data.get(position).Descripcion);

                    startActivity(intent);




                }else {

                    if(mes_pos.isEmpty()){
                        mes_position="0";
                    }else{
                        mes_position=mes_pos;
                    }

                    Intent intent = new Intent(Estadisticas.this, EstadisticaDet.class);
                    intent.putExtra("categoria", getEstadisticaModel.Data.get(position).Codigo);
                    intent.putExtra("anio", anio_sel);
                    intent.putExtra("mes", mes_position);
                    intent.putExtra("codiPersona", codPersona);
                    intent.putExtra("descripcion", getEstadisticaModel.Data.get(position).Descripcion);

                    startActivity(intent);
                }
            }
        });





    }

    public void close(View view){
        finish();
    }

    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        getEstadisticaModel = gson.fromJson(data, GetEstadisticaModel.class);

        EstadisticaAdapter ca = new EstadisticaAdapter(this,getEstadisticaModel.Data);
        list_estadistica.setAdapter(ca);







    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
