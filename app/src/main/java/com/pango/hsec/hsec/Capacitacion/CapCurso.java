package com.pango.hsec.hsec.Capacitacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.CursoDetAdapter;
import com.pango.hsec.hsec.adapter.RespAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.CapCursoModel;
import com.pango.hsec.hsec.model.Maestro;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CapCurso extends AppCompatActivity implements IActivity {

    RecyclerView rec_cursoDet, rec_Expositor;
    String CodCurso;
    CapCursoModel CursoModel;
    CursoDetAdapter detAdapter;
    RespAdapter respAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_curso);
        rec_cursoDet = (RecyclerView) findViewById(R.id.cursoDetalleRv);
        rec_Expositor = (RecyclerView) findViewById(R.id.ResposableRv);

        Bundle datos = this.getIntent().getExtras();
        CodCurso=datos.getString("CodCurso");

        String url= GlobalVariables.Url_base+"Capacitacion/GetCursoId/"+CodCurso;
        final ActivityController obj = new ActivityController("get", url, CapCurso.this,this);
        obj.execute("1");
    }

    public void close(View view){
        finish();
    }

    public void editarnotas(View view){
        Intent intent=new Intent(CapCurso.this, ParticipantesCurso.class);
        intent.putExtra("CodCurso",  CodCurso);
        startActivity(intent);
    }
    public void asistencia(View view){
        String Indice="";
        DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat formatoRender = new SimpleDateFormat("EEE d MMM");
        GlobalVariables.CapaCursoDias= new ArrayList<>();
        Date startDate=null,endDate=null;
            try {
                startDate=formatoInicial.parse(CursoModel.Fecha);
                long t= startDate.getTime();
                final long AddMillis=60000*CursoModel.Duracion;//millisecs
                endDate=new Date(t + AddMillis);
            } catch (Exception e) {
                e.printStackTrace();
            }

        Indice=CursoModel.Fecha.substring(0,10);
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            String ItemDate=formatoInicial.format(date).substring(0,10);
            GlobalVariables.CapaCursoDias.add(new Maestro(ItemDate,formatoRender.format(date)));
            String  Nowdate=formatoInicial.format(Calendar.getInstance().getTime()).substring(0,10);
            if(ItemDate.equals(Nowdate)) Indice=ItemDate;
        }
        Intent intent=new Intent(CapCurso.this, AsistentesCurso.class);
        intent.putExtra("CodCurso",  CodCurso);
        intent.putExtra("Indice", Indice);
        startActivity(intent);
    }

    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {
    //CursoDetAdapter
        Gson gson = new Gson();
        if(Tipo=="1") {
            CursoModel = gson.fromJson(data, CapCursoModel.class);

            rec_cursoDet.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rec_cursoDet.setLayoutManager(llm);
            rec_cursoDet.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            detAdapter = new CursoDetAdapter(this, CursoModel);
            rec_cursoDet.setAdapter(detAdapter);
            //codObsIns=planModel.NroDocReferencia;
            if (!StringUtils.isEmpty(CursoModel.Expositores)) {
                String[] responsable = CursoModel.Expositores.split(";");
                
                rec_Expositor.setHasFixedSize(true);
                LinearLayoutManager llm2 = new LinearLayoutManager(this);
                llm2.setOrientation(LinearLayoutManager.VERTICAL);
                rec_Expositor.setLayoutManager(llm2);
                rec_Expositor.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

                respAdapter = new RespAdapter(this, responsable);
                rec_Expositor.setAdapter(respAdapter);
            }
        }
        }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
