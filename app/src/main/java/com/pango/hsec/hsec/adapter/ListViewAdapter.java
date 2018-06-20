package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.GaleriaModel;

import java.text.DecimalFormat;
import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private Activity activity;
    private List<GaleriaModel> items;

    public ListViewAdapter(Activity activity, List<GaleriaModel> items) {
        this.activity = activity;
        this.items = items;
    }

    public void add(GaleriaModel newdata){
        boolean pass=true;
        for (GaleriaModel temp: items) {
            if(temp.Descripcion.equals(newdata.Descripcion)&&temp.Tamanio.equals(newdata.Tamanio))
                pass=false;
        }
        if(pass)
        {
            items.add(newdata);
            notifyDataSetChanged();
        }
        else Toast.makeText(activity, "El archivo ya existe en la lista" , Toast.LENGTH_SHORT).show();
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
        switch (ext.toLowerCase()) {
            case "pdf":
                idIcon = R.drawable.ic_pdf;
                break;
            case "doc":
            case "docx":
                idIcon = R.drawable.ic_word;
                break;
            case "ppt":
            case "pptx":
                idIcon = R.drawable.ic_ppt;
                break;
            case "xls":
            case "xlsx":
                idIcon = R.drawable.ic_xlsx;
                break;
            case "odt":
                idIcon = R.drawable.ic_contrata;
                break;
            default:
                idIcon = R.drawable.ic_contrata;
        }

        if(items.get(position).Estado!=null&&items.get(position).Estado=="E"){
            viewHolder.imageView.setImageResource(R.drawable.ic_broken);
            //if(position==items.size()-1){GlobalVariables.cambiarIcon=false;}
        }else {
            viewHolder.imageView.setImageResource(idIcon);

        }

        viewHolder.textView.setText(items.get(position).Descripcion);
        if(items.get(position).Tamanio!=null)viewHolder.textView2.setText(getReadableFileSize(Long.parseLong(items.get(position).Tamanio)));
        else viewHolder.textView2.setVisibility(View.GONE);
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
                                              //if(Correlativo>0)Toast.makeText(v.getContext(), "Seleccione 'Guardar cambios' para eliminar definitivamente" , Toast.LENGTH_SHORT).show();
                                             // else Toast.makeText(v.getContext(),"Removed : " +item,Toast.LENGTH_SHORT).show();
                                          }
                                      }
        );

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         GlobalVariables.cancelDownload=false;
                                                         int Correlativo= items.get(position).Correlativo;
                                                         String nameFile= items.get(position).Descripcion;
                                                         if(Correlativo>0){
                                                             viewHolder.progressBar.setVisibility(View.VISIBLE);
                                                             viewHolder.btn_cancelar.setVisibility(View.VISIBLE);
                                                             viewHolder.imageView.setVisibility(View.GONE);

                                                             viewHolder.downloadManager =(DownloadManager) v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                                             String url_serv= GlobalVariables.Url_base+ items.get(position).Url;
                                                             String cadMod= Utils.ChangeUrl(url_serv);
                                                             Uri uri=Uri.parse(cadMod);
                                                             DownloadManager.Request request= new DownloadManager.Request(uri);
                                                             request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                                             viewHolder.downloadReference = viewHolder.downloadManager.enqueue(request);
                                                             Toast.makeText(v.getContext(),"Descargando : " +nameFile,Toast.LENGTH_SHORT).show();

                                                             IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                                                             activity.registerReceiver(viewHolder.downloadReceiver, filter);

                                                         }
                                                     }
                                                 }
        );



        viewHolder.btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.cancelDownload=true;
                viewHolder.downloadManager.remove(viewHolder.downloadReference);
                viewHolder.progressBar.setVisibility(View.GONE);
                viewHolder.btn_cancelar.setVisibility(View.GONE);
                viewHolder.imageView.setVisibility(View.VISIBLE);
            }
        });


    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        private ImageView imageView,btn_cancelar;
        private TextView textView, textView2;
        private ImageButton btn_Delete;
        public int idposition;

        ProgressBar progressBar;
        long downloadReference;
        DownloadManager downloadManager;
        //boolean cancelDownload=true;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            textView = (TextView)view.findViewById(R.id.text);
            textView2 = (TextView)view.findViewById(R.id.text2);
            btn_cancelar=(ImageView) view.findViewById(R.id.btn_cancelar);
            progressBar=(ProgressBar) view.findViewById(R.id.progressBar);

            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
            //itemView.setOnClickListener(this);
        }
/*
        @Override
        public void onClick(View v) {
           if(items.get(idposition).Correlativo>0)
            Toast.makeText(v.getContext(), "Descargando...", Toast.LENGTH_SHORT).show();
        }
*/

        private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                //check if the broadcast message is for our Enqueued download
                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(GlobalVariables.cancelDownload){
                    Toast toast = Toast.makeText(activity,"Descarga Cancelada", Toast.LENGTH_LONG);
                    //toast.setGravity(Gravity.BOTTOM, 25, 400);
                    toast.show();
                    //GlobalVariables.cancelDownload=false;
                }else {
                    progressBar.setVisibility(View.GONE);
                    btn_cancelar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                    Toast toast = Toast.makeText(activity,"Descarga completada", Toast.LENGTH_LONG);
                    //toast.setGravity(Gravity.TOP, 25, 400);
                    toast.show();
                }
            }
        };



    }
}
