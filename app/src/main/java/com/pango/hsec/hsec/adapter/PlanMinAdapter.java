package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Ficha.FichaPersona;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.PlanAccionEdit;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.PlanMinModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Andre on 12/03/2018.
 */

public class PlanMinAdapter extends ArrayAdapter<PlanMinModel> {

    private Activity context;
    private ArrayList<PlanMinModel> data = new ArrayList<PlanMinModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public PlanMinAdapter(Activity context, ArrayList<PlanMinModel> data) {
        super(context, R.layout.public_planmin, data);
        this.data = data;
        this.context = context;
    }
    public void replace(PlanMinModel replacedata){
        data.get(indexOf(replacedata.CodAccion)).CodNivelRiesgo=replacedata.CodNivelRiesgo;
        data.get(indexOf(replacedata.CodAccion)).DesPlanAccion=replacedata.DesPlanAccion;
        data.set(indexOf(replacedata.CodAccion),replacedata);
        notifyDataSetChanged();
    }
    public int indexOf(String value){
        for (int i=0;i<data.size();i++  ) {
            if(data.get(i).CodAccion.equals(value)) return i;
        }
        return 0;
    }
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.public_planmin, null, true);

        ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
        TextView nombre = rowView.findViewById(R.id.nombre);
        TextView fecha = rowView.findViewById(R.id.fecha_sol);
        ImageView editar=rowView.findViewById(R.id.btn_editar);
        ImageView riesgo=rowView.findViewById(R.id.nivel_riesgo);
        TextView tabla_ref = rowView.findViewById(R.id.tabla_ref);
        TextView estado = rowView.findViewById(R.id.estado);
        TextView desTarea = rowView.findViewById(R.id.tx_destarea);


        final String tempimg_perfil=data.get(position).CodSolicitadoPor;
        final String tempNombre = data.get(position).SolicitadoPor;


        final String tempFecha = data.get(position).FechaSolicitud;
        final String tempEditar = data.get(position).Editable;
        final String tempRiesgo = data.get(position).CodNivelRiesgo;

        final String tempTabla_ref = data.get(position).CodTabla;
        final String tempEstado = data.get(position).CodEstadoAccion;
        final String tempDesTarea = data.get(position).DesPlanAccion;

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


        if(tempEditar.equals("1")||tempEditar.equals("3")){
            editar.setVisibility(View.VISIBLE);
        }else{
            editar.setVisibility(View.GONE);
        }

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, PlanAccionEdit.class);
                Gson gson = new Gson();
                i.putExtra("editplan", true);
                i.putExtra("Plan", gson.toJson(data.get(position)));
                context.startActivityForResult(i,3);
            }
        });
        //GlobalVariables.getDescripcion(GlobalVariables.Tipo_obs2,tempTipo);
        tabla_ref.setText(GlobalVariables.getDescripcion(GlobalVariables.Tablas,tempTabla_ref));
        estado.setText(GlobalVariables.getDescripcion(GlobalVariables.Estado_Plan,tempEstado));
        desTarea.setText(tempDesTarea);


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







/*
        comentario.setText(comentarios+" comentarios");

        comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tipoObs=data.get(position).Tipo;

                Toast.makeText(v.getContext(),"Comentarios",Toast.LENGTH_SHORT).show();

                GlobalVariables.istabs=true;

                Intent intent = new Intent(v.getContext(), ActMuroDet.class);
                intent.putExtra("codObs",data.get(position).Codigo);
                intent.putExtra("posTab",4);
                intent.putExtra("tipoObs",tipoObs);

                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                v.getContext().startActivity(intent);



            }
        });
*/
        // img_perfil.setImageResource(R.drawable.fotocarnet);

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
