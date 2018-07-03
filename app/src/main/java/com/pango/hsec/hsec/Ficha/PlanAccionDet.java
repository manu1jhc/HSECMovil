package com.pango.hsec.hsec.Ficha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_observaciones;
import com.pango.hsec.hsec.Busquedas.Busqueda;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.Observaciones.FragmentPlan;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.AccionMejoraAdapter;
import com.pango.hsec.hsec.adapter.PersonaAdapter;
import com.pango.hsec.hsec.adapter.PlandetAdapter;
import com.pango.hsec.hsec.adapter.RespAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.AccionMejoraMinModel;
import com.pango.hsec.hsec.model.AccionMejoraModel;
import com.pango.hsec.hsec.model.GetAccionMejoraModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.GetPlanModel;
import com.pango.hsec.hsec.model.PlanModel;
import com.pango.hsec.hsec.model.UsuarioModel;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import layout.FragmentObservaciones;

public class PlanAccionDet extends AppCompatActivity implements IActivity {
    public static final int REQUEST_CODE = 1;
    String codAccion;
    String url;
    String jsonPlan="";
    //String jsonPlanEnviar="";

    RespAdapter respAdapter;
    String codObsIns="";

    Button ver_obs_insp;
    boolean verBoton;
    LinearLayout ll_levantar;
    ImageButton btn_agregar;

