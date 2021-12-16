package com.pango.hsec.hsec.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.IngresosMA.ActIngresoMA;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.MainActivity;
import com.pango.hsec.hsec.Observaciones.Galeria_detalle;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.PublicacionModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import layout.FragmentMACuasiAccidente;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MACuasiAccidenteAdapter extends ArrayAdapter<PublicacionModel> {
    private Context context;
    View popupView;
    public PopupWindow popupWindow;
    FragmentMACuasiAccidente fragmentMuro;
    private ArrayList<PublicacionModel> data = new ArrayList<PublicacionModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public MACuasiAccidenteAdapter(Context context, ArrayList<PublicacionModel> data,FragmentMACuasiAccidente fragmentMuro) {
        super(context, R.layout.public_ma_cuasiaccidente, data);
        this.data = data;
        this.context = context;
        this.fragmentMuro=fragmentMuro;
    }
    public void remove(int index){
        data.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.public_ma_cuasiaccidente, null, true);
        ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
        ImageView img_det = rowView.findViewById(R.id.mp_imgdet);
        TextView nombre = rowView.findViewById(R.id.mp_nombre);
        TextView fecha = rowView.findViewById(R.id.mp_fecha);
        // TextView tipo_insp = rowView.findViewById(R.id.mp_tipoinsp);
        ImageView btn_editar=rowView.findViewById(R.id.btn_editar);
        TextView comentario=rowView.findViewById(R.id.tx_comentario);
        TextView tx_empresa=rowView.findViewById(R.id.tx_empresa);
        TextView tx_titulo=rowView.findViewById(R.id.tx_titulo);
        TextView tx_descripcion=rowView.findViewById(R.id.tx_descripcion);

        //media/getAvatar/30642172/Carnet.jpg

        final String tempimg_perfil=data.get(position).UrlObs;

        final String tempNombre = data.get(position).ObsPor;
        final String tempFecha = data.get(position).Fecha;

        final int comentarios=data.get(position).Comentarios;
        //final String tempImgDet="";
        final String tempImgDet=data.get(position).UrlPrew;
        final String tempEmpresa=data.get(position).Empresa;
        tx_empresa.setText(tempEmpresa);
        tx_titulo.setText("Derrame de Hidrocarburos");
        tx_descripcion.setText("derrame de aceite usado");

        String editable = data.get(position).Editable;

        if(editable.equals("0")){
            btn_editar.setVisibility(View.GONE);
        }

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
                button1.setText("  Editar MA Cuasi Accidente");
                button3.setText("  Eliminar MA Cuasi Accidente");
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
                        Intent intent = new Intent(getContext(), ActIngresoMA.class);
                        intent.putExtra("codObs", data.get(position).Codigo);
                        intent.putExtra("posTab", 0);
                        v.getContext().startActivity(intent);
                        popupWindow.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
/*
                        Intent intent = new Intent(getContext(),obsfacilitoAprobar.class);
                        intent.putExtra("codObs", data.get(position).Codigo);
                        v.getContext().startActivity(intent);
                        popupWindow.dismiss();*/
                    }
                });
//                    }
                button3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                        alertDialog.setTitle("Desea Eliminar Cuasi Accidente?")
                                .setMessage(data.get(position).Obs)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        popupWindow.dismiss();
                                        // modificar cuando este el servicio
                                        //fragmentMuro.DeleteObject("Inspecciones/Delete/"+ data.get(position).Codigo,position+3);
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

        comentario.setText(comentarios+" comentarios");

        comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // revisar cuando haya servicio
                /*
                Toast.makeText(v.getContext(),"Comentarios",Toast.LENGTH_SHORT).show();
                GlobalVariables.istabs=true;

                Intent intent = new Intent(v.getContext(), ActInspeccionDet.class);
                intent.putExtra("codObs",data.get(position).Codigo);
                intent.putExtra("posTab",3);
                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                v.getContext().startActivity(intent);
*/
            }
        });

        if(tempimg_perfil==null){
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
            String Url_prev= Utils.ChangeUrl(GlobalVariables.Url_base + "media/getImagepreview/"+tempImgDet+"/loco.jpg");

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
                ((MainActivity) fragmentMuro.getActivity()).openFichaPersona();
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