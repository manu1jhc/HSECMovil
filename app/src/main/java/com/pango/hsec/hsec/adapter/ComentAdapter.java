package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.ComentModel;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Andre on 11/01/2018.
 */

public class ComentAdapter extends ArrayAdapter<ComentModel> {
    private Context context;

    private ArrayList<ComentModel> data = new ArrayList<ComentModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public ComentAdapter(@NonNull Context context, ArrayList<ComentModel> data) {
        super(context, R.layout.public_comentario, data);
        this.data = data;
        this.context = context;
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.public_comentario, null, true);

        ImageView com_perfil = rowView.findViewById(R.id.img_foto);
        TextView com_nombre = rowView.findViewById(R.id.tx_nombre);
        TextView com_fecha = rowView.findViewById(R.id.tx_fecha);
        TextView com_detalle = rowView.findViewById(R.id.tx_detalle);



        final String tempcom_perfil="media/getAvatar/"+data.get(position).Estado+"/fotocarnet.jpg";


        final String tempcom_nombre = data.get(position).Nombres;
        final String tempcom_fecha = data.get(position).Fecha;
        final String tempcom_detalle = data.get(position).Comentario;


        com_nombre.setText(tempcom_nombre);
        com_detalle.setText(tempcom_detalle);



       //com_fecha.setText(tempcom_fecha);


        try {
            com_fecha.setText(formatoRender.format(formatoInicial.parse(tempcom_fecha)));

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

                .into(com_perfil);


        return rowView;
    }

}
