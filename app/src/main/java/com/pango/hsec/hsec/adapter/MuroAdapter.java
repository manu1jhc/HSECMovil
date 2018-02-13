package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Andre on 12/02/2018.
 */

public class MuroAdapter extends ArrayAdapter<PublicacionModel> {
    private Context context;

    private ArrayList<PublicacionModel> data = new ArrayList<PublicacionModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public MuroAdapter(Context context, ArrayList<PublicacionModel> data) {
        super(context,  R.layout.publicalist, data);
        this.data = data;
        this.context = context;
    }



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
        //final String tempImgDet="";
        final String tempImgDet=data.get(position).UrlPrew;

        nombre.setText(tempNombre);



        fecha.setText(Obtenerfecha(tempFecha));

        if(tempRiesgo==null) {
            riesgo.setVisibility(View.INVISIBLE);
        }else if (tempRiesgo.equals("BA")) {
            //riesgo.setCardBackgroundColor(Color.GREEN);
            riesgo.setImageResource(R.drawable.green_light);

        } else if (tempRiesgo.equals("ME")) {
            //riesgo.setCardBackgroundColor(Color.YELLOW);
            riesgo.setImageResource(R.drawable.yellow_light);

        } else {
            //riesgo.setCardBackgroundColor(Color.RED);
            riesgo.setImageResource(R.drawable.red_light);

        }

        String tipo_ejm= GlobalVariables.getDescripcion(GlobalVariables.Tipo_obs2,tempTipo);
        String area_ejm=GlobalVariables.getDescripcion(GlobalVariables.Area_obs,tempArea);

        tipo.setText(GlobalVariables.getDescripcion(GlobalVariables.Tipo_obs2,tempTipo));
        area.setText(GlobalVariables.getDescripcion(GlobalVariables.Area_obs,tempArea));
        tx_det.setText(tempDetalle);

        comentario.setText(comentarios+" comentarios");

        comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Comentarios",Toast.LENGTH_SHORT).show();

                GlobalVariables.istabs=true;

                Intent intent = new Intent(v.getContext(), ActMuroDet.class);
                intent.putExtra("codObs",data.get(position).Codigo);
                intent.putExtra("posTab",3);
                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                v.getContext().startActivity(intent);



            }
        });

        // img_perfil.setImageResource(R.drawable.fotocarnet);

        String Url_img=GlobalVariables.Url_base + "media/getAvatar/"+ Utils.ChangeUrl(tempimg_perfil)+"/Carnet.jpg";
        //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
        Glide.with(context)
                .load(Url_img)
                .override(50, 50)

                .into(img_perfil);


        if(tempImgDet==null){
            img_det.setVisibility(View.GONE);

        }else{
            String Url_prev=GlobalVariables.Url_base + "media/getImagepreview/"+tempImgDet+"/Preview.jpg";

            //"media/getImagepreview/" + Descripcion+ "/Preview.jpg"
            //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
            Glide.with(context)
                    .load(Url_prev)
                    //.override(50, 50)

                    .into(img_det);
        }





        return rowView;
    }


    public String Obtenerfecha(String tempcom_fecha) {

        String fecha="";
        try {
            fecha=formatoRender.format(formatoInicial.parse(tempcom_fecha));
        } catch (ParseException e) {
            e.printStackTrace();
            fecha=tempcom_fecha;
        }

        return fecha;

    }


}
