package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.EquipoModel;
import com.pango.hsec.hsec.model.GetPlanModel;
import com.pango.hsec.hsec.model.PlanModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 11/01/2018.
 */

public class PlandetAdapter extends RecyclerView.Adapter<PlandetAdapter.PlandetViewHolder> {


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
    public void onBindViewHolder(PlandetAdapter.PlandetViewHolder plandetViewHolder, int position) {
        //EquipoModel em = equipoModel.get(i);
        //personaViewHolder.Nombre.setText(em.Nombres);
        //personaViewHolder.area.setText(em.Cargo);

        plandetViewHolder.ladoIzquierdo.setText(GlobalVariables.planDetIzq[position]);
        plandetViewHolder.ladoDerecho.setText(Utils.getPlan(planModel,GlobalVariables.planDetCab[position]));


    }



    @Override
    public PlandetAdapter.PlandetViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.obslist, viewGroup, false);

        PlandetAdapter.PlandetViewHolder nvh= new PlandetAdapter.PlandetViewHolder(itemView);

        return nvh;

    }

    @Override
    public int getItemCount() {

        return GlobalVariables.planDetCab.length;

    }


    public static class PlandetViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables

        //protected TextView Nombre;
        //protected TextView area;
        TextView ladoIzquierdo;
        TextView ladoDerecho;

        //uniendo con los layouts
        public PlandetViewHolder(View v) {
            super(v);
            ladoIzquierdo = (TextView)  v.findViewById(R.id.txcab);
            ladoDerecho = (TextView)  v.findViewById(R.id.txdet);

        }


    }









    /*
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

    */


}
