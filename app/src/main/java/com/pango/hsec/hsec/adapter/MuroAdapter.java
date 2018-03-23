package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.pango.hsec.hsec.Ficha.FichaPersona;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Noticias.ActNoticiaDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import layout.FragmentFichaPersonal;

/**
 * Created by Andre on 12/02/2018.
 */

public class MuroAdapter extends ArrayAdapter<PublicacionModel>  {
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
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        int tipo=5;
        String identificador=data.get(position).Codigo.substring(0,3);
        switch (data.get(position).Codigo.substring(0,3)){
            case "OBS":
                tipo=0;
                break;
            case "INS":
                tipo=1;
                break;
            case "NOT":
                tipo=2;
                break;
        }
        return tipo;    //objects[position].getType();
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)  {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        final View rowView;
        int positem=getItemViewType(position);

        if (positem == 0) {
            rowView = inflater.inflate(R.layout.publicalist, null, true);
            ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
            ImageView img_det = rowView.findViewById(R.id.mp_imgdet);
            TextView nombre = rowView.findViewById(R.id.mp_nombre);
            TextView fecha = rowView.findViewById(R.id.mp_fecha);
            ImageView riesgo = rowView.findViewById(R.id.img_riesgo);
            TextView tipo = rowView.findViewById(R.id.mp_tipo);
            TextView area = rowView.findViewById(R.id.mp_area);
            TextView comentario = rowView.findViewById(R.id.tx_comentario);
            TextView tx_det = rowView.findViewById(R.id.mp_txdet);

            final String tempimg_perfil = data.get(position).UrlObs;
            final String tempNombre = data.get(position).ObsPor;
            final String tempFecha = data.get(position).Fecha;
            final String tempRiesgo = data.get(position).NivelR;
            final String tempTipo = data.get(position).Tipo;
            final String tempArea = data.get(position).Area;
            final String tempDetalle = data.get(position).Obs;
            final int comentarios = data.get(position).Comentarios;
            final String tempImgDet = data.get(position).UrlPrew;

            nombre.setText(tempNombre);
            fecha.setText(Obtenerfecha(tempFecha));

            if (tempRiesgo == null) {
                riesgo.setVisibility(View.INVISIBLE);
            } else if (tempRiesgo.equals("BA")) {
                riesgo.setImageResource(R.drawable.green_light);

            } else if (tempRiesgo.equals("ME")) {
                riesgo.setImageResource(R.drawable.yellow_light);

            } else {
                riesgo.setImageResource(R.drawable.red_light);

            }
            tipo.setText(GlobalVariables.getDescripcion(GlobalVariables.Tipo_obs2, tempTipo));
            area.setText(GlobalVariables.getDescripcion(GlobalVariables.Area_obs, tempArea));
            tx_det.setText(tempDetalle);

            comentario.setText(comentarios + " comentarios");
            comentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tipoObs=data.get(position).Tipo;

                    Toast.makeText(v.getContext(), "Comentarios", Toast.LENGTH_SHORT).show();

                    GlobalVariables.istabs = true;
                    Intent intent = new Intent(v.getContext(), ActMuroDet.class);
                    intent.putExtra("codObs", data.get(position).Codigo);
                    intent.putExtra("posTab", 4);
                    intent.putExtra("tipoObs",tipoObs);


                    v.getContext().startActivity(intent);
                }
            });

            ///AQUI ESTA EL ERROR

            if (tempimg_perfil == null) {
                img_perfil.setImageResource(R.drawable.ic_usuario);
            }else {
                String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + tempimg_perfil + "/Carnet.jpg";
                //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
                Glide.with(context)
                        .load(Url_img)
                        .override(50, 50)
                        .into(img_perfil);
            }

            if (tempImgDet == null) {
                img_det.setVisibility(View.GONE);

            } else {
                String Url_prev = GlobalVariables.Url_base + "media/getImagepreview/" + tempImgDet + "/Preview.jpg";
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
        }else if(positem == 1) {
            rowView = inflater.inflate(R.layout.public_inspeccion, null, true);
            ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
            ImageView img_det = rowView.findViewById(R.id.mp_imgdet);
            TextView nombre = rowView.findViewById(R.id.mp_nombre);
            TextView fecha = rowView.findViewById(R.id.mp_fecha);
            ImageView riesgo1=rowView.findViewById(R.id.img_riesgo1);
            ImageView riesgo2=rowView.findViewById(R.id.img_riesgo2);
            ImageView riesgo3=rowView.findViewById(R.id.img_riesgo3);
            TextView tipo_insp = rowView.findViewById(R.id.mp_tipoinsp);

            TextView comentario=rowView.findViewById(R.id.tx_comentario);
            TextView tx_det1 = rowView.findViewById(R.id.mp_txdet);
            TextView tx_det2 = rowView.findViewById(R.id.mp_txdet2);
            TextView tx_det3 = rowView.findViewById(R.id.mp_txdet3);
            TextView txdetcompleta=rowView.findViewById(R.id.txdetcompleta);


            ConstraintLayout const1=rowView.findViewById(R.id.constrain1);
            //media/getAvatar/30642172/Carnet.jpg

            final String tempimg_perfil=data.get(position).UrlObs;

            final String tempNombre = data.get(position).ObsPor;
            final String tempFecha = data.get(position).Fecha;
            final String tempTipoInsp = data.get(position).Tipo;

            final int comentarios=data.get(position).Comentarios;
            //final String tempImgDet="";
            final String tempImgDet=data.get(position).UrlPrew;

            String[] tempRiesgo = new String[0];
            String[] tempDetalle = new String[0];

            if(data.get(position).NivelR!=null){
                tempRiesgo = data.get(position).NivelR.split(";");
                tempDetalle = data.get(position).Obs.split(";");
            }

            tipo_insp.setText(GlobalVariables.getDescripcion(GlobalVariables.Tipo_insp,tempTipoInsp));

            nombre.setText(tempNombre);
            fecha.setText(Obtenerfecha(tempFecha));

            comentario.setText(comentarios+" comentarios");

            comentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Comentarios",Toast.LENGTH_SHORT).show();
                    GlobalVariables.istabs=true;

                    Intent intent = new Intent(v.getContext(), ActInspeccionDet.class);
                    intent.putExtra("codObs",data.get(position).Codigo);
                    intent.putExtra("posTab",3);
                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                    v.getContext().startActivity(intent);

                }
            });

            if(tempimg_perfil==null){
                img_perfil.setImageResource(R.drawable.ic_usuario);
            }else {
                String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + tempimg_perfil + "/Carnet.jpg";
                //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
                Glide.with(context)
                        .load(Url_img)
                        .override(50, 50)
                        .into(img_perfil);
            }
            if(tempImgDet==null){
                img_det.setVisibility(View.GONE);

            }else{
                String Url_prev=Utils.ChangeUrl(GlobalVariables.Url_base + "media/getImagepreview/"+tempImgDet+"/loco.jpg");
                Glide.with(context)
                        .load(Url_prev)
                        //.override(50, 50)
                        .into(img_det);
            }

            if(tempRiesgo.length==0){
                riesgo1.setVisibility(View.GONE);
                riesgo2.setVisibility(View.GONE);
                riesgo3.setVisibility(View.GONE);
                tx_det1.setVisibility(View.GONE);
                tx_det2.setVisibility(View.GONE);
                tx_det3.setVisibility(View.GONE);

            }else
            if(tempRiesgo.length==1){
                riesgo2.setVisibility(View.GONE);
                riesgo3.setVisibility(View.GONE);
                tx_det1.setVisibility(View.GONE);
                tx_det2.setVisibility(View.GONE);
                tx_det3.setVisibility(View.GONE);
                txdetcompleta.setVisibility(View.VISIBLE);
                txdetcompleta.setText(tempDetalle[0].trim());
                switch (tempRiesgo[0]){
                    case "BA":
                        riesgo1.setImageResource(R.drawable.green_light);
                        break;
                    case "ME":
                        riesgo1.setImageResource(R.drawable.yellow_light);
                        break;
                    case "AL":
                        riesgo1.setImageResource(R.drawable.red_light);
                        break;

                }
            }else if(tempRiesgo.length==2){
                riesgo3.setVisibility(View.GONE);
                tx_det1.setText(tempDetalle[0]);
                tx_det2.setText(tempDetalle[1]);
                //const1.getLayoutParams().height=50;

                switch (tempRiesgo[0]){
                    case "BA":
                        riesgo1.setImageResource(R.drawable.green_light);
                        break;
                    case "ME":
                        riesgo1.setImageResource(R.drawable.yellow_light);
                        break;
                    case "AL":
                        riesgo1.setImageResource(R.drawable.red_light);
                        break;

                }

                switch (tempRiesgo[1]){
                    case "BA":
                        riesgo2.setImageResource(R.drawable.green_light);
                        break;
                    case "ME":
                        riesgo2.setImageResource(R.drawable.yellow_light);
                        break;
                    case "AL":
                        riesgo2.setImageResource(R.drawable.red_light);
                        break;

                }
            }else if(tempRiesgo.length==3){
                tx_det1.setText(tempDetalle[0]);
                tx_det2.setText(tempDetalle[1]);
                tx_det3.setText(tempDetalle[2]);
                switch (tempRiesgo[0]){
                    case "BA":
                        riesgo1.setImageResource(R.drawable.green_light);
                        break;
                    case "ME":
                        riesgo1.setImageResource(R.drawable.yellow_light);
                        break;
                    case "AL":
                        riesgo1.setImageResource(R.drawable.red_light);
                        break;

                }

                switch (tempRiesgo[1]){
                    case "BA":
                        riesgo2.setImageResource(R.drawable.green_light);
                        break;
                    case "ME":
                        riesgo2.setImageResource(R.drawable.yellow_light);
                        break;
                    case "AL":
                        riesgo2.setImageResource(R.drawable.red_light);
                        break;
                }
                switch (tempRiesgo[2]) {
                    case "BA":
                        riesgo3.setImageResource(R.drawable.green_light);
                        break;
                    case "ME":
                        riesgo3.setImageResource(R.drawable.yellow_light);
                        break;
                    case "AL":
                        riesgo3.setImageResource(R.drawable.red_light);
                        break;
                }
            }

            img_perfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //GlobalVariables.desdeBusqueda=true;
                    GlobalVariables.barTitulo=false;
                    GlobalVariables.dniUser=data.get(position).UrlObs;
                    Intent intent=new Intent(context,FichaPersona.class);
                    //intent.putExtra("codUsuario",tempimg_perfil);
                    context.startActivity(intent);
                }
            });


            return rowView;
        }else if(positem == 2) {
            rowView = inflater.inflate(R.layout.public_noticias, null, true);
            ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
            ImageView img_preview = rowView.findViewById(R.id.img_preview);
            TextView nombre = rowView.findViewById(R.id.mp_nombre);
            TextView fecha = rowView.findViewById(R.id.mp_fecha);
            TextView titulo = rowView.findViewById(R.id.tx_titulo);
            TextView descripcion = rowView.findViewById(R.id.tx_descripcion);
            TextView comentario=rowView.findViewById(R.id.tx_comentario);

            final String tempimg_perfil=data.get(position).UrlObs;
            final String tempNombre = data.get(position).ObsPor;
            final String tempFecha = data.get(position).Fecha;

            final String tempTitulo = data.get(position).Area;
            final String tempdescripcion = data.get(position).Obs;

            final String tempImgDet=data.get(position).UrlPrew;

            final int comentarios=data.get(position).Comentarios;
            nombre.setText(tempNombre);
            fecha.setText(Obtenerfecha(tempFecha));
            titulo.setText(tempTitulo);
            descripcion.setText(tempdescripcion);
            comentario.setText(comentarios+" comentarios");
            comentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Comentarios",Toast.LENGTH_SHORT).show();
                    GlobalVariables.istabs=true;
                    Intent intent = new Intent(v.getContext(), ActNoticiaDet.class);
                    intent.putExtra("codObs",data.get(position).Codigo);
                    intent.putExtra("posTab",2);
                    //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                    v.getContext().startActivity(intent);

                }
            });


            if(tempimg_perfil==null){
                img_perfil.setImageResource(R.drawable.ic_usuario);
            }else {
                String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + tempimg_perfil + "/Carnet.jpg";
                //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
                Glide.with(context)
                        .load(Url_img)
                        .override(50, 50)
                        .into(img_perfil);
            }

            if(tempImgDet==null){
                img_preview.setVisibility(View.GONE);

            }else{
                img_preview.setVisibility(View.VISIBLE);

                String Url_prev=GlobalVariables.Url_base +"media/getImagepreview/"+Utils.ChangeUrl(tempImgDet)+"/Image.jpg";

                //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
                Glide.with(context)
                        .load(Url_prev)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        //.override(0, 0)
                        .fitCenter()
                        .into(img_preview);
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

        }else {
            return null;
        }
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
