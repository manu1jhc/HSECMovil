package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pango.hsec.hsec.Facilito.ObsFHistorialAtencionDet;
import com.pango.hsec.hsec.Facilito.addAtencionFHistorial;
import com.pango.hsec.hsec.Facilito.obsFacilitoDet;
import com.pango.hsec.hsec.Ficha.AddRegistroAvance;
import com.pango.hsec.hsec.Ficha.RegistroAtencion;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsFHistorialModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by jcila on 10/05/2018.
 */

public class ObsFHistorialAdapter extends RecyclerView.Adapter<ObsFHistorialAdapter.ViewHolder> {
    private Activity activity;
    private List<ObsFHistorialModel> items;
    View popupView;
    PopupWindow popupWindow;
    obsFacilitoDet obsFacilito;
    private Context context;
    private Button btn_editarhistorial,btn_eliminarhistorial;
    private ArrayList<ObsFHistorialModel> data = new ArrayList<ObsFHistorialModel>();
    ArrayList<Maestro> ObsFacilito_estado;
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    public ObsFHistorialAdapter(obsFacilitoDet activity, List<ObsFHistorialModel> items) {
        this.activity = activity;
        this.items = items;
        this.obsFacilito=activity;
    }
    public void add(ObsFHistorialModel newdata){
        items.add(newdata);
        notifyDataSetChanged();
    }
    public void replace(ObsFHistorialModel replacedata,int index){
        items.set(index,replacedata);
        notifyDataSetChanged();
    }
    public void remove(int index){
        items.remove(index);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.public_historial_atencion, parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ObsFHistorialAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.mp_nombre.setText(items.get(position).Persona);
        viewHolder.mp_fecha.setText(Obtenerfecha(items.get(position).Fecha));
        viewHolder.mp_estado.setText(ObtenerDet(items.get(position).Estado));
        viewHolder.mp_txcoment.setText(items.get(position).Comentario);
        viewHolder.idposition=position;
        viewHolder.tempimg_perfil=items.get(position).UrlObs;

        if (viewHolder.tempimg_perfil == null) {
            viewHolder.mp_profile.setImageResource(R.drawable.ic_loginusuario);
        }else {
            String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + viewHolder.tempimg_perfil + "/Carnet.jpg";
            Glide.with(activity)
                    .load(Url_img)
                    .override(50, 50)
                    .transform(new CircleTransform(activity)) // applying the image transformer
                    .into(viewHolder.mp_profile);
        }
        if(items.get(position).CodObsFacilito.equals("0")) viewHolder.btn_editar.setVisibility(View.GONE);
        viewHolder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalVariables.ObjectEditable=true;
                LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);//getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_opcionhistorial, null);

                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, false);
                popupWindow.showAtLocation(viewHolder.btn_editar, Gravity.BOTTOM, 0, 0);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btn_editarhistorial=popupView.findViewById(R.id.btn_editarhistorial);
                btn_eliminarhistorial=popupView.findViewById(R.id.btn_eliminarhistorial);
                RelativeLayout rl1=(RelativeLayout) popupView.findViewById(R.id.rl1);
                rl1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        popupWindow.dismiss();
                    }
                });
                btn_editarhistorial.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        GlobalVariables.flaghistorial=false;
                        String correlativo=items.get(position).Correlativo;
                        Intent intent = new Intent(activity,addAtencionFHistorial.class);
                        //intent.putExtra("correlativo",correlativo);
                        intent.putExtra("index",position);
                        activity.startActivityForResult(intent,2);
                        popupWindow.dismiss();
                        //v.getContext().startActivity(intent);
                    }
                });
                btn_eliminarhistorial.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity,android.R.style.Theme_Material_Dialog_Alert);
                        alertDialog.setTitle("Desea Eliminar Registro de Atencion?")
                                .setMessage(items.get(position).Comentario)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        obsFacilito.DeleteObject("ObsFacilito/DeleteHistorial/"+ items.get(position).Correlativo,position+4);
                                        popupWindow.dismiss();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                        popupWindow.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
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
    public String ObtenerDet(String dataestado){
        String estado="";
        ObsFacilito_estado= new ArrayList<>();
        ObsFacilito_estado.addAll(GlobalVariables.ObsFacilito_estado);
        for(int i=0;i<ObsFacilito_estado.size();i++){
            if(ObsFacilito_estado.get(i).CodTipo.equals(dataestado)){
                estado= ObsFacilito_estado.get(i).Descripcion;
            }
        }
        return estado;
    }
    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{//
        protected TextView mp_nombre,mp_fecha,mp_estado,mp_txcoment;
        protected TextView tx_porcentaje;
        ImageView mp_profile,btn_editar;
        String tempimg_perfil;
        public int idposition,tempComent;
        public ViewHolder(View view) {
            super(view);
            mp_nombre = (TextView)  view.findViewById(R.id.mp_nombre);
            mp_fecha = (TextView)  view.findViewById(R.id.mp_fecha);
            mp_estado = (TextView)  view.findViewById(R.id.mp_estado);
            mp_txcoment = (TextView)  view.findViewById(R.id.mp_txcoment);
            mp_profile = (ImageView)  view.findViewById(R.id.mp_profile);
            btn_editar = (ImageView)  view.findViewById(R.id.btn_editar);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

            Gson gson = new Gson();
            String persona=items.get(idposition).Persona;
            String fecha=items.get(idposition).Fecha;
            String fechafin=items.get(idposition).FechaFin;
            String estado=items.get(idposition).Estado;
            String comentario=items.get(idposition).Comentario;
            String correlativo=items.get(idposition).Correlativo;
            Intent intent = new Intent(activity, ObsFHistorialAtencionDet.class);
            intent.putExtra("correlativo",correlativo);
            intent.putExtra("Persona",persona);
            intent.putExtra("Fecha",fecha);
            intent.putExtra("Estado",estado);
            intent.putExtra("Comentario",comentario);
            intent.putExtra("fechafin",fechafin);
            intent.putExtra("Correlativo",items.get(idposition).Correlativo);
            activity.startActivityForResult(intent, 2);
        }
    }
}
