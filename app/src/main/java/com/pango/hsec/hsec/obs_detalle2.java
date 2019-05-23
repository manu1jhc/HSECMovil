package com.pango.hsec.hsec;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_personasM;
import com.pango.hsec.hsec.adapter.CheckAdapter;
import com.pango.hsec.hsec.adapter.CompCondAadpter;
import com.pango.hsec.hsec.adapter.ListEquipoAdapter;
import com.pango.hsec.hsec.adapter.ListISAdapter;
import com.pango.hsec.hsec.adapter.ListPersonEditAdapter;
import com.pango.hsec.hsec.adapter.ObsComentAdapter;
import com.pango.hsec.hsec.adapter.ObsMetodAdapter;
import com.pango.hsec.hsec.adapter.OsbClasifAdapter;
import com.pango.hsec.hsec.adapter.PersonaAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.EquipoModel;
import com.pango.hsec.hsec.model.GetEquipoModel;
import com.pango.hsec.hsec.model.GetSubDetalleModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsComentModel;
import com.pango.hsec.hsec.model.ObsDetalleModel;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.model.SubDetalleModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class obs_detalle2 extends Fragment implements IActivity{
    private static View mView;
    ImageButton add_personas, add_etapa, add_equipo, add_hha, add_ClasifObs;
    String title = "Personas Observadas";
    String DniAvatar;
    private RecyclerView listView, RecyclerEtapa, listEquipoInsp, rec_listHHA, rec_listClasif;
    private ListEquipoAdapter listPersonAdapter, listEquipoLAdapter;

    private ArrayList<EquipoModel> ListResponsables = new ArrayList<>();
    private ArrayList<EquipoModel> ListEquipoModel = new ArrayList<>();

    public static ArrayList<SubDetalleModel> ListHHA = new ArrayList<>();
    public static ArrayList<SubDetalleModel> ListHHAfinal = new ArrayList<>();

    public static ArrayList<SubDetalleModel> ListClasific = new ArrayList<>();
    public static ArrayList<SubDetalleModel> ListClasificFinal = new ArrayList<>();



    ArrayList<ObsComentModel> ListEtapDes = new ArrayList<>();

    ArrayList<SubDetalleModel> DataPrea=new ArrayList<>();
    ListISAdapter listISAdapter;

    ObsComentAdapter obsComentAdapter;
    OsbClasifAdapter obsClasifAdapter;

    Spinner sp_asp1,sp_asp2,sp_asp3,sp_asp4,sp_asp5,sp_asp6,sp_asp7,sp_asp8;


    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    CheckAdapter checkAdapter;

    CompCondAadpter compCondAadpter;


    public obs_detalle2() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static obs_detalle2 newInstance(String sampleText,String CodTipo) {
        obs_detalle2 f = new obs_detalle2();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        b.putString("bTipo", CodTipo);
        f.setArguments(b);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_obs_detalle2, container,false);
        String codigo_obs = getArguments().getString("bString");
        String Tipo = getArguments().getString("bTipo");
        listView = (RecyclerView) mView.findViewById(R.id.listPersonas);
        RecyclerEtapa = (RecyclerView) mView.findViewById(R.id.listEtapa);
        listEquipoInsp = (RecyclerView) mView.findViewById(R.id.listEquipoInsp);
        rec_listHHA = (RecyclerView) mView.findViewById(R.id.rec_listHHA);
        rec_listClasif = (RecyclerView) mView.findViewById(R.id.rec_listClasif);

        add_personas = mView.findViewById(R.id.add_personas);
        add_etapa = mView.findViewById(R.id.add_etapa);
        add_equipo = mView.findViewById(R.id.add_equipo);
        add_hha = mView.findViewById(R.id.add_hha);
        add_ClasifObs = mView.findViewById(R.id.add_ClasifObs);

        sp_asp1 = mView.findViewById(R.id.sp_asp1);
        sp_asp2 = mView.findViewById(R.id.sp_asp2);
        sp_asp3 = mView.findViewById(R.id.sp_asp3);
        sp_asp4 = mView.findViewById(R.id.sp_asp4);
        sp_asp5 = mView.findViewById(R.id.sp_asp5);
        sp_asp6 = mView.findViewById(R.id.sp_asp6);
        sp_asp7 = mView.findViewById(R.id.sp_asp7);
        sp_asp8 = mView.findViewById(R.id.sp_asp8);


        if(GlobalVariables.Obserbacion==null)changueTipo(Tipo);

       if(GlobalVariables.ObjectEditable){ // load data of server
            //if(GlobalVariables.ObserbacionDetalle.CodObservacion==null)
            //{
            String url= GlobalVariables.Url_base+"Observaciones/GetInvolucrados/"+codigo_obs;
            ActivityController obj = new ActivityController("get", url, obs_detalle2.this,getActivity());
            obj.execute("1-"+ Tipo);

            String url2=GlobalVariables.Url_base+"Observaciones/GetsSubDetalle/"+codigo_obs;
            final ActivityController obj2 = new ActivityController("get", url2, obs_detalle2.this,getActivity());
            obj2.execute("2-"+Tipo);







            // }
            //else setData();
        }
        else // new Obserbacion
        {
            if(GlobalVariables.ObserbacionDetalle.CodObservacion==null){

                /*
                Gson gson = new Gson();
                GlobalVariables.ObserbacionDetalle.CodObservacion=codigo_obs;
                GlobalVariables.ObserbacionDetalle.Observacion="";
                GlobalVariables.ObserbacionDetalle.Accion="";
                GlobalVariables.ObserbacionDetalle.CodActiRel=GlobalVariables.Actividad_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodHHA=GlobalVariables.HHA_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodEstado=GlobalVariables.Estado_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodError=GlobalVariables.Error_obs.get(0).CodTipo;
                GlobalVariables.ObserbacionDetalle.CodTipo=Tipo;

                GlobalVariables.StrObsDetalle=gson.toJson(GlobalVariables.ObserbacionDetalle);
            */
            }
            setData(Tipo);
        }
        //setData();




        add_personas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), B_personasM.class);
                //title
                intent.putExtra("titulo",title);
                startActivityForResult(intent , 1);
            }
        });

        add_etapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton btn_Cerrar;
                EditText et_etapa, et_description;
                Button btn_agregar;

                layoutInflater =(LayoutInflater)mView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_etapa, null);
                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);

                popupWindow.showAtLocation(add_etapa, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.setFocusable(true);
                //popupWindow.update();
                popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                popupWindow.setOutsideTouchable(true);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);

                et_etapa = popupView.findViewById(R.id.et_etapa);
                et_description = popupView.findViewById(R.id.et_description);
                btn_agregar = popupView.findViewById(R.id.btn_agregar);

                btn_Cerrar = (ImageButton) popupView.findViewById(R.id.btn_close);
                btn_Cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }});


                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String etapa = et_etapa.getText().toString();
                        String description = et_description.getText().toString();

                        //txt_description
                        if (etapa.equals("0") || description.equals("")){
                            Toast.makeText(getActivity(), "Los campos no pueden estar vacios" , Toast.LENGTH_LONG).show();

                        }else {

                            ListEtapDes.add(new ObsComentModel("1", etapa, description));

                            //Toast.makeText(getActivity(), TipoComent.Descripcion + " " + description_coment + " ", Toast.LENGTH_LONG).show();


                            obsComentAdapter.notifyDataSetChanged();

                            popupWindow.dismiss();
                        }

                    }
                });


            }
        });

        ArrayAdapter adapterAsp1 = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Opc_aspecto);
        adapterAsp1.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_asp1.setAdapter(adapterAsp1);

        ArrayAdapter adapterAsp2 = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Opc_aspecto);
        adapterAsp2.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_asp2.setAdapter(adapterAsp2);

        ArrayAdapter adapterAsp3 = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Opc_aspecto);
        adapterAsp3.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_asp3.setAdapter(adapterAsp3);

        ArrayAdapter adapterAsp4 = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Opc_aspecto);
        adapterAsp4.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_asp4.setAdapter(adapterAsp4);

        ArrayAdapter adapterAsp5 = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Opc_aspecto);
        adapterAsp5.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_asp5.setAdapter(adapterAsp5);

        ArrayAdapter adapterAsp6 = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Opc_aspecto);
        adapterAsp6.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_asp6.setAdapter(adapterAsp6);

        ArrayAdapter adapterAsp7 = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Opc_aspecto);
        adapterAsp7.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_asp7.setAdapter(adapterAsp7);

        ArrayAdapter adapterAsp8 = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Opc_aspecto);
        adapterAsp8.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_asp8.setAdapter(adapterAsp8);

        sp_asp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp1) ).getSelectedItem();
                //GlobalVariables.ObserbacionDetalle.CodEstado=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp2) ).getSelectedItem();
                //GlobalVariables.ObserbacionDetalle.CodEstado=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp3) ).getSelectedItem();
                //GlobalVariables.ObserbacionDetalle.CodEstado=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp4) ).getSelectedItem();
                //GlobalVariables.ObserbacionDetalle.CodEstado=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               // Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp5) ).getSelectedItem();
                //GlobalVariables.ObserbacionDetalle.CodEstado=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               // Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp6) ).getSelectedItem();
                //GlobalVariables.ObserbacionDetalle.CodEstado=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp7) ).getSelectedItem();
                //GlobalVariables.ObserbacionDetalle.CodEstado=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp8) ).getSelectedItem();
                //GlobalVariables.ObserbacionDetalle.CodEstado=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });




        /// interaccion de seguridad

        add_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), B_personasM.class);
                intent.putExtra("titulo","Equipo de inspección");
                startActivityForResult(intent , 2);

            }
        });

        add_hha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //ImageButton btn_Cerrar;
                RecyclerView listCompCond;
                Button btn_agregar, btn_cerrar;
                CheckBox checkBoxall;
                TextView txt_title;
                CardView id_cv_Otros;
                EditText et_otros;

                layoutInflater =(LayoutInflater)mView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_hha, null);
                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);

                popupWindow.showAtLocation(add_hha, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.setFocusable(true);
                //popupWindow.update();
                popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                popupWindow.setOutsideTouchable(true);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                btn_agregar = popupView.findViewById(R.id.btn_agregar);
                checkBoxall = popupView.findViewById(R.id.checkBoxall);
                listCompCond = (RecyclerView) popupView.findViewById(R.id.list_Metodologia);
                txt_title = popupView.findViewById(R.id.txt_title);
                txt_title.setText("Actividad de Alto riesgo identificada");
                id_cv_Otros = popupView.findViewById(R.id.id_cv_Otros);
                id_cv_Otros.setVisibility(View.GONE);
                et_otros = popupView.findViewById(R.id.et_otros);
                btn_cerrar = popupView.findViewById(R.id.btn_cerrar);


                if (ListHHAfinal.size()> 0){
                    for (int j = 0; j<ListHHAfinal.size(); j++){
                        for (int i = 0; i<ListHHA.size(); i++){
                            if (ListHHAfinal.get(j).Descripcion.equals(ListHHA.get(i).Descripcion)) {
                                ListHHA.get(i).estado = true;
                            }
                        }

                        if(ListHHAfinal.get(j).Descripcion.contains("19")){
                            id_cv_Otros.setVisibility(View.VISIBLE);
                            String[] data =  ListHHAfinal.get(j).Descripcion.split(":");

                            et_otros.setText(data[1]);
                        }else{
                            id_cv_Otros.setVisibility(View.GONE);

                        }




                    }
                }

                btn_cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getActivity(), CheckAdapter.items.get(9).estado + " dat" , Toast.LENGTH_LONG).show();


                        ListHHA = new ArrayList<>();
                        initHHA();

                        if (!ListHHAfinal.isEmpty()){

                            for (int i=0; i<ListHHA.size(); i++){
                                for(int j=0; j<ListHHAfinal.size(); j++) {
                                    if(ListHHA.get(i).Descripcion.equals(ListHHAfinal.get(j).Descripcion)) {
                                        ListHHA.get(i).estado = true;
                                    }
                                }
                            }
                        }
                        //Toast.makeText(getActivity(), ListMetodologiaTemp.get(0).estado + "" , Toast.LENGTH_LONG).show();
                        checkAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();


                    }});

                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                listCompCond.setLayoutManager(horizontalManager);
                //GlobalVariables.ListMetodologia

                checkAdapter = new CheckAdapter(getActivity(), ListHHA, checkBoxall, id_cv_Otros);
                listCompCond.setAdapter(checkAdapter);

                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(getActivity(), CheckAdapter.items.get(9).estado + " dat" , Toast.LENGTH_LONG).show();
                        ListHHAfinal = new ArrayList<>();
                        for (int i = 0; i<ListHHA.size(); i++){
                            if (ListHHA.get(i).estado) {
                                //setear el valor de otros
                                String data = et_otros.getText().toString();
                                if(ListHHA.get(i).Descripcion.equals("19")){
                                    ListHHAfinal.add(new SubDetalleModel("HHAR","",ListHHA.get(i).Descripcion +" : "+ et_otros.getText().toString(),true));
                                }else {
                                    ListHHAfinal.add(ListHHA.get(i));
                                }
                            }
                        }
                        LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rec_listHHA.setLayoutManager(horizontalManager);
                        listISAdapter = new ListISAdapter(getActivity(), ListHHAfinal);
                        rec_listHHA.setAdapter(listISAdapter);
                        popupWindow.dismiss();

                    }
                });

                checkBoxall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i=0; i < ListHHA.size(); i++) {
                            ListHHA.get(i).estado = checkBoxall.isChecked();
                        }
                        if (ListHHA.get(18).estado && ListHHA.get(18).Descripcion.equals("19")){
                            id_cv_Otros.setVisibility(View.VISIBLE);
                        }else {
                            id_cv_Otros.setVisibility(View.GONE);
                        }
                        checkAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

