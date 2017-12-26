package com.pango.hsec.hsec;

import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.View;

import com.pango.hsec.hsec.model.Area;
import com.pango.hsec.hsec.model.PublicacionModel;
import com.pango.hsec.hsec.model.Tipo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by Andre on 12/12/2017.
 */

public class GlobalVariables {

    //public static String Urlbase2 = "entrada/getpaginated/";
    public  static int con_status=0;
    public static String token_auth="";

    public static boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public static Stack<Fragment> fragmentStack= new Stack<Fragment>();

    public static void apilarFrag(Fragment fragment){
        if (GlobalVariables.fragmentStack.size()<=1) {
            GlobalVariables.fragmentStack.push(fragment);
        }else{
            GlobalVariables.fragmentStack.pop();
            GlobalVariables.fragmentStack.push(fragment);
        }
    }



    public static View view_fragment;
    public static boolean isFragment=false;
    public static int contpublic=1;
    public static int num_items=5;
    public static String Url_base="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/";

    public static boolean flagUpSc=false;
   // public static boolean FDown=false;
   public static int count=1;
    public static boolean flag_up_toast=false;

    public static ArrayList<Area> Area_usuario= new ArrayList<>();
    public static ArrayList<Tipo> tipo= new ArrayList<>();

    public static ArrayList<PublicacionModel> listaGlobal = new  ArrayList<PublicacionModel>();
    public static String  json_user="";

}
