package com.pango.hsec.hsec.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

        String Descripcion="";

        switch (em.CodTipo){
            case "OBSR":Descripcion=GlobalVariables.getDescripcion(GlobalVariables.GestionRiesg_obs, em.CodSubtipo); break;
            case "OBSC":Descripcion=GlobalVariables.getDescripcion(GlobalVariables.Clasificacion_Obs, em.CodSubtipo); break;
            case "HHA":Descripcion=GlobalVariables.getDescripcion(GlobalVariables.HHA_obs, em.CodSubtipo); break;
            case "OBCC":Descripcion=GlobalVariables.getDescripcion(GlobalVariables.CondicionComp_Obs, em.CodSubtipo); break;

            case "OBSP":
                Descripcion=GlobalVariables.getDescripcion(GlobalVariables.Pre_Interaccion, em.CodSubtipo);
                break;
            case "OBVE":
                Descripcion=GlobalVariables.getDescripcion(GlobalVariables.Cierre_Interaccion, em.CodSubtipo);
                if(em.Descripcion.equals("R001")){
                    tablaViewHolder.opciones.setImageResource(R.drawable.ic_correcto);
                }else if(em.Descripcion.equals("R002")){
                    tablaViewHolder.opciones.setImageResource(R.drawable.ic_noaplica);
                }

                break;

        }


        if(em.Descripcion !=null && !em.CodTipo.equals("OBSP") && !em.CodTipo.equals("OBVE") ) tablaViewHolder.descripcion.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(tablaViewHolder.descripcion.getContext(), R.color.colorNegro)+">"+Descripcion+": </font>"+em.Descripcion));
        else                      tablaViewHolder.descripcion.setText(Descripcion);


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
