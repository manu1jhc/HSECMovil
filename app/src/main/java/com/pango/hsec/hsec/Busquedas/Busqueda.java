package com.pango.hsec.hsec.Busquedas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;

public class Busqueda extends AppCompatActivity {
    Spinner sp_busqueda;
    String tipo_filtro="";
    Button btn_filtro;
    ImageButton close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        close=findViewById(R.id.imageButton);

        GlobalVariables.LoadData();

        sp_busqueda=(Spinner) findViewById(R.id.sp_busqueda);

        ArrayAdapter adapterBusObs = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, GlobalVariables.busqueda_tipo);
        adapterBusObs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_busqueda.setAdapter(adapterBusObs);

        sp_busqueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipo_filtro= (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //tipo_filtro=GlobalVariables.busqueda_tipo[0];
            }
        });

       btn_filtro=(Button) findViewById(R.id.btn_filtro);
       btn_filtro.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(tipo_filtro.equals(GlobalVariables.busqueda_tipo[0])) {
                   Intent intent = new Intent(Busqueda.this, B_observaciones.class);
                   startActivity(intent);
               }
           }
       });

    }
    public void close(View view){
        finish();
    }



}
