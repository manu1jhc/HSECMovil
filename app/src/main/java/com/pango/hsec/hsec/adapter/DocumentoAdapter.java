package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.Ficha.RegistroAtencion;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.GaleriaModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Andre on 28/03/2018.
 */

public class DocumentoAdapter extends RecyclerView.Adapter<DocumentoAdapter.ViewHolder> {


    private ArrayList<GaleriaModel> Data;
    private Activity activity;
    boolean permiso;
    public DocumentoAdapter(Activity activity, ArrayList<GaleriaModel> Data,boolean permiso) {
        this.activity = activity;
        this.Data = Data;
        this.permiso=permiso;
    }

    @Override
    public DocumentoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.public_documentos, viewGroup, false);

        return new DocumentoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DocumentoAdapter.ViewHolder viewHolder, int position) {
        viewHolder.position_item=position;

        int idIcon=0;
        String [] exts=Data.get(position).Descripcion.split("\\.");
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

        viewHolder.btn_descargar.setImageResource(idIcon);
        viewHolder.nombre_doc.setText(Data.get(position).Descripcion);

        if(Data.get(position).Tamanio!=null)viewHolder.tam_doc.setText(getReadableFileSize(Long.parseLong(Data.get(position).Tamanio)));
        else viewHolder.tam_doc.setVisibility(View.GONE);


        //viewHolder.tam_doc.setText(Data.get(position).Tamanio+" Mb");


    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder  {
        TextView nombre_doc, tam_doc;
        ImageView btn_descargar,btn_cancelar;
        ProgressBar progressBar;
        public int position_item;
        String nombre_file="";
        String cadMod="";
        long downloadReference;
        DownloadManager manager;
        boolean cancelDownload=false;

        public ViewHolder(View view) {
            super(view);
            nombre_doc = (TextView)view.findViewById(R.id.nombre_doc);
            tam_doc = (TextView)view.findViewById(R.id.tam_doc);
            btn_descargar = (ImageView) view.findViewById(R.id.btn_descargar);
            btn_cancelar=(ImageView) view.findViewById(R.id.btn_cancelar);
            progressBar=(ProgressBar) view.findViewById(R.id.progressBar);

            btn_descargar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelDownload=false;
                    if (permiso) {
                        progressBar.setVisibility(View.VISIBLE);
                        btn_cancelar.setVisibility(View.VISIBLE);
                        btn_descargar.setVisibility(View.GONE);
                        String url_serv = GlobalVariables.Url_base + Data.get(position_item).Url;
                        nombre_file = Data.get(position_item).Descripcion;
                        //String url_serv="http://192.168.1.214/SCOM_Service/api/multimedia/GetImagen/182/portal   bug.png";
                        cadMod = Utils.ChangeUrl(url_serv);

                        // registrer receiver in order to verify when download is complete
                        //registerReceiver(new DonwloadCompleteReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(cadMod));
                        //request.setDescription("Downloading file " + NAME_FILE);
                        //request.setTitle("Downloading");
                        // in order for this if to run, you must use the android 3.2 to compile your app
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        }
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nombre_file);

                        // get download service and enqueue file
                        manager = (DownloadManager) v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        //manager.enqueue(request);
                        downloadReference = manager.enqueue(request);
                        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                        activity.registerReceiver(downloadReceiver, filter);

                    }else{
                        Toast.makeText(v.getContext(), "La aplicación no tiene permisos de escritura", Toast.LENGTH_LONG).show();

                    }
                }
            });

            btn_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelDownload=true;
                    manager.remove(downloadReference);
                    progressBar.setVisibility(View.GONE);
                    btn_cancelar.setVisibility(View.GONE);
                    btn_descargar.setVisibility(View.VISIBLE);
                }
            });


        }


        private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                //check if the broadcast message is for our Enqueued download
                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(cancelDownload){
                    Toast toast = Toast.makeText(activity,"Descarga Cancelada", Toast.LENGTH_LONG);
                    //toast.setGravity(Gravity.BOTTOM, 25, 400);
                    toast.show();
                    cancelDownload=false;
                }else {
                    progressBar.setVisibility(View.GONE);
                    btn_cancelar.setVisibility(View.GONE);
                    btn_descargar.setVisibility(View.VISIBLE);

                    Toast toast = Toast.makeText(activity,"Descarga completada", Toast.LENGTH_LONG);
                    //toast.setGravity(Gravity.TOP, 25, 400);
                    toast.show();
                }
            }
        };






    }







}
