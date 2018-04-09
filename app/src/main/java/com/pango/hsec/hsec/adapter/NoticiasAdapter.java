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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pango.hsec.hsec.Ficha.FichaPersona;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Noticias.ActNoticiaDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.NoticiasModel;
import com.pango.hsec.hsec.model.PublicacionModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class NoticiasAdapter extends ArrayAdapter<PublicacionModel> {
    private Context context;
    private ArrayList<PublicacionModel> data = new ArrayList<PublicacionModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public NoticiasAdapter(Context context,  ArrayList<PublicacionModel> data) {
        super(context, R.layout.public_noticias ,data);
        this.data = data;
        this.context = context;
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.public_noticias, null, true);


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

        // img_perfil.setImageResource(R.drawable.fotocarnet);

        if(tempimg_perfil==null){
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
