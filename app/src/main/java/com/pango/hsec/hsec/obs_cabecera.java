package com.pango.hsec.hsec;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.Maestro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class obs_cabecera extends Fragment{

    private static View mView, dView;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    DialogFragment newFragment;
    Button botonEscogerFecha;
    Spinner spinnerArea, spinnerNivel, spinnerUbica,spinnerSububic,spinnerUbicEspec, spinnerTipoObs;
    String Ubicacionfinal="",TipoObs;
    public ArrayAdapter adapterUbicEspc,adapterSubN;


    public static final obs_cabecera newInstance(String sampleText) {
        obs_cabecera f = new obs_cabecera();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_cabecera, container, false);

        String sampleText = getArguments().getString("bString");

        spinnerArea = (Spinner) mView.findViewById(R.id.spinner_area);
        spinnerNivel = (Spinner) mView.findViewById(R.id.spinner_NivelR);
        spinnerUbica = (Spinner) mView.findViewById(R.id.spinner_ubic);
        spinnerSububic = (Spinner) mView.findViewById(R.id.spinner_sububic);
        spinnerUbicEspec = (Spinner) mView.findViewById(R.id.spinner_ubicespc);
        spinnerTipoObs = (Spinner) mView.findViewById(R.id.spinner_tipobs);

        ArrayAdapter adapterTipoObs = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Tipo_obs);
        adapterTipoObs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTipoObs.setAdapter(adapterTipoObs);

        ArrayAdapter adapterArea = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Area_obs);
        adapterArea.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerArea.setAdapter(adapterArea);

        ArrayAdapter adapterNivel = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.NivelRiesgo_obs);
        adapterNivel.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerNivel.setAdapter(adapterNivel);

        //Ubicaciones
        ArrayAdapter adapterUbic = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.Ubicacion_obs);
        adapterUbic.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerUbica.setAdapter(adapterUbic);

        adapterSubN = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.SubUbicacion_obs);
        adapterSubN.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSububic.setAdapter(adapterSubN);

        adapterUbicEspc = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,GlobalVariables.UbicacionEspecifica_obs);
        adapterUbicEspc.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerUbicEspec.setAdapter(adapterUbicEspc);

        spinnerUbica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro ubica = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_ubic) ).getSelectedItem();
                Ubicacionfinal=ubica.CodTipo;
                GlobalVariables.SubUbicacion_obs.clear();
                for (Maestro item: GlobalVariables.loadUbicacion(Ubicacionfinal,2)
                     ) {
                    GlobalVariables.SubUbicacion_obs.add(item);
                }
                adapterSubN.notifyDataSetChanged();
                spinnerSububic.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Ubicacionfinal = "";
            }
        });

        spinnerSububic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Maestro Sububica = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_sububic) ).getSelectedItem();
                Ubicacionfinal=Sububica.CodTipo;
                GlobalVariables.UbicacionEspecifica_obs.clear();
                for (Maestro item: GlobalVariables.loadUbicacion(Ubicacionfinal,3)) {
                    GlobalVariables.UbicacionEspecifica_obs.add(item);
                }
                adapterUbicEspc.notifyDataSetChanged();
                spinnerUbicEspec.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Ubicacionfinal = Ubicacionfinal.split("\\.")[0];
            }
        });

        spinnerUbicEspec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro UbicaEspec = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_ubicespc) ).getSelectedItem();
                Ubicacionfinal=UbicaEspec.CodTipo;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                String Ubic[] =  Ubicacionfinal.split("\\.");
                Ubicacionfinal = Ubic[0]+"."+ Ubic[1];
            }
        });


        spinnerTipoObs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) mView.findViewById(R.id.spinner_tipobs) ).getSelectedItem();
                TabHost mTabHost = (TabHost) getActivity().findViewById(android.R.id.tabhost);
                TabWidget tabWidget =(TabWidget)getActivity().findViewById(android.R.id.tabs);
                GlobalVariables.TipoObservacion=Tipo.CodTipo;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                String Ubic[] =  Ubicacionfinal.split("\\.");
                Ubicacionfinal = Ubic[0]+"."+ Ubic[1];
            }
        });



        TextView txtSampleText = (TextView) mView.findViewById(R.id.id_CodObservacion);
        txtSampleText.setText(sampleText);
        botonEscogerFecha=(Button) mView.findViewById(R.id.btn_fecha);
        myCalendar = Calendar.getInstance();
        Date actual = myCalendar.getTime();
        SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
        botonEscogerFecha.setText(dt.format(actual));

        botonEscogerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newFragment = new SelectDateFragment();
                newFragment.show(getActivity().getFragmentManager(), "DatePicker");
            }
        });

        return mView;
    }
}


