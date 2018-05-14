package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.Maestro;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<Maestro> {
    int groupid;
    Activity context;
    ArrayList<Maestro> list;
    LayoutInflater inflater;
    public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<Maestro>
            list){
        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);
        imageView.setImageResource(Integer.parseInt(list.get(position).Codigo));
        TextView textView=(TextView)itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).Descripcion);
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent){
        return getView(position,convertView,parent);

    }
}