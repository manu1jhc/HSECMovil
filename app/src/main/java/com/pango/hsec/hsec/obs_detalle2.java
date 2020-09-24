package com.pango.hsec.hsec;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.Busquedas.B_personasM;
import com.pango.hsec.hsec.adapter.CheckAdapter;
import com.pango.hsec.hsec.adapter.CompCondAadpter;
import com.pango.hsec.hsec.adapter.IconAdapter;
import com.pango.hsec.hsec.adapter.ListEquipoAdapter;
import com.pango.hsec.hsec.adapter.ListISAdapter;
import com.pango.hsec.hsec.adapter.ListYesNotAdapter;
import com.pango.hsec.hsec.adapter.ObsComentAdapter;
import com.pango.hsec.hsec.adapter.ObsMetodAdapter;
import com.pango.hsec.hsec.adapter.OsbClasifAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.EquipoModel;
import com.pango.hsec.hsec.model.GetEquipoModel;
import com.pango.hsec.hsec.model.GetSubDetalleModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsComentModel;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.model.SubDetalleModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class obs_detalle2 extends Fragment implements IActivity{
    private static View mView;
    ImageButton add_personas, add_etapa, add_equipo, add_hha, add_ClasifObs, add_PreIteraccion;
    String title = "Personas Observadas";
    private RecyclerView listView, RecyclerEtapa, listEquipoInsp, rec_listHHA, rec_listClasif,RecPreIter,RecCierreIter;
    private ListEquipoAdapter listPersonAdapter, listEquipoLAdapter;

    public static ArrayList<SubDetalleModel> ListHHA = new ArrayList<>();
    public static ArrayList<SubDetalleModel> ListClasific = new ArrayList<>();
    public static ArrayList<SubDetalleModel> ListPreInter = new ArrayList<>();

    public ObsMetodAdapter listISAdapter,obsClasifAdapter,listPreIterAdapter;
    ListYesNotAdapter listYesNotAdapter;
    ObsComentAdapter obsComentAdapter;

    Spinner sp_asp1,sp_asp2,sp_asp3,sp_asp4,sp_asp5,sp_asp6,sp_asp7,sp_asp8;


    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    CheckAdapter checkAdapter;

    CompCondAadpter compCondAadpter;
    TextView tx_aspPrev, textView34po, textViewasp1, textViewasp2, textViewasp3, textViewasp4, textViewasp5, textViewasp6, textViewasp7, textViewasp8, textView41is;

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
        RecPreIter = (RecyclerView) mView.findViewById(R.id.rec_listPreIter);
        RecCierreIter = (RecyclerView) mView.findViewById(R.id.rec_listCierreIt);

        add_personas = mView.findViewById(R.id.add_personas);
        add_etapa = mView.findViewById(R.id.add_etapa);
        add_equipo = mView.findViewById(R.id.add_equipo);
        add_hha = mView.findViewById(R.id.add_hha);
        add_ClasifObs = mView.findViewById(R.id.add_ClasifObs);
        add_PreIteraccion=mView.findViewById(R.id.add_preit);
        sp_asp1 = mView.findViewById(R.id.sp_asp1);
        sp_asp2 = mView.findViewById(R.id.sp_asp2);
        sp_asp3 = mView.findViewById(R.id.sp_asp3);
        sp_asp4 = mView.findViewById(R.id.sp_asp4);
        sp_asp5 = mView.findViewById(R.id.sp_asp5);
        sp_asp6 = mView.findViewById(R.id.sp_asp6);
        sp_asp7 = mView.findViewById(R.id.sp_asp7);
        sp_asp8 = mView.findViewById(R.id.sp_asp8);
//tarea

        tx_aspPrev = mView.findViewById(R.id.tx_aspPrev);
        tx_aspPrev.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Aspectos Previos Observados"));

//interaccion de seguridad
        textView41is= mView.findViewById(R.id.textView41is);
        textView41is.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Realizado Por:"));


        if(GlobalVariables.Obserbacion==null)changueTipo(Tipo,"0");

       if(GlobalVariables.ObjectEditable){ // load data of server

           if(GlobalVariables.ObserbacionSubdetalle==null) {
                GlobalVariables.ObserbacionSubdetalle=codigo_obs;
                String url = GlobalVariables.Url_base + "Observaciones/GetInvolucrados/" + codigo_obs;
                ActivityController obj = new ActivityController("get", url, obs_detalle2.this, getActivity());
                obj.execute("1-" + Tipo);
                if(Tipo.equals("TO03")){
                    GlobalVariables.ObserbacionSubdetalle=codigo_obs;
                    String url2=GlobalVariables.Url_base+"Observaciones/GetsSubDetalle/"+codigo_obs;
                    final ActivityController obj2 = new ActivityController("get", url2, obs_detalle2.this,getActivity());
                    obj2.execute("2-"+Tipo);
                }
            }
            else setData(Tipo,GlobalVariables.Obserbacion.CodSubTipo);
        }
        else // new SubDetalle
        {
            if(GlobalVariables.ObserbacionSubdetalle==null){
                GlobalVariables.ObserbacionSubdetalle=codigo_obs;
                GlobalVariables.SubDetalleTa= new ArrayList<>();
                GlobalVariables.ListResponsables= new ArrayList<>();
                GlobalVariables.ListAtendidos= new ArrayList<>();
                for(Maestro item:GlobalVariables.Aspectos_Obs)
                    GlobalVariables.SubDetalleTa.add(new SubDetalleModel("PREA",item.CodTipo,"R003"));
                for(Maestro item:GlobalVariables.Cierre_Interaccion)
                    GlobalVariables.SubDetalleIS.add(new SubDetalleModel("OBVE",item.CodTipo,"R001"));
            }
            setData(Tipo,GlobalVariables.Obserbacion.CodSubTipo);
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
                et_description = popupView.findViewById(R.id.txt_description);
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

                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_description.getWindowToken(), 0);
                        //txt_description
                        if (etapa.equals("0") || description.equals("")){
                            Toast.makeText(getActivity(), "Los campos no pueden estar vacios" , Toast.LENGTH_LONG).show();

                        }else {
                            obsComentAdapter.add(new SubDetalleModel("PETO", etapa, description));
                            obsComentAdapter.notifyDataSetChanged();
                            popupWindow.dismiss();
                        }

                    }
                });
            }
        });

        sp_asp1.setAdapter(new IconAdapter(getActivity().getBaseContext(),GlobalVariables.Opc_aspectoIcon));
        sp_asp2.setAdapter(new IconAdapter(getActivity().getBaseContext(),GlobalVariables.Opc_aspectoIcon));
        sp_asp3.setAdapter(new IconAdapter(getActivity().getBaseContext(),GlobalVariables.Opc_aspectoIcon));
        sp_asp4.setAdapter(new IconAdapter(getActivity().getBaseContext(),GlobalVariables.Opc_aspectoIcon));
        sp_asp5.setAdapter(new IconAdapter(getActivity().getBaseContext(),GlobalVariables.Opc_aspectoIcon));
        sp_asp6.setAdapter(new IconAdapter(getActivity().getBaseContext(),GlobalVariables.Opc_aspectoIcon));
        sp_asp7.setAdapter(new IconAdapter(getActivity().getBaseContext(),GlobalVariables.Opc_aspectoIcon));
        sp_asp8.setAdapter(new IconAdapter(getActivity().getBaseContext(),GlobalVariables.Opc_aspectoIcon));
        sp_asp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp1) ).getSelectedItem();
                ChanguePREA("P001",Tipo.CodTipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp2) ).getSelectedItem();
                ChanguePREA("P002",Tipo.CodTipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp3) ).getSelectedItem();
                ChanguePREA("P003",Tipo.CodTipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp4) ).getSelectedItem();
                ChanguePREA("P004",Tipo.CodTipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp5) ).getSelectedItem();
                ChanguePREA("P005",Tipo.CodTipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp6) ).getSelectedItem();
                ChanguePREA("P006",Tipo.CodTipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp7) ).getSelectedItem();
                ChanguePREA("P007",Tipo.CodTipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sp_asp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_asp8) ).getSelectedItem();
                ChanguePREA("P008",Tipo.CodTipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        /// interaccion de seguridad

        add_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), B_personas.class);
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

                initHHA();
                int cont=0;
                for(SubDetalleModel item2 : GlobalVariables.SubDetalleIS){
                    if(item2.CodTipo.equals("HHA"))
                    {
                        cont++;
                        for(SubDetalleModel item : ListHHA) {
                            if(item2.CodSubtipo.equals(item.CodSubtipo)) {
                                item.Check=true;
                                continue;
                            }
                        }
                        if(item2.CodSubtipo.equals("19")){
                            id_cv_Otros.setVisibility(View.VISIBLE);
                            et_otros.setText(item2.Descripcion);
                        }
                    }
                }
                if(ListHHA.size()==cont)checkBoxall.setChecked(true);
                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                listCompCond.setLayoutManager(horizontalManager);
                checkAdapter = new CheckAdapter(getActivity(), ListHHA, checkBoxall, id_cv_Otros);
                listCompCond.setAdapter(checkAdapter);

                btn_cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                    }});

                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int val=0;
                        List<SubDetalleModel> itemsOther= new ArrayList<>();
                        for(SubDetalleModel item2 : GlobalVariables.SubDetalleIS){
                            if(!item2.CodTipo.equals("HHA")) itemsOther.add(item2);
                        }
                        GlobalVariables.SubDetalleIS.clear();
                        GlobalVariables.SubDetalleIS.addAll(itemsOther);
                        for(SubDetalleModel item : ListHHA) {
                            if(item.Check){
                                if(item.CodSubtipo.equals("19")) item.Descripcion=et_otros.getText().toString();
                                else item.Descripcion=null;
                                listISAdapter.add(item);
                            }
                        }

                        listISAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

                checkBoxall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(SubDetalleModel item : ListHHA) {
                            item.Check=checkBoxall.isChecked();
                        }
                        id_cv_Otros.setVisibility(checkBoxall.isChecked()?View.VISIBLE:View.GONE);
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

                init_clasifObs();
                int cont=0;
                for(SubDetalleModel item2 : GlobalVariables.SubDetalleIS){
                    if(item2.CodTipo.equals("OBSC"))
                    {
                        cont++;
                        for(SubDetalleModel item : ListClasific)
                            if(item2.CodSubtipo.equals(item.CodSubtipo)) {
                                item.Check=true;
                                continue;
                            }
                    }
                }
                if(ListClasific.size()==cont)checkBoxall.setChecked(true);
                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rec_listClasificObs.setLayoutManager(horizontalManager);
                checkAdapter = new CheckAdapter(getActivity(), ListClasific, checkBoxall,id_cv_Otros);
                rec_listClasificObs.setAdapter(checkAdapter);

                btn_cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                    }});

                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        List<SubDetalleModel> itemsOther= new ArrayList<>();
                        for(SubDetalleModel item2 : GlobalVariables.SubDetalleIS){
                            if(!item2.CodTipo.equals("OBSC")) itemsOther.add(item2);
                        }
                        GlobalVariables.SubDetalleIS.clear();
                        GlobalVariables.SubDetalleIS.addAll(itemsOther);
                        for(SubDetalleModel item : ListClasific) {
                            if(item.Check)  {
                                item.Descripcion=null;
                                obsClasifAdapter.add(item);
                            }
                        }
                        obsClasifAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

                checkBoxall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(SubDetalleModel item : ListClasific) {
                            item.Check=checkBoxall.isChecked();
                        }
                        checkAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
