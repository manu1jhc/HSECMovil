package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;

/**
 * Created by Andre on 06/02/2018.
 */

public class DocsAdapter extends BaseAdapter {


    private Context context;
    private GetGaleriaModel getGaleriaModel;
    //RelativeLayout rel_otros;

    public DocsAdapter(Context context,GetGaleriaModel getGaleriaModel) {
        this.context = context;
        this.getGaleriaModel=getGaleriaModel;
    }

    @Override
    public int getCount() {
        return getGaleriaModel.Count;
    }

    @Override
    public GaleriaModel getItem(int position) {
        return getGaleriaModel.Data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
TextView nom_file,tam_file;
    ImageButton icon_des;
    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.public_docs, viewGroup, false);
        }
        ImageButton icono = (ImageButton) view.findViewById(R.id.icon_des);
        icono.setTag(position);

        TextView dNomFile = (TextView)  view.findViewById(R.id.nom_file);
        TextView dTamanio = (TextView)  view.findViewById(R.id.tam_file);
        //rel_otros=(RelativeLayout) findViewById(R.id.rel_otros);



        final String tempNombre=getGaleriaModel.Data.get(position).Descripcion;//data.get(position).getNombre();
        final String tempTamanio=getGaleriaModel.Data.get(position).Urlmin;//Urlmin es el tam de la imagen

        if(getGaleriaModel.Data.get(position).TipoArchivo.equals("TP03")) {
            //rel_otros.setVisibility(View.VISIBLE);

            dNomFile.setText(tempNombre);
            dTamanio.setText(tempTamanio + " Kb");

        }
        final View finalView = view;
        icono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) viewGroup).performItemClick(finalView, position, 0);


                //Intent intent = new Intent();

            }
        });

        return view;
    }
}
