package com.pango.hsec.hsec.adapter;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.Facilito.obsfacilitoAprobar;
import com.pango.hsec.hsec.Facilito.report_obs;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.ObsFacilitoModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.pango.hsec.hsec.Utils.Obtenerfecha;

/**
 * Created by jcila on 20/04/2018.
 */

public class ObsFacilitoAdapter extends  ArrayAdapter<ObsFacilitoModel> {
    private Context context;
    private ArrayList<ObsFacilitoModel> data = new ArrayList<ObsFacilitoModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public ObsFacilitoAdapter(Context context, ArrayList<ObsFacilitoModel> data) {
        super(context, R.layout.public_obsfacilito, data);
        this.data = data;
        this.context = context;
    }
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)  {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        final View rowView;
        rowView = inflater.inflate(R.layout.public_obsfacilito, null, true);
        ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
        ImageView img_det = rowView.findViewById(R.id.mp_imgdet);
        TextView nombre = rowView.findViewById(R.id.mp_nombre);
        TextView fecha = rowView.findViewById(R.id.mp_fecha);
        ImageView riesgo = rowView.findViewById(R.id.img_riesgo);
        TextView tipo = rowView.findViewById(R.id.mp_tipo);
        TextView area = rowView.findViewById(R.id.mp_area);
//        TextView accion=rowView.findViewById(R.id.mp_txaccion);
        TextView tx_det = rowView.findViewById(R.id.mp_txdet);
        ImageView editar = rowView.findViewById(R.id.btn_editar);

        final String tempimg_perfil = data.get(position).UrlObs;
        final String tempNombre = data.get(position).Persona;
        final String tempFecha = data.get(position).FecCreacion;
        final String tempUbicacion = data.get(position).UbicacionExacta;
        final String tempTipo = data.get(position).Tipo;
        final String tempObservacion = data.get(position).Observacion;
        final String tempAccion = data.get(position).Accion;
        final String tempImgDet=data.get(position).UrlPrew;
        final String editable = data.get(position).Editable;
        if(editable.equals("0")){
            editar.setVisibility(View.GONE);
        }
            editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String edit=data.get(position).Editable;
                    if(edit.equals("1") || edit.equals("3")){
                        GlobalVariables.ObjectEditable=true;
                        Intent intent = new Intent(getContext(),report_obs.class);
                        intent.putExtra("codObs", data.get(position).CodObsFacilito);
                        v.getContext().startActivity(intent);
                    }
                    else if(edit.equals("2")){

                        Intent intent = new Intent(getContext(),obsfacilitoAprobar.class);
                        intent.putExtra("codObs", data.get(position).CodObsFacilito);
                        v.getContext().startActivity(intent);
                    }

                }
            });
        nombre.setText(tempNombre);
        fecha.setText(Obtenerfecha(tempFecha));
        tipo.setText(tempTipo);
        area.setText(tempUbicacion);
//        accion.setText(tempAccion);
        tx_det.setText(tempObservacion);
        if (tempTipo == null) {
            riesgo.setVisibility(View.INVISIBLE);
        } else if (tempTipo.equals("A")) {
            riesgo.setImageResource(R.drawable.ic_alertaroja);

        } else if (tempTipo.equals("C")) {
            riesgo.setImageResource(R.drawable.ic_alerta_amarilla);
        }

        if (tempimg_perfil == null) {
            img_perfil.setImageResource(R.drawable.ic_loginusuario);
        }else {
            String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + tempimg_perfil + "/Carnet.jpg";
            //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
            Glide.with(context)
                    .load(Url_img)
                    .override(50, 50)
                    .transform(new CircleTransform(getContext())) // applying the image transformer
                    .into(img_perfil);
        }
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
