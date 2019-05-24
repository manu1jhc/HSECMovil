package com.pango.hsec.hsec;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Busquedas.B_contrata;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.CheckAdapter;
import com.pango.hsec.hsec.adapter.CompCondAadpter;
import com.pango.hsec.hsec.adapter.ObsComentAdapter;
import com.pango.hsec.hsec.adapter.ObsMetodAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetSubDetalleModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsComentModel;
import com.pango.hsec.hsec.model.ObsDetalleModel;
import com.pango.hsec.hsec.model.SubDetalleModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class obs_detalle1 extends Fragment implements IActivity{
    //tarea
    TextView textView6,textView4,textView14,textView15,textView16,textView17,textView35,textView25;
    private static View mView;
    Spinner spinneActividad, spinnerHHA, spinnerActo,spinnerCondicion,spinnerEstado, spinnerError,spinnerStopWork,sp_stopworkIS;
    EditText txtObservacion,txtAccion, txtCodPET, txtComentRO, txt_detcomcon,txt_accion_inmed;
    ImageButton btn_addComent;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    String description_coment = "";
    Maestro TipoComent;
    RecyclerView listView, list_dataMetod, List_dataComCond;
    ObsComentAdapter obsComentAdapter;
    public ArrayList<ObsComentModel> ListComentarios = new ArrayList<>();
    LinearLayout ll_tarea, ll_IS;
    //interaccion de seguridad
    ImageButton btn_buscar_c, add_metodol, add_comp_riesgo;
    public static final int REQUEST_CODE = 1;
    TextView tx_ISempresa;
    EditText txt_equipoInv, txt_InteracSeg;
    public static ArrayList<SubDetalleModel> ListMetodologia = new ArrayList<>();
    public static ArrayList<SubDetalleModel> ListMetodologiaFinal = new ArrayList<>();

    public static ArrayList<SubDetalleModel> ListCompCond = new ArrayList<>();
    public static ArrayList<SubDetalleModel> ListCompCondFinal = new ArrayList<>();


    CheckAdapter checkAdapter, checkAdapter2;
    ObsMetodAdapter obsMetodAdapter;
    CompCondAadpter compCondAadpter;
    public static final com.pango.hsec.hsec.obs_detalle1 newInstance(String sampleText,String CodTipo) {
        obs_detalle1 f = new obs_detalle1();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        b.putString("bTipo", CodTipo);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_detalle1, container,false);
        String codigo_obs = getArguments().getString("bString");
        String Tipo = getArguments().getString("bTipo");
        txtObservacion=(EditText) mView.findViewById(R.id.txt_observacion);
        txtAccion=(EditText) mView.findViewById(R.id.txt_accion);
        btn_addComent = (ImageButton) mView.findViewById(R.id.imgComent);
        listView = (RecyclerView) mView.findViewById(R.id.listComent);
        txtCodPET= (EditText) mView.findViewById(R.id.txt_codpet);
        txtComentRO = (EditText) mView.findViewById(R.id.txt_comentRO);

        // Interaccion de seguridad
        btn_buscar_c = (ImageButton) mView.findViewById(R.id.btn_buscar_c);
        tx_ISempresa = (TextView) mView.findViewById(R.id.tx_ISempresa);
        add_metodol = (ImageButton) mView.findViewById(R.id.add_metodol);
        list_dataMetod = (RecyclerView) mView.findViewById(R.id.list_Metodologia);
        add_comp_riesgo = (ImageButton) mView.findViewById(R.id.add_comp_riesgo);
        List_dataComCond = (RecyclerView) mView.findViewById(R.id.list_ComRiesgo);
        txt_detcomcon = mView.findViewById(R.id.txt_detcomcon);
        txt_accion_inmed = mView.findViewById(R.id.txt_accion_inmed);
        txt_equipoInv = mView.findViewById(R.id.txt_equipoInv);
        txt_InteracSeg = mView.findViewById(R.id.txt_InteracSeg);

        textView6=mView.findViewById(R.id.textView6);
        textView6.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Observación:"));
        textView4=mView.findViewById(R.id.textView4);
        textView4.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Acción:"));
        textView14=mView.findViewById(R.id.textView14);
        textView14.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Actividad Relacionada:"));
        textView15=mView.findViewById(R.id.textView15);
        textView15.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"HHA Relacionada:"));
        textView16=mView.findViewById(R.id.textView16);
        textView16.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Acto Sub-Estándar:"));
        textView35=mView.findViewById(R.id.textView35);
        textView35.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Estado:"));
        textView25=mView.findViewById(R.id.textView25);
        textView25.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Error:"));
        textView17=mView.findViewById(R.id.textView17);
        textView17.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Condición SubEstándar:"));

        spinneActividad = (Spinner) mView.findViewById(R.id.sp_actividad);
        spinnerHHA = (Spinner) mView.findViewById(R.id.sp_hha);
        spinnerActo = (Spinner) mView.findViewById(R.id.sp_acto);
        spinnerCondicion = (Spinner) mView.findViewById(R.id.sp_condicion);
        spinnerEstado = (Spinner) mView.findViewById(R.id.sp_estado);
        spinnerError = (Spinner) mView.findViewById(R.id.sp_error);
        spinnerStopWork = (Spinner) mView.findViewById(R.id.sp_stopwork);
        sp_stopworkIS = (Spinner) mView.findViewById(R.id.sp_stopworkIS);

        ArrayAdapter adapterActividadObs = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Actividad_obs);
        adapterActividadObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinneActividad.setAdapter(adapterActividadObs);

        ArrayAdapter adapterHHA = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.HHA_obs);
        adapterHHA.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerHHA.setAdapter(adapterHHA);

        ArrayAdapter adapterActo = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Acto_obs);
        adapterActo.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerActo.setAdapter(adapterActo);

        ArrayAdapter adapterCondicion = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Condicion_obs);
        adapterCondicion.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerCondicion.setAdapter(adapterCondicion);

        ArrayAdapter adapterEstado = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Estado_obs);
        adapterEstado.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapterEstado);

        ArrayAdapter adapterError = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Error_obs);
        adapterError.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerError.setAdapter(adapterError);

        ArrayAdapter adapterStopWork = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.StopWork_obs);
        adapterStopWork.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerStopWork.setAdapter(adapterStopWork);

        ArrayAdapter adapterStopWorkIS = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.StopWork_obs);
        adapterStopWorkIS.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_stopworkIS.setAdapter(adapterStopWorkIS);

        //StopWork_obs
        //detect chabgues values
        txtObservacion.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                GlobalVariables.ObserbacionDetalle.Observacion = txtObservacion.getText().toString();
            }
        });
        txtComentRO.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                GlobalVariables.ObserbacionDetalle.CodSubEstandar = txtComentRO.getText().toString();
            }
        });
        txtAccion.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                GlobalVariables.ObserbacionDetalle.Accion = txtAccion.getText().toString();
            }
        });

        spinneActividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_actividad) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodActiRel=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerHHA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_hha) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodHHA=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerActo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO01")){
                    Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_acto) ).getSelectedItem();
                    GlobalVariables.ObserbacionDetalle.CodSubEstandar=Tipo.CodTipo;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerCondicion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO02")) {
                    Maestro Tipo = (Maestro) ((Spinner) mView.findViewById(R.id.sp_condicion)).getSelectedItem();
                    GlobalVariables.ObserbacionDetalle.CodSubEstandar = Tipo.CodTipo;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_estado) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodEstado=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerError.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_error) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodError=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        spinnerStopWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        sp_stopworkIS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_stopworkIS) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalleIS.StopWork=Tipo.CodTipo;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


        if(GlobalVariables.Obserbacion==null)changueTipo(Tipo);
