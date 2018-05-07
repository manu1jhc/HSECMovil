package com.pango.hsec.hsec.Facilito;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.ObsFacilitoModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class obsFacilitoDet extends AppCompatActivity implements IActivity {
    private ImageButton close;
    String codObs;
    ConstraintLayout contenedor;
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    private RecyclerView gridView;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<GaleriaModel> DataImg;
    int pos;
    String jsonObsCond="";
    String url;
    TextView tx_codigo,tx_persona,tx_tipo,tx_ubicacion,tx_observacion,tx_accion,tx_fecha,tx_geren,tx_superint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_facilito_det);
        gridView = (RecyclerView)  findViewById(R.id.grid);
        DataImg = new ArrayList<>();
        close=(ImageButton) findViewById(R.id.imageButton);
        tx_codigo=(TextView) findViewById(R.id.tx_codigo);
        tx_persona=(TextView) findViewById(R.id.tx_persona);
        tx_tipo=(TextView) findViewById(R.id.tx_tipo);
        tx_ubicacion=(TextView) findViewById(R.id.tx_ubicacion);
        tx_observacion=(TextView) findViewById(R.id.tx_observacion);
        tx_accion=(TextView) findViewById(R.id.tx_accion);
        tx_fecha=(TextView) findViewById(R.id.tx_fecha);
        tx_geren=(TextView) findViewById(R.id.tx_geren);
        tx_superint=(TextView) findViewById(R.id.tx_superint);
        contenedor=(ConstraintLayout) findViewById(R.id.contenedor);
        Bundle data1 = this.getIntent().getExtras();

        codObs=data1.getString("codObs");
        url= GlobalVariables.Url_base+"ObsFacilito/GetObsFacilitoID/"+codObs;
        final ActivityController obj = new ActivityController("get", url, obsFacilitoDet.this,this);
        obj.execute("1");

        String url1=GlobalVariables.Url_base+"media/GetMultimedia/"+codObs;
        final ActivityController obj1 = new ActivityController("get", url1, obsFacilitoDet.this,this);
        obj1.execute("2");
    }
    public void close(View view){
        DataImg.clear();

        finish();
    }
    public void setdata() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, DataImg);
        gridView.setAdapter(gridViewAdapter);
    }
    @Override
    public void success(String data, String Tipo) {
        if(Tipo.equals("2")){
//            Actives.set(0,1);
            Gson gson = new Gson();
            DataImg=gson.fromJson(data, GetGaleriaModel.class).Data;
        }
        else if (Tipo.equals("1")){
            jsonObsCond =data;
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
            contenedor.setVisibility(View.VISIBLE);
        }
        setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }
    @Override
    public void error(String mensaje, String Tipo) {
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
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

