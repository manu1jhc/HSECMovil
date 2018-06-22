package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.Facilito.obsfacilitoAprobar;
import com.pango.hsec.hsec.Facilito.report_obs;
import com.pango.hsec.hsec.Ficha.FichaPersona;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Ingresos.Inspecciones.AddInspeccion;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.MainActivity;
import com.pango.hsec.hsec.Noticias.ActNoticiaDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.Observaciones.Galeria_detalle;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.PublicacionModel;
import com.pango.hsec.hsec.observacion_edit;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import layout.FragmentFichaPersonal;
import layout.FragmentMuro;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Andre on 12/02/2018.
 */

public class MuroAdapter extends ArrayAdapter<PublicacionModel>  {
    private Context context;

    private ArrayList<PublicacionModel> data = new ArrayList<PublicacionModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    View popupView;
    public PopupWindow popupWindow;
    FragmentMuro fragmentMuro;
    public MuroAdapter(Context context, ArrayList<PublicacionModel> data,FragmentMuro fragmentMuro) {
        super(context,  R.layout.publicalist, data);
        this.data = data;
        this.context = context;
        this.fragmentMuro=fragmentMuro;
    }
    public void add(PublicacionModel newdata){
        data.add(newdata);
    }

    public void remove(int index){
        data.remove(index);
        notifyDataSetChanged();
        //notifyItemRemoved(index);
        //notifyItemRangeChanged(index,data.size());
    }
    @Override
    public int getViewTypeCount() {
        return 4;
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
            case "OBF":
                tipo=3;
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

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height= dm.heightPixels;
        if(width>height)width=height;

        if (positem == 0) { //Observacion
            rowView = inflater.inflate(R.layout.publicalist, null, true);
            ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
            ImageView img_det = rowView.findViewById(R.id.mp_imgdet);
            TextView nombre = rowView.findViewById(R.id.mp_nombre);
            TextView fecha = rowView.findViewById(R.id.mp_fecha);
            ImageView riesgo = rowView.findViewById(R.id.img_riesgo);
            TextView tipo = rowView.findViewById(R.id.mp_tipo);
            //TextView area = rowView.findViewById(R.id.mp_area);//no va
            TextView comentario = rowView.findViewById(R.id.tx_comentario);
            TextView tx_det = rowView.findViewById(R.id.mp_txdet);
            ImageView editar = rowView.findViewById(R.id.btn_editar);
            TextView tx_empresa=rowView.findViewById(R.id.tx_empresa);

            final String tempimg_perfil = data.get(position).UrlObs;
            final String tempNombre = data.get(position).ObsPor;
            final String tempFecha = data.get(position).Fecha;
            final String tempRiesgo = data.get(position).NivelR;
            final String tempTipo = data.get(position).Tipo;
            final String tempArea = data.get(position).Area;
            final String tempDetalle = data.get(position).Obs;
            final int comentarios = data.get(position).Comentarios;
            final String tempImgDet = data.get(position).UrlPrew;
            final String editable = data.get(position).Editable;
            final String tempEmpresa=data.get(positem).Empresa;

            if(editable=="0"||(!tempTipo.equals("TO01")&& !tempTipo.equals("TO02"))){
                editar.setVisibility(View.GONE);
            }
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
                        cv1.setVisibility(View.VISIBLE);
                        cv3.setVisibility(View.VISIBLE);
                    }
                    else if(edit.equals("3")){
                        cv1.setVisibility(View.VISIBLE);
                        cv2.setVisibility(View.VISIBLE);
                        cv3.setVisibility(View.VISIBLE);
                    }
                    button1.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            popupWindow.dismiss();
                            GlobalVariables.ObjectEditable=true;
                            Intent intent = new Intent(getContext(),observacion_edit.class);
                            intent.putExtra("codObs", data.get(position).Codigo);
                            intent.putExtra("tipoObs", data.get(position).Tipo);
                            intent.putExtra("posTab", 0);
                            v.getContext().startActivity(intent);
                        }
                    });
