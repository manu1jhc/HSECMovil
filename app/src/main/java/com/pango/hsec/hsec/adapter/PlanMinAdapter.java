package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Ficha.BusqEstadistica;
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

import layout.FragmentPlanPendiente;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Andre on 12/03/2018.
 */

public class PlanMinAdapter extends ArrayAdapter<PlanMinModel> {

    private Activity context;
    private ArrayList<PlanMinModel> data = new ArrayList<PlanMinModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    FragmentPlanPendiente ActContent;
    View popupView;
    public PopupWindow popupWindow;

    public PlanMinAdapter( Activity context, ArrayList<PlanMinModel> data,FragmentPlanPendiente ActContent) {
        super(context, R.layout.public_planmin, data);
        this.data = data;
        this.context = context;
        this.ActContent=ActContent;
    }
    public void replace(PlanMinModel replacedata){
        data.get(indexOf(replacedata.CodAccion)).CodNivelRiesgo=replacedata.CodNivelRiesgo;
        data.get(indexOf(replacedata.CodAccion)).DesPlanAccion=replacedata.DesPlanAccion;
        data.set(indexOf(replacedata.CodAccion),replacedata);
        notifyDataSetChanged();
    }

    public void remove(int index){
        data.remove(index);
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
        TextView tx_empresa=rowView.findViewById(R.id.tx_empresa);


        final String tempimg_perfil=data.get(position).CodSolicitadoPor;
        final String tempNombre = data.get(position).SolicitadoPor;


        final String tempFecha = data.get(position).FechaSolicitud;
        final String tempEditar = data.get(position).Editable;
        final String tempRiesgo = data.get(position).CodNivelRiesgo;

        final String tempTabla_ref = data.get(position).CodTabla;
        final String tempEstado = data.get(position).CodEstadoAccion;
        final String tempDesTarea = data.get(position).DesPlanAccion;
        final String tempEmpresa=data.get(position).Empresa;

        tx_empresa.setText(tempEmpresa);
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
                String edit=data.get(position).Editable;
                GlobalVariables.ObjectEditable=false;
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);//getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_opcionfacilito, null);

                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, false);
                popupWindow.showAtLocation(editar, Gravity.BOTTOM, 0, 0);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button button1=(Button) popupView.findViewById(R.id.btn_editartv);
                Button button2=(Button) popupView.findViewById(R.id.btn_aprobartv);
                Button button3=(Button) popupView.findViewById(R.id.btn_eliminartv);
                RelativeLayout rl1=(RelativeLayout) popupView.findViewById(R.id.rl1);
                CardView cv1=(CardView) popupView.findViewById(R.id.cv1);
                CardView cv2=(CardView) popupView.findViewById(R.id.cv2);
                CardView cv3=(CardView) popupView.findViewById(R.id.cv3);
                rl1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        popupWindow.dismiss();
                    }
                });
                if(edit.equals("1") || edit.equals("3") ){
                    cv1.setVisibility(View.VISIBLE);
                    cv3.setVisibility(View.VISIBLE);
                }
                button1.setText("  Editar plan de acción");
                button3.setText("  Eliminar plan de acción");
                button1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        popupWindow.dismiss();
                        GlobalVariables.ObjectEditable=true;
                        Intent i = new Intent(context, PlanAccionEdit.class);
                        Gson gson = new Gson();
                        i.putExtra("editplan", true);
                        i.putExtra("Plan", gson.toJson(data.get(position)));
                        context.startActivityForResult(i,3);
                    }
                });
                button3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                        alertDialog.setTitle("Desea eliminar plan de acción?")
                                .setMessage(data.get(position).DesPlanAccion)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        popupWindow.dismiss();
                                        ActContent.DeleteObject("PlanAccion/Delete/"+ data.get(position).CodAccion,position+2);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
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
