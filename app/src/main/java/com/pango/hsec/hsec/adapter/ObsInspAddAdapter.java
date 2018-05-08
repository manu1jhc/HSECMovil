package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Ingresos.Inspecciones.ActObsInspEdit;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.ObsInspAddModel;
import com.pango.hsec.hsec.model.ObsInspDetModel;
import com.pango.hsec.hsec.model.PersonaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 18/04/2018.
 */

public class ObsInspAddAdapter extends RecyclerView.Adapter<ObsInspAddAdapter.ViewHolder>{

private Activity activity;
private Fragment fragment;
private ArrayList<ObsInspAddModel> items;

public ObsInspAddAdapter(Activity activity, Fragment fragment, ArrayList<ObsInspAddModel> items) {
        this.activity = activity;
        this.fragment = fragment;
        this.items = items;
        }
public void add(ObsInspAddModel newdata){
        items.add(newdata);
        notifyDataSetChanged();
        }

    public void replace(ObsInspAddModel replacedata,int index){
        items.set(index,replacedata);
        notifyDataSetChanged();
    }
@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_obsinsp, parent, false);

        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.position_item=position;

        viewHolder.obsinsp.setText(items.get(position).obsInspDetModel.Observacion);
        // viewHolder.DNI.setText(items.get(position).NroDocumento);
        //viewHolder.Cargo.setText(items.get(position).Cargo);
        viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,items.size());
        }
        }
        );

        }

@Override
public int getItemCount() {
        return items.size();
        }

/**
 * View holder to display each RecylerView item
 */
protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {//implements View.OnClickListener
    private TextView obsinsp;
    private ImageButton btn_Delete;
    public int position_item;

    public ViewHolder(View view) {
        super(view);
        obsinsp = (TextView)view.findViewById(R.id.txt_obsinsp);
        btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
        itemView.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {

                Intent intent=new Intent(v.getContext(),ActObsInspEdit.class);
                intent.putExtra("correlativo",items.get(position_item).obsInspDetModel.Correlativo);
                intent.putExtra("Grupo",items.get(position_item).obsInspDetModel.NroDetInspeccion);
                intent.putExtra("Editar",true);
                intent.putExtra("index",position_item);
                fragment.startActivityForResult(intent,2);

        }
}

}
