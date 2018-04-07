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
import com.bumptech.glide.request.target.Target;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.EstadisticaDetModel;
import com.pango.hsec.hsec.model.EstadisticaModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Andre on 05/03/2018.
 */

public class EstadisticaDetAdapter extends ArrayAdapter<EstadisticaDetModel> {

    private Context context;
    private ArrayList<EstadisticaDetModel> data = new ArrayList<EstadisticaDetModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public EstadisticaDetAdapter(Context context,  ArrayList<EstadisticaDetModel> data) {
        super(context, R.layout.public_estadistica_det, data);

        this.data = data;
        this.context = context;

    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.public_estadistica_det, null, true);


        TextView Nombre = rowView.findViewById(R.id.es_titulo);
        TextView Fecha = rowView.findViewById(R.id.tx_fecha);
        TextView Descripcion = rowView.findViewById(R.id.tx_descripcion);
        ImageView profile=rowView.findViewById(R.id.tx_profile);

        final String tempNombre=data.get(position).Responsable;
        final String tempFecha = data.get(position).Fecha;
        final String tempDescripcion = data.get(position).Descripcion;

        final String tempImgProf = data.get(position).ResponsableDNI;

        Descripcion.setText(tempDescripcion);

        Fecha.setText(Obtenerfecha(tempFecha));



        if(tempNombre==null||tempImgProf==null){

            Nombre.setText(GlobalVariables.nombre);

                String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + GlobalVariables.dniUser + "/Carnet.jpg";
                //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
                Glide.with(context)
                        .load(Url_img)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .transform(new CircleTransform(getContext())) // applying the image transformer
                        .fitCenter()
                        .into(profile);
            }else{
            Nombre.setText(tempNombre);
            String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + tempImgProf + "/Carnet.jpg";
            //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
            Glide.with(context)
                    .load(Url_img)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .fitCenter()
                    .into(profile);
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


