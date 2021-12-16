package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.List;

import static com.pango.hsec.hsec.obs_detalle1.ListCompCond;

public class CompCondAadpter extends RecyclerView.Adapter<CompCondAadpter.ViewHolder> {

    private Activity activity;
    public static List<SubDetalleModel> items;

    public CompCondAadpter(Activity activity, List<SubDetalleModel> items) {
        this.activity = activity;
        this.items = items;
    }
    public void add(SubDetalleModel newdata) {
        items.add(newdata);
    }

    @Override
    public CompCondAadpter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_obs_ischeck , parent, false);
        return new CompCondAadpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SubDetalleModel em = items.get(position);

        String[] datapart = em.Descripcion.split(":");
        //if(em.CodTipo.equals("OBCC"))
        if (em.Descripcion.split(":")[0].contains("COMCON11")){
            viewHolder.det_gestion.setText(GlobalVariables.getDescripcion(GlobalVariables.CondicionComp_Obs, em.Descripcion.split(":")[0].trim()) +" : "+ em.Descripcion.split(":")[1]);

        }else {
            viewHolder.det_gestion.setText(GlobalVariables.getDescripcion(GlobalVariables.CondicionComp_Obs, em.Descripcion));
        }



            viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         for (int i=0; i<ListCompCond.size(); i++){
                                                             if(datapart[0].equals(ListCompCond.get(i).Descripcion)){
                                                                 ListCompCond.get(i).Check= false;
                                                                 break;
                                                             }
                                                         }
                                                         items.get(position).Check = false;
                                                         //ListCompCond.get(position).estado= false;
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
        //return 0;
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

///////////////////////////////////////
