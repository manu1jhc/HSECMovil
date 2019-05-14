package com.pango.hsec.hsec.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.EquipoModel;

import java.util.ArrayList;

/**
 * Created by Andre on 16/02/2018.
 */

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder>{

    private ArrayList<EquipoModel> equipoModel;

    public PersonaAdapter(ArrayList<EquipoModel> equipoModel) {
        this.equipoModel = equipoModel;
    }


    @Override
    public void onBindViewHolder(PersonaViewHolder personaViewHolder, int i) {
        EquipoModel em = equipoModel.get(i);
        personaViewHolder.Nombre.setText(em.Nombres);
        personaViewHolder.area.setText(em.Cargo);
        if(em.Estado!=null && em.Estado.equals("1"))
            personaViewHolder.itemView.setBackgroundColor(Color.parseColor("#BFE3DE"));
        //else personaViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }



    @Override
    public PersonaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_tabla, viewGroup, false);

        PersonaViewHolder nvh= new PersonaViewHolder(itemView);

        return nvh;

    }

    @Override
    public int getItemCount() {

        return equipoModel.size();

    }


    public static class PersonaViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables

        protected TextView Nombre;
        protected TextView area;

        //uniendo con los layouts
        public PersonaViewHolder(View v) {
            super(v);
            Nombre = (TextView)  v.findViewById(R.id.part_nombre);
            area = (TextView)  v.findViewById(R.id.part_area);
        }


    }






}
