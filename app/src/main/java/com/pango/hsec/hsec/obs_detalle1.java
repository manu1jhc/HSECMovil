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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pango.hsec.hsec.Busquedas.B_contrata;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.BuscarPersonaAdapter;
import com.pango.hsec.hsec.adapter.CartillaAdapter;
import com.pango.hsec.hsec.adapter.CheckAdapter;
import com.pango.hsec.hsec.adapter.CompCondAadpter;
import com.pango.hsec.hsec.adapter.CriterioCCAdapter;
import com.pango.hsec.hsec.adapter.ObsComentAdapter;
import com.pango.hsec.hsec.adapter.ObsMetodAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.CartillaModel;
import com.pango.hsec.hsec.model.CollectionResponse;
import com.pango.hsec.hsec.model.ControlCriticoModel;
import com.pango.hsec.hsec.model.GetSubDetalleModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsComentModel;
import com.pango.hsec.hsec.model.ObsDetalleModel;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.model.SubDetalleModel;
import com.pango.hsec.hsec.model.UsuarioModel;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class obs_detalle1 extends Fragment implements IActivity{
    //tarea
    TextView textView6,textView4,textView14,textView15,textView16,textView17,textView35,textView25,textView32pet,tx_sw, tx_eq_inv, tx_interac_seg,tx_swIS,textView_correccion,textView18,textView19,textView20,textGerencia;
    private static View mView;
    Spinner spinneActividad, spinnerHHA, spinnerActo,spinnerCondicion,spinnerEstado, spinnerError,spinnerStopWork,spinnerCorreccion,sp_stopworkIS,spinnerCovid,spinnerTipoEpp,spinnerEpp,spinnerGerencia;
    EditText txtObservacion,txtAccion, txtCodPET, txtComentRO, txt_detcomcon,txt_accion_inmed;
    CardView CarCorreccion,CarCovid,CarTipoEpp,CarEpp;
    ListView list_Cartillas;
    ImageButton btn_addComent;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    String description_coment = "";
    Maestro TipoComent;
    RecyclerView listView, list_dataMetod, List_dataComCond,List_CCritico;
    ObsComentAdapter obsComentAdapter;
    public ArrayList<SubDetalleModel> ListComentarios = new ArrayList<>();
    //interaccion de seguridad
    ImageButton btn_buscar_c, add_metodol, add_comp_riesgo,btn_Cartilla;
    public static final int REQUEST_CODE = 1;
    TextView tx_ISempresa,txt_Cartilla;
    EditText txt_equipoInv, txt_InteracSeg;
    public static ArrayList<SubDetalleModel> ListMetodologia = new ArrayList<>();

    public static ArrayList<SubDetalleModel> ListCompCond = new ArrayList<>();


    CheckAdapter checkAdapter, checkAdapter2;
    public ObsMetodAdapter obsMetodAdapter,compCondAadpter;
    public CartillaAdapter cartAdapter;
    public CriterioCCAdapter criterioAdapter;
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

        spinneActividad = (Spinner) mView.findViewById(R.id.sp_actividad);
        spinnerHHA = (Spinner) mView.findViewById(R.id.sp_hha);
        spinnerActo = (Spinner) mView.findViewById(R.id.sp_acto);
        spinnerCovid = (Spinner) mView.findViewById(R.id.sp_covid);
        spinnerTipoEpp = (Spinner) mView.findViewById(R.id.sp_TipoEpp);
        spinnerEpp = (Spinner) mView.findViewById(R.id.sp_Epp);
        spinnerCondicion = (Spinner) mView.findViewById(R.id.sp_condicion);
        spinnerEstado = (Spinner) mView.findViewById(R.id.sp_estado);
        spinnerError = (Spinner) mView.findViewById(R.id.sp_error);
        spinnerStopWork = (Spinner) mView.findViewById(R.id.sp_stopwork);
        spinnerCorreccion = (Spinner) mView.findViewById(R.id.sp_Correccion);
        sp_stopworkIS = (Spinner) mView.findViewById(R.id.sp_stopworkIS);
        spinnerGerencia=(Spinner) mView.findViewById(R.id.spinner_gerencia);


        CarCorreccion=(CardView) mView.findViewById(R.id.id_Correccion);
        CarCovid=(CardView) mView.findViewById(R.id.id_Covid);
        CarTipoEpp=(CardView) mView.findViewById(R.id.id_TipoEpp);
        CarEpp=(CardView) mView.findViewById(R.id.id_Epp);
        // Interaccion de seguridad
        btn_buscar_c = (ImageButton) mView.findViewById(R.id.btn_buscar_c);
        tx_ISempresa = (TextView) mView.findViewById(R.id.tx_ISempresa);
        add_metodol = (ImageButton) mView.findViewById(R.id.add_metodol);
        btn_Cartilla = (ImageButton) mView.findViewById(R.id.btn_Cartilla);
        list_dataMetod = (RecyclerView) mView.findViewById(R.id.list_Metodologia);
        add_comp_riesgo = (ImageButton) mView.findViewById(R.id.add_comp_riesgo);
        List_dataComCond = (RecyclerView) mView.findViewById(R.id.list_ComRiesgo);
        List_CCritico= (RecyclerView) mView.findViewById(R.id.list_CCritico);
        txt_detcomcon = mView.findViewById(R.id.txt_detcomcon);
        txt_accion_inmed = mView.findViewById(R.id.txt_accion_inmed);
        txt_equipoInv = mView.findViewById(R.id.txt_equipoInv);
        txt_InteracSeg = mView.findViewById(R.id.txt_InteracSeg);

        textView6=mView.findViewById(R.id.textView6);
        txt_Cartilla=mView.findViewById(R.id.tx_Cartilla);
        txt_Cartilla.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"SubProceso:<br>  Peligro Fatal:"));
        textView6.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Observación:"));
        if(Tipo.equals("TO04"))textView6.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Tarea Observada:"));
        textView4=mView.findViewById(R.id.textView4);
        textView4.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Acción:"));
        textView14=mView.findViewById(R.id.textView14);
        textView14.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Actividad Relacionada:"));
        textView15=mView.findViewById(R.id.textView15);
        textView15.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"HHA Relacionada:"));
        textView16=mView.findViewById(R.id.textView16);
        textView16.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Acto Sub- Estándar:"));
        textView35=mView.findViewById(R.id.textView35);
        textView35.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Estado:"));
        textView25=mView.findViewById(R.id.textView25);
        textView25.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Error:"));
        textView17=mView.findViewById(R.id.textView17);
        textView17.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Condición SubEstándar:"));
        textView_correccion=mView.findViewById(R.id.tx_sw2);
        textView_correccion.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Correccion:"));

        textView18=mView.findViewById(R.id.textView18);
        textView18.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Sub Tipo:"));
        textView19=mView.findViewById(R.id.textView19);
        textView19.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Tipo Protección:"));
        textView20=mView.findViewById(R.id.textView20);
        textView20.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"EPP:"));
        textGerencia=mView.findViewById(R.id.tx_gerencia);
        textGerencia.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Gerencia:"));

        //tarea
        textView32pet = mView.findViewById(R.id.textView32pet);
        tx_sw = mView.findViewById(R.id.tx_sw);

        textView32pet.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Código PET:"));
        tx_sw.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Stop Work:"));