////////
        if(GlobalVariables.ObjectEditable){ // load data of server

            if(GlobalVariables.ObserbacionDetalle.CodObservacion==null)
            {
                String url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codigo_obs;
                ActivityController obj = new ActivityController("get", url, obs_detalle1.this,getActivity());
                obj.execute(Tipo);


                if (Tipo.equals("TO04")){

                    String url2= GlobalVariables.Url_base+"Observaciones/GetsSubDetalle/"+codigo_obs;
                    //String url2 = "https://app.antapaccay.com.pe/HSECWeb/WHSEC_Service/api/Observaciones/GetsSubDetalle/OBS00310074";
                    ActivityController obj2 = new ActivityController("get", url2, obs_detalle1.this,getActivity());
                    obj2.execute("3");

                    //https://app.antapaccay.com.pe/HSECWeb/WHSEC_Service/api/Observaciones/GetsSubDetalle/OBS00307442
                }



            }
            else setdata(Tipo);

        }
        else // new Obserbacion
        {
            if(GlobalVariables.ObserbacionDetalle.CodObservacion==null){
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
            }
            setdata(Tipo);
        }


        //popup add

        btn_addComent.setOnClickListener(new View.OnClickListener() {
            //@TargetApi(11)
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "popup datos", Toast.LENGTH_LONG).show();
                ImageButton btn_Cerrar;
                EditText txt_description;
                Spinner spinnerComent;
                Button btn_agregar;

                //if ()

                layoutInflater =(LayoutInflater)mView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_comentarios, null);
                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);

                popupWindow.showAtLocation(btn_addComent, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.setFocusable(true);
                //popupWindow.update();
                popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                popupWindow.setOutsideTouchable(true);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);

                spinnerComent = popupView.findViewById(R.id.sp_tipoComent);
                txt_description = popupView.findViewById(R.id.txt_description);
                btn_agregar = popupView.findViewById(R.id.btn_agregar);

                ArrayAdapter adapterCometarios = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.O_Comentarios);
                adapterCometarios.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
                spinnerComent.setAdapter(adapterCometarios);



                spinnerComent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        TipoComent = (Maestro) ( (Spinner) popupView.findViewById(R.id.sp_tipoComent) ).getSelectedItem();
                        //Toast.makeText(getActivity(), TipoComent.Descripcion, Toast.LENGTH_LONG).show();


                        //GlobalVariables.Obserbacion.CodAreaHSEC=Tipo.CodTipo;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });

                btn_Cerrar = (ImageButton) popupView.findViewById(R.id.btn_close);
                btn_Cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }});

                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        description_coment = txt_description.getText().toString();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(txt_description.getWindowToken(), 0);
                        //txt_description
                        boolean datarep = false;

                        if (ListComentarios.size() <3) {

                            for (ObsComentModel obsm : ListComentarios){
                                if (obsm.Tipo!=null&&obsm.Tipo.equals(TipoComent.Descripcion)){
                                    datarep = true;
                                }
                            }

                            if (datarep == true){

                                Toast.makeText(getActivity(), "El tipo de comentario ya existe", Toast.LENGTH_LONG).show();

                            }else if (TipoComent.CodTipo.equals("0") || description_coment.equals("")) {
                                Toast.makeText(getActivity(), "Los campos no pueden estar vacios", Toast.LENGTH_LONG).show();

                            } else {

                                ListComentarios.add(new ObsComentModel(TipoComent.CodTipo, TipoComent.Descripcion, description_coment));
                                //Toast.makeText(getActivity(), TipoComent.Descripcion + " " + description_coment + " ", Toast.LENGTH_LONG).show();
                                obsComentAdapter.notifyDataSetChanged();
                                popupWindow.dismiss();
                            }

                        } else {
                            Toast.makeText(getActivity(), "limite de comentarios", Toast.LENGTH_LONG).show();

                        }





                    }
                });

            }
        });