//                    button2.setOnClickListener(new View.OnClickListener(){
//                        @Override
//                        public void onClick(View v){
//                            popupWindow.dismiss();
//                            Intent intent = new Intent(getContext(),obsfacilitoAprobar.class);
//                            intent.putExtra("codObs", data.get(position).Codigo);
//                            v.getContext().startActivity(intent);
//                        }
//                    });
//                  }
                    button3.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                            alertDialog.setTitle("Desea eliminar observacion?")
                                    .setMessage(tempDetalle)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            popupWindow.dismiss();
                                            fragmentMuro.DeleteObject("Observaciones/Delete/"+ data.get(position).Codigo,position+3);
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
            tx_empresa.setText(tempEmpresa);
            nombre.setText(tempNombre);
            fecha.setText(Obtenerfecha(tempFecha));

            if (tempRiesgo == null) {
                riesgo.setVisibility(View.INVISIBLE);
            } else if (tempRiesgo.equals("BA")) {
                riesgo.setImageResource(R.drawable.ic_alertaverde);

            } else if (tempRiesgo.equals("ME")) {
                riesgo.setImageResource(R.drawable.ic_alerta_amarilla);

            } else {
                riesgo.setImageResource(R.drawable.ic_alertaroja);

            }
            tipo.setText(" / "+GlobalVariables.getDescripcion(GlobalVariables.Tipo_obs2, tempTipo)+" / "+GlobalVariables.getDescripcion(GlobalVariables.Area_obs, tempArea));
            //area.setText(GlobalVariables.getDescripcion(GlobalVariables.Area_obs, tempArea));
            tx_det.setText(tempDetalle);

            comentario.setText(comentarios + " comentarios");
            comentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tipoObs=data.get(position).Tipo;
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

            if (tempImgDet == null) {
                img_det.setVisibility(View.GONE);

            } else {
                String Url_prev = GlobalVariables.Url_base + "media/getImagepreview/" + tempImgDet + "/Preview.jpg";

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
                    ((MainActivity)fragmentMuro.getActivity()).openFichaPersona();
                   /* //GlobalVariables.desdeBusqueda=true;
                    GlobalVariables.barTitulo=false;
                    GlobalVariables.dniUser=tempimg_perfil;
                    Intent intent=new Intent(context,FichaPersona.class);
                    //intent.putExtra("codUsuario",tempimg_perfil);
                    context.startActivity(intent);


                    FragmentFichaPersonal nextFrag= new FragmentFichaPersonal();
                    fragmentMuro.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, nextFrag,"B")
                            .addToBackStack(null)
                            .commit();
                            */
                }
            });

            return rowView;
        }else if(positem == 1) {  // inspecciones
            rowView = inflater.inflate(R.layout.public_inspeccion, null, true);
            ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
            ImageView img_det = rowView.findViewById(R.id.mp_imgdet);
            TextView nombre = rowView.findViewById(R.id.mp_nombre);
            TextView fecha = rowView.findViewById(R.id.mp_fecha);
            ImageView riesgo1=rowView.findViewById(R.id.img_riesgo1);
            ImageView riesgo2=rowView.findViewById(R.id.img_riesgo2);
            ImageView riesgo3=rowView.findViewById(R.id.img_riesgo3);
            TextView tipo_insp = rowView.findViewById(R.id.mp_tipoinsp);
            ImageView btn_editar=rowView.findViewById(R.id.btn_editar);

            TextView comentario=rowView.findViewById(R.id.tx_comentario);
            TextView tx_det1 = rowView.findViewById(R.id.mp_txdet);
            TextView tx_det2 = rowView.findViewById(R.id.mp_txdet2);
            TextView tx_det3 = rowView.findViewById(R.id.mp_txdet3);
            TextView txdetcompleta=rowView.findViewById(R.id.txdetcompleta);
            TextView tx_empresa= rowView.findViewById(R.id.tx_empresa);

            ConstraintLayout const1=rowView.findViewById(R.id.constrain1);
            //media/getAvatar/30642172/Carnet.jpg

            final String tempimg_perfil=data.get(position).UrlObs;

            final String tempNombre = data.get(position).ObsPor;
            final String tempFecha = data.get(position).Fecha;
            final String tempTipoInsp = data.get(position).Tipo;

            final int comentarios=data.get(position).Comentarios;
            //final String tempImgDet="";
            final String tempImgDet=data.get(position).UrlPrew;

            final String tempEmpresa=data.get(positem).Empresa;
            tx_empresa.setText(tempEmpresa);

            String[] tempRiesgo = new String[0];
            String[] tempDetalle = new String[0];
            final String editable = data.get(position).Editable;

            if(data.get(position).NivelR!=null){
                tempRiesgo = data.get(position).NivelR.split(";");
                tempDetalle = data.get(position).Obs.split(";");
            }

            if(editable.equals("0")){
                btn_editar.setVisibility(View.GONE);
            }
////////////////////////////////////////////////////////////////////////////editar inspecciones
            btn_editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String edit=data.get(position).Editable;
                    GlobalVariables.ObjectEditable=true;
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);//getSystemService(LAYOUT_INFLATER_SERVICE);
                    popupView = layoutInflater.inflate(R.layout.popup_opcionfacilito, null);

                    popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, false);
                    popupWindow.showAtLocation(btn_editar, Gravity.BOTTOM, 0, 0);
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
                    button1.setText("  Editar inspección");
                    button3.setText("  Eliminar inspección");
                    if(edit.equals("1")){
                        cv1.setVisibility(View.VISIBLE);
                        cv3.setVisibility(View.VISIBLE);
                    }
                    else if(edit.equals("2")){
                        cv2.setVisibility(View.VISIBLE);
                    }
                    else if(edit.equals("3")){
                        cv1.setVisibility(View.VISIBLE);
                        cv2.setVisibility(View.VISIBLE);
                        cv3.setVisibility(View.VISIBLE);
                    }
                    button1.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){

                            GlobalVariables.ObjectEditable=true;
                            Intent intent = new Intent(getContext(),AddInspeccion.class);
                            intent.putExtra("codObs", data.get(position).Codigo);
                            intent.putExtra("posTab", 0);
                            v.getContext().startActivity(intent);
                            popupWindow.dismiss();
                        }
                    });
                    button2.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){

                            Intent intent = new Intent(getContext(),obsfacilitoAprobar.class);
                            intent.putExtra("codObs", data.get(position).Codigo);
                            v.getContext().startActivity(intent);
                            popupWindow.dismiss();
                        }
                    });
