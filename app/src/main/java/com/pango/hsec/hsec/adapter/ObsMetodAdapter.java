package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.ArrayList;
import java.util.List;

import static com.pango.hsec.hsec.obs_detalle1.ListCompCond;
import static com.pango.hsec.hsec.obs_detalle1.ListMetodologia;

public class ObsMetodAdapter extends RecyclerView.Adapter<ObsMetodAdapter.ViewHolder> {

    private Activity activity;
    public List<SubDetalleModel> items;
    public String Tipo;

    public ObsMetodAdapter(Activity activity, List<SubDetalleModel> items,String Tipo) {
        this.activity = activity;
        this.items = items;
        this.Tipo = Tipo;
        //reorderList();
    }

    public void remove(SubDetalleModel item){
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,items.size());
    }
    public void add(SubDetalleModel newdata) {
        items.add(newdata);
    }

    public void reorderList(){
        List<SubDetalleModel> itemsfilt= new ArrayList<>();
        List<SubDetalleModel> itemsOther= new ArrayList<>();
        for(SubDetalleModel item: items)
            if(item.CodTipo.equals(Tipo)){
                itemsfilt.add(item);
            }
            else  itemsOther.add(item);
        items.clear();
        items.addAll(itemsfilt);
        items.addAll(itemsOther);
    }
    @Override
    public ObsMetodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_obs_ischeck , parent, false);

        return new ObsMetodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SubDetalleModel em = items.get(position);

        if(em.CodTipo.equals(Tipo)){
            String Descripcion="";
            switch (em.CodTipo){
                case "OBSR":Descripcion=GlobalVariables.getDescripcion(GlobalVariables.GestionRiesg_obs, em.CodSubtipo); break;
                case "OBSC":Descripcion=GlobalVariables.getDescripcion(GlobalVariables.Clasificacion_Obs, em.CodSubtipo); break;
                case "HHA":Descripcion=GlobalVariables.getDescripcion(GlobalVariables.HHA_obs, em.CodSubtipo); break;
                case "OBCC":Descripcion=GlobalVariables.getDescripcion(GlobalVariables.CondicionComp_Obs, em.CodSubtipo); break;
            }
            if(em.Descripcion !=null)  viewHolder.det_gestion.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(viewHolder.det_gestion.getContext(), R.color.colorNegro)+">"+Descripcion+": </font>"+em.Descripcion));
            else                      viewHolder.det_gestion.setText(Descripcion);
            viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         //items.get(position).Estado = "E";
                                                         items.remove(position);
                                                         notifyItemRemoved(position);
                                                         notifyItemRangeChanged(position,items.size());
                                                     }
                                                 }
         );

        }
     //   else viewHolder.itemView.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        int size=0;
        for(SubDetalleModel item:items){
            if(item.CodTipo.equals(Tipo))size++;
        }
        return size;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener
        private TextView det_gestion;
        private ImageButton btn_Delete;

        public ViewHolder(View view) {
            super(view);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
            det_gestion = (TextView) view.findViewById(R.id.txt_gestion);
        }

    }






}
