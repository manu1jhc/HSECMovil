package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pango.hsec.hsec.Ficha.AddRegistroAvance;
import com.pango.hsec.hsec.Ficha.PlanAccionDet;
import com.pango.hsec.hsec.Ficha.RegistroAtencion;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.AccionMejoraMinModel;
import com.pango.hsec.hsec.model.AccionMejoraModel;
import com.pango.hsec.hsec.observacion_edit;
import com.pango.hsec.hsec.utilitario.CircleTransform;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import layout.FragmentObservaciones;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AccionMejoraAdapter extends RecyclerView.Adapter<AccionMejoraAdapter.ViewHolder> {
    private Activity activity;
    private List<AccionMejoraMinModel> items;
    private String Responsables,CodResponsables;
    public PlanAccionDet PlanDet;

    View popupView;
    public PopupWindow popupWindow;

    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    public AccionMejoraAdapter(Activity activity, List<AccionMejoraMinModel> items,String Responsables,String CodResponsables,PlanAccionDet PlanDet) {
        this.activity = activity;
        this.items = items;
        this.Responsables=Responsables;
        this.CodResponsables=CodResponsables;
        this.PlanDet=PlanDet;
    }
    public void add(AccionMejoraMinModel newdata){
        items.add(newdata);
        notifyDataSetChanged();
    }
    public void replace(AccionMejoraMinModel replacedata){
        int position=0;
        for(; position< items.size();position++)
            if(items.get(position).Correlativo.equals(replacedata.Correlativo))break;

        if(replacedata.Correlativo.equals("E")){
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,items.size());
        }
        else{
            AccionMejoraMinModel temp= items.get(position) ;// .set(position,replacedata);
            temp.Descripcion= replacedata.Descripcion;
            temp.Fecha=replacedata.Fecha;
            temp.PorcentajeAvance=replacedata.PorcentajeAvance;
            temp.Editable="1";
            notifyDataSetChanged();
        }
    }

    public void remove(int index){
        items.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.public_acc_mejora, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.tx_descripcion.setText(items.get(position).Descripcion);
        viewHolder.tx_porcentaje.setText(items.get(position).PorcentajeAvance+"%");
        viewHolder.btn_editar_m.setVisibility(items.get(position).Editable.equals("1")?View.VISIBLE:View.GONE);//ver
        viewHolder.reg_nombre.setText(items.get(position).Persona);
        viewHolder.reg_fecha.setText(Utils.Obtenerfecha(items.get(position).Fecha));


        if(Integer.parseInt(items.get(position).PorcentajeAvance)>=50){
            viewHolder.pb_porcentaje.setProgress(Integer.parseInt(items.get(position).PorcentajeAvance));
            viewHolder.pb_porcentaje.setSecondaryProgress(0);
        }else{
            viewHolder.pb_porcentaje.setProgress(0);
            viewHolder.pb_porcentaje.setSecondaryProgress(Integer.parseInt(items.get(position).PorcentajeAvance));
        }


        //viewHolder.pb_porcentaje.setProgress(Integer.parseInt(items.get(position).PorcentajeAvance));
        //viewHolder.pb_porcentaje.setSecondaryProgress(75);
        //secondaryProgress







        if (items.get(position).UrlObs == null) {
            viewHolder.mp_profile.setImageResource(R.drawable.ic_usuario);
        }else {
            String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + items.get(position).UrlObs.replace("*","").replace(".","") + "/Carnet.jpg";
            //String Url_img="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/getAvatar/42651514/Carnet.jpg";
            Glide.with(activity)
                    .load(Url_img)
                    .override(50, 50)
                    .transform(new CircleTransform(activity)) // applying the image transformer
                    .into(viewHolder.mp_profile);
        }

        viewHolder.idposition=position;
        /*
        viewHolder.btn_editar_m.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         Intent i = new Intent(activity, AddRegistroAvance.class);
                                                         Gson gson = new Gson();
                                                         items.get(position).Editable=position+"";
                                                         i.putExtra("AccionMejora",  gson.toJson(items.get(position)));
                                                         i.putExtra("Edit", true);
                                                         i.putExtra("CodResponsable",CodResponsables);
                                                         i.putExtra("Responsable",Responsables);
                                                         activity.startActivityForResult(i,2);
                                                     }
                                                 }
        );

        */
/////////
        viewHolder.btn_editar_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit=items.get(position).Editable;
                GlobalVariables.ObjectEditable=true;
                LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);//getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_opcionfacilito, null);

                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, false);
                popupWindow.showAtLocation(viewHolder.btn_editar_m, Gravity.BOTTOM, 0, 0);
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

                button1.setText("  Editar Plan de acción");
                button3.setText("  Eliminar Plan de acción");
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
                        Intent i = new Intent(activity, AddRegistroAvance.class);
                        Gson gson = new Gson();
                        //items.get(position).Editable=position+"";
                        i.putExtra("AccionMejora",  gson.toJson(items.get(position)));
                        i.putExtra("Edit", true);
                        i.putExtra("CodResponsable",CodResponsables);
                        i.putExtra("Responsable",Responsables);
                        activity.startActivityForResult(i,2);
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


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity,android.R.style.Theme_Material_Dialog_Alert);
                        alertDialog.setTitle("Desea Eliminar Plan de acción?")
                                .setMessage("")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        popupWindow.dismiss();
                                        PlanDet.DeleteObject("AccionMejora/Delete/"+ items.get(position).Correlativo,position+3);
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

 ////////


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{//
        protected TextView tx_descripcion,reg_nombre,reg_fecha;
        protected TextView tx_porcentaje;
        ImageView btn_editar_m,mp_profile;
        ProgressBar pb_porcentaje;
        public int idposition;
        public ViewHolder(View view) {
            super(view);
            tx_descripcion = (TextView)  view.findViewById(R.id.tx_descripcion);
            tx_porcentaje = (TextView)  view.findViewById(R.id.tx_porcentaje);
            btn_editar_m = (ImageView)  view.findViewById(R.id.btn_editar_m);
            mp_profile=view.findViewById(R.id.mp_profile);
            reg_nombre=view.findViewById(R.id.reg_nombre);
            reg_fecha=view.findViewById(R.id.reg_fecha);
            pb_porcentaje=view.findViewById(R.id.pb_porcentaje);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {


            Gson gson = new Gson();
            Intent intent=new Intent(activity, RegistroAtencion.class);
            intent.putExtra("Correlativo",items.get(idposition).Correlativo);
            activity.startActivity(intent);
        }
    }
}
