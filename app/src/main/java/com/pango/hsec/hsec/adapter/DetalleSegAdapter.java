package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.SeguridadCAModel;

import java.util.ArrayList;

public class DetalleSegAdapter extends BaseAdapter {
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
    public int getCount() {
        return obsDetcab.size();
    }

    @Override
    public SeguridadCAModel getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

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
    }
    }

