package com.pango.hsec.hsec.adapter;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.SubDetalleModel;
import com.pango.hsec.hsec.observacion_edit;

import java.util.List;

public class ListYesNotAdapter extends RecyclerView.Adapter<ListYesNotAdapter.ViewHolder> {

    private Activity activity;
    private observacion_edit ActivityR;
    public List<SubDetalleModel> items;
    public String Tipo;
    private final static int VISIBLE_ITEM_TYPE = 1;
    private final static int INVISIBLE_ITEM_TYPE = 2;
    public ListYesNotAdapter(Activity activity, List<SubDetalleModel> items,String Tipo, observacion_edit ActivityR) {
        this.activity = activity;
        this.items = items;
        this.Tipo=Tipo;
        this.ActivityR=ActivityR;
    }

    @Override
    public ListYesNotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view ;//= inflater.inflate(R.layout.item_obs_ischeck , parent, false);

        if (viewType == INVISIBLE_ITEM_TYPE) {
            // The type is invisible, so just create a zero-height Space widget to hold the position.
            view = new Space(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        } else {
            view = inflater.inflate(R.layout.item_yesnot , parent, false);
        }
        return new ListYesNotAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == VISIBLE_ITEM_TYPE) {
            String Descripcion= GlobalVariables.getDescripcion(GlobalVariables.Cierre_Interaccion, items.get(position).CodSubtipo);
            viewHolder.txt_descrip.setText(Descripcion);
            viewHolder.sp_combobox.setAdapter(new IconAdapter(activity, GlobalVariables.Opc_YesNot));
            viewHolder.sp_combobox.setSelection(GlobalVariables.indexOf(GlobalVariables.Opc_YesNot, items.get(position).Descripcion));
            viewHolder.sp_combobox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int sp_position, long id) {
                    Maestro Tipo = (Maestro) ((Spinner) parentView.findViewById(R.id.sp_comboBox)).getSelectedItem();
                    items.get(position).Descripcion = Tipo.CodTipo;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public int getItemViewType(int position) {
        return items.get(position).CodTipo.equals(Tipo) ? VISIBLE_ITEM_TYPE:INVISIBLE_ITEM_TYPE ;
    }
    protected class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener
        private TextView txt_descrip;
        private Spinner sp_combobox;
        public ViewHolder(View view) {
            super(view);
            txt_descrip= (TextView) view.findViewById(R.id.txt_Descripcion);
            sp_combobox= (Spinner) view.findViewById(R.id.sp_comboBox);
        }
    }
}