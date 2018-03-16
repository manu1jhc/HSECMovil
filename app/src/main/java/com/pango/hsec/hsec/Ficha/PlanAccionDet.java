package com.pango.hsec.hsec.Ficha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.Observaciones.FragmentPlan;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.PlandetAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPlanModel;
import com.pango.hsec.hsec.model.PlanModel;

public class PlanAccionDet extends AppCompatActivity implements IActivity {
    String codAccion;
    String url;
    String jsonPlan="";
    PlandetAdapter plandetAdapter;
    String codObsIns="";
    PlanModel planModel;
    Button ver_obs_insp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_accion_det);
        ver_obs_insp=findViewById(R.id.ver_obs_insp);

        Bundle datos = this.getIntent().getExtras();
        codAccion=datos.getString("codAccion");

        //GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=false;
        //codObs="OBS00067956";
        url= GlobalVariables.Url_base+"PlanAccion/Get/"+codAccion;


        GlobalVariables.isFragment=true;

        if(jsonPlan.isEmpty()) {
            //GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, PlanAccionDet.this);
            obj.execute("");
        }else{
            success(jsonPlan,"");
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


    }

    public void close(View view){
        finish();
    }


    @Override
    public void success(String data, String Tipo) {

        Gson gson = new Gson();
        planModel = gson.fromJson(data, PlanModel.class);

        ListView list_popup=(ListView) findViewById(R.id.list_plan);

        plandetAdapter = new PlandetAdapter(this,planModel);
        list_popup.setAdapter(plandetAdapter);

        //codObsIns=planModel.NroDocReferencia;

        if(planModel.NroDocReferencia.substring(0,3).equals("OBS")){
            ver_obs_insp.setText("Ver Observación");
            ver_obs_insp.setVisibility(View.VISIBLE);

        }else if(planModel.NroDocReferencia.substring(0,3).equals("INS")){
            ver_obs_insp.setText("Ver Inspección");
            ver_obs_insp.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
