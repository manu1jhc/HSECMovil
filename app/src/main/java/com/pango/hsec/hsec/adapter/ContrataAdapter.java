package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.PersonaModel;

import java.util.ArrayList;

/**
 * Created by Andre on 08/02/2018.
 */

public class ContrataAdapter extends ArrayAdapter<Maestro> {
    private Context context;
    private ArrayList<Maestro> data = new ArrayList<Maestro>();

    public ContrataAdapter(Context context, ArrayList<Maestro> data) {
        super(context, R.layout.public_contrata, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.public_contrata, null, true);


        TextView codigo=rowView.findViewById(R.id.cod_contrata);
        TextView descripcion=rowView.findViewById(R.id.des_contrata);

        final String tempCodigo=data.get(position).CodTipo;
        final String tempDescripcion = data.get(position).Descripcion;


        codigo.setText(tempCodigo);
        descripcion.setText(tempDescripcion);

        return rowView;
    }





}
