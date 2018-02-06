package com.pango.hsec.hsec.Inspecciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Observaciones.FragmentComent;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ComentAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetComentModel;

import org.json.JSONException;
import org.json.JSONObject;


public class FragmentComentIns extends Fragment implements IActivity {
    private View mView;
    String codInsp;
    String url;
    ComentAdapter comentAdapter;
    String jsonComentario="";
    public FragmentComentIns() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentComentIns newInstance(String sampleText) {
        FragmentComentIns fragment = new FragmentComentIns();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;

    }

    ImageButton btn_send;
    EditText et_comentario;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_coment_ins, container, false);
        //GlobalVariables.count=1;
        GlobalVariables.view_fragment=mView;
        //GlobalVariables.isFragment=true;

        btn_send=(ImageButton) mView.findViewById(R.id.btn_send);
        et_comentario=(EditText) mView.findViewById(R.id.et_comentario);

        codInsp=getArguments().getString("bString");

        url= GlobalVariables.Url_base+"Comentario/getObs/"+codInsp;

        if(jsonComentario.isEmpty()) {
            GlobalVariables.istabs=true;

            final ActivityController obj = new ActivityController("get", url, FragmentComentIns.this);
            obj.execute("");
        }else {
            success(jsonComentario,"");
        }


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
                    jsonObject.accumulate("CodComentario",codInsp);
                    jsonObject.accumulate("Comentario",comentario);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                json += jsonObject.toString();

                url= GlobalVariables.Url_base+"Comentario/insert";
                GlobalVariables.isFragment=true;
                final ActivityController obj = new ActivityController("post", url, FragmentComentIns.this);
                obj.execute(json);



            }
        });

        return mView;

    }

    @Override
    public void success(String data, String Tipo) {
        jsonComentario=data;
        Gson gson = new Gson();
        GetComentModel getComentModel= gson.fromJson(data, GetComentModel.class);
        comentAdapter=new ComentAdapter(getContext(),getComentModel.Data);
        ListView listaComentarios = (ListView) mView.findViewById(R.id.list_coment);
        listaComentarios.setAdapter(comentAdapter);

        listaComentarios.setSelection(getComentModel.Data.size()-1);
    }

    @Override
    public void successpost(String data, String Tipo) {
        closeSoftKeyBoard();
        et_comentario.setText("");
        switch (data) {
            case "1":
                Toast.makeText(getContext(),"Comentario enviado",Toast.LENGTH_SHORT).show();
                GlobalVariables.count=5;
                GlobalVariables.isFragment=true;
                url= GlobalVariables.Url_base+"Comentario/getObs/"+codInsp;
                final ActivityController obj = new ActivityController("get", url, FragmentComentIns.this);
                obj.execute("");

                break;

            case "-1":
                Toast.makeText(getContext(),"Ocurrio un error al enviar su mensaje",Toast.LENGTH_SHORT).show();

                break;
            default:
                Toast.makeText(getContext(),"Error"+data,Toast.LENGTH_SHORT).show();		}
    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    public void closeSoftKeyBoard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

}
