package com.pango.hsec.hsec.adapter;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.TextView;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.SubDetalleModel;
import com.pango.hsec.hsec.obs_detalle1;
import com.pango.hsec.hsec.obs_detalle2;
import com.pango.hsec.hsec.observacion_edit;

import java.util.List;

public class ObsMetodAdapter extends RecyclerView.Adapter<ObsMetodAdapter.ViewHolder> {

    private Activity activity;
    private observacion_edit ActivityR;
    public List<SubDetalleModel> items;
    public String Tipo;
    private final static int VISIBLE_ITEM_TYPE = 1;
    private final static int INVISIBLE_ITEM_TYPE = 2;

    public ObsMetodAdapter(Activity activity, List<SubDetalleModel> items, String Tipo, observacion_edit ActivityR) {
        this.activity = activity;
        this.items = items;
        this.Tipo = Tipo;
        this.ActivityR=ActivityR;
    }

    public void add(SubDetalleModel newdata) {
        items.add(newdata);
    }

    @Override
    public ObsMetodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view ;//= inflater.inflate(R.layout.item_obs_ischeck , parent, false);

        if (viewType == INVISIBLE_ITEM_TYPE) {
            // The type is invisible, so just create a zero-height Space widget to hold the position.
            view = new Space(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        } else {
            view = inflater.inflate(R.layout.item_obs_ischeck , parent, false);
        }
        return new ObsMetodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if (viewHolder.getItemViewType() == VISIBLE_ITEM_TYPE) {
            SubDetalleModel em = items.get(position);
                String Descripcion = "";
                switch (em.CodTipo) {
                    case "OBSR":Descripcion = GlobalVariables.getDescripcion(GlobalVariables.GestionRiesg_obs, em.CodSubtipo);break;
                    case "OBSC":Descripcion = GlobalVariables.getDescripcion(GlobalVariables.Clasificacion_Obs, em.CodSubtipo);break;
                    case "HHA":Descripcion = GlobalVariables.getDescripcion(GlobalVariables.HHA_obs, em.CodSubtipo);break;
                    case "OBCC":Descripcion = GlobalVariables.getDescripcion(GlobalVariables.CondicionComp_Obs, em.CodSubtipo);break;
                }
                if (em.Descripcion != null)
                    viewHolder.det_gestion.setText(Html.fromHtml("<font color=" + ContextCompat.getColor(viewHolder.det_gestion.getContext(), R.color.colorNegro) + ">" + Descripcion + ": </font>" + em.Descripcion));
                else viewHolder.det_gestion.setText(Descripcion);
                viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 items.remove(position);
                                                                 notifyItemRemoved(position);
                                                                 notifyItemRangeChanged(position,items.size());

                                                                 obs_detalle1 subtedatlle=(obs_detalle1) ActivityR.pageAdapter.getItem(1);
                                                                 subtedatlle.obsMetodAdapter.notifyDataSetChanged();
                                                                 subtedatlle.compCondAadpter.notifyDataSetChanged();

                                                                 obs_detalle2 subtedatlle2=(obs_detalle2) ActivityR.pageAdapter.getItem(2);
                                                                 subtedatlle2.listISAdapter.notifyDataSetChanged();
                                                                 subtedatlle2.obsClasifAdapter.notifyDataSetChanged();
                                                             }
                                                         }
                );

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
        private TextView det_gestion;
        private ImageButton btn_Delete;

        public ViewHolder(View view) {
            super(view);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
            det_gestion = (TextView) view.findViewById(R.id.txt_gestion);
        }
    }
}