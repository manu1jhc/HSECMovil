package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.VerificacionModel;


public class VerAdapter extends BaseAdapter {
    private Context context;
    VerificacionModel verificacionModel;
    String [] verDetcab;
    String [] verDetIzq;
    public VerAdapter(Context context, VerificacionModel verificacionModel,String [] verDetcab,String []verDetIzq) {
        this.context = context;
        this.verificacionModel=verificacionModel;
        this.verDetcab=verDetcab;
        this.verDetIzq=verDetIzq;

    }


    @Override
    public int getCount() {
        return verDetcab.length;
    }

    @Override
    public VerificacionModel getItem(int position) {
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
        String a=verDetIzq[position];
        ladoIzquierdo.setText(verDetIzq[position]);
        TextView ladoDerecho=convertView.findViewById(R.id.txdet);
        String b= Utils.getListVer(verificacionModel,verDetcab[position]);

        ladoDerecho.setText(Utils.getListVer(verificacionModel,verDetcab[position]));


        return convertView;
    }
}
