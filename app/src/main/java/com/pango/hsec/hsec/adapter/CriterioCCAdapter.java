package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.ControlCriticoModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.SubDetalleModel;
import com.pango.hsec.hsec.obs_detalle1;
import com.pango.hsec.hsec.obs_detalle2;
import com.pango.hsec.hsec.observacion_edit;

import java.util.List;

public class CriterioCCAdapter extends RecyclerView.Adapter<CriterioCCAdapter.ViewHolder> {

    private Activity activity;
    private observacion_edit ActivityR;
    public List<ControlCriticoModel> items;
    public String ControlCritico="";
    public CriterioCCAdapter(Activity activity, List<ControlCriticoModel> items, observacion_edit ActivityR) {
        this.activity = activity;
        this.items = items;
        this.ActivityR=ActivityR;
    }

    @Override
    public CriterioCCAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_criterioeval, parent, false);
        CriterioCCAdapter.ViewHolder nvh= new CriterioCCAdapter.ViewHolder(itemView);
        return nvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if(!ControlCritico.equals(items.get(position).Estado)){
            viewHolder.txt_control.setVisibility(View.VISIBLE);
            viewHolder.txt_control.setText(items.get(position).Estado);
            ControlCritico=items.get(position).Estado;
        }
        else viewHolder.txt_control.setVisibility(View.GONE);
        viewHolder.txt_criterio.setText(items.get(position).CodVcc);
        viewHolder.sp_efectividad.setAdapter(new IconAdapter(activity,GlobalVariables.Opc_aspectoIcon));
        String Respuesta= items.get(position).Respuesta;
        viewHolder.sp_efectividad.setSelection(GlobalVariables.indexOf(GlobalVariables.Opc_aspectoIcon,Respuesta));
        if(Respuesta.equals("R002")){
            viewHolder.carDesviacion.setVisibility(View.VISIBLE);
            viewHolder.carAccion.setVisibility(View.VISIBLE);
            viewHolder.txt_desviacion.setText(items.get(position).Justificacion);
            viewHolder.txt_accion.setText(items.get(position).AccionCorrectiva);
        }
        else {
            viewHolder.carDesviacion.setVisibility(View.GONE);
            viewHolder.carAccion.setVisibility(View.GONE);
        }

        viewHolder.sp_efectividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,int sp_position, long id) {
                Maestro Tipo = (Maestro) ( (Spinner) parentView.findViewById(R.id.sp_efectividad) ).getSelectedItem();
                items.get(position).Respuesta=Tipo.CodTipo;
                if(Tipo.CodTipo.equals("R002")){
                    viewHolder.carDesviacion.setVisibility(View.VISIBLE);
                    viewHolder.carAccion.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.carDesviacion.setVisibility(View.GONE);
                    viewHolder.carAccion.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        viewHolder.txt_desviacion.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                items.get(position).Justificacion=viewHolder.txt_desviacion.getText().toString();
            }
        });
        viewHolder.txt_accion.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }

            @Override
            public void afterTextChanged(Editable s)
            {
                items.get(position).AccionCorrectiva=viewHolder.txt_accion.getText().toString();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener
        private TextView txt_control,txt_criterio;
        private Spinner sp_efectividad;
        private EditText txt_desviacion,txt_accion;
        private CardView carDesviacion,carAccion;

        public ViewHolder(View view) {
            super(view);
            txt_control= (TextView) view.findViewById(R.id.text_control);
            txt_criterio = (TextView) view.findViewById(R.id.text_criterio);
            sp_efectividad= (Spinner) view.findViewById(R.id.sp_efectividad);
            txt_desviacion= (EditText) view.findViewById(R.id.txt_desviacion);
            txt_accion = (EditText) view.findViewById(R.id.txt_accion);
            carDesviacion = (CardView) view.findViewById(R.id.id_cardesviacion);
            carAccion = (CardView) view.findViewById(R.id.id_caraccion);
        }
    }
}