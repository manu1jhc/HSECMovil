package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.ObservacionModel;

import java.util.ArrayList;

/**
 * Created by Andre on 29/01/2018.
 */

public class InspAdapter extends BaseAdapter {

    private Context context;
    InspeccionModel inspeccionModel;
    ArrayList<String> obsDetcab;
    ArrayList<String> obsDetIzq;
    public InspAdapter(Context context, InspeccionModel inspeccionModel,ArrayList<String> obsDetcab,ArrayList<String> obsDetIzq) {
        this.context = context;
        this.inspeccionModel=inspeccionModel;
        this.obsDetcab=obsDetcab;
        this.obsDetIzq=obsDetIzq;

    }

    @Override
    public int getCount() {
        return obsDetcab.size();
    }

    @Override
    public InspeccionModel getItem(int position) {
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
        ladoDerecho.setText(Utils.getInspeccionData(inspeccionModel,obsDetcab.get(position)));
        return convertView;
    }
}
