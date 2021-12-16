package com.pango.hsec.hsec.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.ArrayList;

/**
 * Created by Andre on 19/02/2018.
 */

public class EtapasAdapter extends RecyclerView.Adapter<EtapasAdapter.EtapaViewHolder>{

    private ArrayList<SubDetalleModel> etapaModel;
    public EtapasAdapter(ArrayList<SubDetalleModel> etapaModel) {
        this.etapaModel = etapaModel;
    }


    @Override
    public void onBindViewHolder(EtapasAdapter.EtapaViewHolder etapaViewHolder, int i) {
        SubDetalleModel em = etapaModel.get(i);

        etapaViewHolder.etapa.setText(em.CodSubtipo);
        etapaViewHolder.desviacion.setText(em.Descripcion);



    }

    @Override
    public EtapasAdapter.EtapaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_etapas, viewGroup, false);
        EtapasAdapter.EtapaViewHolder nvh= new EtapasAdapter.EtapaViewHolder(itemView);
        return nvh;
    }

    @Override
    public int getItemCount() {
        return etapaModel.size();
    }

    public static class EtapaViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables
        protected TextView etapa    ;
        protected TextView desviacion;
        //uniendo con los layouts
        public EtapaViewHolder(View v) {
            super(v);
            etapa = (TextView)  v.findViewById(R.id.etapa);
            desviacion = (TextView)  v.findViewById(R.id.desviacion);
        }
    }










}
