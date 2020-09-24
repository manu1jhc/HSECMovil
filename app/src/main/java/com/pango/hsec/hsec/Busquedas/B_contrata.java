package com.pango.hsec.hsec.Busquedas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.ContrataAdapter;
import com.pango.hsec.hsec.model.Maestro;

import java.util.ArrayList;

public class B_contrata extends AppCompatActivity {
EditText id_codigo, id_razon;
Button btn_busqueda;
ListView listView;
ArrayList<Maestro> contrata_datos=new ArrayList<>();
ContrataAdapter contrataAdapter;
TextView tx_buscarc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_contrata);


        id_codigo=(EditText) findViewById(R.id.id_codigo);
        id_razon=(EditText) findViewById(R.id.id_razon);
        btn_busqueda=(Button) findViewById(R.id.btn_busqueda);
        listView=(ListView) findViewById(R.id.listView);
        tx_buscarc=findViewById(R.id.tx_buscarc);


        btn_busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String codigo=String.valueOf(id_codigo.getText()).trim();
            String razon_social= String.valueOf(id_razon.getText()).trim();
            String comp_codigo;
            contrata_datos.clear();
                for (Maestro item: GlobalVariables.Contrata
                        ) {

                    if(codigo.equals("")&&razon_social.equals("")) {
                        contrata_datos.add(item);
                    }else if(codigo.equals("")&&item.Descripcion.toLowerCase().contains(razon_social.toLowerCase())){
                        contrata_datos.add(item);
                    }else if(razon_social.equals("")&&item.CodTipo.equals(codigo)){
                        contrata_datos.add(item);
                    }else if(String.valueOf(Integer.parseInt(item.CodTipo)).equals(codigo)&&item.Descripcion.toLowerCase().contains(razon_social.toLowerCase())){
                        contrata_datos.add(item);
                    }
                }

                lista_contrata(contrata_datos);

                if(contrata_datos.size()==0){
                    tx_buscarc.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }else{
                    listView.setVisibility(View.VISIBLE);
                    tx_buscarc.setVisibility(View.GONE);

                }
                Utils.closeSoftKeyBoard(B_contrata.this);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String codContrata=contrata_datos.get(position).CodTipo;
                String desContrata=contrata_datos.get(position).Descripcion;

                /*Intent intent = new Intent(B_personas.this, B_observaciones.class);
                intent.putExtra("nombreP",nombre);
                intent.putExtra("codpersona",CodPersona);

                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                startActivity(intent);*/

                Intent intent = getIntent();
                intent.putExtra("codContrata",codContrata);
                intent.putExtra("desContrata",desContrata);
                intent.putExtra("tipo","contrata");
                setResult(RESULT_OK, intent);
                finish();
            }
        });








    }

    public void close(View view){
        Utils.closeSoftKeyBoard(B_contrata.this);
        finish();
    }

    public void lista_contrata(ArrayList<Maestro> contrata_datos){

        contrataAdapter = new ContrataAdapter(this,contrata_datos);
        listView.setAdapter(contrataAdapter);

    }


}
