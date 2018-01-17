package com.pango.hsec.hsec.Observaciones;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.Adap_Img;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;

public class FragmentGaleria extends Fragment implements IActivity, AdapterView.OnItemClickListener {

	private static View mView;
	String codObs;
	String url;
	Adap_Img adaptador;

	public static final FragmentGaleria newInstance(String sampleText) {
		FragmentGaleria f = new FragmentGaleria();

		Bundle b = new Bundle();
		b.putString("bString", sampleText);
		f.setArguments(b);

		return f;
	}

	TextView txGaleria;
	GridView grid_gal;
	ConstraintLayout cl_otros;
	FrameLayout frame_otros;
	boolean isPressed=true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_galeria,
				container, false);

		txGaleria=(TextView) mView.findViewById(R.id.tx_gal);
		grid_gal=(GridView) mView.findViewById(R.id.grid_gal);
		cl_otros=(ConstraintLayout) mView.findViewById(R.id.cl_otros);
		frame_otros=(FrameLayout) mView.findViewById(R.id.frame_otros);

		GlobalVariables.count=1;
		GlobalVariables.view_fragment=mView;

		//codObs=getArguments().getString("bString");
		codObs="OBS00240578";

		url= GlobalVariables.Url_base+"media/GetMultimedia/"+codObs;

		//https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/GetMultimedia/OBS00240578


		final ActivityController obj = new ActivityController("get", url, FragmentGaleria.this);
		obj.execute("");







		/////////////////////////////////////////////////////////////////////
		txGaleria.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(grid_gal.getVisibility()==View.GONE){
					grid_gal.setVisibility(View.VISIBLE);
				}else{
					grid_gal.setVisibility(View.GONE);
				}
				//grid_gal.setVisibility(View.GONE);
			}
		});

		cl_otros.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(frame_otros.getVisibility()==View.GONE){
					frame_otros.setVisibility(View.VISIBLE);
				}else{
					frame_otros.setVisibility(View.GONE);
				}
			}
		});
////////////////////////////////////////////////////////////////////////////////7


/*
		String sampleText = getArguments().getString("bString");

		TextView txtSampleText = (TextView) mView.findViewById(R.id.txtViewSample);
		txtSampleText.setText(sampleText);
*/
		return mView;
	}


	@Override
	public void success(String data) {
		Gson gson = new Gson();
		GetGaleriaModel getGaleriaModel = gson.fromJson(data, GetGaleriaModel.class);
		GlobalVariables.listaGaleria=getGaleriaModel.Data;



		grid_gal=(GridView) mView.findViewById(R.id.grid_gal);
		adaptador = new Adap_Img(getContext(),getGaleriaModel);
		grid_gal.setAdapter(adaptador);
		grid_gal.setOnItemClickListener(this);



	}

	@Override
	public void successpost(String data) {

	}

	@Override
	public void error(String mensaje) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		GaleriaModel item= (GaleriaModel) parent.getItemAtPosition(position);
		//int a= item.getId();


		if(GlobalVariables.listaGaleria.get(position).TipoArchivo.equals("TP01")) {

			Intent intent = new Intent(getContext(), Galeria_detalle.class);
			//intent.putExtra(ActImagDet.EXTRA_PARAM_ID, a);
			intent.putExtra("post", position);
			startActivity(intent);
		}else if(GlobalVariables.listaGaleria.get(position).TipoArchivo.equals("TP02")){

			//String finalTempUrl="https://app.antapaccay.com.pe/Proportal/SCOM_Service/Videos/1700.mp4";
			String finalTempUrl=GlobalVariables.Url_base+GlobalVariables.listaGaleria.get(position).Url;

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
}