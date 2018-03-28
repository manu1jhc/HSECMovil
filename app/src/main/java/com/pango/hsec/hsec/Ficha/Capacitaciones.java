package com.pango.hsec.hsec.Ficha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.CapacitacionAdapter;
import com.pango.hsec.hsec.adapter.PerfilCapAdapter;
import com.pango.hsec.hsec.adapter.PersonaAdapter;
import com.pango.hsec.hsec.adapter.PlandetAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetCapRecibidaModel;
import com.pango.hsec.hsec.model.GetPerfilCapModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Capacitaciones extends AppCompatActivity implements IActivity {
    GetCapRecibidaModel getCapRecibidaModel;
    GetPerfilCapModel getPerfilCapModel;
    //Button btn_Abrir_Popup;
    Button btn_Cerrar;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    ImageButton ibclose;

    String codPersona="";
    String url="";
    String url2="";
    TextView cap_recibida,perfil_cap;
    LinearLayout ll_cab1,ll_cab2;
    RecyclerView recList;
    RecyclerView recListperfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacitaciones);
        cap_recibida=findViewById(R.id.cap_recibida);
        perfil_cap=findViewById(R.id.perfil_cap);

        ll_cab1=findViewById(R.id.ll_cab1);
        ll_cab2=findViewById(R.id.ll_cab2);

       recList = (RecyclerView) findViewById(R.id.rec_capacitacion);
       recListperfil = (RecyclerView) findViewById(R.id.rec_perfil);


        Bundle datos = getIntent().getExtras();
        codPersona=datos.getString("CodPersona");


        url= GlobalVariables.Url_base+"FichaPersonal/CapacitacionesRecibidas/"+codPersona;
        //url="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/Usuario/FiltroPersona/@@@@/1/5";
        //Utils.isActivity=true;
        final ActivityController obj = new ActivityController("get", url, Capacitaciones.this);
        obj.execute("1");



        url2= GlobalVariables.Url_base+"FichaPersonal/Perfilcapacitacion/"+codPersona;
        //url="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/Usuario/FiltroPersona/@@@@/1/5";
        //Utils.isActivity=true;
        final ActivityController obj2 = new ActivityController("get", url2, Capacitaciones.this);
        obj2.execute("2");


        cap_recibida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if()
                if(recList.getVisibility()==View.GONE){
                    recList.setVisibility(View.VISIBLE);
                    ll_cab1.setVisibility(View.VISIBLE);

                }else{
                    recList.setVisibility(View.GONE);
                    ll_cab1.setVisibility(View.GONE);

                }

            }
        });

        perfil_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recListperfil.getVisibility()==View.GONE){
                    recListperfil.setVisibility(View.VISIBLE);
                    ll_cab2.setVisibility(View.VISIBLE);

                }else{
                    recListperfil.setVisibility(View.GONE);
                    ll_cab2.setVisibility(View.GONE);

                }
            }
        });





    }
    public void close(View view){
        finish();
    }
    @Override
    public void success(String data, String Tipo) {
       /* Gson gson = new Gson();
        getPerfilCapModel = gson.fromJson(data, GetPerfilCapModel.class);
*/
        if(Tipo=="1"){

            Gson gson = new Gson();
            getCapRecibidaModel = gson.fromJson(data, GetCapRecibidaModel.class);

            if(getCapRecibidaModel.Count!=0) {
                recList.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);
                recList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

                CapacitacionAdapter ca = new CapacitacionAdapter(getCapRecibidaModel.Data);
                recList.setAdapter(ca);



                final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });


                recList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean b) {

                    }

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                        try {
                            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                            //LinearLayout linearLayout5;
                            TextView tx_fecha, tx_duracion, tx_tema, tx_tipo, tx_nota, tx_estado, tx_vencimiento;

                            if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                                DateFormat formatoInicial = new SimpleDateFormat("dd/MM/yyyy");
                                DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

                                int position = recyclerView.getChildAdapterPosition(child);

                                //Toast.makeText(Capacitaciones.this,"The Item Clicked is: "+ position ,Toast.LENGTH_SHORT).show();
//popup

                                layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                popupView = layoutInflater.inflate(R.layout.popup_capacitacion, null);
                            /*ListView list_popup=(ListView) popupView.findViewById(R.id.list_popup);

                            plandetAdapter = new PlandetAdapter(getContext(),getPlanModel.Data.get(position));
                            list_popup.setAdapter(plandetAdapter);
*/
                                //ListView list_popup = (ListView) mView.findViewById(R.id.list_popup);
                                //linearLayout5=popupView.findViewById(R.id.linearLayout5);
                                tx_fecha = popupView.findViewById(R.id.tx_fecha);
                                tx_duracion = popupView.findViewById(R.id.tx_duracion);
                                tx_tema = popupView.findViewById(R.id.tx_tema);
                                tx_tipo = popupView.findViewById(R.id.tx_tipo);
                                tx_nota = popupView.findViewById(R.id.tx_nota);
                                tx_estado = popupView.findViewById(R.id.tx_estado);
                                tx_vencimiento = popupView.findViewById(R.id.tx_vencimiento);

                                tx_fecha.setText(formatoRender.format(formatoInicial.parse(getCapRecibidaModel.Data.get(position).Fecha)));
                                tx_duracion.setText(getCapRecibidaModel.Data.get(position).Duracion + " hrs.");
                                tx_tema.setText(getCapRecibidaModel.Data.get(position).Tema);
                                tx_tipo.setText(getCapRecibidaModel.Data.get(position).Tipo);
                                tx_nota.setText(getCapRecibidaModel.Data.get(position).Nota);
                                tx_estado.setText(getCapRecibidaModel.Data.get(position).Estado);
                                tx_vencimiento.setText(getCapRecibidaModel.Data.get(position).Vencimiento);
                                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, false);

                                btn_Cerrar = (Button) popupView.findViewById(R.id.id_cerrar);
                                btn_Cerrar.setOnClickListener(new Button.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        popupWindow.dismiss();
                                    }
                                });

                                ibclose = (ImageButton) popupView.findViewById(R.id.ibclose);
                                ibclose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popupWindow.dismiss();

                                    }
                                });

                                //popupWindow.showAsDropDown(btn_Abrir_Popup, 50, -400);

                                popupWindow.showAtLocation(recList, Gravity.CENTER, 0, 0);


                                return true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                    }
                });
            }


        }else if(Tipo=="2"){

            Gson gson = new Gson();
            getPerfilCapModel = gson.fromJson(data, GetPerfilCapModel.class);


            if(getPerfilCapModel.Count!=0) {

                final RecyclerView recListperfil = (RecyclerView) findViewById(R.id.rec_perfil);
                recListperfil.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recListperfil.setLayoutManager(llm);
                recListperfil.addItemDecoration(new DividerItemDecoration(this,
                        DividerItemDecoration.VERTICAL));

                PerfilCapAdapter ca = new PerfilCapAdapter(getPerfilCapModel.Data);
                recListperfil.setAdapter(ca);

                final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });


                recListperfil.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean b) {

                    }

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                        try {
                            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                            //LinearLayout linearLayout5;
                            TextView tx_tema, tx_cumplido, tx_tipo, tx_nota, tx_estado, tx_vencimiento;

                            if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                                DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                //DateFormat formatoInicial2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
                                DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

                                int position = recyclerView.getChildAdapterPosition(child);

                                //Toast.makeText(Capacitaciones.this,"The Item Clicked is: "+ position ,Toast.LENGTH_SHORT).show();
//popup

                                layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                popupView = layoutInflater.inflate(R.layout.popup_perfilcap, null);
                            /*ListView list_popup=(ListView) popupView.findViewById(R.id.list_popup);

                            plandetAdapter = new PlandetAdapter(getContext(),getPlanModel.Data.get(position));
                            list_popup.setAdapter(plandetAdapter);
*/
                                //ListView list_popup = (ListView) mView.findViewById(R.id.list_popup);
                                //linearLayout5=popupView.findViewById(R.id.linearLayout5);
                                tx_tema = popupView.findViewById(R.id.tx_tema);
                                tx_cumplido = popupView.findViewById(R.id.tx_cumplido);
                                tx_tipo = popupView.findViewById(R.id.tx_tipo);
                                tx_nota = popupView.findViewById(R.id.tx_nota);
                                tx_estado = popupView.findViewById(R.id.tx_estado);
                                tx_vencimiento = popupView.findViewById(R.id.tx_vencimiento);

                                tx_tema.setText(getPerfilCapModel.Data.get(position).Tema);
                                tx_cumplido.setText(formatoRender.format(formatoInicial.parse(getPerfilCapModel.Data.get(position).Cumplido)));
                                tx_tipo.setText(getPerfilCapModel.Data.get(position).Tipo);
                                tx_nota.setText(getPerfilCapModel.Data.get(position).Nota);
                                tx_estado.setText(getPerfilCapModel.Data.get(position).Estado);
                                tx_vencimiento.setText(formatoRender.format(formatoInicial.parse(getPerfilCapModel.Data.get(position).Vencimiento)));


                                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, false);

                                btn_Cerrar = (Button) popupView.findViewById(R.id.id_cerrar);
                                btn_Cerrar.setOnClickListener(new Button.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        popupWindow.dismiss();
                                    }
                                });

                                ibclose = (ImageButton) popupView.findViewById(R.id.ibclose);
                                ibclose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popupWindow.dismiss();

                                    }
                                });

                                //popupWindow.showAsDropDown(btn_Abrir_Popup, 50, -400);

                                popupWindow.showAtLocation(recListperfil, Gravity.CENTER, 0, 0);


                                return true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                    }
                });


            }










            //////////////////////////////////
        }








    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //check if popupwindow is open
        //Log.e(TAG, "Check if it runs through this section");
        if (popupWindow != null) {

                popupWindow.dismiss();
                popupWindow = null;
                //myWebView = null;

        }else{
            super.onBackPressed();
        }
    }



    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }









}
