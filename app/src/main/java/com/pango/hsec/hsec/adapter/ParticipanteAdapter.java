package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.EquipoModel;

import java.util.ArrayList;

/**
 * Created by Andre on 29/01/2018.
 */

public class ParticipanteAdapter extends BaseAdapter {
    private Context context;
    ArrayList<EquipoModel> equipoModel=new ArrayList<EquipoModel>();
    int count;
    String tipo;

    public ParticipanteAdapter(Context context, ArrayList<EquipoModel> equipoModel,int count,String tipo) {
        this.context = context;
        this.equipoModel=equipoModel;
        this.count=count;
        this.tipo=tipo;

    }



    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.public_tabla, parent, false);

        TextView nombre=convertView.findViewById(R.id.part_nombre);
        TextView area=convertView.findViewById(R.id.part_area);
       /* TextView lider=convertView.findViewById(R.id.part_lider);*/
        /*LinearLayout linear_tabla=convertView.findViewById(R.id.linear_tabla);
        CardView card_nombre=convertView.findViewById(R.id.card_nombre);
        CardView card_area=convertView.findViewById(R.id.card_area);*/

        if(tipo.equals("1"))
        {
            if(equipoModel.get(position).Lider.equals("1")){
                convertView.setBackgroundColor(Color.parseColor("#bfe3de"));
               //lider.setVisibility(View.VISIBLE);
                /*card_nombre.setBackgroundColor(Color.CYAN);
                card_area.setCardBackgroundColor(Color.CYAN);*/
            }
        }
        //card_nombre.setBackgroundColor(Color.CYAN);

        //colorAmarillo
        nombre.setText(equipoModel.get(position).Nombres);
        area.setText(equipoModel.get(position).Cargo);

        return convertView;
    }
}
