package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.GaleriaModel;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private Activity activity;
    private List<GaleriaModel> items;

    public ListViewAdapter(Activity activity, List<GaleriaModel> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        int idIcon=0;
        String [] exts=items.get(position).Descripcion.split("\\.");
        String ext=exts[exts.length-1];
        switch (ext) {
            case "pdf":
                idIcon = R.drawable.ic_content_file;
                break;
            case "doc":
            case "docx":
            case "ppt":
            case "pptx":
            case "xls":
            case "xlsx":
            case "odt":
                idIcon = R.drawable.ic_contrata;
                break;
            default:
                idIcon = R.drawable.ic_2_aprob;
        }
        viewHolder.imageView.setImageResource(idIcon);
        viewHolder.textView.setText(items.get(position).Descripcion);
        viewHolder.idposition=position;
        viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                             // GlobalVariables.listaArchivos.remove(position);
                                              String item= items.get(position).Descripcion;
                                              int Correlativo= items.get(position).Correlativo;
                                              items.remove(position);
                                              GlobalVariables.listaArchivos=items;
                                              notifyItemRemoved(position);
                                              notifyItemRangeChanged(position,items.size());
                                              if(Correlativo>0)Toast.makeText(v.getContext(), "Seleccione 'Guardar cambios' para eliminar definitivamente" , Toast.LENGTH_SHORT).show();
                                             // else Toast.makeText(v.getContext(),"Removed : " +item,Toast.LENGTH_SHORT).show();
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
    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView textView;
        private ImageButton btn_Delete;
        public int idposition;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            textView = (TextView)view.findViewById(R.id.text);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           if(items.get(idposition).Correlativo>0)
            Toast.makeText(v.getContext(), "Descargando...", Toast.LENGTH_SHORT).show();
        }
    }
}
