package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.util.ArrayList;

/**
 * Created by Andre on 06/02/2018.
 */

public class DocsAdapter extends ArrayAdapter<GaleriaModel> {


    private Context context;
    int contador;
    private ArrayList<GaleriaModel> data = new ArrayList<GaleriaModel>();

    //RelativeLayout rel_otros;

    public DocsAdapter(Context context,ArrayList<GaleriaModel> data ) {
        super(context, R.layout.public_docs, data);

        this.context = context;
        this.data=data;

    }


    @Override
    public long getItemId(int position) {
        return 0;
    }
    TextView nom_file,tam_file;
    ImageButton icon_des;
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.public_docs, null, true);


        ImageButton icono = (ImageButton) rowView.findViewById(R.id.icon_des);
        icono.setTag(position);

        TextView dNomFile = (TextView)  rowView.findViewById(R.id.nom_file);
        TextView dTamanio = (TextView)  rowView.findViewById(R.id.tam_file);
        //rel_otros=(RelativeLayout) findViewById(R.id.rel_otros);



        final String tempNombre=data.get(position).Descripcion;//data.get(position).getNombre();
        final String tempTamanio=data.get(position).Tamanio;//Urlmin es el tam de la imagen

        //if(data.get(position).TipoArchivo.equals("TP03")) {
            //rel_otros.setVisibility(View.VISIBLE);

            dNomFile.setText(tempNombre);
            dTamanio.setText(tempTamanio + " Mb");

        //}
       // final View finalView = rowView;
        icono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(convertView, position, 0);

            }
        });

        return rowView;
    }
}
