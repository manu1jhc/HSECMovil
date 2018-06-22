package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Observaciones.ActVidDet;
import com.pango.hsec.hsec.Observaciones.Galeria_detalle;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.GaleriaModel;

import java.util.ArrayList;

/**
 * Created by Andre on 27/03/2018.
 */

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.ViewHolder>{


    private ArrayList<GaleriaModel> Data;
    private Activity activity;
    public GaleriaAdapter(Activity activity, ArrayList<GaleriaModel> Data) {
        this.activity = activity;
        this.Data = Data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.public_grid, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //viewHolder.imageView.setImageResource(items.get(position).getDrawableId());
        viewHolder.position_item=position;
        if(Data.get(position).TipoArchivo.equals("TP01"))
        {

            //"media/getImagepreview/" + Descripcion+ "/Preview.jpg"
            Glide.with(viewHolder.imageView.getContext())
                    .load(GlobalVariables.Url_base +"media/getImagepreview/"+Data.get(position).Correlativo+ "/Preview.jpg")
                    .centerCrop()
                    //.fitCenter()
                    .into(viewHolder.imageView);
        }else{
            viewHolder.btn_play.setVisibility(View.VISIBLE);
            Glide.with(viewHolder.imageView.getContext())
                    .load(GlobalVariables.Url_base +"media/getImagepreview/"+Data.get(position).Correlativo+ "/Preview.jpg")
                 //   .centerCrop()
                    .into(viewHolder.imageView);

        }
        //viewHolder.textView.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        //private TextView textView;
        ImageView btn_play;
        public int position_item;

        public ViewHolder(View view) {
            super(view);
            //textView = (TextView)view.findViewById(R.id.text);
            imageView = (ImageView) view.findViewById(R.id.image);
            btn_play=(ImageView) view.findViewById(R.id.btn_playGrid);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            if(Data.get(position_item).TipoArchivo.equals("TP01")) {

                Intent intent = new Intent(v.getContext(), Galeria_detalle.class);
                //intent.putExtra(ActImagDet.EXTRA_PARAM_ID, a);
                intent.putExtra("post", position_item);
                v.getContext().startActivity(intent);
            }else if(Data.get(position_item).TipoArchivo.equals("TP02")){

                //String finalTempUrl="https://app.antapaccay.com.pe/Proportal/SCOM_Service/Videos/1700.mp4";
                String finalTempUrl=GlobalVariables.Url_base+Data.get(position_item).Url;

                //Toast.makeText(activity,"video",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), ActVidDet.class);
                //intent.putExtra("post",position);
                intent.putExtra("urltemp", finalTempUrl);
                intent.putExtra("isList", true);

                //intent.putExtra("val",0);
                //intent.putExtra(ActVidDet.EXTRA_PARAM_ID, item.getId());
                v.getContext().startActivity(intent);

            }
        }
    }

}
