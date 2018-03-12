package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.PlanModel;

import java.util.ArrayList;
import java.util.List;

public class PlanAdapter extends ArrayAdapter<PlanModel> {
    private ArrayList<PlanModel> data = new ArrayList<PlanModel>();
    private Context context;

    public PlanAdapter(Context context, ArrayList<PlanModel> data) {
        super(context, R.layout.public_plan,data);
        this.data = data;
        this.context = context;
    }
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView=inflater.inflate(R.layout.public_plan, null,true);

        TextView tarea=(TextView)  rowView.findViewById(R.id.tx_tarea);
        TextView responsable=(TextView)  rowView.findViewById(R.id.tx_responsable);
        TextView area=(TextView)  rowView.findViewById(R.id.tx_area);
        TextView estado=(TextView)  rowView.findViewById(R.id.tx_estado);

        final String tempTarea=data.get(position).DesPlanAccion;
        final String tempResponsable=data.get(position).Responsables;
        //GlobalVariables.getDescripcion(GlobalVariables.Area_obs,data.get(position).CodAreaHSEC);

        final String tempArea=GlobalVariables.getDescripcion(GlobalVariables.Area_obs,data.get(position).CodAreaHSEC);

        final String tempEstado=GlobalVariables.getDescripcion(GlobalVariables.Estado_Plan,data.get(position).CodEstadoAccion);

        tarea.setText(tempTarea);
        responsable.setText(tempResponsable);
        area.setText(tempArea);
        estado.setText(tempEstado);




        //return GlobalVariables.getDescripcion(GlobalVariables.Ubicaciones_obs,parts2[0]+"."+parts2[1]);

        return rowView;
    }

}






