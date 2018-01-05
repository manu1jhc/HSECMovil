package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 15/12/2017.
 */

public class PublicacionAdapter extends ArrayAdapter<PublicacionModel> {
    private Context context;

    private ArrayList<PublicacionModel> data = new ArrayList<PublicacionModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public PublicacionAdapter(@NonNull Context context, ArrayList<PublicacionModel> data) {
        super(context, R.layout.publicalist, data);
        this.data = data;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.publicalist, null, true);

        ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
        ImageView img_det = rowView.findViewById(R.id.mp_imgdet);


        TextView nombre = rowView.findViewById(R.id.mp_nombre);
        TextView fecha = rowView.findViewById(R.id.mp_fecha);
        ImageView riesgo=rowView.findViewById(R.id.img_riesgo);
        //CardView riesgo = rowView.findViewById(R.id.mp_nriesgo);
        TextView tipo = rowView.findViewById(R.id.mp_tipo);
        TextView area = rowView.findViewById(R.id.mp_area);
        TextView comentario=rowView.findViewById(R.id.tx_comentario);

        TextView tx_det = rowView.findViewById(R.id.mp_txdet);

        final String tempimg_perfil=data.get(position).UrlObs;
        final String tempNombre = data.get(position).ObsPor;
        final String tempFecha = data.get(position).Fecha;
        final String tempRiesgo = data.get(position).NivelR;
        final String tempTipo = data.get(position).Tipo;
        final String tempArea = data.get(position).Area;
        final String tempDetalle = data.get(position).Obs;
        final int comentarios=data.get(position).Comentarios;

        final String tempImgDet=data.get(position).UrlPrew;



        nombre.setText(tempNombre);
        fecha.setText(tempFecha);

        if (tempRiesgo.equals("BA")) {
            //riesgo.setCardBackgroundColor(Color.GREEN);
            riesgo.setImageResource(R.drawable.green_light);

        } else if (tempRiesgo.equals("ME")) {
            //riesgo.setCardBackgroundColor(Color.YELLOW);
            riesgo.setImageResource(R.drawable.yellow_light);

        } else {
            //riesgo.setCardBackgroundColor(Color.RED);
            riesgo.setImageResource(R.drawable.red_light);

        }


        tipo.setText(ObtenerTipo(tempTipo));
        area.setText(ObtenerArea(tempArea));
        tx_det.setText(tempDetalle);

        comentario.setText(comentarios+" comentarios");



        comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Comentarios",Toast.LENGTH_SHORT).show();

            }
        });

       // img_perfil.setImageResource(R.drawable.fotocarnet);

        String Url_img=GlobalVariables.Url_base + Utils.ChangeUrl(tempimg_perfil);
        //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
        Glide.with(context)
                .load(Url_img)
                .override(50, 50)

                .into(img_perfil);


        if(tempImgDet==null){
            img_det.setVisibility(View.GONE);

        }else{
            String Url_prev=GlobalVariables.Url_base + Utils.ChangeUrl(tempImgDet);
            //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
            Glide.with(context)
                    .load(Url_prev)
                    //.override(50, 50)

                    .into(img_det);
        }





        return rowView;
    }




    public String ObtenerTipo(String tempTipo) {
        String tipo="";

        for(int i=0;i<GlobalVariables.tipo.size();i++){
            if(tempTipo.equals(GlobalVariables.tipo.get(i).getCodTipo())){
                tipo=GlobalVariables.tipo.get(i).getNombre();
                break;
            }
        }

        return tipo;



    }

    public String ObtenerArea(String tempArea) {

        String area="";
        for(int i=0;i<GlobalVariables.Area_usuario.size();i++){
            if(tempArea.equals(GlobalVariables.Area_usuario.get(i).getCodArea())){
                area=GlobalVariables.Area_usuario.get(i).getNombre();
                break;
            }
        }

        return area;



    }


    }