package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.ComentModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Andre on 24/05/2018.
 */

public class ComentRecAdapter extends RecyclerView.Adapter<ComentRecAdapter.ComentRecViewHolder> {

    private Context context;
    private ArrayList<ComentModel> data = new ArrayList<ComentModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");



    public ComentRecAdapter(ArrayList<ComentModel> data, Context context) {
        this.data = data;
        this.context=context;
    }


    @Override
    public void onBindViewHolder(ComentRecAdapter.ComentRecViewHolder comentRecViewHolder, int i) {
        //data cm = data.get(i);

        final String tempcom_perfil="media/getAvatar/"+data.get(i).Estado.replace("*","").replace(".","")+"/fotocarnet.jpg";
        final String tempcom_nombre = data.get(i).Nombres;
        final String tempcom_fecha = data.get(i).Fecha;

        final String tempcom_detalle = data.get(i).Comentario;

        comentRecViewHolder.com_nombre.setText(tempcom_nombre);

        comentRecViewHolder.com_detalle.setText(tempcom_detalle);

        try {
            comentRecViewHolder.com_fecha.setText(formatoRender.format(formatoInicial.parse(tempcom_fecha)));

        } catch (ParseException e) {
            e.printStackTrace();
        }



        //com_detalle.setVisibility(View.VISIBLE);

        // img_perfil.setImageResource(R.drawable.fotocarnet);

        String Url_img= GlobalVariables.Url_base + Utils.ChangeUrl(tempcom_perfil);
        //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
        Glide.with(context)
                .load(Url_img)
                .override(50, 50)

                .into(comentRecViewHolder.com_perfil);




    }



    @Override
    public ComentRecAdapter.ComentRecViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_comentario, viewGroup, false);

        ComentRecAdapter.ComentRecViewHolder nvh= new ComentRecAdapter.ComentRecViewHolder(itemView);

        return nvh;

    }

    @Override
    public int getItemCount() {

        return data.size();

    }


    public static class ComentRecViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables

        protected ImageView com_perfil;
        protected TextView com_nombre;
        protected TextView com_fecha;
        protected TextView com_detalle;

        //uniendo con los layouts
        public ComentRecViewHolder(View v) {
            super(v);

             com_perfil = v.findViewById(R.id.img_foto);
             com_nombre = v.findViewById(R.id.tx_nombre);
             com_fecha = v.findViewById(R.id.tx_fecha);
             com_detalle = v.findViewById(R.id.tx_detalle);


        }


    }





}
