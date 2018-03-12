package com.pango.hsec.hsec.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.CapRecibidaModel;

import java.util.ArrayList;

/**
 * Created by Andre on 06/03/2018.
 */

public class AdicionalAdapter extends RecyclerView.Adapter<AdicionalAdapter.AdicionalViewHolder>{


    private String[] DatosAd;
    //private ArrayList<CapRecibidaModel> capRecibidaModel;

    public AdicionalAdapter(String[] DatosAd) {
        this.DatosAd = DatosAd;
    }


    @Override
    public void onBindViewHolder(AdicionalAdapter.AdicionalViewHolder adicionalViewHolder, int i) {
        //CapRecibidaModel cm = capRecibidaModel.get(i);

        String[] cabdet=DatosAd[i].split(":");


        adicionalViewHolder.cabecera.setText(cabdet[0]);
        //capacitacionViewHolder.duracion_nota.setText(cm.Duracion);
        adicionalViewHolder.detalle.setText(cabdet[1]);

    }



    @Override
    public AdicionalAdapter.AdicionalViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_etapas, viewGroup, false);

        AdicionalAdapter.AdicionalViewHolder nvh= new AdicionalAdapter.AdicionalViewHolder(itemView);

        return nvh;

    }

    @Override
    public int getItemCount() {

        return DatosAd.length;

    }


    public static class AdicionalViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables

        protected TextView cabecera;
        //protected TextView duracion_nota;
        protected TextView detalle;

        //uniendo con los layouts
        public AdicionalViewHolder(View v) {
            super(v);
            cabecera = (TextView)  v.findViewById(R.id.etapa);
            //duracion_nota = (TextView)  v.findViewById(R.id.duracion_nota);
            detalle = (TextView)  v.findViewById(R.id.desviacion);

        }
    }
}
