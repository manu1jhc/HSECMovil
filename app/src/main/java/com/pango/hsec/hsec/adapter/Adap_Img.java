package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
    //RelativeLayout rel_otros;

    public Adap_Img(Context context,GetGaleriaModel getGaleriaModel) {
        this.context = context;
        this.getGaleriaModel=getGaleriaModel;
    }


    @Override
    public int getCount() {
        return getGaleriaModel.Data.size();
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
        //rel_otros=(RelativeLayout) view.findViewById(R.id.rel_otros);

        final GaleriaModel item = getItem(position);
        if(getGaleriaModel.Data.get(position).TipoArchivo.equals("TP03")) {
           /// rel_otros.setVisibility(View.VISIBLE);

    } else

        if(getGaleriaModel.Data.get(position).TipoArchivo.equals("TP01"))
            {

                //"media/getImagepreview/" + Descripcion+ "/Preview.jpg"
            Glide.with(imagen.getContext())
                    .load(GlobalVariables.Url_base +"media/getImagepreview/"+item.Correlativo+ "/Preview.jpg")
                    .into(imagen);
        }
        else if(getGaleriaModel.Data.get(position).TipoArchivo.equals("TP04")){
            Glide.with(imagen.getContext())
                    .load(item.Url)
                    .into(imagen);
        }
        else{
            btn_play.setVisibility(View.VISIBLE);
            Glide.with(imagen.getContext())
                    .load(GlobalVariables.Url_base +"media/getImagepreview/"+item.Correlativo+ "/Preview.jpg")
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
