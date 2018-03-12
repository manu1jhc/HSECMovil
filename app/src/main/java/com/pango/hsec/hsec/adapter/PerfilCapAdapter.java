package com.pango.hsec.hsec.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.CapRecibidaModel;
import com.pango.hsec.hsec.model.PerfilCapModel;

import java.util.ArrayList;

/**
 * Created by Andre on 27/02/2018.
 */

public class PerfilCapAdapter extends RecyclerView.Adapter<PerfilCapAdapter.PerfilCapViewHolder>{


    private ArrayList<PerfilCapModel> perfilCapModel;

    public PerfilCapAdapter(ArrayList<PerfilCapModel> perfilCapModel) {
        this.perfilCapModel = perfilCapModel;
    }


    @Override
    public void onBindViewHolder(PerfilCapAdapter.PerfilCapViewHolder perfilCapViewHolder, int i) {
        PerfilCapModel cm = perfilCapModel.get(i);

        perfilCapViewHolder.tema.setText(cm.Tema);
        //capacitacionViewHolder.duracion_nota.setText(cm.Duracion);
        perfilCapViewHolder.estado.setText(cm.Estado+"\n"+cm.Nota);

    }



    @Override
    public PerfilCapAdapter.PerfilCapViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_capacitacion, viewGroup, false);

        PerfilCapAdapter.PerfilCapViewHolder nvh= new PerfilCapAdapter.PerfilCapViewHolder(itemView);

        return nvh;

    }

    @Override
    public int getItemCount() {

        return perfilCapModel.size();

    }


    public static class PerfilCapViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables

        protected TextView tema;
        //protected TextView duracion_nota;
        protected TextView estado;

        //uniendo con los layouts
        public PerfilCapViewHolder(View v) {
            super(v);
            tema = (TextView)  v.findViewById(R.id.tx_tema);
            //duracion_nota = (TextView)  v.findViewById(R.id.duracion_nota);
            estado = (TextView)  v.findViewById(R.id.tx_estado);

        }


    }






}
