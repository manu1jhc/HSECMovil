package com.pango.hsec.hsec;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.FontRequest;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import com.pango.hsec.hsec.Observaciones.ActVidDet;
import com.pango.hsec.hsec.Observaciones.Galeria_detalle;
import com.pango.hsec.hsec.R;

import android.app.ProgressDialog;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.adapter.Adap_Img;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.ImageEntry;
import com.pango.hsec.hsec.util.Picker;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.adapter.ListViewAdapter;


import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class obs_archivos extends Fragment implements IActivity,Picker.PickListener {

    private static View mView;
    /*MyGridView grid_gal;
    Adap_Img adaptador;
    private RecyclerView mImageSampleRecycler;*/

    private RecyclerView listView;
    private RecyclerView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<GaleriaModel> DataFiles;
    private ArrayList<GaleriaModel> DataImg;

    private ArrayList<ImageEntry> mSelectedImages;
    ProgressDialog progressDialog;
    final String[] ACCEPT_MIME_TYPES = {
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/msword",
            "application/odt",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-excel"
    };

    public static final com.pango.hsec.hsec.obs_archivos newInstance(String sampleText) {
        obs_archivos f = new obs_archivos();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_archivos,  container, false);
        //String sampleText = getArguments().getString("bString");

        //TextView txtSampleText = (TextView) mView.findViewById(R.id.txtViewSample);
        //txtSampleText.setText(sampleText);
        ImageButton btnFotos=(ImageButton) mView.findViewById(R.id.btn_addfotos);
        ImageButton btnFiles=(ImageButton) mView.findViewById(R.id.btn_addfiles);
       /* mImageSampleRecycler = (RecyclerView) mView.findViewById(R.id.images_sample);
        setupRecycler();*/
        gridView = (RecyclerView)  mView.findViewById(R.id.grid);
        DataImg = new ArrayList<>();

        listView = (RecyclerView) mView.findViewById(R.id.list);
        DataFiles = new ArrayList<>();

        //grid_gal=(MyGridView) mView.findViewById(R.id.grid_gal);

        btnFotos.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            loadImage();
                                        }
                                    }
        );

        btnFiles.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent();
                                            intent.setType("application/pdf");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
                                            startActivityForResult(Intent.createChooser(intent, "Choose Document"), 1);
                                        }
                                    }
        );

        return mView;
    }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data)
   {
       if(resultCode==RESULT_CANCELED)
       {
           // action cancelled
       }
       if(resultCode==RESULT_OK)
       {
           Uri uri = data.getData();
           String uriString = uri.toString();
           File myFile = new File(uriString);
           String displayName="";
           if (uriString.startsWith("content://")) {
               Cursor cursor = null;
               try {
                   cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                   if (cursor != null && cursor.moveToFirst()) {
                       displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                   }
               } finally {
                   cursor.close();
               }
           } else if (uriString.startsWith("file://")) {
               displayName = myFile.getName();
           }

           String [] exts=displayName.split("\\.");
           String ext=exts[exts.length-1];
           switch (ext){
               case "pdf":case "doc":case "docx":case "ppt":case "pptx":case "xls":case "xlsx":case "odt":
                   DataFiles.add(new GaleriaModel(myFile.getAbsolutePath(),"TP03", myFile.getTotalSpace()+"", displayName));
                   GlobalVariables.listaArchivos=DataFiles;
                   //listView.setHasFixedSize(true);
                   //set layout manager and adapter for "ListView"
                   LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                   listView.setLayoutManager(horizontalManager);
                   listViewAdapter = new ListViewAdapter(getActivity(), DataFiles);
                   listView.setAdapter(listViewAdapter);

                   break;
               default: Toast.makeText(getActivity(), "Archivo no permitido", Toast.LENGTH_LONG).show();
           }
       }
   }
   /* private static boolean isDownloadManagerAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }*/
    public void loadImage(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
        else  if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {Manifest.permission.CAMERA}, 1022);
        else  if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {android.Manifest.permission.RECORD_AUDIO}, 1033);
        else
        {
            new Picker.Builder(getActivity(), obs_archivos.this, R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setVideosEnabled(true)
                    .setLimit(10)
                    .build()
                    .startActivity();
        }
    }
    @SuppressLint("ValidFragment")
    @Override

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1011: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(getActivity(), "Para poder agregar archivos multimedia, necesita otorgar los persmisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 1022: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(getActivity(), "Para poder utilizar la camara, necesita otorgar los permisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 1033: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(getActivity(), "Para poder grabar un video, necesita otorgar los permisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* final int id = item.getItemId();

        if (id == R.id.action_about) {
            showAbout();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPickedSuccessfully(ArrayList<ImageEntry> images) {

        for (ImageEntry image:images) {
            DataImg.add(new GaleriaModel(image.path,image.isVideo?"TP02":"TP01",image.dateAdded+"",image.imageId+""));
        }
        GlobalVariables.listaGaleria=DataImg;
        mSelectedImages = images;
      //  gridView.setHasFixedSize(true);

        //set layout manager and adapter for "GridView"
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(getActivity(), DataImg);
        gridView.setAdapter(gridViewAdapter);

    }

    @Override
    public void onCancel() {
        //Log.i(TAG, "User canceled picker activity");
       // Toast.makeText(getActivity(), "User canceld picker activtiy", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void success(String data, String Tipo) {

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }


    private class ImageSamplesAdapter extends RecyclerView.Adapter<ImageSampleViewHolder> {

        @Override
        public ImageSampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ImageView imageView = new ImageView(parent.getContext());
            return new ImageSampleViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(ImageSampleViewHolder holder, int position) {

            final String path = mSelectedImages.get(position).path;
            loadImage(path, holder.thumbnail);
        }

        @Override
        public int getItemCount() {
            return mSelectedImages.size();
        }

        private void loadImage(final String path, final ImageView imageView) {
            imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 440));

            Glide.with(getActivity())
                    .load(path)
                    .asBitmap()
                    .into(imageView);
        }
    }


    class ImageSampleViewHolder extends RecyclerView.ViewHolder {

        protected ImageView thumbnail;

        public ImageSampleViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView;
        }
    }
}