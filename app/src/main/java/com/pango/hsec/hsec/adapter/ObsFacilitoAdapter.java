package com.pango.hsec.hsec.adapter;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.Facilito.obsfacilitoAprobar;
import com.pango.hsec.hsec.Facilito.opcionfacilito;
import com.pango.hsec.hsec.Facilito.report_obs;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.MainActivity;
import com.pango.hsec.hsec.Observaciones.Galeria_detalle;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsFacilitoMinModel;
import com.pango.hsec.hsec.model.ObsFacilitoModel;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import layout.FragmentObsFacilito;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.pango.hsec.hsec.Utils.Obtenerfecha;

/**
 * Created by jcila on 20/04/2018.
 */

public class ObsFacilitoAdapter extends  ArrayAdapter<ObsFacilitoMinModel> {
//    LayoutInflater layoutInflater;
    View popupView;
    public PopupWindow popupWindow;
    private Context context;
    FragmentObsFacilito ActContent;
    ArrayList<Maestro> ObsFacilito_estado;
    private ArrayList<ObsFacilitoMinModel> data = new ArrayList<ObsFacilitoMinModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public ObsFacilitoAdapter(Context context, ArrayList<ObsFacilitoMinModel> data, FragmentObsFacilito ActContent) {
        super(context, R.layout.public_obsfacilitomin, data);
        this.data = data;
        this.context = context;
        this.ActContent=ActContent;
    }

    public void remove(int index){
        data.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)  {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutInflater layoutInflater;
        ListView list_Obs;
        final View rowView;
        rowView = inflater.inflate(R.layout.public_obsfacilitomin, null, true);
        ImageView img_perfil = rowView.findViewById(R.id.mp_profile);
        ImageView img_det = rowView.findViewById(R.id.mp_imgdet);
        TextView nombre = rowView.findViewById(R.id.mp_nombre);
        TextView fecha = rowView.findViewById(R.id.mp_fecha);
        ImageView riesgo = rowView.findViewById(R.id.img_riesgo);
        TextView tipo = rowView.findViewById(R.id.mp_tipo);
        TextView estado = rowView.findViewById(R.id.mp_estado);
        TextView tx_det = rowView.findViewById(R.id.mp_txdet);
        ImageView editar = rowView.findViewById(R.id.btn_editar);

        final String tempimg_perfil = data.get(position).UrlObs;
        final String tempNombre = data.get(position).Persona;
        final String tempFecha = data.get(position).Fecha;
        final String tempEstado = data.get(position).Estado;
        final String tempTipo = data.get(position).Tipo;
        final String tempObservacion = data.get(position).Observacion;
        final String tempImgDet=data.get(position).UrlPrew;
        final String editable = data.get(position).Editable;
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
                    if(edit.equals("1") || edit.equals("3") ){
                        cv1.setVisibility(View.VISIBLE);
                        cv3.setVisibility(View.VISIBLE);
                    }
                    button1.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            GlobalVariables.flagFacilito=true;
                            Intent intent = new Intent(getContext(),report_obs.class);
                            intent.putExtra("codObs", data.get(position).CodObsFacilito);
                            intent.putExtra("editable",data.get(position).Editable);
                            v.getContext().startActivity(intent);
                            popupWindow.dismiss();
                        }
                    });
                    button3.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                            alertDialog.setTitle("Desea Eliminar Observacion")
                            .setMessage(tempObservacion)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActContent.DeleteObject("ObsFacilito/Delete/"+ data.get(position).CodObsFacilito,position+2);
//                                    final Timer t = new Timer();
//                                    t.schedule(new TimerTask() {
//                                        public void run() {
                                            popupWindow.dismiss();
//                                            t.cancel();
//                                        }
//                                    }, 2000);
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
        nombre.setText(tempNombre);
        fecha.setText(Obtenerfecha(tempFecha));
        estado.setText(ObtenerDet(tempEstado));
        tx_det.setText(tempObservacion);
        if (tempEstado == null) {
            riesgo.setVisibility(View.INVISIBLE);
        } else if (tempEstado.equals("P")) {
            riesgo.setImageResource(R.drawable.ic_alertaroja);
            tipo.setText("Acto");

        } else if (tempEstado.equals("A")) {
            riesgo.setImageResource(R.drawable.ic_alertaverde);
            tipo.setText("Condicion");
        } else if (tempEstado.equals("S")) {
            riesgo.setImageResource(R.drawable.ic_alerta_amarilla);
            tipo.setText("Condicion");
        } else if (tempEstado.equals("O")) {
            riesgo.setImageResource(R.drawable.ic_interrogacion);
            tipo.setText("Condicion");
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


        img_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Galeria_detalle.class);
                //intent.putExtra(ActImagDet.EXTRA_PARAM_ID, a);
                intent.putExtra("post", tempImgDet);
                intent.putExtra("codigo", "G"+data.get(position).CodObsFacilito);
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
                    GlobalVariables.dniUser=data.get(position).UrlObs;
                    ((MainActivity)ActContent.getActivity()).openFichaPersona();
                }
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

}

