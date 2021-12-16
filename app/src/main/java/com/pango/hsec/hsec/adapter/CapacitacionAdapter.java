package com.pango.hsec.hsec.adapter;


import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.CapRecibidaModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CapacitacionAdapter extends RecyclerView.Adapter<CapacitacionAdapter.CapacitacionViewHolder> {

    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("dd-MM-yyyy");
    private ArrayList<CapRecibidaModel> capRecibidaModel;

    public CapacitacionAdapter(ArrayList<CapRecibidaModel> capRecibidaModel) {
        this.capRecibidaModel = capRecibidaModel;
    }

    @Override
    public void onBindViewHolder(CapacitacionAdapter.CapacitacionViewHolder capacitacionViewHolder, int i) {
        CapRecibidaModel cm = capRecibidaModel.get(i);

        capacitacionViewHolder.tema.setText(cm.Tema);
        //capacitacionViewHolder.duracion_nota.setText(cm.Duracion);
        capacitacionViewHolder.estado.setText(cm.Estado+(cm.Nota!=null?"\n"+cm.Nota:""));

        try {
            capacitacionViewHolder.tx_fecha.setText(formatoRender.format(formatoInicial.parse(cm.Fecha)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        protected TextView tx_fecha;

        //uniendo con los layouts
        public CapacitacionViewHolder(View v) {
            super(v);
            tema = (TextView)  v.findViewById(R.id.tx_tema);
            //duracion_nota = (TextView)  v.findViewById(R.id.duracion_nota);
            estado = (TextView)  v.findViewById(R.id.tx_estado);
            tx_fecha=(TextView) v.findViewById(R.id.tx_fecha);
        }


    }








}
