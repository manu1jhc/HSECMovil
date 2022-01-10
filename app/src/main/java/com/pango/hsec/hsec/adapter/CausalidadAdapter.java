package com.pango.hsec.hsec.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.CausalidadModel;
import com.pango.hsec.hsec.model.SeguridadCAModel;

import java.util.ArrayList;

public class CausalidadAdapter extends RecyclerView.Adapter<CausalidadAdapter.TablaViewHolder> {
    private ArrayList<CausalidadModel> seguridadCA;

    public CausalidadAdapter(ArrayList<CausalidadModel> seguridadCA) {
        this.seguridadCA=seguridadCA;
    }
    @Override
    public void onBindViewHolder(CausalidadAdapter.TablaViewHolder holder, int position) {
        CausalidadModel em = seguridadCA.get(position);

        holder.tx_tipo_causa.setText(em.TipoCausa);
        holder.tx_condicion.setText(em.Condicion);
        holder.tx_causa.setText(em.Causa);
        holder.tx_comentar.setText(em.Comentario);

    }

    @Override
    public CausalidadAdapter.TablaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.causalidad_adapter, parent, false);
        CausalidadAdapter.TablaViewHolder nvh= new CausalidadAdapter.TablaViewHolder(itemView);
        return nvh;
    }
    public static class TablaViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables
        protected TextView tx_tipo_causa;
        protected TextView tx_condicion;
        protected TextView tx_causa;
        protected TextView tx_comentar;
        //uniendo con los layouts
        public TablaViewHolder(View v) {
            super(v);
            tx_tipo_causa = (TextView)  v.findViewById(R.id.tx_tipo_causa);
            tx_condicion = (TextView)  v.findViewById(R.id.tx_condicion);
            tx_causa = (TextView)  v.findViewById(R.id.tx_causa);
            tx_comentar = (TextView)  v.findViewById(R.id.tx_comentar);
        }
    }

    @Override
    public int getItemCount() {

        return seguridadCA.size();
    }
}
