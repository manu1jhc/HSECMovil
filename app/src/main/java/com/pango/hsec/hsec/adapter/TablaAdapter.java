package com.pango.hsec.hsec.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.ArrayList;

/**
 * Created by Andre on 20/02/2018.
 */

public class TablaAdapter extends RecyclerView.Adapter<TablaAdapter.TablaViewHolder> {


    private ArrayList<SubDetalleModel> dataTabla;

    public TablaAdapter(ArrayList<SubDetalleModel> dataTabla) {
        this.dataTabla = dataTabla;
    }


    @Override
    public void onBindViewHolder(TablaAdapter.TablaViewHolder tablaViewHolder, int i) {
        SubDetalleModel em = dataTabla.get(i);

        //tablaViewHolder.descripcion.setText(GlobalVariables.getDescripcion(GlobalVariables.Aspectos_Obs, em.CodSubtipo));

        //tablaViewHolder.descripcion.setText();
        tablaViewHolder.opciones.setImageResource(R.drawable.ic_correcto);

        if(em.CodTipo.equals("OBSR")){
            tablaViewHolder.descripcion.setText(GlobalVariables.getDescripcion(GlobalVariables.GestionRiesg_obs, em.Descripcion));
        }else if(em.CodTipo.equals("OBSC")){
            tablaViewHolder.descripcion.setText(GlobalVariables.getDescripcion(GlobalVariables.Clasificacion_Obs, em.Descripcion));
        }else if(em.CodTipo.equals("OBCC")){
            tablaViewHolder.descripcion.setText(GlobalVariables.getDescripcion(GlobalVariables.CondicionComp_Obs, em.Descripcion));
        }else if(em.CodTipo.equals("HHA")){
            tablaViewHolder.descripcion.setText(GlobalVariables.getDescripcion(GlobalVariables.HHA_obs, em.Descripcion));
        }





/*
        if(aspectoModel.get(i).Descripcion.isEmpty()){
            aspectoViewHolder.opciones.setImageResource(R.drawable.ic_noaplica);
        }else if (aspectoModel.get(i).Descripcion.equals("R001")){
            aspectoViewHolder.opciones.setImageResource(R.drawable.icon_check);
        }else if(aspectoModel.get(i).Descripcion.equals("R002")){
            aspectoViewHolder.opciones.setImageResource(R.drawable.icon_cross);
        }else if(aspectoModel.get(i).Descripcion.equals("R003")){
            aspectoViewHolder.opciones.setImageResource(R.drawable.icon_na);
        }

*/



    }



    @Override
    public TablaAdapter.TablaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_aspectos, viewGroup, false);
        TablaAdapter.TablaViewHolder nvh= new TablaAdapter.TablaViewHolder(itemView);
        return nvh;
    }

    @Override
    public int getItemCount() {
        return dataTabla.size();
    }

    public static class TablaViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables
        protected TextView descripcion;
        protected ImageView opciones;
        //uniendo con los layouts
        public TablaViewHolder(View v) {
            super(v);
            descripcion = (TextView)  v.findViewById(R.id.part_nombre);
            opciones = (ImageView)  v.findViewById(R.id.img_aspectos);
        }
    }







}
