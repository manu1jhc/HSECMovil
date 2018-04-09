package com.pango.hsec.hsec.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.EquipoModel;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.ArrayList;

import static com.pango.hsec.hsec.GlobalVariables.Aspectos_Obs;

/**
 * Created by Andre on 19/02/2018.
 */

public class AspectosAdapter extends RecyclerView.Adapter<AspectosAdapter.AspectoViewHolder>{

    private ArrayList<SubDetalleModel> aspectoModel;

    public AspectosAdapter(ArrayList<SubDetalleModel> aspectoModel) {
        this.aspectoModel = aspectoModel;
    }


    @Override
    public void onBindViewHolder(AspectosAdapter.AspectoViewHolder aspectoViewHolder, int i) {
        SubDetalleModel em = aspectoModel.get(i);

        aspectoViewHolder.descripcion.setText(GlobalVariables.getDescripcion(GlobalVariables.Aspectos_Obs, em.CodSubtipo));

        if(aspectoModel.get(i).Descripcion.isEmpty()){
            aspectoViewHolder.opciones.setImageResource(R.drawable.ic_noaplica);
        }else if (aspectoModel.get(i).Descripcion.equals("R001")){
            aspectoViewHolder.opciones.setImageResource(R.drawable.ic_correcto);
        }else if(aspectoModel.get(i).Descripcion.equals("R002")){
            aspectoViewHolder.opciones.setImageResource(R.drawable.ic_incorrecto);
        }else if(aspectoModel.get(i).Descripcion.equals("R003")){
            aspectoViewHolder.opciones.setImageResource(R.drawable.ic_noaplica);
        }
    }

    @Override
    public AspectosAdapter.AspectoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_aspectos, viewGroup, false);
        AspectosAdapter.AspectoViewHolder nvh= new AspectosAdapter.AspectoViewHolder(itemView);
        return nvh;
    }

    @Override
    public int getItemCount() {
        return aspectoModel.size();
    }

    public static class AspectoViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables
        protected TextView descripcion;
        protected ImageView opciones;
        //uniendo con los layouts
        public AspectoViewHolder(View v) {
            super(v);
            descripcion = (TextView)  v.findViewById(R.id.part_nombre);
            opciones = (ImageView)  v.findViewById(R.id.img_aspectos);
        }
    }









}
