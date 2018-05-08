package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.Ficha.FichaPersona;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.PublicacionModel;
import com.pango.hsec.hsec.observacion_edit;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Andre on 15/12/2017.
 */

public class PublicacionAdapter extends ArrayAdapter<PublicacionModel> {
    private Context context;

    private ArrayList<PublicacionModel> data = new ArrayList<PublicacionModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public PublicacionAdapter(Context context, ArrayList<PublicacionModel> data) {
        super(context, R.layout.publicalist, data);
        this.data = data;
        this.context = context;
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        final String editable = data.get(position).Editable;

        ImageView editar = rowView.findViewById(R.id.btn_editar);

        if(editable.equals("0")||(!tempTipo.equals("TO01")&& !tempTipo.equals("TO02"))){
            editar.setVisibility(View.GONE);
        }
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalVariables.ObjectEditable=true;
                GlobalVariables.isFragment=false;
                Intent intent = new Intent(getContext(),observacion_edit.class);
                intent.putExtra("codObs", data.get(position).Codigo);
                intent.putExtra("tipoObs", data.get(position).Tipo);
                intent.putExtra("posTab", 0);
                v.getContext().startActivity(intent);
            }
        });
        nombre.setText(tempNombre);

        fecha.setText(Obtenerfecha(tempFecha));

        if(tempRiesgo==null) {
            riesgo.setVisibility(View.INVISIBLE);
        }else if (tempRiesgo.equals("BA")) {
            //riesgo.setCardBackgroundColor(Color.GREEN);
            riesgo.setImageResource(R.drawable.ic_alertaverde);

        } else if (tempRiesgo.equals("ME")) {
            //riesgo.setCardBackgroundColor(Color.YELLOW);
            riesgo.setImageResource(R.drawable.ic_alerta_amarilla);

        } else {
            //riesgo.setCardBackgroundColor(Color.RED);
            riesgo.setImageResource(R.drawable.ic_alertaroja);

        }

        String tipo_ejm=GlobalVariables.getDescripcion(GlobalVariables.Tipo_obs2,tempTipo);
        String area_ejm=GlobalVariables.getDescripcion(GlobalVariables.Area_obs,tempArea);

        tipo.setText(" / "+GlobalVariables.getDescripcion(GlobalVariables.Tipo_obs2, tempTipo)+" / "+GlobalVariables.getDescripcion(GlobalVariables.Area_obs, tempArea));
        //area.setText(GlobalVariables.getDescripcion(GlobalVariables.Area_obs,tempArea));
        tx_det.setText(tempDetalle);

        comentario.setText(comentarios+" comentarios");

        comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tipoObs=data.get(position).Tipo;

                GlobalVariables.istabs=true;

                Intent intent = new Intent(v.getContext(), ActMuroDet.class);
                intent.putExtra("codObs",data.get(position).Codigo);
                intent.putExtra("posTab",4);
                intent.putExtra("tipoObs",tipoObs);

                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                v.getContext().startActivity(intent);



            }
        });

       // img_perfil.setImageResource(R.drawable.fotocarnet);

        if (tempimg_perfil == null) {
            img_perfil.setImageResource(R.drawable.ic_usuario);
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


        img_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GlobalVariables.desdeBusqueda=true;
                GlobalVariables.barTitulo=false;
                GlobalVariables.dniUser=tempimg_perfil;
                Intent intent=new Intent(context,FichaPersona.class);
                //intent.putExtra("codUsuario",tempimg_perfil);
                context.startActivity(intent);
            }
        });




        return rowView;
    }


    public String ObtenerTipo(String tempTipo) {
        String tipo="";

        for(int i=0;i<GlobalVariables.Tipo_obs.size();i++){
            if(tempTipo.equals(GlobalVariables.Tipo_obs.get(i).getCodTipo())){
                tipo=GlobalVariables.Tipo_obs.get(i).getDescripcion();
                break;
            }
        }

        return tipo;
    }

    public String ObtenerArea(String tempArea) {
/*
        String area="";
        for(int i=0;i<GlobalVariables.Area_obs.size();i++){
            if(tempArea.equals(GlobalVariables.Area_obs.get(i).getCodTipo())){
                area=GlobalVariables.Area_obs.get(i).getDescripcion();
                break;
            }
        }*/
        return GlobalVariables.getDescripcion(GlobalVariables.Area_obs,tempArea);

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