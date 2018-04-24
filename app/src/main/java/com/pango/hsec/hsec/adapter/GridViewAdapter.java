package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Observaciones.ActVidDet;
import com.pango.hsec.hsec.Observaciones.Galeria_detalle;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.util.Compressor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {
    private List<GaleriaModel> items;
    private Activity activity;

    public GridViewAdapter(Activity activity, List<GaleriaModel> items) {
        this.activity = activity;
        this.items = items;
        GlobalVariables.listaGaleria=items;
    }

    public void add(GaleriaModel newdata){
        boolean pass=true;
        for (GaleriaModel temp: items) {
            if(temp.Descripcion.equals(newdata.Descripcion)&&temp.Tamanio.equals(newdata.Tamanio))
                pass=false;
        }
        if(pass)
        {
            if(newdata.TipoArchivo.equals("TP01")) newdata.Estado="P";
                items.add(newdata);
                notifyDataSetChanged();
        }
        else Toast.makeText(activity, "El archivo ya existe en la lista" , Toast.LENGTH_SHORT).show();
    }

    public void ProcesarImagens(){
        boolean pass= false;
        for (GaleriaModel item:items)
        {
            if(item.Estado!=null&&item.Estado.equals("P")){
                   if(!pass){
                       Toast.makeText(activity, "Procesando Imagen..." , Toast.LENGTH_SHORT).show();
                       pass=true;
                }
                customimage(item);
            }
        }
    }

    public boolean finaliceProceso(){
        for (GaleriaModel item: items) {
            if(item.Estado.equals("P")) return false;
        }
        return true;
    }

    public void replace(GaleriaModel replacedata){
        for (int i=0;i<items.size();i++){
            if(items.get(i).Descripcion.equals(replacedata.Descripcion)){
                items.set(i,replacedata);
                notifyDataSetChanged();
            }
        }
    }

    public void customimage(GaleriaModel mFileup){

        File mfile= new File(mFileup.Url);
        Bitmap bMap = BitmapFactory.decodeFile(mfile.getAbsolutePath());
        if (bMap.getHeight() > bMap.getWidth()) {
            try {
                mfile = new Compressor(activity)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(mfile);
                replace(new GaleriaModel(mfile.getAbsolutePath(),"TP01",mfile.length()+"",mfile.getName()));

            } catch (IOException e) {
                e.printStackTrace();
                showError(e.getMessage());
            }
        } else {
            try {
                mfile = new Compressor(activity)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(mfile);
                replace(new GaleriaModel(mfile.getAbsolutePath(),"TP01",mfile.length()+"",mfile.getName()));
            } catch (IOException e) {
                e.printStackTrace();
                showError(e.getMessage());
            }
        }
    }

    private void showError(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_grid, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.selectid=position;
        String urlMedio=items.get(position).Url;
        if (items.get(position).Correlativo>0) urlMedio=GlobalVariables.Url_base +"media/getImagepreview/"+items.get(position).Correlativo+ "/Preview.jpg";
        if(items.get(position).TipoArchivo.equals("TP02")){
            viewHolder.btn_play.setVisibility(View.VISIBLE);
            Glide.with(viewHolder.imageView.getContext())
                    .load(urlMedio)
                    .into(viewHolder.imageView);
            //viewHolder.imageView.setImageResource(items.get(position).getDrawableId());
           // viewHolder.textView.setText(items.get(position).Descripcion);
        }
        else{
            Glide.with(viewHolder.imageView.getContext())
                    .load(urlMedio)
                    //.fitCenter()
                    .into(viewHolder.imageView);
            //viewHolder.imageView.setImageResource(items.get(position).getDrawableId());
           // viewHolder.textView.setText(items.get(position).Descripcion);
        }
        viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         // GlobalVariables.listaArchivos.remove(position);
                                                         String item= items.get(position).Descripcion;
                                                         int Correlativo= items.get(position).Correlativo;
                                                         items.remove(position);
                                                         GlobalVariables.listaGaleria=items;
                                                         notifyItemRemoved(position);
                                                         notifyItemRangeChanged(position,items.size());
                                                        // if(Correlativo>0) Toast.makeText(v.getContext(), "Seleccione 'Guardar cambios' para eliminar definitivamente" , Toast.LENGTH_SHORT).show();
                                                         //else Toast.makeText(v.getContext(),"Removed : " +item,Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private ImageView imageView;
       // private TextView textView;
        private ImageView btn_play;
        private ImageButton btn_Delete ;
        public int selectid;

        public ViewHolder(View view) {
            super(view);
            //textView = (TextView)view.findViewById(R.id.text);
            imageView = (ImageView) view.findViewById(R.id.image);
            btn_play=(ImageView) view.findViewById(R.id.btn_playGrid);
            btn_Delete= (ImageButton) view.findViewById(R.id.button_r);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(items.get(selectid).TipoArchivo.equals("TP01")) {

                Intent intent = new Intent(v.getContext(), Galeria_detalle.class);
                //intent.putExtra(ActImagDet.EXTRA_PARAM_ID, a);
                intent.putExtra("post", selectid);
                v.getContext().startActivity(intent);
            }else if(items.get(selectid).TipoArchivo.equals("TP02")){

                //String finalTempUrl="https://app.antapaccay.com.pe/Proportal/SCOM_Service/Videos/1700.mp4";
                String finalTempUrl=(items.get(selectid).Correlativo>0?GlobalVariables.Url_base:"")+items.get(selectid).Url;

                //Toast.makeText(activity,"video",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), ActVidDet.class);
                //intent.putExtra("post",position);
                intent.putExtra("urltemp", finalTempUrl);
                intent.putExtra("isList", true);

                //intent.putExtra("val",0);
                //intent.putExtra(ActVidDet.EXTRA_PARAM_ID, item.getId());
                v.getContext().startActivity(intent);

            }
        }
    }
}