// boton añadir Pre-Interaccion
        add_PreIteraccion.setOnClickListener(new View.OnClickListener() {
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

                popupWindow.showAtLocation(add_PreIteraccion, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.setFocusable(true);
                //popupWindow.update();
                popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                popupWindow.setOutsideTouchable(true);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                btn_agregar = popupView.findViewById(R.id.btn_agregar);
                checkBoxall = popupView.findViewById(R.id.checkBoxall);
                txt_title = popupView.findViewById(R.id.txt_title);
                txt_title.setText("PRE-INTERACCIÓN");
                rec_listClasificObs = (RecyclerView) popupView.findViewById(R.id.list_Metodologia);
                id_cv_Otros = popupView.findViewById(R.id.id_cv_Otros);
                id_cv_Otros.setVisibility(View.GONE);
                btn_cerrar = popupView.findViewById(R.id.btn_cerrar);

                init_PreIter();
                int cont=0;
                for(SubDetalleModel item2 : GlobalVariables.SubDetalleIS){
                    if(item2.CodTipo.equals("OBSP"))  //OBVE
                    {
                        cont++;
                        for(SubDetalleModel item : ListPreInter)
                            if(item2.CodSubtipo.equals(item.CodSubtipo)) {
                                item.Check=true;
                                continue;
                            }
                    }
                }
                if(ListPreInter.size()==cont)checkBoxall.setChecked(true);
                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rec_listClasificObs.setLayoutManager(horizontalManager);
                checkAdapter = new CheckAdapter(getActivity(), ListPreInter, checkBoxall,id_cv_Otros);
                rec_listClasificObs.setAdapter(checkAdapter);

                btn_cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                    }});

                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        List<SubDetalleModel> itemsOther= new ArrayList<>();
                        for(SubDetalleModel item2 : GlobalVariables.SubDetalleIS){
                            if(!item2.CodTipo.equals("OBSP")) itemsOther.add(item2);
                        }
                        GlobalVariables.SubDetalleIS.clear();
                        GlobalVariables.SubDetalleIS.addAll(itemsOther);
                        for(SubDetalleModel item : ListPreInter) {
                            if(item.Check)  {
                                item.Descripcion=null;
                                listPreIterAdapter.add(item);
                            }
                        }
                        listPreIterAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

                checkBoxall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(SubDetalleModel item : ListPreInter) {
                            item.Check=checkBoxall.isChecked();
                        }
                        checkAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        return mView;
    }

    public void ChanguePREA(String Preg, String Resp){
        boolean pass=true;
        for(SubDetalleModel item: GlobalVariables.SubDetalleTa){
            if(item.CodTipo.equals("PREA")&&item.CodSubtipo.equals(Preg)){
                item.Descripcion=Resp;
                pass=false;
                break;
            }
        }
        if(pass) GlobalVariables.SubDetalleTa.add(new SubDetalleModel("PREA",Preg,Resp));
    }
    public void  initHHA(){
        ListHHA.clear();
        for (int i=1; i<GlobalVariables.HHA_obs.size(); i++)
            ListHHA.add(new SubDetalleModel("HHA",GlobalVariables.HHA_obs.get(i).CodTipo, GlobalVariables.HHA_obs.get(i).Descripcion));
    }

    public void init_clasifObs(){
        ListClasific.clear();
        for (int i=0; i<GlobalVariables.Clasificacion_Obs.size(); i++)
            ListClasific.add(new SubDetalleModel("OBSC",GlobalVariables.Clasificacion_Obs.get(i).CodTipo,GlobalVariables.Clasificacion_Obs.get(i).Descripcion));
    }

    public void init_PreIter(){
        ListPreInter.clear();
        for (int i=0; i<GlobalVariables.Pre_Interaccion.size(); i++)
            ListPreInter.add(new SubDetalleModel("OBSP",GlobalVariables.Pre_Interaccion.get(i).CodTipo,GlobalVariables.Pre_Interaccion.get(i).Descripcion));
    }
    public void setData(String Tipo,String SubTipo) {

        if (Tipo.equals("TO03")) {
            //personas observadas
            LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(horizontalManager);
            listPersonAdapter = new ListEquipoAdapter(getActivity(), GlobalVariables.ListResponsables, false);
            listView.setAdapter(listPersonAdapter);

            //etapa/desviacion
            LinearLayoutManager horizontalManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            RecyclerEtapa.setLayoutManager(horizontalManager2);
            obsComentAdapter = new ObsComentAdapter(getActivity(), GlobalVariables.SubDetalleTa);
            RecyclerEtapa.setAdapter(obsComentAdapter);


            for(SubDetalleModel item: GlobalVariables.SubDetalleTa){
                if(item.CodTipo.equals("PREA")){
                    int CodResp=GlobalVariables.indexOf(GlobalVariables.Opc_aspecto, item.Descripcion);
                    switch (item.CodSubtipo){
                        case "P001": sp_asp1.setSelection(CodResp);break;
                        case "P002": sp_asp2.setSelection(CodResp);break;
                        case "P003": sp_asp3.setSelection(CodResp);break;
                        case "P004": sp_asp4.setSelection(CodResp);break;
                        case "P005": sp_asp5.setSelection(CodResp);break;
                        case "P006": sp_asp6.setSelection(CodResp);break;
                        case "P007": sp_asp7.setSelection(CodResp);break;
                        case "P008": sp_asp8.setSelection(CodResp);break;
                    }
                }
            }
        }
        else if(Tipo.equals("TO04"))//if(Tipo.equals("TO04"))
        {
            //interaccion de seguridad
            /// interaccion de seguridad

            int Subtipo = Integer.parseInt(SubTipo);
            if(Subtipo>1){
                LinearLayoutManager horizontalManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                RecPreIter.setLayoutManager(horizontalManager2);
                listPreIterAdapter = new ObsMetodAdapter(getActivity(),  GlobalVariables.SubDetalleIS,"OBSP",(observacion_edit) this.getActivity());
                RecPreIter.setAdapter(listPreIterAdapter);

                LinearLayoutManager horizontalManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                RecCierreIter.setLayoutManager(horizontalManager1);
                listYesNotAdapter = new ListYesNotAdapter(getActivity(), GlobalVariables.SubDetalleIS,"OBVE",(observacion_edit) this.getActivity());
                RecCierreIter.setAdapter(listYesNotAdapter);
            }
            else {
                LinearLayoutManager horizontalManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                listEquipoInsp.setLayoutManager(horizontalManager3);
                listEquipoLAdapter = new ListEquipoAdapter(getActivity(), GlobalVariables.ListAtendidos, false);
                listEquipoInsp.setAdapter(listEquipoLAdapter);

                LinearLayoutManager horizontalManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rec_listHHA.setLayoutManager(horizontalManager4);
                listISAdapter = new ObsMetodAdapter(getActivity(), GlobalVariables.SubDetalleIS, "HHA", (observacion_edit) this.getActivity());
                rec_listHHA.setAdapter(listISAdapter);

                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rec_listClasif.setLayoutManager(horizontalManager);
                obsClasifAdapter = new ObsMetodAdapter(getActivity(), GlobalVariables.SubDetalleIS, "OBSC", (observacion_edit) this.getActivity());
                rec_listClasif.setAdapter(obsClasifAdapter);
            }
        }
    }

    public void changueTipo(String Tipo,String SubTipo) {

        if(Tipo.equals("TO03")){ //!StringUtils.isEmpty(Tipo)&&
            mView.findViewById(R.id.ll_tarea).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.ll_is).setVisibility(View.GONE);
            mView.findViewById(R.id.ll_iscc).setVisibility(View.GONE);
            setData(Tipo,SubTipo);
        }else { // if (!StringUtils.isEmpty(Tipo)&& Tipo.equals("TO04"))
            mView.findViewById(R.id.ll_tarea).setVisibility(View.GONE);
            int Subtipo = Integer.parseInt(SubTipo);
            if(Subtipo>1)  {
                mView.findViewById(R.id.ll_is).setVisibility(View.GONE);
                mView.findViewById(R.id.ll_iscc).setVisibility(View.VISIBLE);
            }
            else {
                mView.findViewById(R.id.ll_is).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.ll_iscc).setVisibility(View.GONE);
            }
            setData(Tipo,SubTipo);
        }
       /* else {
            mView.findViewById(R.id.ll_tarea).setVisibility(View.GONE);
            mView.findViewById(R.id.ll_is).setVisibility(View.GONE);
        }*/
    }
        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1  && resultCode  == RESULT_OK) {
                for(PersonaModel item: GlobalVariables.lista_Personas)
                    if(item.Check) listPersonAdapter.add(new EquipoModel(item));
                listPersonAdapter.notifyDataSetChanged();
            }
            else if (requestCode == 2 && resultCode  == RESULT_OK) {

                GlobalVariables.ListAtendidos.clear();
                listEquipoLAdapter.add(new EquipoModel(data.getStringExtra("codpersona"),data.getStringExtra("nombreP"),data.getStringExtra("dni"),data.getStringExtra("cargo")));
                listEquipoLAdapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<Integer> actives= new ArrayList<>();
    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {

        String[] tipoObs = Tipo.split("-");

        if (tipoObs[0].equals("1")) {
            Gson gson = new Gson();
            GetEquipoModel getEquipoModel = gson.fromJson(data, GetEquipoModel.class);

            if (tipoObs[1].equals("TO03")) {
                GlobalVariables.ListResponsables = getEquipoModel.Data;
                for(EquipoModel item:GlobalVariables.ListResponsables)
                    GlobalVariables.StrResponsables.add((EquipoModel)item.clone());
            }else {

                for(EquipoModel item:getEquipoModel.Data)
                {
                    if(item.Estado.equals("1")){
                        item.Estado=null;
                        GlobalVariables.ListAtendidos.clear();
                        GlobalVariables.ListAtendidos.add((EquipoModel)item.clone());
                        GlobalVariables.StrAtendidos.add((EquipoModel)item.clone());
                        break;
                    }
                }
            }
            actives.add(1);
            if(tipoObs[1].equals("TO04"))setData(tipoObs[1],GlobalVariables.Obserbacion.CodSubTipo);
            else if(actives.size()==2) setData(tipoObs[1],"0");
        } else if (tipoObs[0].equals("2")) {

            Gson gson = new Gson();
            GetSubDetalleModel getSubDetalleModel = gson.fromJson(data, GetSubDetalleModel.class);

            GlobalVariables.SubDetalleTa = getSubDetalleModel.Data;
            for(SubDetalleModel item:GlobalVariables.SubDetalleTa)
                GlobalVariables.StrSubDetalleTa.add((SubDetalleModel)item.clone());
            actives.add(1);
            if(actives.size()==2)setData(tipoObs[1],"0");
        }
    }

    @Override
    public void successpost(String data, String Tipo){
    }
    @Override
    public void error(String mensaje, String Tipo) {
    }
}