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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.Observaciones.FragmentPlan;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.AccionMejoraAdapter;
import com.pango.hsec.hsec.adapter.PersonaAdapter;
import com.pango.hsec.hsec.adapter.PlandetAdapter;
import com.pango.hsec.hsec.adapter.RespAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetAccionMejoraModel;
import com.pango.hsec.hsec.model.GetPlanModel;
import com.pango.hsec.hsec.model.PlanModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PlanAccionDet extends AppCompatActivity implements IActivity {
    String codAccion;
    String url;
    String jsonPlan="";
    PlandetAdapter plandetAdapter;
    RespAdapter respAdapter;
    String codObsIns="";
    PlanModel planModel;
    Button ver_obs_insp;
    boolean verBoton;
    LinearLayout ll_levantar;
    GetAccionMejoraModel getAccionMejoraModel;
    ImageButton btn_agregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_accion_det);
        ver_obs_insp=findViewById(R.id.ver_obs_insp);
        ll_levantar=findViewById(R.id.ll_levantar);
        btn_agregar=findViewById(R.id.btn_agregar);

        Bundle datos = this.getIntent().getExtras();
        codAccion=datos.getString("codAccion");
        jsonPlan=datos.getString("jsonPlan");
        verBoton=datos.getBoolean("verBoton");
        //GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=false;
        //codObs="OBS00067956";


        GlobalVariables.isFragment=true;

        if(jsonPlan.isEmpty()) {
            //GlobalVariables.istabs=true;
            url= GlobalVariables.Url_base+"PlanAccion/Get/"+codAccion;
            final ActivityController obj = new ActivityController("get", url, PlanAccionDet.this);
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
                //abre uno vacio

                Intent intent=new Intent(PlanAccionDet.this, AddRegistroAvance.class);
                startActivity(intent);
            }
        });


    }

    public void close(View view){
        finish();
    }


    @Override
    public void success(String data, String Tipo) {

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
        String[]responsable=planModel.Responsables.split(";");

        final RecyclerView rec_responsable = (RecyclerView) findViewById(R.id.rec_responsable);
        rec_responsable.setHasFixedSize(true);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        rec_responsable.setLayoutManager(llm2);
        rec_responsable.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        respAdapter = new RespAdapter(this,responsable);
        rec_responsable.setAdapter(respAdapter);


        if(verBoton) {
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
            final ActivityController obj = new ActivityController("get", url, PlanAccionDet.this);
            obj.execute("2");

        }else{
            ll_levantar.setVisibility(View.GONE);
        }

        }else if(Tipo=="2"){
            getAccionMejoraModel = gson.fromJson(data, GetAccionMejoraModel.class);
            int Count=getAccionMejoraModel.Count;

            final RecyclerView rec_mejora = (RecyclerView) findViewById(R.id.rec_lev_obs);
            rec_mejora.setHasFixedSize(true);
            LinearLayoutManager llm3 = new LinearLayoutManager(this);
            llm3.setOrientation(LinearLayoutManager.VERTICAL);
            rec_mejora.setLayoutManager(llm3);
            rec_mejora.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            AccionMejoraAdapter accionMejoraAdapter= new AccionMejoraAdapter(this,getAccionMejoraModel.Data,data);
            rec_mejora.setAdapter(accionMejoraAdapter);


        }

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
