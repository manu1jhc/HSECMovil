package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.TextView;
import com.pango.hsec.hsec.R;

import java.util.Collections;
import java.util.List;
import com.pango.hsec.hsec.model.ObsComentModel;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ObsComentAdapter extends RecyclerView.Adapter<ObsComentAdapter.ViewHolder> {
//    public interface ClickListener {
//        void onItemClick(int position, View v);
//    }

    private Activity activity;
    private List<SubDetalleModel> items;
    private final static int VISIBLE_ITEM_TYPE = 1;
    private final static int INVISIBLE_ITEM_TYPE = 2;
//    public static ClickListener clickListener;



//    public void setOnItemClickListener(ClickListener clickListener) {
//        ObsComentAdapter.clickListener = clickListener;
//    }

    public ObsComentAdapter(Activity activity, List<SubDetalleModel> items) {
        this.activity = activity;
        this.items = items;
    }
    public void add(SubDetalleModel newdata){
        items.add(newdata);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;//= inflater.inflate(R.layout.item_obs_ischeck , parent, false);
        if (viewType == INVISIBLE_ITEM_TYPE) {
            // The type is invisible, so just create a zero-height Space widget to hold the position.
            view = new Space(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        } else {
            LayoutInflater inflater = activity.getLayoutInflater();
             view = inflater.inflate(R.layout.item_obscoment, parent, false);
        }



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if (viewHolder.getItemViewType() == VISIBLE_ITEM_TYPE){
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
    public int getItemViewType(int position) {
        return (items.get(position).CodTipo.equals("PETO") || items.get(position).CodTipo.equals("COM")) ? VISIBLE_ITEM_TYPE:INVISIBLE_ITEM_TYPE ;
    }
    @Override
    public int getItemCount() {
       return items.size();
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
//            view.setOnClickListener(this);

            Layout = (ConstraintLayout)view.findViewById(R.id.constraintLayout2);
            tipoComent = (TextView)view.findViewById(R.id.txt_tipocom);
            descripcion = (TextView)view.findViewById(R.id.txt_descripcion);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
        }

//        @Override
//        public void onClick(View v) {
//            clickListener.onItemClick(getAdapterPosition(), v);
//        }


    }

    public SubDetalleModel getItem(int position) {
        return (items != null) ? items.get(position) : null;
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
