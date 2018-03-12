package com.pango.hsec.hsec.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.CapRecibidaModel;

import java.util.ArrayList;

public class CapacitacionAdapter extends RecyclerView.Adapter<CapacitacionAdapter.CapacitacionViewHolder> {


    private ArrayList<CapRecibidaModel> capRecibidaModel;

    public CapacitacionAdapter(ArrayList<CapRecibidaModel> capRecibidaModel) {
        this.capRecibidaModel = capRecibidaModel;
    }


    @Override
    public void onBindViewHolder(CapacitacionAdapter.CapacitacionViewHolder capacitacionViewHolder, int i) {
        CapRecibidaModel cm = capRecibidaModel.get(i);

        capacitacionViewHolder.tema.setText(cm.Tema);
        //capacitacionViewHolder.duracion_nota.setText(cm.Duracion);
        capacitacionViewHolder.estado.setText(cm.Estado+"\n"+cm.Nota);

    }



    @Override
    public CapacitacionAdapter.CapacitacionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_capacitacion, viewGroup, false);

        CapacitacionAdapter.CapacitacionViewHolder nvh= new CapacitacionAdapter.CapacitacionViewHolder(itemView);

        return nvh;

    }

    @Override
    public int getItemCount() {

        return capRecibidaModel.size();

    }


    public static class CapacitacionViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables

        protected TextView tema;
        //protected TextView duracion_nota;
        protected TextView estado;

        //uniendo con los layouts
        public CapacitacionViewHolder(View v) {
            super(v);
            tema = (TextView)  v.findViewById(R.id.tx_tema);
            //duracion_nota = (TextView)  v.findViewById(R.id.duracion_nota);
            estado = (TextView)  v.findViewById(R.id.tx_estado);

        }


    }








}
