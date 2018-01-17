package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.PersonaModel;

import java.util.ArrayList;

/**
 * Created by Andre on 17/01/2018.
 */

public class BuscarPersonaAdapter extends ArrayAdapter<PersonaModel> {
    private Context context;
    private ArrayList<PersonaModel> data = new ArrayList<PersonaModel>();

    public BuscarPersonaAdapter(@NonNull Context context, ArrayList<PersonaModel> data) {
        super(context, R.layout.public_buscarp, data);
        this.data = data;
        this.context = context;
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.public_buscarp, null, true);


        TextView nombre_p=rowView.findViewById(R.id.tx_user);
        TextView dni_p=rowView.findViewById(R.id.tx_userdni);
        TextView cargo_p=rowView.findViewById(R.id.tx_usercargo);

        final String tempnombre_p=data.get(position).Nombres;
        final String tempdni_p = data.get(position).NroDocumento;
        final String tempcargo_p = data.get(position).Cargo;


        nombre_p.setText(tempnombre_p);
        dni_p.setText(tempdni_p);
        cargo_p.setText(tempcargo_p);

        return rowView;
    }








}
