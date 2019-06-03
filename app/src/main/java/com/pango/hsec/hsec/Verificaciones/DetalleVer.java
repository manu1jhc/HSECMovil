package com.pango.hsec.hsec.Verificaciones;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.Maestro;

import org.apache.commons.lang3.StringUtils;


public class DetalleVer extends Fragment{
    TextView textView6,textView4,textView5;
    private static View mView;
    Spinner spinneStopWork;
    EditText txtObservacion,txtAccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_detallever, container,false);
        txtObservacion=(EditText) mView.findViewById(R.id.txt_observacion);
        txtAccion=(EditText) mView.findViewById(R.id.txt_accion);

        textView6=mView.findViewById(R.id.textView6);
        textView6.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Observación:"));

        textView4=mView.findViewById(R.id.textView4);
        textView4.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Acción:"));
        textView5=mView.findViewById(R.id.textView5);
        textView5.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(getActivity(), R.color.colorRojo)+"> * </font>"+"Stop Work:"));


        spinneStopWork = (Spinner) mView.findViewById(R.id.sp_stopwork);

        ArrayAdapter adapterStopWork = new ArrayAdapter(getActivity().getBaseContext(),R.layout.custom_spinner_item, GlobalVariables.StopWork_obs);
        adapterStopWork.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinneStopWork.setAdapter(adapterStopWork);

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
                GlobalVariables.Verificacion.Observacion = txtObservacion.getText().toString();
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
                GlobalVariables.Verificacion.Accion = txtAccion.getText().toString();
            }
        });

        spinneStopWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.sp_stopwork) ).getSelectedItem();
                GlobalVariables.Verificacion.StopWork=Tipo.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        return mView;
    }

    public void setdata(){
        if(!StringUtils.isEmpty(GlobalVariables.Verificacion.Observacion))txtObservacion.setText(GlobalVariables.Verificacion.Observacion);
        if(!StringUtils.isEmpty(GlobalVariables.Verificacion.Accion))txtAccion.setText(GlobalVariables.Verificacion.Accion);
        if(!StringUtils.isEmpty(GlobalVariables.Verificacion.StopWork))spinneStopWork.setSelection(GlobalVariables.indexOf(GlobalVariables.StopWork_obs,GlobalVariables.Verificacion.StopWork));
    }
}