    PlandetAdapter plandetAdapter;
    AccionMejoraAdapter accionMejoraAdapter;
    ArrayList<AccionMejoraMinModel> ListMejoras;
    PlanModel planModel;
    GetAccionMejoraModel getAccionMejoraModel;
    RecyclerView rec_mejora;
    int proviene;
    ImageView imageView20;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_accion_det);
        ver_obs_insp=findViewById(R.id.ver_obs_insp);
        ll_levantar=findViewById(R.id.ll_levantar);
        btn_agregar=findViewById(R.id.btn_agregar);
        rec_mejora = (RecyclerView) findViewById(R.id.rec_lev_obs);
        imageView20=findViewById(R.id.imageView20);
        textView=findViewById(R.id.textView);
        Bundle datos = this.getIntent().getExtras();
        codAccion=datos.getString("codAccion");
        jsonPlan=datos.getString("jsonPlan");
        verBoton=datos.getBoolean("verBoton");


        proviene=datos.getInt("proviene");

        if(proviene==1){//proviene de una observacion
            imageView20.setImageResource(R.drawable.ic_iobservacion);
            textView.setText("Obs/Plan de acción");
        }else if(proviene==2)// proviene de una Inspeccion
        {
            imageView20.setImageResource(R.drawable.ic_iinspeccion);
            textView.setText("Insp/Obs/Plan de acción");
        }else{//proviene del listado de plan de accion
            imageView20.setImageResource(R.drawable.ic_4_registro);
            textView.setText("Plan de acción");
        }


        ListMejoras= new ArrayList<>();
        if(jsonPlan.isEmpty()) {
            //GlobalVariables.istabs=true;
            url= GlobalVariables.Url_base+"PlanAccion/Get/"+codAccion;
            final ActivityController obj = new ActivityController("get", url, PlanAccionDet.this,this);
            obj.execute("1");
        }else{

            success(jsonPlan,"1");
        }

        ver_obs_insp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(planModel.NroDocReferencia.substring(0,3).equals("OBS")){
                    String tipoObs=planModel.CodTipoObs;

                    Intent intent = new Intent(PlanAccionDet.this, ActMuroDet.class);
                    intent.putExtra("codObs",planModel.NroDocReferencia);
                    intent.putExtra("posTab",0);
                    intent.putExtra("tipoObs",tipoObs);
                    startActivity(intent);

                }else if(planModel.NroDocReferencia.substring(0,3).equals("INS")){
                    Intent intent = new Intent(PlanAccionDet.this, ActInspeccionDet.class);
                    intent.putExtra("codObs",planModel.NroDocReferencia);
                    intent.putExtra("posTab",0);
                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                    startActivity(intent);
                }
            }
        });

        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson= new Gson();
                AccionMejoraModel temp= new AccionMejoraModel();
                temp.CodAccion=planModel.CodAccion;
                temp.Correlativo="-1";
                temp.PorcentajeAvance="0";
                temp.Descripcion="";
                //temp.Files= new GetGaleriaModel();
                temp.CodResponsable=planModel.CodResponsables.split(";").length>1?"":planModel.CodResponsables;
                Intent intent=new Intent(PlanAccionDet.this, AddRegistroAvance.class);
                intent.putExtra("AccionMejora",  gson.toJson(temp));
                intent.putExtra("Edit", false);
                intent.putExtra("CodResponsable",planModel.CodResponsables);
                intent.putExtra("Responsable",planModel.Responsables);
                startActivityForResult(intent , REQUEST_CODE);
            }
        });
    }

    public void close(View view){
        finish();
    }

    @Override
    public void success(String data, String Tipo) {
        //jsonPlanEnviar=data;
        Gson gson = new Gson();
        if(Tipo=="1"){
        planModel = gson.fromJson(data, PlanModel.class);
        //ListView list_popup=(ListView) findViewById(R.id.list_plan);
        //plandetAdapter = new PlandetAdapter(this,planModel);
        //list_popup.setAdapter(plandetAdapter);
        final RecyclerView list_popup = (RecyclerView) findViewById(R.id.list_plan);
        list_popup.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list_popup.setLayoutManager(llm);
        list_popup.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        plandetAdapter = new PlandetAdapter(this,planModel);
        list_popup.setAdapter(plandetAdapter);
        //codObsIns=planModel.NroDocReferencia;
       if(!StringUtils.isEmpty(planModel.CodResponsables)){
           String[]responsable=planModel.Responsables.split(";");

           final RecyclerView rec_responsable = (RecyclerView) findViewById(R.id.rec_responsable);
           rec_responsable.setHasFixedSize(true);
           LinearLayoutManager llm2 = new LinearLayoutManager(this);
           llm2.setOrientation(LinearLayoutManager.VERTICAL);
           rec_responsable.setLayoutManager(llm2);
           rec_responsable.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

           respAdapter = new RespAdapter(this,responsable);
           rec_responsable.setAdapter(respAdapter);
       }


        if(verBoton&&!StringUtils.isEmpty(planModel.CodResponsables)) {
            ll_levantar.setVisibility(View.VISIBLE);

            if (planModel.NroDocReferencia.substring(0, 3).equals("OBS")) {
                ver_obs_insp.setText("Ver Observación");
                ver_obs_insp.setVisibility(View.VISIBLE);

            } else if (planModel.NroDocReferencia.substring(0, 3).equals("INS")) {
                ver_obs_insp.setText("Ver Inspección");
                ver_obs_insp.setVisibility(View.VISIBLE);
            }

            GlobalVariables.isFragment=true;
            url= GlobalVariables.Url_base+"AccionMejora/Get/"+codAccion;
            final ActivityController obj = new ActivityController("get", url, PlanAccionDet.this,this);
            obj.execute("2");

        }else{
            ll_levantar.setVisibility(View.GONE);
        }

        }else if(Tipo=="2"){

           ListMejoras = gson.fromJson(data, GetAccionMejoraModel.class).Data;

            LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rec_mejora.setLayoutManager(horizontalManager);
            accionMejoraAdapter= new AccionMejoraAdapter(this,ListMejoras,planModel.Responsables,planModel.CodResponsables,PlanAccionDet.this);
            rec_mejora.setAdapter(accionMejoraAdapter);
        }else{
            if(data.contains("-1")) Toast.makeText(this, "Ocurrio un error al eliminar registro.",    Toast.LENGTH_SHORT).show();
            else accionMejoraAdapter.remove(Integer.parseInt(Tipo)-3);
        }


    }

    @Override
    public void successpost(String data, String Tipo) {
    }

    @Override
    public void error(String mensaje, String Tipo) {
    }


    public void DeleteObject(String Url, int index){
        String url= GlobalVariables.Url_base+Url;
        ActivityController obj = new ActivityController("get", url, PlanAccionDet.this,this);
        obj.execute(""+index);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Gson gson = new Gson();
        try{
            if(requestCode == 1 && resultCode == this.RESULT_OK) { // new plan
                AccionMejoraMinModel accionMejora= gson.fromJson(data.getStringExtra("AccionMejora"),AccionMejoraMinModel.class);
                UsuarioModel userLoad=gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
                accionMejora.UrlObs= userLoad.NroDocumento;
                accionMejora.Editable="1";
                accionMejora.Persona=userLoad.Nombres;
                accionMejoraAdapter.add(accionMejora);
            }
            if(requestCode == 2 && resultCode == this.RESULT_OK) { // edit plan

                AccionMejoraMinModel accionMejora= gson.fromJson(data.getStringExtra("AccionMejora"),AccionMejoraMinModel.class);
                accionMejoraAdapter.replace(accionMejora);
            }
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
