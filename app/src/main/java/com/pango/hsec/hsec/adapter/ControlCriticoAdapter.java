package com.pango.hsec.hsec.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.ControlCriticoModel;
import com.pango.hsec.hsec.model.Maestro;

import java.util.ArrayList;

public class ControlCriticoAdapter extends RecyclerView.Adapter<ControlCriticoAdapter.ControlCriticoViewHolder>{

    private ArrayList<ControlCriticoModel> controlCritico;
    public String ControlCriticodes="";
    public ControlCriticoAdapter(ArrayList<ControlCriticoModel> controlCritico) {
        this.controlCritico = controlCritico;
    }
    @Override
    public void onBindViewHolder(ControlCriticoAdapter.ControlCriticoViewHolder controlCriticoViewHolder, int i) {

        ControlCriticoModel cc = controlCritico.get(i);
        String temp_controlCritico = cc.Estado;
        String temp_criterio = cc.CodVcc;
        //String temp_CCefectivo =
        String temp_desviacion = cc.Justificacion;
        String temp_accion = cc.AccionCorrectiva;
if(!ControlCriticodes.equals(cc.CodigoCC)) controlCriticoViewHolder.cv_critico.setVisibility(View.VISIBLE);
        controlCriticoViewHolder.control_critico.setText(temp_controlCritico);
        controlCriticoViewHolder.criterio.setText("Criterio: " + temp_criterio);
        controlCriticoViewHolder.desviacion.setText(temp_desviacion);
        controlCriticoViewHolder.accion.setText(temp_accion);
        controlCriticoViewHolder.img_efectivo.setImageResource(getIcon(GlobalVariables.Opc_aspectoIcon, cc.Respuesta));

        if(cc.Respuesta.equals("R002")){ //no
            controlCriticoViewHolder.cv_desv.setVisibility(View.VISIBLE);
            controlCriticoViewHolder.cv_accion.setVisibility(View.VISIBLE);
        } else  {
            controlCriticoViewHolder.cv_desv.setVisibility(View.GONE);
            controlCriticoViewHolder.cv_accion.setVisibility(View.GONE);
        }

    }

    @Override
    public ControlCriticoAdapter.ControlCriticoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_cartilla, viewGroup, false);
        ControlCriticoAdapter.ControlCriticoViewHolder nvh= new ControlCriticoAdapter.ControlCriticoViewHolder(itemView);
        return nvh;
    }

    @Override
    public int getItemCount() {
        return controlCritico.size();
    }

    public static class ControlCriticoViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables
        protected TextView control_critico, criterio, desviacion, accion;
        protected ImageView img_efectivo;
        protected CardView cv_critico, cv_desv, cv_accion;

        //uniendo con los layouts
        public ControlCriticoViewHolder(View v) {
            super(v);
            control_critico = (TextView)  v.findViewById(R.id.det_cc);
            criterio = (TextView)  v.findViewById(R.id.det_cc2);
            desviacion = (TextView)  v.findViewById(R.id.det_cc4);
            accion = (TextView)  v.findViewById(R.id.det_cc5);
            img_efectivo = (ImageView)  v.findViewById(R.id.img_aspectos);

            cv_critico = (CardView)  v.findViewById(R.id.cv_cc1);
            cv_desv = (CardView)  v.findViewById(R.id.cv_cc4);
            cv_accion = (CardView)  v.findViewById(R.id.cv_cc5);

        }
    }


    public int getIcon(ArrayList<Maestro> Obj, String value){
        for (Maestro o : Obj  ) {
            if(o.CodTipo!=null&&o.CodTipo.equals(value)) return o.Icon;
        }
        return 0;
    }


}