package com.pango.hsec.hsec.Facilito;

import android.content.Intent;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Login;
import com.pango.hsec.hsec.MainActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.adapter.ObsFHistorialAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.controller.GetTokenController;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.GetObsFHistorialModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsFHistorialModel;
import com.pango.hsec.hsec.model.ObsFacilitoModel;
import com.pango.hsec.hsec.model.UsuarioModel;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class obsFacilitoDet extends AppCompatActivity implements IActivity {
    private ImageButton close,btn_historial;
    String codObs, codpersona;
    ConstraintLayout contenedor;
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    private RecyclerView gridView;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<GaleriaModel> DataImg;
    ObsFHistorialAdapter ca;
    ArrayList<Maestro> ObsFacilito_estado;
//    ListView list_ObsHistorial;
    RecyclerView list_ObsHistorial;
    CardView cv11;
    int contPublicacion;
    int pos;
    String jsonObsCond="";
    String url;
    TextView tx_codigo,tx_persona,tx_tipo,tx_ubicacion,tx_observacion,tx_accion,tx_fecha,tx_geren,tx_superint,tx_estadofac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_facilito_det);
        gridView = (RecyclerView)  findViewById(R.id.grid);
        list_ObsHistorial=(RecyclerView) findViewById(R.id.list_ObsHistorial);
        btn_historial=(ImageButton) findViewById(R.id.btn_historial);
        DataImg = new ArrayList<>();
        close=(ImageButton) findViewById(R.id.imageButton);
        tx_codigo=(TextView) findViewById(R.id.tx_codigo);
        tx_persona=(TextView) findViewById(R.id.tx_persona);
        tx_tipo=(TextView) findViewById(R.id.tx_tipo);
        tx_estadofac=(TextView) findViewById(R.id.tx_estadofac);
        tx_ubicacion=(TextView) findViewById(R.id.tx_ubicacion);
        tx_observacion=(TextView) findViewById(R.id.tx_observacion);
        tx_accion=(TextView) findViewById(R.id.tx_accion);
        tx_fecha=(TextView) findViewById(R.id.tx_fecha);
        tx_geren=(TextView) findViewById(R.id.tx_geren);
        tx_superint=(TextView) findViewById(R.id.tx_superint);
        contenedor=(ConstraintLayout) findViewById(R.id.contenedor);

        cv11= (CardView) findViewById(R.id.cv11);
        Bundle data1 = this.getIntent().getExtras();

        codObs=data1.getString("codObs",GlobalVariables.codFacilito);
        String addViews=data1.getString("verBoton","-1");
        int AddView= Integer.parseInt(addViews);

        if(codObs.equals("")&&AddView<0){ // read data of notification open
            Intent startingIntent = getIntent();
            if (startingIntent != null) {
                codObs = startingIntent.getStringExtra("codigo"); // Retrieve the id
                codpersona = startingIntent.getStringExtra("codpersona");
                GlobalVariables.NotCodigo=codObs;
                GlobalVariables.NotCodPersona=codpersona;
                Gson gson = new Gson();
                UsuarioModel UserLoged= gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
                if(!codpersona.equals(UserLoged.CodPersona)) {
                    Toast.makeText(this, "Usuario logueado no coincide con usuario a notificar",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(obsFacilitoDet.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }

        if(AddView<2) btn_historial.setVisibility(View.GONE);

        if(AddView<0&&StringUtils.isEmpty(GlobalVariables.token_auth)){ // open app in OBF
                if(Utils.obtener_status(this)){

                    String url_token=GlobalVariables.Url_base+"membership/authenticate";//?"+"username="+user+"&password="+pass+"&domain="+dom;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.accumulate("username",Utils.obtener_usuario(this));
                        jsonObject.accumulate("password",Utils.obtener_pass(this));
                        jsonObject.accumulate("domain","anyaccess");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final ActivityController obj1 = new ActivityController("post-2", url_token, obsFacilitoDet.this,this);
                    obj1.execute(jsonObject.toString());
                }
                else {
                    GlobalVariables.pasnotification=true;
                    Intent intent = new Intent(obsFacilitoDet.this,Login.class);
                    startActivity(intent);
                    finish();
                }
        }
        else loadData();

        btn_historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.flaghistorial=true;
                Intent intent = new Intent(obsFacilitoDet.this,addAtencionFHistorial.class);
                intent.putExtra("codObs",codObs);
                startActivityForResult(intent, 1);
                //v.getContext().startActivity(intent);
        }
        });
    }

    public void loadData(){

        GlobalVariables.codObsHistorial=codObs;
        url= GlobalVariables.Url_base+"ObsFacilito/GetObsFacilitoID/"+codObs;
        final ActivityController obj = new ActivityController("get", url, obsFacilitoDet.this,this);
        obj.execute("1");

        String url1=GlobalVariables.Url_base+"media/GetMultimedia/"+codObs+"-1";
        final ActivityController obj1 = new ActivityController("get", url1, obsFacilitoDet.this,this);
        obj1.execute("2");

        String url2=GlobalVariables.Url_base+"ObsFacilito/GetHistorialAtencion/"+codObs;
        final ActivityController obj2 = new ActivityController("get", url2, obsFacilitoDet.this,this);
        obj2.execute("3");
    }
   /* public boolean obtener_status(){
        SharedPreferences check_status = this.getSharedPreferences("checked", Context.MODE_PRIVATE);
        Boolean status = check_status.getBoolean("check",false);
        return status;
    }
    public String obtener_usuario(){
        SharedPreferences user_login = this.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String usuario = user_login.getString("user","");
        return usuario;
    }
    public String obtener_pass(){
        SharedPreferences user_login = this.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String password = user_login.getString("password","");
        return password;
    }*/

    public void close(View view){
        DataImg.clear();

        finish();
    }
    public void setdata() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, DataImg);
        gridViewAdapter.tacho=true;
        gridView.setAdapter(gridViewAdapter);
        if(DataImg.size()>0) cv11.setVisibility(View.VISIBLE);
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
            if(obsFacilitoModel!=null)
            {
                String gerencia=GlobalVariables.getDescripcion(GlobalVariables.Gerencia,obsFacilitoModel.CodPosicionGer).trim().replace("=","").replace("-","").replace("->","");
                String superint=GlobalVariables.getDescripcion(GlobalVariables.SuperIntendencia,obsFacilitoModel.CodPosicionGer +"."+obsFacilitoModel.CodPosicionSup).trim().replace("=","");
                tx_codigo.setText(String.valueOf(obsFacilitoModel.CodObsFacilito));
                tx_persona.setText(String.valueOf(obsFacilitoModel.Persona));
                if(obsFacilitoModel.Tipo.equals("A")){
                    tx_tipo.setText("Acto");
                }
                if (obsFacilitoModel.Tipo.equals("C")){
                    tx_tipo.setText("Condición");
                }
                tx_estadofac.setText(ObtenerDet(obsFacilitoModel.Estado));
                tx_geren.setText(String.valueOf(gerencia));
                tx_superint.setText(String.valueOf(superint));
                tx_ubicacion.setText(String.valueOf(obsFacilitoModel.UbicacionExacta));
                tx_observacion.setText(String.valueOf(obsFacilitoModel.Observacion));
                tx_accion.setText(String.valueOf(obsFacilitoModel.Accion));
                tx_fecha.setText(String.valueOf(Obtenerfecha(obsFacilitoModel.FecCreacion)));
                contenedor.setVisibility(View.VISIBLE);
            }
            else  Toast.makeText(this, "Ocurrio un error al cargar reporte facilito",    Toast.LENGTH_SHORT).show();
        }
        else if(Tipo.equals("3")){
            Gson gson = new Gson();
            GetObsFHistorialModel getObsFHistorialModel = gson.fromJson(data, GetObsFHistorialModel.class);
            contPublicacion=getObsFHistorialModel.Count;
            GlobalVariables.listaGlobalObsHistorial.clear();
            if(contPublicacion>0){
                GlobalVariables.listaGlobalObsHistorial.clear();
            }

            //if(GlobalVariables.listaGlobalObsHistorial.size()==0)
            GlobalVariables.listaGlobalObsHistorial = getObsFHistorialModel.Data;
          /*  else if(!(GlobalVariables.listaGlobalObsHistorial.get(GlobalVariables.listaGlobalObsHistorial.size()-1).CodObsFacilito.equals(getObsFHistorialModel.Data.get(getObsFHistorialModel.Data.size()-1).CodObsFacilito))){
                    GlobalVariables.listaGlobalObsHistorial.addAll(getObsFHistorialModel.Data);}*/
            LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            list_ObsHistorial.setLayoutManager(horizontalManager);
            ca = new ObsFHistorialAdapter(this,GlobalVariables.listaGlobalObsHistorial);
            list_ObsHistorial.setAdapter(ca);
            ca.notifyDataSetChanged();
        }
        else {
            if(data.contains("-1")) Toast.makeText(this, "Ocurrio un error al eliminar registro.",    Toast.LENGTH_SHORT).show();
            else ca.remove(Integer.parseInt(Tipo)-4);
        }
        setdata();

    }

    @Override
    public void successpost(String data, String Tipo) {
        data= data.substring(1,data.length()-1);
        if(data.length()>40){
            GlobalVariables.token_auth=data;

            GetTokenController objT = new GetTokenController("",obsFacilitoDet.this,null);
            objT.LoadData();
            loadData();
        }
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
    public void DeleteObject(String Url, int index){
        String url= GlobalVariables.Url_base+Url;
        ActivityController obj = new ActivityController("get", url, obsFacilitoDet.this,this);
        obj.execute(""+index);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //listViewAdapter.onActivityResult(requestCode, resultCode, data);
        try{
            Gson gson = new Gson();
            if (requestCode == 1  && resultCode  == RESULT_OK) {
                if(!data.equals(null)){
                    ObsFHistorialModel histlast = gson.fromJson(data.getStringExtra("historial"), ObsFHistorialModel.class);
                    if(!histlast.equals(null)){
                        ca.add(histlast);
                    }
                    else {}
                }
            }
            if (requestCode == 2  && resultCode  == RESULT_OK) {
                if(!data.equals(null)){
                    ObsFHistorialModel histlast = gson.fromJson(data.getStringExtra("historial"), ObsFHistorialModel.class);
                    if(!histlast.equals(null)){
                        int index=data.getIntExtra("index",-1);
                        ca.replace(histlast,index);
                    }else {}
                }
            }
        }
        catch (Exception ex) {
//            if(!data.equals(null)) {
//                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
//            }
        }
    }

}

