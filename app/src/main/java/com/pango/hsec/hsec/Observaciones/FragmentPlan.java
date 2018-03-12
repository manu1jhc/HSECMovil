package com.pango.hsec.hsec.Observaciones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.PlanAdapter;
import com.pango.hsec.hsec.adapter.PlandetAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPlanModel;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FragmentPlan extends Fragment implements IActivity {

	private static View mView;
	String codObs;
	String url;
	ListView listPlan;
	PlandetAdapter plandetAdapter;
	String jsonPlan="";
	TextView tx_msj_plan;
	public static final FragmentPlan newInstance(String sampleText) {
		FragmentPlan f = new FragmentPlan();

		Bundle b = new Bundle();
		b.putString("bString", sampleText);
		f.setArguments(b);

		return f;
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_plan, container, false);
		listPlan=(ListView) mView.findViewById(R.id.list_plan);
		tx_msj_plan=mView.findViewById(R.id.tx_msj_plan);
		codObs=getArguments().getString("bString");
		//GlobalVariables.count=1;
		GlobalVariables.view_fragment=mView;
		GlobalVariables.isFragment=true;

		//codObs="OBS00067956";
		url= GlobalVariables.Url_base+"PlanAccion/GetPlanes/"+codObs;

		if(jsonPlan.isEmpty()) {
			GlobalVariables.istabs=true;
			final ActivityController obj = new ActivityController("get", url, FragmentPlan.this);
			obj.execute("");
		}else{
			success(jsonPlan,"");
		}

		return mView;
	}

	//Button btn_Abrir_Popup;
	Button btn_Cerrar;
	LayoutInflater layoutInflater;
	View popupView;
	PopupWindow popupWindow;
	ImageButton ibclose;

	@Override
	public void success(String data,String Tipo) {
		//GlobalVariables.istabs=false;
		jsonPlan=data;

		Gson gson = new Gson();
		final GetPlanModel getPlanModel = gson.fromJson(data, GetPlanModel.class);
		String a="";

		PlanAdapter ca = new PlanAdapter(getContext(), getPlanModel.Data);
		listPlan.setAdapter(ca);

		if (getPlanModel.Data.size()==0){
			listPlan.setVisibility(View.GONE);
			tx_msj_plan.setVisibility(View.VISIBLE);
		}else{
			listPlan.setVisibility(View.VISIBLE);
			tx_msj_plan.setVisibility(View.GONE);
		}

		listPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				layoutInflater =(LayoutInflater)mView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				popupView = layoutInflater.inflate(R.layout.popup_plan, null);
				ListView list_popup=(ListView) popupView.findViewById(R.id.list_popup);

				plandetAdapter = new PlandetAdapter(getContext(),getPlanModel.Data.get(position));
				list_popup.setAdapter(plandetAdapter);

				//ListView list_popup = (ListView) mView.findViewById(R.id.list_popup);


				popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.WRAP_CONTENT,RadioGroup.LayoutParams.WRAP_CONTENT,false);

				btn_Cerrar = (Button)popupView.findViewById(R.id.id_cerrar);
				btn_Cerrar.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
					}});

				ibclose=(ImageButton) popupView.findViewById(R.id.ibclose);
				ibclose.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						popupWindow.dismiss();

					}
				});

				//popupWindow.showAsDropDown(btn_Abrir_Popup, 50, -400);

				popupWindow.showAtLocation(listPlan, Gravity.CENTER, 0, 0);






			}
		});

	}

	@Override
	public void successpost(String data,String Tipo) {

	}

	@Override
	public void error(String mensaje,String Tipo) {

	}


/*
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//check if popupwindow is open
		//Log.e(TAG, "Check if it runs through this section");
		if (popupWindow != null) {

			popupWindow.dismiss();
			popupWindow = null;
			//myWebView = null;

		}else{
			onBackPressed();
		}
	}
*/






}