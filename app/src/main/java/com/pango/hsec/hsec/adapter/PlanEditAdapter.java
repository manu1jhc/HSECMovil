package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.PlanAccionEdit;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.PlanModel;

import java.util.List;

public class PlanEditAdapter extends RecyclerView.Adapter<PlanEditAdapter.ViewHolder> {
    private Activity activity;
    private Fragment fragment;
    private List<PlanModel> items;
    public PlanEditAdapter(Activity activity,Fragment fragment, List<PlanModel> items) {
        this.activity = activity;
        this.items = items;
        this.fragment = fragment;
    }
    public void add(PlanModel newdata){
        items.add(newdata);
        notifyDataSetChanged();
    }
    public void replace(PlanModel replacedata){
        items.set(Integer.parseInt(replacedata.Editable),replacedata);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_plan, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.Tarea.setText(items.get(position).DesPlanAccion);
        viewHolder.idposition=position;
        String tempadcional= GlobalVariables.getDescripcion(GlobalVariables.Estado_Plan, items.get(position).CodEstadoAccion)+"\n"+GlobalVariables.getDescripcion(GlobalVariables.NivelRiesgo_obs, items.get(position).CodNivelRiesgo);
        viewHolder.Acicional.setText(tempadcional);
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
    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{//
        private TextView Tarea, Acicional;
        private ImageButton btn_Delete;
        public int idposition;
        public ViewHolder(View view) {
            super(view);
            Tarea = (TextView)view.findViewById(R.id.txt_tarea);
            Acicional = (TextView)view.findViewById(R.id.txt_adicional);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(fragment.getActivity(), PlanAccionEdit.class);
            i.putExtra("editplan",true);
            Gson gson = new Gson();
            items.get(idposition).Editable=idposition+"";
            i.putExtra("Plan", gson.toJson(items.get(idposition)));
            fragment.startActivityForResult(i,2);
        }
    }
}
