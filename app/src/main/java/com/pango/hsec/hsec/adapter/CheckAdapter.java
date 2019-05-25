package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.List;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder> {
    private Activity activity;
    public static  List<SubDetalleModel> items;
    private Context ctx;
    CheckBox check2;
    CardView id_cv_Otros;

    public CheckAdapter(Activity activity, List<SubDetalleModel> items, CheckBox check2, CardView id_cv_Otros) {
        this.activity = activity;
        this.items = items;
        this.check2 = check2;
        this.id_cv_Otros= id_cv_Otros;
    }


    public void add(SubDetalleModel newdata) {
        items.add(newdata);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_check, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        SubDetalleModel em = items.get(position);
            viewHolder.desciption.setText(em.Descripcion);
            //id_cv_Otros.setVisibility(View.GONE);
            viewHolder.check_items.setChecked(em.Check);
            viewHolder.check_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.get(position).Check=!items.get(position).Check;
                    if ((em.CodTipo.equals("OBCC") || em.CodTipo.equals("HHA"))&&(items.get(position).CodSubtipo.equals("COMCON11") || items.get(position).CodSubtipo.equals("19"))){
                        id_cv_Otros.setVisibility(items.get(position).Check?View.VISIBLE:View.GONE);
                    }
                    int count = 0;
                    for (int i=0; i<items.size(); i++ ){
                        if(items.get(i).Check){
                            count = count +1;
                        }
                    }
                    if(count==items.size()){
                        check2.setChecked(true);
                    }else {
                        check2.setChecked(false);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return items.size();
        //return 0;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener
        private CheckBox check_items;
        private TextView desciption;

        public ViewHolder(View view) {
            super(view);
            check_items= (CheckBox) view.findViewById(R.id.checkdata);
            desciption = (TextView) view.findViewById(R.id.chect_text);
        }

    }





}






