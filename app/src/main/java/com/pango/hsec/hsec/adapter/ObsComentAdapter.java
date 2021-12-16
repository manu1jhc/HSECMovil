package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;

import java.util.List;

import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.SubDetalleModel;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ObsComentAdapter extends RecyclerView.Adapter<ObsComentAdapter.ViewHolder> {
    private Activity activity;
    private List<SubDetalleModel> items;
    Maestro TipoComent;
    View popupView;
    private final static int VISIBLE_ITEM_TYPE = 1;
    private final static int INVISIBLE_ITEM_TYPE = 2;

    public ObsComentAdapter(Activity activity, List<SubDetalleModel> items) {
        this.activity = activity;
        this.items = items;
    }
    public void add(SubDetalleModel newdata){
        items.add(newdata);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;//= inflater.inflate(R.layout.item_obs_ischeck , parent, false);
        if (viewType == INVISIBLE_ITEM_TYPE) {
            // The type is invisible, so just create a zero-height Space widget to hold the position.
            view = new Space(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        } else {
            LayoutInflater inflater = activity.getLayoutInflater();
             view = inflater.inflate(R.layout.item_obscoment, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if (viewHolder.getItemViewType() == VISIBLE_ITEM_TYPE){
            viewHolder.tipoComent.setText(items.get(position).CodSubtipo);
            viewHolder.descripcion.setText(items.get(position).Descripcion);
            viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             SubDetalleModel item=items.get(position);
                                                             switch (item.Codigo){
                                                                 case "1": GlobalVariables.ObserbacionDetalle.ComOpt1=null; break;
                                                                 case "2": GlobalVariables.ObserbacionDetalle.ComOpt2=null; break;
                                                                 case "3": GlobalVariables.ObserbacionDetalle.ComOpt3=null; break;
                                                             }
                                                             items.remove(position);
                                                             notifyItemRemoved(position);
                                                             notifyItemRangeChanged(position,items.size());
                                                         }
                                                     }
            );
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           SubDetalleModel item = items.get(position);
                                                           String Tipo=item.CodTipo;
                                                           ImageButton btn_Cerrar;
                                                           EditText et_etapa,txt_description;
                                                           Spinner spinnerComent;
                                                           Button btn_agregar;
                                                           LayoutInflater layoutInflater = (LayoutInflater) v.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                                                           if(Tipo.equals("COM")) popupView= layoutInflater.inflate(R.layout.popup_comentarios, null);
                                                           else popupView= layoutInflater.inflate(R.layout.popup_etapa, null);
                                                           PopupWindow popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);

                                                           popupWindow.showAtLocation(v.getRootView(), Gravity.NO_GRAVITY, 0, 0);
                                                           popupWindow.setFocusable(true);
                                                           popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                                                           popupWindow.setOutsideTouchable(true);
                                                           popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);


                                                           et_etapa = popupView.findViewById(R.id.et_etapa);
                                                           txt_description = popupView.findViewById(R.id.txt_description);
                                                           btn_agregar = popupView.findViewById(R.id.btn_agregar);
                                                           txt_description.setText(item.Descripcion);
                                                           if(Tipo.equals("COM")){
                                                               spinnerComent = popupView.findViewById(R.id.sp_tipoComent);
                                                               ArrayAdapter adapterCometarios = new ArrayAdapter( v.getContext(), R.layout.custom_spinner_item, GlobalVariables.O_Comentarios);
                                                               adapterCometarios.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
                                                               spinnerComent.setAdapter(adapterCometarios);
                                                               spinnerComent.setSelection(GlobalVariables.indexOf(GlobalVariables.O_Comentarios, item.Codigo));
                                                               spinnerComent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                   @Override
                                                                   public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                                                       TipoComent = (Maestro) ((Spinner) popupView.findViewById(R.id.sp_tipoComent)).getSelectedItem();
                                                                   }

                                                                   @Override
                                                                   public void onNothingSelected(AdapterView<?> parentView) {
                                                                   }
                                                               });
                                                           }
                                                           else et_etapa.setText(item.CodSubtipo);

                                                           btn_Cerrar = (ImageButton) popupView.findViewById(R.id.btn_close);
                                                           btn_Cerrar.setOnClickListener(new Button.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   popupWindow.dismiss();
                                                               }
                                                           });

                                                           btn_agregar.setOnClickListener(new Button.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   String Codigo="-1";
                                                                   String txt_Descripcion1="";
                                                                   String txt_Descripcion2=  txt_description.getText().toString();
                                                                   if(Tipo.equals("COM")){
                                                                       Codigo=TipoComent.CodTipo;
                                                                       txt_Descripcion1=TipoComent.Descripcion;
                                                                   }
                                                                   else txt_Descripcion1=  et_etapa.getText().toString();

                                                                   InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                   imm.hideSoftInputFromWindow(txt_description.getWindowToken(), 0);
                                                                   //txt_description
                                                                   if (txt_Descripcion1.trim().equals("")|| txt_Descripcion1.trim().equals("-Seleccione-")|| txt_Descripcion2.trim().equals(""))
                                                                   {
                                                                       Toast.makeText(v.getContext(), "Los campos no pueden estar vacios", Toast.LENGTH_LONG).show();
                                                                       return;
                                                                   }
                                                                   if(Tipo.equals("COM")){
                                                                       boolean datarep = false;
                                                                       for (SubDetalleModel obsm : items) {
                                                                           if (obsm.Codigo.equals(TipoComent.CodTipo)) { //obsm.CodTipo!=null&&
                                                                               datarep = true;
                                                                           }
                                                                       }

                                                                       if (datarep&&item.Codigo!=Codigo)
                                                                           Toast.makeText(v.getContext(), "El tipo de comentario ya existe", Toast.LENGTH_LONG).show();
                                                                       else {
                                                                           //add(new SubDetalleModel(Codigo,"COM", txt_Descripcion1, txt_Descripcion2));
                                                                           item.Codigo=Codigo;
                                                                           item.CodSubtipo=txt_Descripcion1;
                                                                           item.Descripcion=txt_Descripcion2;
                                                                           switch (item.Codigo){
                                                                               case "1": GlobalVariables.ObserbacionDetalle.ComOpt1=item.Descripcion; break;
                                                                               case "2": GlobalVariables.ObserbacionDetalle.ComOpt2=item.Descripcion; break;
                                                                               case "3": GlobalVariables.ObserbacionDetalle.ComOpt3=item.Descripcion; break;
                                                                           }
                                                                           notifyDataSetChanged();
                                                                           popupWindow.dismiss();
                                                                       }

                                                                   }
                                                                   else  {
                                                                       item.CodSubtipo=txt_Descripcion1;
                                                                       item.Descripcion=txt_Descripcion2;
                                                                       notifyDataSetChanged();
                                                                       popupWindow.dismiss();
                                                                   }

                                                               }
                                                           });
                                                       }
                                                   }
            );
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (items.get(position).CodTipo.equals("PETO") || items.get(position).CodTipo.equals("COM")) ? VISIBLE_ITEM_TYPE:INVISIBLE_ITEM_TYPE ;
    }
    @Override
    public int getItemCount() {
       return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener
        private TextView tipoComent, descripcion;
        private ImageButton btn_Delete;

        public ViewHolder(View view) {
            super(view);
            tipoComent = (TextView)view.findViewById(R.id.txt_tipocom);
            descripcion = (TextView)view.findViewById(R.id.txt_descripcion);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
        }
    }

   /* public class DividerItemDecoration extends RecyclerView.ItemDecoration {


        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            for (int i = 0; i < parent.getChildCount(); i++) {

                if (parent.getChildAt(i).getVisibility() == View.GONE)
                    continue;
            }
        }
    }*/











}
