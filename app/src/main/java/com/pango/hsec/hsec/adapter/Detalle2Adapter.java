package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.IncidentesMAModel;

import java.util.ArrayList;

public class Detalle2Adapter extends BaseAdapter {
    private Context context;
    IncidentesMAModel maCuasiAccidenteModel;
    ArrayList<String> obsDetcab;
    ArrayList<String> obsDetIzq;
    public Detalle2Adapter(Context context, IncidentesMAModel maCuasiAccidenteModel,ArrayList<String> obsDetcab,ArrayList<String> obsDetIzq) {
        this.context = context;
        this.maCuasiAccidenteModel=maCuasiAccidenteModel;
        this.obsDetcab=obsDetcab;
        this.obsDetIzq=obsDetIzq;

    }
    @Override
    public int getCount() {
        return obsDetcab.size();
    }

    @Override
    public IncidentesMAModel getItem(int position) {
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
        ladoDerecho.setText(Utils.getMACuasiDataDetalle(maCuasiAccidenteModel,obsDetcab.get(position)));
        return convertView;
    }
}