// interaccion de seguridad
        btn_buscar_c.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), B_contrata.class);
                startActivityForResult(intent , REQUEST_CODE);
            }
        });

        add_metodol.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ImageButton btn_Cerrar;
                RecyclerView list_Metodologia;
                Button btn_agregar, btn_cerrar;
                CheckBox checkBoxall;
                TextView txt_title;
                ArrayList<Boolean> estado = new ArrayList<Boolean>();
                CardView id_cv_Otros;
                layoutInflater =(LayoutInflater)mView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_metodologia, null);
                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);
                popupWindow.showAtLocation(add_metodol, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.setFocusable(true);
                //popupWindow.update();
                popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                popupWindow.setOutsideTouchable(true);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                btn_agregar = popupView.findViewById(R.id.btn_agregar);
                checkBoxall = popupView.findViewById(R.id.checkBoxall);
                txt_title = popupView.findViewById(R.id.txt_title);
                txt_title.setText("Metodologia de gestión de riesgos aplicada antes del inicio de la tarea o actividad");
                list_Metodologia = (RecyclerView) popupView.findViewById(R.id.list_Metodologia);
                id_cv_Otros = popupView.findViewById(R.id.id_cv_Otros);
                id_cv_Otros.setVisibility(View.GONE);
                btn_cerrar = popupView.findViewById(R.id.btn_cerrar);


                if (ListMetodologiaFinal.size()> 0){
                    for (int j = 0; j<ListMetodologiaFinal.size(); j++){
                        for (int i = 0; i<ListMetodologia.size(); i++){
                            if (ListMetodologiaFinal.get(j).Descripcion.equals(ListMetodologia.get(i).Descripcion)) {
                                ListMetodologia.get(i).estado = true;
                            }
                        }
                    }
                }


                btn_cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ListMetodologia = new ArrayList<>();
                        init_List();

                        if (!ListMetodologiaFinal.isEmpty()){

                            for (int i=0; i<ListMetodologia.size(); i++){
                                for(int j=0; j<ListMetodologiaFinal.size(); j++) {
                                    if(ListMetodologia.get(i).Descripcion.equals(ListMetodologiaFinal.get(j).Descripcion)) {
                                        ListMetodologia.get(i).estado = true;
                                    }
                                }
                            }
                        }
                        //Toast.makeText(getActivity(), ListMetodologiaTemp.get(0).estado + "" , Toast.LENGTH_LONG).show();
                        checkAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }});

                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                list_Metodologia.setLayoutManager(horizontalManager);
                //GlobalVariables.ListMetodologia

                checkAdapter = new CheckAdapter(getActivity(), ListMetodologia, checkBoxall,id_cv_Otros);
                list_Metodologia.setAdapter(checkAdapter);

                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), CheckAdapter.items.get(1).estado + "" , Toast.LENGTH_LONG).show();
                        //list_Metodologia

                        ListMetodologiaFinal = new ArrayList<>();
                        for (int i = 0; i<ListMetodologia.size(); i++){
                            if (ListMetodologia.get(i).estado) {
                                ListMetodologiaFinal.add(ListMetodologia.get(i));
                            }
                        }

                        LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        list_dataMetod.setLayoutManager(horizontalManager);
                        obsMetodAdapter = new ObsMetodAdapter(getActivity(), ListMetodologiaFinal);
                        list_dataMetod.setAdapter(obsMetodAdapter);
                        popupWindow.dismiss();

                    }
                });

                checkBoxall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i=0; i < ListMetodologia.size(); i++) {
                            ListMetodologia.get(i).estado = checkBoxall.isChecked();
                        }
                        checkAdapter.notifyDataSetChanged();

                    }
                });


            }
        });


        add_comp_riesgo.setOnClickListener(new Button.OnClickListener() {
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
                popupView = layoutInflater.inflate(R.layout.popup_metodologia, null);
                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);

                popupWindow.showAtLocation(add_metodol, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.setFocusable(true);
                //popupWindow.update();
                popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                popupWindow.setOutsideTouchable(true);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                btn_agregar = popupView.findViewById(R.id.btn_agregar);
                checkBoxall = popupView.findViewById(R.id.checkBoxall);
//                if (ListCompCondFinal.size()==ListCompCond.size()){checkBoxall.setChecked(true); }
//                else{checkBoxall.setChecked(false);}
                listCompCond = (RecyclerView) popupView.findViewById(R.id.list_Metodologia);
                txt_title = popupView.findViewById(R.id.txt_title);
                txt_title.setText("Comportamiento/Condición relacionado con:");
                id_cv_Otros = popupView.findViewById(R.id.id_cv_Otros);
                id_cv_Otros.setVisibility(View.GONE);
                et_otros = popupView.findViewById(R.id.et_otros);
                btn_cerrar = popupView.findViewById(R.id.btn_cerrar);



                if (ListCompCondFinal.size()> 0){
                    for (int j = 0; j<ListCompCondFinal.size(); j++){
                        for (int i = 0; i<ListCompCond.size(); i++){
                            if (ListCompCondFinal.get(j).Descripcion.equals(ListCompCond.get(i).Descripcion)) {
                                ListCompCond.get(i).estado = true;
                            }
                        }

                        if(ListCompCondFinal.get(j).Descripcion.contains("COMCON11")){
                            id_cv_Otros.setVisibility(View.VISIBLE);
                            String[] data =  ListCompCondFinal.get(j).Descripcion.split(":");

                            et_otros.setText(data[1]);
                        }else{
                            id_cv_Otros.setVisibility(View.GONE);

                        }

                    }
                }

                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                listCompCond.setLayoutManager(horizontalManager);
                //GlobalVariables.ListMetodologia

                checkAdapter2 = new CheckAdapter(getActivity(), ListCompCond, checkBoxall, id_cv_Otros);
                listCompCond.setAdapter(checkAdapter2);

                btn_cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), CheckAdapter.items.get(9).estado + " dat" , Toast.LENGTH_LONG).show();


                        ListCompCond = new ArrayList<>();
                        init_List_Comp();

                        if (!ListCompCondFinal.isEmpty()){

                            for (int i=0; i<ListCompCond.size(); i++){
                                for(int j=0; j<ListCompCondFinal.size(); j++) {
                                    if(ListCompCond.get(i).Descripcion.equals(ListCompCondFinal.get(j).Descripcion)) {
                                        ListCompCond.get(i).estado = true;
                                    }
                                }
                            }
                        }
                        //Toast.makeText(getActivity(), ListMetodologiaTemp.get(0).estado + "" , Toast.LENGTH_LONG).show();
                        checkAdapter2.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }});


                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), CheckAdapter.items.get(9).estado + " dat" , Toast.LENGTH_LONG).show();
                        //list_Metodologia

                        ListCompCondFinal = new ArrayList<>();
                        for (int i = 0; i<ListCompCond.size(); i++){
                            if (ListCompCond.get(i).estado) {
                                //setear el valor de otros
                                String data = et_otros.getText().toString();
                                if(ListCompCond.get(i).Descripcion.equals("COMCON11")){
                                    ListCompCondFinal.add(new SubDetalleModel("OBSR","",ListCompCond.get(i).Descripcion +" : "+ et_otros.getText().toString(),true));
                                }else {
                                    ListCompCondFinal.add(ListCompCond.get(i));
                                }
                            }
                        }

                        LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        List_dataComCond.setLayoutManager(horizontalManager);

                        compCondAadpter = new CompCondAadpter(getActivity(), ListCompCondFinal);
                        List_dataComCond.setAdapter(compCondAadpter);
                        popupWindow.dismiss();

                    }
                });

                checkBoxall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i=0; i < ListCompCond.size(); i++) {
                            ListCompCond.get(i).estado = checkBoxall.isChecked();
                        }
                        if (ListCompCond.get(9).estado && ListCompCond.get(9).Descripcion.equals("COMCON11")){
                            id_cv_Otros.setVisibility(View.VISIBLE);
                        }else {
                            id_cv_Otros.setVisibility(View.GONE);
                        }
                        checkAdapter2.notifyDataSetChanged();
                    }
                });
            }
        });

        return mView;
    }

    public void changueTipo(String Tipo){

       // Toast.makeText(getActivity(), Tipo, Toast.LENGTH_LONG).show();

        if(!StringUtils.isEmpty(Tipo)&& Tipo.equals("TO01")){
            mView.findViewById(R.id.id_Condicion).setVisibility(View.GONE);
            mView.findViewById(R.id.id_Acto).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.id_Estado).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.id_Error).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.id_accion).setVisibility(View.VISIBLE);


            mView.findViewById(R.id.id_codPET).setVisibility(View.GONE);
            mView.findViewById(R.id.id_comentRO).setVisibility(View.GONE);
            mView.findViewById(R.id.ll_list_coment).setVisibility(View.GONE);

            mView.findViewById(R.id.ll_tarea).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.ll_IS).setVisibility(View.GONE);

        }
        else if (!StringUtils.isEmpty(Tipo)&& Tipo.equals("TO02")) {
            mView.findViewById(R.id.id_Condicion).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.id_Acto).setVisibility(View.GONE);
            mView.findViewById(R.id.id_Estado).setVisibility(View.GONE);
            mView.findViewById(R.id.id_Error).setVisibility(View.GONE);
            mView.findViewById(R.id.id_accion).setVisibility(View.VISIBLE);

            mView.findViewById(R.id.id_codPET).setVisibility(View.GONE);
            mView.findViewById(R.id.id_comentRO).setVisibility(View.GONE);
            mView.findViewById(R.id.ll_list_coment).setVisibility(View.GONE);

            mView.findViewById(R.id.ll_tarea).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.ll_IS).setVisibility(View.GONE);
        }
        else if (!StringUtils.isEmpty(Tipo)&& Tipo.equals("TO03")) {
            mView.findViewById(R.id.id_Condicion).setVisibility(View.GONE);
            mView.findViewById(R.id.id_Acto).setVisibility(View.GONE);
            mView.findViewById(R.id.id_Estado).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.id_Error).setVisibility(View.VISIBLE);

            mView.findViewById(R.id.id_accion).setVisibility(View.GONE);

            mView.findViewById(R.id.id_codPET).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.id_comentRO).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.ll_list_coment).setVisibility(View.VISIBLE);

            mView.findViewById(R.id.ll_tarea).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.ll_IS).setVisibility(View.GONE);
        }
        else if (!StringUtils.isEmpty(Tipo)&& Tipo.equals("TO04")) {

            mView.findViewById(R.id.ll_tarea).setVisibility(View.GONE);
            mView.findViewById(R.id.ll_IS).setVisibility(View.VISIBLE);

        }




    }
