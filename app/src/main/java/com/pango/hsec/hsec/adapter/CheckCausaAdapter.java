package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.Maestro;

import java.util.List;

public class CheckCausaAdapter extends RecyclerView.Adapter<CheckCausaAdapter.ViewHolder>{
    private Activity activity;
    public static List<Maestro> items;
    private Context ctx;

    public CheckCausaAdapter(Activity activity, List<Maestro> items) {
        this.activity = activity;
        this.items = items;
    }

    public void add(Maestro newdata) {
        items.add(newdata);
    }
    public void addAll(List<Maestro> newData){
        items.clear();
        items = newData;
    }

    public void remove(){
        items.clear();
    }


    @Override
    public CheckCausaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_check, parent, false);

        return new CheckCausaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheckCausaAdapter.ViewHolder viewHolder, final int position) {

        Maestro em = items.get(position);
        viewHolder.desciption.setText(em.Descripcion);
        //viewHolder.check_items.setOnCheckedChangeListener(null);
        //id_cv_Otros.setVisibility(View.GONE);
        viewHolder.check_items.setChecked(em.Checked);

    }

    @Override
    public int getItemCount() {
        return items.size();
        //return 8;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener
        private CheckBox check_items;
        private TextView desciption;

        public ViewHolder(View view) {
            super(view);
            check_items = (CheckBox) view.findViewById(R.id.checkdata);
            desciption = (TextView) view.findViewById(R.id.chect_text);
        }
    }
}
