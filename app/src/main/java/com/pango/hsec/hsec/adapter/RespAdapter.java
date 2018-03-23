package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.PlanModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Andre on 23/03/2018.
 */

public class RespAdapter extends RecyclerView.Adapter<RespAdapter.RespViewHolder>{

    String[]responsable;

    private Context context;
    //private ArrayList<PlanModel> data = new ArrayList<PlanModel>();
    //PlanModel planModel;
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public RespAdapter(Context context, String[]responsable) {
        this.responsable = responsable;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(RespAdapter.RespViewHolder respViewHolder, int position) {
        //EquipoModel em = equipoModel.get(i);
        //personaViewHolder.Nombre.setText(em.Nombres);
        //personaViewHolder.area.setText(em.Cargo);

        String []nombreCargo=responsable[position].split(":");

        RespViewHolder.part_nombre.setText(nombreCargo[0]);
        RespViewHolder.part_area.setText(nombreCargo[1]);


    }



    @Override
    public RespAdapter.RespViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_tabla, viewGroup, false);

        RespAdapter.RespViewHolder nvh= new RespAdapter.RespViewHolder(itemView);

        return nvh;

    }

    @Override
    public int getItemCount() {

        return responsable.length;

    }


    public static class RespViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables

        //protected TextView Nombre;
        //protected TextView area;
        protected static TextView part_nombre;
        protected static TextView part_area;

        //uniendo con los layouts
        public RespViewHolder(View v) {
            super(v);
            part_nombre = (TextView)  v.findViewById(R.id.part_nombre);
            part_area = (TextView)  v.findViewById(R.id.part_area);

        }


    }








}