//                    }
                    button3.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                            alertDialog.setTitle("Desea eliminar inspección?")
                                    .setMessage(data.get(position).Obs)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            popupWindow.dismiss();
                                            fragmentMuro.DeleteObject("Inspecciones/Delete/"+ data.get(position).Codigo,position+3);
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
                String Url_prev=Utils.ChangeUrl(GlobalVariables.Url_base + "media/getImagepreview/"+tempImgDet+"/loco.jpg");
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
                        riesgo1.setImageResource(R.drawable.ic_alertaverde);
                        break;
                    case "ME":
                        riesgo1.setImageResource(R.drawable.ic_alerta_amarilla);
                        break;
                    case "AL":
                        riesgo1.setImageResource(R.drawable.ic_alertaroja);
                        break;

                }
            }else if(tempRiesgo.length==2){
                riesgo3.setVisibility(View.GONE);
                tx_det1.setText(tempDetalle[0]);
                tx_det2.setText(tempDetalle[1]);
                //const1.getLayoutParams().height=50;

                switch (tempRiesgo[0]){
                    case "BA":
                        riesgo1.setImageResource(R.drawable.ic_alertaverde);
                        break;
                    case "ME":
                        riesgo1.setImageResource(R.drawable.ic_alerta_amarilla);
                        break;
                    case "AL":
                        riesgo1.setImageResource(R.drawable.ic_alertaroja);
                        break;

                }

                switch (tempRiesgo[1]){
                    case "BA":
                        riesgo2.setImageResource(R.drawable.ic_alertaverde);
                        break;
                    case "ME":
                        riesgo2.setImageResource(R.drawable.ic_alerta_amarilla);
                        break;
                    case "AL":
                        riesgo2.setImageResource(R.drawable.ic_alertaroja);
                        break;

                }
            }else if(tempRiesgo.length==3){
                tx_det1.setText(tempDetalle[0]);
                tx_det2.setText(tempDetalle[1]);
                tx_det3.setText(tempDetalle[2]);
                switch (tempRiesgo[0]){
                    case "BA":
                        riesgo1.setImageResource(R.drawable.ic_alertaverde);
                        break;
                    case "ME":
                        riesgo1.setImageResource(R.drawable.ic_alerta_amarilla);
                        break;
                    case "AL":
                        riesgo1.setImageResource(R.drawable.ic_alertaroja);
                        break;

                }

                switch (tempRiesgo[1]){
                    case "BA":
                        riesgo2.setImageResource(R.drawable.ic_alertaverde);
                        break;
                    case "ME":
                        riesgo2.setImageResource(R.drawable.ic_alerta_amarilla);
                        break;
                    case "AL":
                        riesgo2.setImageResource(R.drawable.ic_alertaroja);
                        break;
                }
                switch (tempRiesgo[2]) {
                    case "BA":
                        riesgo3.setImageResource(R.drawable.ic_alertaverde);
                        break;
                    case "ME":
                        riesgo3.setImageResource(R.drawable.ic_alerta_amarilla);
                        break;
                    case "AL":
                        riesgo3.setImageResource(R.drawable.ic_alertaroja);
                        break;
                }
            }

            img_perfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GlobalVariables.dniUser=data.get(position).UrlObs;
                    ((MainActivity)fragmentMuro.getActivity()).openFichaPersona();
                }
            });


            return rowView;
        }else if(positem == 2) { // Noticias
            rowView = inflater.inflate(R.layout.public_noticias, null, true);
            ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
            ImageView img_preview = rowView.findViewById(R.id.img_preview);
            TextView nombre = rowView.findViewById(R.id.mp_nombre);
            TextView fecha = rowView.findViewById(R.id.mp_fecha);
            TextView titulo = rowView.findViewById(R.id.tx_titulo);
            TextView descripcion = rowView.findViewById(R.id.tx_descripcion);
            TextView comentario=rowView.findViewById(R.id.tx_comentario);
            TextView tx_empresa=rowView.findViewById(R.id.tx_empresa);

            final String tempimg_perfil=data.get(position).UrlObs;
            final String tempNombre = data.get(position).ObsPor;
            final String tempFecha = data.get(position).Fecha;

            final String tempTitulo = data.get(position).Area;
            final String tempdescripcion = data.get(position).Obs;
            final String tempImgDet=data.get(position).UrlPrew;
            final String tempEmpresa=data.get(positem).Empresa;
            tx_empresa.setText(tempEmpresa);

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
                img_preview.setVisibility(View.GONE);

            }else{
                img_preview.setVisibility(View.VISIBLE);

                String Url_prev=GlobalVariables.Url_base +"media/getImagepreview/"+Utils.ChangeUrl(tempImgDet)+"/Image.jpg";

                //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
                Glide.with(context)
                        .load(Url_prev)
                        .override(width, width)
                        .fitCenter()
                        .into(img_preview);
            }

            img_preview.setOnClickListener(new View.OnClickListener() {
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
                    ((MainActivity)fragmentMuro.getActivity()).openFichaPersona();
                }
            });
            return rowView;

        }else if(positem == 3){//facilito

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
            TextView tx_empresa=rowView.findViewById(R.id.tx_empresa);

            final String tempimg_perfil = data.get(position).UrlObs;
            final String tempNombre = data.get(position).ObsPor;
            final String tempFecha = data.get(position).Fecha;
            //final String tempUbicacion = data.get(position).UbicacionExacta;
            final String tempTipo = data.get(position).Tipo;
            final String tempObservacion = data.get(position).Obs;
            //final String tempAccion = data.get(position).Accion;
            final String tempImgDet=data.get(position).UrlPrew;
            final String editable = data.get(position).Editable;

            final String tempEmpresa=data.get(positem).Empresa;
            tx_empresa.setText(tempEmpresa);

            if(editable.equals("0")){
                editar.setVisibility(View.GONE);
            }

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

                    button1.setText("  Editar reporte facilito");
                    button3.setText("  Eliminar reporte facilito");
                    if(edit.equals("1")){
                        cv1.setVisibility(View.VISIBLE);
                        cv3.setVisibility(View.VISIBLE);
                    }
                    else if(edit.equals("2")){
                        cv2.setVisibility(View.VISIBLE);
                    }
                    else if(edit.equals("3")){
                        cv1.setVisibility(View.VISIBLE);
                        cv2.setVisibility(View.VISIBLE);
                        cv3.setVisibility(View.VISIBLE);
                    }
                    button1.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            popupWindow.dismiss();
                            GlobalVariables.ObjectEditable=true;
                            Intent intent = new Intent(getContext(),report_obs.class);
                            intent.putExtra("codObs", data.get(position).Codigo);
                            v.getContext().startActivity(intent);
                        }
                    });
                    button2.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            popupWindow.dismiss();
                            Intent intent = new Intent(getContext(),obsfacilitoAprobar.class);
                            intent.putExtra("codObs", data.get(position).Codigo);
                            v.getContext().startActivity(intent);
                        }
                    });
//                    }
                    button3.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                            alertDialog.setTitle("Desea eliminar reporte facilito?")
                                    .setMessage(tempObservacion)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            popupWindow.dismiss();
                                            fragmentMuro.DeleteObject("ObsFacilito/Delete/"+ data.get(position).Codigo,position+3);
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
            tipo.setText(" / "+GlobalVariables.Obtener_Tipo(tempTipo));


            //area.setText(tempUbicacion);
//        accion.setText(tempAccion);
            tx_det.setText(tempObservacion);

            /*
            if (tempTipo == null) {
                riesgo.setVisibility(View.INVISIBLE);
            } else if (tempTipo.equals("A")) {
                riesgo.setImageResource(R.drawable.ic_alertaroja);

            } else if (tempTipo.equals("C")) {
                riesgo.setImageResource(R.drawable.ic_alerta_amarilla);
            }
*/
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
                    ((MainActivity)fragmentMuro.getActivity()).openFichaPersona();
                }
            });

            return rowView;
        }else
            {
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
