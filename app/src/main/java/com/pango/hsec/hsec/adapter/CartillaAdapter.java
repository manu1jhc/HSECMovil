package com.pango.hsec.hsec.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.CartillaModel;

import java.util.ArrayList;

public class CartillaAdapter extends ArrayAdapter<CartillaModel> {
    private Context context;
    private ArrayList<CartillaModel> item = new ArrayList<>();

    public CartillaAdapter(@NonNull Context context, ArrayList<CartillaModel> data) {
        super(context, R.layout.public_buscarp, data);
        this.item = data;
        this.context = context;
    }

    public void add(CartillaModel newItem){
        item.add(newItem);
    }
    public CartillaModel getItem(int position){
        return item.get(position);
    }
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.list_row, null, true);

        TextView cartillaName=rowView.findViewById(R.id.tvName);
        TextView PeligroF=rowView.findViewById(R.id.tvCargo);
        TextView Pendiente=rowView.findViewById(R.id.tvNota);
        CardView cardView = rowView.findViewById(R.id.cardView3);

        final String cartilla=item.get(position).CodCartilla;
        final String peligroFT = item.get(position).Estado;
        final Integer Pendientev = item.get(position).Pendientes;

        cartillaName.setText(cartilla);
        PeligroF.setText(peligroFT);
        if(Pendientev!=null)Pendiente.setText(Pendientev+"");
        else cardView.setVisibility(View.GONE);
        return rowView;
    }
}