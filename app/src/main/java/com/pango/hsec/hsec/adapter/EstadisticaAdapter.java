package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.EstadisticaModel;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.util.ArrayList;

/**
 * Created by Andre on 01/03/2018.
 */

public class EstadisticaAdapter extends ArrayAdapter<EstadisticaModel> {

    private Context context;
    private ArrayList<EstadisticaModel> data = new ArrayList<EstadisticaModel>();

    public EstadisticaAdapter(@NonNull Context context, ArrayList<EstadisticaModel> data) {
        super(context, R.layout.publicalist, data);
        this.data = data;
        this.context = context;
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.public_estadistica, null, true);


        TextView Titulo = rowView.findViewById(R.id.es_titulo);
        TextView N_estimado = rowView.findViewById(R.id.tx_planeado);
        TextView N_ejecutado = rowView.findViewById(R.id.tx_ejecutado);

        final String tempTitulo=data.get(position).Descripcion;
        final String tempN_estimado = data.get(position).Estimados;
        final String tempN_ejecutado = data.get(position).Ejecutados;

        Titulo.setText(tempTitulo.toUpperCase());
        N_estimado.setText("("+tempN_estimado+") "+"Estimados");
        N_ejecutado.setText("("+tempN_ejecutado+") "+"Ejecutados");

        return rowView;
    }


}
