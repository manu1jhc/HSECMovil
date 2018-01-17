package com.pango.hsec.hsec.Busquedas;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Observaciones.FragmentComent;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.BuscarPersonaAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.GetPersonaModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.PlanModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class B_personas extends AppCompatActivity implements IActivity {
    ArrayList<Maestro> gerenciadata;
    ArrayList<Maestro> superintdata;

    Spinner spinnerGerencia,spinnerSuperInt;
    TextView tx_b_persona;
    ConstraintLayout const_persona;
    Button btn_busqueda;
    String gerencia,superint,filtro;
    EditText id_apellidos,id_nombre,id_dni;
    String url="";
    ArrayAdapter adapterGerencia,adapterSuperInt;
    int contPublicacion;
    ListView List_personas;
    GetPersonaModel getPersonaModel;

    //int first_spinner = 0, first_spinner_counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_personas);
        tx_b_persona=(TextView) findViewById(R.id.tx_b_persona);
        const_persona=(ConstraintLayout) findViewById(R.id.const_persona);
        btn_busqueda=(Button) findViewById(R.id.btn_busqueda);
        id_apellidos=(EditText) findViewById(R.id.id_apellidos);
        id_nombre=(EditText) findViewById(R.id.id_nombre);
        id_dni=(EditText) findViewById(R.id.id_dni);
        List_personas=findViewById(R.id.listView);


        spinnerGerencia=(Spinner) findViewById(R.id.spinner_gerencia);
        spinnerSuperInt=(Spinner) findViewById(R.id.spinner_superint);
        //spinnerGerencia.setOnItemSelectedListener(this);
        gerenciadata= new ArrayList<>();
        gerenciadata.add(new Maestro("","-  Seleccione  -"));
        gerenciadata.addAll(GlobalVariables.Gerencia);

        superintdata=new ArrayList<>();
        superintdata.add(new Maestro("","-  Seleccione  -"));
//        superintdata.addAll(GlobalVariables.SuperIntendencia);


        adapterGerencia = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, gerenciadata);
        adapterGerencia.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

        adapterSuperInt = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, superintdata);
        adapterSuperInt.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSuperInt.setAdapter(adapterSuperInt);


        spinnerGerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               /* Maestro Geren = (Maestro) ( (Spinner) view.findViewById(R.id.spinner_gerencia) ).getSelectedItem();
                gerencia=Geren.CodTipo;*/
                superint="";
                gerencia = gerenciadata.get(position).CodTipo;
                superintdata.clear();
                for (Maestro item: GlobalVariables.loadSuperInt(gerencia)
                        ) {
                    superintdata.add(item);
                }
                adapterSuperInt.notifyDataSetChanged();
                spinnerSuperInt.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gerencia="";
            }
        });


        spinnerSuperInt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(position!=0) {
                    superint = superintdata.get(position).CodTipo.split("\\.")[1];
                }else{
                    superint="";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                superint="";
            }
        });



        tx_b_persona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(const_persona.getVisibility()==View.GONE){
                    const_persona.setVisibility(View.VISIBLE);
                }else{
                    const_persona.setVisibility(View.GONE);

                }

            }
        });
        //busqueda
        String url2;
        btn_busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtro= String.valueOf(String.valueOf(id_nombre.getText())+"@"+id_apellidos.getText())+"@"+String.valueOf(id_dni.getText())
                +"@"+gerencia+"@"+superint;

                url= GlobalVariables.Url_base+"Usuario/FiltroPersona/"+filtro+"/1/5";
                //url="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/Usuario/FiltroPersona/@@@@/1/5";

                final ActivityController obj = new ActivityController("get", url, B_personas.this);
                obj.execute("");


            }
        });




    }
    public void close(View view){
        finish();
    }

    @Override
    public void success(String data,String Tipo) {
        Gson gson = new Gson();
        getPersonaModel = gson.fromJson(data, GetPersonaModel.class);
        contPublicacion=getPersonaModel.Count;

        BuscarPersonaAdapter ca = new BuscarPersonaAdapter(this,getPersonaModel.Data);
        List_personas.setAdapter(ca);

        List_personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String nombre=getPersonaModel.Data.get(position).Nombres;
                String CodPersona=getPersonaModel.Data.get(position).CodPersona;
                Intent intent = new Intent(B_personas.this, B_observaciones.class);
                intent.putExtra("nombreP",nombre);
                intent.putExtra("codpersona",CodPersona);

                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                startActivity(intent);


            }
        });





    }

    @Override
    public void successpost(String data,String Tipo) {

    }

    @Override
    public void error(String mensaje,String Tipo) {

    }
}
