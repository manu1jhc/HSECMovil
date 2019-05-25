package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
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
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.List;

public class ObsComentAdapter extends RecyclerView.Adapter<ObsComentAdapter.ViewHolder> {
    private Activity activity;
    private List<SubDetalleModel> items;

    public ObsComentAdapter(Activity activity, List<SubDetalleModel> items) {
        this.activity = activity;
        this.items = items;
    }
    public void add(SubDetalleModel newdata){
        items.add(0,newdata);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_obscoment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if(items.get(position).CodTipo.equals("PETO") || items.get(position).CodTipo.equals("COM")){
            viewHolder.tipoComent.setText(items.get(position).CodSubtipo);
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
    }

    @Override
    public int getItemCount() {
        int size=0;
        for(SubDetalleModel item:items){
            if(item.CodTipo.equals("PETO")||item.CodTipo.equals("COM"))size++;
        }
        return size;
     //  return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener
        private TextView tipoComent, descripcion;
        private ImageButton btn_Delete;
        private ConstraintLayout Layout;

        public ViewHolder(View view) {
            super(view);
            Layout = (ConstraintLayout)view.findViewById(R.id.constraintLayout2);
            tipoComent = (TextView)view.findViewById(R.id.txt_tipocom);
            descripcion = (TextView)view.findViewById(R.id.txt_descripcion);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
        }

    }

   /* public class DividerItemDecoration extends RecyclerView.ItemDecoration {


        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            for (int i = 0; i < parent.getChildCount(); i++) {

                if (parent.getChildAt(i).getVisibility() == View.GONE)
                    continue;
            }
        }
    }*/











}
