package com.pango.hsec.hsec.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Ficha.AddRegistroAvance;
import com.pango.hsec.hsec.Ficha.PlanAccionDet;
import com.pango.hsec.hsec.Ficha.RegistroAtencion;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.AccionMejoraModel;
import com.pango.hsec.hsec.model.PerfilCapModel;

import java.util.ArrayList;

public class AccionMejoraAdapter extends RecyclerView.Adapter<AccionMejoraAdapter.AccionMejoraViewHolder>{

    static int position;

    private static ArrayList<AccionMejoraModel> accionMejoraModel;
    static String data="";
    PlanAccionDet planAccionDet;

    public AccionMejoraAdapter(PlanAccionDet planAccionDet, ArrayList<AccionMejoraModel> accionMejoraModel,String data) {
        this.accionMejoraModel = accionMejoraModel;
        this.data=data;
        this.planAccionDet=planAccionDet;
    }


    @Override
    public void onBindViewHolder(AccionMejoraAdapter.AccionMejoraViewHolder accionMejoraViewHolder, int i) {
        AccionMejoraModel cm = accionMejoraModel.get(i);
        position=i;
        accionMejoraViewHolder.tx_descripcion.setText(cm.Descripcion);
        //capacitacionViewHolder.duracion_nota.setText(cm.Duracion);
        accionMejoraViewHolder.tx_porcentaje.setText(cm.PorcentajeAvance);


        if(Boolean.parseBoolean(cm.Editable)){
            //accionMejoraViewHolder.btn_editar_m.setEnabled(Boolean.parseBoolean(cm.Editable));
            accionMejoraViewHolder.btn_editar_m.setVisibility(View.VISIBLE);
        }else{
            //accionMejoraViewHolder.btn_editar_m.setEnabled(false);
            accionMejoraViewHolder.btn_editar_m.setVisibility(View.INVISIBLE);

        }



        accionMejoraViewHolder.btn_editar_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), AddRegistroAvance.class);
                v.getContext().startActivity(intent);
            }
        });




    }



    @Override
    public AccionMejoraAdapter.AccionMejoraViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_acc_mejora, viewGroup, false);
        AccionMejoraAdapter.AccionMejoraViewHolder nvh= new AccionMejoraAdapter.AccionMejoraViewHolder(itemView);






        return nvh;

    }

    @Override
    public int getItemCount() {

        return accionMejoraModel.size();

    }


    public static class AccionMejoraViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //definicion de las variables

        protected TextView tx_descripcion;
        //protected TextView duracion_nota;
        protected TextView tx_porcentaje;
        ImageButton btn_editar_m;

        //uniendo con los layouts
        public AccionMejoraViewHolder(View v) {
            super(v);
            tx_descripcion = (TextView)  v.findViewById(R.id.tx_descripcion);
            //duracion_nota = (TextView)  v.findViewById(R.id.duracion_nota);
            tx_porcentaje = (TextView)  v.findViewById(R.id.tx_porcentaje);
            itemView.setOnClickListener(this);
            btn_editar_m = (ImageButton)  v.findViewById(R.id.btn_editar_m);


        }


        @Override
        public void onClick(View v) {

            String json = "";
            Gson gson = new Gson();
            json = gson.toJson(accionMejoraModel.get(position));



            Intent intent=new Intent(v.getContext(), RegistroAtencion.class);
            intent.putExtra("json",json);
            //intent.putExtra("position",position);
            v.getContext().startActivity(intent);

            //Toast.makeText(PlanAccionDet.this,"The Item Clicked is: "+ position ,Toast.LENGTH_SHORT).show();

        }
    }
/*
    public void clicAddReg(View v){

        Intent intent=new Intent(v.getContext(), AddRegistroAvance.class);
        v.getContext().startActivity(intent);

    }


*/





}
