package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.TextView;
import com.pango.hsec.hsec.R;
import java.util.List;
import com.pango.hsec.hsec.model.ObsComentModel;

import java.util.List;

public class ObsComentAdapter extends RecyclerView.Adapter<ObsComentAdapter.ViewHolder> {
    private Activity activity;
    private List<ObsComentModel> items;

    public ObsComentAdapter(Activity activity, List<ObsComentModel> items) {
        this.activity = activity;
        this.items = items;
    }
    public void add(ObsComentModel newdata){
        items.add(newdata);
/*      boolean pass=true;
        for (ObsComentModel temp: items) {
            if(temp.CodComentario.equals(newdata.CodComentario))
                pass=false;
        }
        if(pass)
        {
            items.add(newdata);
            //notifyDataSetChanged();
        }
        else Toast.makeText(activity, "comentario ya existe en la lista" , Toast.LENGTH_SHORT).show();   */
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_obscoment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.tipoComent.setText(items.get(position).Tipo);
        // viewHolder.DNI.setText(items.get(position).NroDocumento);
        viewHolder.descripcion.setText(items.get(position).Descripcion);
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
        //return 0;
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener
        private TextView tipoComent, descripcion;
        private ImageButton btn_Delete;

        public ViewHolder(View view) {
            super(view);
            tipoComent = (TextView)view.findViewById(R.id.txt_tipocom);
            descripcion = (TextView)view.findViewById(R.id.txt_descripcion);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
        }

    }











}
