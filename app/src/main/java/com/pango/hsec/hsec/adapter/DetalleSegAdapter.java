package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.SeguridadCAModel;

import java.util.ArrayList;

public class DetalleSegAdapter extends RecyclerView.Adapter<DetalleSegAdapter.TablaViewHolder> {
    private Context context;
    SeguridadCAModel seguridadCAModel;
    ArrayList<String> obsDetcab;
    ArrayList<String> obsDetIzq;

    public DetalleSegAdapter(Context context, SeguridadCAModel seguridadCAModel,ArrayList<String> obsDetcab,ArrayList<String> obsDetIzq) {
        this.context = context;
        this.seguridadCAModel=seguridadCAModel;
        this.obsDetcab=obsDetcab;
        this.obsDetIzq=obsDetIzq;

    }
    @Override
    public void onBindViewHolder(DetalleSegAdapter.TablaViewHolder holder, int position) {
        String em;

        holder.ladoIzquierdo.setText(obsDetIzq.get(position));
        holder.ladoDerecho.setText(Utils.getSeguridadDetCA(seguridadCAModel,obsDetcab.get(position)));

    }

    @Override
    public DetalleSegAdapter.TablaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.obslist, parent, false);
        DetalleSegAdapter.TablaViewHolder nvh= new DetalleSegAdapter.TablaViewHolder(itemView);
        return nvh;
    }
    public static class TablaViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables
        protected  TextView ladoIzquierdo;
        protected  TextView ladoDerecho;
        //uniendo con los layouts
        public TablaViewHolder(View v) {
            super(v);
            ladoIzquierdo = (TextView)  v.findViewById(R.id.txcab);
            ladoDerecho = (TextView)  v.findViewById(R.id.txdet);
        }
    }
/*
    @Override
    public long getItemId(int position) {
        return 0;
    }*/

    @Override
    public int getItemCount() {
        return obsDetcab.size();
    }
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.obslist, parent, false);

        TextView ladoIzquierdo=convertView.findViewById(R.id.txcab);
        ladoIzquierdo.setText(obsDetIzq.get(position));
        TextView ladoDerecho=convertView.findViewById(R.id.txdet);
        ladoDerecho.setText(Utils.getSeguridadDetCA(seguridadCAModel,obsDetcab.get(position)));
        return convertView;
    }*/


    }

