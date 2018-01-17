package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.GetPlanModel;
import com.pango.hsec.hsec.model.PlanModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 11/01/2018.
 */

public class PlandetAdapter extends BaseAdapter {
    private Context context;
    //private ArrayList<PlanModel> data = new ArrayList<PlanModel>();
    PlanModel planModel;
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public PlandetAdapter(Context context, PlanModel planModel) {
        this.planModel = planModel;
        this.context = context;
    }

    @Override
    public int getCount() {
        return GlobalVariables.planDetCab.length;
    }

    @Override
    public Object getItem(int position) {
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
        ladoIzquierdo.setText(GlobalVariables.planDetIzq[position]);



        TextView ladoDerecho=convertView.findViewById(R.id.txdet);
        ladoDerecho.setText(Utils.getPlan(planModel,GlobalVariables.planDetCab[position]));


        return convertView;
    }


}
