package com.pango.hsec.hsec.Observaciones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ObsAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.ObservacionModel;

import layout.FragmentMuro;

public class FragmentObs extends Fragment implements IActivity {
	ObsAdapter obsAdapter;

	private View mView;
	String codObs;
	public static FragmentObs newInstance(String sampleText) {
		FragmentObs f = new FragmentObs();

		Bundle b = new Bundle();
		b.putString("bString", sampleText);
		f.setArguments(b);

		return f;
	}

	////////////////////////////////////////////////////////
	String url;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_obs, container, false);
		codObs=getArguments().getString("bString");
		url= GlobalVariables.Url_base+"Observaciones/Get/"+codObs;

		final ActivityController obj = new ActivityController("get", url, FragmentObs.this);
		obj.execute();

		return mView;
	}


	@Override
	public void success(String data) {
		Gson gson = new Gson();
		ObservacionModel getUsuarioModel = gson.fromJson(data, ObservacionModel.class);

		//if(getUsuarioModel.CodUbicacion)
		String[] parts = getUsuarioModel.CodUbicacion.split(".");



		//String area=getUsuarioModel.CodAreaHSEC;





		obsAdapter = new ObsAdapter(getContext(),getUsuarioModel);

		ListView listaDetalles = (ListView) mView.findViewById(R.id.list_det);
		listaDetalles.setAdapter(obsAdapter);






	}

	@Override
	public void error(String mensaje) {



	}
}