// boton añadir clasificacion
        add_ClasifObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ImageButton btn_Cerrar;
                RecyclerView rec_listClasificObs;
                Button btn_agregar, btn_cerrar;
                CheckBox checkBoxall;
                TextView txt_title;
                //ArrayList<Boolean> estado = new ArrayList<Boolean>();
                CardView id_cv_Otros;

                layoutInflater =(LayoutInflater)mView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_metodologia, null);
                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);

                popupWindow.showAtLocation(add_ClasifObs, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.setFocusable(true);
                //popupWindow.update();
                popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                popupWindow.setOutsideTouchable(true);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                btn_agregar = popupView.findViewById(R.id.btn_agregar);
                checkBoxall = popupView.findViewById(R.id.checkBoxall);
                txt_title = popupView.findViewById(R.id.txt_title);
                txt_title.setText("Metodologia de gestión de riesgos aplicada antes del inicio de la tarea o actividad");
                rec_listClasificObs = (RecyclerView) popupView.findViewById(R.id.list_Metodologia);
                id_cv_Otros = popupView.findViewById(R.id.id_cv_Otros);
                id_cv_Otros.setVisibility(View.GONE);
                btn_cerrar = popupView.findViewById(R.id.btn_cerrar);



            if (ListClasificFinal.size()> 0){
                for (int j = 0; j<ListClasificFinal.size(); j++){
                    for (int i = 0; i<ListClasific.size(); i++){
                            if (ListClasificFinal.get(j).Descripcion.equals(ListClasific.get(i).Descripcion)) {
                            ListClasific.get(i).estado = true;
                        }
                    }
                }
            }





                btn_cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {


                        ListClasific = new ArrayList<>();
                        init_clasifObs();

                        if (!ListClasificFinal.isEmpty()){

                            for (int i=0; i<ListClasific.size(); i++){
                                for(int j=0; j<ListClasificFinal.size(); j++) {
                                    if(ListClasific.get(i).Descripcion.equals(ListClasificFinal.get(j).Descripcion)) {
                                        ListClasific.get(i).estado = true;
                                    }
                                }
                            }
                        }
                        //Toast.makeText(getActivity(), ListMetodologiaTemp.get(0).estado + "" , Toast.LENGTH_LONG).show();
                        checkAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();



                    }});

                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rec_listClasificObs.setLayoutManager(horizontalManager);
                //GlobalVariables.ListMetodologia

                checkAdapter = new CheckAdapter(getActivity(), ListClasific, checkBoxall,id_cv_Otros);
                rec_listClasificObs.setAdapter(checkAdapter);

                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //Toast.makeText(getActivity(), CheckAdapter.items.get(1).estado + "" , Toast.LENGTH_LONG).show();
                        //list_Metodologia

                        ListClasificFinal = new ArrayList<>();


                        for (int i = 0; i<ListClasific.size(); i++){
                            if (ListClasific.get(i).estado) {
                                ListClasificFinal.add(ListClasific.get(i));
                            }
                        }

                            LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            rec_listClasif.setLayoutManager(horizontalManager);
                            obsClasifAdapter = new OsbClasifAdapter(getActivity(), ListClasificFinal);
                            rec_listClasif.setAdapter(obsClasifAdapter);



                        popupWindow.dismiss();


                    }
                });

                checkBoxall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i=0; i < ListClasific.size(); i++) {
                            ListClasific.get(i).estado = checkBoxall.isChecked();
                        }
                        checkAdapter.notifyDataSetChanged();




                    }
                });




            }
        });





        return mView;
    }

    public void setData(String Tipo) {

        //ListResponsables = new ArrayList<>();
        //ListEtapDes = new ArrayList<>();


        ListHHA = new ArrayList<>();
        initHHA();
        ListClasific = new ArrayList<>();
        init_clasifObs();



        if (!Tipo.equals("TO04")) {
            //personas observadas
            LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(horizontalManager);
            listPersonAdapter = new ListEquipoAdapter(getActivity(), ListResponsables, false);
            listView.setAdapter(listPersonAdapter);

            //etapa/desviacion
            LinearLayoutManager horizontalManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            RecyclerEtapa.setLayoutManager(horizontalManager2);
            obsComentAdapter = new ObsComentAdapter(getActivity(), ListEtapDes);
            RecyclerEtapa.setAdapter(obsComentAdapter);



            //if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodActiRel))
            //sp_asp1.setSelection(GlobalVariables.indexOf(GlobalVariables.Actividad_obs,GlobalVariables.ObserbacionDetalle.CodActiRel));

            if (DataPrea.size() != 0) {
                sp_asp1.setSelection(GlobalVariables.indexOf(GlobalVariables.Opc_aspecto, DataPrea.get(0).Descripcion));
                sp_asp2.setSelection(GlobalVariables.indexOf(GlobalVariables.Opc_aspecto, DataPrea.get(1).Descripcion));
                sp_asp3.setSelection(GlobalVariables.indexOf(GlobalVariables.Opc_aspecto, DataPrea.get(2).Descripcion));
                sp_asp4.setSelection(GlobalVariables.indexOf(GlobalVariables.Opc_aspecto, DataPrea.get(3).Descripcion));
                sp_asp5.setSelection(GlobalVariables.indexOf(GlobalVariables.Opc_aspecto, DataPrea.get(4).Descripcion));
                sp_asp6.setSelection(GlobalVariables.indexOf(GlobalVariables.Opc_aspecto, DataPrea.get(5).Descripcion));
                sp_asp7.setSelection(GlobalVariables.indexOf(GlobalVariables.Opc_aspecto, DataPrea.get(6).Descripcion));
                sp_asp8.setSelection(GlobalVariables.indexOf(GlobalVariables.Opc_aspecto, DataPrea.get(7).Descripcion));

            }
        }else if (Tipo.equals("TO04")) {
            //interaccion de seguridad

            /// interaccion de seguridad
            LinearLayoutManager horizontalManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            listEquipoInsp.setLayoutManager(horizontalManager3);
            listEquipoLAdapter = new ListEquipoAdapter(getActivity(), ListEquipoModel, false);
            listEquipoInsp.setAdapter(listEquipoLAdapter);




        }


    }



    public void changueTipo(String Tipo) {

        if(!StringUtils.isEmpty(Tipo)&& Tipo.equals("TO03")){
            mView.findViewById(R.id.ll_tarea).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.ll_is).setVisibility(View.GONE);

        }else if (!StringUtils.isEmpty(Tipo)&& Tipo.equals("TO04")) {
            mView.findViewById(R.id.ll_tarea).setVisibility(View.GONE);
            mView.findViewById(R.id.ll_is).setVisibility(View.VISIBLE);

        }else {

            mView.findViewById(R.id.ll_tarea).setVisibility(View.GONE);
            mView.findViewById(R.id.ll_is).setVisibility(View.GONE);

        }

    }






        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == 1  && resultCode  == RESULT_OK) {
                //String name=data.getStringExtra("nombreP");
                //String codSolicitado=data.getStringExtra("codpersona");
                for(PersonaModel item: GlobalVariables.lista_Personas)
                    if(item.Check) listPersonAdapter.add(new EquipoModel(item));
                listPersonAdapter.notifyDataSetChanged();
                // listPersonAdapter.add(new EquipoModel(data.getStringExtra("codpersona"),data.getStringExtra("nombreP"),data.getStringExtra("dni"),data.getStringExtra("cargo")));

            }



            if (requestCode == 2 && resultCode  == RESULT_OK) {

                //Gson gson = new Gson();
                //GlobalVariables.ListResponsables=gson.fromJson(data, GetEquipoModel.class).Data;
                for(PersonaModel item:GlobalVariables.lista_Personas)
                    if(item.Check) listEquipoLAdapter.add(new EquipoModel(item));
                listEquipoLAdapter.notifyDataSetChanged();
///////crear el adapter

                //actives.add(1);

            }





        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }





    }


    @Override
    public void success(String data, String Tipo){
        //ArrayList<SubDetalleModel> DataPeto=new ArrayList<>(); // etapa/desviacion

        String[] tipoObs = Tipo.split("-");

        if (tipoObs[0].equals("1")) {
            Gson gson = new Gson();
            GetEquipoModel getEquipoModel = gson.fromJson(data, GetEquipoModel.class);

            if (tipoObs[1].equals("TO03")) {
                ListResponsables = getEquipoModel.Data;
            }else {
                ListEquipoModel = getEquipoModel.Data;
            }

        } else if (tipoObs[0].equals("2")) {

            Gson gson = new Gson();
            GetSubDetalleModel getSubDetalleModel = gson.fromJson(data, GetSubDetalleModel.class);
            int count=getSubDetalleModel.Count;

            for(int i = 0; i < getSubDetalleModel.Data.size(); i++){
                if (tipoObs[1].equals("TO03")) {
                    if (getSubDetalleModel.Data.get(i).CodTipo.equals("PREA")) {
                        DataPrea.add(new SubDetalleModel(getSubDetalleModel.Data.get(i).CodTipo, getSubDetalleModel.Data.get(i).CodSubtipo, getSubDetalleModel.Data.get(i).Descripcion));
                    } else if (getSubDetalleModel.Data.get(i).CodTipo.equals("PETO")) {
                        //DataPeto.add(new SubDetalleModel(getSubDetalleModel.Data.get(i).CodTipo,getSubDetalleModel.Data.get(i).CodSubtipo,getSubDetalleModel.Data.get(i).Descripcion));
                        ListEtapDes.add(new ObsComentModel(getSubDetalleModel.Data.get(i).CodTipo, getSubDetalleModel.Data.get(i).CodSubtipo, getSubDetalleModel.Data.get(i).Descripcion));

                    }
                } else if(tipoObs[1].equals("TO04")){

                    if (getSubDetalleModel.Data.get(i).CodTipo.equals("HHA")) {
                        ListHHAfinal = new ArrayList<>();
                        ListHHAfinal.add(new SubDetalleModel("","",getSubDetalleModel.Data.get(i).CodSubtipo,true));
                    //            ListHHA.add(new SubDetalleModel("HHAR","",GlobalVariables.HHA_obs.get(i).CodTipo, false));

                }else if (getSubDetalleModel.Data.get(i).CodTipo.equals("OBSC")) {
                        ListClasificFinal = new ArrayList<>();
                        ListClasificFinal.add(new SubDetalleModel("","",getSubDetalleModel.Data.get(i).CodSubtipo,true));


                    }

                }

            }


            LinearLayoutManager horizontalManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rec_listHHA.setLayoutManager(horizontalManager4);
            listISAdapter = new ListISAdapter(getActivity(), ListHHAfinal);
            rec_listHHA.setAdapter(listISAdapter);


            LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rec_listClasif.setLayoutManager(horizontalManager);
            obsClasifAdapter = new OsbClasifAdapter(getActivity(), ListClasificFinal);
            rec_listClasif.setAdapter(obsClasifAdapter);




        }
        setData(tipoObs[1]);

    }

    @Override
    public void successpost(String data, String Tipo){

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }


    public void  initHHA(){

        for (int i=1; i<GlobalVariables.HHA_obs.size(); i++){
            ListHHA.add(new SubDetalleModel("HHAR","",GlobalVariables.HHA_obs.get(i).CodTipo, false));
        }

    }


    public void init_clasifObs(){
        for (int i=0; i<GlobalVariables.Clasificacion_Obs.size(); i++) {

            ListClasific.add(new SubDetalleModel("","",GlobalVariables.Clasificacion_Obs.get(i).CodTipo,false));
            }
        }


}
