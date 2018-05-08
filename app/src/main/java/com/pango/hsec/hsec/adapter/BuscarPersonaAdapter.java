package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.PersonaModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by Andre on 17/01/2018.
 */

public class BuscarPersonaAdapter extends ArrayAdapter<PersonaModel> {
    private Context context;
    private ArrayList<PersonaModel> data = new ArrayList<PersonaModel>();
    public Button botonAgregar;
    public boolean btntrue;

    public BuscarPersonaAdapter(@NonNull Context context, ArrayList<PersonaModel> data,Button botonAgregar) {
        super(context, R.layout.public_buscarp, data);
        this.data = data;
        this.context = context;
        this.botonAgregar=botonAgregar;
        btntrue=false;
    }

    public void add(PersonaModel newItem){
        data.add(newItem);
    }
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.public_buscarp, null, true);

        TextView nombre_p=rowView.findViewById(R.id.tx_user);
        Button btn_Agregar=rowView.findViewById(R.id.btn_Agregar);
        TextView dni_p=rowView.findViewById(R.id.tx_userdni);
        TextView cargo_p=rowView.findViewById(R.id.tx_usercargo);

        final String tempnombre_p=data.get(position).Nombres;
        final String tempdni_p = data.get(position).NroDocumento;
        final String tempcargo_p = data.get(position).Cargo;


        if(StringUtils.isEmpty(tempnombre_p)) nombre_p.setVisibility(View.GONE);
        else nombre_p.setText(tempnombre_p);
        if(StringUtils.isEmpty(tempdni_p)) dni_p.setVisibility(View.GONE);
        else dni_p.setText(tempdni_p);
        if(StringUtils.isEmpty(tempcargo_p)) cargo_p.setVisibility(View.GONE);
        else cargo_p.setText(tempcargo_p);

        final View finalConvertView = rowView;

        if(botonAgregar!= null){
            CheckBox personaCheckSeleccionar = (CheckBox) rowView.findViewById(R.id.checkBoxListaPersonas);
            personaCheckSeleccionar.setVisibility(View.VISIBLE);
            personaCheckSeleccionar.setChecked(data.get(position).Check);

            String BackgrColor= "#FFFFFF";
            if(data.get(position).Check) BackgrColor= "#D6EAF8";
            rowView.setBackgroundColor(Color.parseColor(BackgrColor));

            personaCheckSeleccionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.get(position).Check=!data.get(position).Check;
                    String BackgrColor= "#FFFFFF";
                    if(data.get(position).Check)  BackgrColor= "#D6EAF8";
                    finalConvertView.setBackgroundColor(Color.parseColor(BackgrColor));

                    int flag=View.GONE;
                    btntrue=false;
                    for(PersonaModel item:data)
                    {
                        if(item.Check){
                            flag=View.VISIBLE;
                            btntrue=true;
                        }
                    }
                    botonAgregar.setVisibility(flag);
                }
            });
        }

        return rowView;
    }








}
