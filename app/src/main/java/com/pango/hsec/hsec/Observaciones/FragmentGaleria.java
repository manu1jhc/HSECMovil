package com.pango.hsec.hsec.Observaciones;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.Adap_Img;
import com.pango.hsec.hsec.adapter.DocsAdapter;
import com.pango.hsec.hsec.adapter.DocumentoAdapter;
import com.pango.hsec.hsec.adapter.GaleriaAdapter;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;

import java.util.ArrayList;


public class FragmentGaleria extends Fragment implements IActivity {
	int contador=0;
	ArrayList<GaleriaModel> DataDocs=new ArrayList<GaleriaModel>();
	ArrayList<GaleriaModel> DataImg=new ArrayList<GaleriaModel>();
	GetGaleriaModel getImg;
	private static View mView;
	String codObs="";
	String jsonGaleria="";
	String url="";
	Adap_Img adaptador;
	DownloadManager downloadManager;
	private static final short REQUEST_CODE = 6545;

	boolean permiso=false;
	DocumentoAdapter documentoAdapter;
	//GaleriaAdapter galeriaAdapter;
	GridViewAdapter galeriaAdapter;

	public static final FragmentGaleria newInstance(String sampleText) {
		FragmentGaleria f = new FragmentGaleria();

		Bundle b = new Bundle();
		b.putString("bString", sampleText);
		f.setArguments(b);

		return f;
	}

	TextView txGaleria,mensaje;
	//GridView grid_gal;
	ConstraintLayout cl_otros;
	FrameLayout frame_otros;
	//ListView list_docs;
	RelativeLayout rel_otros;
	LinearLayout galeria_foto;
	RecyclerView gridView,listView;

	boolean isPressed=true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_galeria,
				container, false);
		gridView = (RecyclerView) mView.findViewById(R.id.rec_galeria);
		listView = (RecyclerView) mView.findViewById(R.id.list_docs);

		txGaleria=(TextView) mView.findViewById(R.id.tx_gal);
		//grid_gal=(GridView) mView.findViewById(R.id.grid_gal);
		cl_otros=(ConstraintLayout) mView.findViewById(R.id.cl_otros);
		frame_otros=(FrameLayout) mView.findViewById(R.id.frame_otros);
		//list_docs=(ListView) mView.findViewById(R.id.list_docs);
		rel_otros=(RelativeLayout) mView.findViewById(R.id.rel_otros);
		galeria_foto=(LinearLayout) mView.findViewById(R.id.galeria_foto);
		mensaje=(TextView) mView.findViewById(R.id.mensaje);
		//GlobalVariables.count=1;
		GlobalVariables.view_fragment=mView;
		GlobalVariables.isFragment=true;

		codObs=getArguments().getString("bString");
		//codObs="OBS00089866";
		//codObs="OBS00240578";

		url= GlobalVariables.Url_base+"media/GetMultimedia/"+codObs;

		//https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/GetMultimedia/OBS00240578
		if(jsonGaleria.isEmpty()) {
			GlobalVariables.istabs=true;
			final ActivityController obj = new ActivityController("get", url, FragmentGaleria.this,getActivity());
			obj.execute("");

		}else{
			success(jsonGaleria,"");
		}


		/////////////////////////////////////////////////////////////////////
		txGaleria.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(gridView.getVisibility()==View.GONE){
					gridView.setVisibility(View.VISIBLE);
				}else{
					gridView.setVisibility(View.GONE);
				}
				//grid_gal.setVisibility(View.GONE);
			}
		});

		cl_otros.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listView.getVisibility()==View.GONE){
					listView.setVisibility(View.VISIBLE);
				}else{
					listView.setVisibility(View.GONE);
				}
			}
		});
////////////////////////////////////////////////////////////////////////////////7


/*
		String sampleText = getArguments().getString("bString");

		TextView txtSampleText = (TextView) mView.findViewById(R.id.txtViewSample);
		txtSampleText.setText(sampleText);
*/

