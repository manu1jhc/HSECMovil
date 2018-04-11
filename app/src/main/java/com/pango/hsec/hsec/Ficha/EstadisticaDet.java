package com.pango.hsec.hsec.Ficha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.AdicionalAdapter;
import com.pango.hsec.hsec.adapter.EstadisticaDetAdapter;
import com.pango.hsec.hsec.adapter.PlandetAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetEstadisticaDetModel;
import com.pango.hsec.hsec.model.GetEstadisticaModel;

public class EstadisticaDet extends AppCompatActivity implements IActivity {

    String categoria,anio,mes,codPersona,descripcion;
    TextView tipo_estadistica;

    String url="";
    ListView list_estadistica;
    Spinner sp_anio, sp_mes;
    String[] busqueda_anio;
    String anio_sel="";
    //String mes_sel="";
    String mes_pos="";
    ImageButton ibclose;
    ImageView btn_buscar_e;
    GetEstadisticaDetModel getEstadisticaDetModel;
    AdicionalAdapter adicionalAdapter;
    TextView tx_ref,tx_autor,tx_fecha,tx_descripcion;

    Button btn_Cerrar;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadistica_det);
        Bundle datos = getIntent().getExtras();
        categoria=datos.getString("categoria");
        anio= datos.getString("anio");
        mes= datos.getString("mes");
        codPersona=datos.getString("codiPersona");
        descripcion=datos.getString("descripcion");

        tipo_estadistica=findViewById(R.id.tipo_estadistica);
        tipo_estadistica.setText(descripcion);
        btn_buscar_e=findViewById(R.id.btn_buscar_e);
        list_estadistica=findViewById(R.id.list_estadistica);
        sp_anio=findViewById(R.id.spinner_anio);
        sp_mes=findViewById(R.id.spinner_mes);


        ArrayAdapter adapterAnio = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, GlobalVariables.busqueda_anio);
        adapterAnio.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_anio.setAdapter(adapterAnio);
        sp_anio.setSelection(find(anio));

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
        sp_mes.setSelection(Integer.parseInt(mes));
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


        if(mes.equals("0")){
            mes="";
        }


        Utils.isActivity=true;
        url= GlobalVariables.Url_base+"FichaPersonal/EstadisticasDetalles?Categoria="+categoria+"&CodPersona="+codPersona+"&anho="+anio+"&mes="+mes;

        //url="http://192.168.1.2/whsec_Servicedmz/api/FichaPersonal/EstadisticasDetalles?Categoria=03&CodPersona=S0020013157&anho=2017";
        final ActivityController obj = new ActivityController("get", url, EstadisticaDet.this,this);
        obj.execute("");





//    TextView tx_ref,tx_autor,tx_fecha,tx_descripcion;

        list_estadistica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(EstadisticaDet.this,EstadisticaAdicional.class);
                intent.putExtra("position",position);
                intent.putExtra("descripcion",descripcion);
                startActivity(intent);

            }
        });


        btn_buscar_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.isActivity=true;
                url= GlobalVariables.Url_base+"FichaPersonal/EstadisticasDetalles?Categoria="+categoria+"&CodPersona="+codPersona+"&anho="+anio_sel+"&mes="+mes_pos;

                //url="http://192.168.1.2/whsec_Servicedmz/api/FichaPersonal/EstadisticasDetalles?Categoria=03&CodPersona=S0020013157&anho=2017";
                final ActivityController obj = new ActivityController("get", url, EstadisticaDet.this,EstadisticaDet.this);
                obj.execute("");
            }
        });





    }

    public void close(View view){
        finish();
    }

    public int find(String anio){
        int pdata=0;
        for (int j=0;j<GlobalVariables.busqueda_anio.length;j++){
            if(anio.equals(GlobalVariables.busqueda_anio[j])){
                pdata=j;
            }
        }
        return pdata;
    }




    @Override
    public void success(String data, String Tipo) {

        Gson gson = new Gson();
        getEstadisticaDetModel=gson.fromJson(data, GetEstadisticaDetModel.class);

        GlobalVariables.dataAdicional=getEstadisticaDetModel.Data;

        tipo_estadistica.setText(descripcion+" ("+getEstadisticaDetModel.Count+")");


        EstadisticaDetAdapter ca = new EstadisticaDetAdapter(this,getEstadisticaDetModel.Data);
        list_estadistica.setAdapter(ca);






    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
