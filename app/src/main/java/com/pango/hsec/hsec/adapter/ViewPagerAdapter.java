package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Observaciones.ActVidDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.utilitario.TouchImageView;

import java.util.List;

/**
 * Created by Andre on 09/01/2018.
 */

public class ViewPagerAdapter extends PagerAdapter {

    Activity activity;


    List<GaleriaModel> images;
    LayoutInflater inflater;
    int positionIn;

    public ViewPagerAdapter(Activity activity, List<GaleriaModel> images, int positionIn) {
        this.activity = activity;
        this.images = images;
        this.positionIn = positionIn;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        inflater = (LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.viewpager_item,container,false);

        TouchImageView image;

        //ImageView image;
        //GlobalVariables.listaGaleria
        ImageView btn_play=(ImageView)  itemView.findViewById(R.id.btn_play);
        image = (TouchImageView) itemView.findViewById(R.id.imagen_extendida);
        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);

        String host = GlobalVariables.listaGaleria.get(position).Correlativo>0?GlobalVariables.Url_base:"";
        String url= GlobalVariables.listaGaleria.get(position).Url;
        if(GlobalVariables.listaGaleria.get(position).TipoArchivo.equals("TP02")){
            btn_play.setVisibility(View.VISIBLE);
            if(GlobalVariables.listaGaleria.get(position).Correlativo>0) url="media/getImagepreview/"+GlobalVariables.listaGaleria.get(position).Correlativo+ "/Preview.jpg";
        }

        if (!GlobalVariables.listaGaleria.get(position).TipoArchivo.equals("TP03")){
            try {
                Glide.with(image.getContext())
                        .load(host +url)
                        // .fitCenter()
                        .into(image);
                // positionIn= position;
            } catch (Exception ex) {
            }
            container.addView(itemView);
        }

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalTempUrl=host+GlobalVariables.listaGaleria.get(position).Url;
                Intent intent = new Intent(activity, ActVidDet.class);
                intent.putExtra("urltemp", finalTempUrl);
                intent.putExtra("isList", true);
                activity.startActivity(intent);

            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View)object);
    }

}