/*
		list_docs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//String NAME_FILE = GlobalVariables.listaGaleria.get(position).Descripcion;


				downloadManager=(DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
				//Uri uri=Uri.parse("http://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQf9GsdJZOxuApw8q86bV211L8tPhh1RB3zj6qIJbfVV9HwIBwlfg");
				String url_serv= GlobalVariables.Url_base+ DataDocs.get(position).Url;
				//String url_serv="http://192.168.1.214/SCOM_Service/api/multimedia/GetImagen/182/portal   bug.png";
				String cadMod= Utils.ChangeUrl(url_serv);
				//;
				Uri uri=Uri.parse(cadMod);
				// Uri uri=Uri.parse("http://192.168.1.214/SCOM_Service/api/multimedia/GetImagen/182/portal%20bug.png");
				DownloadManager.Request request= new DownloadManager.Request(uri);
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

				Long reference = downloadManager.enqueue(request);
				Toast.makeText(getActivity(), "Descargando...", Toast.LENGTH_SHORT).show();

				//   Toast.makeText(context,"Boton detalles: "+position,Toast.LENGTH_LONG);

				// registrer receiver in order to verify when download is complete

			}
		});
*/



		return mView;
	}

	private static boolean isDownloadManagerAvailable() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return true;
		}
		return false;
	}


	public void checkSelfPermission() {

		if (ContextCompat.checkSelfPermission(getActivity(),
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(getActivity(),
					new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
					REQUEST_CODE);

		} else {

			executeDownload();

		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission was granted! Do the work
					executeDownload();
				} else {
					// permission denied!
					Toast.makeText(getActivity(), "Please give permissions ", Toast.LENGTH_LONG).show();
				}
				return;
			}
		}
	}

	private void executeDownload() {


		permiso=true;




	}


	@Override
	public void success(String data,String Tipo) {
		//GlobalVariables.istabs=false;

		jsonGaleria = data;
		DataDocs.clear();
		DataImg.clear();
		//int resultado=data.indexOf("TP03");
		Gson gson = new Gson();
		GetGaleriaModel getGaleriaModel = gson.fromJson(data, GetGaleriaModel.class);
		int count=getGaleriaModel.Count;
		//GlobalVariables.listaGaleria=getGaleriaModel.Data;
		if(count!=0){
			if(data.contains("TP01") ||data.contains("TP02")){
				galeria_foto.setVisibility(View.VISIBLE);
			}else {
				galeria_foto.setVisibility(View.GONE);
			}

		if(data.contains("TP03")){
			rel_otros.setVisibility(View.VISIBLE);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				checkSelfPermission();
				//executeDownload();

			} else {
				Toast.makeText(getActivity(), "Download manager is not available", Toast.LENGTH_LONG).show();
			}

		}else{
			rel_otros.setVisibility(View.GONE);
		}

		for(int i=0;i<getGaleriaModel.Data.size();i++){
			if(getGaleriaModel.Data.get(i).TipoArchivo.equals("TP03")){
				rel_otros.setVisibility(View.VISIBLE);
				DataDocs.add(getGaleriaModel.Data.get(i));
			}else{
				DataImg.add(getGaleriaModel.Data.get(i));
			}
		}


			GlobalVariables.listaGaleria=DataImg;

			GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
			gridView.setLayoutManager(layoutManager);


			galeriaAdapter = new GridViewAdapter(getActivity(),DataImg );
			galeriaAdapter.tacho=true;
			gridView.setAdapter(galeriaAdapter);


			LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
			listView.setLayoutManager(horizontalManager);
			documentoAdapter = new DocumentoAdapter(getActivity(), DataDocs,permiso);
			listView.setAdapter(documentoAdapter);



		}else{
			mensaje.setVisibility(View.VISIBLE);
			rel_otros.setVisibility(View.GONE);
			galeria_foto.setVisibility(View.GONE);
		}
	}

	@Override
	public void successpost(String data,String Tipo) {

	}

	@Override
	public void error(String mensaje,String Tipo) {

	}


	/*
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		//GaleriaModel item= (GaleriaModel) parent.getItemAtPosition(position);
		//int a= item.getId();


		if(DataImg.get(position).TipoArchivo.equals("TP01")) {

			Intent intent = new Intent(getContext(), Galeria_detalle.class);
			//intent.putExtra(ActImagDet.EXTRA_PARAM_ID, a);
			intent.putExtra("post", position);
			startActivity(intent);
		}else if(DataImg.get(position).TipoArchivo.equals("TP02")){

			//String finalTempUrl="https://app.antapaccay.com.pe/Proportal/SCOM_Service/Videos/1700.mp4";
			String finalTempUrl=GlobalVariables.Url_base+DataImg.get(position).Url;

			//Toast.makeText(activity,"video",Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getContext(), ActVidDet.class);
			//intent.putExtra("post",position);
			intent.putExtra("urltemp", finalTempUrl);
			intent.putExtra("isList", true);

			//intent.putExtra("val",0);
			//intent.putExtra(ActVidDet.EXTRA_PARAM_ID, item.getId());
			getContext().startActivity(intent);

		}
	}

	*/




}