/*
    @Override
    public void onResume() {
        super.onResume();
    }
*/
    /*
    public void showHideFragment(final Fragment fragment){

        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);

        if (fragment.isHidden()) {
            fragTransaction.show(fragment);
            Log.d("hidden","Show");
        } else {
            fragTransaction.hide(fragment);
            Log.d("Shown","Hide");
        }

        fragTransaction.commit();
    }
*/

    public void setdata(String Tipo){

        ListMetodologia = new ArrayList<>();
        init_List();
        ListCompCond = new ArrayList<>();
        init_List_Comp();

        if(!Tipo.equals("TO04")){


            //Toast.makeText(getActivity(), ListMetodologiaTemp.get(0).estado + "" , Toast.LENGTH_LONG).show();

            if(GlobalVariables.ObserbacionDetalle.Observacion!=null)txtObservacion.setText(GlobalVariables.ObserbacionDetalle.Observacion);
            //if(GlobalVariables.ObserbacionDetalle.Observacion!=null)txtObservacion.setText(GlobalVariables.ObserbacionDetalle.Observacion);

            if(GlobalVariables.ObserbacionDetalle.Accion!=null) {
                txtAccion.setText(GlobalVariables.ObserbacionDetalle.Accion);
                txtCodPET.setText(GlobalVariables.ObserbacionDetalle.Accion);
            }
            //observacion tarea
            if (GlobalVariables.ObserbacionDetalle.CodSubEstandar!=null)txtComentRO.setText(GlobalVariables.ObserbacionDetalle.CodSubEstandar);

            if(GlobalVariables.ObserbacionDetalle.ComOpt1!=null) ListComentarios.add(new ObsComentModel("", GlobalVariables.TipoComentario[0],GlobalVariables.ObserbacionDetalle.ComOpt1));
            if(GlobalVariables.ObserbacionDetalle.ComOpt2!=null) ListComentarios.add(new ObsComentModel("", GlobalVariables.TipoComentario[1],GlobalVariables.ObserbacionDetalle.ComOpt2));
            if(GlobalVariables.ObserbacionDetalle.ComOpt3!=null) ListComentarios.add(new ObsComentModel("", GlobalVariables.TipoComentario[2],GlobalVariables.ObserbacionDetalle.ComOpt3));

            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodActiRel))spinneActividad.setSelection(GlobalVariables.indexOf(GlobalVariables.Actividad_obs,GlobalVariables.ObserbacionDetalle.CodActiRel));
            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodHHA))spinnerHHA.setSelection(GlobalVariables.indexOf(GlobalVariables.HHA_obs,GlobalVariables.ObserbacionDetalle.CodHHA));

            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar))spinnerActo.setSelection(GlobalVariables.indexOf(GlobalVariables.Acto_obs,GlobalVariables.ObserbacionDetalle.CodSubEstandar));
            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar))spinnerCondicion.setSelection(GlobalVariables.indexOf(GlobalVariables.Condicion_obs,GlobalVariables.ObserbacionDetalle.CodSubEstandar));

            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodEstado))spinnerEstado.setSelection(GlobalVariables.indexOf(GlobalVariables.Estado_obs,GlobalVariables.ObserbacionDetalle.CodEstado));
            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodError))spinnerError.setSelection(GlobalVariables.indexOf(GlobalVariables.Error_obs,GlobalVariables.ObserbacionDetalle.CodError));

            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.StopWork))spinnerStopWork.setSelection(GlobalVariables.indexOf(GlobalVariables.StopWork_obs,GlobalVariables.ObserbacionDetalle.StopWork));

            LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(horizontalManager);
            obsComentAdapter = new ObsComentAdapter(getActivity(), ListComentarios);
            listView.setAdapter(obsComentAdapter);



        }else {
            // interaccion de seguridad


            //ObserbacionDetalle2


            if(GlobalVariables.ObserbacionDetalleIS.Observacion!=null)txtObservacion.setText(GlobalVariables.ObserbacionDetalleIS.Observacion);
            if(GlobalVariables.ObserbacionDetalleIS.CodError!=null)tx_ISempresa.setText(GlobalVariables.getDescripcion(GlobalVariables.Contrata, GlobalVariables.ObserbacionDetalleIS.CodError));
            if(GlobalVariables.ObserbacionDetalleIS.CodHHA!=null)txt_equipoInv.setText(GlobalVariables.ObserbacionDetalleIS.CodHHA);
            if(GlobalVariables.ObserbacionDetalleIS.CodSubEstandar!=null)txt_InteracSeg.setText(GlobalVariables.ObserbacionDetalleIS.CodSubEstandar);

            if(GlobalVariables.ObserbacionDetalleIS.StopWork!=null)sp_stopworkIS.setSelection(GlobalVariables.indexOf(GlobalVariables.StopWork_obs,GlobalVariables.ObserbacionDetalleIS.StopWork));
            if(GlobalVariables.ObserbacionDetalleIS.CodActiRel!=null)txt_detcomcon.setText(GlobalVariables.ObserbacionDetalleIS.CodActiRel);
            if(GlobalVariables.ObserbacionDetalleIS.Accion!=null)txt_accion_inmed.setText(GlobalVariables.ObserbacionDetalleIS.Accion);

            //Toast.makeText(getActivity(), GlobalVariables.ObserbacionDetalle2.CodError + " " + GlobalVariables.getDescripcion(GlobalVariables.C_Empresa, GlobalVariables.ObserbacionDetalle2.CodError) , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void success(String data, String Tipo) {

        if (Tipo.equals("TO04")){
            Gson gson = new Gson();
            String codigo_obs = GlobalVariables.Obserbacion.CodObservacion;
            ObsDetalleModel temp= gson.fromJson(data, ObsDetalleModel.class);

            GlobalVariables.ObserbacionDetalleIS = temp;
            if (GlobalVariables.ObserbacionDetalleIS == null)
                GlobalVariables.ObserbacionDetalleIS = new ObsDetalleModel();
            GlobalVariables.ObserbacionDetalleIS.CodObservacion = codigo_obs;
            GlobalVariables.StrObsDetalle = gson.toJson(GlobalVariables.ObserbacionDetalleIS);
            setdata(Tipo);

        } else if(Tipo.equals("3")){
            Gson gson = new Gson();
            GetSubDetalleModel getSubDetalleModel = gson.fromJson(data, GetSubDetalleModel.class);

            GlobalVariables.ListSubDetalleData = new ArrayList<>();
            GlobalVariables.ListSubDetalleData = getSubDetalleModel.Data;

            for (int i=0; i<GlobalVariables.ListSubDetalleData.size(); i++){
                if(GlobalVariables.ListSubDetalleData.get(i).CodTipo.equals("OBSR")){
                    ListMetodologiaFinal = new ArrayList<>();
                    ListMetodologiaFinal.add(new SubDetalleModel("OBSR","", GlobalVariables.ListSubDetalleData.get(i).CodSubtipo,true));
                } else if(GlobalVariables.ListSubDetalleData.get(i).CodTipo.equals("OBCC")){
                    ListCompCondFinal = new ArrayList<>();
                    ListCompCondFinal.add(new SubDetalleModel("OBSR","", GlobalVariables.ListSubDetalleData.get(i).CodSubtipo,true));
                }
            }

            LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            list_dataMetod.setLayoutManager(horizontalManager);
            obsMetodAdapter = new ObsMetodAdapter(getActivity(), ListMetodologiaFinal);
            list_dataMetod.setAdapter(obsMetodAdapter);


            LinearLayoutManager horizontalManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            List_dataComCond.setLayoutManager(horizontalManager2);

            compCondAadpter = new CompCondAadpter(getActivity(), ListCompCondFinal);
            List_dataComCond.setAdapter(compCondAadpter);

            //ListMetodologiaFinal.add(ListMetodologia.get(i));
//        ListMetodologia.add(new SubDetalleModel("OBSR","","GESRIES1", false));

        }else {
            Gson gson = new Gson();
            String codigo_obs = GlobalVariables.Obserbacion.CodObservacion;
            ObsDetalleModel temp= gson.fromJson(data, ObsDetalleModel.class);

            GlobalVariables.ObserbacionDetalle = temp;
            if (GlobalVariables.ObserbacionDetalle == null)   GlobalVariables.ObserbacionDetalle = new ObsDetalleModel();
            if (Tipo.equals("TO03")){
                String[] comRecOport = GlobalVariables.ObserbacionDetalle.CodSubEstandar.split(";",-1);
                if(!comRecOport[0].equals(""))GlobalVariables.ObserbacionDetalle.ComOpt1=comRecOport[0];
                if(!comRecOport[1].equals(""))GlobalVariables.ObserbacionDetalle.ComOpt2=comRecOport[1];
                if(!comRecOport[2].equals(""))GlobalVariables.ObserbacionDetalle.ComOpt3=comRecOport[2];
                GlobalVariables.ObserbacionDetalle.CodSubEstandar=comRecOport[3];
            }
            GlobalVariables.ObserbacionDetalle.CodObservacion = codigo_obs;
            GlobalVariables.StrObsDetalle = gson.toJson(GlobalVariables.ObserbacionDetalle);
            setdata(Tipo);

        }


    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                String tipo_dato=data.getStringExtra("tipo");

                String cod_contrata = data.getStringExtra("codContrata");
                String des_contrata = data.getStringExtra("desContrata");
                tx_ISempresa.setText(des_contrata);

                //GlobalVariables.AddInspeccion.CodContrata=cod_contrata;
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }



    public void init_List(){
//        ListMetodologia.add(new SubDetalleModel("OBSR","","GESRIES1", false));
//        ListMetodologia.add(new SubDetalleModel("OBSR","","GESRIES2", false));
//        ListMetodologia.add(new SubDetalleModel("OBSR","","GESRIES3", false));

        for (int i=0; i<GlobalVariables.GestionRiesg_obs.size(); i++){
            ListMetodologia.add(new SubDetalleModel("OBSR","",GlobalVariables.GestionRiesg_obs.get(i).CodTipo, false));
        }
    }

    public void init_List_Comp(){

        for (int i=0; i<GlobalVariables.CondicionComp_Obs.size(); i++){
            ListCompCond.add(new SubDetalleModel("OBCC","",GlobalVariables.CondicionComp_Obs.get(i).CodTipo, false));
        }

    }





}