package com.pango.hsec.hsec.Noticias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.NoticiasModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentDetalle extends Fragment implements IActivity {

    private static View mView;
    String codNot="";
    String jsonNoticia="";
    String url="";
    TextView not_titulo,txfecha;
    WebView notDetalle;


    // TODO: Rename and change types and number of parameters
    public static FragmentDetalle newInstance(String sampleText) {

        FragmentDetalle f = new FragmentDetalle();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_detalle, container, false);
        codNot=getArguments().getString("bString");
        GlobalVariables.view_fragment=mView;
        GlobalVariables.isFragment=true;

        url= GlobalVariables.Url_base+"Noticia/Get/"+codNot;

        if(jsonNoticia.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentDetalle.this);
            obj.execute("");
        }else {
            success(jsonNoticia,"");
        }



        return mView;
    }




    @Override
    public void success(String data, String Tipo) {
        DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        not_titulo=(TextView) mView.findViewById(R.id.not_titulo);
        txfecha=(TextView) mView.findViewById(R.id.txfecha);
        notDetalle=(WebView) mView.findViewById(R.id.Visor);
        jsonNoticia =data;
        Gson gson = new Gson();
        NoticiasModel getNoticiaModel = gson.fromJson(data, NoticiasModel.class);

        Date temp= null;
        try {
            temp= formatoInicial.parse(getNoticiaModel.Fecha);
        } catch (Exception e) {
            e.printStackTrace();
        }

        not_titulo.setText(getNoticiaModel.Titulo);
        txfecha.setText(formatoRender.format(temp));

        notDetalle.setWebViewClient(new MyWebViewClient());

        notDetalle.loadDataWithBaseURL("",getNoticiaModel.Descripcion , "text/html", "UTF-8", "");
        WebSettings settings=notDetalle.getSettings();
        settings.setJavaScriptEnabled(true);




    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }



    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;

        }
    }

}
