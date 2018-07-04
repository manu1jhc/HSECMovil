//ParticipantesCurso

package com.pango.hsec.hsec.Capacitacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.Busquedas.B_personasM;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.AsistenteAdapter;
import com.pango.hsec.hsec.adapter.OnLoadMoreListener;
import com.pango.hsec.hsec.adapter.ParticipantesAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPersonaModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class ParticipantesCurso extends AppCompatActivity implements IActivity {

    int contPublicacion;
    int paginacion=1,paginacion2=1;
    Spinner spinnerDias;
    SwipeRefreshLayout swipeRefreshLayout;
    PersonaModel AsistenteAdd;
    public RecyclerView list_Personas;
    //ConstraintLayout constraintLayout;
    //TextView tx_texto;
    String CodCurso, Indice;
    public ArrayList<PersonaModel>personasList;
    int itemSel=-1;
    boolean loadingTop=false;
    ParticipantesAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    //popup
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    protected Handler handler;
    int alto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participantes_curso);
        list_Personas = (RecyclerView) findViewById(R.id.ParticipantesRv);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);
        Bundle datos = this.getIntent().getExtras();
        CodCurso=datos.getString("CodCurso");
        personasList= new ArrayList<>();
        handler = new Handler();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                // tx_texto.setVisibility(View.VISIBLE);
                loadingTop=true;
                paginacion=2;
                getdata();
            } });
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        alto = metrics.heightPixels; // alto absoluto en pixels

        getdata();
    }
    public void updateNota(int index,String nota)
    {
        Gson gson = new Gson();
        PersonaModel temp= new PersonaModel();
        temp.CodPersona= personasList.get(index).CodPersona;
        temp.NroDocumento=CodCurso;
        temp.Estado=nota;
        String url= GlobalVariables.Url_base+"Capacitacion/UpdateParticipante";
        final ActivityController obj = new ActivityController("post", url, ParticipantesCurso.this,this);
        obj.execute(gson.toJson(temp),index+"-"+nota);
    }
    public void DeleteObject(int index){
        String Url=GlobalVariables.Url_base+"Capacitacion/DeleteParticipante?CodpersonaP="+personasList.get(index).CodPersona+"&CodCurso="+CodCurso;
        ActivityController obj = new ActivityController("get-2", Url, ParticipantesCurso.this,this);
        obj.execute((index+3)+"");
    }

    public void getdata(){
        paginacion2=1;
        String url=GlobalVariables.Url_base+"Capacitacion/GetParticipantesCurso?CodCurso="+CodCurso+"&Pagenumber=1&Elemperpage=14";
        ActivityController obj = new ActivityController("get-"+paginacion, url, ParticipantesCurso.this,this);
        obj.execute("1");
    }

    public void close(View view){
        finish();
    }


    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {
        Gson gson = new Gson();
        if(Tipo.equals("1")){  // new adapter
            GetPersonaModel getpersonaModel = gson.fromJson(data, GetPersonaModel.class);
            contPublicacion=getpersonaModel.Count;

            list_Personas.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            // use a linear layout manager
            list_Personas.setLayoutManager(mLayoutManager);
            personasList= new ArrayList<>();
            personasList=getpersonaModel.Data;
            mAdapter = new ParticipantesAdapter(this,personasList,list_Personas,swipeRefreshLayout);
            mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom
                    if(personasList.size()<contPublicacion)
                    {
                        personasList.add(null);
                        mAdapter.notifyItemInserted(personasList.size() - 1);
                        paginacion2++;
                        //paginacion2+=1;
                        String url=GlobalVariables.Url_base+"Capacitacion/GetParticipantesCurso?CodCurso="+CodCurso+"&Pagenumber="+paginacion2+"&Elemperpage=14";
                        ActivityController obj = new ActivityController("get-2", url, ParticipantesCurso.this,ParticipantesCurso.this);
                        obj.execute("2");
                    }
                }
            });
            list_Personas.setAdapter(mAdapter);
            if(loadingTop)
            {
                loadingTop=false;
                swipeRefreshLayout.setRefreshing(false);
                //tx_texto.setVisibility(View.GONE);
                swipeRefreshLayout.setEnabled( false );
            }
        }
        else if(Tipo.equals("2")){  // add adapter
            //constraintLayout.setVisibility(View.GONE);
            GetPersonaModel getpersonaModel = gson.fromJson(data, GetPersonaModel.class);
            //   remove progress item
            personasList.remove(personasList.size() - 1);
            mAdapter.notifyItemRemoved(personasList.size());
            for(PersonaModel item:getpersonaModel.Data)
            {
                personasList.add(item);
                mAdapter.notifyItemInserted(personasList.size());
            }
            mAdapter.setLoaded();
        }
        else
        {
            if(data.contains("-1")) confirmUpdate("E","Ocurrio un error al eliminar registro.");
            else{
                mAdapter.remove(Integer.parseInt(Tipo)-3);
                confirmUpdate("A","");
            }

        }
    }

    boolean fail;
    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {
        Gson gson = new Gson();
        if(data.contains("-1"))
            confirmUpdate("E","Ocurrio un error, no se pudo guardar la nota.");
        else {
            confirmUpdate("A","");
            mAdapter.update(Tipo);
        }
    }

    public void confirmUpdate(String Tipo,String sms){

        if(popupWindow!=null && popupWindow.isShowing())popupWindow.dismiss();
        layoutInflater =(LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_snackbar, null);

        popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(list_Personas, Gravity.NO_GRAVITY, 0, alto-180);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
        popupWindow.setOutsideTouchable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
        ImageView imgTitle=(ImageView) popupView.findViewById(R.id.img_content);
        CardView cardResult=(CardView) popupView.findViewById(R.id.card_content);
        TextView txtContenet=(TextView) popupView.findViewById(R.id.txt_content);

        if(Tipo.equals("E")){
            imgTitle.setImageResource(R.drawable.erroricon);
            txtContenet.setText(sms);
            cardResult.setCardBackgroundColor(Color.parseColor("#C23829"));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, 5000);
    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