//interaccon de seguridad

        tx_eq_inv= mView.findViewById(R.id.tx_eq_inv);
        tx_interac_seg = mView.findViewById(R.id.tx_interac_seg);
        tx_swIS = mView.findViewById(R.id.tx_swIS);

        tx_eq_inv.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Equipo Involucrado:"));
        tx_interac_seg.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Interacción de Seguridad:"));

        tx_swIS.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Stop Work:"));
/////////////////////////////////////////////
        ArrayAdapter adapterActividadObs = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Actividad_obs);
        adapterActividadObs.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinneActividad.setAdapter(adapterActividadObs);

        ArrayAdapter adapterHHA = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.HHA_obs);
        adapterHHA.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerHHA.setAdapter(adapterHHA);

        ArrayAdapter adapterActo = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Reverse(GlobalVariables.Acto_obs));
        adapterActo.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerActo.setAdapter(adapterActo);

        ArrayAdapter adapterCovid = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Covid_obs);
        adapterCovid.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerCovid.setAdapter(adapterCovid);

        ArrayAdapter adapterTipoEpp = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.TipoEpp_obs);
        adapterTipoEpp.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerTipoEpp.setAdapter(adapterTipoEpp);

        ArrayAdapter adapterEpp = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Epp_obs);
        adapterEpp.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerEpp.setAdapter(adapterEpp);

        ArrayAdapter adapterCondicion = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Reverse(GlobalVariables.Condicion_obs));
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

        ArrayAdapter adapterCorreccion = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Correccion_obs);
        adapterCorreccion.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerCorreccion.setAdapter(adapterCorreccion);

        ArrayAdapter adapterStopWorkIS = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.StopWork_obs);
        adapterStopWorkIS.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        sp_stopworkIS.setAdapter(adapterStopWorkIS);

        ArrayAdapter adapterGerencia = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.Gerencia);
        adapterGerencia.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerGerencia.setAdapter(adapterGerencia);

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
        txt_equipoInv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO04"))GlobalVariables.ObserbacionDetalle.CodHHA = txt_equipoInv.getText().toString();
            }
        });
        txt_InteracSeg.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO04"))GlobalVariables.ObserbacionDetalle.CodSubEstandar = txt_InteracSeg.getText().toString();
            }
        });
        txt_detcomcon.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO04") && GlobalVariables.Obserbacion.CodSubTipo.equals("1"))
                    GlobalVariables.ObserbacionDetalle.CodActiRel = txt_detcomcon.getText().toString();
            }
        });
        txt_accion_inmed.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO04")) GlobalVariables.ObserbacionDetalle.Accion = txt_accion_inmed.getText().toString();
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
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO03"))GlobalVariables.ObserbacionDetalle.CodSubEstandar = txtComentRO.getText().toString();
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
               if(GlobalVariables.Obserbacion.CodTipo.equals("TO01") || GlobalVariables.Obserbacion.CodTipo.equals("TO02"))
                   GlobalVariables.ObserbacionDetalle.Accion = txtAccion.getText().toString();
            }
        });
        txtCodPET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO03")) GlobalVariables.ObserbacionDetalle.Accion = txtCodPET.getText().toString();
            }
        });
        spinnerGerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_gerencia) ).getSelectedItem();
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO04"))GlobalVariables.ObserbacionDetalle.Accion=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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
                    if(Tipo.Descripcion.contains("COVID")){
                        GlobalVariables.Covid_obs.clear();
                        GlobalVariables.Covid_obs.add(new Maestro("-  Seleccione  -"));
                        if (Tipo.CodTipo != null )
                        for (Maestro item : GlobalVariables.Covid) {
                            if(item.CodTipo.contains(Tipo.CodTipo))
                                GlobalVariables.Covid_obs.add(item);
                        }
                        adapterCovid.notifyDataSetChanged();
                        if(GlobalVariables.ObserbacionDetalle.ComOpt1!=null)spinnerCovid.setSelection(GlobalVariables.indexOf(GlobalVariables.Covid_obs,GlobalVariables.ObserbacionDetalle.ComOpt1));
                        else spinnerCovid.setSelection(0);
                        CarCovid.setVisibility(View.VISIBLE);
                        CarTipoEpp.setVisibility(View.GONE);
                        CarEpp.setVisibility(View.GONE);
                    }
                    else if(Tipo.Descripcion.contains("EPP")){
                        if(GlobalVariables.ObserbacionDetalle.ComOpt1!=null){
                            String TipoEppCod=GlobalVariables.ObserbacionDetalle.ComOpt1.split("\\.")[0];
                            spinnerTipoEpp.setSelection(GlobalVariables.indexOf(GlobalVariables.TipoEpp_obs,TipoEppCod));
                        }
                        else spinnerTipoEpp.setSelection(0);
                        CarTipoEpp.setVisibility(View.VISIBLE);
                        CarEpp.setVisibility(View.VISIBLE);
                        CarCovid.setVisibility(View.GONE);
                    }
                    else {
                        CarCovid.setVisibility(View.GONE);
                        CarTipoEpp.setVisibility(View.GONE);
                        CarEpp.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerCovid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_covid) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.ComOpt1=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerTipoEpp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(GlobalVariables.Obserbacion.CodTipo.equals("TO01")){
                    Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_TipoEpp) ).getSelectedItem();

                        GlobalVariables.Epp_obs.clear();
                        GlobalVariables.Epp_obs.add(new Maestro("-  Seleccione  -"));
                        if (Tipo.CodTipo != null)
                            for (Maestro item : GlobalVariables.EPP) {
                                if(item.CodTipo.contains(Tipo.CodTipo+'.'))GlobalVariables.Epp_obs.add(item);
                            }
                        adapterEpp.notifyDataSetChanged();
                        if(GlobalVariables.ObserbacionDetalle.ComOpt1!=null)spinnerEpp.setSelection(GlobalVariables.indexOf(GlobalVariables.Epp_obs,GlobalVariables.ObserbacionDetalle.ComOpt1));
                        else spinnerEpp.setSelection(0);
                        //GlobalVariables.ObserbacionDetalle.ComOpt1=Tipo.CodTipo;
                    //}
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerEpp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_Epp) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.ComOpt1=Tipo.CodTipo;
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
                    if(Tipo.Descripcion.contains("COVID")){
                        GlobalVariables.Covid_obs.clear();
                        GlobalVariables.Covid_obs.add(new Maestro("-  Seleccione  -"));
                        if (Tipo.CodTipo != null )
                        for (Maestro item : GlobalVariables.Covid) {
                            if(item.CodTipo.contains(Tipo.CodTipo))
                                GlobalVariables.Covid_obs.add(item);
                        }
                        adapterCovid.notifyDataSetChanged();
                        if(GlobalVariables.ObserbacionDetalle.ComOpt1!=null)spinnerCovid.setSelection(GlobalVariables.indexOf(GlobalVariables.Covid_obs,GlobalVariables.ObserbacionDetalle.ComOpt1));
                        else spinnerCovid.setSelection(0);
                        CarCovid.setVisibility(View.VISIBLE);
                    }
                    else CarCovid.setVisibility(View.GONE);
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
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_stopwork) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.StopWork=Tipo.CodTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
        spinnerCorreccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_Correccion) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.CodCorreccion=Tipo.CodTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
        sp_stopworkIS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_stopworkIS) ).getSelectedItem();
                GlobalVariables.ObserbacionDetalle.StopWork=Tipo.CodTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        if(GlobalVariables.Obserbacion==null)changueTipo(Tipo,"");
        if(GlobalVariables.ObjectEditable){ // load data of server

            if(GlobalVariables.ObserbacionDetalle.CodObservacion==null)
            {
                String url= GlobalVariables.Url_base+"Observaciones/GetDetalle/"+codigo_obs;
                ActivityController obj = new ActivityController("get", url, obs_detalle1.this,getActivity());
                obj.execute(Tipo);

                if (Tipo.equals("TO04")){
                    String url2= GlobalVariables.Url_base+"Observaciones/GetsSubDetalle/"+codigo_obs;
                    ActivityController obj1 = new ActivityController("get", url2, obs_detalle1.this,getActivity());
                    obj1.execute("1");
                }
            }
           // else setdata(Tipo);

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
                GlobalVariables.SubDetalleIS= new ArrayList<>();
            }
            //setdata(GlobalVariables.Obserbacion.CodTipo==null?Tipo:GlobalVariables.Obserbacion.CodTipo);
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
                layoutInflater = (LayoutInflater) mView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
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

                ArrayAdapter adapterCometarios = new ArrayAdapter(getActivity().getBaseContext(), R.layout.custom_spinner_item, GlobalVariables.O_Comentarios);
                adapterCometarios.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
                spinnerComent.setAdapter(adapterCometarios);


                spinnerComent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        TipoComent = (Maestro) ((Spinner) popupView.findViewById(R.id.sp_tipoComent)).getSelectedItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });

                btn_Cerrar = (ImageButton) popupView.findViewById(R.id.btn_close);
                btn_Cerrar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        description_coment = txt_description.getText().toString();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(txt_description.getWindowToken(), 0);
                        //txt_description
                        if (TipoComent.CodTipo.equals("0") || description_coment.trim().equals(""))
                        {
                            Toast.makeText(getActivity(), "Los campos no pueden estar vacios", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (ListComentarios.size() < 3) {
                            boolean datarep = false;
                            for (SubDetalleModel obsm : ListComentarios) {
                                if (obsm.Codigo.equals(TipoComent.CodTipo)) { //obsm.CodTipo!=null&&
                                    datarep = true;
                                }
                            }

                            if (datarep)
                                    Toast.makeText(getActivity(), "El tipo de comentario ya existe", Toast.LENGTH_LONG).show();
                            else {
                                    obsComentAdapter.add(new SubDetalleModel(TipoComent.CodTipo,"COM", TipoComent.Descripcion, description_coment));
                                    switch (TipoComent.CodTipo){
                                        case "1": GlobalVariables.ObserbacionDetalle.ComOpt1=description_coment; break;
                                        case "2": GlobalVariables.ObserbacionDetalle.ComOpt2=description_coment; break;
                                        case "3": GlobalVariables.ObserbacionDetalle.ComOpt3=description_coment; break;
                                    }
                                    obsComentAdapter.notifyDataSetChanged();
                                    popupWindow.dismiss();
                                }
                        }
                        else Toast.makeText(getActivity(), "Limite de comentarios", Toast.LENGTH_LONG).show();
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
                checkBoxall.setChecked(false);
                init_List();
                int cont=0;
                for(SubDetalleModel item2 : GlobalVariables.SubDetalleIS)
                    if(item2.CodTipo.equals("OBSR"))
                    {
                        cont++;
                        for(SubDetalleModel item : ListMetodologia)
                            if(item2.CodSubtipo.equals(item.CodSubtipo)) {
                                item.Check=true;
                                continue;
                            }
                    }

                if(ListMetodologia.size()==cont)checkBoxall.setChecked(true);

                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                list_Metodologia.setLayoutManager(horizontalManager);
                checkAdapter = new CheckAdapter(getActivity(), ListMetodologia, checkBoxall, id_cv_Otros);
                list_Metodologia.setAdapter(checkAdapter);

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
                            if(!item2.CodTipo.equals("OBSR")) itemsOther.add(item2);
                        }
                        GlobalVariables.SubDetalleIS.clear();
                        GlobalVariables.SubDetalleIS.addAll(itemsOther);
                        for(SubDetalleModel item : ListMetodologia) {
                            if(item.Check){
                                item.Descripcion=null;
                                obsMetodAdapter.add(item);
                            }
                        }
                        obsMetodAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

                checkBoxall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(SubDetalleModel item : ListMetodologia) {
                            item.Check=checkBoxall.isChecked();
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
                listCompCond = (RecyclerView) popupView.findViewById(R.id.list_Metodologia);
                txt_title = popupView.findViewById(R.id.txt_title);
                txt_title.setText("Comportamiento/Condición relacionado con:");
                id_cv_Otros = popupView.findViewById(R.id.id_cv_Otros);
                id_cv_Otros.setVisibility(View.GONE);
                et_otros = popupView.findViewById(R.id.et_otros);
                btn_cerrar = popupView.findViewById(R.id.btn_cerrar);

                init_List_Comp();
                int cont=0;
                for(SubDetalleModel item2 : GlobalVariables.SubDetalleIS){
                    if(item2.CodTipo.equals("OBCC"))
                    {
                        cont++;
                        for(SubDetalleModel item : ListCompCond) {
                            if(item2.CodSubtipo.equals(item.CodSubtipo)) {
                                item.Check=true;
                                continue;
                            }
                        }
                        if(item2.CodSubtipo.equals("COMCON11")){
                            id_cv_Otros.setVisibility(View.VISIBLE);
                            et_otros.setText(item2.Descripcion);
                        }
                    }
                }
                if(ListCompCond.size()==cont)checkBoxall.setChecked(true);

                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                listCompCond.setLayoutManager(horizontalManager);
                checkAdapter2 = new CheckAdapter(getActivity(), ListCompCond, checkBoxall, id_cv_Otros);
                listCompCond.setAdapter(checkAdapter2);

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
                            if(!item2.CodTipo.equals("OBCC")) itemsOther.add(item2);
                        }
                        GlobalVariables.SubDetalleIS.clear();
                        GlobalVariables.SubDetalleIS.addAll(itemsOther);

                        for(SubDetalleModel item : ListCompCond) {
                            if(item.Check) {
                                if(item.CodSubtipo.equals("COMCON11")) item.Descripcion=et_otros.getText().toString();
                                else item.Descripcion=null;
                                compCondAadpter.add(item);
                            }
                        }
                        compCondAadpter.notifyDataSetChanged();
                        popupWindow.dismiss();

                    }
                });

                checkBoxall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(SubDetalleModel item : ListCompCond) {
                            item.Check=checkBoxall.isChecked();
                        }
                        id_cv_Otros.setVisibility(checkBoxall.isChecked()?View.VISIBLE:View.GONE);
                        checkAdapter2.notifyDataSetChanged();
                    }
                });
            }
        });
        btn_Cartilla.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ImageButton btn_Cerrar;
                Button btn_mascartillas,btn_cancelar;
                ImageButton btn_cerrar;

                layoutInflater =(LayoutInflater)mView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_cartillas, null);
                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);
                popupWindow.showAtLocation(add_metodol, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                popupWindow.setOutsideTouchable(true);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                btn_cancelar = popupView.findViewById(R.id.btn_cancelar);
                btn_cerrar = popupView.findViewById(R.id.btn_close);

                list_Cartillas = (ListView) popupView.findViewById(R.id.list_Cartillas);


                init_List();
                int cont=0;
                for(SubDetalleModel item2 : GlobalVariables.SubDetalleIS)
                    if(item2.CodTipo.equals("OBSR"))
                    {
                        cont++;
                        for(SubDetalleModel item : ListMetodologia)
                            if(item2.CodSubtipo.equals(item.CodSubtipo)) {
                                item.Check=true;
                                continue;
                            }
                    }

                String url2= GlobalVariables.Url_base+"Observaciones/BuscarCartillas?CodCartilla="+GlobalVariables.Obserbacion.CodObservadoPor+"&PeligroFatal="+GlobalVariables.Obserbacion.CodSubTipo;
                ActivityController obj2 = new ActivityController("get", url2, obs_detalle1.this,getActivity());
                obj2.execute("2");

                list_Cartillas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CartillaModel item= cartAdapter.getItem(position);
                        String url3= GlobalVariables.Url_base+"Observaciones/GetRespControlCritico?id="+GlobalVariables.Obserbacion.CodObservacion+"&Cartilla="+item.CodCartilla;
                        ActivityController obj3 = new ActivityController("get", url3, obs_detalle1.this,getActivity());
                        obj3.execute("4");

                        GlobalVariables.ObserbacionDetalle.CodActiRel=item.PeligroFatal;
                        GlobalVariables.ObserbacionDetalle.CodEstado=item.CodCartilla;
                        String Titulo="SubProceso: "+item.CodCartilla.trim()+" <br>  Peligro Fatal:"+item.Estado.trim();
                        txt_Cartilla.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+Titulo));
                        popupWindow.dismiss();
                    }
                });

                btn_cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }});

                btn_cancelar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }});
            }
        });
        return mView;
    }

    public void init_List(){
        ListMetodologia.clear();
        for (int i=0; i<GlobalVariables.GestionRiesg_obs.size(); i++)
            ListMetodologia.add(new SubDetalleModel("OBSR",GlobalVariables.GestionRiesg_obs.get(i).CodTipo,GlobalVariables.GestionRiesg_obs.get(i).Descripcion));
    }

    public void init_List_Comp(){
        ListCompCond.clear();
        for (int i=0; i<GlobalVariables.CondicionComp_Obs.size(); i++)
            ListCompCond.add(new SubDetalleModel("OBCC",GlobalVariables.CondicionComp_Obs.get(i).CodTipo,GlobalVariables.CondicionComp_Obs.get(i).Descripcion));
    }

    public void changueTipo(String Tipo,String Subtipo){
       // Toast.makeText(getActivity(), Tipo, Toast.LENGTH_LONG).show();
        textView6.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Observación:"));
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
            CarTipoEpp.setVisibility(View.GONE);
            CarEpp.setVisibility(View.GONE);

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
        else if(!StringUtils.isEmpty(Tipo)&& Tipo.equals("TO04"))
        {
            textView6.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Tarea Observada:"));
            mView.findViewById(R.id.ll_tarea).setVisibility(View.GONE);
            mView.findViewById(R.id.ll_IS).setVisibility(View.VISIBLE);
            if(!StringUtils.isEmpty(Subtipo)&&(Subtipo.equals("2")||Subtipo.equals("3"))){
                mView.findViewById(R.id.ll_IS1).setVisibility(View.GONE);
                mView.findViewById(R.id.IS_CCritico).setVisibility(View.VISIBLE);
                if(Subtipo.equals("2")) mView.findViewById(R.id.id_Obs).setVisibility(View.VISIBLE);
                else mView.findViewById(R.id.id_Obs).setVisibility(View.GONE);
            }
            else{
                mView.findViewById(R.id.IS_CCritico).setVisibility(View.GONE);
                mView.findViewById(R.id.ll_IS1).setVisibility(View.VISIBLE);
            }
        }
        setdata(Tipo,Subtipo);
    }

    public void setdata(String Tipo,String Subtipo){

        if(GlobalVariables.ObserbacionDetalle.Observacion!=null)txtObservacion.setText(GlobalVariables.ObserbacionDetalle.Observacion);
        if(GlobalVariables.ObserbacionDetalle.Accion!=null) {
            txtAccion.setText(GlobalVariables.ObserbacionDetalle.Accion);
            txtCodPET.setText(GlobalVariables.ObserbacionDetalle.Accion);
            txt_accion_inmed.setText(GlobalVariables.ObserbacionDetalle.Accion);

        }
        else{
            txtAccion.setText("");  txtCodPET.setText("");  txt_accion_inmed.setText("");
        }
        //observacion tarea
        if (GlobalVariables.ObserbacionDetalle.CodSubEstandar!=null)
        {
            txt_InteracSeg.setText(GlobalVariables.ObserbacionDetalle.CodSubEstandar);
            txtComentRO.setText(GlobalVariables.ObserbacionDetalle.CodSubEstandar);
        }
        else{
            txt_InteracSeg.setText("");txtComentRO.setText("");
        }
        if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodActiRel))
        {
            spinneActividad.setSelection(GlobalVariables.indexOf(GlobalVariables.Actividad_obs,GlobalVariables.ObserbacionDetalle.CodActiRel));
            if(Tipo.equals("TO04")&&Subtipo.equals("1"))txt_detcomcon.setText(GlobalVariables.ObserbacionDetalle.CodActiRel);
        }
        else{
            spinneActividad.setSelection(0);
            txt_detcomcon.setText("");
        }
        if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodHHA)){
            spinnerHHA.setSelection(GlobalVariables.indexOf(GlobalVariables.HHA_obs,GlobalVariables.ObserbacionDetalle.CodHHA));
            txt_equipoInv.setText(GlobalVariables.ObserbacionDetalle.CodHHA);
        }
        else{
            spinnerHHA.setSelection(0);
            txt_equipoInv.setText("");
        }

        if (GlobalVariables.ObserbacionDetalle.StopWork != null)
        {
            sp_stopworkIS.setSelection(GlobalVariables.indexOf(GlobalVariables.StopWork_obs, GlobalVariables.ObserbacionDetalle.StopWork));
            spinnerStopWork.setSelection(GlobalVariables.indexOf(GlobalVariables.StopWork_obs,GlobalVariables.ObserbacionDetalle.StopWork));
        }

        if (GlobalVariables.ObserbacionDetalle.CodCorreccion != null)
        {
            spinnerCorreccion.setSelection(GlobalVariables.indexOf(GlobalVariables.Correccion_obs, GlobalVariables.ObserbacionDetalle.CodCorreccion));
        }

        if(Tipo.equals("TO03")) CarCorreccion.setVisibility(View.GONE);
        else CarCorreccion.setVisibility(View.VISIBLE);
        if(Tipo.equals("TO04")) {// interaccion de seguridad
            if (GlobalVariables.ObserbacionDetalle.CodError != null)
                tx_ISempresa.setText(GlobalVariables.getDescripcion(GlobalVariables.Contrata, GlobalVariables.ObserbacionDetalle.CodError));
            int SubTipo= Integer.parseInt(Subtipo);
            if(SubTipo>1) // control Critico
            {
                String PeligroF="";
                if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodActiRel)) PeligroF= GlobalVariables.getDescripcion(GlobalVariables.Peligro_fatal,GlobalVariables.ObserbacionDetalle.CodActiRel);
                String Titulo="SubProceso: "+(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodEstado)?"":GlobalVariables.ObserbacionDetalle.CodEstado)+" <br>  Peligro Fatal:"+PeligroF;
                txt_Cartilla.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+Titulo));

                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                List_CCritico.setLayoutManager(horizontalManager);
                criterioAdapter = new CriterioCCAdapter(getActivity(), GlobalVariables.CriteriosEvalCC,(observacion_edit) this.getActivity());
                List_CCritico.setAdapter(criterioAdapter);

                if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.Accion)) spinnerGerencia.setSelection(GlobalVariables.indexOf(GlobalVariables.Gerencia,GlobalVariables.ObserbacionDetalle.Accion));
                else {
                    Gson gson = new Gson();
                    UsuarioModel UserLoged= gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
                    spinnerGerencia.setSelection(GlobalVariables.indexOf(GlobalVariables.Gerencia,UserLoged.Estado));
                }
            }
            else{ // Interaccion de seguridad
                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                list_dataMetod.setLayoutManager(horizontalManager);
                obsMetodAdapter = new ObsMetodAdapter(getActivity(), GlobalVariables.SubDetalleIS, "OBSR",(observacion_edit) this.getActivity());
                list_dataMetod.setAdapter(obsMetodAdapter);

                LinearLayoutManager horizontalManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                List_dataComCond.setLayoutManager(horizontalManager1);
                compCondAadpter = new ObsMetodAdapter(getActivity(), GlobalVariables.SubDetalleIS,"OBCC",(observacion_edit) this.getActivity());
                List_dataComCond.setAdapter(compCondAadpter);
            }
        }
        else {
            ListComentarios.clear();
            if(GlobalVariables.ObserbacionDetalle.ComOpt1!=null) ListComentarios.add(new SubDetalleModel("1","COM", GlobalVariables.TipoComentario[0],GlobalVariables.ObserbacionDetalle.ComOpt1));
            if(GlobalVariables.ObserbacionDetalle.ComOpt2!=null) ListComentarios.add(new SubDetalleModel("2", "COM",GlobalVariables.TipoComentario[1],GlobalVariables.ObserbacionDetalle.ComOpt2));
            if(GlobalVariables.ObserbacionDetalle.ComOpt3!=null) ListComentarios.add(new SubDetalleModel("3", "COM",GlobalVariables.TipoComentario[2],GlobalVariables.ObserbacionDetalle.ComOpt3));

            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar)&& Tipo.equals("TO01"))spinnerActo.setSelection(GlobalVariables.indexOf(GlobalVariables.Reverse(GlobalVariables.Acto_obs),GlobalVariables.ObserbacionDetalle.CodSubEstandar));
            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar)&& Tipo.equals("TO02"))spinnerCondicion.setSelection(GlobalVariables.indexOf(GlobalVariables.Reverse(GlobalVariables.Condicion_obs),GlobalVariables.ObserbacionDetalle.CodSubEstandar));

            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodEstado))spinnerEstado.setSelection(GlobalVariables.indexOf(GlobalVariables.Estado_obs,GlobalVariables.ObserbacionDetalle.CodEstado));
            if(!StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodError))spinnerError.setSelection(GlobalVariables.indexOf(GlobalVariables.Error_obs,GlobalVariables.ObserbacionDetalle.CodError));

            LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(horizontalManager);
            obsComentAdapter = new ObsComentAdapter(getActivity(), ListComentarios);
            listView.setAdapter(obsComentAdapter);
        }


        GlobalVariables.ObserbacionDetalle.CodTipo=Tipo;
    }
    ArrayList<Integer> actives= new ArrayList<>();
    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {

        if (Tipo.equals("TO04")){
            Gson gson = new Gson();
            String codigo_obs = GlobalVariables.Obserbacion.CodObservacion;
            ObsDetalleModel temp= gson.fromJson(data, ObsDetalleModel.class);
            GlobalVariables.ObserbacionDetalle = temp;

            if (GlobalVariables.ObserbacionDetalle == null)
                GlobalVariables.ObserbacionDetalle = new ObsDetalleModel();
            GlobalVariables.ObserbacionDetalle.ComOpt1=GlobalVariables.ObserbacionDetalle.CodObservacion;
            GlobalVariables.ObserbacionDetalle.ComOpt2=GlobalVariables.ObserbacionDetalle.CodTipo;

            GlobalVariables.ObserbacionDetalle.CodObservacion = GlobalVariables.Obserbacion.CodObservacion;
            GlobalVariables.ObserbacionDetalle.CodTipo=GlobalVariables.Obserbacion.CodTipo;
            GlobalVariables.StrObsDetalle = gson.toJson(GlobalVariables.ObserbacionDetalle);

           // if(actives.size()==2) setdata("TO04",GlobalVariables.ObserbacionDetalle.CodHHA);
            if(Integer.parseInt(GlobalVariables.Obserbacion.CodSubTipo)>1){
                String url3= GlobalVariables.Url_base+"Observaciones/GetRespControlCritico?id="+codigo_obs+"&Cartilla=";
                ActivityController obj2 = new ActivityController("get", url3, obs_detalle1.this,getActivity());
                obj2.execute("3");
            }
            else  {
                actives.add(1);
                if(actives.size()==2) setdata("TO04",GlobalVariables.ObserbacionDetalle.CodHHA);
            }
        } else if(Tipo.equals("1")){ // subdetalle IS
            Gson gson = new Gson();
            GetSubDetalleModel getSubDetalleModel = gson.fromJson(data, GetSubDetalleModel.class);
            GlobalVariables.SubDetalleIS = getSubDetalleModel.Data;
            for(SubDetalleModel item:GlobalVariables.SubDetalleIS)
                GlobalVariables.StrSubDetalleIS.add((SubDetalleModel)item.clone());
            actives.add(1);
            if(actives.size()==2)setdata("TO04",GlobalVariables.ObserbacionDetalle.CodHHA);
        }else if(Tipo.equals("2")){  // getCartillas
            Gson gson = new Gson();
            Type listType = new TypeToken<CollectionResponse<CartillaModel>>(){}.getType();
            CollectionResponse<CartillaModel> getCartillas = gson.fromJson(data, listType);
            cartAdapter = new CartillaAdapter(this.getActivity(),getCartillas.Data);
            list_Cartillas.setAdapter(cartAdapter);
        }else if(Tipo.equals("3")){  // getCRespuestasCArtilla edit
            Gson gson = new Gson();
            Type listType = new TypeToken<CollectionResponse<ControlCriticoModel>>(){}.getType();
            CollectionResponse<ControlCriticoModel> getCartillas = gson.fromJson(data, listType);
            GlobalVariables.CriteriosEvalCC = getCartillas.Data;
            for(ControlCriticoModel item:GlobalVariables.CriteriosEvalCC)
                GlobalVariables.StrCriteriosEvalCC.add((ControlCriticoModel)item.clone());
            actives.add(1);
            if(actives.size()==2)setdata(Tipo,GlobalVariables.ObserbacionDetalle.CodHHA);
        }else if(Tipo.equals("4")){  // getCRespuestasCArtilla
            Gson gson = new Gson();
            Type listType = new TypeToken<CollectionResponse<ControlCriticoModel>>(){}.getType();
            CollectionResponse<ControlCriticoModel> getCartillas = gson.fromJson(data, listType);
            GlobalVariables.CriteriosEvalCC = getCartillas.Data;
            LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            List_CCritico.setLayoutManager(horizontalManager);
            criterioAdapter = new CriterioCCAdapter(getActivity(), GlobalVariables.CriteriosEvalCC,(observacion_edit) this.getActivity());
            List_CCritico.setAdapter(criterioAdapter);
        }else {
            Gson gson = new Gson();
            ObsDetalleModel temp= gson.fromJson(data, ObsDetalleModel.class);

            GlobalVariables.ObserbacionDetalle = temp;
            GlobalVariables.ObserbacionDetalle.CodObservacion=GlobalVariables.Obserbacion.CodObservacion;
            GlobalVariables.ObserbacionDetalle.CodTipo=GlobalVariables.Obserbacion.CodTipo;
            if (GlobalVariables.ObserbacionDetalle == null)   GlobalVariables.ObserbacionDetalle = new ObsDetalleModel();
            if (Tipo.equals("TO03")){
                String[] comRecOport = GlobalVariables.ObserbacionDetalle.CodSubEstandar.split(";",-1);
                if(!comRecOport[0].equals(""))GlobalVariables.ObserbacionDetalle.ComOpt1=comRecOport[0];
                if(!comRecOport[1].equals(""))GlobalVariables.ObserbacionDetalle.ComOpt2=comRecOport[1];
                if(!comRecOport[2].equals(""))GlobalVariables.ObserbacionDetalle.ComOpt3=comRecOport[2];
                GlobalVariables.ObserbacionDetalle.CodSubEstandar=comRecOport[3];
            }
            GlobalVariables.StrObsDetalle = gson.toJson(GlobalVariables.ObserbacionDetalle);
            setdata(Tipo,"0");
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
                GlobalVariables.ObserbacionDetalle.CodError=cod_contrata;
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


}