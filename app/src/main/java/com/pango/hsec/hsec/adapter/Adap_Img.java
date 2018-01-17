package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;

/**
 * Created by Andre on 09/01/2018.
 */

public class Adap_Img extends BaseAdapter {
    private Context context;
    private GetGaleriaModel getGaleriaModel;
    public Adap_Img(Context context,GetGaleriaModel getGaleriaModel) {
        this.context = context;
        this.getGaleriaModel=getGaleriaModel;
    }


    @Override
    public int getCount() {
        return getGaleriaModel.Count;
    }

    @Override
    public GaleriaModel getItem(int position) {
        return getGaleriaModel.Data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_galeria, viewGroup, false);
        }


        ImageView imagen = (ImageView) view.findViewById(R.id.img_adap);
        ImageView btn_play=(ImageView) view.findViewById(R.id.btn_playGrid);

        final GaleriaModel item = getItem(position);
        if(getGaleriaModel.Data.get(position).TipoArchivo.equals("TP03")) {

        } else if(getGaleriaModel.Data.get(position).TipoArchivo.equals("TP01"))
            {
            Glide.with(imagen.getContext())
                    .load(GlobalVariables.Url_base + item.Urlmin)
                    .into(imagen);
        }else{
            btn_play.setVisibility(View.VISIBLE);
            Glide.with(imagen.getContext())
                    .load(GlobalVariables.Url_base + item.Urlmin)
                    .into(imagen);
        }
       /*
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */
        return view;
    }
}
