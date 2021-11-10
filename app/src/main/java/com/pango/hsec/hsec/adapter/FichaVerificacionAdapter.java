package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
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
import com.pango.hsec.hsec.Ficha.BusqEstadistica;
import com.pango.hsec.hsec.Ficha.FichaPersona;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Observaciones.Galeria_detalle;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Verificaciones.ActVerificacionDet;
import com.pango.hsec.hsec.Verificaciones.AddVerificacion;
import com.pango.hsec.hsec.model.PublicacionModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FichaVerificacionAdapter extends ArrayAdapter<PublicacionModel> {
    private Context context;
    private BusqEstadistica ActContent;
    View popupView;
    public PopupWindow popupWindow;
    private ArrayList<PublicacionModel> data = new ArrayList<PublicacionModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public FichaVerificacionAdapter(BusqEstadistica context, ArrayList<PublicacionModel> data) {
        super(context, R.layout.publicalist, data);
        this.data = data;
        this.context = context;
        this.ActContent=context;
    }

    public void remove(int index){
        data.remove(index);
        notifyDataSetChanged();
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height= dm.heightPixels;
        if(width>height)width=height;

        View rowView = inflater.inflate(R.layout.public_verificacion, null, true);

        ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
        ImageView img_det = rowView.findViewById(R.id.mp_imgdet);
        TextView nombre = rowView.findViewById(R.id.mp_nombre);
        TextView fecha = rowView.findViewById(R.id.mp_fecha);
        ImageView riesgo=rowView.findViewById(R.id.img_riesgo);
        //CardView riesgo = rowView.findViewById(R.id.mp_nriesgo);
        TextView tipo = rowView.findViewById(R.id.mp_tipo);
        TextView area = rowView.findViewById(R.id.mp_area);
        TextView comentario=rowView.findViewById(R.id.tx_comentario);
        TextView empresa = rowView.findViewById(R.id.tx_empresa);
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
        final String Empresa = data.get(position).Empresa;
        empresa.setText(Empresa);
        //if(editable.equals("1")&&(tempTipo.equals("TO01") || tempTipo.equals("TO02") || tempTipo.equals("TO03") || tempTipo.equals("TO04") )){
            editar.setVisibility(View.VISIBLE);
        //}

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit=data.get(position).Editable;
                GlobalVariables.ObjectEditable=true;
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

                button1.setText("  Editar observación");
                button3.setText("  Eliminar observación");
                if(edit.equals("1")){
                    cv1.setVisibility(View.VISIBLE);
                    cv3.setVisibility(View.VISIBLE);
                }
                else if(edit.equals("2")){
                    cv2.setVisibility(View.VISIBLE);
                }
                else if(edit.equals("3")){
                    cv1.setVisibility(View.VISIBLE);
                    // cv2.setVisibility(View.VISIBLE);
                    cv3.setVisibility(View.VISIBLE);
                }
                button1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        popupWindow.dismiss();
                        GlobalVariables.ObjectEditable=true;
                        Intent intent = new Intent(getContext(), AddVerificacion.class);
                        intent.putExtra("codObs", data.get(position).Codigo);
//                        intent.putExtra("tipoObs", data.get(position).Tipo);
//                        intent.putExtra("posTab", 0);
                        v.getContext().startActivity(intent);
                    }
                });
                button2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                       /* popupWindow.dismiss();
                        Intent intent = new Intent(getContext(),obsfacilitoAprobar.class);
                        intent.putExtra("codObs", data.get(position).Codigo);
                        v.getContext().startActivity(intent);*/
                    }
                });
//                    }
                button3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                        alertDialog.setTitle("Desea eliminar verificacion?")
                                .setMessage(tempDetalle)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        popupWindow.dismiss();
                                        ActContent.DeleteObject("Verificacion/Delete/"+ data.get(position).Codigo,position+2);
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

        String tipo_ejm=GlobalVariables.getDescripcion(GlobalVariables.Tipo_Ver,tempTipo);
        String area_ejm=GlobalVariables.getDescripcion(GlobalVariables.Area_obs,tempArea);

        tipo.setText(" / "+GlobalVariables.getDescripcion(GlobalVariables.Tipo_Ver, tempTipo)+" / "+GlobalVariables.getDescripcion(GlobalVariables.Area_obs, tempArea));
        //area.setText(GlobalVariables.getDescripcion(GlobalVariables.Area_obs,tempArea));
        tx_det.setText(tempDetalle);

        comentario.setText(comentarios+" comentarios");

        comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tipoObs=data.get(position).Tipo;

                GlobalVariables.istabs=true;

                Intent intent = new Intent(v.getContext(), ActVerificacionDet.class);
                intent.putExtra("codVer",data.get(position).Codigo);
                intent.putExtra("posTab",4);
                intent.putExtra("tipoVer",tipoObs);

                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                v.getContext().startActivity(intent);

            }
        });

        // img_perfil.setImageResource(R.drawable.fotocarnet);

        if (tempimg_perfil == null) {
            img_perfil.setImageResource(R.drawable.ic_usuario);
        }else {
            String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + tempimg_perfil.replace("*","").replace(".","") + "/Carnet.jpg";
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
                    .override(width, width)
                    .into(img_det);
        }

        img_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Galeria_detalle.class);
                //intent.putExtra(ActImagDet.EXTRA_PARAM_ID, a);
                intent.putExtra("post", tempImgDet);
                intent.putExtra("codigo", "G"+data.get(position).Codigo);
                v.getContext().startActivity(intent);
                //GlobalVariables.desdeBusqueda=true;
            }
        });
        img_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.dniUser=data.get(position).UrlObs;
                if(GlobalVariables.dniUser!=null)
                {
                    Intent intent=new Intent(context, FichaPersona.class);
                    context.startActivity(intent);
                }
            }
        });


        return rowView;
    }


    public String ObtenerTipo(String tempTipo) {
        String tipo="";

        for(int i=0;i<GlobalVariables.Tipo_obs2.size();i++){
            if(tempTipo.equals(GlobalVariables.Tipo_obs2.get(i).getCodTipo())){
                tipo=GlobalVariables.Tipo_obs2.get(i).getDescripcion();
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