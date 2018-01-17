package com.pango.hsec.hsec.Observaciones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ComentAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetComentModel;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentComent extends Fragment implements IActivity {

	private static View mView;
	String codObs;
	String url;
	String UrlObs;

	public static final FragmentComent newInstance(String sampleText) {
		FragmentComent f = new FragmentComent();

		Bundle b = new Bundle();
		b.putString("bString", sampleText);
		//b.putString("urlobs",urlObs);
		f.setArguments(b);

		return f;
	}

	ImageButton btn_send;
	EditText et_comentario;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_coment, container, false);
		GlobalVariables.count=1;
		GlobalVariables.view_fragment=mView;

		btn_send=(ImageButton) mView.findViewById(R.id.btn_send);
		et_comentario=(EditText) mView.findViewById(R.id.et_comentario);

		//codObs=getArguments().getString("bString");
		//UrlObs=getArguments().getString("urlobs");

		codObs="OBS00240761";
		url= GlobalVariables.Url_base+"Comentario/getObs/"+codObs;

		final ActivityController obj = new ActivityController("get", url, FragmentComent.this);
		obj.execute("");

		btn_send.setEnabled(false);

		et_comentario.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				String a=s.toString().trim();
				if(a.equals("")) {
					btn_send.setEnabled(false);
				}else{
					btn_send.setEnabled(true);
				}
			}
		});



		btn_send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			String comentario= String.valueOf(et_comentario.getText());

				String json = "";
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.accumulate("CodComentario",codObs);
					jsonObject.accumulate("Comentario",comentario);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				json += jsonObject.toString();

				url= GlobalVariables.Url_base+"Comentario/insert";

				final ActivityController obj = new ActivityController("post", url, FragmentComent.this);
				obj.execute(json);

			String a="";

			}
		});






		return mView;
	}

	ComentAdapter comentAdapter;
	@Override
	public void success(String data,String Tipo) {
		Gson gson = new Gson();
		GetComentModel getComentModel= gson.fromJson(data, GetComentModel.class);
		comentAdapter=new ComentAdapter(getContext(),getComentModel.Data);
		ListView listaComentarios = (ListView) mView.findViewById(R.id.list_coment);
		listaComentarios.setAdapter(comentAdapter);

		listaComentarios.setSelection(getComentModel.Data.size()-1);



	}

	@Override
	public void successpost(String data,String Tipo) {
		closeSoftKeyBoard();
		et_comentario.setText("");
		switch (data) {
			case "1":
				Toast.makeText(getContext(),"Comentario enviado",Toast.LENGTH_SHORT).show();
				GlobalVariables.count=5;
				url= GlobalVariables.Url_base+"Comentario/getObs/"+codObs;
				final ActivityController obj = new ActivityController("get", url, FragmentComent.this);
				obj.execute("");


				break;

			case "-1":
				Toast.makeText(getContext(),"Ocurrio un error al enviar su mensaje",Toast.LENGTH_SHORT).show();

				break;
			default:
				Toast.makeText(getContext(),"Error"+data,Toast.LENGTH_SHORT).show();		}
	}

	@Override
	public void error(String mensaje,String Tipo) {

	}


	public void closeSoftKeyBoard() {
		InputMethodManager inputMethodManager =
				(InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
	}

}