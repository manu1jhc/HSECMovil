package com.pango.hsec.hsec.CuasiAccidente.Seguridad.IngresosSeguridad;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.CheckCausaAdapter;
import com.pango.hsec.hsec.model.CausalidadModel;
import com.pango.hsec.hsec.model.Maestro;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddCausalidad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddCausalidad extends Fragment implements IActivity {
    View mView;
    String Causafinal = "", CondInicial = "";
    ArrayList<Maestro> causalidadData;
    ArrayList<CausalidadModel> listaSelCausalidad = new ArrayList<>();

    ImageButton add_causalidad;
    RecyclerView rec_listCausalidad;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;


    public FragmentAddCausalidad() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static FragmentAddCausalidad newInstance(String sampleText) {
        FragmentAddCausalidad fragment = new FragmentAddCausalidad();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_causalidad, container, false);

        add_causalidad = mView.findViewById(R.id.add_causalidad);
        rec_listCausalidad = (RecyclerView) mView.findViewById(R.id.listCausalidad);
/*
        LinearLayoutManager horizontalManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rec_listCausalidad.setLayoutManager(horizontalManager1);
        checkCausaAdapter = new CheckCausaAdapter(getActivity(), listaSelCausalidad);
        rec_listCausalidad.setAdapter(checkCausaAdapter);
*/


        add_causalidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] pass = {false,false},passCausa={false};
                Integer[] itemSel = {0,0};
                ArrayList<Maestro> condiciondata;

                Button btn_Cerrar;
                Spinner sp_tipoCausa, sp_tipoCond;
                Button btn_agregar;
                RecyclerView rec_list_causacond;
                TextView titulo_cc;

                CheckCausaAdapter checkCausaAdapter;
                layoutInflater =(LayoutInflater)mView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_condiciones, null);
                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);

                popupWindow.showAtLocation(add_causalidad, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.setFocusable(true);
                //popupWindow.update();
                popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                popupWindow.setOutsideTouchable(true);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);

                titulo_cc =  popupView.findViewById(R.id.titulo_cc);
                sp_tipoCausa = popupView.findViewById(R.id.sp_tipoCausa);
                sp_tipoCond = popupView.findViewById(R.id.sp_tipoCond);
                btn_agregar = popupView.findViewById(R.id.btn_agregar);
                rec_list_causacond = popupView.findViewById(R.id.list_causacond);

                btn_Cerrar = popupView.findViewById(R.id.btn_cerrar);
                btn_Cerrar.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }});

                ArrayAdapter adapterTcausa = new ArrayAdapter(getActivity().getBaseContext(), R.layout.custom_spinner_item, GlobalVariables.TipoCausa);
                adapterTcausa.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
                sp_tipoCausa.setAdapter(adapterTcausa);
                sp_tipoCausa.setSelection(0);

                condiciondata=new ArrayList<>();
                Maestro ubica = (Maestro) ((Spinner) popupView.findViewById(R.id.sp_tipoCausa)).getSelectedItem();
                for (Maestro item : GlobalVariables.loadCondicion(ubica.CodTipo)) {
                    condiciondata.add(item);
                }
                ArrayAdapter adapterTcondicion = new ArrayAdapter(getActivity().getBaseContext(), R.layout.custom_spinner_item, condiciondata);
                adapterTcondicion.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
                sp_tipoCond.setAdapter(adapterTcondicion);
                sp_tipoCond.setSelection(0);
//                ArrayList<Maestro> val = GlobalVariables.Tcausalidad;


                causalidadData=new ArrayList<>();
                for (Maestro item : GlobalVariables.loadCausalidades("co01")) {
                    causalidadData.add(item);
                }
                titulo_cc.setText("Condiciones - Total: " + causalidadData.size());

                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rec_list_causacond.setLayoutManager(horizontalManager);
                checkCausaAdapter = new CheckCausaAdapter(getActivity(), causalidadData);
                rec_list_causacond.setAdapter(checkCausaAdapter);




                sp_tipoCausa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if(itemSel[0]!=position) {
                            itemSel[0]=position;
                            Maestro ubica = (Maestro) ((Spinner) popupView.findViewById(R.id.sp_tipoCausa)).getSelectedItem();
                            Causafinal = ubica.CodTipo;
                            condiciondata.clear();
                            for (Maestro item : GlobalVariables.loadCondicion(Causafinal)) {
                                condiciondata.add(item);
                            }
                            String Tipos[]=condiciondata.get(0).CodTipo.split("\\.");
                            CondInicial = Tipos[1];
                            adapterTcondicion.notifyDataSetChanged();
                            sp_tipoCond.setSelection(0);

                            causalidadData=new ArrayList<>();
                            for (Maestro item : GlobalVariables.loadCausalidades(CondInicial)) {
                                causalidadData.add(item);
                            }
                            titulo_cc.setText("Condiciones - Total: " + causalidadData.size());
                            checkCausaAdapter.addAll(causalidadData);
                            checkCausaAdapter.notifyDataSetChanged();

//                            if(!pass[0]&&GlobalVariables.AddIncidenteSeg.Gerencia!=null)
//                            {
//                                passGer[0] =true;
//                                if(!StringUtils.isEmpty(GlobalVariables.AddIncidenteSeg.SuperInt))
//                                    spinner_superint.setSelection(GlobalVariables.indexOf(superintdata,Gerenciafinal+"."+GlobalVariables.AddIncidenteSeg.SuperInt));
//                            }
//                            else {
//                                GlobalVariables.AddIncidenteSeg.Gerencia=Gerenciafinal;
//                                spinner_superint.setSelection(0);
//                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //gerencia="";
                    }
                });

                sp_tipoCond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        Maestro Tipo = (Maestro) ( (Spinner) popupView.findViewById(R.id.sp_tipoCond) ).getSelectedItem();
                        if(!StringUtils.isEmpty(Tipo.CodTipo)){
                           // GlobalVariables.AddInspeccion.SuperInt=Tipo.CodTipo.split("\\.")[1];
                            String Tipos[]=Tipo.CodTipo.split("\\.");
                            CondInicial = Tipos[1];
                            causalidadData=new ArrayList<>();
                            for (Maestro item : GlobalVariables.loadCausalidades(CondInicial)) {
                                causalidadData.add(item);
                            }
                            titulo_cc.setText("Condiciones - Total: " + causalidadData.size());
                            checkCausaAdapter.addAll(causalidadData);
                            checkCausaAdapter.notifyDataSetChanged();

                        }



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // superint="";
                    }
                });


                btn_agregar.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String etapa = et_etapa.getText().toString();
//                        String description = et_description.getText().toString();
//
//                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(et_description.getWindowToken(), 0);
//                        //txt_description
//                        if (etapa.equals("0") || description.equals("")){
//                            Toast.makeText(getActivity(), "Los campos no pueden estar vacios" , Toast.LENGTH_LONG).show();
//
//                        }else {
//                            obsComentAdapter.add(new SubDetalleModel("PETO", etapa, description));
//                            obsComentAdapter.notifyDataSetChanged();
//                            popupWindow.dismiss();
//                        }

                    }
                });





            }
        });




        return mView;
    }

    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}