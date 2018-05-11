package com.pango.hsec.hsec.Facilito;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.Maestro;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ObsFHistorialAtencionDet extends AppCompatActivity implements IActivity {
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    TextView tx_codigohistorial,tx_atendidopor,tx_fechaatendido,tx_txestado,tx_coments,tx_fechafin;
    private ArrayList<GaleriaModel> DataImg;
    String codObs,persona,fecha,estado,comentario,fechafin;
    ArrayList<Maestro> ObsFacilito_estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_fhistorial_atencion_det);
        tx_codigohistorial=(TextView) findViewById(R.id.tx_codigohistorial);
        tx_atendidopor=(TextView) findViewById(R.id.tx_atendidopor);
        tx_fechaatendido=(TextView) findViewById(R.id.tx_fechaatendido);
        tx_txestado=(TextView) findViewById(R.id.tx_txestado);
        tx_coments=(TextView) findViewById(R.id.tx_coments);
        tx_fechafin=(TextView) findViewById(R.id.tx_fechafin);
        Bundle data1 = this.getIntent().getExtras();

        codObs=data1.getString("CodObsFacilito");
        persona=data1.getString("Persona");
        fecha=data1.getString("Fecha");
        estado=data1.getString("Estado");
        comentario=data1.getString("Comentario");
        fechafin=data1.getString("fechafin");
        setdata();
    }
    public void setdata() {
        tx_codigohistorial.setText(String.valueOf(codObs));
        tx_atendidopor.setText(String.valueOf(persona));
        tx_fechaatendido.setText(String.valueOf(Obtenerfecha(fecha)));
        tx_txestado.setText(ObtenerDet(estado));
        tx_coments.setText(String.valueOf(comentario));
        try
        {
            if(!fechafin.equals(null)){
                tx_fechafin.setText(String.valueOf(Obtenerfecha(fechafin)));
            }
            else if(fechafin.equals(null)){
                tx_fechafin.setText("Fecha no definida");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void close(View view){
        finish();
    }

    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {

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
    public String ObtenerDet(String dataestado){
        String estado="";
        ObsFacilito_estado= new ArrayList<>();
        ObsFacilito_estado.addAll(GlobalVariables.ObsFacilito_estado);
        for(int i=0;i<ObsFacilito_estado.size();i++){
            if(ObsFacilito_estado.get(i).CodTipo.equals(dataestado)){
                estado= ObsFacilito_estado.get(i).Descripcion;
            }
        }
        return estado;
    }
}
