package com.pango.hsec.hsec.Facilito;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsFacilitoModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class obsfacilitoAprobar extends AppCompatActivity implements IActivity {
    private ImageButton close;
    ArrayList<Maestro> ObsFacilito_tiempo;
    String codObs;
    Spinner spinner_tipobs;
    String spinner_Cod;
    Button btn_Rechazar,btn_Aprobar;
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    int pos;
    String jsonObsCond="",aprobar="A",rechazar="R";
    String url;
    TextView tx_codigo,tx_persona,tx_tipo,tx_ubicacion,tx_observacion,tx_accion,tx_fecha,tx_geren,tx_superint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data1 = this.getIntent().getExtras();
        codObs=data1.getString("codObs");
        setContentView(R.layout.activity_obsfacilito_aprobar);
        tx_codigo=(TextView) findViewById(R.id.tx_codigo);
        tx_persona=(TextView) findViewById(R.id.tx_persona);
        tx_tipo=(TextView) findViewById(R.id.tx_tipo);
        tx_ubicacion=(TextView) findViewById(R.id.tx_ubicacion);
        tx_observacion=(TextView) findViewById(R.id.tx_observacion);
        tx_accion=(TextView) findViewById(R.id.tx_accion);
        tx_fecha=(TextView) findViewById(R.id.tx_fecha);
        tx_geren=(TextView) findViewById(R.id.tx_geren);
        tx_superint=(TextView) findViewById(R.id.tx_superint);
        btn_Rechazar=(Button) findViewById(R.id.btn_Rechazar);
        btn_Aprobar=(Button) findViewById(R.id.btn_Aprobar);
        spinner_tipobs = (Spinner) findViewById(R.id.spinner_tipobs);
        ObsFacilito_tiempo= new ArrayList<>();
        ObsFacilito_tiempo.addAll(GlobalVariables.ObsFacilito_tiempo);
        ArrayAdapter adapterNivelR = new ArrayAdapter(this,android.R.layout.simple_spinner_item, ObsFacilito_tiempo);
        adapterNivelR.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_tipobs.setAdapter(adapterNivelR);
        spinner_tipobs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                spinner_Cod = ObsFacilito_tiempo.get(position).CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_Cod="";
            }
        });
        url= GlobalVariables.Url_base+"ObsFacilito/GetObsFacilitoID/"+codObs;
        final ActivityController obj = new ActivityController("get", url, obsfacilitoAprobar.this,this);
        obj.execute("");
        //enviar reporte de observacion
        btn_Rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Utils.observacionFacilitoModel.CodObsFacilito=codObs;
                    Utils.observacionFacilitoModel.Estado=String.valueOf(rechazar);
                    Utils.observacionFacilitoModel.Plazo=Integer.parseInt(spinner_Cod);
                    String json = "";
                    Gson gson = new Gson();
                    json = gson.toJson(Utils.observacionFacilitoModel);
                    ObsFacilitoModel Obs=new ObsFacilitoModel();
//                    Obs.CodObsFacilito=codObs;
//                    Obs.Estado=String.valueOf(rechazar);
//                    Obs.Plazo=Integer.parseInt(spinner_Cod);
                    String url= GlobalVariables.Url_base+"ObsFacilito/AprobarObsFaci";
                    final ActivityController obj = new ActivityController("post", url, obsfacilitoAprobar.this,obsfacilitoAprobar.this);
                    obj.execute(json);
                    Toast.makeText(obsfacilitoAprobar.this,"La Observacion fue rechazada",Toast.LENGTH_LONG).show();
                    finish();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(obsfacilitoAprobar.this,"La Observacion No se puede rechazar",Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_Aprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Utils.observacionFacilitoModel.CodObsFacilito=codObs;
                    Utils.observacionFacilitoModel.Estado=String.valueOf(aprobar);
                    Utils.observacionFacilitoModel.Plazo=Integer.parseInt(spinner_Cod);
                    String json = "";
                    Gson gson = new Gson();
                    json = gson.toJson(Utils.observacionFacilitoModel);
                    ObsFacilitoModel Obs=new ObsFacilitoModel();
//                    Obs.CodObsFacilito=codObs;
//                    Obs.Estado=String.valueOf(aprobar);
//                    Obs.Plazo=Integer.parseInt(spinner_Cod);
                    String url= GlobalVariables.Url_base+"ObsFacilito/AprobarObsFaci";
                    final ActivityController obj = new ActivityController("post", url, obsfacilitoAprobar.this,obsfacilitoAprobar.this);
                    obj.execute(json);
                    Toast.makeText(obsfacilitoAprobar.this,"La Observacion se aprobó con éxito",Toast.LENGTH_LONG).show();
                    finish();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(obsfacilitoAprobar.this,"La Observacion No se aprobar ",Toast.LENGTH_LONG).show();
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
        ObsFacilitoModel obsFacilitoModel = gson.fromJson(data, ObsFacilitoModel.class);
        String gerencia=GlobalVariables.getDescripcion(GlobalVariables.Gerencia,obsFacilitoModel.CodPosicionGer).trim().replace("=","");
        String superint=GlobalVariables.getDescripcion(GlobalVariables.SuperIntendencia,obsFacilitoModel.CodPosicionGer +"."+obsFacilitoModel.CodPosicionSup).trim().replace("=","");
        tx_codigo.setText(String.valueOf(obsFacilitoModel.CodObsFacilito));
        tx_persona.setText(String.valueOf(obsFacilitoModel.Persona));
        tx_tipo.setText(String.valueOf(obsFacilitoModel.Tipo));
        tx_geren.setText(String.valueOf(gerencia));
        tx_superint.setText(String.valueOf(superint));
        tx_ubicacion.setText(String.valueOf(obsFacilitoModel.UbicacionExacta));
        tx_observacion.setText(String.valueOf(obsFacilitoModel.Observacion));
        tx_accion.setText(String.valueOf(obsFacilitoModel.Accion));
        tx_fecha.setText(String.valueOf(Obtenerfecha(obsFacilitoModel.FecCreacion)));
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
    public String Obtenerfecha(String tempcom_fecha) {

        String fecha="";
        try {
            fecha=formatoRender.format(formatoInicial.parse(tempcom_fecha));
        } catch (ParseException e) {
            e.printStackTrace();
            fecha=tempcom_fecha;
        }

        return fecha;

    }
}
