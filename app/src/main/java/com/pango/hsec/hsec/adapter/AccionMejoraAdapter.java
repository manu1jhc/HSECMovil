package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Ficha.AddRegistroAvance;
import com.pango.hsec.hsec.Ficha.RegistroAtencion;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.AccionMejoraMinModel;
import com.pango.hsec.hsec.model.AccionMejoraModel;
import java.util.List;

public class AccionMejoraAdapter extends RecyclerView.Adapter<AccionMejoraAdapter.ViewHolder> {
    private Activity activity;
    private List<AccionMejoraMinModel> items;
    private String Responsables,CodResponsables;
    public AccionMejoraAdapter(Activity activity, List<AccionMejoraMinModel> items,String Responsables,String CodResponsables) {
        this.activity = activity;
        this.items = items;
        this.Responsables=Responsables;
        this.CodResponsables=CodResponsables;
    }
    public void add(AccionMejoraMinModel newdata){
        items.add(newdata);
        notifyDataSetChanged();
    }
    public void replace(AccionMejoraMinModel replacedata){
        int position=Integer.parseInt(replacedata.Editable);
        if(replacedata.Correlativo.equals("E")){
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,items.size());
        }
        else{
            replacedata.Editable="true";
            items.set(position,replacedata);
            notifyDataSetChanged();
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.public_acc_mejora, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.tx_descripcion.setText(items.get(position).Descripcion);
        viewHolder.tx_porcentaje.setText(items.get(position).PorcentajeAvance);
        viewHolder.btn_editar_m.setVisibility(Boolean.parseBoolean(items.get(position).Editable)?View.VISIBLE:View.GONE);

        viewHolder.idposition=position;
        viewHolder.btn_editar_m.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         Intent i = new Intent(activity, AddRegistroAvance.class);
                                                         Gson gson = new Gson();
                                                         items.get(position).Editable=position+"";
                                                         i.putExtra("AccionMejora",  gson.toJson(items.get(position)));
                                                         i.putExtra("Edit", true);
                                                         i.putExtra("CodResponsable",CodResponsables);
                                                         i.putExtra("Responsable",Responsables);
                                                         activity.startActivityForResult(i,2);
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
    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{//
        protected TextView tx_descripcion;
        protected TextView tx_porcentaje;
        ImageView btn_editar_m;
        public int idposition;
        public ViewHolder(View view) {
            super(view);
            tx_descripcion = (TextView)  view.findViewById(R.id.tx_descripcion);
            tx_porcentaje = (TextView)  view.findViewById(R.id.tx_porcentaje);
            btn_editar_m = (ImageView)  view.findViewById(R.id.btn_editar_m);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {


            Gson gson = new Gson();
            Intent intent=new Intent(activity, RegistroAtencion.class);
            intent.putExtra("Correlativo",items.get(idposition).Correlativo);
            activity.startActivity(intent);
        }
    }
}
